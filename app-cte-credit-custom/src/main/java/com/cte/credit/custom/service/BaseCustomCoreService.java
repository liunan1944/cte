package com.cte.credit.custom.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.cte.credit.api.dto.CRSCoreResponse;
import com.cte.credit.api.dto.DataSource;
import com.cte.credit.api.enums.CRSStatusEnum;
import com.cte.credit.api.iface.IDataSourceService;
import com.cte.credit.common.annotation.CustomClass;
import com.cte.credit.common.util.SpringContextUtils;
import com.cte.credit.api.dto.Param;

@Service
public class BaseCustomCoreService
{
  private final Logger logger = LoggerFactory.getLogger(BaseCustomCoreService.class);
  @Resource(name="dataSourceService")
  private IDataSourceService dataSourceService;
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
    DataSource ds = new DataSource();
    ds.setId(ds_id);
    ds.setRefProdCode(prd_id);
    ds.setParams_in(convertParams(paramsMap));
    Map<String, Object> rets = this.dataSourceService.fetch(trade_id, ds);
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