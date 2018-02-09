package com.cte.credit.ds.client.wandaEdu;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
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
import com.cte.credit.common.util.ParamUtil;
import com.cte.credit.common.util.StringUtil;
import com.cte.credit.ds.dao.iface.edu.IEduCheckService;
import com.cte.credit.ds.iface.IDataSourceRequestor;
import com.cte.credit.ds.utils.dto.ProductDto;

@DataSourceClass(bindingDataSourceId="ds_wanda_edu")
public class WandaEduRequestor extends BaseWandaSourceRequestor
implements IDataSourceRequestor {
	private final  Logger logger = LoggerFactory.getLogger(WandaEduRequestor.class);
	@Autowired
	PropertyUtil propertyEngine;
	@Autowired
	DataSourceLogUtil dataLogEngine;
	@Autowired
	private DESUtils desService;
	@Autowired
	private IEduCheckService eduService;
	@Override
	public Map<String, Object> request(String trade_id, DataSource ds){
		final String prefix = trade_id + " " + Conts.KEY_SYS_AGENT_HEADER;
		logger.info("{}万达学历数据源查询开始...", prefix);
		String des_key = propertyEngine.readById("sys_public_des_key");//加密key
		boolean jixin_106mock = "1".equals(propertyEngine.readById("sys_ds_jixin_106mock"));//1走mock接口
		boolean doPrint = "1".equals(propertyEngine.readById("sys_public_logprint_switch"));
		String url = propertyEngine.readById("sys_ds_wanda_url");
		String account = propertyEngine.readById("sys_ds_wanda_account");
		String key = propertyEngine.readById("sys_ds_wanda_key");
		String prod040 = propertyEngine.readById("sys_ds_wanda_prod040");
		int timeout = Integer.valueOf(propertyEngine.readById("sys_public_env_timeout"));
		
		DataSourceLogVO logObj = new DataSourceLogVO();
		Map<String, Object> reqparam = new HashMap<String, Object>();
		logObj.setReq_time(new Timestamp(System.currentTimeMillis()));//log请求时间
		logObj.setDs_id(ds.getId());
		logObj.setIncache("0");
		TreeMap<String, Object> retdata = new TreeMap<String, Object>();
		Map<String, Object> rets = new TreeMap<String, Object>();
		String resource_tag = Conts.TAG_SYS_ERROR;
		String enCardNo = "";
		try{	
			String name = ParamUtil.findValue(ds.getParams_in(), paramIds[0]).toString();   //姓名 
			String cardNo = ParamUtil.findValue(ds.getParams_in(), paramIds[1]).toString(); //身份证号码
			enCardNo = desService.encode(des_key,cardNo);
			reqparam.put("name", name);
			reqparam.put("cardNo", enCardNo);
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
				if(jixin_106mock){
					logger.warn("{} 学历走mock!", prefix);
					resource_tag = Conts.TAG_MATCH;
					rets.put(Conts.KEY_RET_TAG, resource_tag);
					rets.put(Conts.KEY_RET_DATA, retdata);
					rets.put(Conts.KEY_RET_STATUS, CRSStatusEnum.STATUS_SUCCESS);
					rets.put(Conts.KEY_RET_MSG, "采集成功!");
				}else{
					logger.info("{}万达学历数据源-入参包装...", prefix);
					String req_sn = UUID.randomUUID().toString().replace("-", "");
					Map<String, Object> productDetailParam = new HashMap<String, Object>();
					//产品入参
					productDetailParam.put("name", name);
					productDetailParam.put("cardNo", cardNo);
					ProductDto productDto = new ProductDto();
					productDto.setAcct_id(account);
					//产品编号
					productDto.setInf_id(prod040);
					productDto.setProd_id(prod040);
					productDto.setReq_time(System.currentTimeMillis());
					productDto.setRequest_sn(req_sn); //请保证这个请求交易号唯一
					productDto.setReq_data(productDetailParam);
					String rsp040 = WandaSend(trade_id,productDto,url,key,timeout);
					if(doPrint)
						logger.info("{} 万达调用返回信息：{}",prefix,rsp040);
					if(StringUtil.isEmpty(rsp040)){
						logger.info("{}万达学历数据源返回信息为空", prefix);
						resource_tag = Conts.TAG_TST_FAIL;
						rets.put(Conts.KEY_RET_STATUS, CRSStatusEnum.STATUS_FAILED_DS_EDU_EXCEPTION);
						rets.put(Conts.KEY_RET_MSG, "学历查询返回失败");
						rets.put(Conts.KEY_RET_TAG, resource_tag);
						return rets;
					}
					JSONObject json = JSONObject.parseObject(rsp040);
					if(json.get("response_sn")==null){
						resource_tag = Conts.TAG_TST_FAIL;
						rets.put(Conts.KEY_RET_STATUS, CRSStatusEnum.STATUS_FAILED_DS_EDU_EXCEPTION);
						rets.put(Conts.KEY_RET_MSG, "学历查询返回失败");
						rets.put(Conts.KEY_RET_TAG, resource_tag);
						return rets;
					}
					logObj.setIncache("0");
					logObj.setBiz_code1(String.valueOf(json.get("response_sn")));
					logObj.setState_code(Conts.TRADE_STATE_SUCC);
					eduService.batchSave(trade_id, rsp040, req_sn,
							String.valueOf(json.get("response_sn")));
					if("000000".equals(String.valueOf(json.get("retcode")))){
						logger.info("{}万达学历数据源查询信息成功", prefix);
						JSONObject ret_data = JSONObject.parseObject(String.valueOf(json.get("retdata")));
						if("0".equals(String.valueOf(ret_data.get("edu_result")))){//查得学历
							JSONObject college = JSONObject.parseObject(String.valueOf(ret_data.get("college")));
							JSONObject degree = JSONObject.parseObject(String.valueOf(ret_data.get("degree")));
							JSONObject personBase = JSONObject.parseObject(String.valueOf(ret_data.get("personBase")));
							retdata.put("is_edu", "0");	
							retdata.put("degree", degree.get("degree")!=null?degree.get("degree"):"");
							retdata.put("college", college.get("college")!=null?college.get("college"):"");
							retdata.put("graduateTime", degree.get("graduateTime")!=null?degree.get("graduateTime"):"");
							retdata.put("graduateYears", personBase.get("graduateYears")!=null?personBase.get("graduateYears"):"");
							retdata.put("studyStyle", degree.get("studyStyle")!=null?degree.get("studyStyle"):"");
							retdata.put("studyType", degree.get("studyType")!=null?degree.get("studyType"):"");
							retdata.put("studyResult", degree.get("studyResult")!=null?degree.get("studyResult"):"");							
						}else{
							logger.info("{}万达学历数据源查询未查得学历", prefix);
							retdata.put("is_edu", "1");
							retdata.put("degree", "");
							retdata.put("college", "");
							retdata.put("graduateTime", "");
							retdata.put("graduateYears", "");
							retdata.put("studyStyle", "");
							retdata.put("studyType", "");
							retdata.put("studyResult", "");						
						}	
						resource_tag = Conts.TAG_TST_SUCCESS;	
						rets.put(Conts.KEY_RET_TAG, resource_tag);
						rets.put(Conts.KEY_RET_DATA, retdata);
						rets.put(Conts.KEY_RET_STATUS, CRSStatusEnum.STATUS_SUCCESS);
						rets.put(Conts.KEY_RET_MSG, "采集成功!");
					}else{
						logger.info("{}万达学历数据源查询信息失败", prefix);
						resource_tag = Conts.TAG_TST_FAIL;
						rets.put(Conts.KEY_RET_STATUS, CRSStatusEnum.STATUS_FAILED_DS_EDU_EXCEPTION);
						rets.put(Conts.KEY_RET_MSG, "学历查询返回失败");
						rets.put(Conts.KEY_RET_TAG, resource_tag);
						return rets;
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
			resource_tag = String.valueOf(rets.get(Conts.KEY_RET_TAG));
			logObj.setRsp_time(new Timestamp(System.currentTimeMillis()));
			logObj.setTag(resource_tag);
			dataLogEngine.writeDsLog(trade_id, logObj);
			dataLogEngine.writeDsReqLog(trade_id,ds.getId(), reqparam, logObj);
		}
		return rets;
	}
}
