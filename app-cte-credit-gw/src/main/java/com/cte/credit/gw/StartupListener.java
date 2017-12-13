package com.cte.credit.gw;

import java.net.UnknownHostException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cte.credit.common.util.DateUtil;
import com.cte.credit.gw.quartz.init.AccountInitUtil;

public class StartupListener {
	private static Logger logger = LoggerFactory.getLogger(StartupListener.class);
	/**
	 * start
	 * @throws UnknownHostException 
	 */ 
	public void start(){
		try {
			logger.info("==============================");
			logger.info("        gw初始化启动          ");
			AccountInitUtil.initAccount();		
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("gw初始化启动失败! ");
		}
		logger.info("==============================");
		try {
			logger.info("服务暴露的ip: "+ java.net.InetAddress.getLocalHost().getHostAddress());
			logger.info("服务器时间: "+ DateUtil.getSimpleDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			logger.info("操作系统: "+ System.getProperty("os.name"));
			logger.info("==============================");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Get IP failed! ");
		}
	}
}
