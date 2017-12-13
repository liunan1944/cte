package com.cte.credit.ds.quartz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cte.credit.common.template.PropertyUtil;
import com.cte.credit.common.util.StringUtil;
import com.cte.credit.ds.quartz.init.BaseDataParser;
import com.cte.credit.ds.quartz.init.PropertyInitUtil;
/**
 * 加载系统变量任务
 * */
@Component
public class PropertyTask {
	private static Logger logger = LoggerFactory.getLogger(PropertyTask.class);
	@Autowired
	private BaseDataParser propertyService;
	public void exec() {
		String trade_id = StringUtil.getRandomNo();	
		logger.info("{} 加载系统变量任务开始...",trade_id);
		try{
			String ownerId = propertyService.queryOwnerid(PropertyInitUtil.model_id);
			if(StringUtil.isEmpty(ownerId)){
				logger.info("{} 加载系统变量任务,ownerId为空",trade_id);
			}else{
				boolean reload_property = false;
				if(!ownerId.equals(PropertyInitUtil.model_property_owner_id)){
					reload_property = true;
					PropertyInitUtil.model_property_owner_id = ownerId;
					logger.info("{} 系统变量有修改,重新加载:{}",trade_id,ownerId);
				}else{
					logger.info("{} 系统变量无更新,不需加载",trade_id);
				}
				if(reload_property){
					List<Map<String, Object>> property_list =propertyService.queryProperties(PropertyInitUtil.model_id);
					Map<String,String> property_map = new HashMap<String,String>();
					for(Map<String, Object> map:property_list){
						property_map.put(String.valueOf(map.get("KEY_CODE")), String.valueOf(map.get("KEY_VALUE")));
					}
					PropertyUtil.properties=property_map;
				}				
			}		
		}catch(Exception e){
			logger.info("{} 加载系统变量任务异常!",trade_id);
		}		
		logger.info("{} 加载系统变量任务结束!",trade_id);
	}
}
