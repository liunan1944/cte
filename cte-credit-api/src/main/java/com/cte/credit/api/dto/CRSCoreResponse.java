package com.cte.credit.api.dto;

import java.util.Map;
/**
* @Title: 接口返回信息
* @Package com.bill99.ifs.crs.common.executor 
* @Description: 接口请求信息
* @author liunan1944@163.com    
* @date 2017年12月5日 下午12:16:36 
* @version V1.0
*/
public class CRSCoreResponse extends ResponseHeader {
	private static final long serialVersionUID = 1L;
	/** 返回码 */
	private String retcode; 
	/** 返回消息 */
	private String retmsg;
	/** 接口数据 */
	private Map<String,Object> retdata;
	/** 数据源标签 */
	private String ds_tags;
	/** 接口标签 */
	private String iface_tags;
	

	public String getRetcode() {
		return retcode;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public String getRetmsg() {
		return retmsg;
	}

	public void setRetmsg(String retmsg) {
		this.retmsg = retmsg;
	}

	public Map<String, Object> getRetdata() {
		return retdata;
	}

	public void setRetdata(Map<String, Object> retdata) {
		this.retdata = retdata;
	}

	public String getDs_tags() {
		return ds_tags;
	}

	public void setDs_tags(String ds_tags) {
		this.ds_tags = ds_tags;
	}

	public String getIface_tags() {
		return iface_tags;
	}

	public void setIface_tags(String iface_tags) {
		this.iface_tags = iface_tags;
	}

	 
	 
}
