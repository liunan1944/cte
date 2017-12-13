package com.cte.credit.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class SystemPropertiesUtil {

	private static Properties systemProps;
	
	static{
		if(systemProps==null){
			systemProps = readProperties("/system.properties");
		}
	}
	 
	private static Properties readProperties(String fileName) {
		InputStream in = null;
		Properties props = null;
		try {
			props = new Properties();
			in =SystemPropertiesUtil.class.getResourceAsStream(fileName);
			props.load(new InputStreamReader(in, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return props;
	}
	/**
	 * 获取某个属性
	 */
	public static String getSysProperty(String key){
		return systemProps.getProperty(key);
	}
}

