package com.example.tmac.testapp;


import com.example.tmac.testapp.utils.AesKeyUtils;
import com.example.tmac.testapp.utils.codec.Ciphertext;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

//@RunWith(RobolectricTestRunner.class)
public class AesKeyUtilsTest {

    public static String AES_KEY_BASE64 = "UW5hbUtEU1JwZ281bVpUSw==\\nejF5TEh6a0hKT3puSUJnYQ==";

    @Test
    public void testEncryptAndDecrypt() throws Exception {
        String text = "haha";
        String key = new String(Base64.decodeBase64(AES_KEY_BASE64));
        Ciphertext cipher = AesKeyUtils.encryptAES(text,key);
        String encryptedText = cipher.getBase64();
        System.out.println(encryptedText);

        System.out.println("=====================================================");

        //------------------decrypt----------------
        Ciphertext decryptedCipher = AesKeyUtils.decryptBase64TextByAES(encryptedText,key);
        String decrypted = decryptedCipher.getString();
        System.out.println(decrypted);
    }

}