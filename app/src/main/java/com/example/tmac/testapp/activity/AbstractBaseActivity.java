package com.example.tmac.testapp.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class AbstractBaseActivity extends AppCompatActivity {
    public Handler handler = new Handler(){
         public void handleMessage(android.os.Message msg) {
             Log.i("当前主线程是----->", Thread.currentThread()+"");
             Toast toast = Toast.makeText(AbstractBaseActivity.this, "这是一个普通的Toast!", Toast.LENGTH_SHORT);
             toast.show();
             Intent i = new Intent(AbstractBaseActivity.this,LoginActivity.class);
             startActivity(i);
         };
    };


}
