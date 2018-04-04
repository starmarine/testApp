package com.example.tmac.testapp.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.tmac.testapp.exception.ApplicationException;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by tmac on 2018/4/2.
 */

public class HttpUtils {
    public static final MediaType JSON  = MediaType.parse("application/json; charset=utf-8");
    public static OkHttpClient client = new OkHttpClient();

    public static String get(String url, Handler handler){
        try{
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                if(response.code() == 200){
                    if(handler != null){
                        Message msg = Message.obtain();
                        Bundle b=new Bundle();//信息封包
                        b.putString("type","noLogin");
                        msg.setData(b);
                        handler.sendMessage(msg);
                    }
                }
            }

            return response.body().string();
        } catch (IOException e) {
            throw new ApplicationException(e.getMessage(),e);
        }
    }

    public String post(String url, String json){
        try{
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }catch(IOException ex){
            throw new ApplicationException(ex.getMessage(),ex);
        }
    }
}
