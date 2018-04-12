package com.cte.credit.custom.salaryQuery.dao.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cte.credit.common.dao.DaoService;
import com.cte.credit.custom.salaryQuery.dao.iface.ISalaryDaoService;
@Service
@Transactional
public class SalaryServiceImpl  implements ISalaryDaoService {
	private final Logger logger = LoggerFactory.getLogger(SalaryServiceImpl.class);
	@Autowired
	private DaoService daoService;

	public List<Map<String, Object>> findSalary(String name,String cardNo){
		String sql ="select data from t_ds_guozt_salary_test t where t.cardno=? and t.name=? ";
		List<Map<String, Object>> result = this.daoService.getJdbcTemplate().queryForList(sql,cardNo,name);
		return result;
	}
	public boolean findTestUser(String acct_id,String prod_id){
		String sql ="select count(1) as cnt from cpdb_ds.t_sys_acct_prods d where d.acct_id=? and d.prod_limit=? and d.isfee='1' ";
		int result = this.daoService.findOneBySql(sql, new Object[]{acct_id,prod_id},Integer.class);
		return result>0;
	}
}
