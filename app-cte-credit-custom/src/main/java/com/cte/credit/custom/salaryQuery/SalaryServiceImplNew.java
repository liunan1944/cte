package com.cte.credit.custom.salaryQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cte.credit.api.Conts;
import com.cte.credit.api.dto.CRSCoreRequest;
import com.cte.credit.api.dto.CRSCoreResponse;
import com.cte.credit.api.enums.CRSStatusEnum;
import com.cte.credit.api.enums.custom.CustomServiceEnum;
import com.cte.credit.common.annotation.CustomClass;
import com.cte.credit.common.template.PropertyUtil;
import com.cte.credit.common.util.CardNoValidator;
import com.cte.credit.common.util.ExceptionUtil;
import com.cte.credit.common.util.StringUtil;
import com.cte.credit.custom.salaryQuery.dao.iface.ISalaryDaoService;
import com.cte.credit.custom.service.BaseCustomCoreService;
import com.cte.credit.custom.service.iface.ICustomCoreService;


/**
* @Title: 薪资水平查询产品
* @Description: 薪资水平查询产品
* @author nan.liu 
* @date 2018年4月8日 下午15:16:36 
* @version V1.0
*/ 
@Service
@CustomClass(bindingService=CustomServiceEnum.SalaryQueryServiceImpl)
public class SalaryServiceImplNew extends BaseCustomCoreService
		implements ICustomCoreService {
	private final Logger logger = LoggerFactory.getLogger(SalaryServiceImplNew.class);
	private final String ds_guozt_salary = "ds_guozt_salary";
	@Autowired
	PropertyUtil propertyEngine;
	@Autowired
	private ISalaryDaoService salaryService;
	@Override
	public CRSCoreResponse handler(String trade_id, CRSCoreRequest request) {
		String prefix = trade_id + " " + Conts.KEY_CUSTOM_HEADER; // 流水号标识
		logger.info("{} 定制产品服务处理开始,产品名称:{}", prefix,
				CustomServiceEnum.SalaryQueryServiceImpl.prod_name);//
		boolean doPrint = "1".equals(propertyEngine.readById("sys_public_logprint_switch"));
		CRSCoreResponse response = new CRSCoreResponse();
		try{			
			String name = request.getParams().get("name").toString();
			String cardNo = request.getParams().get("cardNo").toString();
			String validate = CardNoValidator.validate(cardNo);
			if (!StringUtil.isEmpty(validate)) {
				logger.info("{} 身份证格式错误 {}", prefix, validate);

				response.setRetcode(CRSStatusEnum.STATUS_FAILED_DS_JUXINLI_IDCARD_ERROR
						.getRet_sub_code());
				response.setRetmsg(CRSStatusEnum.STATUS_FAILED_DS_JUXINLI_IDCARD_ERROR
						.getRet_msg());
				return response;
			}
			logger.info("{} 开始请求薪资水平查询产品:{}", prefix,ds_guozt_salary);
			Map<String, Object> retdata = new TreeMap<String, Object>();
			if(salaryService.findTestUser(request.getAcct_id(), request.getProdId())){
				logger.info("{} 测试账号查询", prefix);
				List<Map<String, Object>> list = salaryService.findSalary(name, cardNo);
				if(list.size()>0){
					Map<String, Object> map = list.get(0);
					retdata.put("data", String.valueOf(map.get("DATA")));
					retdata.put("is_inc", "T");
				}else{
					retdata.put("data", "0");
					retdata.put("is_inc", "F");
				}
				response.setDs_tags(Conts.TAG_TST_SUCCESS);
				response.setIface_tags(Conts.TAG_TST_SUCCESS);
				response.setRetcode(Conts.KEY_STATUS_SUCCESS);
				response.setRetdata(retdata);
				response.setRetmsg("交易成功");
				return response;
			}
			
			Map<String,Object> params_in = new HashMap<String,Object>();
			params_in.put("name", name);
			params_in.put("cardNo", cardNo);
			retdata = salaryResorce(ds_guozt_salary,trade_id,params_in);
			if (!isSuccess(retdata)) {
				CRSStatusEnum retstatus = CRSStatusEnum.valueOf(retdata.get(
						Conts.KEY_RET_STATUS).toString());
				response.setRetcode(retstatus.getRet_sub_code());
				response.setRetmsg(String.valueOf(retdata.get(
						Conts.KEY_RET_MSG)));
			} else {
				response.setRetcode(Conts.KEY_STATUS_SUCCESS);
				response.setRetdata((Map<String, Object>) retdata.get("retdata"));
				response.setRetmsg("交易成功");
			}			
			response.setDs_tags(String.valueOf(retdata.get("rettag")));
			response.setIface_tags(String.valueOf(retdata.get("rettag")));
		} catch (Exception e) {
			logger.error("{} 系统处理时异常，异常信息:{}", prefix ,ExceptionUtil.getTrace(e));
			response.setRetcode(Conts.KEY_STATUS_FAILED);
			response.setRetmsg("对不起! 系统处理时异常，请牢记此次交易凭证号.");
		}
		if(doPrint)
			logger.info("{} 最终返回消息:\n{}", prefix ,JSONObject.toJSONString(response, true));
      return response;
	}
	@Override
	public Map<String, String> callback(String paramString,
			CRSCoreRequest paramCRSCoreRequest,
			CRSCoreResponse paramCRSCoreResponse) {
		// TODO Auto-generated method stub
		return null;
	}
	public Map<String,Object> salaryResorce(String ds_id,String trade_id,Map<String,Object> params_in) {
		// TODO Auto-generated method stub
		String prefix = trade_id + " " + Conts.KEY_CUSTOM_HEADER; // 流水号标识
		Map<String,Object> params_out = null;
		try {
			params_out = fetchByDataSource(trade_id, ds_id,CustomServiceEnum.SalaryQueryServiceImpl.prod_code, params_in);					
		} catch (Exception e) {
			logger.info("{} 调用{}异常...", prefix,ds_id);
			logger.error("{} 数据源处理时异常：{}",prefix,ExceptionUtil.getTrace(e));
		}
		return params_out;
	}
}
