package com.cte.credit.api.exception;


public class ServiceException extends Exception {
	static final long serialVersionUID = -7034897190745766939L;
	private long errorCode;
	private String retValue;

	public String retValue() {
		return retValue;
	}

	public void retValue(String value) {
		this.retValue = value;
	}

	/**
	 * default construct
	 */
	public ServiceException() {
		super();
		this.errorCode = 0l;
	}

	/**
	 * override parent contrutor and set error code with zero;
	 * 
	 * @param message
	 *            your message;
	 */
	public ServiceException(String message) {
		super(message);
		this.errorCode = 0l;
		retValue = message;
	}

	/**
	 * override parent contrutor
	 * 
	 * @param message
	 *            your message
	 * @param errorCode
	 *            your code(must more than one)
	 */
	public ServiceException(String message, long errorCode) {
		super(message);
		this.errorCode = errorCode;
		retValue = message;
	}

	/**
	 * override parent contrutor
	 * 
	 * @param errorCode
	 *            your code(must more than one)
	 */
	public ServiceException(long errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public ServiceException(long errorCode, long errorCode2) {
		super();
		this.errorCode = errorCode;
	}

	/**
	 * override parent contrutor
	 * 
	 * @param message
	 *            your message
	 * @param cause
	 *            your cause
	 */
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
		this.errorCode = 0l;
		retValue = message;
	}

	/**
	 * override parent contrutor
	 * 
	 * @param cause
	 *            your cause
	 */
	public ServiceException(Throwable cause) {
		super(cause);
		this.errorCode = 0l;
	}

	/**
	 * override parent contrutor
	 * 
	 * @param message
	 *            your message
	 * @param errorCode
	 *            your code(must more than one)
	 * @param cause
	 *            your cause
	 */
	public ServiceException(String message, long errorCode, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
		retValue = message;
	}

	/**
	 * error code getter
	 * 
	 * @return your code(must more than one)
	 */
	public long getErrorCode() {
		return errorCode;
	}

	/**
	 * error code setter
	 * 
	 * @param errorCode
	 *            your code(must more than one)
	 */
	public void setErrorCode(long errorCode) {
		this.errorCode = errorCode;
	}
}
