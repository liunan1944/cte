package com.cte.credit.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于产品调用的时候，标识它是一个产品拦截器
 */
@Retention(RetentionPolicy.RUNTIME) //注解会在class中存在，运行时可通过反射获取
@Target(ElementType.TYPE)			//目标是类
@Documented							//文档生成时，该注解将被包含在javadoc中，可去掉
public @interface DataSourceClass{
	public String bindingDataSourceId(); //绑定的DS id
}

