package com.cte.credit.common.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;

/**
 * 
 * 该类是 JDBC返回结果的隐射抽象类
 *
 */
@SuppressWarnings("rawtypes")
public abstract class BaseRowMapper implements RowMapper {
	public static final Log log = LogFactory.getLog(BaseRowMapper.class);

	public abstract Object mapRow(ResultSet rs, int index) throws SQLException;

	@SuppressWarnings("unchecked")
	protected <T> T getResultFromRs(ResultSet rs, String alias, Class<T> clazz) throws SQLException{
		log.debug("get Result from : [Column]" + alias + " Class:" + clazz);
		if(clazz == null)
			return (T) rs.getObject(alias);
		else{
			if(rs.getObject(alias) == null) return null;
			if(clazz.toString().equals(String.class.toString()))
				return (T) rs.getString(alias);
			else if(clazz.toString().equals(java.math.BigDecimal.class.toString()))
				return (T) rs.getBigDecimal(alias);
			else if(clazz.toString().equals(java.io.InputStream.class.toString()))
				return (T) rs.getBinaryStream(alias);
			else if(clazz.toString().equals(java.sql.Blob.class.toString()))
				return (T) rs.getBlob(alias);
			else if(clazz.toString().equals(Boolean.class.toString()))
				return (T) new Boolean(rs.getBoolean(alias));
			else if(clazz.toString().equals(Byte.class.toString()))
				return (T) new Byte(rs.getByte(alias));
			else if(clazz.toString().equals(Byte[].class.toString()))
				return (T) rs.getBytes(alias);
			else if(clazz.toString().equals(java.io.Reader.class.toString()))
				return (T) rs.getCharacterStream(alias);
			else if(clazz.toString().equals(java.sql.Clob.class.toString()))
				return (T) rs.getClob(alias);
			else if(clazz.toString().equals(java.sql.Date.class.toString()))
				return (T) rs.getDate(alias);
			else if(clazz.toString().equals(Double.class.toString()))
				return (T) new Double(rs.getDouble(alias));
			else if(clazz.toString().equals(Float.class.toString()))
				return (T) new Float(rs.getFloat(alias));
			else if(clazz.toString().equals(Integer.class.toString()))
				return (T) new Integer(rs.getInt(alias));
			else if(clazz.toString().equals(Long.class.toString()))
				return (T) new Long(rs.getLong(alias));
			else if(clazz.toString().equals(Short.class.toString()))
				return (T) new Short(rs.getShort(alias));
			else if(clazz.toString().equals(java.sql.Time.class.toString()))
				return (T) rs.getTime(alias);
			else if(clazz.toString().equals(java.sql.Timestamp.class.toString()))
				return (T) rs.getTimestamp(alias);
			else if(clazz.toString().equals(java.util.Date.class.toString()))
				return (T) (rs.getTimestamp(alias) == null ? null :new java.util.Date(rs.getTimestamp(alias).getTime()));
			else
				return (T) rs.getObject(alias);
		}
	}

	@SuppressWarnings("unchecked")
	protected <T> T getResultFromRs(ResultSet rs, int icolumn, Class<T> clazz) throws SQLException{
		log.debug("get Result from : [Column]" + icolumn + " Class:" + clazz);
		if(clazz == null)
			return (T) rs.getObject(icolumn);
		else{
			if(rs.getObject(icolumn) == null) return null;
			if(clazz.toString().equals(String.class.toString()))
				return (T) rs.getString(icolumn);
			else if(clazz.toString().equals(java.math.BigDecimal.class.toString()))
				return (T) rs.getBigDecimal(icolumn);
			else if(clazz.toString().equals(java.io.InputStream.class.toString()))
				return (T) rs.getBinaryStream(icolumn);
			else if(clazz.toString().equals(java.sql.Blob.class.toString()))
				return (T) rs.getBlob(icolumn);
			else if(clazz.toString().equals(Boolean.class.toString()))
				return (T) new Boolean(rs.getBoolean(icolumn));
			else if(clazz.toString().equals(Byte.class.toString()))
				return (T) new Byte(rs.getByte(icolumn));
			else if(clazz.toString().equals(Byte[].class.toString()))
				return (T) rs.getBytes(icolumn);
			else if(clazz.toString().equals(java.io.Reader.class.toString()))
				return (T) rs.getCharacterStream(icolumn);
			else if(clazz.toString().equals(java.sql.Clob.class.toString()))
				return (T) rs.getClob(icolumn);
			else if(clazz.toString().equals(java.sql.Date.class.toString()))
				return (T) rs.getDate(icolumn);
			else if(clazz.toString().equals(Double.class.toString()))
				return (T) new Double(rs.getDouble(icolumn));
			else if(clazz.toString().equals(Float.class.toString()))
				return (T) new Float(rs.getFloat(icolumn));
			else if(clazz.toString().equals(Integer.class.toString()))
				return (T) new Integer(rs.getInt(icolumn));
			else if(clazz.toString().equals(Long.class.toString()))
				return (T) new Long(rs.getLong(icolumn));
			else if(clazz.toString().equals(Short.class.toString()))
				return (T) new Short(rs.getShort(icolumn));
			else if(clazz.toString().equals(java.sql.Time.class.toString()))
				return (T) rs.getTime(icolumn);
			else if(clazz.toString().equals(java.sql.Timestamp.class.toString()))
				return (T) rs.getTimestamp(icolumn);
			else if(clazz.toString().equals(java.util.Date.class.toString()))
				return (T) (rs.getTimestamp(icolumn) == null ? null :new java.util.Date(rs.getTimestamp(icolumn).getTime()));
			else
				return (T) rs.getObject(icolumn);
		}
	}


	protected Long getLongWithNull(ResultSet rs, String alias) throws SQLException{
		long value = rs.getLong(alias);
		if(value == 0l) return null;
		return value;
	}


	protected Integer getIntegerWithNull(ResultSet rs, String alias) throws SQLException{
		int value = rs.getInt(alias);
		if(value == 0) return null;
		return value;
	}
}
