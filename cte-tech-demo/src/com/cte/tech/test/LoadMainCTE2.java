package com.cte.tech.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.codec.DecoderException;
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
import com.cte.tech.util.CTEAES;
import com.cte.tech.util.CTECryptoAESException;

/**
 * Created by liunan
 */
public class LoadMainCTE2 {
	public static final int MAX_THREAD_NUM=10;
	public static final ExecutorService fixedThreadPool 
	= Executors.newFixedThreadPool(MAX_THREAD_NUM);
	// TODO 请联系CTE员工提供相关的信息
	public final static String ACCOUNT_ID = "lnzcp_user";
	public final static String HEX_AES_128_PASSWORD = "5ffa26d0e9520a01e0530100007f7ee5";
	public final static String INVOKE_URI = "http://111.231.82.46:30000/app-cte-credit-gw/main/service";
    //超时时间设置10s
	private static final int timeout = 10000;
	public static void main(String[] paras) throws Exception {
		for (int i = 0; i <= 4; i++) {
			final int i_run = i;
			fixedThreadPool.execute(new Runnable(){
				@Override
				public void run() {
					for(int j=0;j<3000;j++){
						String req_sn = UUID.randomUUID().toString().replace("-", "");
						System.out.println(i_run+"-"+j+" 请求交易号为:"+req_sn);
						Map<String, Object> productDetailParam = new HashMap<>();
						//产品入参
						productDetailParam.put("name", "祝期");
						productDetailParam.put("cardNo", "13092919850429467X");
						productDetailParam.put("cardId", "6222024301081668996");
						productDetailParam.put("phone", "15861824887");
						ProductDto productDto = new ProductDto();
						productDto.setAcct_id(ACCOUNT_ID);
						//产品编号
						productDto.setProd_id("CTE_001");
						productDto.setReq_time(System.currentTimeMillis());
						productDto.setRequest_sn(req_sn); //请保证这个id唯一
						productDto.setReq_data(productDetailParam);
						CTEAES cteAES;
						try {
							cteAES = new CTEAES(Hex.decodeHex(HEX_AES_128_PASSWORD.toCharArray()));
							String jsonString = JSON.toJSONString(productDto);
							byte[] encryptBytes = cteAES.encrypt(jsonString);
					        //2.2 BASE64
					        String base64String = Base64.encodeBase64String(encryptBytes);
					        JSONObject jsonResult = new JSONObject();	
							jsonResult.put("request_body", base64String);
							System.out.println(i_run+"-"+j+"请求信息:"+JSON.toJSONString(jsonResult));
							String response = HttpSend(JSON.toJSONString(jsonResult),INVOKE_URI);
							System.out.println(i_run+"-"+j+"调用身份核查产品返回结果值:\n"+response);
						} catch (DecoderException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ClientProtocolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (CTECryptoAESException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}
			});
			
		}
		
	}
	 public static String HttpSend(String paramStr,String url) 
			 throws ClientProtocolException, IOException {
    	DefaultHttpClient httpClient = new DefaultHttpClient();
    	HttpParams httpParams = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, timeout);  //设定10秒超时
		HttpConnectionParams.setSoTimeout(httpParams, timeout);
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader(HTTP.CONTENT_TYPE,
				"application/json");
		httpPost.addHeader("X_CTE_ACCT_ID", ACCOUNT_ID);
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
