package com.cte.credit.ds.dao.domain.salary;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.cte.credit.common.domain.BaseDomain;

/**
 * 国政通工资水平表
 */
@Entity
@Table(name = "T_DS_GUOZT_SALARY_RESULT",schema="CPDB_DS")
@SequenceGenerator(name = "SEQ_T_DS_GUOZT_SALARY_RESULT", sequenceName = "SEQ_T_DS_GUOZT_SALARY_RESULT")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class GuoZT_salary_result extends BaseDomain {
	private static final long serialVersionUID = 1L;
	 private long id  ;
	 private String trade_id ;
	 private String cardno ;
	 private String name ;
	 private String code ;
	 private String data  ;
	 private String message;
	
	/**
	 * 获取 主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_DS_GUOZT_SALARY_RESULT")
	@Column(name = "ID", unique = true, nullable = false)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTrade_id() {
		return trade_id;
	}

	public void setTrade_id(String trade_id) {
		this.trade_id = trade_id;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
