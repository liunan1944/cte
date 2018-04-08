package com.cte.credit.ds.client.salary;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class JwtToken {
    /**
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getencodedJWT (String iss, String secret) throws UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String token = JWT.create()
                .withIssuer(iss)
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400L))
                .sign(algorithm);
        return token;
    }
}