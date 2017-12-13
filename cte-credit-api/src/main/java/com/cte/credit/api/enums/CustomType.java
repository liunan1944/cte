package com.cte.credit.api.enums;

public enum CustomType {
	nomal("常规产品"),
	seeker("爬虫类产品");
	String name;
	private CustomType(String name) {
		this.name=name;
	}
}
