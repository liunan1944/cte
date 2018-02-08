package com.cte.credit.ds.dao.impl.edu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.cte.credit.common.dao.DaoService;
import com.cte.credit.common.service.impl.BaseServiceImpl;
import com.cte.credit.ds.dao.domain.Nciic_Check_Result;
import com.cte.credit.ds.dao.iface.edu.IEduCheckService;
@Service
public class EduCheckServiceImpl extends BaseServiceImpl<Nciic_Check_Result>  implements IEduCheckService{
	private final  Logger logger = LoggerFactory.getLogger(EduCheckServiceImpl.class);
	@Autowired 
	private DaoService  daoService;
	
	@Override
	@Async
	public void batchSave(String trade_id,String rspStr,String request_sn,String response_sn) {
		logger.info("{} 学历数据插入开始...", trade_id );
		String sql = " insert into t_ds_edu_result(id,trade_id ,request_sn,response_sn,rsp_msg)"
				+ " values (cpdb_ds.seq_ds_edu_result.nextval,?,?,?,?) ";
		daoService.getJdbcTemplate().update(sql,
				trade_id,
				request_sn,
				response_sn,
				rspStr);
		logger.info("{} 学历数据插入结束", trade_id );
	}
}
