package com.cte.credit.ds.dao.iface.jixin;

import java.util.List;
import com.cte.credit.common.service.IBaseService;
import com.cte.credit.ds.dao.domain.jixin.Jixin_bank_result;


public interface IJixinBankService extends IBaseService<Jixin_bank_result>{
	/**
	 * 批量保存卡号查询信息
	 * @param score
	 */
	public void batchSave(String prefix,Jixin_bank_result result);
	/**
	 * 查询信息
	 * @param score
	 */
	public  Jixin_bank_result queryJiXin(String trade_id,Jixin_bank_result jixin);
}
