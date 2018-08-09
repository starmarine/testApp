package com.example.tmac.testapp.constants;

import com.example.tmac.testapp.exception.ApplicationException;
import com.example.tmac.testapp.utils.ProfileUtils;

import org.apache.commons.lang3.StringUtils;

public class Constants {
    public static String TEMP_HOST;

    public static String PATH_EXCHANGE_AES_KEY = "/mobile/idtoken/exchange/aeskey";
    public static String PATH_BIND_INFO= "/mobile/idtoken/bindInfo";
    public static String PATH_SEND_VERIFY_CODE= "/mobile/idtoken/sendVerifyCode";
    public static String PATH_FINISH_BIND= "/mobile/idtoken/finishBind";
    public static String PATH_DELETE_BIND= "/mobile/idtoken/deleteBind";


    public static String URL_EXCHANGE_AES_KEY = TEMP_HOST + PATH_EXCHANGE_AES_KEY;

    public static void updateHost(String hostVar){
        TEMP_HOST = hostVar;
    }

    public static String generateURL(String path){
        String host = ProfileUtils.getHost();
        if(StringUtils.isNotBlank(host)){
            return host + path;
        }else if(StringUtils.isNotBlank(TEMP_HOST)){
            return TEMP_HOST + path;
        }else{
            throw new ApplicationException("no host is found");
        }
    }
}
