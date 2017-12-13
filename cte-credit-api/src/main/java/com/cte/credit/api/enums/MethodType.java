package com.cte.credit.api.enums;

public enum MethodType {
	synchronous("同步"),
	asynchronous("异步");
	String name;
	private MethodType(String name) {
		this.name=name;
	}
}
