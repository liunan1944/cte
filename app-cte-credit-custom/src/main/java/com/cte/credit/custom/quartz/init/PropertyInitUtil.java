package com.cte.credit.custom.quartz.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cte.credit.common.template.PropertyUtil;

/**
 * 应用启动时加载系统变量等信息
 * */
@Component
public class PropertyInitUtil {
	private static Logger logger = LoggerFactory.getLogger(PropertyInitUtil.class);
	
	public final static String model_id = "0753513de038453c820cf6869w3f7d3e";
	public static String model_property_owner_id = "";
	private static PropertyInitUtil baseAcctUtils;
	@Autowired
	private BaseDataParser accountService;
	public static void initAccount(){
		logger.info("初始化系统变量信息加载开始...");
		List<Map<String, Object>> property_list = baseAcctUtils.accountService.queryProperties(model_id);
		Map<String,String> property_map = new HashMap<String,String>();
		for(Map<String, Object> map:property_list){
			property_map.put(String.valueOf(map.get("KEY_CODE")), String.valueOf(map.get("KEY_VALUE")));
		}
		PropertyUtil.properties=property_map;
		model_property_owner_id = baseAcctUtils.accountService.queryOwnerid(model_id);
		logger.info("初始化系统变量信息加载完成");
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
