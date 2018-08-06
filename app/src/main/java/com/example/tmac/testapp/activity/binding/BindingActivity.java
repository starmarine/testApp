package com.example.tmac.testapp.activity.binding;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tmac.testapp.R;
import com.example.tmac.testapp.activity.AbstractBaseActivity;
import com.example.tmac.testapp.utils.ProfileUtils;

public class BindingActivity extends AbstractBaseActivity {
    private TextView textView1;
    private Button scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding);
        textView1 = findViewById(R.id.bindingTextView);
        scanButton = findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileUtils.setDeviceCode("myDeviceCode");
                Log.i("","bind success");
            }
        });
    }

}
