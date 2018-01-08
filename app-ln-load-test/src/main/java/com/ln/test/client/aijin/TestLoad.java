package com.ln.test.client.aijin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLoad {
	private final  Logger logger = LoggerFactory.getLogger(TestLoad.class);
	public void test01(String prefix,String str){
		logger.info("{} 测试类加载001:{}",prefix,str);
	}
}
