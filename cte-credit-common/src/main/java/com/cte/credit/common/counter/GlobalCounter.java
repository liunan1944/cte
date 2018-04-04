package com.cte.credit.common.counter;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import com.cte.credit.api.Conts;
import com.cte.credit.api.exception.ServiceException;
/**
* @Title: 通用计数器
* @Package com.bill99.ifs.crs.counter 
* @Description:  通用计数器(redis)
* @author xiaocl chenglin.xiao@99bill.com   
* @date 2015年11月18日 下午12:16:36 
* @version V1.0
*/
public class GlobalCounter {
	private static final  Logger logger = LoggerFactory.getLogger(GlobalCounter.class);
	private static ShardedJedisPool pool;  
	/**
	 * 新注册一计数项
	 * @param key
	 * @param total
	 */
	public static void regist(String key,
			int total) {
		ShardedJedis jedis = pool.getResource();
		try {
			jedis.set(key, total + "");
			jedis.expire(key, 86400);
			logger.info("流量提示: " + key + " 注册成功,预设值: "+total);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
	}
	/**
	 * 新注册一计数项(指定失效时间)
	 * @param key
	 * @param total
	 */
	public static void regist(String key,
			int total,int seconds) {
		ShardedJedis jedis = pool.getResource();
		try {
			jedis.set(key, total + "");
			jedis.expire(key, seconds);
			logger.info("流量提示: " + key + " 注册成功,预设值: "+total);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
	}
	
	/**
	 * 向Redis中注册值（String）
	 * @param key
	 * @param total
	 */
	public static void registStr(String key,
			String value,int seconds) {
		ShardedJedis jedis = pool.getResource();
		try {
			jedis.set(key, value);
			jedis.expire(key, seconds);
			logger.info("信息: " + key + " 注册成功,预设值: "+value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
	}
	/**
	 * 新注册一计数项(指定失效时间)
	 * @param key
	 * @param value
	 * @param seconds
	 * @throws ServiceException
	 */
	public static void regist(String key, String value, int seconds) {
		ShardedJedis jedis = pool.getResource();
		try {
			jedis.lpush(key, value);
			jedis.expire(key, seconds);
			logger.info("集合提示: " + key + " 注册成功,预设值: " + value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
	}
	/**
	 * 删除指定V的K
	 * @param key
	 * @param value
	 */
	public static void del(String key, String value) {
		ShardedJedis jedis = pool.getResource();
		try {
			jedis.lrem(key, 1L, value);
			logger.info("集合提示: " + value + " 移除成功!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
	}
	/**
	 * 判断指定v的K是否存在
	 * @param key
	 * @param value
	 * @return
	 * @throws ServiceException
	 */
	public static boolean exists(String key, String value) throws ServiceException{
		init();
		ShardedJedis jedis = pool.getResource();
		try {
			List list = jedis.lrange(key, 0L, -1L);
			if (list != null) {
				String[] values = (String[]) (String[]) list
						.toArray(new String[list.size()]);
				return ArrayUtils.contains(values, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return false;
	}
	/**
	 * 删除注册
	 * @param key
	 * @throws ServiceException
	 */
	public static void del(String key) throws ServiceException {
		init();
		ShardedJedis jedis = pool.getResource();
		try {
			jedis.del(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
	}
	/**
	 * 新增注册
	 * @param key
	 * @throws ServiceException
	 */
	public static void sign(String key) throws ServiceException {
		if(!contain(key))
			regist(key, 1);
		else
			signByNum(key,1);
	}
	/**
	 * 新增注册(设置失效时间)
	 * @param key
	 * @throws ServiceException
	 */
	public static void sign(String key,int seconds) throws ServiceException {
		if(!contain(key))
			regist(key, 1 ,seconds);
		else
			signByNum(key,1);
	}
	/**
	 * 设置字符串
	 * @param key
	 * @param value
	 * @throws ServiceException 
	 */
	public static  void setString(String key,String value) throws ServiceException{
		init();
		ShardedJedis jedis = pool.getResource();
		try {
			jedis.set(key, value);
			jedis.expire(key, 86400);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
	}
	/**
	 * 设置字符串
	 * @param key
	 * @param value
	 * @param seconds 失效时间 单位秒
	 * @throws ServiceException 
	 */
	public static  void setString(String key,String value,int seconds) throws ServiceException{
		init();
		ShardedJedis jedis = pool.getResource();
		try {
			jedis.set(key, value);
			jedis.expire(key, seconds);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
	}
	/**
	 * 取得字符串
	 * @param key
	 * @return
	 * @throws ServiceException 
	 */
	public static String getString(String key) throws ServiceException{
		init();
		ShardedJedis jedis = pool.getResource();
		String value = null;
		try {
			value = jedis.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return value;
	}
	/**
	 * 新增注册
	 * @param key
	 * @param num
	 * @throws ServiceException
	 */
	public static synchronized void signByNum(String key, int num) throws ServiceException {
		init();
		ShardedJedis jedis = pool.getResource();
		try {
			jedis.incrBy(key, num);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
	}
	/**
	 * 查询计数总次数
	 * @param key
	 * @return
	 * @throws ServiceException
	 */
	public static synchronized int getCount(String... keys) throws ServiceException {
		init();
		ShardedJedis jedis = pool.getResource();
		int cnt = 0;
		try {
			for(String key : keys){
				if (contain(key))
					cnt += Integer.valueOf(jedis.get(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return cnt;
	}
	/**
	 * 查询计数项是否已添加
	 * @param key
	 * @return
	 * @throws ServiceException
	 */
	public static synchronized boolean contain(String key) throws ServiceException {
		init();
		ShardedJedis jedis = pool.getResource();
		Boolean value = false;
		try {
			value =  jedis.exists(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
		return value;
	}
	/**
	 * 计数器初始化
	 */
	private static final String beanName = "shardedJedisPool";
	public static void init() throws ServiceException{
		if (pool == null) {
			WebApplicationContext wc  = ContextLoader
					.getCurrentWebApplicationContext();
			if(wc.containsBean(beanName)){
				pool =((ShardedJedisPool) wc.getBean(beanName));
			}else{
				throw new ServiceException("请先在spring.xml中引入 base-redis-conf.xml!");
			}
		}
	}
}
