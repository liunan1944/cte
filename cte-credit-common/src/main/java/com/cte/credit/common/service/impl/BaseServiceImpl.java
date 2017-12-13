package com.cte.credit.common.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;



import com.cte.credit.api.exception.ServiceException;
import com.cte.credit.common.dao.DaoService;
import com.cte.credit.common.domain.BaseDomain;
import com.cte.credit.common.service.IBaseService;
import com.cte.credit.common.util.ReflectionUtils;
import com.cte.credit.common.web.RestResponse;

/**
 * 
 * 该类是 业务逻辑类的抽象父类，该类中已包含各实体所需的基本方法
 * 
 */
@Transactional(rollbackFor = ServiceException.class)
public abstract class BaseServiceImpl<T extends BaseDomain> implements Serializable, IBaseService<T> {
	/**
	 *
	 */
	protected final Logger log = Logger.getLogger(getClass());

	private static final long serialVersionUID = -7038118274908662502L;

	@Autowired
	protected DaoService daoService;

	private Class<T> clazz;

	public BaseServiceImpl() {
		clazz = ReflectionUtils.getClassGenricType(getClass());
	}

	public void setDaoService(DaoService daoService) {
		this.daoService = daoService;
	}

	public RestResponse createResponse(List<T> list) {
		return daoService.createResponse(list);
	}

	public void delete(final String id) throws ServiceException {
		try {
			daoService.removeByPk(clazz, id);
		} catch (Exception e) {
			throw new ServiceException(e);
		}

	}

	public T merge(T domain) throws ServiceException {
		try {
			domain = daoService.merge(domain);
			return domain;
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	public T save(T domain) throws ServiceException {
		try {
			domain = daoService.saveOrUpdate(domain);
			return domain;
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	public T add(T domain) throws ServiceException {
		try {
			domain = daoService.create(domain);
			return domain;
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	public void add(List<T> domain) throws ServiceException {
		try {
			daoService.create(domain);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw new ServiceException(e);
		}
	}

	public void batchDelete(final String[] ids) throws ServiceException {
		try {
			for (String id : ids) {
				daoService.removeByPk(clazz, id);
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw new ServiceException(e);
		}

	}

	public T get(final String id) {
		return daoService.getByPrimaryKey(clazz, id);
	}

	@SuppressWarnings("unchecked")
	public List<T> query(int startRow, final int endRow) {
		final List<T> results = daoService.query("From " + clazz.getName(), null, startRow, endRow);
		return results;
	}

	public RestResponse query(Example example, Map<String, Criterion> criterions, Long startRow, Long endRow,
			String sortBy) {

		return daoService.queryByCriteria(clazz, startRow, endRow, example, criterions, sortBy);
	}

	public RestResponse query(T domain, Map<String, Criterion> criterions, Long startRow, Long endRow, String sortBy) {
		Example example = Example.create(domain).enableLike(MatchMode.ANYWHERE).ignoreCase();

		return this.query(example, criterions, startRow, endRow, sortBy);
	}

	public List<T> query(T obj) {
		return this.query(obj, null, null);
	}

	public List<T> query(Example example) {
		return this.query(example, null);
	}

	@SuppressWarnings("unchecked")
	public List<T> query(Example example, Map<String, Criterion> criterions) {
		RestResponse response = daoService.queryByCriteria(clazz, null, null, example, criterions, null);

		return (List<T>) response.getData();
	}
	public List<T> query(T obj, Map<String, Criterion> criterions) {
		return this.query(obj, criterions ,MatchMode.ANYWHERE);
	}
	public List<T> query(T obj, Map<String, Criterion> criterions,MatchMode model) {
		Example example = null;
		if (obj != null) {
			if(model!=null)
				example = Example.create(obj).enableLike(model);
			else
				example = Example.create(obj);
		}
		return query(example, criterions);
	}
	public long getQueryCount() {
		final long total = daoService.getTotalCount("Select obj From " + clazz.getName() + " obj", null);
		return total;
	}

}
