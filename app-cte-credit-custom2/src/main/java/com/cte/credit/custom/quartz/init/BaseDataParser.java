package com.cte.credit.custom.quartz.init;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cte.credit.common.dao.DaoService;

@Component
public class BaseDataParser {
	@Autowired 
	private DaoService  daoService;
	
	public List<Map<String, Object>> queryProperties(String model_id){
		String sql = " SELECT M.KEY_CODE KEY_CODE,M.KEY_VALUE KEY_VALUE FROM CPDB_DS.T_SYS_PROPERTY_PARAM M "
				+" WHERE M.OWNERID=( SELECT OWNERID FROM CPDB_DS.T_SYS_PROPERTY_MODEL D WHERE D.MODEL_ID=? "
				+" AND D.STATUS='3' AND ROWNUM=1) AND M.STATUS='3' ";
		List<Map<String, Object>> result = this.daoService.getJdbcTemplate().queryForList(sql,model_id);
		return result;
	}
	public String queryOwnerid(String model_id){
		String result_str = "";
		String sql = " SELECT OWNERID FROM CPDB_DS.T_SYS_PROPERTY_MODEL D "
				+ "WHERE D.MODEL_ID=? AND D.STATUS='3' AND ROWNUM=1 ";
		List<Map<String, Object>> result = this.daoService.getJdbcTemplate().queryForList(sql,model_id);
		if(result.size()>0){
			result_str = String.valueOf(result.get(0).get("OWNERID"));
		}
		return result_str;
	}
}
