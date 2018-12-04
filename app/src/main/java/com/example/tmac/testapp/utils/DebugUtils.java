package com.example.tmac.testapp.utils;

import com.example.tmac.testapp.utils.codec.KeyUtils;

public class DebugUtils {

    public static boolean isDebug = true;
    private static String Base64PublicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALhHae2OxGQEqZ9+o1tBfGyi6HKlDJwcb8TeH5r7cP+IbyVcMyQOXzZUe93F0nq3Rf2FQFxSvv64tSuhyqN4GgUCAwEAAQ==";
    private static String Base64PrivateKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAuEdp7Y7EZASpn36jW0F8bKLocqUMnBxvxN4fmvtw/4hvJVwzJA5fNlR73cXSerdF/YVAXFK+/ri1K6HKo3gaBQIDAQABAkB0/qH5+JInDe7DHBuKbGEad7MsVi1TT5qGWp+mPczX7isH2L5jS3t3pyseJRyjjurZcMxw31Uzs35ecrThEYIBAiEA3ckn4EWmeJZHC/sfuYp5/CEj98h4muyfrqVzKPmdwlECIQDUtQaPCqTxYqhFLYNUffmR3pKRKy81CItnLs7G8zPbdQIhAMFCzDCuAMYLjZVvGtH/TRTbfBAq7I1zrCHvok4OVd5BAiBnDRkTXf3RcJHDiixRWAL8d8+cg0aItA4NvHCzlIowLQIgXhCx6K1cAJB6hyjpFSqyEjEAUqTcBFzGZPw/txsyjl0=";
    private static String Host = "http://mobile.test.id.link:6060";
    private static String DeviceCode = "79d5bb01-0d27-4d93-ad45-76893719b240";

    public static void setProfile(){
        if(isDebug){
            ProfileUtils.setDeviceCode(DeviceCode);
            KeyUtils.Base64KeyPair pair = new KeyUtils.Base64KeyPair(Base64PublicKey,Base64PrivateKey);
            ProfileUtils.setKeyPair(pair);
            ProfileUtils.setHost(Host);
        }
    }
}
