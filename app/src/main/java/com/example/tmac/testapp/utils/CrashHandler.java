package com.example.tmac.testapp.utils;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * 作用:
 * 1.收集错误信息
 * 2.保存错误信息
 * Created by shenhua on 2017-08-22-0022.
 * Email shenhuanet@126.com
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler sInstance = null;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;
    // 保存手机信息和异常信息
    private Map<String, String> mMessage = new HashMap<>();

    public static CrashHandler getInstance() {
        if (sInstance == null) {
            synchronized (CrashHandler.class) {
                if (sInstance == null) {
                    synchronized (CrashHandler.class) {
                        sInstance = new CrashHandler();
                    }
                }
            }
        }
        return sInstance;
    }

    private CrashHandler() {
    }

    /**
     * 初始化默认异常捕获
     *
     * @param context context
     */
    public void init(Context context) {
        mContext = context;
        // 获取默认异常处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 将此类设为默认异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        handleException(e);
    }

    private boolean handleException(final Throwable e) {
        if (e == null) {// 异常是否为空
            return false;
        }
        new Thread() {// 在主线程中弹出提示
            @Override
            public void run() {
                Looper.prepare();
                if(isNetException(e)){
                    Toast.makeText(mContext, "访问网络出错，请检查网络连接", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mContext, "发生错误:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Looper.loop();
            }
        }.start();
        e.printStackTrace();
        return false;
    }

    private boolean isNetException(Throwable e){
        Throwable cause = e.getCause();
        while(cause != null){
            if(cause instanceof UnknownHostException){
                return true;
            }else{
                cause = cause.getCause();
            }
        }


        return false;
    }

}
