package com.cte.credit.api.dto;

import java.io.Serializable;
/**
 * @Title: 请求头信息
 * @Package com.bill99.ifs.crs.common.executor
 * @Description: 接口请求信息
 * @author xiaocl chenglin.xiao@99bill.com
 * @date 2015年8月12日 下午12:16:36
 * @version V1.0
 */
public class RequestHeader implements Serializable{
    private static final long serialVersionUID = 1L;
    /** 请求序列号 */
    private String requst_sn;
    /** 版本号 */
    private String version;
    /** 账号 */
    private String acct_id;
    /** 分配的API KEY */
    private String api_key;
    /** 参数签名 */
    private String sign;
    /** MAC地址 */
    private String mac_address;
    /** IP地址 */
    private String ip_address;
    /** 操作用户 */
    private String operid;
    /** tooken */
    private String access_token;
    /** 网关产品ID */
    private String product_id;
    /** 调用来源 */
    private String source;
    /**
     * 预留字段1
     */
    private String EXT_COL1;
    private String EXT_COL2;
    private String EXT_COL3;
    private String EXT_COL4;
    private String EXT_COL5;
    private String EXT_COL6;
    private String EXT_COL7;
    private String EXT_COL8;
    private String EXT_COL9;
    private String EXT_COL10;

    public String getRequst_sn() {
        return requst_sn;
    }
    public void setRequst_sn(String requst_sn) {
        this.requst_sn = requst_sn;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getAcct_id() {
        return acct_id;
    }
    public void setAcct_id(String acct_id) {
        this.acct_id = acct_id;
    }
    public String getApi_key() {
        return api_key;
    }
    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }
    public String getSign() {
        return sign;
    }
    public void setSign(String sign) {
        this.sign = sign;
    }
    public String getMac_address() {
        return mac_address;
    }
    public void setMac_address(String mac_address) {
        this.mac_address = mac_address;
    }
    public String getIp_address() {
        return ip_address;
    }
    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }
    public String getOperid() {
        return operid;
    }
    public void setOperid(String operid) {
        this.operid = operid;
    }
    public String getAccess_token() {
        return access_token;
    }
    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getEXT_COL1() {
        return EXT_COL1;
    }

    public void setEXT_COL1(String EXT_COL1) {
        this.EXT_COL1 = EXT_COL1;
    }

    public String getEXT_COL2() {
        return EXT_COL2;
    }

    public void setEXT_COL2(String EXT_COL2) {
        this.EXT_COL2 = EXT_COL2;
    }

    public String getEXT_COL3() {
        return EXT_COL3;
    }

    public void setEXT_COL3(String EXT_COL3) {
        this.EXT_COL3 = EXT_COL3;
    }

    public String getEXT_COL4() {
        return EXT_COL4;
    }

    public void setEXT_COL4(String EXT_COL4) {
        this.EXT_COL4 = EXT_COL4;
    }

    public String getEXT_COL5() {
        return EXT_COL5;
    }

    public void setEXT_COL5(String EXT_COL5) {
        this.EXT_COL5 = EXT_COL5;
    }

    public String getEXT_COL6() {
        return EXT_COL6;
    }

    public void setEXT_COL6(String EXT_COL6) {
        this.EXT_COL6 = EXT_COL6;
    }

    public String getEXT_COL7() {
        return EXT_COL7;
    }

    public void setEXT_COL7(String EXT_COL7) {
        this.EXT_COL7 = EXT_COL7;
    }

    public String getEXT_COL8() {
        return EXT_COL8;
    }

    public void setEXT_COL8(String EXT_COL8) {
        this.EXT_COL8 = EXT_COL8;
    }

    public String getEXT_COL9() {
        return EXT_COL9;
    }

    public void setEXT_COL9(String EXT_COL9) {
        this.EXT_COL9 = EXT_COL9;
    }

    public String getEXT_COL10() {
        return EXT_COL10;
    }

    public void setEXT_COL10(String EXT_COL10) {
        this.EXT_COL10 = EXT_COL10;
    }
}
