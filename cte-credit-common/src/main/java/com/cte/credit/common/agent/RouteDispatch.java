package com.cte.credit.common.agent;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.cte.credit.api.exception.ServiceException;
import com.cte.credit.common.counter.GlobalCounter;
import com.cte.credit.common.util.StringUtil;
/**
 * 投递比例分配
 * */
@Service
public class RouteDispatch {
	private static final  Logger logger = LoggerFactory.getLogger(RouteDispatch.class);
	private static final String suffix = "-";

	public String dispatch(String _default,String prodCode , String dsRatio) throws Exception {
		String route = _default;
		String[] p = dsRatio.split("=");
		String[] is = p[0].split(":");
		String[] rs = p[1].split(":");
		String[] nis = new String[is.length];
		for (int j = 0; j < is.length; j++) 
			nis[j] = prodCode + suffix + is[j];
		double s = GlobalCounter.getCount(nis);
		logger.info(nis + " 流量监控汇总："+s);
		for (int i = 0; i < nis.length; i++) {
			double r = Double.parseDouble(rs[i]) / 10.0;
			double c = GlobalCounter.getCount(nis[i]);
			if (s <= 0 || c / s <= r) {
				return is[i];
			}
		}
		if(!StringUtil.isEmpty(is[0])){
			route = is[0];
		}
		return route;
	}
	public String dispatchLocal(String _default,String prodCode , 
			String dsRatio,HashMap<String,Integer> total) throws Exception {
		String route = _default;
		String[] p = dsRatio.split("=");
		String[] is = p[0].split(":");
		String[] rs = p[1].split(":");
		String[] nis = new String[is.length];
		for (int j = 0; j < is.length; j++) 
			nis[j] = prodCode + suffix + is[j];
		double s = getLocalCount(total,nis);
		logger.info(nis + " 流量监控汇总："+s);
		for (int i = 0; i < nis.length; i++) {
			double r = Double.parseDouble(rs[i]) / 10.0;
			double c = getLocalCount(total,nis[i]);
			if (s <= 0 || c / s <= r) {
				return is[i];
			}
		}
		if(!StringUtil.isEmpty(is[0])){
			route = is[0];
		}
		return route;
	}
	/**
	 * 查询计数总次数
	 * @param key
	 * @return
	 * @throws ServiceException
	 */
	public static synchronized int getLocalCount(HashMap<String,Integer> total,
			String... keys) throws ServiceException {
		int cnt = 0;
		try {
			for(String key : keys){
				if (total.get(key)!=null)
					cnt += Integer.valueOf(total.get(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return cnt;
	}
}
