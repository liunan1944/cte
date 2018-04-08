package com.cte.credit.ds.dao.iface.guozt;

import com.cte.credit.ds.dao.domain.salary.GuoZT_salary_result;


public interface IGuoZTSalaryService{
	/**
	 * 批量保存卡号查询信息
	 * @param score
	 */
	void batchSave(String prefix,GuoZT_salary_result result);
}
