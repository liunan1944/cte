package com.cte.credit.ds.dao.impl.jixin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.cte.credit.common.dao.DaoService;
import com.cte.credit.ds.dao.domain.jixin.Jixin_bank_result;
import com.cte.credit.ds.dao.iface.jixin.IJixinBankService;
@Service
public class JixinBankServiceImpl   implements IJixinBankService{
	private final  Logger logger = LoggerFactory.getLogger(JixinBankServiceImpl.class);
	@Autowired 
	private DaoService  daoService;
	
	@Override
	@Async
	public void batchSave(String prefix,Jixin_bank_result result) {
		logger.info("{} 吉信106数据插入开始...", prefix );
		String sql = " insert into cpdb_ds.t_ds_jixin_result(id,trade_id,cardno,name,"
				+ "dsorderid,merchno,returncode,errtext,transcode,ordersn,orderid,sign) "
				+" values(cpdb_ds.seq_t_ds_jixin_result.nextval,?,?,?,?,?,?,?,?,?,?,?) ";
		daoService.getJdbcTemplate().update(sql,
				result.getTrade_id(),
				result.getCardno(),
				result.getName(),
				result.getDsorderid(),
				result.getMerchno(),
				result.getReturncode(),
				result.getErrtext(),
				result.getTranscode(),
				result.getOrdersn(),
				result.getOrderid(),
				result.getSign());
		logger.info("{} 吉信106数据插入结束", prefix );
	}
}
