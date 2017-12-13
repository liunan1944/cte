package com.cte.credit.api.iface;

import java.util.Map;

import com.cte.credit.api.dto.DataSource;

public interface IDataSourceService {
	/**
	 * 数据源服务接口
	 * @param trade_id
	 * @param ds
	 * @return
	 * @throws Exception
	 */
	public abstract Map<String, Object> fetch(String trade_id,DataSource ds) throws Exception;
}
