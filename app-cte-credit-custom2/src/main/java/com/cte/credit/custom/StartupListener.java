package com.cte.credit.custom;

import java.net.InetAddress;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cte.credit.common.util.DateUtil;
import com.cte.credit.custom.quartz.init.PropertyInitUtil;


public class StartupListener{
	private static Logger logger = LoggerFactory.getLogger(StartupListener.class);

	public void start(){
	    try{
		      logger.info("==============================");
		      logger.info("        Custom包初始化启动          ");
		      PropertyInitUtil.initAccount();
		      logger.info("==============================");
		      logger.info("服务暴露的ip: " + InetAddress.getLocalHost().getHostAddress());
		      logger.info("服务器时间: " + DateUtil.getSimpleDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		      logger.info("操作系统: " + System.getProperty("os.name"));
		      logger.info("==============================");
	    } catch (Exception e) {
		      e.printStackTrace();
		      logger.error("Custom包初始化启动失败! ");
	    }
  	}
}