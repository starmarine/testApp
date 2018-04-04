package com.example.tmac.testapp.exception;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Context mContext;
    private static ExceptionHandler INSTANCE = new ExceptionHandler ();

    public static ExceptionHandler getInstance ( ) {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init ( Context context ) {
        mContext = context;
        Thread.setDefaultUncaughtExceptionHandler ( this );
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Log.i("error",throwable.getMessage());
        Toast.makeText ( mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG ).show ( );
    }
}
