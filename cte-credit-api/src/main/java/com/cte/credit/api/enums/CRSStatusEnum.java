package com.cte.credit.api.enums;

public enum CRSStatusEnum {
	//系统级
	STATUS_SUCCESS		("000000","000000","交易成功","交易成功"),
	STATUS_FAILED		("-99999","err_000","交易失败","建议联系CTE公司人员"),
	
	//应用系统级别
	STATUS_SYS_APP_PARAM		("-99999","000010","输入参数错误","建议联系CTE公司人员"),
	STATUS_SYS_APP_REQEUST_SN   ("-99999","000011","请求交易号重复","建议联系CTE公司人员"),
	STATUS_SYS_APP_FAILED		("-99999","200000","调用远程系统异常","建议联系CTE公司人员"),
	STATUS_SYS_ACCT_FAILED		("-99999","200010","用户不存在或状态不存在","建议联系CTE公司人员"),
	STATUS_SYS_PROD_FAILED		("-99999","200011","用户没有找到产品的订购关系","建议联系CTE公司人员"),
	STATUS_SYS_PROD_TEST_FAILED	("-99999","200012","授信条数已用完","建议联系CTE公司人员"),
	STATUS_SYS_ACCT_LOCK		("-99999","200013","账号冻结，请联系相关CTE公司人员","建议联系CTE公司人员"),
	STATUS_SYS_ACCT_KEY		("-99999","200014","账号信息错误","建议联系CTE公司人员"),
	STATUS_SYS_ACCT_COST_LOCK	("-99999","200016","余额不足","建议联系CTE公司人员"),
	
	STATUS_FAILED_SYS_ACCT_OUTOFBALANCE("-99999","err_001","您的账户余额不足!","建议联系CTE公司人员"),
	STATUS_FAILED_SYS_ACCT_NOTEXISTS_KEYINVALID("-99999","err_002","您的账户不存在或未被授权!","建议联系CTE公司人员"),
	STATUS_FAILED_SYS_ACCT_LOCKED("-99999","err_003","您的账户被锁定","建议联系CTE公司人员"),
	STATUS_FAILED_SYS_ACCT_LOGOUT("-99999","err_004","您的账户被注销","建议联系CTE公司人员"),
	STATUS_FAILED_SYS_ACCT_CLOSE("-99999","err_005","您的账户被销户","建议联系CTE公司人员"),
	STATUS_FAILED_SYS_ACCT_EXPIRED("-99999","err_006","您的账户已到期","建议联系CTE公司人员"),
	STATUS_FAILED_SYS_SIGN_INVALID("-99999","err_007","数字签名校验不通过","建议联系CTE公司人员"),
	STATUS_FAILED_SYS_CALLBACK_FAILED("-99999","err_008","回调客户端失败","建议联系CTE公司人员"),
	STATUS_FAILED_SYS_DELIVE_FAILED("-99999","err_009","消息路由投递失败","建议联系CTE公司人员"),
	
	
	
	STATUS_FAILED_SYS_PROD_NOLIMIT("-99999","err_010","校验不通过:您没有权限查询此产品","建议征信组联系人"),
	STATUS_FAILED_SYS_PROD_NOTEXISTS("-99999","err_011","校验不通过:无合适的适配产品","建议参考接口文档进行核对"),
	STATUS_FAILED_SYS_PARAM_INVALID("-99999","err_012","校验不通过:传入参数不正确","建议参考接口文档进行核对"),
	STATUS_FAILED_SYS_PRODUCT_EXPIRED("-99999","err_013","校验不通过:对应产品已过期","原因可能是产品已失效或已下架"),
	STATUS_FAILED_SYS_MODEL_EXE_FAILED("-99999","err_014","模型计算请求失败,请联系系统管理员","原因可能是某个模型计算请求处理时异常,请及时联系."),
	STATUS_FAILED_SYS_DS_EXCEPTION("-99999","err_015","数据源查询异常!","建议联系CTE公司人员"),
	STATUS_FAILED_SYS_MC_EXCEPTION("-99999","err_016","模型计算请求时出现异常!","建议联系CTE公司人员"),
	STATUS_FAILED_SYS_DS_TIMEOUT("-99999","err_017","数据源请求超时!","建议联系CTE公司人员"),
	STATUS_FAILED_SYS_MC_TIMEOUT("-99999","err_018","模型计算请求超时!","建议联系CTE公司人员"),
	STATUS_FAILED_SYS_DS_NOT_MATCHED("-99999","err_019","无合适的数据源驱动!","建议联系CTE公司人员"),
	STATUS_FAILED_SYS_CUSTOM_NOT_MATCHED("-99999","err_020","无合适的定制服务实现!","建议联系CTE公司人员"),	
	
	STATUS_FAILED_SYS_TOKEN_ERR("-99999","err_021","校验不通过:您的令牌已失效或许可次数已到!","请联系您的客户经理"),
	STATUS_FAILED_SYS_SIGN_ERR("-99999","err_022","校验不通过:您的签名安全校验未通过!","请参考接口文档进行调整"),
	STATUS_FAILED_SYS_CRT_INVALID("-99999","err_023","校验不通过:您的证书安全校验未通过!",null),
	STATUS_FAILED_SYS_IP_NOTALLOW("-99999","err_024","校验不通过:您的IP安全校验未通过!",null),
	STATUS_FAILED_SYS_CRT_NOTMATCHED("-99999","err_025","校验不通过:服务器证书库中尚未配置!",null),
	STATUS_FAILED_SYS_IP_NOTEXISTS("-99999","err_026","校验不通过:您的账号尚未添加IP白名单库!",null),
	
	//集成征信存在性校验
	STATUS_FAILED_SYS_ACCT_ID_EXISTS("-99999","err_027","校验不通过:账户已存在","请勿重复提交请求，或检查您的账户ID"),
	STATUS_FAILED_SYS_API_KEY_EXISTS("-99999","err_028","校验不通过:授权KEY已存在","请检查您的授权KEY"),
	STATUS_FAILED_SYS_ACCT_ID_NOTEXISTS("-99999","err_029","校验不通过:账户不存在","请检查您的账户ID"),
	
	STATUS_FAILED_SYS_LIMIT_REACHED("-99999","err_030","系统繁忙,请稍后再试","系统繁忙,请稍后再试"),
	STATUS_FAILED_SYS_DELIVE_NOT_MACTHED("-99999","err_031","不支持的定制产品","建议联系CTE公司人员"),
	//业务级(数据源请求)
	STATUS_FAILED_DS_POLICE_EXCEPTION("000001","warn_001","身份核验请求失败!","建议联系CTE公司人员"),
	STATUS_WARN_DS_POLICE_INVALID("000001","warn_002","申请人身份证,姓名校验不一致","建议再次核对输入信息"),
	STATUS_WARN_DS_POLICE_NOTEXISTS("000001","warn_003","申请人身份证号码校验不存在","建议再次核对输入信息"),
	STATUS_WARN_DS_POLICE_PHOTO_NOTEXISTS("000001","warn_004","申请人户籍照片不存在","建议再次核对输入信息"),
	STATUS_FAILED_DS_YITU1_EXCEPTION("000001","warn_005","登记照片失败!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_YITU2_EXCEPTION("000001","warn_006","人脸识别失败!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_SJQ1_EXCEPTION("000001","warn_007","个人手机号风险验证返回失败","建议联系CTE公司人员"),
	STATUS_FAILED_DS_SJQ2_EXCEPTION("000001","warn_008","银行卡号欺诈验证返回失败","建议联系CTE公司人员"),
	STATUS_FAILED_DS_PAINT_EXCEPTION("000001","warn_009","银联用户画像返回失败","建议联系CTE公司人员"),
	STATUS_FAILED_DS_EDU_EXCEPTION("000001","warn_010","学历查询返回失败","建议联系CTE公司人员"),
	STATUS_FAILED_DS_EDU_NOTFOUND_EXCEPTION("000001","warn_011","没有查询到此人的任何学历信息,请核实!","建议核对输入信息"),
	STATUS_FAILED_DS_ZS_B_NOTFOUND_EXCEPTION("000001","warn_012","没有查询到该营业执照号码或社会统一信用代码的信息,请核实!","建议核对输入信息"),
	STATUS_FAILED_DS_ZS_C_NOTFOUND_EXCEPTION("000001","warn_013","没有查询到该身份证号码的任何资信信息,请核实!","建议核对输入信息"),
	STATUS_FAILED_DS_PHONE_EXCEPTION("000001","warn_014","手机核查与状态查询失败","建议联系CTE公司人员"),
	STATUS_FAILED_DS_PHONE_NOTFOUND_EXCEPTION("000001","warn_015","没有查询到该手机号码的任何信息,请核实!","建议核对输入信息"),
	STATUS_FAILED_DS_SMS_EXCEPTION("000001","warn_016","短信批量发送失败!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_NOT_OPENED_EXCEPTION("000001","warn_017","暂不向此人开放该产品","建议联系CTE公司人员"),
	STATUS_FAILED_DS_CREDIT_EXCEPTION("000001","warn_018","信用分请求计算失败!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_YL_NOTFOUND_EXCEPTION("000001","warn_019","银联反馈：未找到对应的商户!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_PHONE_ONLY_CHECK_EXCEPTION("000001","warn_020","手机核查查询失败","建议联系CTE公司人员"),
	STATUS_FAILED_DS_HUIFA1_EXCEPTION("000001","warn_021","个人或企业精确查询失败!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_HUIFA2_EXCEPTION("000001","warn_022","个人、企业信息批量录入失败!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_HUIFA3_EXCEPTION("000001","warn_023","个人、企业批量查询结果失败!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_BCC1_EXCEPTION("000001","warn_024","报告正在准备中，请稍候重新提交!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_ZHENGTONG_EXCEPTION("000001","warn_025","人脸识别失败!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_HUIFA4_EXCEPTION("000001","warn_026","扣费查询结果失败!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JINGZHONG_EXCEPTION("000001","warn_027","飞行数据查询异常!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JINGZHONG_FAIL("000001","warn_028","飞行数据查询失败!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JINGZHONG_NULL("000001","warn_029","飞行数据未查得!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JINGZHONG_IHCACHE_NULL("000001","warn_030","没有查询到乘机记录!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JINGZHONG_IHCACHE_INVALID("000001","warn_031","姓名、身份证号码不匹配!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JINGZHONG_IHCACHE_NOTEXISTS("000001","warn_032","未找到该证件信息!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_QIANHAI_LOANEE_EXCEPTION("000001","warn_034","常贷客数据源请求失败!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_QIANHAI_BLACKLIST_EXCEPTION("000001","warn_035","黑名单数据源请求失败!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_QIANHAI_CREDOO_EXCEPTION("000001","warn_036","好信度数据源请求失败!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_YITU3_EXCEPTION("000001","warn_037","人脸识别次数达到上限","建议联系CTE公司人员"),
	STATUS_FAILED_DS_YITU4_EXCEPTION("000001","warn_038","上传照片不符合要求","建议重新进行拍摄"),
	STATUS_FAILED_DS_YITU_PTYPE_ERROR("000001","warn_039","照片类型不符合要求","建议重新进行拍摄"),
	STATUS_FAILED_DS_ZS_P_NOTFOUND_EXCEPTION("000001","warn_040","没有查询到该企业和人员的任何信息,请核实!","建议核对输入信息"),
	STATUS_WARN_DS_POLICE_LOCAL_NOTEXISTS("000001","warn_042","申请人本地未查得","建议再次核对输入信息"),
	STATUS_WARN_DS_POLICE_PARAM_FAILED("000001","warn_043","传入参数不符合要求","建议再次核对输入信息"),
	STATUS_WARN_DS_JIAO_NAME_ERROR("000001","warn_044","姓名格式错误","建议再次核对输入信息"),
	STATUS_WARN_DS_JIAO_NO_RESULT("000001","warn_045","查无此记录","建议联系CTE公司人员"),
	STATUS_WARN_DS_JIAO_NAME_LM_ERROR("000001","warn_046","姓名不能为乱码","建议再次核对输入信息"),
	STATUS_WARN_DS_ENCODE_FAILED("000001","warn_047","传入字符串为空","建议再次核对输入信息"),
	STATUS_FAILED_DS_EDU_PHOTO_NOTFOUND("000001","warn_048","学历照片不存在!","建议核对输入信息"),
	//add houxiaobin 20171010
	STATUS_FAILED_DS_NO_MATCH_ATTRIBUTE("000001","warn_049","没有匹配的运营商!","建议核对输入信息"),
	STATUS_FAILED_DS_NOT_SUPPORT_VNO("000001","warn_050","暂不支持虚拟运营商!","建议核对输入信息"),
	
	STATUS_FAILED_DS_JIEWEI_INVALIDCOND_EXCEPTION("000001","warn_051","查询条件不足，请核对输入信息是否完整","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JIEWEI_INVALIDBUSILICNO_EXCEPTION("000001","warn_052","营业执照号输入错误","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JIEWEI_INVALIDCREDITNO_EXCEPTION("000001","warn_053","统一社会信用代码输入错误","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JIEWEI_QUERYCORPINFO_EXCEPTION("000001","warn_054","数据查询失败","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JIEWEI_QUERYTIMEOUT_EXCEPTION("000001","warn_055","数据查询超时","建议联系CTE公司人员"),
	STATUS_FAILED_DS_SEQU_NUMB_ERROR("000001","warn_056","序列号不符合规范","建议联系CTE公司人员"),
	STATUS_WARN_DS_GUOZT_CRIME_NORECORD("000001","warn_057","查询成功,未查询到犯罪吸毒黑名单记录","建议联系CTE公司人员"),
	STATUS_WARN_DS_GUOZT_EDUCATION_NORECORD("000001","warn_058","查询成功,未查询到学历信息","建议联系CTE公司人员"),
	STATUS_WARN_DS_MOBILE_NO_ERROR("000001","warn_059","您输入的手机号码无效，请核对后重新输入!","建议联系CTE公司人员"),
	
	//银联商户画像业务提示返回码MerchantUnionPayPaint
	STATUS_FAILED_DS_UNIONPAY_MERPAINT_MID_ERROR("000001","warn_060","未查询到结果，可能是MID输入错误","建议联系CTE公司人员"),
	STATUS_FAILED_DS_UNIONPAY_PERSON_CARDID_ERROR("000001","warn_061","未查询到结果，可能是卡号输入错误","建议联系CTE公司人员"),
	STATUS_FAILED_DS_CARDID_ERROR("000001","warn_062","银行卡卡号格式错误","建议联系CTE公司人员"),
	STATUS_FAILED_DS_CARDNO_ERROR("000001","warn_063","证件号码格式错误","建议联系CTE公司人员"),
	STATUS_FAILED_DS_CARDNO_TYPE_ERROR("000001","warn_064","证件类型格式错误","建议联系CTE公司人员"),
	STATUS_FAILED_DS_ZS_B_NAME_EXCEPTION("000001","warn_070","没有查询到该企业名称的任何信息,请核实!","建议核对输入信息"),
	STATUS_FAILED_DS_ZS_B_CODE_EXCEPTION("000001","warn_071","没有查询到该组织机构代码的任何信息,请核实!","建议核对输入信息"),
	STATUS_FAILED_DS_ZS_B_EXCEPTION("000001","warn_072","未查到营业执照号和企业名称下的任何信息，请核实！","建议核对输入信息"),
	STATUS_FAILED_DS_ZS_B_VALID("000001","warn_073","企业工商注册号、社会统一信用代码或组织机构代码输入错误！","请核实!"),
	STATUS_FAILED_DS_ZS_B_IDVALID("000001","warn_074","身份证输入有误，请核实!","建议核对输入信息"),
	STATUS_FAILED_DS_ZS_B_MarkVALID("000001","warn_075","企业标识输入有误，请核实!","建议核对输入信息"),
	//众安保险业务码
	STATUS_FAILED_DS_ZHONGAN_CODE_EXCEPTION("000001","warn_080","入参信息有误,请核实!","建议核对输入信息"),
	//国政通租车业务码
	STATUS_FAILED_DS_GUOZT_CAR_FAIL("000001","warn_090","租车黑名单信息查询失败!","建议核对输入信息"),
	//聚信立对应请求返回码从100开始,其他请求返回码继续沿用原来的返回码
	STATUS_FAILED_DS_JUXINLI_REQUESTID_NOTEXSIT("000001","warn_100","request_id不存在或未完成采集请求,请使用正确的request_id!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JUXINLI_REQUESTID_NOUSE("000001","warn_101","request_id失效,请重新开始流程!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JUXINLI_REPEAT_SUBMIT("000001","warn_102","该数据源已提交采集请求!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JUXINLI_ILLEGAL_SUBMIT("000001","warn_103","非法采集请求!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JUXINLI_PAW_ERROR("000001","warn_104","密码错误,请确认后重新输入!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JUXINLI_NEED_CAPTCHA("000001","warn_105","请输入动态验证码!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JUXINLI_CAPTCHA_ERROR("000001","warn_106","动态验证码错误!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JUXINLI_CAPTCHA_NOUSE("000001","warn_107","动态验证码失效已重新下发!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JUXINLI_PAW_SIMPLE("000001","warn_108","密码过于简单无法登陆,请联系运营商修改密码!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JUXINLI_WEBSITE_ERROR("000001","warn_109","运营商或电商网站异常无法获取数据!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JUXINLI_SUBMIT_ING("000001","warn_110","未完成所有提交采集请求!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JUXINLI_IDCARD_ERROR("000001","warn_111","您输入的为无效身份证号码，请核对后重新输入!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JUXINLI_CAPTCHATYPE_ERROR("000001","warn_112","参数captcha_type错误，请确认后重新输入!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JUXINLI_NO_REPORTDATA("000001","warn_113","没有对应的报告数据,可能未提交采集请求或采集请求未完成!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JUXINLI_ACCESS_TOKEN_ERROR("000001","warn_114","生成安全码失败，请稍后再试!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JUXINLI_GS_NO_DATASOURCES("000001","warn_115","暂时没有支持的工商网站信息!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JUXINLI_GS_NO_DATA("000001","warn_116","没有该企业工商信息数据!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JUXINLI_PARAM_ERROR("000001","warn_117","传入参数错误!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JUXINLI_NO_NECESSARY_PARAMTER("000001","warn_118","缺少必要参数!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JUXINLI_JILIN_GET_CAPTCHA("000001","warn_119","请用本机发送CXXD至10001获取查询详单的验证码!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JUXINLI_JILIN_GET_CAPTCHA_AGAIN("000001","warn_120","验证码失效请用本机发送CXXD至10001获取查询详单的验证码!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JUXINLI_SUB_AGAIN("000001","warn_121","需要再次提交采集请求!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JUXINLI_NEED_CAPTCHA_AGAIN("000001","warn_122","请再次输入短信验证码!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JUXINLI_NEED_QUERY_PWD("000001","warn_123","请输入查询密码!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_JUXINLI_QUERY_PWD_ERROR("000001","warn_124","查询密码错误，请核对后重新输入!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_MOBILE_NO_SUPPORT("000001","warn_125","暂不支持此运营商!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_MOBILE_NO_SEARCH("000001","warn_126","查无此记录","建议联系CTE公司人员"),
	
	STATUS_FAILED_DS_ZT_BANKCARD_AUTHEN_EXCEPTION("000001","warn_130","银行卡鉴权失败!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_BANKCARD_AUTHEN_NOTSUPPORT("000001","warn_131","不支持该银行卡验证!","建议联系CTE公司人员"),

	STATUS_FAILED_DS_PY_CARILLEGAL_EXCEPTION("000001","warn_140","车辆违章信息查询失败!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_PY_CARILLEGAL_NORECORD("000001","warn_141","未查询到车辆违章记录!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_PY_DRIVERLICENSE_EXCEPTION("000001","warn_150","驾驶证信息核查失败!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_PY_CARINFO_EXCEPTION("000001","warn_160","车辆信息核查失败!","建议联系CTE公司人员"),
	
	STATUS_FAILED_DS_PY_CITY_MAINTENANCE("000001","warn_161","查询城市正在维护或不支持!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_PY_EMPTY("000001","warn_162","车辆信息不存在!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_PY_PARAMETER_ERROR("000001","warn_163","输入参数错误!","建议联系CTE公司人员"),

	STATUS_FAILED_DS_YX_BLACKLIST_EXCEPTION("000001","warn_180","风险名单息查询失败!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_YX_BLACKLIST_NOTFOUND("000001","warn_181","未查询到风险名单信息!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_YX_LOANRECORD_EXCEPTION("000001","warn_182","借款信息查询失败!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_YX_LOANRECORD_NOTFOUND("000001","warn_183","未查询到借款信息!","建议联系CTE公司人员"),

	STATUS_FAILED_PRD_RISKINFO_EXCEPTION("000001","warn_190","风险名单信息查询失败!","建议联系CTE公司人员"),
	STATUS_FAILED_PRD_RISKINFO_NOTFOUND("000001","warn_191","未查询到风险名单信息!","建议联系CTE公司人员"),
	STATUS_FAILED_PRD_RISKINFO_REPORT_INVALID("000001","warn_192","风险名单信息上送失败,数据格式校验不通过!","建议联系CTE公司人员"),
	STATUS_FAILED_CARADNO_INVALID("000001","warn_193","身份证号格式不正确","建议联系CTE公司人员"),
	STATUS_FAILED_NO_CMCC_MOBILE("000001","warn_194","非移动号段，请核对后重新输入","建议联系CTE公司人员"),

	STATUS_FAILED_SPECIALAMTACT_NOTFOUND("000001","warn_200","找不到该会员的快易花申请记录","建议联系CTE公司人员"),
	STATUS_FAILED_ANTI_FRAUD_FAILED("000001","warn_210","反欺诈认证失败","建议联系CTE公司人员"),
	//信用扫描仪业务码
	STATUS_WARN_DS_SCAN_RISK_FAIL("000001","warn_220","信用扫描仪接口查询失败!","建议联系CTE公司人员"),
	STATUS_WARN_DS_SCAN_SCORE_FAIL("000001","warn_221","获取信用分失败!","建议联系CTE公司人员"),

	STATUS_FAILED_DS_JIEWEI_CORPNAME_NOTMATCH("000001","warn_230","企业名称不存在，请输入完整且正确的企业名称","建议联系CTE公司人员"),
	STATUS_FAILED_PRD_CORP_NOTFOUND("000001","warn_231","没有查询到该企业的任何信息,请核实!","建议联系CTE公司人员"),
	STATUS_FAILED_MODULE_QUERY("000001","warn_232","模块信息查询失败","建议联系CTE公司人员"),
    //(鹏元)企业知识产权一般查询（马飞-add 2017/10/13）
	STATUS_WARN_DS_PENYUAN_PARAM_FAILED("000001","warn_235","参数不正确(非法参数)","建议再次核对输入信息"),
	STATUS_WARN_DS_PENYUAN_PARAMETER_FAILED("000001","warn_236","参数不正确(格式错误，或不规范)","建议再次核对输入信息"),
	STATUS_WARN_DS_PENYUAN_NO_RESULT("000001","warn_237","未查到任何知识产权信息","请联系万达征信客户经理!"),
	
	//滴滴租车业务码
	STATUS_FAILED_DS_DIDI_CAR_FAIL("000001","warn_240","滴滴回调信息请求失败!","建议联系CTE公司人员"),
	//网数生物识别业务码
	STATUS_FAILED_DS_WANDA_FACE_FAIL("000001","warn_250","人脸识别服务请求异常!","建议联系CTE公司人员"),
	STATUS_FAILED_DS_WANDA_OCR_FAIL("000001","warn_251","身份证OCR服务请求失败!","建议联系CTE公司人员"),
	
	//照片解析
	STATUS_FAILED_DS_YITU_PHOTO_NOTANLASIS("000001","warn_252","照片无法解析","建议联系CTE公司人员"),
	
	//光线不足
	STATUS_FAILED_DS_YITU_INSUFFICIENT_LIGHT("000001","warn_253","光线条件不足","建议联系CTE公司人员"),
	
	//误报率
	STATUS_FAILED_DS_YITU_PARAMETER_ERRO("000001","warn_255","误报率填入有误","建议联系CTE公司人员"),	
	//疑似风险用户
	STATUS_FAILED_DS_YITU__RISK_USER("000001","warn_254","疑似风险用户,请关注","建议联系CTE公司人员"),	
	//识别错误
	STATUS_FAILED_DS_YIDAO_RECOGNITION_ERRO("000001","warn_258","图片识别错误,请关注","建议联系CTE公司人员"),	
	//无合格图片
	STATUS_FAILED_DS_YIDAO_PICTURE_ERRO("000001","warn_259","无合格图片,请关注","建议联系CTE公司人员"),
	//输入证件类型错误
	STATUS_FAILED_DS_YIDAO_RECOTYPE("000001","warn_262","输入证件类型有误,请关注","建议联系CTE公司人员"),
	//输入身份证对应面错误
	STATUS_FAILED_DS_RIGHT_WRONG("000001","warn_263","输入身份证对应面有误,请关注","建议联系CTE公司人员"),
	//入参不符合要求
	STATUS_FAILED_DS_VIDEOFACE_ERRO("000001","warn_264","入参不符合要求,请关注","建议联系CTE公司人员"),
	//活体检测未通过
	STATUS_FAILED_DS_CHECK_FAILED("000001","warn_265","活体检测未通过,请关注","建议联系CTE公司人员"),
	//学历核查业务码
	STATUS_FAILED_DS_EDU_CHECK_FAIL("000001","warn_280","学历核查失败","建议联系CTE公司人员"),
	//外国人验真业务码
	STATUS_FAILED_DS_WGR_CHECK_FAIL("000001","warn_290","错误的国家编码","建议联系CTE公司人员"),
	//非凡信用分
	STATUS_FAILED_SCORE_NOTFOUND("000001","warn_600","未查询到信用分信息","建议联系CTE公司人员"),
	STATUS_FAILED_HISSCORE_INVALIDPARAMS("000001","warn_601","身份证和手机号不能同时为空","建议联系CTE公司人员"),
	STATUS_FAILED_FFSCOREFLAG_NOTFOUND("000001","warn_602","未查询到非凡信用分标签","建议联系CTE公司人员"),
	STATUS_FAILED_PERSON_INVALIDPARAMS("000001","warn_601","企业名称和人员姓名和人员id不能为空","建议联系CTE公司人员"),
	STATUS_FAILED_VERIFYCODE_INVALID("000001","warn_605","短信验证码已失效，请重新验证","建议联系CTE公司人员"),
	STATUS_FAILED_VERIFYCODE_INCOREECT("000001","warn_606","短信验证码输入错误，请重新输入","建议联系CTE公司人员"),
	STATUS_FAILED_PWID_INCOREECT("000001","warn_607","未查询到此账号信息，请重新输入","建议联系CTE公司人员"),

	//国政通就业潜力查询业务码
	STATUS_FAILED_DS_GUOZT_JYQL_FAIL("000001","warn_270","就业潜力核查失败","建议联系CTE公司人员"),
	STATUS_FAILED_DS_GUOZT_JYQL_EVAL01("000001","warn_271","信息有误,无法评估","建议联系CTE公司人员"),
	STATUS_FAILED_DS_GUOZT_JYQL_EVAL02("000001","warn_272","信息无误,评估异常","建议联系CTE公司人员"),
	
	//内部标签查询业务码
	STATUS_FAILED_DS_TAG_FAIL("000001","warn_281","您输入的标签ID有误,请重新输入","建议联系CTE公司人员"),
	STATUS_FAILED_DS_TAG_PARAM_FAIL("000001","warn_282","未查得相关信息","建议联系CTE公司人员"),
	//启信宝企业查询业务码
	STATUS_FAILED_DS_QIXINBAO_REGIST_FAIL("000001","warn_260","企业注册号输入错误","建议联系CTE公司人员"),
	STATUS_FAILED_DS_QIXINBAO_CODE_FAIL("000001","warn_261","组织机构代码输入错误","建议联系CTE公司人员"),

	//企业用电量
	STATUS_FAILED_ELEC_FAIL01("000001","warn_300","查询企业用电量的查询月份格式错误","建议联系CTE公司人员"),
	STATUS_FAILED_ELEC_FAIL02("000001","warn_301","没有查询企业用电量的任何记录","建议联系CTE公司人员"),

    //银行卡鉴权
    STATUS_FAILED_INVALID_CARD("000001","warn_302","您输入的银行卡号无效，请核对后重新输入!","建议联系CTE公司人员"),
    STATUS_FAILED_INVALID_NAME("000001","warn_303","您输入的姓名无效，请核对后重新输入!","建议联系CTE公司人员"),
    STATUS_FAILED_INVALID_PARAM("000001","warn_304","您输入的多个参数无效，请核对后重新输入!","建议联系CTE公司人员"),
    STATUS_FAILED_REPEAT_PARAM("000001","warn_305","同一参数多次请求，请核对后重新输入!","建议联系CTE公司人员");

	
	public String ret_code;
	public String ret_sub_code;
	public String ret_msg;
	public String ret_detail;
	public String getRet_code() { 
		return ret_code;
	}
	public void setRet_code(String ret_code) {
		this.ret_code = ret_code;
	}
	public String getRet_sub_code() {
		return ret_sub_code;
	}
	public void setRet_sub_code(String ret_sub_code) {
		this.ret_sub_code = ret_sub_code;
	}
	public String getRet_msg() {
		return ret_msg;
	}
	public void setRet_msg(String ret_msg) {
		this.ret_msg = ret_msg;
	}
	public String getRet_detail() {
		return ret_detail;
	}
	public void setRet_detail(String ret_detail) {
		this.ret_detail = ret_detail;
	}
	private CRSStatusEnum(String ret_code, String ret_sub_code, String ret_msg,
			String ret_detail) {
		this.ret_code = ret_code;
		this.ret_sub_code = ret_sub_code;
		this.ret_msg = ret_msg;
		this.ret_detail = ret_detail;
	}
	public static CRSStatusEnum match(String code){
		for(CRSStatusEnum em : CRSStatusEnum.values()){
			if(em.getRet_code().equals(code))
				return em;
		}
		return null;
	}
	public static CRSStatusEnum matchSubCode(String ret_sub_code){
		for(CRSStatusEnum em : CRSStatusEnum.values()){
			if(em.getRet_sub_code().equals(ret_sub_code))
				return em;
		}
		return null;
	}
}