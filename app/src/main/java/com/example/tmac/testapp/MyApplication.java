package com.example.tmac.testapp;


import android.app.Application;

import com.example.tmac.testapp.exception.ExceptionHandler;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ExceptionHandler.getInstance().init(this);
    }
}
