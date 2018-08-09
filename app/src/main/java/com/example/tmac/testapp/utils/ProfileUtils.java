package com.example.tmac.testapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.example.tmac.testapp.MyApplication;
import com.example.tmac.testapp.dto.vo.TotpKeyVO;
import com.example.tmac.testapp.utils.codec.KeyUtils;

public class ProfileUtils {
    public static String USER_PREFERENCES = "USER_PREFERENCES";
    public static String KEY_DEVICE_CODE = "DEVICE_CODE";
    public static String KEY_USER_NAME = "USER_NAME";
    public static String KEY_DISPLAY_NAME = "DISPLAY_NAME";
    public static String KEY_PUBLIC_KEY = "PUBLIC_KEY";
    public static String KEY_PRIVATE_KEY = "PRIVATE_KEY";
    //---------------绑定成功之后要把服务器的地址放到这里
    public static String KEY_HOST = "HOST";
    public static String KEY_TOTP_KEY = "KEY_TOTP_KEY";



    public static String getDeviceCode(){
        return getUserPreferences().getString(KEY_DEVICE_CODE,null);
    }

    public static void setDeviceCode(String deviceCode){
        SharedPreferences sp = getUserPreferences();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_DEVICE_CODE, deviceCode);
        editor.commit();
    }

    public static String getUserName(){
        return getUserPreferences().getString(KEY_USER_NAME,null);
    }

    public static String getPublicKey(){
        return getUserPreferences().getString(KEY_PUBLIC_KEY,null);
    }

    public static String getPrivateKey(){
        return getUserPreferences().getString(KEY_PRIVATE_KEY,null);
    }

    public static void setKeyPair(KeyUtils.Base64KeyPair keyPair){
        SharedPreferences sp = getUserPreferences();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_PUBLIC_KEY, keyPair.publicKey);
        editor.putString(KEY_PRIVATE_KEY, keyPair.privateKey);
        editor.commit();

    }

    public static String getHost(){
        return getUserPreferences().getString(KEY_HOST,null);
    }

    public static void setHost(String host){
        SharedPreferences sp = getUserPreferences();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_HOST, host);
        editor.commit();
    }

    public static void setDisplayName(String displayName){
        SharedPreferences sp = getUserPreferences();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_DISPLAY_NAME, displayName);
        editor.commit();
    }

    public static void setTotp(TotpKeyVO totpKeyVO){
        SharedPreferences sp = getUserPreferences();
        SharedPreferences.Editor editor = sp.edit();
        String json = JSON.toJSONString(totpKeyVO);
        editor.putString(KEY_TOTP_KEY, json);
        editor.commit();
    }

    public static TotpKeyVO getTotp(){
        String totpKeyVOString = getUserPreferences().getString(KEY_TOTP_KEY,null);
        if(totpKeyVOString == null){
            return null;
        }else{
            TotpKeyVO vo = JSON.parseObject(totpKeyVOString, TotpKeyVO.class);
            return vo;
        }
    }

    public static String getDisplayName(){
        return getUserPreferences().getString(KEY_DISPLAY_NAME,null);
    }

    private static SharedPreferences getUserPreferences(){
        SharedPreferences preferences = MyApplication.getContext().getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        return preferences;
    }
}
