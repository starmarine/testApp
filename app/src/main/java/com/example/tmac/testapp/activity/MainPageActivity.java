package com.example.tmac.testapp.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.tmac.testapp.R;
import com.example.tmac.testapp.utils.ProfileUtils;

public class MainPageActivity extends AbstractBaseActivity {
    private TextView tvSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        tvSuccess = findViewById(R.id.tvSuccess);

        tvSuccess.setText("绑定成功:"+ProfileUtils.getDeviceCode() + "公钥:"+ProfileUtils.getPublicKey()+"私钥:"+ProfileUtils.getPrivateKey());
    }

}
