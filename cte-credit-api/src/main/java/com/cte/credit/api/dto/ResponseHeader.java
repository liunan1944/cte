package com.cte.credit.api.dto;

import java.io.Serializable;
import java.util.Date;

/**
* @Title: 返回头信息
* @Package com.bill99.ifs.crs.common.executor 
* @Description: 接口请求信息
* @author xiaocl chenglin.xiao@99bill.com   
* @date 2015年8月12日 下午12:16:36 
* @version V1.0
*/
public class ResponseHeader implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 响应序列号 */  
	private String response_sn;
	/** 版本号 */
	private String version;
	/** 时间戳 */
	private Date retdate;
	
	 
	public String getResponse_sn() {
		return response_sn;
	}
	public void setResponse_sn(String response_sn) {
		this.response_sn = response_sn;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Date getRetdate() {
		return retdate;
	}
	public void setRetdate(Date retdate) {
		this.retdate = retdate;
	}
	 
}
