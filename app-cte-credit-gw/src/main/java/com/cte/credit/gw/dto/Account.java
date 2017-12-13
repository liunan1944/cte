package com.cte.credit.gw.dto;

import java.util.Date;
import java.util.List;


public class Account{
	private String acct_id;
	private String api_key;
	private Double balance;
	private String status;
	private String isfee;
	private Date   enddate;	

	public String getApi_key() {
		return api_key;
	}
	public void setApi_key(String api_key) {
		this.api_key = api_key;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getEnddate() {
		return enddate;
	}
	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public String getAcct_id() {
		return acct_id;
	}
	public void setAcct_id(String acct_id) {
		this.acct_id = acct_id;
	}
	
	public String getIsfee() {
		return isfee;
	}
	public void setIsfee(String isfee) {
		this.isfee = isfee;
	}
	
}
