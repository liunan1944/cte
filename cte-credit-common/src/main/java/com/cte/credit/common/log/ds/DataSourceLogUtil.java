package com.cte.credit.common.log.ds;

import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.cte.credit.common.dao.DaoService;
import com.cte.credit.common.log.ds.vo.DataSourceLogVO;
import com.cte.credit.common.log.ds.vo.DataSourceReqVO;
import com.cte.credit.common.template.PropertyUtil;
import com.cte.credit.common.util.DESUtils;
import com.cte.credit.common.util.StringUtil;

/**
 * 数据源ds日志插入工具类
 * */
@Service
public class DataSourceLogUtil {
	private static Logger logger = LoggerFactory.getLogger(DataSourceLogUtil.class);
	@Autowired
	protected DaoService daoService;
	@Autowired
	private DESUtils desService;
	@Autowired
	PropertyUtil propertyEngine;
	/**通用数据源请求数据insert SQL*/
	private final static String reqSQL = "INSERT INTO CPDB_DS.T_DS_DATASOURCE_REQ "
			+ " (ID,TRADE_ID,ACCT_ID,REQ_NAME,REQ_VALUE) "
			+ " VALUES (CPDB_DS.SEQ_DS_DATASOURCE_REQ.NEXTVAL,?,?,?,?)";
	
	/**通用数据源响应数据insert SQL*/
	private final static String logSQL = "INSERT INTO CPDB_DS.T_DS_DATASOURCE_LOG (ID, TRADE_ID, DS_ID, STATE_CODE, STATE_MSG,"
			+ " REQ_URL, INCACHE, BIZ_CODE1, BIZ_CODE2, "
			+ " BIZ_CODE3, FIELD_ID, REQ_TIME, RSP_TIME, "
			+ " TOTAL_COST,TAG,prod_id,acct_id)"
			+ " VALUES (cpdb_ds.seq_ds_datasource_log.nextval, ?, ?, ?,?, "
			+ " ?, ?, ?, ?, "
			+ " ?, ?, ?, ?, "
			+ " ?,?,?,?)";
	@Async
	public void writeDsLog(String trade_id,DataSourceLogVO log){
		if(StringUtils.isBlank(log.getTrade_id())){
			log.setTrade_id(trade_id);
		}
		/**统计交易耗时*/
		if(log.getReq_time() != null && log.getRsp_time() != null 
				&& log.getTotal_cost() == null){
			log.setTotal_cost(log.getRsp_time().getTime() 
					- log.getReq_time().getTime());
		}
		
		/**如果state_msg 的字节长度  >4000 截取 前1000个字符*/
		try {
			if(log.getState_msg().getBytes("utf-8").length > 4000){
			   log.setState_msg(log.getState_msg().substring(0, 1000)+"...");
			}
		} catch (UnsupportedEncodingException e) {
			logger.error(trade_id +" 数据源日志处理异常 ",e);
		}	
		logger.info("{} start save dslog",trade_id);
        /***/  
		this.daoService.getJdbcTemplate().update(logSQL, new Object[]{
				log.getTrade_id(),log.getDs_id(),log.getState_code(),log.getState_msg(),
				log.getReq_url(),log.getIncache(),log.getBiz_code1(),log.getBiz_code2(),
				log.getBiz_code3(),log.getField_id(),log.getReq_time(),log.getRsp_time(),
				log.getTotal_cost(),log.getTag(),log.getProd_id(),log.getAcct_id()
		});
		logger.info("{} end save dslog",trade_id);
	}
	@Async
	public void writeDsReqLog(final String trade_id,final String acct_id, Map<String, Object> params,
			DataSourceLogVO relatedLog){
		final String encryptWords = propertyEngine.readById("sys_public_encode_keys");
		if(params == null || params.size() == 0) return;	
		final List<DataSourceReqVO> reqs = new ArrayList<DataSourceReqVO>();
		DataSourceReqVO  req = null;
		String des_key = propertyEngine.readById("sys_public_des_key");
		for(Entry<String,Object> entry : params.entrySet()){
			if(relatedLog != null){
				req = new DataSourceReqVO(relatedLog);				
			}else{
				req = new DataSourceReqVO();
			}
			req.setTrade_id(trade_id);
			req.setReq_name(entry.getKey());
			Object value = entry.getValue();
			if (!StringUtil.isEmpty(value)
					&& ArrayUtils.contains(encryptWords.split(","),req.getReq_name())) {
				try {
					//todo 加密
					value = desService.encode(des_key,String.valueOf(value));
				} catch (Exception e) {
					logger.error("目标值 {} 加密异常,具体原因：{}", value,
							e);
				}
			}
			req.setReq_value(value != null? value.toString() : null);
			reqs.add(req);
		}		
		logger.info("{} start save ds params",trade_id);
        /**采取批量存储的方法提高效率*/
		this.daoService.getJdbcTemplate().batchUpdate(reqSQL,
				new BatchPreparedStatementSetter() {
					@Override
					public int getBatchSize() {
						return reqs.size();
					}

					@Override
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						ps.setString(1, trade_id);
						ps.setString(2, acct_id);
						ps.setString(3, reqs.get(i).getReq_name());
						ps.setString(4, reqs.get(i).getReq_value());
					}

				});
		logger.info("{} end save ds params",trade_id);
	}
}
