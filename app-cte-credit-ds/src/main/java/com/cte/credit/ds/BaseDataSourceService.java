package com.cte.credit.ds;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.cte.credit.api.Conts;
import com.cte.credit.api.enums.CRSStatusEnum;
import com.cte.credit.api.exception.ServiceException;
import com.cte.credit.common.annotation.DataSourceClass;
import com.cte.credit.common.counter.GlobalCounter;
import com.cte.credit.common.util.SpringContextUtils;
import com.cte.credit.common.util.StringUtil;
import com.cte.credit.common.util.SystemPropertiesUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class BaseDataSourceService {
	private final  Logger logger = LoggerFactory.getLogger(BaseDataSourceService.class);

	private static Map<String,Object> implObjectMap = null;

	public final String  tail_errorlist="_errorlist";
	public final String  tail_send_time="_sendtime";
	
	/**从配置参数字符串（如dsid1:300,dsid2:50,*:10）取得对应ds的熔断阈值，默认10*/
	protected static int getDsErrorConfigMax(String numstring ,String dsid){
		//numstring="dsid1:300,dsid2:50,*:10";
		String[] arr= numstring.split(",");
		for (int i = 0; i < arr.length; i++) {
			String item=arr[i];
			if(item.indexOf(dsid)>-1)
				return Integer.parseInt(item.substring(item.indexOf(":")+1));
		}
		return 10;
	}

	/**
	 * 判断交易返回是否成功(包含预警)
	 * @param result
	 * @return
     */
	public static boolean isSuccess(Map<String, Object> result) {
		if (result == null)
			return false;
		CRSStatusEnum retstatus = CRSStatusEnum.valueOf(result.get(
				Conts.KEY_RET_STATUS).toString());
		return CRSStatusEnum.STATUS_SUCCESS.equals(retstatus);
	}
	/**
	 * 判断交易是否需要捕获异常
	 * @param result
	 * @return ret
	 */
	public boolean isErr(Map<String, Object> result){
//		String ds_errors_watch = propertyEngine.readById("ds_errors_watch");
//		Object obj_retstatus=result.get(Conts.KEY_RET_STATUS);
//		if(obj_retstatus!=null&&obj_retstatus instanceof CRSStatusEnum){
//			String status_code =((CRSStatusEnum)obj_retstatus).getRet_sub_code();
//			if(StringUtil.areNotEmpty(status_code,ds_errors_watch)
//					&&StringUtil.isStrInStrs(status_code, ds_errors_watch.split(","))){ 
//				return true;
//			}
//		}
		return false;
	}
	/**
	 * 反射查找数据源requestor实现
	 * @param ds_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseDataSourceRequestor> T matchDataSourceRequestor(String ds_id) {
		if (implObjectMap == null) {
			ApplicationContext ac = SpringContextUtils.getContext();
			implObjectMap = ac.getBeansWithAnnotation(DataSourceClass.class);
		}
		for(Object obj :  implObjectMap.values()){
			if (obj != null) {
				DataSourceClass dataSourceClass = obj.getClass().getAnnotation(
						DataSourceClass.class);
				if (dataSourceClass.bindingDataSourceId().equalsIgnoreCase(ds_id)) {
					return (T) obj;
				}
			}
		}
		return null;
	}
	
	private final String start_suffix  ="mock_resp_";
	/**
	 * 是否启用mock
	 * @param ds_id
	 * @return
	 */
	public boolean enableMock(String ds_id){
//		String mock_exprs = propertyEngine.readById("sys.credit.client.mock.expr");
//		for(String expr : mock_exprs.split(",")){
//			if(expr.equalsIgnoreCase(ds_id) || expr.equals(all_suffix)){
//				 return true;
//			}
//		}
		return false;
	}
	/**
	 * mock报文
	 * @param ds_id
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public Map<String, Object> getMockResponse(String ds_id) throws Exception {
		String respBody = SystemPropertiesUtil.getSysProperty(start_suffix + ds_id);
		if(!StringUtil.isEmpty(respBody)){
			return JSONObject.parseObject(respBody, Map.class);
		}else
			throw new ServiceException("mock报文尚未配置!");
	}
	/** 从AB配置中查找最佳DS（如果熔断） 
	 * @throws ServiceException */
	public String findABDs(String ds_ab_watch, String dsid)
			throws ServiceException {
		String next_dsid = dsid;
		if (!StringUtil.isEmpty(ds_ab_watch) && GlobalCounter.exists(tail_errorlist, dsid)) {
			String[] abs = ds_ab_watch.split(",");
			for (String ab : abs) {
				if(!next_dsid.equals(dsid)){
					break;
				}
				String[] confs = ab.split(":");
				for (int i = 0; i < confs.length; i++) {
					if(!next_dsid.equals(dsid)){
						break;
					}
					if (dsid.equalsIgnoreCase(confs[i])) {
						for(String conf:confs){
							if(!conf.equals(dsid) && !GlobalCounter.exists(tail_errorlist, conf)){
								next_dsid = conf;
								break;
							}
						}
					}
				}
			}
		}
		logger.warn("数据源重定向策略返回数据{} {}",dsid,next_dsid);
		return next_dsid;
	}
}
