package com.cte.credit.common.service;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;

import com.cte.credit.api.exception.ServiceException;
import com.cte.credit.common.domain.BaseDomain;
import com.cte.credit.common.web.RestResponse;

/**
 * 
 * 该类是所有业务逻辑处理的接口父类，定义了所有的实体都该具备的方法
 * 
 * 
 * 包含的方法为:
 * 新增对象
 * 删除对象
 * 查找对象
 * 更新对象
 * 对象的翻页查询
 * 
 */
public interface IBaseService<T extends BaseDomain> {

	public void delete(final String id) throws ServiceException;

	public T add(T domain) throws ServiceException;

	public void add(List<T> domain) throws ServiceException;

	public T save(T domain) throws ServiceException;

	public T merge(T domain) throws ServiceException;

	public void batchDelete(String[] ids) throws ServiceException;

	public T get(String id);

	public RestResponse query(Example example, Map<String, Criterion> criterions, Long startRow, Long endRow,
			String sortBy);

	public RestResponse query(T domain, Map<String, Criterion> criterions, Long startRow, Long endRow, String sortBy);

	public List<T> query(T example);

	public List<T> query(T example, Map<String, Criterion> criterions);

	public long getQueryCount();

	RestResponse createResponse(List<T> list);
}
