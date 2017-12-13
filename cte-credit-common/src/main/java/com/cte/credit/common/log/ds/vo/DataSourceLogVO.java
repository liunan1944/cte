package com.cte.credit.common.log.ds.vo;


import java.sql.Timestamp;

/**
 * 
 * 数据源请求响应数据对象
 **/
@SuppressWarnings("unused")
public class DataSourceLogVO {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String trade_id;
	private String ds_id;
	private String prod_id;
	private String acct_id;
	private String state_code;
	private String state_msg;
	private String req_url;
	private String incache;
	private String biz_code1;
	private String biz_code2;
	private String biz_code3;
	private String field_id;
	private Timestamp req_time;
	private Timestamp rsp_time;
	private Long total_cost;
	private String tag;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void commaEndingAndSetTag(String tag){
		if(tag == null){return ;}
	    else if(tag.trim().endsWith(";")){
			this.setTag(tag);
		}else{
			int pos = tag.indexOf(";");
			if(pos > -1){
				this.setTag(tag+";");
			}else{
				this.setTag(tag);
			}
		}
	}
	

	/**
	 * 获取 主键
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTrade_id() {
		return trade_id;
	}

	public void setTrade_id(String trade_id) {
		this.trade_id = trade_id;
	}

	public String getDs_id() {
		return ds_id;
	}

	public void setDs_id(String ds_id) {
		this.ds_id = ds_id;
	}

	public String getState_code() {
		return state_code;
	}

	public void setState_code(String state_code) {
		this.state_code = state_code;
	}

	public String getState_msg() {
		return state_msg;
	}

	public void setState_msg(String state_msg) {
		this.state_msg = state_msg;
	}

	public String getReq_url() {
		return req_url;
	}

	public void setReq_url(String req_url) {
		this.req_url = req_url;
	}

	public String getIncache() {
		return incache;
	}

	public void setIncache(String incache) {
		this.incache = incache;
	}

	public String getBiz_code1() {
		return biz_code1;
	}

	public void setBiz_code1(String biz_code1) {
		this.biz_code1 = biz_code1;
	}

	public String getBiz_code2() {
		return biz_code2;
	}

	public void setBiz_code2(String biz_code2) {
		this.biz_code2 = biz_code2;
	}

	public String getBiz_code3() {
		return biz_code3;
	}

	public void setBiz_code3(String biz_code3) {
		this.biz_code3 = biz_code3;
	}

	public String getField_id() {
		return field_id;
	}

	public void setField_id(String field_id) {
		this.field_id = field_id;
	}

	public Timestamp getReq_time() {
		return req_time;
	}

	public void setReq_time(Timestamp req_time) {
		this.req_time = req_time;
	}

	public Timestamp getRsp_time() {
		return rsp_time;
	}

	public void setRsp_time(Timestamp rsp_time) {
		this.rsp_time = rsp_time;
	}

	public Long getTotal_cost() {
		return total_cost;
	}

	public void setTotal_cost(Long total_cost) {
		this.total_cost = total_cost;
	}

	public String getProd_id() {
		return prod_id;
	}

	public void setProd_id(String prod_id) {
		this.prod_id = prod_id;
	}

	public String getAcct_id() {
		return acct_id;
	}

	public void setAcct_id(String acct_id) {
		this.acct_id = acct_id;
	}
}