package com.cte.credit.ds.client.jixin;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cte.credit.api.Conts;
import com.cte.credit.api.enums.CRSStatusEnum;
import com.cte.credit.ds.BaseDataSourceRequestor;
import com.cte.credit.ds.dao.domain.jixin.Jixin_bank_result;

public class BaseJiXinSourceRequestor 
extends BaseDataSourceRequestor {
	private final Logger logger = LoggerFactory
			.getLogger(BaseJiXinSourceRequestor.class);
	/**
	 * 包装吉信返回结果
	 * */
	public Map<String, Object> buildJiXinResp(String prefix,Jixin_bank_result jixin){
		logger.info("{} 吉信返回结果开始包装...", prefix);
		Map<String, Object> rets = new HashMap<String, Object>();
		Map<String, Object> retdata = new HashMap<String, Object>();

		String resource_tag = Conts.TAG_SYS_ERROR;
		if("0000".equals(jixin.getReturncode())){
			resource_tag = Conts.TAG_MATCH;
			retdata.put("respCode", "00");
			retdata.put("respDesc", "认证一致");
			rets.put(Conts.KEY_RET_TAG, resource_tag);
			rets.put(Conts.KEY_RET_DATA, retdata);
			rets.put(Conts.KEY_RET_STATUS, CRSStatusEnum.STATUS_SUCCESS);
			rets.put(Conts.KEY_RET_MSG, "采集成功!");
		}else if("0034".equals(jixin.getReturncode())){
			resource_tag = Conts.TAG_UNMATCH;
			retdata.put("respCode", "01");
			retdata.put("respDesc", "认证不一致");
			if("卡状态不正确".equals(jixin.getErrtext())){
				retdata.put("respDetail", "卡状态不正常");
			}else if("输入的密码、有效期或CVN2有误".equals(jixin.getErrtext())){
				retdata.put("respDetail", "输入的密码、有效期或CVN2有误，交易失败");
			}else if("验证信息不一致".equals(jixin.getErrtext())){
				retdata.put("respDetail", "验证信息不一致");
			}		
			rets.put(Conts.KEY_RET_TAG, resource_tag);
			rets.put(Conts.KEY_RET_DATA, retdata);
			rets.put(Conts.KEY_RET_STATUS, CRSStatusEnum.STATUS_SUCCESS);
			rets.put(Conts.KEY_RET_MSG, "采集成功!");
		}else if("0006".equals(jixin.getReturncode())){
			resource_tag = Conts.TAG_UNSUPPORT;
			retdata.put("respCode", "02");
			retdata.put("respDesc", "不支持验证");
			if("银行不支持,请联系客服确认".equals(jixin.getErrtext())){
				retdata.put("respDetail", "您的银行卡暂不支持该业务");
			}else if("银行卡未开通银联无卡支付功能".equals(jixin.getErrtext())){
				retdata.put("respDetail", "请开通无卡支付服务");
			}	
			rets.put(Conts.KEY_RET_TAG, resource_tag);
			rets.put(Conts.KEY_RET_DATA, retdata);
			rets.put(Conts.KEY_RET_STATUS, CRSStatusEnum.STATUS_SUCCESS);
			rets.put(Conts.KEY_RET_MSG, "采集成功!");
		}else if("0001".equals(jixin.getReturncode())){
			resource_tag = Conts.TAG_TST_FAIL;			
			if("验证次数超限".equals(jixin.getErrtext())){
				retdata.put("respCode", "01");
				retdata.put("respDesc", "认证不一致");
				retdata.put("respDetail", "已超过最大查询次数或操作过于频繁");
				rets.put(Conts.KEY_RET_TAG, resource_tag);
				rets.put(Conts.KEY_RET_DATA, retdata);
				rets.put(Conts.KEY_RET_STATUS, CRSStatusEnum.STATUS_SUCCESS);
				rets.put(Conts.KEY_RET_MSG, "采集成功!");
			}else if("卡号无效,请输入正确卡号".equals(jixin.getErrtext())){
				resource_tag = Conts.TAG_TST_FAIL;
				rets.put(Conts.KEY_RET_STATUS, 
						CRSStatusEnum.STATUS_FAILED_INVALID_CARD);
				rets.put(Conts.KEY_RET_MSG, "您输入的银行卡号无效，请核对后重新输入!");
				rets.put(Conts.KEY_RET_TAG, resource_tag);
			}else if("卡bin不支持,请联系客服".equals(jixin.getErrtext())){
				resource_tag = Conts.TAG_TST_FAIL;
				rets.put(Conts.KEY_RET_STATUS, 
						CRSStatusEnum.STATUS_FAILED_DS_ZT_BANKCARD_AUTHEN_EXCEPTION);
				rets.put(Conts.KEY_RET_MSG, "银行卡鉴权失败!");
				rets.put(Conts.KEY_RET_TAG, resource_tag);
			}else if("证件类型有误".equals(jixin.getErrtext())){
				resource_tag = Conts.TAG_TST_FAIL;
				rets.put(Conts.KEY_RET_STATUS, 
						CRSStatusEnum.STATUS_FAILED_DS_JUXINLI_IDCARD_ERROR);
				rets.put(Conts.KEY_RET_MSG, "您输入的为无效身份证号码，请核对后重新输入!");
				rets.put(Conts.KEY_RET_TAG, resource_tag);
			}else{
				resource_tag = Conts.TAG_SYS_ERROR;
				rets.put(Conts.KEY_RET_STATUS, CRSStatusEnum.STATUS_FAILED_SYS_DS_EXCEPTION);
				rets.put(Conts.KEY_RET_MSG, "数据源处理时异常!");
				rets.put(Conts.KEY_RET_TAG, resource_tag);
			}				
		}	
		logger.info("{} 吉信返回结果包装完成", prefix);
		return rets;
	}
}
