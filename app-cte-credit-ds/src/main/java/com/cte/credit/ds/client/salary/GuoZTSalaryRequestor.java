package com.cte.credit.ds.client.salary;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

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
import com.cte.credit.common.util.JSONUtil;
import com.cte.credit.common.util.ParamUtil;
import com.cte.credit.ds.BaseDataSourceRequestor;
import com.cte.credit.ds.dao.domain.salary.GuoZT_salary_result;
import com.cte.credit.ds.dao.iface.guozt.IGuoZTSalaryService;
import com.cte.credit.ds.iface.IDataSourceRequestor;

import net.sf.json.JSONObject;

@DataSourceClass(bindingDataSourceId="ds_guozt_salary")
public class GuoZTSalaryRequestor extends BaseDataSourceRequestor
implements IDataSourceRequestor {
	private final  Logger logger = LoggerFactory.getLogger(GuoZTSalaryRequestor.class);
	@Autowired
	PropertyUtil propertyEngine;
	@Autowired
	DataSourceLogUtil dataLogEngine;
	@Autowired
	private DESUtils desService;
	@Autowired
	private IGuoZTSalaryService guoztService;
	@Override
	public Map<String, Object> request(String trade_id, DataSource ds){
		final String prefix = trade_id + " " + Conts.KEY_SYS_AGENT_HEADER;
		logger.info("{}国政通工资水平数据源查询开始...", prefix);
		String des_key = propertyEngine.readById("sys_public_des_key");//加密key
		boolean jixin_106mock = "1".equals(propertyEngine.readById("sys_ds_jixin_106mock"));//1走mock接口
		boolean doPrint = "1".equals(propertyEngine.readById("sys_public_logprint_switch"));
		String url = propertyEngine.readById("sys_ds_guozt_url");
		String secret = propertyEngine.readById("sys_ds_guozt_secret");
		String key = propertyEngine.readById("sys_ds_guozt_key");
		String host = propertyEngine.readById("sys_ds_guozt_host");
		int timeout = Integer.valueOf(propertyEngine.readById("sys_public_env_timeout"));
		logger.warn("{}加载系统变量值:{}", prefix,url);
		DataSourceLogVO logObj = new DataSourceLogVO();
		Map<String, Object> reqparam = new HashMap<String, Object>();
		logObj.setReq_time(new Timestamp(System.currentTimeMillis()));//log请求时间
		logObj.setDs_id(ds.getId());
		logObj.setIncache("0");
		Map<String, Object> retdata = new LinkedHashMap<String, Object>();
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
					logger.warn("{} 工资水平数据源走mock!", prefix);
					resource_tag = Conts.TAG_MATCH;
					rets.put(Conts.KEY_RET_TAG, resource_tag);
					rets.put(Conts.KEY_RET_DATA, retdata);
					rets.put(Conts.KEY_RET_STATUS, CRSStatusEnum.STATUS_SUCCESS);
					rets.put(Conts.KEY_RET_MSG, "采集成功!");
				}else{
					logger.warn("{} 工资水平数据源入参包装开始...", prefix);
					String token = JwtToken.getencodedJWT(key, secret);
			        //json
			        String str = "{\"name\":" + "\"" + name + "\"" + ", \"idcard\":" + "\"" + cardNo + "\"}";
			        //加密参数
			        String encryptSecret = secret.substring(0,16);
			        String param = "{\"params\":\"" + AesEncrypt.encrypt(encryptSecret, str,
			        		encryptSecret.getBytes()) + "\"}";
			        HttpClient http = new HttpClient();
			        logger.warn("{} 工资水平数据源请求开始...", prefix);
			        String result = http.httpRequest(url, param, "POST", host, "Bearer " +token, 
			        		"application/json; charset=utf-8",timeout);
			        logger.warn("{} 工资水平数据源请求成功,返回结果:{}", prefix,result);
			        JSONObject salaryRsp = (JSONObject) JSONObject.fromObject(result);
			        GuoZT_salary_result salary = (GuoZT_salary_result) JSONUtil.convertJson2Object(
			        		salaryRsp, GuoZT_salary_result.class);
			        salary.setTrade_id(trade_id);
			        salary.setName(name);
			        salary.setCardno(enCardNo);
			        if("200".equals(salary.getCode())){
			        	logger.warn("{} 工资水平数据源交易成功", prefix);
			        	if("0".equals(salary.getData())){
			        		retdata.put("is_inc", "F");
			        		resource_tag = Conts.TAG_STATUS_UNFOUND;
			        	}else{
			        		retdata.put("is_inc", "T");
			        		resource_tag = Conts.TAG_TST_SUCCESS;
			        	}
			        	retdata.put("data", salary.getData());
			        	rets.put(Conts.KEY_RET_TAG, resource_tag);
						rets.put(Conts.KEY_RET_DATA, retdata);
						rets.put(Conts.KEY_RET_STATUS, CRSStatusEnum.STATUS_SUCCESS);
						rets.put(Conts.KEY_RET_MSG, "采集成功!");
			        }else{
			        	logger.warn("{} 工资水平数据源查询失败", prefix);
			        	resource_tag = Conts.TAG_TST_FAIL;
						rets.put(Conts.KEY_RET_STATUS, CRSStatusEnum.STATUS_FAILED);
						rets.put(Conts.KEY_RET_MSG, "查询失败");
						rets.put(Conts.KEY_RET_TAG, resource_tag);
			        }
			        guoztService.batchSave(prefix, salary);
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
