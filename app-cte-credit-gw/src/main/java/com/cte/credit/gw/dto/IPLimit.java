package com.cte.credit.gw.dto;

public class IPLimit{
	private String ip_limit;
	private String acct_id;
	private String status;
 
	
	public String getIp_limit() {
		return ip_limit;
	}
	public void setIp_limit(String ip_limit) {
		this.ip_limit = ip_limit;
	}

	public String getAcct_id() {
		return acct_id;
	}
	public void setAcct_id(String acct_id) {
		this.acct_id = acct_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
