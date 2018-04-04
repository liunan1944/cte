package com.cte.credit.ds;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.cte.credit.api.Conts;
import com.cte.credit.api.dto.DataSource;
import com.cte.credit.api.enums.CRSStatusEnum;
import com.cte.credit.api.exception.ServiceException;
import com.cte.credit.common.annotation.DataSourceClass;
import com.cte.credit.common.counter.GlobalCounter;
import com.cte.credit.common.template.PropertyUtil;
import com.cte.credit.common.util.DateUtil;
import com.cte.credit.common.util.ExceptionUtil;
import com.cte.credit.common.util.SendEmail;
import com.cte.credit.common.util.SpringContextUtils;
import com.cte.credit.common.util.StringUtil;
import com.cte.credit.common.util.SystemPropertiesUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class BaseDataSourceService {
	private final  Logger logger = LoggerFactory.getLogger(BaseDataSourceService.class);
	private final String ds_error_warn_sms = "【预警提示】数据源区间异常次数达到指定阈值，熔断保护启用，已暂时进行关闭处理，后续请及时跟踪和分析!";
	private final String ds_error_on_sms = "【平台提示】数据源动态调整已启用！";
	private static Map<String,Object> implObjectMap = null;

	public final String  tail_errorlist="_errorlist";
	public final String  tail_send_time="_sendtime";
	@Autowired
    SendEmail  senEmailService;
	//是否进行熔断保护
	public boolean isfuseOff(final String trade_id,final DataSource ds,PropertyUtil propertyEngine) throws ServiceException{
		final String prefix = trade_id +" "+ Conts.KEY_SYS_AGENT_HEADER; //流水号标识
		final String ds_id = ds.getId();
		final String ds_name = ds.getName();
		String max_error_num_strs = propertyEngine.readById("ds_max_error_num");//数据源error熔断阈值
		int ds_sms_time_rate_mini = Integer.parseInt(propertyEngine.readById("ds_sms_time_rate"));//sms频率，单位分
		String emails = propertyEngine.readById("sys_public_email_accts");
		boolean isFuse = "1".equals(propertyEngine.readById("sys_public_fuse_switch"));//1不进行熔断,0进行熔断
		final long ds_sms_time_rate_ms=ds_sms_time_rate_mini*60*1000;//单位毫秒
		String ds_error_flag = ds_id + tail_errorlist;
		final int ds_error_count_value=GlobalCounter.getCount(ds_error_flag);
		//熔断阈值定制化
		int  max_error_num=getDsErrorConfigMax(max_error_num_strs, max_error_num_strs.indexOf(ds_id)>-1?ds_id:"*");
		logger.info("{} 监控情况：KEY:{},COUNT:{},MAX_LIMIT:{}", new Object[]{prefix,ds_error_flag,ds_error_count_value,max_error_num});
		if(GlobalCounter.exists(tail_errorlist, ds.getId())){
			return true;
		}
		if(ds_error_count_value>=max_error_num){
			logger.warn("{} 熔断阈值触达,自动关闭开启!!",prefix);
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Date send_time=new Date();
						Long now=send_time.getTime();
						String last_send_time_key = ds_id+tail_send_time;
						String last_send_time_value=GlobalCounter.getString(last_send_time_key);
						boolean is_send=false;
						if(last_send_time_value!=null){
							long timsmap_redis=Long.parseLong(last_send_time_value);
							logger.info("{} 当前时间戳:{},近一次发送的时间戳:{},间隔周期(ms):{}", new Object[]{prefix,now,timsmap_redis,ds_sms_time_rate_ms});
							if((now-timsmap_redis)>=ds_sms_time_rate_ms){
								is_send=true;
							}
						}else{
							is_send=true;
						}
						if(is_send){
							senEmailService.sendEmails(emails, ds_error_warn_sms +"\n接口ID："+ds_id
									+"\n接口名称："+ds_name +"\n当前异常汇总："+ds_error_count_value
									+" \n报告时间:"+new SimpleDateFormat(DateUtil.DATETIMESHOWFORMAT).format(send_time), "CTE数据源报错预警");
							logger.info("{} 告警短信发送完毕.",prefix);
							GlobalCounter.setString(last_send_time_key, now+"");
						}else{
							logger.error("{} 短信发送时间间隔小于设定时限，不发送短信！",prefix);
						}
					} catch (Exception e) {
						logger.error("{}短信发送时发生异常：{}", prefix,ExceptionUtil.getTrace(e));
					}
				}
			}).start();
			
			if(!GlobalCounter.exists(tail_errorlist, ds.getId())){
				String ds_ab_watch = propertyEngine.readById("ds_ab_watch");
				String ds_ab_expire_sec = propertyEngine.readById("ds_ab_expire_sec");
				GlobalCounter.regist(tail_errorlist, ds.getId(), Integer.parseInt(ds_ab_expire_sec));
				String forwardDsId = findABDs(ds_ab_watch,ds.getId());
				if(!forwardDsId.equalsIgnoreCase(ds.getId())){
					try {
						senEmailService.sendEmails(emails, ds_error_on_sms +"\n当前已熔断："+ds.getId()
								+"\n流量切换至："+forwardDsId
								+"\n规则配置："+ds_ab_watch
								+"\n预计恢复时间："+new SimpleDateFormat(DateUtil.DATETIMESHOWFORMAT).format(new Date(System.currentTimeMillis()+(Integer.parseInt(ds_ab_expire_sec)*1000)))
								+"\n报告时间:"+new SimpleDateFormat(DateUtil.DATETIMESHOWFORMAT).format(new Date()),"CTE切流提示");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					logger.warn("{} 数据源重定向策略准备开始...{}",prefix,ds_ab_watch);
				}
			}
			if(isFuse){
				return false;
			}
			return true;
		}
		return false;
	}
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
	public boolean isErr(Map<String, Object> result,PropertyUtil propertyEngine){
		String ds_errors_watch = propertyEngine.readById("ds_errors_watch");
		Object obj_retstatus=result.get(Conts.KEY_RET_STATUS);
		if(obj_retstatus!=null&&obj_retstatus instanceof CRSStatusEnum){
			String status_code =((CRSStatusEnum)obj_retstatus).getRet_sub_code();
			if(StringUtil.areNotEmpty(status_code,ds_errors_watch)
					&&StringUtil.isStrInStrs(status_code, ds_errors_watch.split(","))){ 
				return true;
			}
		}
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
