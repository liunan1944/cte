package com.cte.credit.ds.client.aijin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cte.credit.ds.BaseDataSourceRequestor;

public class BaseAijinDataSourceRequestor extends BaseDataSourceRequestor {
	private final Logger logger = LoggerFactory
			.getLogger(BaseAijinDataSourceRequestor.class);
	private static final int timeout = 5000;
	protected String aijin_address;
	protected String account;
	protected String privateKey;

	protected String CODE_EQUAL = "gajx_001";
	protected String CODE_NOEQUAL = "gajx_002";
	protected String CODE_NOEXIST = "gajx_003";

	public String simpleCheck(String cardNo, String name, String sign) {
		String url = null;
		try {
			url = aijin_address + "idNumber="
					+ URLEncoder.encode(cardNo, "UTF-8") + "&name="
					+ URLEncoder.encode(name, "UTF-8") + "&account=" + account
					+ "&pwd=" + privateKey + "&sign=" + sign;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String json = getHtml(url);
		return json;
	}

	static String md5(String text) {
		byte[] bts;
		try {
			bts = text.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bts_hash = md.digest(bts);
			StringBuffer buf = new StringBuffer();
			for (byte b : bts_hash) {
				buf.append(String.format("%02X", b & 0xff));
			}
			return buf.toString();
		} catch (java.io.UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		} catch (java.security.NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}

	static String getHtml(String url) {
		BufferedReader br = null;
		try {
			trustAllHttpsCertificates();
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setReadTimeout(timeout);
			br = new BufferedReader(new InputStreamReader(con.getInputStream(),
					"UTF-8"));
			StringBuffer response = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				response.append(line);
			}
			// br.close();
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * wcs add 20160830
	 * 
	 * @throws Exception
	 */
	static String postHtml(String url, String postData) throws Exception {
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

	public String getAijin_address() {
		return aijin_address;
	}

	public void setAijin_address(String aijin_address) {
		this.aijin_address = aijin_address;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public static void trustAllHttpsCertificates() throws Exception {
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		javax.net.ssl.TrustManager tm = new miTrustManager();
		trustAllCerts[0] = tm;
		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
				.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
				.getSocketFactory());
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
