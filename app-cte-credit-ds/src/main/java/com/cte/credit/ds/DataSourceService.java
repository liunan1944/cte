package com.cte.credit.ds;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.cte.credit.api.Conts;
import com.cte.credit.api.dto.DataSource;
import com.cte.credit.api.enums.CRSStatusEnum;
import com.cte.credit.api.iface.IDataSourceService;
import com.cte.credit.common.counter.GlobalCounter;
import com.cte.credit.common.dao.DaoService;
import com.cte.credit.common.template.PropertyUtil;
import com.cte.credit.common.template.iface.ITemplateEngine;
import com.cte.credit.ds.iface.IDataSourceRequestor;

@Service
public class DataSourceService extends BaseDataSourceService implements IDataSourceService {
	private final  Logger logger = LoggerFactory.getLogger(DataSourceService.class);
	private static WebApplicationContext wac ;

	@Autowired
	public DaoService daoService;
	@Autowired
	public ITemplateEngine templateEngine;
	@Autowired
	PropertyUtil propertyEngine;
	@Override
	public Map<String, Object> fetch(String trade_id, final DataSource ds)
			throws Exception {
		long start = new Date().getTime();
		boolean doPrint = "1".equals(propertyEngine.readById("sys_public_logprint_switch"));
		Map<String, Object> rets = new HashMap<String, Object>();
		DataSource dsNew = new DataSource();
		dsNew = ds;
		final String prefix = trade_id; //流水号标识
		
		String ds_id = ds.getId();
		logger.info("{} 数据源适配器收到请求...", prefix);
		//Step1: 是否启用mock
		if(enableMock(ds_id)){
			logger.warn("{} mock开关已启用!{}", prefix, ds_id);
			return getMockResponse(ds_id);
		}
		dsNew.setId(ds_id);
		logger.info("{} 采集任务适配开始...", prefix);
		String ds_flow_flag = ds.getRefProdCode() + "-" + ds_id;
		String ds_error_flag = ds_id + tail_errorlist;
		boolean isConfiguredDS = false;
		Object wacBean = null;
		
		//Step3: 数据源配置检查
		try{
			if (null == wac)
				wac = ContextLoader.getCurrentWebApplicationContext();
			wacBean = wac.getBean(ds_id);
			if (null == wacBean
					|| wacBean.getClass().isAssignableFrom(
							BaseDataSourceRequestor.class)) {
				rets.put(Conts.KEY_RET_STATUS, "-99999");
				rets.put(Conts.KEY_RET_MSG, "数据源配置异常，请检查参数!");
				logger.error("{} 数据源配置异常，请检查参数!{}", prefix, ds_id);
				return rets;
			}
		}catch(NoSuchBeanDefinitionException ex){
			rets.put(Conts.KEY_RET_STATUS,CRSStatusEnum.STATUS_FAILED_SYS_DS_NOT_MATCHED);
			rets.put(Conts.KEY_RET_MSG,"无合适的数据源驱动!");
			logger.warn("{} 无合适的数据源驱动!{}", prefix, ds_id);
			return rets;
		}
		
		//Step4: 数据源适配
		logger.warn("{} 数据源适配开始...", prefix);
		IDataSourceRequestor requestor;
		if(!isConfiguredDS){
			requestor = (IDataSourceRequestor) matchDataSourceRequestor(ds_id);
			if(requestor==null){
				rets.put(Conts.KEY_RET_STATUS,CRSStatusEnum.STATUS_FAILED_SYS_DS_NOT_MATCHED);
				rets.put(Conts.KEY_RET_MSG,"无合适的数据源驱动!");
				logger.warn("{} 无合适的数据源驱动!{}", prefix, ds_id);
				return rets;
			}	
		}else{
			requestor = (IDataSourceRequestor) wacBean;
		}		
		logger.info("{}采集任务适配成功!", prefix);
		
		//Step5: 数据源参数校验
		logger.info("{}数据源参数验证开始...", prefix);
		rets = requestor.valid(trade_id, dsNew);
		if (!isSuccess(rets)){
			rets.put(Conts.KEY_RET_STATUS,CRSStatusEnum.STATUS_FAILED_SYS_PARAM_INVALID);
			rets.put(Conts.KEY_RET_MSG,"校验不通过:传入参数不正确");
			logger.warn("{} 校验不通过:传入参数不正确!{}", prefix, ds_id);
			return rets;
		}
		logger.info("{} 验证通过", prefix);
		
		//Step6: 数据源采集启动
		logger.info("{} 数据采集任务开始...", prefix);
		long reqStart = new Date().getTime();
		rets = requestor.request(trade_id, ds);
		if(ds.getLimit_out()!=null)
			templateEngine.filterParams(rets, ds.getLimit_out());
		//add by wangjing
		logger.info("{} 数据采集任务完毕,耗时:{}", prefix ,new Date().getTime() - reqStart +" ms");
		
		//Step7: redis数据源流量监控
		GlobalCounter.sign(ds_flow_flag);
		logger.info("{} 流量监控+1", prefix);
		
		//Step8: redis数据源熔断监控 
		if(isErr(rets)){
			logger.warn("{} 数据源熔断收集器+1：{}", prefix, ds_id);
			GlobalCounter.sign(ds_error_flag, 30000);
			logger.info("{} 数据源熔断收集器当前统计数：{}", prefix,
					GlobalCounter.getCount(ds_error_flag));
		}
		if (doPrint) {
			logger.info("{} 数据采集任务完毕,返回消息:{}", prefix, JSONObject.toJSONString(rets,true));
		}
		logger.info("{} 总计耗时:{}", prefix, new Date().getTime() - start +" ms");
		return rets;
	}
}
