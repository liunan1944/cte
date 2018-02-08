package com.cte.credit.ds.client.wandaEdu;

import java.io.IOException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.cte.credit.ds.BaseDataSourceRequestor;
import com.cte.credit.ds.utils.HttpUtil;
import com.cte.credit.ds.utils.crypto.WandaAES;
import com.cte.credit.ds.utils.crypto.WandaCryptoAESException;
import com.cte.credit.ds.utils.dto.ProductDto;

public class BaseWandaSourceRequestor extends BaseDataSourceRequestor {
	private final Logger logger = LoggerFactory
			.getLogger(BaseWandaSourceRequestor.class);
	/**
	 * 网络请求万达接口
	 * @param trade_id
	 * @param productDto
	 * @param url
	 * @param key
	 * @param timeout
	 * @return
	 */
	protected String WandaSend(String trade_id,ProductDto productDto,
			String url,String key,int timeout) 
					throws DecoderException, WandaCryptoAESException, 
					ClientProtocolException, IOException{		
		WandaAES wandaAES = new WandaAES(Hex.decodeHex(key.toCharArray()));
		String jsonString = JSON.toJSONString(productDto);
		byte[] encryptBytes = wandaAES.encrypt(jsonString);
        //2.2 BASE64
        String base64String = Base64.encodeBase64String(encryptBytes);
        logger.info("{} 网络请求万达接口开始...",trade_id);
		String response = HttpUtil.HttpSend(base64String,url,
				wandaAES,productDto.getAcct_id(),timeout);
		logger.info("{} 网络请求万达接口完成",trade_id);
		return response;
	}
}
