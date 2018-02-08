package com.cte.credit.ds.dao.iface.edu;

import com.cte.credit.common.service.IBaseService;
import com.cte.credit.ds.dao.domain.Nciic_Check_Result;


public interface IEduCheckService extends IBaseService<Nciic_Check_Result>{

	/**
	 * 批量保存卡号查询信息
	 * @param score
	 */
	void batchSave(String trade_id,String rspStr,String request_sn,String response_sn);
}
