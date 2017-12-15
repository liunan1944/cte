package com.cte.credit.custom.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.cte.credit.api.dto.CRSCoreResponse;
import com.cte.credit.api.dto.DataSource;
import com.cte.credit.api.dto.Param;
import com.cte.credit.api.enums.CRSStatusEnum;
import com.cte.credit.api.iface.IDataSourceService;
import com.cte.credit.common.agent.RouteDispatch;
import com.cte.credit.common.annotation.CustomClass;
import com.cte.credit.common.template.PropertyUtil;
import com.cte.credit.common.util.SpringContextUtils;
import com.cte.credit.common.util.StringUtil;
import com.cte.credit.custom.quartz.init.PropertyInitUtil;

@Service
public class BaseCustomCoreService
{
  private final Logger logger = LoggerFactory.getLogger(BaseCustomCoreService.class);
  @Resource(name="dataSourceService")
  private IDataSourceService dataSourceService;
  @Resource(name="ds2Service")
  private IDataSourceService ds2Service;
  @Autowired
  private PropertyUtil propertyEngine;
  @Autowired
  private RouteDispatch routeDispatch;
  private static Map<String, Object> implObjectMap = null;

  public boolean valid(String trade_id, Map<String, Object> params, String[] paramIds, String[] nullAbleparamIds)
  {
    if (paramIds != null) {
      for (String paramId : paramIds){
    	  boolean isnull = false;
    	  if (nullAbleparamIds != null) {
    		  for(String nullParam:nullAbleparamIds){
	    		  if(nullParam.equals(paramId)){
	    			  isnull = true;
	    			  continue;
	    		  }    			  
	    	  } 
    	  }	 
    	  if(isnull)
    		  continue;
    	  if ((!params.containsKey(paramId)) || 
    	          (params.get(paramId) == null) || 
    	          (!StringUtils.isNotEmpty(params.get(paramId)
    	          .toString())))
    	        {
    	          return false;
    	        }
      }
        
    }
    return true;
  }

  public Object extractValueFromResult(String key, Map<String, Object> params_out)
  {
    Object retdataObj = params_out.get("retdata");
    if ((retdataObj != null) && 
      ((retdataObj instanceof Map))) {
      return ((Map)params_out.get("retdata"))
        .get(key);
    }

    return null;
  }

  public Object extractRetFromResult(String key, Map<String, Object> params_out) {
    Object retdataObj = params_out;
    if ((retdataObj != null) && 
      ((retdataObj instanceof Map))) {
      return params_out
        .get(key);
    }

    return null;
  }

  public boolean isSuccess(Map<String, Object> params_out)
  {
    if (params_out == null)
      return false;
    CRSStatusEnum retstatus = CRSStatusEnum.valueOf(params_out.get(
      "retstatus").toString());
    return CRSStatusEnum.STATUS_SUCCESS.equals(retstatus);
  }

  /**
	 * 匹配服务实现类
	 * @param prod_code
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T findMatchedImpl(String prod_code) {
		if(implObjectMap==null){
			ApplicationContext ac = SpringContextUtils.getContext();
			implObjectMap = ac.getBeansWithAnnotation(CustomClass.class);
		}
		for(Object obj :  implObjectMap.values()){
			if (obj != null) {
				CustomClass customClass = obj.getClass().getAnnotation(
						CustomClass.class);
				if (customClass.bindingService().getProd_code()
						.equalsIgnoreCase(prod_code)) {
					return (T) obj;
				}
			}
		}
		return null;
	}

  public Map<String, Object> fetchByDataSource(String trade_id, String ds_id, String prd_id,
		  Map<String,Object> paramsMap)throws Exception{
	String route_ds = propertyEngine.readById("sys_public_hint_customTods");//强制路由:1强制
	String balance_ds = propertyEngine.readById("sys_public_balance_customTods");//负载均衡
    String final_route = "routeDs";
	DataSource ds = new DataSource();
    ds.setId(ds_id);
    ds.setRefProdCode(prd_id);
    ds.setParams_in(convertParams(paramsMap));
    Map<String, Object> rets = new HashMap<String, Object>();
    String[] hint_route = route_ds.split(",");
    if("1".equals(hint_route[0])){
    	logger.info("{} 强制路由:{}",trade_id,route_ds);
    	if(!StringUtil.isEmpty(hint_route[1])){
    		final_route = hint_route[1];
    	}   	
    }else{
    	logger.info("{} 负载均衡:{}",trade_id,balance_ds);
    	final_route = routeDispatch.dispatchLocal(final_route, 
    			"custom", balance_ds,PropertyInitUtil.custom_route_map);
    }
    if("routeDs2".equals(final_route)){
    	logger.info("{} 路由到ds2",trade_id);
    	rets = this.ds2Service.fetch(trade_id, ds);
    }else{
    	logger.info("{} 路由到ds",trade_id);
    	rets = this.dataSourceService.fetch(trade_id, ds);
    }
    // redis数据流量监控
    String redis_lisen = "custom-"+final_route;
    PropertyInitUtil.routeSet(trade_id,redis_lisen,false);
	logger.info("{} 流量监控+1:{}", trade_id,redis_lisen);
    return rets;
  }

	
  public String getStrFromRetdata(String prefix, String key, CRSCoreResponse response)
  {
    String value = "";
    if ((response != null) && (response.getRetdata() != null) && (response.getRetdata().containsKey(key))) {
      value = getStrByKey(prefix, key, response.getRetdata());
    }
    return value;
  }

  protected String getStrByKey(String prefix, String key, Map<String, Object> map) {
    if (map == null) {
      this.logger.info("{} 获取{}值时map为空", prefix, key);
      return "";
    }
    String str = map.get(key) != null ? map.get(key).toString() : "";
    str = str.trim();

    return str;
  }

  public int getIntFormStr(String str) {
    if ((str == null) || ("".equals(str))) {
      str = "0";
    }
    return Integer.valueOf(str).intValue();
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
}