package com.cte.credit.gw.quartz.init;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cte.credit.common.template.PropertyUtil;
import com.cte.credit.gw.dto.Account;
import com.cte.credit.gw.dto.ProdLimit;


/**
 * 应用启动时加载账户、调用连接、日志打印等信息
 * */
@Component
public class AccountInitUtil {
	private static Logger logger = LoggerFactory.getLogger(AccountInitUtil.class);
	public static List<Account> accountList = new ArrayList<Account>();
	public static List<ProdLimit> prodLimitList = new ArrayList<ProdLimit>();
	public final static String model_id = "0753513de038453c820cf6869w3f7d3e";
	public static String model_property_owner_id = "";
	
	private static AccountInitUtil baseAcctUtils;
	@Autowired
	private BaseDataParser accountService;
	public static void initAccount(){
		logger.info("初始化账户信息加载开始...");
		List<Map<String, Object>> account = baseAcctUtils.accountService.queryAcctAll();
		for(Map<String, Object> map:account){
			if(map.get("ACCT_ID")!=null){
				Account acct_tmp = new Account();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				acct_tmp.setAcct_id(map.get("ACCT_ID")!=null?String.valueOf(map.get("ACCT_ID")):"");
				acct_tmp.setApi_key(map.get("API_KEY")!=null?String.valueOf(map.get("API_KEY")):"");
				acct_tmp.setBalance(map.get("BALANCE")!=null?
						Double.valueOf(String.valueOf(map.get("BALANCE"))):0);
				acct_tmp.setIsfee(map.get("ISFEE")!=null?String.valueOf(map.get("ISFEE")):"");
				acct_tmp.setStatus(map.get("STATUS")!=null?String.valueOf(map.get("STATUS")):"");
				try {
					acct_tmp.setEnddate(map.get("ENDDATE")!=null?
							sdf.parse(String.valueOf(map.get("ENDDATE"))):(new Date()));
				} catch (ParseException e) {
					logger.error("初始化账户信息报错,报错账户信息{}",JSON.toJSONString(map));
				}
				logger.info("初始化账户产品列表权限信息开始加载...");
				List<Map<String, Object>> prods = baseAcctUtils.accountService.queryAcctProds(
						String.valueOf(map.get("ACCT_ID")));
				if(prods.size()>0){
					for(Map<String, Object> map1:prods){
						logger.info("初始化账户产品列表信息01:{}",JSON.toJSONString(map1));
						ProdLimit prod_tmp = new ProdLimit();
						prod_tmp.setAcct_id(map1.get("ACCT_ID")!=null?String.valueOf(map1.get("ACCT_ID")):"");
						prod_tmp.setPay_tags(map1.get("PAY_TAGS")!=null?String.valueOf(map1.get("PAY_TAGS")):"");
						prod_tmp.setProd_limit(map1.get("PROD_LIMIT")!=null?
								String.valueOf(map1.get("PROD_LIMIT")):"");
						prod_tmp.setPrice(map1.get("PRICE")!=null?
						Double.valueOf(String.valueOf(map1.get("PRICE"))):0);
						prod_tmp.setTest_num(map1.get("TEST_NUM")!=null?
								Integer.valueOf(String.valueOf(map1.get("TEST_NUM"))):0);
						prod_tmp.setStatus(map1.get("STATUS")!=null?String.valueOf(map1.get("STATUS")):"");
						logger.info("初始化账户产品列表信息02:{}",JSON.toJSONString(prod_tmp));
						prodLimitList.add(prod_tmp);
					}
					logger.info("初始化账户产品列表权限信息加载完成");
				}
				accountList.add(acct_tmp);
			}					
		}
		
		logger.info("初始化账户信息加载完成");
		logger.info("初始化系统变量信息加载开始...");
		List<Map<String, Object>> property_list = baseAcctUtils.accountService.queryProperties(model_id);
		Map<String,String> property_map = new HashMap<String,String>();
		for(Map<String, Object> map1:property_list){
			property_map.put(String.valueOf(map1.get("KEY_CODE")), String.valueOf(map1.get("KEY_VALUE")));
		}
		PropertyUtil.properties=property_map;
		model_property_owner_id = baseAcctUtils.accountService.queryOwnerid(model_id);
		logger.info("初始化系统变量信息加载完成");
	}
	/**
	 * 获取账户信息
	 * */
	public static synchronized Account acctMatch(String acct_id) {
		Account match = null;
		for (Account tmp : accountList) {
			if (acct_id.equals(tmp.getAcct_id())) {
				match = tmp;
				break;
			}
		}
		return match;
	}
	public void setAccountService(BaseDataParser accountService) {
		this.accountService = accountService;
	}

	@PostConstruct
	public void init() {
		baseAcctUtils = this;
		baseAcctUtils.accountService = this.accountService;
	}
}
