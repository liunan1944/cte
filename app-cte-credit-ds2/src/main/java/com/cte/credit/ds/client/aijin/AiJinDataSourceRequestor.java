package com.cte.credit.ds.client.aijin;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.cte.credit.api.Conts;
import com.cte.credit.api.dto.DataSource;
import com.cte.credit.api.enums.CRSStatusEnum;
import com.cte.credit.common.annotation.DataSourceClass;
import com.cte.credit.common.util.ExceptionUtil;
import com.cte.credit.common.util.ParamUtil;
import com.cte.credit.ds.dao.iface.INciicCheckService;
import com.cte.credit.ds.iface.IDataSourceRequestor;

@DataSourceClass(bindingDataSourceId="ds_aijin_jx")
public class AiJinDataSourceRequestor extends BaseAijinDataSourceRequestor
		implements IDataSourceRequestor {
	private final  Logger logger = LoggerFactory.getLogger(AiJinDataSourceRequestor.class);
	private final  String CHANNEL_NO = "01";
	@Autowired
	private INciicCheckService nciicCheckService;
	@Override
	public Map<String, Object> request(String trade_id, DataSource ds){
		final String prefix = trade_id + " " + Conts.KEY_SYS_AGENT_HEADER;
		TreeMap<String, Object> retdata = new TreeMap<String, Object>();
		Map<String, Object> rets = new HashMap<String, Object>();
		Map<String, Object> reqparam = new HashMap<String, Object>();
		String enCardNo = "";
		String resource_tag = Conts.TAG_SYS_ERROR;
		try{	
			String name = ParamUtil.findValue(ds.getParams_in(), paramIds[0]).toString();   //姓名 
			String cardNo = ParamUtil.findValue(ds.getParams_in(), paramIds[1]).toString(); //身份证号码
			String flag = ParamUtil.findValue(ds.getParams_in(), paramIds[2]).toString(); //是否带照片,01带照片,02不带照片
			String acct_id = ParamUtil.findValue(ds.getParams_in(), paramIds[3]).toString();//账户号
			if(flag.equals(CHANNEL_NO)){//判断是否走无照片输出通道
				logger.info("{}爱金简项数据源查询走带照片通道!", new String[] { prefix });
			}else{
				logger.info("{}爱金简项数据源查询走无照片通道!", new String[] { prefix });
			}
			reqparam = nciicCheckService.inCached(name,cardNo);
			logger.info("{} 查询数据为:{}", new String[] { prefix,JSON.toJSONString(reqparam)});	 		
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
