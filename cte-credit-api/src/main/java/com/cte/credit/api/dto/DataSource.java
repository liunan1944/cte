package com.cte.credit.api.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
* @Title: 数据源模板定义
* @Package com.wanda.credit.api.iface.common.executor 
* @Description: 基本属性定义
* @author liunan1944@163.com   
* @date 2015年8月12日 下午12:16:36 
* @version V1.0
*/
public class DataSource implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String id;
	public String refProdCode;
	public String name;
	public Double price;
	public Integer level;
	public String  condition;
	public Boolean ignoreErr;
	public Boolean deliveNext;
	public List<Param> params_in;
	public String[] limit_out;
	public String[] limit_out_alias;//数据源输出假名设置，调用多数据源同key输出时使用 add by wanging 
	public Map<String,Object> params_out;
	public String[] getLimit_out_alias() {
		return limit_out_alias;
	}
	public void setLimit_out_alias(String[] limit_out_alias) {
		this.limit_out_alias = limit_out_alias;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRefProdCode() {
		return refProdCode;
	}
	public void setRefProdCode(String refProdCode) {
		this.refProdCode = refProdCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public Boolean getIgnoreErr() {
		return ignoreErr;
	}
	public void setIgnoreErr(Boolean ignoreErr) {
		this.ignoreErr = ignoreErr;
	}
	public Boolean getDeliveNext() {
		return deliveNext;
	}
	public void setDeliveNext(Boolean deliveNext) {
		this.deliveNext = deliveNext;
	}
	public Map<String, Object> getParams_out() {
		return params_out;
	}
	public void setParams_out(Map<String, Object> params_out) {
		this.params_out = params_out;
	}
	public String[] getLimit_out() {
		return limit_out;
	}
	public void setLimit_out(String[] limit_out) {
		this.limit_out = limit_out;
	}
	public List<Param> getParams_in() {
		return params_in;
	}
	public void setParams_in(List<Param> params_in) {
		this.params_in = params_in;
	}
	
}
