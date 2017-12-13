package com.cte.credit.ds.client.jixin;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cte.credit.api.Conts;
import com.cte.credit.api.dto.DataSource;
import com.cte.credit.api.enums.CRSStatusEnum;
import com.cte.credit.common.annotation.DataSourceClass;
import com.cte.credit.common.log.ds.DataSourceLogUtil;
import com.cte.credit.common.log.ds.vo.DataSourceLogVO;
import com.cte.credit.common.template.PropertyUtil;
import com.cte.credit.common.util.CardNoValidator;
import com.cte.credit.common.util.DESUtils;
import com.cte.credit.common.util.ExceptionUtil;
import com.cte.credit.common.util.HttpsHelper;
import com.cte.credit.common.util.JSONUtil;
import com.cte.credit.common.util.ParamUtil;
import com.cte.credit.common.util.StringUtil;
import com.cte.credit.ds.BaseDataSourceRequestor;
import com.cte.credit.ds.client.jixin.base.CommonBean;
import com.cte.credit.ds.client.jixin.base.TransUtil;
import com.cte.credit.ds.dao.domain.jixin.Jixin_bank_result;
import com.cte.credit.ds.dao.iface.jixin.IJixinBankService;
import com.cte.credit.ds.iface.IDataSourceRequestor;

@DataSourceClass(bindingDataSourceId="ds_jixin_bank3")
public class JiXin106Requestor extends BaseDataSourceRequestor
implements IDataSourceRequestor {
	private final  Logger logger = LoggerFactory.getLogger(JiXin106Requestor.class);
	@Autowired
	private IJixinBankService jixinService;
	@Autowired
	PropertyUtil propertyEngine;
	@Autowired
	DataSourceLogUtil dataLogEngine;
	@Autowired
	private DESUtils desService;
	@Override
	public Map<String, Object> request(String trade_id, DataSource ds){
		final String prefix = trade_id + " " + Conts.KEY_SYS_AGENT_HEADER;
		logger.info("{}吉信106三要素数据源查询开始...", prefix);
		String des_key = propertyEngine.readById("sys_public_des_key");//加密key
		boolean jixin_106mock = "1".equals(propertyEngine.readById("sys_ds_jixin_106mock"));//1走mock接口
		boolean doPrint = "1".equals(propertyEngine.readById("sys_public_logprint_switch"));
		String url = propertyEngine.readById("sys_ds_jixin_url");
		String merchkey = propertyEngine.readById("sys_ds_jixin_merchkey");
		String merchno = propertyEngine.readById("sys_ds_jixin_merchno");
		String transcode = propertyEngine.readById("sys_ds_jixin_transcode");
		String version = propertyEngine.readById("sys_ds_jixin_version");
		int timeout = Integer.valueOf(propertyEngine.readById("sys_public_env_timeout"));
		DataSourceLogVO logObj = new DataSourceLogVO();
		Map<String, Object> reqparam = new HashMap<String, Object>();
		logObj.setReq_time(new Timestamp(System.currentTimeMillis()));//log请求时间
		logObj.setDs_id(ds.getId());
		logObj.setIncache("0");
		TreeMap<String, Object> retdata = new TreeMap<String, Object>();
		Map<String, Object> rets = new HashMap<String, Object>();
		String resource_tag = Conts.TAG_SYS_ERROR;
		String enCardNo = "";
		String enCardId = "";
		try{	
			String name = ParamUtil.findValue(ds.getParams_in(), paramIds[0]).toString();   //姓名 
			String cardNo = ParamUtil.findValue(ds.getParams_in(), paramIds[1]).toString(); //身份证号码
			String cardId = ParamUtil.findValue(ds.getParams_in(), paramIds[2]).toString(); //银行卡号
			enCardNo = desService.encode(des_key,cardNo);
			enCardId = desService.encode(des_key,cardId);
			reqparam.put("name", name);
			reqparam.put("cardNo", enCardNo);
			reqparam.put("cardId", enCardId);
			logObj.setReq_url(url);
			logObj.setState_code(Conts.TRADE_STATE_FAIL);
			logObj.setState_msg("处理失败");
			if(StringUtils.isNotEmpty(CardNoValidator.validate(cardNo))){
				logger.warn("{}入参格式不符合要求!", prefix);
				logObj.setIncache("1");
				logObj.setState_msg("身份证号码不符合规范");
				rets.clear();
				rets.put(Conts.KEY_RET_STATUS, CRSStatusEnum.STATUS_FAILED_DS_JUXINLI_IDCARD_ERROR);
				rets.put(Conts.KEY_RET_MSG, "您输入的为无效身份证号码，请核对后重新输入!");
				rets.put(Conts.KEY_RET_TAG, resource_tag);
				return rets;
			}else{
				logObj.setState_msg("处理成功");
				retdata.put("name", name);
				retdata.put("cardNo", cardNo);
				retdata.put("cardId", cardId);
				if(jixin_106mock){
					logger.warn("{} 吉信106走mock!", prefix);
					rets.put(Conts.KEY_RET_TAG, resource_tag);
					rets.put(Conts.KEY_RET_DATA, retdata);
					rets.put(Conts.KEY_RET_STATUS, CRSStatusEnum.STATUS_SUCCESS);
					rets.put(Conts.KEY_RET_MSG, "采集成功!");
				}else{
					Jixin_bank_result jixin = new Jixin_bank_result();
					CommonBean trans = new CommonBean();
					trans.setTranscode(transcode);
					trans.setVersion(version);
					trans.setOrdersn(trade_id);
					trans.setDsorderid(System.currentTimeMillis() + "");
					//使用相应的商户号即可
					trans.setMerchno(merchno);
					trans.setUsername(name);
					trans.setBankcard(cardId);
					trans.setIdtype("01");
					trans.setIdcard(cardNo);
					
					TransUtil tu = new TransUtil();
					byte[] reponse = tu.packet(trans, merchkey);
					//URL换成相应的环境  即可
					logger.info("{} 吉信106三要素http请求开始...", prefix );
					String response = HttpsHelper.httpSend(new String(reponse),url,timeout);
					logger.info("{} 吉信106三要素http请求结束", prefix,response );
					if(doPrint)
						logger.info("{} 吉信106三要素返回信息:{}", prefix,response);
					if(StringUtil.isEmpty(response)){
						resource_tag = Conts.TAG_TST_FAIL;
						rets.put(Conts.KEY_RET_STATUS, CRSStatusEnum.STATUS_FAILED_DS_ZT_BANKCARD_AUTHEN_EXCEPTION);
						rets.put(Conts.KEY_RET_MSG, "银行卡鉴权失败!");
						rets.put(Conts.KEY_RET_TAG, resource_tag);
					}else{
						JSONObject jixinRsp = (JSONObject) JSONObject.fromObject(response);
						jixin = (Jixin_bank_result) JSONUtil.convertJson2Object(
								jixinRsp, Jixin_bank_result.class);
						jixin.setCardno(enCardNo);
						jixin.setTrade_id(trade_id);
						jixin.setName(name);
						jixinService.batchSave(prefix,jixin);
						logObj.setState_msg(jixin.getErrtext());
						logObj.setState_code(Conts.TRADE_STATE_SUCC);
						
						boolean is_fail = false;
						if("0000".equals(jixin.getReturncode())){
							resource_tag = Conts.TAG_MATCH;
							retdata.put("respCode", "00");
							retdata.put("respDesc", "认证一致");
						}else if("0034".equals(jixin.getReturncode()) ||
								"0001".equals(jixin.getReturncode())){
							resource_tag = Conts.TAG_UNMATCH;
							retdata.put("respCode", "01");
							retdata.put("respDesc", "认证不一致");
							retdata.put("respDetail", jixin.getErrtext());
						}else if("0006".equals(jixin.getReturncode())){
							retdata.put("respCode", "02");
							retdata.put("respDesc", "不支持验证");
							retdata.put("respDetail", jixin.getErrtext());
							resource_tag = Conts.TAG_UNSUPPORT;
						}else{
							is_fail = true;							
						}
						if(is_fail){
							resource_tag = Conts.TAG_TST_FAIL;
							rets.put(Conts.KEY_RET_STATUS, 
									CRSStatusEnum.STATUS_FAILED_DS_ZT_BANKCARD_AUTHEN_EXCEPTION);
							rets.put(Conts.KEY_RET_MSG, jixin.getErrtext());
							rets.put(Conts.KEY_RET_TAG, resource_tag);
						}else{
							rets.put(Conts.KEY_RET_TAG, resource_tag);
							rets.put(Conts.KEY_RET_DATA, retdata);
							rets.put(Conts.KEY_RET_STATUS, CRSStatusEnum.STATUS_SUCCESS);
							rets.put(Conts.KEY_RET_MSG, "采集成功!");
						}						
					}						
				}				
			}						
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("{} 数据源处理时异常：{}",prefix,ExceptionUtil.getTrace(ex));
			resource_tag = Conts.TAG_SYS_ERROR;
			rets.put(Conts.KEY_RET_STATUS, CRSStatusEnum.STATUS_FAILED_SYS_DS_EXCEPTION);
			rets.put(Conts.KEY_RET_MSG, "数据源处理时异常! 详细信息:"+ex.getMessage());
			rets.put(Conts.KEY_RET_TAG, resource_tag);
		}finally {
			logObj.setRsp_time(new Timestamp(System.currentTimeMillis()));
			logObj.setTag(resource_tag);
			dataLogEngine.writeDsLog(trade_id, logObj);
			dataLogEngine.writeDsReqLog(trade_id,ds.getId(), reqparam, logObj);
		}
		return rets;
	}
}
