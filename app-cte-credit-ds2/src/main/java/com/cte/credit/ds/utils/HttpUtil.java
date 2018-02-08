package com.cte.credit.ds.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import com.cte.credit.ds.utils.crypto.WandaAES;
import com.cte.credit.ds.utils.crypto.WandaCryptoAESException;

public class HttpUtil {
	/**
	 * 万达网络请求调用
	 * @param paramStr
	 * @param url
	 * @param wandaAES
	 * @param account
	 * @param timeout
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws WandaCryptoAESException
	 */
	public static String HttpSend(String paramStr,String url,
			WandaAES wandaAES,String account,int timeout) 
			 throws ClientProtocolException, IOException, WandaCryptoAESException{
		String result = "";
		String result_tmp = "";
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpParams httpParams = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, timeout);  //设定10秒超时
		HttpConnectionParams.setSoTimeout(httpParams, timeout);
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader(HTTP.CONTENT_TYPE,
				"application/json");
		httpPost.addHeader("X_WANDA_ACCT_ID", account);
		StringEntity se = new StringEntity(paramStr,"utf-8");
		httpPost.setEntity(se);
		HttpResponse response = httpClient.execute(httpPost);
		HttpEntity entity = response.getEntity();
		InputStream content = entity.getContent();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(content));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			result_tmp = sb.toString();
			byte[] encryptAESResponseByte = Base64.decodeBase64(result_tmp);
	        byte[] responseByte = wandaAES.decrypt(encryptAESResponseByte);
	        result = new String(responseByte);
		} catch (Exception e1) {
			e1.printStackTrace();
			result = result_tmp;
		}		
        return result;
   }
}
