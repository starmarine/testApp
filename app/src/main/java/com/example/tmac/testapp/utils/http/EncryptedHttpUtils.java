package com.example.tmac.testapp.utils.http;

import com.example.tmac.testapp.constants.Constants;
import com.example.tmac.testapp.exception.ApplicationException;
import com.example.tmac.testapp.utils.codec.AesKeyUtils;
import com.example.tmac.testapp.utils.codec.KeyUtils;
import com.example.tmac.testapp.utils.token.ITokenStore;

import org.apache.commons.codec.binary.Base64;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tmac on 2018/4/2.
 */

public class EncryptedHttpUtils {

    public static String post(String url, String body, ITokenStore tokenStore, HandlerWrapper handler, int retry){
        if(retry > 1){
            throw new ApplicationException("发起http请求重试次数超过1次");
        }

        String token = tokenStore.findToken();
        String deviceCode = tokenStore.findDeviceCode();
        if(token == null || token.length() == 0){
            //--------------------没有token-------------------
            if(tokenStore.isBinded()){
                //----手机已经绑定，发送challenge获取token---------------
                HttpResponse httpResponse = doPost(Constants.URL_EXCHANGE_AES_KEY,"",deviceCode);
                String encryptedToken = httpResponse.getBody();
                System.out.println(encryptedToken);
                token = KeyUtils.decryptByPrivateKey(encryptedToken,tokenStore.getPrivateKey());
                tokenStore.saveToken(token);
                System.out.println(token);
                return post(url,body,tokenStore,handler,retry+1);
            }else{
                //----手机未绑定,页面提示toast先去绑定手机---------------
                handler.sendMessage("手机未绑定,请先绑定手机");
            }
        }else{
            //--------------------已经有token-----------------
//            String encryptedBody = AesKeyUtils.encryptAES(body,token).getBase64();
            byte[] bytes = AesKeyUtils.encryptAESCBC(token,body.getBytes());
            String encryptedBody = Base64.encodeBase64String(bytes);
            System.out.println(encryptedBody);
            HttpResponse httpResponse = doPost(url,encryptedBody,deviceCode);
            if(httpResponse.getStatus() == 200){
                //----------------处理正常的返回-------------------
                String encryptedMessage = httpResponse.getBody();
//                Ciphertext decryptedCipher = AesKeyUtils.decryptBase64TextByAES(encryptedMessage,token);
//                String decrypted = decryptedCipher.getString();
                byte[] encryptedBytes = Base64.decodeBase64(encryptedMessage);
                byte[] decodeBytes = AesKeyUtils.decryptAESCBC(token,encryptedBytes);
                String decrypted = new String(decodeBytes);
                return decrypted;
            } else if(httpResponse.getStatus() == 401){
                //-----------challenge消息,包含了token----------------
                String encryptedToken = httpResponse.getBody();
                token = KeyUtils.decryptByPrivateKey(encryptedToken,tokenStore.getPrivateKey());
                tokenStore.saveToken(token);
                return post(url,body,tokenStore,handler,retry+1);
            }else if(httpResponse.getStatus() == 403){
                //---------------未授权，分为未绑定和用户被禁用两种类型-----------------------------
                if("not_bind".equalsIgnoreCase(httpResponse.getBody())){
                    //-----------未绑定-----------------------------------
                    handler.sendMessage("手机未绑定,请先绑定手机");
                }else if("user_disabled".equalsIgnoreCase(httpResponse.getBody())){
                    //-----------用户被禁用-------------------------------
                    handler.sendMessage("用户已经被禁用,请联系管理员");
                }
            }else {
                throw new ApplicationException("未知错误status:"+httpResponse.getStatus()+" message:"+httpResponse.getBody());
            }

        }

        return null;
    }

    private static HttpResponse doPost(String url,String body,String deviceCode){
        Map<String,String> headers = new HashMap<>();
        headers.put("deviceCode",deviceCode);
        return HttpUtils.doPost(url,body,headers);
    }
}