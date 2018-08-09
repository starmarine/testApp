package com.example.tmac.testapp;


import com.example.tmac.testapp.utils.codec.KeyUtils;

import org.junit.Assert;
import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//@RunWith(RobolectricTestRunner.class)
public class KeyUtilsTest {

    private String data = "pdqa/9BlcLXIewJFCYm1kdpaQYsVL8YirUs2MsLKxgBZH390rZTTo00IUwPBOjj20L5sNtDwfDCLmxfTJ48JiQ==";

    @Test
    public void testGenerateKeyPair() throws Exception {
        KeyUtils.Base64KeyPair keyPair = KeyUtils.generateKeyPair();
        System.out.println(keyPair.privateKey);
        System.out.println("=============================");
        System.out.println(keyPair.publicKey);
    }

    @Test
    public void testEncryptByPublicKey() throws Exception{
//        KeyUtils.Base64KeyPair keyPair = KeyUtils.generateKeyPair();
        String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALJrIg44qC2IsSdyixaiXvIhLME32Zlc+qJp8J0NuQ+t\\njPYgyhmcA71wtPC70A50U/WDiZG1ZqdabZho2JZGhgMCAwEAAQ==\\n";
        String privateKey = "";
        String encrypted = "pdqa/9BlcLXIewJFCYm1kdpaQYsVL8YirUs2MsLKxgBZH390rZTTo00IUwPBOjj20L5sNtDwfDCLmxfTJ48JiQ==";
        System.out.println(encrypted);

        String decrypted = KeyUtils.decryptByPrivateKey(encrypted,privateKey);
        System.out.println(decrypted);

        Assert.assertEquals(data,decrypted);
    }
}