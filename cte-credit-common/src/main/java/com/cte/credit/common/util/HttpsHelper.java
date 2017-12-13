package com.cte.credit.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description  
 * @author wuchsh 
 * @version 1.0
 * @createdate 2016年9月29日 下午4:24:42 
 *  
 */
public class HttpsHelper {
	private static final int timeout = Integer.parseInt("10000");
	private static final Logger log = LoggerFactory.getLogger(HttpsHelper.class);

	public  static String doGet(String url) {
		BufferedReader br = null;
		try {
			trustAllHttpsCertificates();
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setReadTimeout(timeout);
			br = new BufferedReader(new InputStreamReader(
					con.getInputStream(), "UTF-8"));
			StringBuffer response = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				response.append(line);
			}
			//br.close();
			return response.toString();
		} catch (Exception e) {
			log.error("HttpsHelper.doGet oper error",e);
			return "";
		}finally{
			try {
				if(br != null){
					br.close();
				}				
			} catch (IOException e) {
				log.error("HttpsHelper.doGet oper error",e);
			}
		}
	}
	/**
	 * post请求,吉信等用
	 * */
	public static String httpSend(String param,String url,int timout) throws Exception{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpParams httpParams = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, timout);  //设定10秒超时
		HttpConnectionParams.setSoTimeout(httpParams, timout);
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader(HTTP.CONTENT_TYPE,
				"application/json");
		StringEntity se = new StringEntity(param,"utf-8");
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
       return string;
   }
	public static void trustAllHttpsCertificates() {  
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];  
        javax.net.ssl.TrustManager tm = new miTrustManager();  
        trustAllCerts[0] = tm;  
        javax.net.ssl.SSLContext sc;
		try {
			sc = javax.net.ssl.SSLContext  
			        .getInstance("SSL");		
			sc.init(null, trustAllCerts, null);
	        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc  
	                .getSocketFactory());  
		} catch (Exception e) {
			log.error("HttpsHelper.doGet oper error",e);
		}  
    }  
	
    /**
     * @throws Exception */
	public static String doPost(String url, String postData) throws Exception {
			trustAllHttpsCertificates();
			URL obj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
			conn.setRequestMethod("POST");
			conn.setReadTimeout(timeout);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			PrintWriter out = new PrintWriter(conn.getOutputStream());
			out.print(postData);
			out.flush();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			StringBuffer response = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				response.append(line);
			}
			br.close();
			return response.toString();

		}
	 
	  static class miTrustManager implements javax.net.ssl.TrustManager,  
      javax.net.ssl.X509TrustManager {  
  public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
      return null;  
  }  

  public boolean isServerTrusted(  
          java.security.cert.X509Certificate[] certs) {  
      return true;  
  }  

  public boolean isClientTrusted(  
          java.security.cert.X509Certificate[] certs) {  
      return true;  
  }  

  public void checkServerTrusted(  
          java.security.cert.X509Certificate[] certs, String authType)  
          throws java.security.cert.CertificateException {  
      return;  
  }  

  public void checkClientTrusted(  
          java.security.cert.X509Certificate[] certs, String authType)  
          throws java.security.cert.CertificateException {  
      return;  
  }  
}  
}
