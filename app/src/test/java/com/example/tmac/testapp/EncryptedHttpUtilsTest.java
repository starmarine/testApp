package com.example.tmac.testapp;

import com.example.tmac.testapp.constants.Constants;
import com.example.tmac.testapp.utils.http.EncryptedHttpUtils;
import com.example.tmac.testapp.utils.http.HandlerWrapper;
import com.example.tmac.testapp.utils.token.FakeTokenStore;

import org.junit.Test;


//@RunWith(RobolectricTestRunner.class)
public class EncryptedHttpUtilsTest {

    @Test
    public void test() throws Exception {
        String url = Constants.host + "/mobile/idtoken/userInfo";
        String body = "{\"data\": \"hahaha\"}";
        System.out.println(body);
        FakeTokenStore tokenStore = new FakeTokenStore();
        tokenStore.setDeviceCode("028c058c-d725-44fc-a0dc-fbb085d38bf3");
        tokenStore.setPrivateKey("MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAk1fHUBbzm42BVu/r10uthJteQvM6GpoL7JVQsW8Kykaif+/ZtcD9rDGCGoPUaT1o7L0blHfSpRxu7aegvfJSXQIDAQABAkBMEbZyD9dnSLAs3SMKfSSxYLFSqVYnI77iPthxMp4EfZMh+KOeDOIjvn2XpMD1Uw3E5HcdRyztRoOYSylrOXj1AiEA9UntD4naJzNV3q5jVmCIfvYQbcA07Hw16f5GUbd2FnsCIQCZxutMzZYLkvLi9EL9Q/T7svDVrKCzz7M07qnFjauPBwIgeAa9pr3Y7xeyrC1/VHQWNrInpdzIyPOa2i5fsHrqoOMCIQCHpNiPwdT11qSbH10r7FLf5uLbiE+k7LQmH9PvZ90J5wIgFj2bfefbj6gTCUAtv3Vh7Y/8QOLuBVtP0jvST/BNvgo=");
        String result = EncryptedHttpUtils.post(url,body,tokenStore,new HandlerWrapper(),0);
        System.out.println(result);
    }

}