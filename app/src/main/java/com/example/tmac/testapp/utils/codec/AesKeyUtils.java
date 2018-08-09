package com.example.tmac.testapp.utils.codec;

import android.util.Log;

import com.example.tmac.testapp.exception.ApplicationException;
import com.example.tmac.testapp.exception.DecryptionException;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
//import android.util.Base64;

/**
 * Created by tmac on 2018/3/29.
 */

public class AesKeyUtils {

    private static String CIPHER_ALGORITHM = "AES"; // optional value AES/DES/DESede

    private static Key getKey(String strKey) {
        try {
            if (strKey == null) {
                strKey = "";
            }
            KeyGenerator _generator = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(strKey.getBytes("utf-8"));
            _generator.init(128, secureRandom);
            return _generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(" 初始化密钥出现异常 ");
        }
    }

    /**
     * AES加密
     *
     * @param content
     * @param password
     * @return 二进制密文
     */
    public static Ciphertext encryptAES(String content, String password) {
        try {
            SecureRandom sr = new SecureRandom();
            Key secureKey = getKey(password);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secureKey, sr);
            return new Ciphertext(cipher.doFinal(content.getBytes("utf-8")));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException | UnsupportedEncodingException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
    }

    /**
     * AES解密
     *
     * @param content
     *            二进制密文
     * @param password
     * @return 二进制原文
     */
    public static Ciphertext decryptAES(byte[] content, String password) {
        try {
            SecureRandom sr = new SecureRandom();
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            Key secureKey = getKey(password);
            cipher.init(Cipher.DECRYPT_MODE, secureKey, sr);
            return new Ciphertext(cipher.doFinal(content));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {
            throw new DecryptionException(e.getMessage(), e);
        }
    }

    public static Ciphertext decryptBase64TextByAES(String base64Content, String password) {
        byte[] content = android.util.Base64.decode(base64Content,android.util.Base64.DEFAULT);
        return decryptAES(content,password);
    }

    /**
     * @param base64Secret 一个Base64的key和矩阵
     * @param data         被加密的数据
     * @return
     */
    public static byte[] encryptAESCBC(String base64Secret, byte[] data) {
        try{
            Log.i("base64Secret",base64Secret);
            byte[] key = android.util.Base64.decode(base64Secret.split("\n")[0],android.util.Base64.NO_PADDING);
            byte[] initVector = android.util.Base64.decode(base64Secret.split("\n")[1],android.util.Base64.NO_PADDING);

            System.out.println(key.length);

            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(data);
            return encrypted;
        }catch(Exception ex){
            throw new ApplicationException(ex.getMessage(),ex);
        }
    }

    /**
     * @param base64Secret 一个Base64的key和矩阵
     * @param data         要解密的数据
     * @return
     */
    public static byte[] decryptAESCBC(String base64Secret, byte[] data){
        try{
            byte[] key = android.util.Base64.decode(base64Secret.split("\n")[0],android.util.Base64.NO_PADDING);
            byte[] initVector = android.util.Base64.decode(base64Secret.split("\n")[1],android.util.Base64.NO_PADDING);

            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(data);
            return original;
        }catch(NoSuchPaddingException |NoSuchAlgorithmException | IllegalBlockSizeException | InvalidKeyException |InvalidAlgorithmParameterException | BadPaddingException ex){
            throw new DecryptionException(ex.getMessage(), ex);
        }
    }

}
