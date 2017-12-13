package com.cte.credit.gw.dto;

public class ProdLimit{

	private String prod_limit;
	private String acct_id;
	private String status;
	private Double price;
	private int test_num;
	private String pay_tags;


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
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getProd_limit() {
		return prod_limit;
	}
	public void setProd_limit(String prod_limit) {
		this.prod_limit = prod_limit;
	}
	public int getTest_num() {
		return test_num;
	}
	public void setTest_num(int test_num) {
		this.test_num = test_num;
	}
	public String getPay_tags() {
		return pay_tags;
	}
	public void setPay_tags(String pay_tags) {
		this.pay_tags = pay_tags;
	}
	
}
