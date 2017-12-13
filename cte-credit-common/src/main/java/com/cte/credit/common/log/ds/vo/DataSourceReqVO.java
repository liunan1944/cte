package com.cte.credit.common.log.ds.vo;

/**
 * 数据源请求数据对象
 **/
@SuppressWarnings("unused")
public class DataSourceReqVO {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String trade_id;
	private String req_name;
	private String req_value;
	private Long refId;

	public DataSourceReqVO() {
		super();
	}

	public DataSourceReqVO(DataSourceLogVO log){
		super();
		setRelatedRspLog(log);
	}
	
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

	public String getReq_name() {
		return req_name;
	}

	public void setReq_name(String req_name) {
		this.req_name = req_name;
	}

	public String getReq_value() {
		return req_value;
	}

	public void setReq_value(String req_value) {
		this.req_value = req_value;
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
	}

	public void setRelatedRspLog(DataSourceLogVO log){
		if(log != null){
		   this.setRefId(log.getId());	
		}
	}

}