package com.ln.test.client.aijin;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.cte.credit.api.Conts;
import com.cte.credit.api.dto.DataSource;
import com.cte.credit.api.enums.CRSStatusEnum;
import com.cte.credit.common.annotation.DataSourceClass;
import com.cte.credit.common.util.ExceptionUtil;
import com.cte.credit.common.util.ParamUtil;
import com.ln.test.iface.IDataSourceRequestor;

@DataSourceClass(bindingDataSourceId="ds_aijin_jx")
public class AiJinDataSourceRequestor extends BaseAijinDataSourceRequestor
		implements IDataSourceRequestor {
	private final  Logger logger = LoggerFactory.getLogger(AiJinDataSourceRequestor.class);
	@Override
	public Map<String, Object> request(String trade_id, DataSource ds){
		final String prefix = trade_id + " " + Conts.KEY_SYS_AGENT_HEADER;
		TreeMap<String, Object> retdata = new TreeMap<String, Object>();
		Map<String, Object> rets = new HashMap<String, Object>();
		Map<String, Object> reqparam = new HashMap<String, Object>();
		String resource_tag = Conts.TAG_SYS_ERROR;
		try{	
			String name = ParamUtil.findValue(ds.getParams_in(), paramIds[0]).toString();   //姓名 
			String cardNo = ParamUtil.findValue(ds.getParams_in(), paramIds[1]).toString(); //身份证号码

			logger.info("{} 查询数据",  prefix);
			TestLoad test = new TestLoad();
			test.test01(prefix,name+cardNo);
			rets.put(Conts.KEY_RET_TAG, new String[]{resource_tag});
			rets.put(Conts.KEY_RET_DATA, retdata);
			rets.put(Conts.KEY_RET_STATUS, CRSStatusEnum.STATUS_SUCCESS);
			rets.put(Conts.KEY_RET_MSG, "采集成功!");
		}catch(Exception ex){
			logger.error("{} 数据源处理时异常：{}",prefix,ExceptionUtil.getTrace(ex));
			resource_tag = Conts.TAG_SYS_ERROR;
			rets.put(Conts.KEY_RET_STATUS, CRSStatusEnum.STATUS_FAILED_SYS_DS_EXCEPTION);
			rets.put(Conts.KEY_RET_MSG, "数据源处理时异常! 详细信息:"+ex.getMessage());
			rets.put(Conts.KEY_RET_TAG, new String[]{resource_tag});
		}
		return rets;
	}
}
