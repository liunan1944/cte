package com.cte.credit.api.iface;

import com.cte.credit.api.dto.CRSCoreRequest;
import com.cte.credit.api.dto.CRSCoreResponse;
/**
* @Title: 核心适配器接口
* @Package com.cte.credit.api.iface.common.executor 
* @Description: 核心适配器处理
* @author xiaocl chenglin.xiao@99bill.com   
* @date 2015年8月12日 下午12:16:36 
* @version V1.0
*/
public interface ICoreService { 
	/**
	 * 接口交易入口(同步方式)  
	 * @param request  请求对象
	 * @param trade_id  交易ID
	 * @return         返回对象
	 * @throws Exception
	 */
	public CRSCoreResponse request(String trade_id,CRSCoreRequest request);
}
