package com.cte.credit.ds.dao.iface;

import java.util.List;
import java.util.Map;

import com.cte.credit.common.service.IBaseService;
import com.cte.credit.ds.dao.domain.Nciic_Check_Result;


public interface INciicCheckService extends IBaseService<Nciic_Check_Result>{
	/**
	 * 缓存查询一致性 
	 * @param score
	 */
	Map<String, Object> inCached(String name, String cardNo);
	/**
	 * 批量保存卡号查询信息
	 * @param score
	 */
	void batchSave(List<Nciic_Check_Result> result);
	/**
	 * 更新卡号查询信息
	 * @param score
	 */
	void updateCard(String name, String cardNo,String photo);
	/**
	 * 查询数据库是否存在此数据
	 * @param score
	 */
	public boolean inCachedCount(String name, String cardNo,int days);
}
