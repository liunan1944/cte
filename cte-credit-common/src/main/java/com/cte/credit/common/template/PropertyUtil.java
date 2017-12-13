package com.cte.credit.common.template;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cte.credit.common.util.StringUtil;

@Service
public class PropertyUtil {
	private static Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
	public static Map<String,String> properties = new HashMap<String,String>();
	public String readById(String ids){
		if(StringUtil.isEmpty(ids))
			return "";
		return properties.get(ids);
	}
}
