package com.cte.credit.api.dto;

import java.io.Serializable;

/**
* @Title: 参数模板定义
* @Description: 基本属性定义
* @author xiaocl chenglin.xiao@99bill.com   
* @date 2015年8月12日 下午12:16:36 
* @version V1.0
*/
public class Param implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String id;
	public String name;
	public Object value;
	public String fixedValue;
	public String sourceId;
	public String validExpr;
	public String type;
	public Boolean bySql;
	public Boolean byDecrypt;
	public String  sql;
	public String  isQuerySql;//空值或0-查询sql 1-insert或update语句 add by wangjing
	public Boolean allowNullOrBlank;
	public Boolean allowNodeNotExist;
	public Boolean ignoreLowerCase;
	public Boolean byLast = false;
	public String getIsQuerySql() {
		return isQuerySql;
	}
	public void setIsQuerySql(String isQuerySql) {
		this.isQuerySql = isQuerySql;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Boolean getBySql() {
		return bySql;
	}
	public void setBySql(Boolean bySql) {
		this.bySql = bySql;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public Boolean getAllowNullOrBlank() {
		return allowNullOrBlank;
	}
	public void setAllowNullOrBlank(Boolean allowNullOrBlank) {
		this.allowNullOrBlank = allowNullOrBlank;
	}
	public Boolean getAllowNodeNotExist() {
		return allowNodeNotExist;
	}
	public void setAllowNodeNotExist(Boolean allowNodeNotExist) {
		this.allowNodeNotExist = allowNodeNotExist;
	}
	public Boolean getIgnoreLowerCase() {
		return ignoreLowerCase;
	}
	public void setIgnoreLowerCase(Boolean ignoreLowerCase) {
		this.ignoreLowerCase = ignoreLowerCase;
	}
	public Boolean getByLast() {
		return byLast;
	}
	public void setByLast(Boolean byLast) {
		this.byLast = byLast;
	}
	public String getFixedValue() {
		return fixedValue;
	}
	public void setFixedValue(String fixedValue) {
		this.fixedValue = fixedValue;
	}
	
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	
	public String getValidExpr() {
		return validExpr;
	}
	public void setValidExpr(String validExpr) {
		this.validExpr = validExpr;
	}
	public Boolean getByDecrypt() {
		return byDecrypt;
	}
	public void setByDecrypt(Boolean byDecrypt) {
		this.byDecrypt = byDecrypt;
	}
	public Param(String id, String name, Object value, String fixedValue, String sourceId,String validExpr, String type,
			Boolean bySql,Boolean byDecrypt, String sql, Boolean allowNullOrBlank,
			Boolean allowNodeNotExist, Boolean ignoreLowerCase, Boolean byLast) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
		this.fixedValue = fixedValue;
		this.sourceId = sourceId;
		this.validExpr = validExpr;
		this.type = type;
		this.bySql = bySql;
		this.byDecrypt=byDecrypt;
		this.sql = sql;
		this.allowNullOrBlank = allowNullOrBlank;
		this.allowNodeNotExist = allowNodeNotExist;
		this.ignoreLowerCase = ignoreLowerCase;
		this.byLast = byLast;
	}
	public Param() {
		super();
	}
	
}
