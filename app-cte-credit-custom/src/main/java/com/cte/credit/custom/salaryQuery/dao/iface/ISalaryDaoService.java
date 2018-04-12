package com.cte.credit.custom.salaryQuery.dao.iface;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
@Service
public interface ISalaryDaoService{
	/**
	 * 查询测试工资
	 * @param score
	 */
	public List<Map<String, Object>> findSalary(String name,String cardNo);
	
	public boolean findTestUser(String acct_id,String prod_id);
}
