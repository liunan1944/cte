package com.cte.credit.custom.service;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.cte.credit.api.dto.CRSCoreRequest;
import com.cte.credit.api.dto.CRSCoreResponse;
import com.cte.credit.api.enums.CRSStatusEnum;
import com.cte.credit.api.enums.custom.CustomServiceEnum;
import com.cte.credit.api.iface.ICoreService;
import com.cte.credit.common.limit.LimitEngine;
import com.cte.credit.common.util.ExceptionUtil;
import com.cte.credit.common.util.ParamUtil;
import com.cte.credit.custom.service.iface.ICustomCoreService;


@Component
public class CustomCoreService extends BaseCustomCoreService implements
	ICoreService {
	private final Logger logger = LoggerFactory
			.getLogger(CustomCoreService.class);

	public CRSCoreResponse request(String trade_id, CRSCoreRequest request) {
		long startTime = new Date().getTime();
		CRSCoreResponse response = new CRSCoreResponse();
		String prefix = trade_id + " " + "【CRS CUSTOM ADAPTER】";
		boolean doPrint = true;
		this.logger.info("{}收到请求.", prefix);
		if (doPrint)
			this.logger.info("{}交易请求数据: {}", prefix,
					JSONObject.toJSONString(request, true));
		CustomServiceEnum customServiceEnum = CustomServiceEnum.match(request
				.getProdId());
		
		ICustomCoreService customizedService = null;
		try {
			this.logger.info("{}入口参数校验开始...", prefix);
			if ((StringUtils.isEmpty(request.getProdId()))
					|| (StringUtils.isEmpty(request.getAcct_id()))
					|| (StringUtils.isEmpty(request.getApi_key()))
					|| (request.getParams() == null)) {
				this.logger.error("{}入口参数校验不通过.", prefix);
				response.setRetcode(CRSStatusEnum.STATUS_FAILED_SYS_PARAM_INVALID.ret_sub_code);
				response.setRetmsg(CRSStatusEnum.STATUS_FAILED_SYS_PARAM_INVALID.ret_msg);
			} else {
				this.logger.info("{}入口参数校验通过.", prefix);
				this.logger.info("{}产品适配开始...", prefix);
				if (customServiceEnum == null) {
					this.logger.error("{}无适配产品,产品：{}.", prefix,
							request.getProdId());
					response.setRetcode(CRSStatusEnum.STATUS_FAILED_SYS_PROD_NOTEXISTS.ret_sub_code);
					response.setRetmsg(CRSStatusEnum.STATUS_FAILED_SYS_PROD_NOTEXISTS.ret_msg);
				} else {
					this.logger.info("{}产品适配成功", prefix);
					this.logger.info("{}流量限制校验开始...", prefix);
					boolean limitAllow = LimitEngine.limit(
							customServiceEnum.getProd_code(),
							"P_C_B252:100;P_C_B253:100;P_C_B254:100;P_C_B255:100;P_B_B078:100;P_B_B079:100");
					if (limitAllow) {
						this.logger.info("{}流量限制校验通过", prefix);
						this.logger.info("{}引擎计算开始...", prefix);

						boolean valided = super
								.valid(trade_id, request.getParams(),
										customServiceEnum.paramIds,customServiceEnum.nullAbleparamIds);
						if (!valided) {
							this.logger.error("{}参数校验不通过.", prefix);
							response.setRetcode(CRSStatusEnum.STATUS_FAILED_SYS_PARAM_INVALID.ret_sub_code);
							response.setRetmsg(CRSStatusEnum.STATUS_FAILED_SYS_PARAM_INVALID.ret_msg);
						} else {
							customizedService = (ICustomCoreService) findMatchedImpl(customServiceEnum
									.getProd_code());
							if (customizedService != null) {
								this.logger.info("{}定制服务实现适配成功.", prefix);

								request.getParams().putAll(
										ParamUtil.copyHeaders(trade_id,
												request));
								response = customizedService.handler(trade_id,
										request);
//								if(customizedService instanceof BaseCustomCoreService){//update by lijiong.tang
//									logger.info("{} 标签构建开始.", prefix);
//									((BaseCustomCoreService)customizedService).buildTags(response, request.getProdId());
//									logger.info("{} 标签构建结束.", prefix);
//								}
									
								response.setResponse_sn(trade_id);
							} else {
								this.logger.error("{}无合适的定制服务实现.", prefix);
								response.setRetcode(CRSStatusEnum.STATUS_FAILED_SYS_CUSTOM_NOT_MATCHED.ret_sub_code);
								response.setRetmsg(CRSStatusEnum.STATUS_FAILED_SYS_CUSTOM_NOT_MATCHED.ret_msg);
							}
						}

						this.logger.info("{}引擎计算结束", prefix);
					} else {
						this.logger.warn("{}流量限制校验不通过!", prefix);
						response.setRetcode(CRSStatusEnum.STATUS_FAILED_SYS_LIMIT_REACHED.ret_sub_code);
						response.setRetmsg(CRSStatusEnum.STATUS_FAILED_SYS_LIMIT_REACHED.ret_msg);
					}
				}
			}
		} catch (Exception ex) {
			this.logger.error("数据源处理时异常:{}", ExceptionUtil.getTrace(ex));
			response.setRetcode("-99999");
			response.setRetmsg("系统处理时出现异常，请联系管理员!");
		} finally {
			if (customServiceEnum != null) {
				if (!CRSStatusEnum.STATUS_FAILED_SYS_LIMIT_REACHED.ret_sub_code
						.equals(response.getRetcode()))
					LimitEngine.release(customServiceEnum.getProd_code());
			}
		}
		response.setResponse_sn(trade_id);
		response.setVersion(request.getVersion());
		response.setRetdate(new Date());
		this.logger.info("{}交易结束", prefix);
		if (doPrint) {
			this.logger.info(
					"{}最终交易结果: \n" + JSONObject.toJSONString(response, true),
					prefix);
		}
		
		this.logger.info("{}总计耗时: " + (new Date().getTime() - startTime)
				+ " ms", prefix);
		return response;
	}
}