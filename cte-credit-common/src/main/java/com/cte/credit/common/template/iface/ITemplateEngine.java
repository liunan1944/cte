package com.cte.credit.common.template.iface;

import java.util.Map;

import org.springframework.stereotype.Service;
/**
* @Title: 产品模板引擎接口
* @Description: 模板加载，解析，校验,填充值等
* @author liunan1944@163.com   
* @date 2017年12月6日 下午12:16:36 
* @version V1.0
*/
@Service
public  interface ITemplateEngine {
	/**
	 * 过滤输出参数
	 * @param params     待过滤参数
	 * @param params_out 限制输出
	 */
	public void filterParams(Map<String, Object> params_out,String[] limit_out)  throws Exception;
	 
}
