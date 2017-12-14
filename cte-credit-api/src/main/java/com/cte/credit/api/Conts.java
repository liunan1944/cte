package com.cte.credit.api;

/**
 * 常量配置
 */
public class Conts
{
  //ds数据源日志表调用结果状态
  public static final String TRADE_STATE_SUCC="00";
  public static final String TRADE_STATE_FAIL="01";
  public static final String TRADE_STATE_TIMEOUT="02";
	
  public static final String[] WK_ALIAS = new String[]{"20008","20009"};
  public static final String WS_VERSION = "1.0";
  public static final String USER_ADMIN_NAME = "sys_admin";
  public static final String USER_ADMIN_API_KEY = "1FEA16F0770C038AE053F40FA8C0E573";
  public static final int COUNTER_REDIS_EXPIRE = 60 * 60 * 24;
  public static final int MODEL_REDIS_EXPIRE = 60 * 5;
  public static final int SMS_TEMPLATE_ID = 19714437;
  public static final String SUPPORT_OPER_ID = "app_support";
  public static final String KEY_RET_STATUS = "retstatus";
  //系统返回码
  public static final String KEY_RET_TAG = "rettag";
  public static final String KEY_RET_CODE = "retcode";
  public static final String KEY_RET_MSG = "retmsg";
  public static final String KEY_RET_DATA = "retdata";
  public static final String KEY_RET_DATE = "retdate";
  public static final String KEY_RET_RSP_SN = "response_sn";
  public static final String KEY_RET_REQ_SN = "request_sn";
  public static final String KEY_RET_VERSION = "version";
  
  public static final String KEY_RET_BUG = "retbug";
  public static final String KEY_PRE_RET_DESC = "上一阶段计算结果";
  public static final String KEY_PRE_RET_CODE = "pre_retcode";
  public static final String KEY_PRE_RET_MSG = "pre_retmsg";
  public static final String KEY_PRE_RET_DATA = "pre_retdata";
  public static final String KEY_PRE_RET_BUG = "pre_retbug";
  public static final String KEY_WS_HEADER = "【CRS WS ADAPTER】";
  public static final String KEY_SUPPORT_HEADER = "【CRS GATE ADAPTER】";
  public static final String KEY_QUARTZ_HEADER = "【CRS QUARTZ RUNNER】";
  public static final String KEY_ROUTE_INNER_HEADER = "【CRS INNER ROUTE】";
  public static final String KEY_ROUTE_OUTER_HEADER = "【CRS OUTER ROUTE】";
  public static final String KEY_ROUTE_CUSTOM_HEADER = "【CRS CUSTOM ROUTE】";
  public static final String KEY_SCORE_HEADER = "【CRS SCORE ADAPTER】";
  public static final String KEY_CORE_HEADER = "【CRS CORE ADAPTER】";
  public static final String KEY_CUSTOM_HEADER = "【CRS CUSTOM ADAPTER】";
  public static final String KEY_SEEKER_HEADER = "【CRS SEEKER ADAPTER】";
  public static final String KEY_SYS_AGENT_HEADER = "【CRS AGENT ADAPTER】";
  public static final String KEY_PRD_CODE = "prd_code";
  public static final String KEY_TEMPLATE_HEADER = "信用评级产品阵列";
  public static final String KEY_DS_AGENT_POOL_ID = "DS_POOL";
  public static final String KEY_DS_EVENTBUS_ID = "DS_EVENT_BUS";
  public static final String KEY_MODEL_AGENT_POOL_ID = "MODEL_POOL";
  public static final String KEY_QUARTZ_AGENT_POOL_ID = "QUARTZ_POOL";
  public static final String KEY_CUSTOM_AGENT_POOL_ID = "CUSTOM_POOL";
  public static final String KEY_RUN_MODEL_CX = "串行";
  public static final String KEY_RUN_MODEL_BX = "并行";
  public static final String KEY_STATUS_SUCCESS = "000000";
  public static final String KEY_STATUS_WARN_L1 = "000001";
  public static final String KEY_STATUS_WARN_L2 = "000002";
  public static final String KEY_STATUS_WARN_L3 = "000003";
  public static final String KEY_STATUS_FAILED = "-99999";
  public static final String KEY_POLICE_TARGET_NAME = "name";
  public static final String KEY_POLICE_TARGET_CARDNO = "cardNo";
  public static final String KEY_POLICE_PARAMS_KEY = "ossData";
  public static final String KEY_TARGET_IMAGES = "images";
  public static final String KEY_YITU_REGIST_URL = "/face/v1/user/database_image";
  public static final String KEY_YITU_VERIF_URL = "/face/v1/service/user/face_verification";
  public static final String KEY_SYS_MONITOR_SYS_ID = "sys";
  public static final String KEY_SYS_MONITOR_MODEL_TYPE = "ml_c";
  public static final String KEY_SYS_MONITOR_DS_TYPE = "ds_c";
  public static final String KEY_SYS_MONITOR_MODEL_NAME = "模型计算监控";
  public static final String KEY_SYS_MONITOR_DS_NAME = "数据源计算监控";
  public static final String KEY_SYS_MODEL_API_KEY ="87998277131";
  public static final String KEY_SYS_MODEL_API_SECRET ="78209973728";
  public static final String KEY_SYS_MODEL_CODE_PARAMS_DEFINE ="M_core_params";
  public static final String KEY_SYS_MODEL_CODE_MODEL_DEFINE ="M_sys_prod_template";
  public static final String KEY_SYS_MODEL_CODE_ROUTE_DEFINE ="M_sys_prod_q_route";
  public static final String KEY_SYS_MODEL_CODE_PROPERTY_DEFINE ="M_sys_properties";
  public static final String KEY_SYS_MODEL_CODE_KYCPROPERTY_DEFINE ="M_sys_kycproperties";
  public static final String KEY_SYS_MODEL_SUBCODE_PARAMSIN_DEFINE ="params_in";
  public static final String KEY_SYS_MODEL_SUBCODE_DS_DEFINE ="ds";
  public static final String KEY_SYS_MODEL_SUBCODE_DS_RATIO_DEFINE ="assignedRatio";
  public static final String KEY_SYS_MODEL_SUBCODE_MC_DEFINE ="mc";
  public static final String KEY_SYS_MODEL_SUBCODE_RO_DEFINE ="result_out";
  //运营商原始数据其他手机号码软加密密钥 
  public static final String KEY_DESENC_KEY = "wandacredit";
  
  /***************************标签常量定义******************************/
  public static final String TAG_UNSUPPORT = "unsupport";
  public static final String TAG_SYS_ERROR = "error";
  public static final String TAG_SYS_TIMEOUT = "time_out";
  public static final String TAG_INCACHE_FOUND = "incache_found";
  public static final String TAG_INCACHE_UNFOUND = "incache_unfound";
  public static final String TAG_INCACHE_MATCH = "incache_match";
  public static final String TAG_INCACHE_UNMATCH = "incache_unmatch";
  
  public static final String TAG_FOUND = "found";
  public static final String TAG_UNFOUND = "unfound";
  public static final String TAG_UNFOUND_OTHERS = "unfound_others";
  public static final String TAG_FOUND_OLDRECORDS = "found_oldrecords";
  public static final String TAG_FOUND_NEWRECORDS = "found_newrecords";
  public static final String TAG_TST_SUCCESS = "tst_success";
  public static final String TAG_INCACHE_TST_SUCCESS = "incache_tst_success";
  public static final String TAG_TST_PROCESS = "tst_process";
  public static final String TAG_TST_FAIL = "tst_fail";
  public static final String TAG_MATCH = "match";
  public static final String TAG_UNMATCH = "unmatch";
  public static final String TAG_BASIC_FOUND = "basic_found";
  public static final String TAG_BASIC_UNFOUND = "basic_unfound";
  public static final String TAG_BASIC_UNFOUND_OTHERS = "basic_unfound_others";
  public static final String TAG_STATUS_FOUND = "status_found";
  public static final String TAG_STATUS_UNFOUND = "status_unfound";
  public static final String TAG_STATUS_UNFOUND_OTHERS = "status_unfound_others";
  public static final String TAG_CAR_MODELS_MATCH = "car_m_match";
  public static final String TAG_CAR_MODELS_UNMATCH = "car_m_unmatch";
  public static final String TAG_CAR_MODELS_UNFOUND = "car_m_unfound";
  public static final String TAG_CAR_MODELS_UNFOUND_OTHERS = "car_m_unfound_others";
  public static final String TAG_INITIAL_DATE_MATCH = "in_d_match";
  public static final String TAG_INITIAL_DATE_UNMATCH = "in_d_unmatch";
  public static final String TAG_INITIAL_DATE_UNFOUND = "in_d_unfound";
  public static final String TAG_INITIAL_DATE_UNFOUND_OTHERS = "in_d_unfound_others";
  public static final String TAG_ARCH_NUMBER_MATCH = "ar_n_match";
  public static final String TAG_ARCH_NUMBER_UNMATCH = "ar_n_unmatch";
  public static final String TAG_ARCH_NUMBER_UNFOUND = "ar_n_unfound";
  public static final String TAG_ARCH_NUMBER_UNFOUND_OTHERS = "ar_n_unfound_others";
  
  public static final String TAG_CAR_DETAIL_FOUND = "car_d_found";
  public static final String TAG_CAR_DETAIL_UNFOUND = "car_d_unfound";
  public static final String TAG_CAR_DETAIL_UNFOUND_OTHERS = "car_d_unfound_others";
  public static final String TAG_REGIST_TIME_MATCH = "re_t_match";
  public static final String TAG_REGIST_TIME_UNMATCH = "re_t_unmatch";
  public static final String TAG_REGIST_TIME_UNFOUND = "re_t_unfound";
  public static final String TAG_REGIST_TIME_UNFOUND_OTHERS = "re_t_unfound_others";
  public static final String TAG_VIN_MATCH = "vin_match";
  public static final String TAG_VIN_UNMATCH = "vin_unmatch";
  public static final String TAG_VIN_UNFOUND = "vin_unfound";
  public static final String TAG_VIN_UNFOUND_OTHERS = "vin_unfound_others";
  public static final String TAG_BASIC_INCACHE_FOUND = "basic_incache_found";
  public static final String TAG_YL_00 = "tst_success_00";//银联画像：计费
  public static final String TAG_YL_01 = "tst_success_01";//银联画像：不计费
  
}