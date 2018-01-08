package com.ln.test;


import java.net.UnknownHostException;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cte.credit.common.util.DateUtil;


public class StartupListener{

	private static Logger logger = LoggerFactory.getLogger(StartupListener.class);
	/**
	 * start
	 * @throws UnknownHostException 
	 */
	public void start(){
		try {
			logger.info("==============================");
			logger.info("        test包初始化启动            ");
			logger.info("        系统变量开始加载...");
			logger.info("        系统变量加载完成...");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Ds包初始化启动失败! ");
		}
		logger.info("==============================");
		try {
			logger.info("服务暴露的ip: "+ java.net.InetAddress.getLocalHost().getHostAddress());
			logger.info("服务器时间: "+ DateUtil.getSimpleDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			logger.info("操作系统: "+ System.getProperty("os.name"));
			logger.info("==============================");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			logger.error("Get IP failed! ");
		}
	}
}
