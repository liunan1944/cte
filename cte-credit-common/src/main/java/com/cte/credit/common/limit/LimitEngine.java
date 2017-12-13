package com.cte.credit.common.limit;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
* @Title: 限流引擎
* @Description: 初始化，注册限流对象
* @author liunan1944@163.com   
* @date 2017年12月6日 下午12:16:36 
* @version V1.0
*/
public class LimitEngine {
	private static final  Logger logger = LoggerFactory.getLogger(LimitEngine.class);
	private static Map<String,LimitConfig> semaphores = new HashMap<String,LimitConfig>();;
	
	/**
	 * 判断是否需要限流
	 * @param prodCode
	 * @param rule
	 * @return
	 */
	public synchronized static boolean limit(String prodCode,String rule){
		Integer limitSize = null;
		String[] limits = rule.split(";");
		for (String limit : limits) {
			if (limit.startsWith(prodCode+":") || limit.startsWith("*:")) {
				limitSize = Integer.parseInt(limit.split(":")[1]);
				break;
			}
		}
		if (limitSize != null) {
			if (changed(prodCode, limitSize)) {
				reRegist(prodCode, limitSize);
			}
			if (!exist(prodCode))
				regist(prodCode, limitSize);
			if (!tryAcquire(prodCode)) {
				return false;
			}
		}
		return true;
	}
	
	public static  boolean changed(String key, int size) {
		if(semaphores.containsKey(key))
			return size != semaphores.get(key).getSize();
		else 
			return false;
	}
	
	public static  boolean exist(String key) {
		return semaphores.containsKey(key);
	}
	
	public static  void regist(String key, int size) {
		if (!semaphores.containsKey(key)) {
			semaphores.put(key, new LimitConfig(size,new Semaphore(size, true)));
			logger.info("限流注册提示: " + key + " 注册成功,预设值: "+size);
		}
	}
	public static Semaphore get(String key) {
		LimitConfig lc = semaphores.get(key);
		return lc != null ? semaphores.get(key).getSemaphore() : null;
	}
	
	public static  void reRegist(String key, int size) {
		semaphores.put(key, new LimitConfig(size,new Semaphore(size, true)));
		logger.info("限流重置提示: " + key + " 注册成功,预设值: "+size);
	}
	
	public static  boolean tryAcquire(String key) {
		Semaphore semp = get(key);
		if(semp!=null){
			logger.info("限流许可剩余: {}",semp.availablePermits());
			return semp.tryAcquire();
		}
		return false;
	}
	public static  void release(String key) {
		Semaphore semp = get(key);
		if(semp!=null){
			 semp.release();
		}
	}
	
	static class LimitConfig {
		private int size;
		private Semaphore semaphore;

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}

		public Semaphore getSemaphore() {
			return semaphore;
		}

		public void setSemaphore(Semaphore semaphore) {
			this.semaphore = semaphore;
		}

		public LimitConfig(int size, Semaphore semaphore) {
			super();
			this.size = size;
			this.semaphore = semaphore;
		}
	}
}
