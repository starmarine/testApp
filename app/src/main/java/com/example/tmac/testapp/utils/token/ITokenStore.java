package com.example.tmac.testapp.utils.token;

/**
 * Created by tmac on 2018/8/5.
 */

public interface ITokenStore {
    void saveToken(String token);
    String findToken();
    String findDeviceCode();
    String getPrivateKey();
    boolean isBinded();
    void bind(String deviceCode);
    void unBind();
}
