package com.cte.credit.ds.dao.iface.jixin;

import com.cte.credit.ds.dao.domain.jixin.Jixin_bank_result;


public interface IJixinBankService{
	/**
	 * 批量保存卡号查询信息
	 * @param score
	 */
	void batchSave(String prefix,Jixin_bank_result result);
}
