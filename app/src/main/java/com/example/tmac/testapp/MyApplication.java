package com.example.tmac.testapp;


import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
//        ExceptionHandler.getInstance().init(this);
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
