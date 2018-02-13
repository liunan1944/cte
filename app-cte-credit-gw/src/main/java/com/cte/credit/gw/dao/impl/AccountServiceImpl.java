package com.cte.credit.gw.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.cte.credit.common.dao.DaoService;
import com.cte.credit.gw.dao.iface.IAccountService;
import com.cte.credit.gw.dto.Account;
import com.cte.credit.gw.dto.ProdLimit;
import com.cte.credit.gw.quartz.init.AccountInitUtil;
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
	@Override
	public synchronized boolean isOnlineUser(String acct_id,String prod){
		String sql = "select count(1) cnt from cpdb_ds.t_sys_acct_prods d "
				+ "where d.acct_id=? and d.prod_limit=? and d.test_num>0";
		Integer result = this.daoService.findOneBySql(sql, 
				new Object[]{acct_id,prod},Integer.class);
		return result >0 ;
	}
	//测试账户扣除测试条数
	@Override
	public synchronized void updateTestProd(String trade_id,
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
	@Override
	public synchronized boolean updateAcctProd(String trade_id,String tag,
			String acct_id,String prod,String flag){
		if("1".equals(flag)){//进行扣费
			int acctnum = 0;
			for(Account acct_tmp:AccountInitUtil.accountList){
				if(acct_id.equals(acct_tmp.getAcct_id())){
					for(ProdLimit prod_tmp:AccountInitUtil.prodLimitList){
						if(prod.equals(prod_tmp.getProd_limit())){
							if(acct_tmp.getBalance()>0 && (acct_tmp.getBalance()-prod_tmp.getPrice())>0){
								AccountInitUtil.accountList.get(acctnum).setBalance(acct_tmp.getBalance()-prod_tmp.getPrice());
								return true;
							}							
						}
					}
				}
				acctnum++;
			}
		}else if("2".equals(flag)){//进行账户加载
			String sql = " update t_sys_account d set d.balance=? where d.acct_id=? ";
			final List<Account> accountListNew = AccountInitUtil.accountList;
			this.daoService.getJdbcTemplate().batchUpdate(sql,
	                new BatchPreparedStatementSetter() {
	                    @Override
	                    public int getBatchSize() {
	                        return accountListNew.size();
	                    }
	                    @Override
	                    public void setValues(PreparedStatement ps, int i)
	                            throws SQLException {
	                    	ps.setDouble(1, accountListNew.get(i).getBalance());
	                        ps.setString(2, accountListNew.get(i).getAcct_id());
	                    }
	                });
			String sql1 = " select d.acct_id,d.api_key,d.balance,d.enddate,d.isfee,d.status "
					+ "from cpdb_ds.t_sys_account d where d.status in('0','-1') ";
			List<Map<String, Object>> result = this.daoService.getJdbcTemplate().queryForList(sql1);
			List<Account> accountListTmp = new ArrayList<Account>();
			for(Map<String, Object> map:result){
				if(map.get("ACCT_ID")!=null){
					Account acct_tmp = new Account();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					acct_tmp.setAcct_id(map.get("ACCT_ID")!=null?String.valueOf(map.get("ACCT_ID")):"");
					acct_tmp.setApi_key(map.get("API_KEY")!=null?String.valueOf(map.get("API_KEY")):"");
					acct_tmp.setBalance(map.get("BALANCE")!=null?
							Double.valueOf(String.valueOf(map.get("BALANCE"))):0);
					acct_tmp.setIsfee(map.get("ISFEE")!=null?String.valueOf(map.get("ISFEE")):"");
					acct_tmp.setStatus(map.get("STATUS")!=null?String.valueOf(map.get("STATUS")):"");
					try {
						acct_tmp.setEnddate(map.get("ENDDATE")!=null?
								sdf.parse(String.valueOf(map.get("ENDDATE"))):(new Date()));
					} catch (ParseException e) {
						logger.error("{} 初始化账户信息报错,报错账户信息{}",trade_id,JSON.toJSONString(map));
					}
					logger.info("{} 初始化账户产品列表权限信息开始加载...",trade_id);
					String sql2 = " select d.acct_id,d.prod_limit,d.price,d.status,d.test_num,d.pay_tags from "
							+ " cpdb_ds.t_sys_acct_prods d where d.acct_id=? ";
					List<Map<String, Object>> prods = this.daoService.getJdbcTemplate().
							queryForList(sql2,String.valueOf(map.get("ACCT_ID")));
					if(prods.size()>0){
						for(Map<String, Object> map1:prods){
							ProdLimit prod_tmp = new ProdLimit();
							prod_tmp.setAcct_id(map1.get("ACCT_ID")!=null?String.valueOf(map1.get("ACCT_ID")):"");
							prod_tmp.setPay_tags(map1.get("PAY_TAGS")!=null?String.valueOf(map1.get("PAY_TAGS")):"");
							prod_tmp.setProd_limit(map1.get("PROD_LIMIT")!=null?
									String.valueOf(map1.get("PROD_LIMIT")):"");
							prod_tmp.setPrice(map1.get("PRICE")!=null?
							Double.valueOf(String.valueOf(map1.get("PRICE"))):0);
							prod_tmp.setTest_num(map1.get("TEST_NUM")!=null?
									Integer.valueOf(String.valueOf(map1.get("TEST_NUM"))):0);
							prod_tmp.setStatus(map1.get("STATUS")!=null?String.valueOf(map1.get("STATUS")):"");
							AccountInitUtil.prodLimitList.add(prod_tmp);
						}
						logger.info("{} 初始化账户产品列表权限信息加载完成",trade_id);
					}
					accountListTmp.add(acct_tmp);
				}	
				
			}
			AccountInitUtil.accountList = accountListTmp;
		}
		return false;
	}
}
