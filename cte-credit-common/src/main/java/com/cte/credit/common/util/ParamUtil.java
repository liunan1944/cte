package com.cte.credit.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.cte.credit.api.dto.CRSCoreRequest;
import com.cte.credit.api.dto.Param;


public class ParamUtil {
	/**
	 * 提取入参参数值
	 * @param params 
	 * @param key
	 * @return
	 */
	public static Object findValue(List<Param> params ,String key){
		if (params != null && StringUtils.isNotEmpty(key)) {
			for (Param param : params) {
				if (StringUtils.isNotEmpty(param.getId()) 
						&& key.equals(param.getId()))
					return param.getValue();
			}
		}
		return "";  
	}
	/**
	 * 转换参数
	 * @param params     待转换参数
	 * @return           转换后参数
	 */
	public static Map<String, Object> convertParams(List<Param> params_in){
		Map<String,Object> map = new HashMap<String,Object>();
		for(Param p : params_in){
			if(!StringUtils.isEmpty(p.getId()))
				map.put(p.getId(), p.getValue());
		}
		return map;
	}
	/**
	 * 转换参数
	 * @param params     待转换参数
	 * @return           转换后参数
	 */
	public static List<Param> convertParams(Map<String, Object> params_in)
			throws Exception {
		List<Param> params = new ArrayList<Param>();
		for (Iterator<Entry<String, Object>> entry = params_in.entrySet()
				.iterator(); entry.hasNext();) {
			Entry<String, Object> entryObj = entry.next();
			Param p = new Param();
			p.setId(entryObj.getKey());
			p.setValue(entryObj.getValue());
			params.add(p);
		}
		return params;
	}

	/**
	 * 复制授权信息
	 * @param request
	 * @return
	 */
	public static Map<String,Object> copyHeaders(String trade_id,CRSCoreRequest request){
		Map<String,Object> retdata = new HashMap<String,Object>();
		retdata.put("trade_id", trade_id);
		if(request!=null){
			if(request.getRequst_sn()!=null){
				retdata.put("requst_sn", request.getRequst_sn());
			}
			if(request.getVersion()!=null){
				retdata.put("version", request.getVersion());
			}
			if(request.getAcct_id()!=null){
				retdata.put("acct_id", request.getAcct_id());
			}
			if(request.getApi_key()!=null){
				retdata.put("api_key", request.getApi_key());
			}
			if(request.getSign()!=null){
				retdata.put("sign", request.getSign());
			}
			if(request.getMac_address()!=null){
				retdata.put("mac_address", request.getMac_address());
			}
			if(request.getIp_address()!=null){
				retdata.put("ip_address", request.getIp_address());
			}
			if(request.getOperid()!=null){
				retdata.put("operid", request.getOperid());
			}
			if(request.getAccess_token()!=null){
				retdata.put("access_token", request.getAccess_token());
			}
		}
		return retdata;
	}
}
