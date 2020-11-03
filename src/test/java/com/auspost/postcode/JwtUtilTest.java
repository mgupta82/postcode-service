package com.auspost.postcode;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class JwtUtilTest {

    @Test
    public void testGenerateJwtWithRsa() throws Exception {
        String token = JwtUtil.generateJwtToken();
        System.out.println("TOKEN:" + token);
        JwtUtil.decodeToken(token);
    }

    @Test
    public void testGenerateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
        keyGenerator.initialize(1024);

        KeyPair kp = keyGenerator.genKeyPair();
        PublicKey publicKey = (PublicKey) kp.getPublic();
        PrivateKey privateKey = (PrivateKey) kp.getPrivate();

        String encodedPublicKey = java.util.Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String encodedPrivateKey = java.util.Base64.getEncoder().encodeToString(privateKey.getEncoded());
        System.out.println("Private Key : " + encodedPrivateKey);
        System.out.println("Public Key : " + encodedPublicKey);
    }

}
