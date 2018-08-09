package com.example.tmac.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.tmac.testapp.activity.MainPageActivity;
import com.example.tmac.testapp.activity.binding.Step12Activity;
import com.example.tmac.testapp.utils.ProfileUtils;

import org.apache.commons.lang3.StringUtils;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        String deviceCode = ProfileUtils.getDeviceCode();
        if(StringUtils.isNotBlank(deviceCode)){
            //---------已经绑定,显示动态码页面-----------
            Log.i("test",deviceCode);
            jump(MainPageActivity.class);
        }else {
            //---------未绑定,显示绑定页面-----------
            Log.i("test", "no deviceCode");
            jump(Step12Activity.class);
        }
    }

    private void jump(final Class activityClass){
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, activityClass);
                startActivity(intent);
                finish();
            }
        }, 500);
    }

}
