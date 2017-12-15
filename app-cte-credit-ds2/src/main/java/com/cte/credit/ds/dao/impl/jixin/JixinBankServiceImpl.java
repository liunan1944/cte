package com.cte.credit.ds.dao.impl.jixin;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cte.credit.api.exception.ServiceException;
import com.cte.credit.common.service.impl.BaseServiceImpl;
import com.cte.credit.ds.dao.domain.jixin.Jixin_bank_result;
import com.cte.credit.ds.dao.iface.jixin.IJixinBankService;
@Service
@Transactional
public class JixinBankServiceImpl  extends BaseServiceImpl<Jixin_bank_result> implements IJixinBankService{
	private final  Logger logger = LoggerFactory.getLogger(JixinBankServiceImpl.class);

	
	@Override
	@Async
	public void batchSave(String prefix,Jixin_bank_result result) {
		logger.info("{} 吉信106数据插入开始...", prefix );
		try {
			this.add(result);
		} catch (ServiceException e) {
			logger.error("批量保存失败，详细信息:{}" , e.getMessage());
			e.printStackTrace();
		}
		logger.info("{} 吉信106数据插入结束", prefix );
	}
	@Override
	public Jixin_bank_result queryJiXin(String trade_id,Jixin_bank_result jixin){
		logger.info("{} 吉信定时查询开始...", trade_id);
		Jixin_bank_result jixin_tmp = new Jixin_bank_result();
		List<Jixin_bank_result> result = this.query(jixin);
		if(result.size()>0){
			jixin_tmp = result.get(0);
		}
		logger.info("{} 吉信定时查询结束", trade_id);
		return jixin_tmp;
	}
}
