package com.cte.credit.gw.action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.cte.credit.api.Conts;
import com.cte.credit.api.dto.CRSCoreRequest;
import com.cte.credit.api.dto.CRSCoreResponse;
import com.cte.credit.api.enums.CRSStatusEnum;
import com.cte.credit.common.log.prod.ProductLogUtil;
import com.cte.credit.common.template.PropertyUtil;
import com.cte.credit.common.util.ExceptionUtil;
import com.cte.credit.common.util.IPUtils;
import com.cte.credit.common.util.StringUtil;
import com.cte.credit.gw.action.crypto.WandaAES;
import com.cte.credit.gw.action.crypto.WandaCryptoAESException;
import com.cte.credit.gw.action.dto.ProductDto;
import com.cte.credit.gw.action.dto.Request;
import com.cte.credit.gw.dao.iface.IAccountService;
import com.cte.credit.gw.dto.Account;
import com.cte.credit.gw.quartz.init.AccountInitUtil;


@Controller
@RequestMapping(value = "/main")
public class CreditMainAction extends BaseServiceAction{
	private static Logger logger = LoggerFactory.getLogger(CreditMainAction.class);
	
	@Autowired
	ProductLogUtil prodLogEngine;
	@Autowired
	IAccountService acctEngine;
	@Autowired
	PropertyUtil propertyEngine;
	/**
	 * 认证产品请求
	 * */
	@RequestMapping(value = "service",method = {RequestMethod.POST,RequestMethod.OPTIONS})
	@ResponseBody
	public Map<String,Object> service(final HttpServletRequest request,@RequestBody final Request product)  {
		long startTime = System.currentTimeMillis();
		String trade_id = StringUtil.getRandomNo();
		String prefix = trade_id +" "+ Conts.KEY_SUPPORT_HEADER; //流水号标识
		logger.info("{} 产品处理器：收到请求",prefix);
		CRSCoreRequest req = null;
		CRSCoreResponse resp = null;
		String acct_id = "";
		Account account = null;
		Map<String,Object> output = new HashMap<String,Object>();
		try {
			acct_id = request.getHeader("X_CTE_ACCT_ID");
			logger.info("{} 获取账户信息为:{}",prefix,acct_id);
			account = AccountInitUtil.acctMatch(acct_id);//匹配账户信息
		}catch(Exception ex){
			logger.info("{} 匹配账户信息报错:{}",prefix,ex.getMessage());
			output.put(Conts.KEY_RET_CODE, CRSStatusEnum.STATUS_SYS_ACCT_FAILED.ret_sub_code);
			output.put(Conts.KEY_RET_MSG, CRSStatusEnum.STATUS_SYS_ACCT_FAILED.ret_msg);
			output.put(Conts.KEY_RET_DATA, null);
		}
		if(StringUtil.isEmpty(acct_id) || account==null){
			logger.info("{} 获取账户信息为空",prefix);
			output.put(Conts.KEY_RET_CODE, CRSStatusEnum.STATUS_SYS_ACCT_FAILED.ret_sub_code);
			output.put(Conts.KEY_RET_MSG, CRSStatusEnum.STATUS_SYS_ACCT_FAILED.ret_msg);
			output.put(Conts.KEY_RET_DATA, null);
		}else{
			try {		
				ProductDto productDto = encodeRepStr(trade_id,account,product.getRequest_body());				
				//IP白名单校验
				logger.info("{} 产品处理器：IP安全校验开始.",prefix);
				String remoteIp=IPUtils.getIp(request);
				logger.info("{} 产品处理器：远程IP请求地址:{}.",prefix,remoteIp);
				
				int acct_valid = acctNormal(account,productDto.getProd_id());
				if(acct_valid==-3) {
					logger.info("{} 获取权限校验失败:-3",prefix);
					output.put(Conts.KEY_RET_CODE, CRSStatusEnum.STATUS_SYS_ACCT_FAILED.ret_sub_code);
					output.put(Conts.KEY_RET_MSG, CRSStatusEnum.STATUS_SYS_ACCT_FAILED.ret_msg);
					output.put(Conts.KEY_RET_DATA, null);
				}else if(acct_valid==-1){
					logger.info("{} 获取权限校验失败:-1",prefix);
					output.put(Conts.KEY_RET_CODE, CRSStatusEnum.STATUS_SYS_PROD_FAILED.ret_sub_code);
					output.put(Conts.KEY_RET_MSG, CRSStatusEnum.STATUS_SYS_PROD_FAILED.ret_msg);
					output.put(Conts.KEY_RET_DATA, null);
				}else{
					boolean isTestUser = false;
					boolean testUserNormal = false;
					logger.info("{} 获取权限校验完成",prefix);
					if("1".equals(account.getIsfee())){//测试用户
						isTestUser = true;
						if(acctEngine.isTestUser(acct_id, productDto.getProd_id())){//测试用户,测试条数未用完
							logger.info("{} 测试用户,扣除测试条数:{}",prefix,acct_id);
							testUserNormal = true;
						}
					}
					if(isTestUser && !testUserNormal){//测试用户,测试条数已用完
						logger.info("{} 测试条数已用完",prefix);
						output.put(Conts.KEY_RET_CODE, CRSStatusEnum.STATUS_SYS_PROD_TEST_FAILED.ret_sub_code);
						output.put(Conts.KEY_RET_MSG, CRSStatusEnum.STATUS_SYS_PROD_TEST_FAILED.ret_msg);
						output.put(Conts.KEY_RET_DATA, null);
					}else{
						Map<String,Object> params = productDto.getReq_data();
						if(params == null || StringUtil.isEmpty(productDto.getRequest_sn())){
							output.put(Conts.KEY_RET_CODE, CRSStatusEnum.STATUS_SYS_APP_PARAM.ret_sub_code);
							output.put(Conts.KEY_RET_MSG, CRSStatusEnum.STATUS_SYS_APP_PARAM.ret_msg);
							output.put(Conts.KEY_RET_DATA, null);
						}else{
							if(acctEngine.isRepeatTrad(productDto.getRequest_sn(), 
									acct_id, productDto.getProd_id())){	
								logger.info("{} 请求交易号重复{} {}",prefix,productDto.getProd_id(),productDto.getRequest_sn());			
								output.put(Conts.KEY_RET_CODE, CRSStatusEnum.STATUS_SYS_APP_REQEUST_SN.ret_sub_code);
								output.put(Conts.KEY_RET_MSG, CRSStatusEnum.STATUS_SYS_APP_REQEUST_SN.ret_msg);
								output.put(Conts.KEY_RET_DATA, null);
							}else{
								req = new CRSCoreRequest();
								req.setRequst_sn(productDto.getRequest_sn());
								req.setApi_key(account.getApi_key());
								req.setIp_address(remoteIp);
								req.setOperid(Conts.SUPPORT_OPER_ID);
								req.setProdId(productDto.getProd_id());
								req.setVersion(Conts.WS_VERSION);
								req.setMac_address("UNKOWN");
								req.setAcct_id(acct_id);
								req.setParams(params);
						        req.setProduct_id(productDto.getProd_id());
						        prodLogEngine.writeReqLog(trade_id, req);
								resp = route2Next(trade_id,req);
								formatOutPut(prefix,resp,
										productDto.getRequest_sn(),output);
								prodLogEngine.writeRspLog(trade_id, 
										productDto.getProd_id(), resp, 
										new Date().getTime()-startTime);
							}						
						}
					}					
				}															
			} catch (DecoderException e) {
				logger.error("{} 产品处理器：解密异常01:{}",prefix,ExceptionUtil.getTrace(e));
				output.put(Conts.KEY_RET_CODE, CRSStatusEnum.STATUS_SYS_ACCT_KEY.ret_sub_code);
				output.put(Conts.KEY_RET_MSG, CRSStatusEnum.STATUS_SYS_ACCT_KEY.ret_msg);
				output.put(Conts.KEY_RET_DATA, null);
			}catch (WandaCryptoAESException e) {
				logger.error("{} 产品处理器：解密异常02:{}",prefix,ExceptionUtil.getTrace(e));
				output.put(Conts.KEY_RET_CODE, CRSStatusEnum.STATUS_SYS_ACCT_KEY.ret_sub_code);
				output.put(Conts.KEY_RET_MSG, CRSStatusEnum.STATUS_SYS_ACCT_KEY.ret_msg);
				output.put(Conts.KEY_RET_DATA, null);
			}catch (Exception e) {
				logger.error("{} 产品处理器：系统处理时异常03:{}",prefix,ExceptionUtil.getTrace(e));
				output.put(Conts.KEY_RET_CODE, CRSStatusEnum.STATUS_FAILED.ret_sub_code);
				output.put(Conts.KEY_RET_MSG, CRSStatusEnum.STATUS_FAILED.ret_msg);
				output.put(Conts.KEY_RET_DATA, null);
			}
		}
		logger.info("{} 产品处理器：交易结束,总计耗时：{} ms",new Object[]{prefix,new Date().getTime()-startTime});
		return output;
	}
	
	/**
	 * 认证产品权限测试
	 * */
	@RequestMapping(value = "test",method = {RequestMethod.POST,RequestMethod.OPTIONS})
	@ResponseBody
	public Map<String,Object> test(final HttpServletRequest request,
			@RequestBody final ProductDto product)  {
		long startTime = System.currentTimeMillis();
		String trade_id = StringUtil.getRandomNo();
		String prefix = trade_id +" "+ Conts.KEY_SUPPORT_HEADER; //流水号标识
		logger.info("{} test产品处理器：收到请求",prefix);
		boolean is_mock = "1".equals(
				propertyEngine.readById("sys_public_gw_test_mock"));//1走mock
		String acct_id = "";
		String api_key = "";
		Map<String,Object> output = new HashMap<String,Object>();
		if(is_mock){
			logger.info("{} test走mock",prefix);
			output.put("request_body", "saw123");
		}else{
			try {
				acct_id = request.getHeader("X_CTE_ACCT_ID");
				api_key = request.getHeader("X_CTE_API_KEY");
				logger.info("{} test获取账户信息为:{} {}",prefix,acct_id,api_key);
			}catch(Exception ex){
				logger.info("{} test匹配账户信息报错:{}",prefix,ExceptionUtil.getTrace(ex));
				output.put(Conts.KEY_RET_CODE, CRSStatusEnum.STATUS_SYS_ACCT_FAILED.ret_sub_code);
				output.put(Conts.KEY_RET_MSG, CRSStatusEnum.STATUS_SYS_ACCT_FAILED.ret_msg);
				output.put(Conts.KEY_RET_DATA, null);
			}
			if(StringUtil.isEmpty(acct_id) || api_key==null){
				logger.info("{} test获取账户信息为空",prefix);
				output.put(Conts.KEY_RET_CODE, CRSStatusEnum.STATUS_SYS_ACCT_FAILED.ret_sub_code);
				output.put(Conts.KEY_RET_MSG, CRSStatusEnum.STATUS_SYS_ACCT_FAILED.ret_msg);
				output.put(Conts.KEY_RET_DATA, null);
			}else{
				try{
					String req_sn = UUID.randomUUID().toString().replace("-", "");
					product.setAcct_id(acct_id);
					product.setReq_time(System.currentTimeMillis());
					product.setRequest_sn(req_sn);
					WandaAES wandaAES = new WandaAES(Hex.decodeHex(api_key.toCharArray()));
					String jsonString = JSON.toJSONString(product);
					byte[] encryptBytes = wandaAES.encrypt(jsonString);
			        //2.2 BASE64
			        String base64String = Base64.encodeBase64String(encryptBytes);
			        output.put("request_body", base64String);
				}catch(Exception ex){
					logger.info("{} test匹配账户信息报错:{}",prefix,ExceptionUtil.getTrace(ex));
					output.put(Conts.KEY_RET_CODE, CRSStatusEnum.STATUS_SYS_ACCT_FAILED.ret_sub_code);
					output.put(Conts.KEY_RET_MSG, CRSStatusEnum.STATUS_SYS_ACCT_FAILED.ret_msg);
					output.put(Conts.KEY_RET_DATA, null);
				}			
			}
		}		
		logger.info("{} test产品处理器：交易结束,总计耗时：{} ms",new Object[]{prefix,new Date().getTime()-startTime});
		return output;
	}
}
