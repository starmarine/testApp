package com.example.tmac.testapp.utils.token;

/**
 * Created by tmac on 2018/8/5.
 */

public class FakeTokenStore implements ITokenStore {
    public static String token;
    public static String deviceCode;
    //-----------base64编码的privateKey
    public static String privateKey;

    public void saveToken(String tokenVar){
        token = tokenVar;
    }

    public String findToken(){
        return token;
    }

    public String findDeviceCode(){
        return deviceCode;
    }

    public void setDeviceCode(String deviceCo){
        deviceCode = deviceCo;
    }

    public String getPrivateKey(){
        return privateKey;
    }

    public void setPrivateKey(String prvateK){
        privateKey = prvateK;
    }

    public boolean isBinded(){
        return deviceCode != null;
    }

    public void bind(String deviceCodeVar){
        deviceCode = deviceCodeVar;
    }

    public void unBind(){
        deviceCode = null;
    }
}
