package com.cte.tech.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cte.tech.dto.ProductDto;
import com.cte.tech.util.WandaAES;

/**
 * Created by bjy on 16/8/4.
 */
public class MainCTE001 {

	// TODO 请联系万达员工提供相关的信息
	public static String ACCOUNT_ID = "lnzcp_user";
	public static String HEX_AES_128_PASSWORD = "5ffa26d0e9520a01e0530100007f7ee5";
	public static String INVOKE_URI = "http://111.231.82.46:30000/app-cte-credit-gw/main/service";
    //超时时间设置10s
	private static final int timeout = 10000;
	public static void main(String[] paras) throws Exception {
		String req_sn1 = UUID.randomUUID().toString().replace("-", "");
		System.out.println("请求交易号为:"+req_sn1);
		Map<String, Object> productDetailParam = new HashMap<>();
		//产品入参
		productDetailParam.put("name", "丰志浩");
		productDetailParam.put("cardNo", "330824198211082315");
		productDetailParam.put("cardId", "6222021001124486678");
		ProductDto productDto = new ProductDto();
		productDto.setAcct_id(ACCOUNT_ID);
		//产品编号
		productDto.setProd_id("CTE_001");
		productDto.setReq_time(System.currentTimeMillis());
		productDto.setRequest_sn(req_sn1); //请保证这个id唯一
		productDto.setReq_data(productDetailParam);
		WandaAES wandaAES = new WandaAES(Hex.decodeHex(HEX_AES_128_PASSWORD.toCharArray()));
		String jsonString = JSON.toJSONString(productDto);
		byte[] encryptBytes = wandaAES.encrypt(jsonString);
        //2.2 BASE64
        String base64String = Base64.encodeBase64String(encryptBytes);
        JSONObject jsonResult = new JSONObject();	
		jsonResult.put("request_body", base64String);
		System.out.println("请求信息:"+JSON.toJSONString(jsonResult));
		String response = HttpSend(JSON.toJSONString(jsonResult),INVOKE_URI,wandaAES);
		System.out.println("调用身份核查产品返回结果值:\n"+response);
	}
	 public static String HttpSend(String paramStr,String url,WandaAES wandaAES) 
			 throws ClientProtocolException, IOException {
    	DefaultHttpClient httpClient = new DefaultHttpClient();
    	HttpParams httpParams = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, timeout);  //设定10秒超时
		HttpConnectionParams.setSoTimeout(httpParams, timeout);
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader(HTTP.CONTENT_TYPE,
				"application/json");
		httpPost.addHeader("X_CTE_ACCT_ID", ACCOUNT_ID);
		System.out.println("获取账户信息:"+ACCOUNT_ID);
		StringEntity se = new StringEntity(paramStr,"utf-8");
		httpPost.setEntity(se);
		HttpResponse response = httpClient.execute(httpPost);
		HttpEntity entity = response.getEntity();
		InputStream content = entity.getContent();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(content));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		String string = sb.toString();
//		System.out.println("返回信息:"+string);
		return string;
    }
}
