package com.cte.credit.gw.quartz;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cte.credit.common.counter.GlobalCounter;
import com.cte.credit.common.template.PropertyUtil;
import com.cte.credit.common.util.SendEmail;
import com.cte.credit.common.util.StringUtil;
import com.cte.credit.gw.quartz.init.BaseDataParser;


/**
 * 监听账户计费可用条数预警任务
 * */
@Service
public class ListenAcctTask {
	private static Logger logger = LoggerFactory.getLogger(ListenAcctTask.class);
	@Autowired
	private BaseDataParser accountService;
	@Autowired
    SendEmail  senEmailService;
	@Autowired
	PropertyUtil propertyEngine;
	public void exec() {
		String trade_id = StringUtil.getRandomNo();	
		logger.info("{} 可用条数预警任务加载开始...",trade_id);
		String emails = propertyEngine.readById("sys_public_email_accts");
		int times = Integer.valueOf(propertyEngine.readById("sys_public_email_expire"));//发送邮件相隔时间
		List<Map<String, Object>> account = accountService.queryAcctAll();
		for(Map<String, Object> map:account){
			if(map.get("ACCT_ID")!=null){
				String is_fee = map.get("ISFEE")!=null?String.valueOf(map.get("ISFEE")):"";
				if("0".equals(is_fee)){
					logger.info("{} 可用条数预警任务-产品列表权限信息开始加载...",trade_id);
					List<Map<String, Object>> prods = accountService.queryAcctProds(
							String.valueOf(map.get("ACCT_ID")));
					if(prods.size()>0){
						for(Map<String, Object> map1:prods){
							int nums = map1.get("TEST_NUM")!=null?
									Integer.valueOf(String.valueOf(map1.get("TEST_NUM"))):0;
							int listen_nums = map1.get("LISTEN_NUM")!=null?
									Integer.valueOf(String.valueOf(map1.get("LISTEN_NUM"))):0;
							if(nums <= listen_nums){
								for(String email:emails.split(",")){
									try {
										if(StringUtil.isEmpty(GlobalCounter.getString(email))){
											GlobalCounter.registStr(email,"1", times);
											logger.info("{} 可用条数预警任务,账户:{},产品:{} 进行预警",trade_id,
													String.valueOf(map.get("ACCT_ID")),String.valueOf(map1.get("PROD_LIMIT")));
											senEmailService.sendEmails(email, 
													"账户:"+String.valueOf(map.get("ACCT_ID"))+",产品:"
											+String.valueOf(map1.get("PROD_LIMIT"))+";可调用条数已不足:"+listen_nums, "CTE账户预警");
										}
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
								}
								
							}
						}
						
					}
				}
			}			
		}
		logger.info("{} 可用条数预警任务加载完成",trade_id);
	}
}
