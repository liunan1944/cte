package com.cte.credit.common.log.prod;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.cte.credit.api.dto.CRSCoreRequest;
import com.cte.credit.api.dto.CRSCoreResponse;
import com.cte.credit.common.dao.DaoService;
import com.cte.credit.common.template.PropertyUtil;
import com.cte.credit.common.util.DESUtils;
import com.cte.credit.common.util.StringUtil;

/**
 * 数据源ds日志插入工具类
 * */
@Service
public class ProductLogUtil {
	private static Logger logger = LoggerFactory.getLogger(ProductLogUtil.class);
	@Autowired
	protected DaoService daoService;
	@Autowired
	private DESUtils desService;
	@Autowired
	PropertyUtil propertyEngine;

	@Async
	public void writeReqLog(final String trade_id,CRSCoreRequest req) {
		logger.info("{}交易请求保存开始...", trade_id);
		final String encryptWords = propertyEngine.readById("sys_public_encode_keys");
		final String des_key = propertyEngine.readById("sys_public_des_key");
		String sql = "INSERT INTO CPDB_DS.T_SYS_REQ_HEADER("
				+"ID,"
				+ "REQUST_SN ,"
				+ "TRADE_ID ,"
				+ "VERSION ,"
				+ "ACCT_ID ,"
				+ "MAC_ADDRESS ,"
				+ "IP_ADDRESS ,"
				+ "OPERID ,"
				+ "REQ_TIME ,"
				+ "PROD_ID) VALUES(CPDB_DS.SEQ_SYS_REQ_HEADER.NEXTVAL,?,?,?,?,?,?,?,?,?)";
		this.daoService.getJdbcTemplate().update(sql, new Object[]{	
				req.getRequst_sn(),
				trade_id,
				req.getVersion(),
				req.getAcct_id(),
				req.getMac_address(),
				req.getIp_address(),
				req.getOperid(),
				new Date(),
				req.getProdId()	
		});
		if(req.getParams()!=null){
			sql = "INSERT INTO CPDB_DS.T_SYS_REQ_PARAM(ID,TRADE_ID,KEY_CODE,VALUE) "
					+ "VALUES(CPDB_DS.SEQ_SYS_REQ_PARAM.NEXTVAL,?,?,?)";
			final Map<String, Object> params = new HashMap<String, Object>();
			params.putAll(req.getParams());
			final String[] keys = params.keySet().toArray(new String[params.size()]);
			this.daoService.getJdbcTemplate().batchUpdate(sql,
	                new BatchPreparedStatementSetter() {
	                    @Override
	                    public int getBatchSize() {
	                        return keys.length;
	                    }
	                    @Override
	                    public void setValues(PreparedStatement ps, int i)
	                            throws SQLException {
	                        ps.setString(1, trade_id);
	                        ps.setString(2, keys[i].toString());
	                        String value = params.get(keys[i])!=null ? params.get(keys[i]).toString() : null;
							if (!StringUtil.isEmpty(value) &&
									ArrayUtils.contains(encryptWords.split(","),keys[i].toString())) {
								try {
									value = desService.encode(des_key,value);
									if(value.length()>=4000){
										value = "";
									}
								} catch (Exception e) {
									logger.error("目标值 {} 加密异常,具体原因：{}", value,
											e);
								}
							}
	                        ps.setString(3, value);
	                    }

	                });
		}
		logger.info("{}交易请求保存结束...", trade_id);
	}
	@Async
	public void writeRspLog(final String trade_id,final String prod_id,CRSCoreResponse rsp, Long cost) {
		logger.info("{}交易响应保存开始...", trade_id);
		final String encryptWords = propertyEngine.readById("sys_public_encode_keys");
		final String des_key = propertyEngine.readById("sys_public_des_key");

				String sql = "INSERT INTO CPDB_DS.T_SYS_RSP_HEADER("
						+"ID,"
						+ "RESPONSE_SN ,"
						+ "TRADE_ID ,"
						+ "VERSION ,"
						+ "RET_DATE ,"
						+ "RET_CODE ,"
						+ "RET_MSG ,"
						+ "TIME_COST ,"
						+ "IFACE_TAG ,"
						+ "DS_TAG) VALUES(CPDB_DS.SEQ_SYS_RSP_HEADER.NEXTVAL,?,?,?,?,?,?,?,?,?)";
				this.daoService.getJdbcTemplate().update(sql, new Object[]{
						rsp.getResponse_sn(),
						trade_id,
						rsp.getVersion(),
						rsp.getRetdate(),
						rsp.getRetcode(),
						rsp.getRetmsg(),
						cost,
						rsp.getIface_tags(),
						rsp.getDs_tags()
				});
				if(rsp.getRetdata()!=null){
					sql = "INSERT INTO CPDB_DS.T_SYS_RSP_PARAM(ID,TRADE_ID,KEY_CODE,VALUE) "
							+ "VALUES(CPDB_DS.SEQ_SYS_RSP_PARAM.NEXTVAL,?,?,?)";
					final Map<String, Object> params = new HashMap<String, Object>();
					params.putAll(rsp.getRetdata());
					final String[] keys = params.keySet().toArray(new String[params.size()]);
					this.daoService.getJdbcTemplate().batchUpdate(sql,
			                new BatchPreparedStatementSetter() {
			                    @Override
			                    public int getBatchSize() {
			                        return keys.length;
			                    }
			                    @Override
			                    public void setValues(PreparedStatement ps, int i)
			                            throws SQLException {
			                        ps.setString(1, trade_id);
			                        ps.setString(2, keys[i].toString());
									String value = params.get(keys[i]) != null ? params
											.get(keys[i]).toString() : null;
									if (!StringUtil.isEmpty(value)
											&& ArrayUtils.contains(encryptWords.split(","),keys[i].toString())) {
										try {
											value = desService.encode(des_key,value);
											if(value.length()>=4000){
												value = "";
											}
										} catch (Exception e) {
											logger.error("目标值 {} 加密异常,具体原因：{}", value,
													e);
										}
									}
			                        ps.setString(3, value);
			                    }

			                });
			}
	 logger.info("{}交易响应保存结束...", trade_id);
	}
}
