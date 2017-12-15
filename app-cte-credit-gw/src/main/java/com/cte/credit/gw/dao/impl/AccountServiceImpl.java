package com.cte.credit.gw.dao.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cte.credit.common.dao.DaoService;
import com.cte.credit.gw.dao.iface.IAccountService;
@Service
@Transactional
public class AccountServiceImpl  implements IAccountService{
	private static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
	@Autowired 
	private DaoService  daoService;
	@Override
	public List<Map<String, Object>> queryAcctAll(){
		String sql = " select d.acct_id,d.api_key,d.balance,d.enddate "
				+ "from cpdb_app.t_sys_account d where d.status in('0','-1') ";
		List<Map<String, Object>> result = this.daoService.getJdbcTemplate().queryForList(sql);
		return result;
	}
	@Override
	public List<Map<String, Object>> queryAcctIps(String acct_id){
		String sql = " select d.ip_limit,d.status from cpdb_app.t_sys_acct_ip_limits d ";		
		List<Map<String, Object>> result = this.daoService.getJdbcTemplate().queryForList(sql,acct_id);
		return result;
	}
	@Override
	public List<Map<String, Object>> queryAcctProds(String acct_id){
		String sql = " select d.prod_limit,d.price,d.status from cpdb_app.t_sys_acct_prods d ";
		List<Map<String, Object>> result = this.daoService.getJdbcTemplate().queryForList(sql,acct_id);
		return result;
	}
	//计算是否有可用测试条数,并扣除
	@Override
	public synchronized boolean isTestUser(String acct_id,String prod){
		String sql = "select count(1) cnt from cpdb_ds.t_sys_acct_prods d "
				+ "where d.acct_id=? and d.prod_limit=? and d.test_num>0";
		Integer result = this.daoService.findOneBySql(sql, 
				new Object[]{acct_id,prod},Integer.class);
		if(result>0){
			String sql1 = " update cpdb_ds.t_sys_acct_prods d set d.test_num=d.test_num-1 where d.acct_id=? and d.prod_limit=? ";
			this.daoService.getJdbcTemplate().update(sql1,acct_id,prod);
		}		
		return result>0;
	}
	//测试账户扣除测试条数
	@Override
	public synchronized void updateTestProd(String trade_id,String tag,
			String acct_id,String prod){
		String sql1 = " update cpdb_ds.t_sys_acct_prods d set d.test_num=d.test_num-1 where d.acct_id=? and d.prod_limit=? ";
		this.daoService.getJdbcTemplate().update(sql1,acct_id,prod);
	}
	@Override
	public boolean isRepeatTrad(String reqeust_sn,String acct_id,String prod){
		String sql = " select count(1) cnt from cpdb_ds.t_sys_req_header m "
				+ "where m.requst_sn=? and m.acct_id=? and m.prod_id=? ";
		Integer result = this.daoService.findOneBySql(sql, new Object[]{reqeust_sn,acct_id,prod},Integer.class);
		return result>0;
	}
}
