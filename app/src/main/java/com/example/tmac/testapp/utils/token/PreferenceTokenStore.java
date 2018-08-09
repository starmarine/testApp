package com.example.tmac.testapp.utils.token;

import com.example.tmac.testapp.exception.ApplicationException;
import com.example.tmac.testapp.utils.ProfileUtils;

/**
 * Created by tmac on 2018/8/5.
 */

public class PreferenceTokenStore implements ITokenStore {
    public static String token;

    public void saveToken(String tokenVar){
        token = tokenVar;
    }

    public String findToken(){
        return token;
    }

    public String findDeviceCode(){
        return ProfileUtils.getDeviceCode();
    }

    public void setDeviceCode(String deviceCo){
        throw new ApplicationException("method not supported");
    }

    public String getPrivateKey(){
        return ProfileUtils.getPrivateKey();
    }

    public void setPrivateKey(String prvateK){
        throw new ApplicationException("method not supported");
    }

    public boolean isBinded(){
        return ProfileUtils.getDeviceCode() != null;
    }

    public void bind(String deviceCodeVar){
        throw new ApplicationException("method not supported");
    }

    public void unBind(){
        token = null;
    }
}
