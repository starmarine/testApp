package com.example.tmac.testapp;


import android.util.Log;

import com.example.tmac.testapp.utils.KeyUtils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//@RunWith(RobolectricTestRunner.class)
public class KeyUtilsTest {

    private String data = "hahaha";

    @Test
    public void testGenerateKeyPair() throws Exception {
        KeyUtils.Base64KeyPair keyPair = KeyUtils.generateKeyPair();
        System.out.println(keyPair.privateKey);
        System.out.println("=============================");
        System.out.println(keyPair.publicKey);
    }

    @Test
    public void testEncryptByPublicKey() throws Exception{
        KeyUtils.Base64KeyPair keyPair = KeyUtils.generateKeyPair();
        String encrypted = KeyUtils.encryptByPublicKey(data.getBytes("UTF-8"),keyPair.publicKey);
        System.out.println(encrypted);

        String decrypted = new String(KeyUtils.decryptByPrivateKey(encrypted,keyPair.privateKey),"UTF-8");
        System.out.println(decrypted);

        Assert.assertEquals(data,decrypted);
    }
}