package com.example.tmac.testapp.utils.http;

import android.os.Handler;

/**
 * Created by tmac on 2018/8/5.
 */

public class HandlerWrapper {
    private Handler handler;

    public HandlerWrapper(){
    }

    public HandlerWrapper(Handler handler){
        this.handler = handler;
    }

    public void sendMessage(String message){
        if(handler == null){
            System.out.println(message);
        }else{

        }

    }
}
