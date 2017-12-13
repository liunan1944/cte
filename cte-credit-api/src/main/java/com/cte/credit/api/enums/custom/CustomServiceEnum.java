package com.cte.credit.api.enums.custom;

import com.cte.credit.api.enums.CustomType;
import com.cte.credit.api.enums.MethodType;
/**
* @Title: 定制服务枚举
* @Package com.cte.ifs.crs.common.executor 
* @Description: 定制服务枚举
* @author liunan1944@163.com 
* @date 2016年02月01日 下午15:16:36 
* @version V1.0
*/
public enum CustomServiceEnum {
	/**银行卡鉴权*/
	BankCardAuthGenericServiceImpl(
			"CTE_001",                                                     
			"银行卡鉴权通用产品",	   										    
			0.0,															 
			MethodType.synchronous,	
			CustomType.nomal,
			true,
			new String[] { "name","cardId","cardNo"	},		
			null,													 
			null,
			new String[] {},
			null);

	public String prod_code;			//产品ID
	public Double prod_price;			//产品价格
	public String prod_name;			//产品名称
	public MethodType mtype;			//通讯方式(同步、异步)
	public CustomType ctype;			//产品形态(普通、爬虫)
	public boolean snapshot;			//保存快照数据
	public String[] paramIds;			//必传参数	
	public String[] attachedProds;		//附属产品ID
	public String[] attachedDs;			//附属数据源ID
	public String[] attachedModels;		//附属模型ID
	public String[] nullAbleparamIds;	//可为空参数
	public String getProd_code() {
		return prod_code;
	}
	public void setProd_code(String prod_code) {
		this.prod_code = prod_code;
	}
	public String getProd_name() {
		return prod_name;
	}
	public void setProd_name(String prod_name) {
		this.prod_name = prod_name;
	}
	public Double getProd_price() {
		return prod_price;
	}
	public void setProd_price(Double prod_price) {
		this.prod_price = prod_price;
	}
	public MethodType getMtype() {
		return mtype;
	}
	public void setMtype(MethodType mtype) {
		this.mtype = mtype;
	}
	public CustomType getCtype() {
		return ctype;
	}
	public void setCtype(CustomType ctype) {
		this.ctype = ctype;
	}
	public String[] getParamIds() {
		return paramIds;
	}
	public void setParamIds(String[] paramIds) {
		this.paramIds = paramIds;
	}
	public String[] getAttachedProds() {
		return attachedProds;
	}
	public void setAttachedProds(String[] attachedProds) {
		this.attachedProds = attachedProds;
	}
	
	public String[] getAttachedDs() {
		return attachedDs;
	}
	public void setAttachedDs(String[] attachedDs) {
		this.attachedDs = attachedDs;
	}
	public String[] getAttachedModels() {
		return attachedModels;
	}
	public void setAttachedModels(String[] attachedModels) {
		this.attachedModels = attachedModels;
	}
	private CustomServiceEnum(String prod_code, String prod_name,Double prod_price,MethodType mtype,CustomType ctype,boolean snapshot,
			String[] paramIds,String[] attachedProds,String[] attachedDs,String[] attachedModels,String[] nullAbleparamIds) {
		this.prod_code = prod_code;
		this.prod_name = prod_name;
		this.prod_price = prod_price;
		this.mtype = mtype;
		this.ctype = ctype;
		this.snapshot=snapshot;
		this.paramIds = paramIds;
		this.attachedProds = attachedProds;
		this.attachedDs = attachedDs;
		this.attachedModels = attachedModels;
		this.nullAbleparamIds = nullAbleparamIds;
	}
	public static CustomServiceEnum match(String code){
		for(CustomServiceEnum em : CustomServiceEnum.values()){
			if(em.getProd_code().equals(code))
				return em;
		}
		return null;
	}
	public boolean isSnapshot() {
		return snapshot;
	}
	public void setSnapshot(boolean snapshot) {
		this.snapshot = snapshot;
	}
	public String[] getNullAbleparamIds() {
		return nullAbleparamIds;
	}
	public void setNullAbleparamIds(String[] nullAbleparamIds) {
		this.nullAbleparamIds = nullAbleparamIds;
	}
}