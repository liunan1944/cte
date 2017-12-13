package com.cte.credit.api.dto;

import java.util.Map;
/**
* @Title: 接口请求信息
* @Package com.cte.ifs.crs.api
* @Description: 接口请求信息
* @author liunan1944@163.com    
* @date 2017年12月5日 下午12:16:36 
* @version V1.0
*/
public class CRSCoreRequest extends RequestHeader {
	private static final long serialVersionUID = 1L;
	/** 产品ID */
	private String prodId;
	/** 传入的业务参数 */ 
	private Map<String,Object> params;
	public String getProdId() {
		return prodId;
	}
	public void setProdId(String prodId) {
		this.prodId = prodId;
	}
	public Map<String, Object> getParams() {
		return params;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
}
