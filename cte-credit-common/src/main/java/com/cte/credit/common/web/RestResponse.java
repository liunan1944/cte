package com.cte.credit.common.web;

import java.io.Serializable;
/**
* @Title: Rest 请求响应类
* @Package com.cte.common.web 
* @author liunan1944@163.com   
* @date 2017年12月5日 下午4:19:11 
* @version V1.0
 */
public class RestResponse implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean fromOfflineCache;
	private boolean invalidateCache;
	private Object clientContent;
	private Object data;
	private Object errors;
	private int endRow;
	private int startRow;
	private long offlineTimestamp;
	private int status;
	private long totalRows;
	/**
	 * @return the fromOfflineCache
	 */
	public boolean isFromOfflineCache() {
		return fromOfflineCache;
	}
	/**
	 * @param fromOfflineCache the fromOfflineCache to set
	 */
	public void setFromOfflineCache(boolean fromOfflineCache) {
		this.fromOfflineCache = fromOfflineCache;
	}
	/**
	 * @return the invalidateCache
	 */
	public boolean isInvalidateCache() {
		return invalidateCache;
	}
	/**
	 * @param invalidateCache the invalidateCache to set
	 */
	public void setInvalidateCache(boolean invalidateCache) {
		this.invalidateCache = invalidateCache;
	}
	/**
	 * @return the clientContent
	 */
	public Object getClientContent() {
		return clientContent;
	}
	/**
	 * @param clientContent the clientContent to set
	 */
	public void setClientContent(Object clientContent) {
		this.clientContent = clientContent;
	}
	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}
	/**
	 * @return the errors
	 */
	public Object getErrors() {
		return errors;
	}
	/**
	 * @param errors the errors to set
	 */
	public void setErrors(Object errors) {
		this.errors = errors;
	}
	/**
	 * @return the endRow
	 */
	public int getEndRow() {
		return endRow;
	}
	/**
	 * @param endRow the endRow to set
	 */
	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}
	/**
	 * @return the startRow
	 */
	public int getStartRow() {
		return startRow;
	}
	/**
	 * @param startRow the startRow to set
	 */
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	/**
	 * @return the offlineTimestamp
	 */
	public long getOfflineTimestamp() {
		return offlineTimestamp;
	}
	/**
	 * @param offlineTimestamp the offlineTimestamp to set
	 */
	public void setOfflineTimestamp(long offlineTimestamp) {
		this.offlineTimestamp = offlineTimestamp;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return the totalRows
	 */
	public long getTotalRows() {
		return totalRows;
	}
	/**
	 * @param totalRows the totalRows to set
	 */
	public void setTotalRows(long totalRows) {
		this.totalRows = totalRows;
	}

}
