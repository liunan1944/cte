package com.cte.credit.ds.dao.impl.guozt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.cte.credit.common.dao.DaoService;
import com.cte.credit.ds.dao.domain.salary.GuoZT_salary_result;
import com.cte.credit.ds.dao.iface.guozt.IGuoZTSalaryService;
@Service
public class GuoZTSalaryServiceImpl   implements IGuoZTSalaryService{
	private final  Logger logger = LoggerFactory.getLogger(GuoZTSalaryServiceImpl.class);
	@Autowired 
	private DaoService  daoService;
	
	@Override
	@Async
	public void batchSave(String prefix,GuoZT_salary_result result) {
		logger.info("{} 工资水平数据插入开始...", prefix );
		String sql = " insert into cpdb_ds.t_ds_guozt_salary_result(id,trade_id,cardno,name,code,data,message) "
				+ " values (cpdb_ds.seq_t_ds_guozt_salary_result.nextval,?,?,?,?,?,?) ";
		daoService.getJdbcTemplate().update(sql,
				result.getTrade_id(),
				result.getCardno(),
				result.getName(),
				result.getCode(),
				result.getData(),
				result.getMessage());
		logger.info("{} 工资水平数据插入结束", prefix );
	}
}
