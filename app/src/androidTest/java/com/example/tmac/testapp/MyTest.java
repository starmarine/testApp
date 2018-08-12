package com.example.tmac.testapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.tmac.testapp.constants.Constants;
import com.example.tmac.testapp.utils.http.EncryptedHttpUtils;
import com.example.tmac.testapp.utils.http.HandlerWrapper;
import com.example.tmac.testapp.utils.token.FakeTokenStore;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MyTest {

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
            Constants.inTestCase = true;
            Constants.TEMP_HOST = "http://192.168.1.189:6060";
            String url = Constants.generateURL("/mobile/idtoken/userInfo1");
            String body = "{\"data\": \"hahaha\"}";
            Log.i("tag",body);
            FakeTokenStore tokenStore = new FakeTokenStore();
            tokenStore.setDeviceCode("028c058c-d725-44fc-a0dc-fbb085d38bf3");
            tokenStore.setPrivateKey("MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAk1fHUBbzm42BVu/r10uthJteQvM6GpoL7JVQsW8Kykaif+/ZtcD9rDGCGoPUaT1o7L0blHfSpRxu7aegvfJSXQIDAQABAkBMEbZyD9dnSLAs3SMKfSSxYLFSqVYnI77iPthxMp4EfZMh+KOeDOIjvn2XpMD1Uw3E5HcdRyztRoOYSylrOXj1AiEA9UntD4naJzNV3q5jVmCIfvYQbcA07Hw16f5GUbd2FnsCIQCZxutMzZYLkvLi9EL9Q/T7svDVrKCzz7M07qnFjauPBwIgeAa9pr3Y7xeyrC1/VHQWNrInpdzIyPOa2i5fsHrqoOMCIQCHpNiPwdT11qSbH10r7FLf5uLbiE+k7LQmH9PvZ90J5wIgFj2bfefbj6gTCUAtv3Vh7Y/8QOLuBVtP0jvST/BNvgo=");
            String result = EncryptedHttpUtils.post(url,body,tokenStore,new HandlerWrapper(),0);
            Log.i("tag",result);


//        assertEquals("com.example.tmac.testapp", appContext.getPackageName());
    }

    @Test
    public void test2() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        Constants.inTestCase = true;
        Constants.TEMP_HOST = "http://192.168.1.189:6060";
        String url = Constants.generateURL("/mobile/test/test1");
        String body = "{\"data\": \"hahaha\"}";
        Log.i("tag",body);
        FakeTokenStore tokenStore = new FakeTokenStore();
        tokenStore.setDeviceCode("028c058c-d725-44fc-a0dc-fbb085d38bf3");
        tokenStore.setPrivateKey("MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAk1fHUBbzm42BVu/r10uthJteQvM6GpoL7JVQsW8Kykaif+/ZtcD9rDGCGoPUaT1o7L0blHfSpRxu7aegvfJSXQIDAQABAkBMEbZyD9dnSLAs3SMKfSSxYLFSqVYnI77iPthxMp4EfZMh+KOeDOIjvn2XpMD1Uw3E5HcdRyztRoOYSylrOXj1AiEA9UntD4naJzNV3q5jVmCIfvYQbcA07Hw16f5GUbd2FnsCIQCZxutMzZYLkvLi9EL9Q/T7svDVrKCzz7M07qnFjauPBwIgeAa9pr3Y7xeyrC1/VHQWNrInpdzIyPOa2i5fsHrqoOMCIQCHpNiPwdT11qSbH10r7FLf5uLbiE+k7LQmH9PvZ90J5wIgFj2bfefbj6gTCUAtv3Vh7Y/8QOLuBVtP0jvST/BNvgo=");
        String result = EncryptedHttpUtils.post(url,body,tokenStore,new HandlerWrapper(),0);
        Log.i("tag",result);


//        assertEquals("com.example.tmac.testapp", appContext.getPackageName());
    }
}
