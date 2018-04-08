package com.cte.credit.ds.client.salary;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class AesEncrypt {

    /**
     * @param strKey
     * @param strIn
     * @return
     * @throws Exception
     */

    public static String encrypt(String strKey, String strIn, byte[] vi) {
        Cipher cipher = null;

        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128);
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(
                    Cipher.ENCRYPT_MODE,
                    new SecretKeySpec(strKey.getBytes(), "AES"),
                    new IvParameterSpec(vi)
            );

            byte[] encrypted = cipher.doFinal(strIn.getBytes());

            return org.apache.commons.codec.binary.Base64.encodeBase64String(encrypted);

//                return java.util.Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param strKey
     * @param strIn
     * @return
     */
    public static String decrypt(String strKey, String strIn, byte[] vi) {
        try {
//                strIn = URLDecoder.decode(strIn,"utf-8");
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(strIn);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keyspec = new SecretKeySpec(strKey.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(vi);
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}