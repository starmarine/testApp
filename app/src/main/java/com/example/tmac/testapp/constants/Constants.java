package com.example.tmac.testapp.constants;

import com.example.tmac.testapp.exception.ApplicationException;
import com.example.tmac.testapp.utils.ProfileUtils;

import org.apache.commons.lang3.StringUtils;

public class Constants {
    public static boolean inTestCase = false;

    public static String TEMP_HOST;

    public static String PATH_EXCHANGE_AES_KEY = "/mobile/idtoken/exchange/aeskey";
    public static String PATH_BIND_INFO= "/mobile/idtoken/bindInfo";
    public static String PATH_SEND_VERIFY_CODE= "/mobile/idtoken/sendVerifyCode";
    public static String PATH_FINISH_BIND= "/mobile/idtoken/finishBind";
    public static String PATH_DELETE_BIND= "/mobile/idtoken/deleteBind";
    public static String PATH_EXCHANGE_TOTP_KEY= "/mobile/idtoken/exchangeTotpKey";

    //--------------------------MFA的二维码扫码---------------------------------
    public static String PATH_QRCODE_SCAN= "/mobile/idtoken/qrcode/scan";
    public static String PATH_QRCODE_CONFIRM= "/mobile/idtoken/qrcode/confirm";

    public static void updateHost(String hostVar){
        TEMP_HOST = hostVar;
    }

    /**
     * 1、优先使用preferences中的host
     * 2、其次使用静态变量TEMP_HOST，这个变量是绑定前扫码获取到的BindingDto中的字段
     *
     * @param path
     * @return
     */
    public static String generateURL(String path){
        if(inTestCase){
            //------------为测试准备的----------
            return TEMP_HOST + path;
        }else{
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
}
