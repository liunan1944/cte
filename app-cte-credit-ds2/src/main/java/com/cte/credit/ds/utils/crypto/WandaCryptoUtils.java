package com.cte.credit.ds.utils.crypto;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Security;

/**
 * Created by caoyanfei079 on 4/23/15.
 */
public final class WandaCryptoUtils {
    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }
    public static final String createAESPasswordAsHex() throws WandaCryptoAESException {
        return createAESPasswordAsHex(AESConstants.AES_SIZE_128);
    }
    public static final String createAESPasswordAsHex(Integer length) throws WandaCryptoAESException {
        byte[] result = createAESPassword(length);
        return Hex.encodeHexString(result);
    }

    public static final byte[] createAESPassword() throws WandaCryptoAESException {
        return createAESPassword(AESConstants.AES_SIZE_128);
    }

    public static final byte[] createAESPassword(Integer length) throws WandaCryptoAESException {
        try {
            KeyGenerator kg = KeyGenerator.getInstance(AESConstants.ALGORITHM_AES);
            kg.init(length);//要生成多少位，只需要修改这里即可128, 192或256
            SecretKey sk = kg.generateKey();
            return sk.getEncoded();
        } catch (Exception e) {
            throw new WandaCryptoAESException("Failed to create the aes(" + AESConstants.AES_SIZE_128 + ") password", e);
        }
    }
}
