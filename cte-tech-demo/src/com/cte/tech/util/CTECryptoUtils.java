package com.cte.tech.util;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Security;

/**
 * Created by liunan
 */
public final class CTECryptoUtils {
    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }
    public static final String createAESPasswordAsHex() throws CTECryptoAESException {
        return createAESPasswordAsHex(AESConstants.AES_SIZE_128);
    }
    public static final String createAESPasswordAsHex(Integer length) throws CTECryptoAESException {
        byte[] result = createAESPassword(length);
        return Hex.encodeHexString(result);
    }

    public static final byte[] createAESPassword() throws CTECryptoAESException {
        return createAESPassword(AESConstants.AES_SIZE_128);
    }

    public static final byte[] createAESPassword(Integer length) 
    		throws CTECryptoAESException {
        try {
            KeyGenerator kg = KeyGenerator.getInstance(AESConstants.ALGORITHM_AES);
            kg.init(length);
            SecretKey sk = kg.generateKey();
            return sk.getEncoded();
        } catch (Exception e) {
            throw new CTECryptoAESException("Failed to create the aes("
        + AESConstants.AES_SIZE_128 + ") password", e);
        }
    }
}
