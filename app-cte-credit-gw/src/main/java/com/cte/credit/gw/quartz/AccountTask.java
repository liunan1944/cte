package com.cte.credit.gw.quartz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.cte.credit.common.util.StringUtil;
import com.cte.credit.gw.dto.Account;
import com.cte.credit.gw.dto.ProdLimit;
import com.cte.credit.gw.quartz.init.AccountInitUtil;
import com.cte.credit.gw.quartz.init.BaseDataParser;


/**
 * 账户加载任务
 * */
@Service
public class AccountTask {
	private static Logger logger = LoggerFactory.getLogger(AccountTask.class);
	@Autowired
	private BaseDataParser accountService;
	public void exec() {
		String trade_id = StringUtil.getRandomNo();	
		logger.info("{} 初始化账户信息加载开始...",trade_id);
		List<Account> accountListTmp = new ArrayList<Account>();
		List<Map<String, Object>> account = accountService.queryAcctAll();
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
					logger.error("{} 初始化账户信息报错,报错账户信息{}",trade_id,JSON.toJSONString(map));
				}
				logger.info("{} 初始化账户产品列表权限信息开始加载...",trade_id);
				List<Map<String, Object>> prods = accountService.queryAcctProds(
						String.valueOf(map.get("ACCT_ID")));
				if(prods.size()>0){
					for(Map<String, Object> map1:prods){
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
						prod_tmp.setIsfee(map1.get("ISFEE")!=null?String.valueOf(map1.get("ISFEE")):"");
						AccountInitUtil.prodLimitList.add(prod_tmp);
					}
					logger.info("{} 初始化账户产品列表权限信息加载完成",trade_id);
				}
				accountListTmp.add(acct_tmp);
			}	
			
		}
		AccountInitUtil.accountList = accountListTmp;
		logger.info("{} 初始化账户信息加载完成",trade_id);
	}
}
