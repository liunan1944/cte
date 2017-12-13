package com.cte.credit.common.template;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cte.credit.api.Conts;
import com.cte.credit.common.dao.DaoService;
import com.cte.credit.common.template.iface.ITemplateEngine;
 
/**
* @Title: 产品模板引擎
* @Description: 模板解析，校验等
* @author xiaocl chenglin.xiao@99bill.com   
* @date 2015年8月12日 下午12:16:36 
* @version V1.0
*/
@Service
public class TemplateEngine implements ITemplateEngine{
	private final  Logger logger = LoggerFactory.getLogger(TemplateEngine.class);

	@Autowired
	private DaoService daoService; 

	@SuppressWarnings("unchecked")
	@Override
	public void filterParams(Map<String, Object> params_out,
			String[] limit_out) throws Exception {
		Map<String, Object> rets = new HashMap<String, Object>();
		if(params_out.containsKey(Conts.KEY_RET_DATA) && params_out.get(Conts.KEY_RET_DATA)!=null){
			Map<String, Object> retdata = (Map<String, Object>) params_out.get(Conts.KEY_RET_DATA);
			for(String key : limit_out)
				rets.put(key, retdata.get(key));
		}
		params_out.put(Conts.KEY_RET_DATA, rets);
	}
	
}
