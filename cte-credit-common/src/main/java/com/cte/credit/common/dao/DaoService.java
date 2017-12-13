package com.cte.credit.common.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.ManyToOne;
import javax.sql.DataSource;
import javax.swing.tree.TreePath;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cte.credit.common.domain.BaseDomain;
import com.cte.credit.common.util.SpringStoredProcedure;
import com.cte.credit.common.util.StringUtil;
import com.cte.credit.common.web.RestResponse;

/**
 * 
 * 该类是 通用DAO类
 * 
 */
@Repository
@Transactional
public class DaoService {
	/**
	 * Logger
	 */
	protected final Log log = LogFactory.getLog(getClass());
	
	private static final String paramOut_key1 = "P_CODE";
	private static final String paramOut_key2 = "P_CUR";

	/**
	 * Hibernate SessionFactory which will be injected by Spring
	 */
	private SessionFactory sessionFactory;

	private DataSource dataSource;

	private JdbcTemplate jdbcTemplate;

	private boolean queryCacheable = false;

	public boolean isQueryCacheable() {
		return queryCacheable;
	}

	public void setQueryCacheable(boolean queryCacheable) {
		this.queryCacheable = queryCacheable;
	}

	/**
	 * Getter for sessionFactory
	 * 
	 * @return
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * Setter for sessionFactory
	 * 
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;

		this.dataSource = SessionFactoryUtils.getDataSource(this.sessionFactory);
		this.jdbcTemplate = new JdbcTemplate(this.dataSource);
	}

	/**
	 * Find the model by its primary key
	 * 
	 * @param clazz
	 *            model class
	 * @param pk
	 *            value of the primary key
	 * @return model whose primary key is matched with pk.
	 * @throws DataAccessException
	 *             if there is any problem or model is not found.
	 */
	public <T extends BaseDomain> T findByPrimaryKey(Class<T> clazz, Serializable pk, String... properties)
			throws DataAccessException {
		return (T) findByPrimaryKeyNative(clazz, pk, null, properties);
	}

	public <T extends BaseDomain> T findByPrimaryKey(Class<T> clazz, Serializable pk, LockMode lockMode,
			String... properties) throws DataAccessException {
		return (T) findByPrimaryKeyNative(clazz, pk, lockMode, properties);
	}

	/**
	 * Find the model by its primary key and initial it
	 * 
	 * @param clazz
	 *            model class
	 * @param pk
	 *            value of the primary key
	 * @return model whose primary key is matched with pk.
	 * @throws DataAccessException
	 *             if there is any problem or model is not found.
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseDomain> T findByPrimaryKeyInitialized(Class<T> clazz, Serializable pk)
			throws DataAccessException {
		BaseDomain result = findByPrimaryKeyNative(clazz, pk, null);
		try {
			Field[] fs = clazz.getDeclaredFields();
			for (Field f : fs) {
				if (f.getAnnotation(ManyToOne.class) != null) {
					String m = "get" + StringUtil.upcaseFirstLetter(f.getName());
					Method method = clazz.getMethod(m, (Class[]) null);
					Hibernate.initialize(method.invoke(result, (Object[]) null));
				}
			}
		} catch (NoSuchMethodException e) {
			log.error("Initial proxy object failed.");
		} catch (IllegalAccessException e) {
			log.error("Initial proxy object failed.");
		} catch (InvocationTargetException e) {
			log.error("Initial proxy object failed.");
		}

		return (T) result;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	private <T extends BaseDomain> T findByPrimaryKeyNative(Class<T> clazz, Serializable pk, LockMode lockMode,
			String... properties) throws DataAccessException {
		BaseDomain bm = null;
		Session sess = sessionFactory.getCurrentSession();
		if (log.isDebugEnabled()) {
			log.debug("get model " + clazz.getName() + " with pk:" + pk);
		}
		try {
			if (lockMode == null)
				bm = (BaseDomain) sess.load(clazz, StringEscapeUtils.escapeSql(pk.toString()));
			else
				bm = (BaseDomain) sess.load(clazz, StringEscapeUtils.escapeSql(pk.toString()), lockMode);
			log.debug("get model success:" + bm);
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}
		return (T) bm;
	}

	/**
	 * Find the model by its primary key. if no matched found, <b>null value</b>
	 * will returned.
	 * 
	 * @param clazz
	 *            model class
	 * @param pk
	 *            value of the primary key
	 * @return model whose primary key is matched with pk.
	 * @throws DataAccessException
	 *             if there is any problem.
	 */
	public <T extends BaseDomain> T getByPrimaryKey(Class<T> clazz, Serializable pk) throws DataAccessException {
		return (T) getByPrimaryKeyNative(clazz, pk);
	}

	/**
	 * Check whether model exists by provided primary key's value
	 * 
	 * @param clazz
	 *            model class
	 * @param pk
	 *            value of the primary key
	 * @return boolean value
	 * @throws DataAccessException
	 *             if there is any problem.
	 */
	public <T extends BaseDomain> boolean isExists(Class<T> clazz, Serializable pk) throws DataAccessException {
		BaseDomain bm = getByPrimaryKeyNative(clazz, pk);
		if (bm == null)
			return false;
		else
			return true;
	}

	@SuppressWarnings("unchecked")
	private <T extends BaseDomain> T getByPrimaryKeyNative(Class<T> clazz, Serializable pk) throws DataAccessException {
		BaseDomain bm = null;
		Session sess = sessionFactory.getCurrentSession();
		if (log.isDebugEnabled()) {
			log.debug("get model " + clazz.getName() + " with pk:" + pk);
		}
		try {
			bm = (BaseDomain) sess.get(clazz, StringEscapeUtils.escapeSql(pk.toString()));
			log.debug("get model success:" + bm);
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}
		return (T) bm;
	}

	/**
	 * Persist model
	 * 
	 * @param obj
	 * @return model which is persistent
	 * @throws DataAccessException
	 *             if there is any problem.
	 */
	public <T extends BaseDomain> T create(T obj) throws DataAccessException {
		try {
			Session sess = sessionFactory.getCurrentSession();
			sess.save(obj);
			sess.flush();
		} catch (HibernateException e) {
			log.error("unable to persist object", e);
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}
		return obj;
	}

	public <T extends BaseDomain> void create(List<T> obj) throws DataAccessException {
		try {
			Session sess = sessionFactory.getCurrentSession();
			int i = 0;
			for (T t : obj) {
				sess.save(t);
				if (i % 20 == 0) {
					sess.flush();
					sess.clear();
				}
				i++;
			}

		} catch (HibernateException e) {
			log.error("unable to persist object", e);
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}

	}

	/**
	 * Update transient model
	 * 
	 * @param obj
	 * @return model which is persistent
	 * @throws DataAccessException
	 *             if there is any problem.
	 */
	public <T extends BaseDomain> T update(T obj) throws DataAccessException {
		try {
			Session sess = sessionFactory.getCurrentSession();
			sess.update(obj);
			// sess.merge(obj);
			sess.flush();
		} catch (HibernateException e) {
			log.error("update object error", e);
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}
		return obj;
	}

	public <T extends BaseDomain> T saveOrUpdate(T obj) throws DataAccessException {
		try {
			Session sess = sessionFactory.getCurrentSession();
			sess.saveOrUpdate(obj);
			// sess.merge(obj);
			sess.flush();
		} catch (HibernateException e) {
			log.error("update object error", e);
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}
		return obj;
	}

	public <T extends BaseDomain> T merge(T obj) throws DataAccessException {
		try {
			Session sess = sessionFactory.getCurrentSession();
			sess.merge(obj);
			sess.flush();
		} catch (HibernateException e) {
			log.error("update object error", e);
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}
		return obj;
	}

	/**
	 * Delete persistent model
	 * 
	 * @param obj
	 * @throws DataAccessException
	 *             if there is any problem.
	 */
	public <T extends BaseDomain> void remove(T obj) throws DataAccessException {
		removeNative(obj);
	}

	/**
	 * Delete persistent model
	 * 
	 * @param clazz
	 *            model class
	 * @param pk
	 *            value of the primary key
	 * @throws DataAccessException
	 *             if there is any problem.
	 */
	public <T extends BaseDomain> void removeByPk(Class<T> clazz, Serializable pk) throws DataAccessException {
		removeNative(findByPrimaryKeyNative(clazz, pk, null));
	}

	private <T extends BaseDomain> void removeNative(T obj) throws DataAccessException {
		try {
			Session sess = sessionFactory.getCurrentSession();
			sess.delete(obj);
		} catch (HibernateException e) {
			log.error("remove object error", e);
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}
	}

	// public String getQueryLanguageByName(String queryName){
	// return namedQuerys.getQueryByName(queryName);
	// }

	/**
	 * Query using HQL
	 * 
	 * @param hqlName
	 *            named query
	 * @return collection result
	 * @throws DataAccessException
	 *             if there is any problem.
	 */
	public <T> List<T> findByNamedHQL(String hql) throws DataAccessException {
		return findByHQLNative(hql, null, -1, -1);
	}

	/**
	 * Query using HQL with parameter
	 * 
	 * @param hqlName
	 *            named query
	 * @param params
	 *            query parameter
	 * @return collection result
	 * @throws DataAccessException
	 *             if there is any problem.
	 */
	public <T> List<T> findByNamedHQL(String hql, Map<String, Object> params) throws DataAccessException {
		return findByHQLNative(hql, params, -1, -1);
	}

	/**
	 * Query using HQL with parameter
	 * 
	 * @param hqlName
	 *            named query
	 * @param params
	 *            query parameter
	 * @return collection result
	 * @throws DataAccessException
	 *             if there is any problem.
	 */
	public <T> List<T> findByNamedHQL(String hql, Object params) throws DataAccessException {
		return findByHQLNative(hql, params, -1, -1);
	}

	/**
	 * Query using HQL with pagenation
	 * 
	 * @param hqlName
	 *            named query
	 * @param begin
	 *            start place
	 * @param count
	 *            max record counts for fetching records
	 * @return collection result
	 * @throws DataAccessException
	 *             if there is any problem.
	 */
	public <T> List<T> findByNamedHQL(String hql, int begin, int count) throws DataAccessException {
		return findByHQLNative(hql, null, begin, count);
	}

	/**
	 * Query using HQL with pagenation and parameter
	 * 
	 * @param hqlName
	 *            named query
	 * @param params
	 *            query parameter
	 * @param begin
	 *            start place
	 * @param count
	 *            max record counts for fetching records
	 * @return collection result
	 * @throws DataAccessException
	 *             if there is any problem.
	 */
	public <T> List<T> findByNamedHQL(String hql, Map<String, Object> params, int begin, int count)
			throws DataAccessException {
		return findByHQLNative(hql, params, begin, count);
	}

	/**
	 * Query using HQL with pagenation and parameter
	 * 
	 * @param hqlName
	 *            named query
	 * @param params
	 *            query parameter
	 * @param begin
	 *            start place
	 * @param count
	 *            max record counts for fetching records
	 * @return collection result
	 * @throws DataAccessException
	 *             if there is any problem.
	 */
	public <T> List<T> findByNamedHQL(String hql, Object params, int begin, int count) throws DataAccessException {
		return findByHQLNative(hql, params, begin, count);
	}

	/**
	 * Query one model sqlQL
	 * 
	 * @param hql
	 *            named query
	 * @return matched model found. If no matched record, <b>null value</b> will
	 *         be returned.
	 * @throws DataAccessException
	 *             if there is any problem.
	 */
	public <T> T findOneByNamedHQL(String hql) throws DataAccessException {
		Collection<T> result = findByHQLNative(hql, null, -1, -1);
		if (result == null || result.size() == 0)
			return null;
		else
			return (T) result.iterator().next();
	}

	/**
	 * Query one model using HQL with parameter
	 * 
	 * @param hql
	 *            named query
	 * @param params
	 *            query parameter
	 * @return matched model found. If no matched record, <b>null value</b> will
	 *         be returned.
	 * @throws DataAccessException
	 *             if there is any problem.
	 */
	public <T> T findOneByNamedHQL(String hql, Map<String, Object> params) throws DataAccessException {
		Collection<T> result = findByHQLNative(hql, params, -1, -1);
		if (result == null || result.size() == 0)
			return null;
		else
			return (T) result.iterator().next();
	}

	/**
	 * Query one model using HQL with parameter
	 * 
	 * @param hql
	 *            named query
	 * @param params
	 *            query parameter
	 * @return matched model found. If no matched record, <b>null value</b> will
	 *         be returned.
	 * @throws DataAccessException
	 *             if there is any problem.
	 */
	public <T> T findOneByNamedHQL(String hql, Object params) throws DataAccessException {
		Collection<T> result = findByHQLNative(hql, params, -1, -1);
		if (result == null || result.size() == 0)
			return null;
		else
			return (T) result.iterator().next();
	}

	/**
	 * Query using HQL
	 * 
	 * @param hql
	 *            hibernate query language
	 * @return collection result
	 * @throws DataAccessException
	 *             if there is any problem.
	 */
	public <T> List<T> findByHQL(String hql) throws DataAccessException {
		return findByHQLNative(hql, null, -1, -1);
	}

	/**
	 * Query using HQL with parameter
	 * 
	 * @param hql
	 *            hibernate query language
	 * @param params
	 *            query parameter
	 * @return collection result
	 * @throws DataAccessException
	 *             if there is any problem.
	 */
	public <T> List<T> findByHQL(String hql, Map<String, Object> params) throws DataAccessException {
		return findByHQLNative(hql, params, -1, -1);
	}

	/**
	 * Query using HQL with parameter
	 * 
	 * @param hql
	 *            hibernate query language
	 * @param params
	 *            query parameter
	 * @return collection result
	 * @throws DataAccessException
	 *             if there is any problem.
	 */
	public <T> List<T> findByHQL(String hql, Object params) throws DataAccessException {
		return findByHQLNative(hql, params, -1, -1);
	}

	/**
	 * Query using HQL with pagenation
	 * 
	 * @param hql
	 *            hibernate query language
	 * @param begin
	 *            start place
	 * @param count
	 *            max record counts for fetching records
	 * @return collection result
	 * @throws DataAccessException
	 *             if there is any problem.
	 */
	public <T> List<T> findByHQL(String hql, int begin, int count) throws DataAccessException {
		return findByHQLNative(hql, null, begin, count);
	}

	/**
	 * Query using HQL with pagenation and parameter
	 * 
	 * @param hql
	 *            hibernate query language
	 * @param params
	 *            query parameter
	 * @param begin
	 *            start place
	 * @param count
	 *            max record counts for fetching records
	 * @return collection result
	 * @throws DataAccessException
	 *             if there is any problem.
	 */
	public <T> List<T> findByHQL(String hql, Map<String, Object> params, int begin, int count)
			throws DataAccessException {
		return findByHQLNative(hql, params, begin, count);
	}

	/**
	 * Query using HQL with pagenation and parameter
	 * 
	 * @param hql
	 *            hibernate query language
	 * @param params
	 *            query parameter
	 * @param begin
	 *            start place
	 * @param count
	 *            max record counts for fetching records
	 * @return collection result
	 * @throws DataAccessException
	 *             if there is any problem.
	 */
	public <T> List<T> findByHQL(String hql, Object params, int begin, int count) throws DataAccessException {
		return findByHQLNative(hql, params, begin, count);
	}

	/**
	 * Query one model using HQL
	 * 
	 * @param hql
	 *            hibernate query language
	 * @return matched model found. If no matched record, <b>null value</b> will
	 *         be returned.
	 * @throws DataAccessException
	 *             if there is any problem.
	 */
	public <T> T findOneByHQL(String hql) throws DataAccessException {
		Collection<T> result = findByHQLNative(hql, null, -1, -1);
		if (result == null || result.size() == 0)
			return null;
		else
			return (T) result.iterator().next();
	}

	/**
	 * Query one model using HQL with parameter
	 * 
	 * @param hql
	 *            hibernate query language
	 * @param params
	 *            query parameter
	 * @return matched model found. If no matched record, <b>null value</b> will
	 *         be returned.
	 * @throws DataAccessException
	 *             if there is any problem.
	 */
	public <T> T findOneByHQL(String hql, Map<String, Object> params) throws DataAccessException {
		Collection<T> result = findByHQLNative(hql, params, -1, -1);
		if (result == null || result.size() == 0)
			return null;
		else
			return (T) result.iterator().next();
	}

	/**
	 * Query one model using HQL with parameter
	 * 
	 * @param hql
	 *            hibernate query language
	 * @param params
	 *            query parameter
	 * @return matched model found. If no matched record, <b>null value</b> will
	 *         be returned.
	 * @throws DataAccessException
	 *             if there is any problem.
	 */
	public <T> T findOneByHQL(String hql, Object params) throws DataAccessException {
		Collection<T> result = findByHQLNative(hql, params, -1, -1);
		if (result == null || result.size() == 0)
			return null;
		else
			return (T) result.iterator().next();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> List<T> findByHQLNative(String hql, Map<String, Object> params, int begin, int count)
			throws DataAccessException {
		List<T> list = null;
		try {
			if (log.isDebugEnabled()) {
				log.debug("HQL Query:");
				log.debug(hql);
				if (params != null && params.size() > 0) {
					log.debug("parameters:");
					for (String key : params.keySet())
						log.debug(key + ":" + params.get(key));
				}
			}
			Session sess = sessionFactory.getCurrentSession();
			Query query = sess.createQuery(hql);
			query.setCacheable(queryCacheable);
			if (begin >= 0)
				query.setFirstResult(begin);
			if (count >= 0)
				query.setMaxResults(count);
			if (params != null && params.size() > 0) {
				Set paramKey = params.keySet();
				for (Iterator iterator = paramKey.iterator(); iterator.hasNext();) {
					String paramName = (String) iterator.next();
					Object paramValue = params.get(paramName);
					if (paramValue instanceof Collection) {
						query.setParameterList(paramName, (Collection) paramValue);
					} else {
						query.setParameter(paramName, paramValue);
					}
				}
			}
			list = query.list();
		} catch (HibernateException e) {
			log.error("HibernateException--find all object error", e);
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> findByHQLNative(String hql, Object params, int begin, int count) throws DataAccessException {
		List<T> list = null;
		try {
			Session sess = sessionFactory.getCurrentSession();
			Query query = sess.createQuery(hql);
			query.setCacheable(queryCacheable);
			if (begin >= 0)
				query.setFirstResult(begin);
			if (count >= 0)
				query.setMaxResults(count);
			if (params != null)
				query.setProperties(params);
			list = query.list();
		} catch (HibernateException e) {
			log.error("HibernateException--find all object error", e);
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}
		return list;
	}

	/**
	 * expose session for external use
	 * 
	 * @return
	 */
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * execute one sql or ddl. User will need manually evict hibernate's cache
	 * to resync.
	 * 
	 * @param sql
	 * @throws DataAccessException
	 *             if there is any problem.
	 */
	public void executeSql(String sql) throws DataAccessException {
		jdbcTemplate.execute(sql);
	}

	public void executeSqlBatch(String[] sqls) throws DataAccessException {
		for (String sql : sqls)
			jdbcTemplate.execute(sql);
	}

	/**
	 * execute one sql
	 * 
	 * @param sql
	 * @param conditions
	 * @throws DataAccessException
	 */
	public int executeSql(String sql, List<Object> conditions) throws DataAccessException {
		if (log.isDebugEnabled()) {
			log.debug("SQL:");
			log.debug(sql);
			if (conditions != null && conditions.size() > 0) {
				log.debug("conditions:");
				for (int i = 0; i < conditions.size(); i++)
					log.debug("P" + (i + 1) + "):" + conditions.get(i));
			}
		}
		if (conditions == null || conditions.size() == 0)
			return jdbcTemplate.update(sql);
		else
			return jdbcTemplate.update(sql, conditions.toArray());
	}

	public int executeNamedSql(String sql, List<Object> conditions) throws DataAccessException {
		if (log.isDebugEnabled()) {
			log.debug("SQL:");
			log.debug(sql);
			if (conditions != null && conditions.size() > 0) {
				log.debug("conditions:");
				for (int i = 0; i < conditions.size(); i++)
					log.debug("P" + (i + 1) + "):" + conditions.get(i));
			}
		}
		if (conditions == null || conditions.size() == 0)
			return jdbcTemplate.update(sql);
		else
			return jdbcTemplate.update(sql, conditions.toArray());
	}

	public int executeSql(String sql, List<Object> conditions, int[] argTypes) throws DataAccessException {
		if (log.isDebugEnabled()) {
			log.debug("SQL:");
			log.debug(sql);
			if (conditions != null && conditions.size() > 0) {
				log.debug("conditions:");
				for (int i = 0; i < conditions.size(); i++)
					log.debug("P" + (i + 1) + "):" + conditions.get(i));
			}
		}
		if (conditions == null || conditions.size() == 0)
			return jdbcTemplate.update(sql);
		else
			return jdbcTemplate.update(sql, conditions.toArray(), argTypes);
	}

	public int executeNamedSql(String sql, List<Object> conditions, int[] argTypes) throws DataAccessException {
		if (log.isDebugEnabled()) {
			log.debug("SQL:");
			log.debug(sql);
			if (conditions != null && conditions.size() > 0) {
				log.debug("conditions:");
				for (int i = 0; i < conditions.size(); i++)
					log.debug("P" + (i + 1) + "):" + conditions.get(i));
			}
		}
		if (conditions == null || conditions.size() == 0)
			return jdbcTemplate.update(sql);
		else
			return jdbcTemplate.update(sql, conditions.toArray(), argTypes);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> List<T> findAllBySqlNative1(String sql, Object[] conditions, RowMapper mapper) {
		if (log.isDebugEnabled()) {
			log.debug("SQL Query:");
			log.debug(sql);
			if (conditions != null && conditions.length > 0) {
				log.debug("conditions:");
				for (int i = 0; i < conditions.length; i++)
					log.debug("P" + (i + 1) + "):" + conditions[i]);
			}
		}
		if (conditions != null)
			return (List<T>) jdbcTemplate.query(sql, conditions, mapper);
		else
			return (List<T>) jdbcTemplate.query(sql, mapper);
	}

	public <T> List<T> findAllBySql(String sql, Object[] conditions, RowMapper<T> mapper) {
		return findAllBySqlNative1(sql, conditions, mapper);
	}

	public <T> List<T> findAllByOracleSql(String sql, Object[] conditions, RowMapper<T> mapper, int begin, int count) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from (select pagequery.*, rownum rowno from(");
		sb.append(sql);
		sb.append(") pagequery where rownum <" + (begin + count));
		sb.append(") where rowno >= " + begin);
		return findAllBySqlNative1(sb.toString(), conditions, mapper);
	}

	public <T> List<T> findAllByNamedSql(String sql, Object[] conditions, RowMapper<T> mapper) {
		return findAllBySqlNative1(sql, conditions, mapper);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> T findOneBySqlNative1(String sql, Object[] conditions, RowMapper mapper) {
		if (log.isDebugEnabled()) {
			log.debug("SQL Query:");
			log.debug(sql);
			if (conditions != null && conditions.length > 0) {
				log.debug("conditions:");
				for (int i = 0; i < conditions.length; i++)
					log.debug("P" + (i + 1) + "):" + conditions[i]);
			}
		}
		try {
			if (conditions != null)
				return (T) jdbcTemplate.queryForObject(sql, conditions, mapper);
			else
				return (T) jdbcTemplate.queryForObject(sql, mapper);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (IncorrectResultSizeDataAccessException e) {
			log.debug("query result has more than one record. First one will be returned.");
			Collection<T> result = null;
			if (conditions != null)
				result = jdbcTemplate.query(sql, conditions, mapper);
			else
				result = jdbcTemplate.query(sql, mapper);
			return result.iterator().next();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T findOneBySql(String sql, Object[] conditions, RowMapper mapper) {
		return (T) findOneBySqlNative1(sql, conditions, mapper);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T findOneByNamedSql(String sql, Object[] conditions, RowMapper mapper) {
		return (T) findOneBySqlNative1(sql, conditions, mapper);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> T findOneBySqlNative1(String sql, Object[] conditions, Class clazz) {
		if (log.isDebugEnabled()) {
			log.debug("SQL Query:");
			log.debug(sql);
			if (conditions != null && conditions.length > 0) {
				log.debug("conditions:");
				for (int i = 0; i < conditions.length; i++)
					log.debug("P" + (i + 1) + "):" + conditions[i]);
			}
		}
		try {
			if (conditions != null)
				return (T) jdbcTemplate.queryForObject(sql, conditions, clazz);
			else
				return (T) jdbcTemplate.queryForObject(sql, clazz);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (IncorrectResultSizeDataAccessException e) {
			log.debug("query result has more than one record. First one will be returned.");
			Collection<T> result = null;
			if (conditions != null)
				result = jdbcTemplate.query(sql, conditions, new InnerRowMapper1(null, clazz));
			else
				result = jdbcTemplate.query(sql, new InnerRowMapper1(null, clazz));
			return result.iterator().next();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T findOneBySql(String sql, Object[] conditions, Class clazz) {
		return (T) findOneBySqlNative1(sql, conditions, clazz);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T findOneByNamedSql(String sql, Object[] conditions, Class clazz) {
		return (T) findOneBySqlNative1(sql, conditions, clazz);
	}

	/**
	 * find records using sql
	 * 
	 * @param sql
	 * @param alias
	 *            column name
	 * @param clazz
	 *            return Class Type
	 * @return
	 */
	@Transactional(readOnly = true)
	public <T> List<T> findAllBySql(String sql, String alias) throws DataAccessException {
		return findAllBySqlNative(sql, alias, null);
	}

	/**
	 * find records using sql
	 * 
	 * @param sql
	 * @param alias
	 *            column name
	 * @param clazz
	 *            return Class Type
	 * @return
	 */
	@Transactional(readOnly = true)
	public <T> List<T> findAllBySql(String sql, String alias, Class<T> clazz) throws DataAccessException {
		return findAllBySqlNative(sql, alias, clazz);
	}

	/**
	 * find records using named sql
	 * 
	 * @param sql
	 * @param alias
	 *            column name
	 * @param clazz
	 *            return Class Type
	 * @return
	 */
	@Transactional(readOnly = true)
	public <T> List<T> findAllByNamedSql(String sql, String alias, Class<T> clazz) throws DataAccessException {
		return findAllBySqlNative(sql, alias, clazz);
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> findAllBySqlNative(String sql, String alias, Class<T> clazz) throws DataAccessException {
		if (log.isDebugEnabled()) {
			log.debug("SQL Query:");
			log.debug(sql);
		}
		return (List<T>) jdbcTemplate.query(sql, new InnerRowMapper1(alias, clazz));

	}

	/**
	 * find records using named sql
	 * 
	 * @param sql
	 * @param alias
	 *            column name
	 * @param clazz
	 *            return Class Type
	 * @return
	 */
	@Transactional(readOnly = true)
	public <T> List<T> findAllByNamedSql(String sql, List<Object> conditions, String alias, Class<T> clazz)
			throws DataAccessException {
		return findAllBySqlNative(sql, conditions, alias, clazz);
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> findAllBySqlNative(String sql, List<Object> conditions, String alias, Class<T> clazz)
			throws DataAccessException {
		List<T> objs = null;
		if (log.isDebugEnabled()) {
			log.debug("SQL Query:");
			log.debug(sql);
			if (conditions != null && conditions.size() > 0) {
				log.debug("conditions:");
				for (int i = 0; i < conditions.size(); i++) {
					log.debug("P" + (i + 1) + "):" + conditions.get(i));
				}
				objs = jdbcTemplate.query(sql, conditions.toArray(), new InnerRowMapper1(alias, clazz));
			} else {
				objs = jdbcTemplate.query(sql, new Object[0], new InnerRowMapper1(alias, clazz));
			}
		}
		return objs;

	}

	@SuppressWarnings("unchecked")
	private class InnerRowMapper1 extends BaseRowMapper {
		public <T> InnerRowMapper1(String alias, Class<T> clazz) {
			this.alias = alias;
			this.clazz = clazz;
		}

		private String alias;
		@SuppressWarnings("rawtypes")
		private Class clazz;

		public Object mapRow(ResultSet rs, int index) throws SQLException {
			if (alias != null) {
				return getResultFromRs(rs, alias, clazz);
			} else
				return getResultFromRs(rs, 1, clazz);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.swing.tree.RowMapper#getRowsForPaths(javax.swing.tree.TreePath
		 * [])
		 */
		@SuppressWarnings("unused")
		public int[] getRowsForPaths(TreePath[] arg0) {
			// TODO Auto-generated method stub
			return new int[0];
		}
	}

	/**
	 * find records using sql
	 * 
	 * @param sql
	 * @param alias
	 *            column list
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Map<String, Object>> findAllBySql(String sql, String[] alias) throws DataAccessException {
		return findAllBySqlNative(sql, alias, null);
	}

	/**
	 * find records using sql
	 * 
	 * @param sql
	 * @param alias
	 *            column list
	 * @param clazzes
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@Transactional(readOnly = true)
	public List<Map<String, Object>> findAllByNamedSql(String sql, String[] alias, Class[] clazzes)
			throws DataAccessException {
		return findAllBySqlNative(sql, alias, clazzes);
	}

	/**
	 * find records using sql
	 * 
	 * @param sql
	 * @param alias
	 *            column list
	 * @param clazzes
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@Transactional(readOnly = true)
	public List<Map<String, Object>> findAllBySql(String sql, String[] alias, Class[] clazzes)
			throws DataAccessException {
		return findAllBySqlNative(sql, alias, clazzes);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Map<String, Object>> findAllBySqlNative(String sql, String[] alias, Class[] clazzes)
			throws DataAccessException {
		if (log.isDebugEnabled()) {
			log.debug("SQL Query:");
			log.debug(sql);
		}
		return (List<Map<String, Object>>) jdbcTemplate.query(sql, new InnerRowMapper2(alias, clazzes));
	}

	@SuppressWarnings("unchecked")
	private class InnerRowMapper2 extends BaseRowMapper {
		@SuppressWarnings("rawtypes")
		public InnerRowMapper2(String[] alias, Class[] clazzes) {
			this.alias = alias;
			this.clazzes = clazzes;
		}

		private String[] alias;
		@SuppressWarnings("rawtypes")
		private Class[] clazzes;

		public Object mapRow(ResultSet rs, int index) throws SQLException {
			Map<String, Object> result = new HashMap<String, Object>();
			for (int i = 0; i < alias.length; i++) {
				result.put(alias[i], getResultFromRs(rs, alias[i], clazzes[i]));
			}
			return result;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> findAllBySql(String sql) throws DataAccessException {
		if (log.isDebugEnabled()) {
			log.debug("SQL Query:");
			log.debug(sql);
		}
		return (List<Object[]>) jdbcTemplate.query(sql, new InnerRowMapper4());
	}

	public List<Map<String, Object>> findAllBySqlForMap(String sql) throws DataAccessException {
		if (log.isDebugEnabled()) {
			log.debug("SQL Query:");
			log.debug(sql);
		}
		return jdbcTemplate.queryForList(sql);
	}
	
	private class InnerRowMapper4 extends BaseRowMapper {
		public InnerRowMapper4() {
		}

		public Object mapRow(ResultSet rs, int index) throws SQLException {
			int count = rs.getMetaData().getColumnCount();
			Object[] objs = new Object[count];
			for (int i = 0; i < count; i++)
				objs[i] = rs.getObject(i + 1);
			return objs;
		}
	}

	/**
	 * find records using sql
	 * 
	 * @param sql
	 * @param conditions
	 *            parameters which match the placehoulders(?) in sql
	 * @param alias
	 *            column list
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Map<String, Object>> findAllBySql(String sql, List<Object> conditions, String[] alias)
			throws DataAccessException {
		return findAllBySqlNative(sql, conditions, alias, null);
	}

	/**
	 * find records using sql
	 * 
	 * @param sql
	 * @param conditions
	 *            parameters which match the placehoulders(?) in sql
	 * @param alias
	 *            column list
	 * @param clazzes
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@Transactional(readOnly = true)
	public List<Map<String, Object>> findAllBySql(String sql, List<Object> conditions, String[] alias, Class[] clazzes)
			throws DataAccessException {
		return findAllBySqlNative(sql, conditions, alias, clazzes);
	}

	/**
	 * find records using sql
	 * 
	 * @param sql
	 * @param conditions
	 *            parameters which match the placehoulders(?) in sql
	 * @param alias
	 *            column list
	 * @param clazzes
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@Transactional(readOnly = true)
	public List<Map<String, Object>> findAllByNamedSql(String sql, List<Object> conditions, String[] alias,
			Class[] clazzes) throws DataAccessException {
		return findAllBySqlNative(sql, conditions, alias, clazzes);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Map<String, Object>> findAllBySqlNative(String sql, List<Object> conditions, String[] alias,
			Class[] clazzes) throws DataAccessException {
		List<Map<String, Object>> objs = null;
		if (log.isDebugEnabled()) {
			log.debug("SQL Query:");
			log.debug(sql);
			if (conditions != null && conditions.size() > 0) {
				log.debug("conditions:");
				for (int i = 0; i < conditions.size(); i++) {
					log.debug("P" + (i + 1) + "):" + conditions.get(i));
				}
				jdbcTemplate.query(sql, conditions.toArray(), new InnerRowMapper2(alias, clazzes));
			} else {
				objs = jdbcTemplate.query(sql, new Object[0], new InnerRowMapper2(alias, clazzes));
			}
		}
		return objs;
	}

	/**
	 * find one record using sql
	 * 
	 * @param sql
	 * @param alias
	 * @return
	 */
	@Transactional(readOnly = true)
	public Object findOneBySql(String sql, String alias) throws DataAccessException {
		Map<String, Object> result = findOneBySqlNative(sql, null, new String[] { alias }, null);
		if (result == null)
			return null;
		else
			return result.get(alias);
	}
	
	/**
	 * find one record using sql
	 * 
	 * @param sql
	 * @return
	 */
	@Transactional(readOnly = true)
	public Map<String, Object> findOneBySql(String sql) throws DataAccessException {
		if (log.isDebugEnabled()) {
			log.debug("SQL Query:");
			log.debug(sql);
		}
		return jdbcTemplate.queryForMap(sql);
	}
	
	/**
	 * fetch one record using sql
	 * 
	 * @param sql
	 * @return
	 */
	@Transactional(readOnly = true)
	public Map<String, Object> fetchOneBySql(String sql) throws DataAccessException {
		List<Map<String, Object>> list = null;
		if (log.isDebugEnabled()) {
			log.debug("SQL Query:");
			log.debug(sql);
		}
		list = this.findAllBySqlForMap(sql);
		if (list != null && list.size() > 0)
			return list.get(0);
		else
			return null;
	}

	/**
	 * 
	 * @param sql
	 * @param conditions
	 * @param alias
	 * @return
	 * @throws DataAccessException
	 */
	@Transactional(readOnly = true)
	public Object findOneBySql(String sql, List<Object> conditions, String alias) throws DataAccessException {
		Map<String, Object> result = findOneBySqlNative(sql, conditions, new String[] { alias }, null);
		if (result == null)
			return null;
		else
			return result.get(alias);
	}

	/**
	 * 
	 * @param sql
	 * @param alias
	 * @param clazz
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = true)
	public <T> T findOneBySql(String sql, String alias, Class clazz) throws DataAccessException {
		Map<String, Object> result = findOneBySqlNative(sql, null, new String[] { alias }, new Class[] { clazz });
		if (result == null)
			return null;
		else
			return (T) result.get(alias);
	}

	/**
	 * 
	 * @param sql
	 * @param conditions
	 * @param alias
	 * @param clazz
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = true)
	public <T> T findOneBySql(String sql, List<Object> conditions, String alias, Class clazz)
			throws DataAccessException {
		Map<String, Object> result = findOneBySqlNative(sql, conditions, new String[] { alias }, new Class[] { clazz });
		if (result == null)
			return null;
		else
			return (T) result.get(alias);
	}

	/**
	 * 
	 * @param sql
	 * @param alias
	 * @param clazz
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public <T> T findOneByNamedSql(String sql, String alias, Class<T> clazz) throws DataAccessException {
		Map<String, Object> result = findOneBySqlNative(sql, null, new String[] { alias }, new Class[] { clazz });
		if (result == null)
			return null;
		else
			return (T) result.get(alias);
	}

	/**
	 * 
	 * @param sql
	 * @param conditions
	 * @param alias
	 * @param clazz
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public <T> T findOneByNamedSql(String sql, List<Object> conditions, String alias, Class<T> clazz)
			throws DataAccessException {
		Map<String, Object> result = findOneBySqlNative(sql, conditions, new String[] { alias }, new Class[] { clazz });
		if (result == null)
			return null;
		else
			return (T) result.get(alias);
	}

	/**
	 * find one record using sql
	 * 
	 * @param sql
	 * @param alias
	 * @return
	 */
	@Transactional(readOnly = true)
	public Map<String, Object> findOneBySql(String sql, String[] alias) throws DataAccessException {
		return findOneBySqlNative(sql, null, alias, null);
	}

	/**
	 * 
	 * @param sql
	 * @param conditions
	 * @param alias
	 * @return
	 * @throws DataAccessException
	 */
	@Transactional(readOnly = true)
	public Map<String, Object> findOneBySql(String sql, List<Object> conditions, String[] alias)
			throws DataAccessException {
		return findOneBySqlNative(sql, conditions, alias, null);
	}

	/**
	 * find one record using sql
	 * 
	 * @param sql
	 * @param alias
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@Transactional(readOnly = true)
	public Map<String, Object> findOneBySql(String sql, String[] alias, Class[] clazzes) throws DataAccessException {
		return findOneBySqlNative(sql, null, alias, clazzes);
	}

	public <T> T findObjBySql(String sql, Object[] args, Class<T> requiredType) throws DataAccessException {
		return jdbcTemplate.queryForObject(sql, args, requiredType);
	}

	/**
	 * 
	 * @param sql
	 * @param conditions
	 * @param alias
	 * @param clazzes
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings({ "rawtypes" })
	@Transactional(readOnly = true)
	public Map<String, Object> findOneBySql(String sql, List<Object> conditions, String[] alias, Class[] clazzes)
			throws DataAccessException {
		return findOneBySqlNative(sql, conditions, alias, clazzes);
	}

	/**
	 * find one record using sql
	 * 
	 * @param sql
	 * @param alias
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@Transactional(readOnly = true)
	public Map<String, Object> findOneByNamedSql(String sql, String[] alias, Class[] clazzes)
			throws DataAccessException {
		return findOneBySqlNative(sql, null, alias, clazzes);
	}

	/**
	 * 
	 * @param sql
	 * @param conditions
	 * @param alias
	 * @param clazzes
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings({ "rawtypes" })
	@Transactional(readOnly = true)
	public Map<String, Object> findOneByNamedSql(String sql, List<Object> conditions, String[] alias, Class[] clazzes)
			throws DataAccessException {
		return findOneBySqlNative(sql, conditions, alias, clazzes);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, Object> findOneBySqlNative(String sql, List<Object> conditions, String[] alias, Class[] clazzes) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("SQL Query:");
				log.debug(sql);
				if (conditions != null && conditions.size() > 0) {
					log.debug("conditions:");
					for (int i = 0; i < conditions.size(); i++)
						log.debug("P" + (i + 1) + "):" + conditions.get(i));
				}
			}
			if (conditions != null)
				return (Map<String, Object>) jdbcTemplate.queryForObject(sql, conditions.toArray(),
						new InnerRowMapper2(alias, clazzes));
			else
				if(clazzes==null){
					clazzes=new Class[]{Object.class};
				}
				return (Map<String, Object>) jdbcTemplate.queryForObject(sql, new InnerRowMapper2(alias, clazzes));
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (IncorrectResultSizeDataAccessException e) {
			log.debug("query result has more than one record. First one will be returned.");
			Collection<Map<String, Object>> result = null;
			if (conditions != null)
				result = jdbcTemplate.query(sql, conditions.toArray(), new InnerRowMapper2(alias, clazzes));
			else
				result = jdbcTemplate.query(sql, new InnerRowMapper2(alias, clazzes));
			return result.iterator().next();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Object> findAllBySql(String sql, List<Object> conditions, int index, Class clazz) {
		List<Object> objs = null;
		if (log.isDebugEnabled()) {
			log.debug("SQL Query:");
			log.debug(sql);
			if (conditions != null && conditions.size() > 0) {
				log.debug("conditions:");
				for (int i = 0; i < conditions.size(); i++) {
					log.debug("P" + (i + 1) + "):" + conditions.get(i));
				}
				objs = jdbcTemplate.query(sql, conditions.toArray(), new InnerRowMapper3(index, clazz));
			} else {
				objs = jdbcTemplate.query(sql, new Object[0], new InnerRowMapper3(index, clazz));
			}
		}
		return objs;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T findOneBySql(String sql, List<Object> conditions, int index, Class clazz) {
		try {
			Object obj = null;
			if (log.isDebugEnabled()) {
				log.debug("SQL Query:");
				log.debug(sql);

				if (conditions != null && conditions.size() > 0) {
					log.debug("conditions:");
					for (int i = 0; i < conditions.size(); i++) {
						log.debug("P" + (i + 1) + "):" + conditions.get(i));
					}
					obj = jdbcTemplate.queryForObject(sql, conditions.toArray(), new InnerRowMapper3(index, clazz));
				} else {
					obj = jdbcTemplate.queryForObject(sql, null, new InnerRowMapper3(index, clazz));
				}
				log.debug("column Index:" + index);
			}
			return (T) obj;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	private class InnerRowMapper3 extends BaseRowMapper {
		public <T> InnerRowMapper3(int index, Class<T> clazz) {
			this.col = index;
			this.clazz = clazz;
		}

		private int col;
		@SuppressWarnings("rawtypes")
		private Class clazz;

		@SuppressWarnings("unchecked")
		public Object mapRow(ResultSet rs, int index) throws SQLException {
			return getResultFromRs(rs, col, clazz);
		}
	}

	/**
	 * 
	 * @param procedureName
	 * @param inParams
	 * @return
	 * @throws DataAccessException
	 */
	@SuppressWarnings({ "rawtypes" })
	public Map executeSP(StoredProcedure sp, Object... inParams) throws DataAccessException {
		sp.setDataSource(dataSource);
		sp.compile();
		return sp.execute(inParams);
	}

	/**
	 * 
	 * @param procedureName
	 * @param inParams
	 * @return list
	 * @desc 存储过程执行
	 * @throws DataAccessException
	 */
	public ArrayList<HashMap> executeProc(String procName,Map<String,Object> params){
		if(StringUtils.isEmpty(procName) || params.size()==0){
			return null;
		}
		SpringStoredProcedure ssp = new SpringStoredProcedure(
				dataSource, procName);
		Map<String, Object> in = new HashMap<String, Object>();
		for (Map.Entry<String,Object> param : params.entrySet()) {  			    
		    in.put(param.getKey().toString(),param.getValue().toString());
		    ssp.setParameter(param.getKey().toString(), java.sql.Types.VARCHAR);
		}  
		 
		ssp.setOutParameter(paramOut_key1,12);
		ssp.setOutParameter(paramOut_key2,-10);
		ssp.setFunction(false);		
		ssp.SetInParam(in);
		ssp.execute();
		ArrayList<HashMap> result = ssp.alist;
		return result;		
	}
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@SuppressWarnings({ "rawtypes" })
	public List retrieveByPage(String queryStr, int pageSize, int pageId) {
		return retrieveByPage(queryStr, null, pageSize, pageId);
	}

	@SuppressWarnings({ "rawtypes" })
	public List retrieveByPage(String queryStr, Map paramMap, int pageSize, int pageId, String... properties) {
		return query(queryStr, paramMap, pageSize * (pageId - 1), pageSize);

	}

	@SuppressWarnings({ "rawtypes" })
	public List query(String queryStr, int startRow, int endRow) {
		return query(queryStr, null, startRow, endRow);
	}

	@SuppressWarnings({ "rawtypes" })
	public List query(String queryStr, Map paramMap, int startRow, int endRow) {
		List results = null;
		Session session = SessionFactoryUtils.getSession(sessionFactory, false);

		if (log.isDebugEnabled()) {
			log.debug("Retrieve [" + queryStr + "]  startRow:[" + startRow + "] endRow:[" + endRow + "]");
		}
		Query query = session.createQuery(StringEscapeUtils.escapeSql(queryStr));

		if (paramMap != null) {
			Set paramKey = paramMap.keySet();
			for (Iterator iterator = paramKey.iterator(); iterator.hasNext();) {
				String paramName = (String) iterator.next();
				Object paramValue = paramMap.get(paramName);
				if (paramValue instanceof Collection) {
					query.setParameterList(paramName, (Collection) paramValue);
				} else {
					query.setParameter(paramName, paramValue);
				}
			}
		}
		query.setFirstResult(startRow);

		query.setMaxResults(endRow);

		long ts1 = System.currentTimeMillis();
		results = query.list();
		long ts2 = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("result records count: " + results.size() + " time used: " + (ts2 - ts1) + "ms");
		}

		return results;

	}

	public long getTotalCount(String hql, Map<String, Object> map) {
		try {
			hql = getCountQueryHqlByHql(hql);

			Long count = findOneByHQL(hql, map);

			return count;
		} catch (RuntimeException re) {
			throw re;
		}

	}

	private String getCountQueryHqlByHql(String hql) {
		if (hql == null)
			return null;
		String lowercaseOQL = hql.toLowerCase();
		int delim1 = lowercaseOQL.indexOf("from");
		int delim2 = lowercaseOQL.indexOf("order by");
		if (delim1 < 0) {
			if (log.isDebugEnabled()) {
				log.debug("It seemed that current hql is not one valid one.");
				log.debug("HQL:" + hql);
			}
			return null;
		}
		if (delim2 == -1)
			delim2 = hql.length();
		String fieldlist = hql.substring(7, delim1 - 1);
		int delim3 = fieldlist.indexOf(",");
		if (delim3 == -1)
			delim3 = fieldlist.length();
		String countOQL = "select count(" + fieldlist.substring(0, delim3) + ") " + hql.substring(delim1, delim2);
		log.debug("Count OQL:" + countOQL);
		return countOQL;
	}

	public RestResponse createResponse(List<? extends BaseDomain> list) {
		RestResponse result = new RestResponse();
		result.setStartRow(0);
		result.setEndRow(list.size() - 1);
		result.setTotalRows(list.size());
		result.setData(list);
		return result;
	}

	public RestResponse queryByCriteria(Class<? extends BaseDomain> persistentClass, Long startRow, Long endRow,
			Example example, Map<String, Criterion> criterions, String sortBy) {

		RestResponse result = new RestResponse();

		Criteria criteria = getSession().createCriteria(persistentClass);
		if (example != null) {
			criteria.add(example);
		}
		if (criterions != null) {
			for (String key : criterions.keySet()) {
				Criterion c = criterions.get(key);
				if (c instanceof Example) {
					String[] keys = StringUtils.split(key, ".");
					Criteria sc = criteria;
					for (String s : keys) {
						sc = sc.createCriteria(s);
					}

					sc.add(c);

				} else {
					criteria.add(c);
				}
			}
		}
		Object rowCount = criteria.setProjection(Projections.rowCount()).uniqueResult();
		long totalRows = 0L;

		if ((rowCount instanceof Integer))
			totalRows = ((Integer) rowCount).intValue();
		else if ((rowCount instanceof Long)) {
			totalRows = ((Long) rowCount).longValue();
		}

		criteria = getSession().createCriteria(persistentClass);
		if (example != null) {
			criteria.add(example);
		}
		if (criterions != null) {
			for (String key : criterions.keySet()) {
				Criterion c = criterions.get(key);
				if (c instanceof Example) {
					String[] keys = StringUtils.split(key, ".");
					Criteria sc = criteria;
					for (String s : keys) {
						sc = sc.createCriteria(s);
					}
					sc.add(c);
				} else {
					criteria.add(c);
				}
			}
		}

		if (startRow != null && endRow != null) {
			endRow = Math.min((long) endRow, totalRows);

			criteria.setFirstResult(startRow.intValue());
			criteria.setMaxResults(endRow.intValue() - startRow.intValue());

			result.setStartRow(startRow.intValue());

			result.setEndRow(endRow.intValue());
			result.setTotalRows(totalRows);
		}

		if (sortBy != null)
			criteria.addOrder(sortBy.startsWith("-") ? Order.desc(sortBy.substring(1)) : Order.asc(sortBy));

		result.setData(criteria.list());

		return result;
	}

	public List<Map<String, Object>> findAllByOrgSql(String sql, Object[] params) throws SQLException {
		//return jdbcTemplate.queryForList(StringEscapeUtils.escapeSql(sql), params);
		return jdbcTemplate.queryForList(sql, params);
	}

	// 判断被指定的数据是否存在,如果存在返回 true
	public boolean exists(String hql, Map<String, Object> params) {
		Number count = 0;
		Session sess = sessionFactory.getCurrentSession();
		Query query = sess.createQuery(hql);
		query.setCacheable(queryCacheable);
		if (params != null && params.size() > 0) {
			Set<String> paramKey = params.keySet();
			for (Iterator<String> iterator = paramKey.iterator(); iterator.hasNext();) {
				String paramName = (String) iterator.next();
				Object paramValue = params.get(paramName);
				if (paramValue instanceof Collection) {
					query.setParameterList(paramName, (Collection<?>) paramValue);
				} else {
					query.setParameter(paramName, paramValue);
				}
			}
		}
		if (query.uniqueResult() != null) {
			count = (Number) query.uniqueResult();
		}
		return count.intValue() > 0;
	}

	@SuppressWarnings("unchecked")
	public <T extends BaseDomain> T getByProperty(Class<T> clazz, String propertyName, Object value) {
		Session sess = sessionFactory.getCurrentSession();
		Criteria criteria = sess.createCriteria(clazz);
		return (T) createCriteria(criteria, Restrictions.eq(propertyName, value)).uniqueResult();
	}

	private Criteria createCriteria(Criteria criteria, final Criterion... criterions) {
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}
	
}
