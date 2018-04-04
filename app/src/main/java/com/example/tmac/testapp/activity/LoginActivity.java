package com.example.tmac.testapp.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.tmac.testapp.R;

public class LoginActivity extends AbstractBaseActivity {
    private TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textView1 = findViewById(R.id.textViewLogin);
        textView1.setText(R.string.loginText);
    }


}
