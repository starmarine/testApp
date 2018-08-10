package com.example.tmac.testapp.utils.codec;

import android.util.Base64;

import com.example.tmac.testapp.exception.ApplicationException;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * Created by tmac on 2018/3/29.
 */

public class KeyUtils {

    public static String ENCRYPTION_TYPE = "RSA";

    public static class Base64KeyPair {

        private static final long serialVersionUID = -1331011012431969897L;
        public final String publicKey;
        public final String privateKey;

        public Base64KeyPair(String publicKey, String privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }
    }

    public static Base64KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ENCRYPTION_TYPE);
            keyGen.initialize(512);
            KeyPair genKeyPair = keyGen.genKeyPair();
            PublicKey publicKey = genKeyPair.getPublic();
            PrivateKey privateKey = genKeyPair.getPrivate();
            String base64PublicKey = Base64.encodeToString(publicKey.getEncoded(), Base64.DEFAULT);
            String base64PrivateKey = Base64.encodeToString(privateKey.getEncoded(), Base64.DEFAULT);
            Base64KeyPair result = new Base64KeyPair(base64PublicKey, base64PrivateKey);

            return result;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(),ex);
        }
    }

    public static byte[] encryptByKey(byte[] data, Key key) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(ENCRYPTION_TYPE);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    public static PrivateKey decodePrivateKey(String base64String) throws GeneralSecurityException {
        byte[] keyBytes = Base64.decode(base64String,Base64.DEFAULT);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ENCRYPTION_TYPE);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    public static byte[] encryptByPrivateKey(byte[] data, String base64PrivateKey) throws GeneralSecurityException {
        PrivateKey key = decodePrivateKey(base64PrivateKey);
        return encryptByKey(data, key);
    }

    public static PublicKey decodePublicKey(String base64String) throws GeneralSecurityException {
        byte[] keyBytes = Base64.decode(base64String,Base64.DEFAULT);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ENCRYPTION_TYPE);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    public static String encryptByPublicKey(byte[] data, String base64PublicKey) {
        try {
            PublicKey key = decodePublicKey(base64PublicKey);
            byte[] encrypt = encryptByKey(data, key);
            return Base64.encodeToString(encrypt, Base64.DEFAULT);
        } catch (GeneralSecurityException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    public static byte[] decryptByKey(byte[] data, Key key) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    public static String decryptByPrivateKey(String data, String base64PrivateKey){
        try{
            PrivateKey key = decodePrivateKey(base64PrivateKey);
            byte[] bytes =  decryptByKey(Base64.decode(data,Base64.DEFAULT), key);
            return new String(bytes);
        }catch(Exception ex){
            throw new ApplicationException(ex.getMessage(),ex);
        }
    }

    public static String signByPrivateKey(byte[] data, String base64PrivateKey) throws GeneralSecurityException {
        PrivateKey key = decodePrivateKey(base64PrivateKey);
        byte[] bytes = encryptByKey(data, key);
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

}
