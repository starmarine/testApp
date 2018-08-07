package com.example.tmac.testapp.activity.binding;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.tmac.testapp.R;
import com.example.tmac.testapp.activity.AbstractBaseActivity;
import com.example.tmac.testapp.dto.vo.DeviceBindingVO;

public class SmsActivity extends AbstractBaseActivity {
    private TextView textViewDisplayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("SmsActivity","into SmsActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        textViewDisplayName = findViewById(R.id.textViewDisplayName);
        DeviceBindingVO vo = (DeviceBindingVO) getIntent().getSerializableExtra(BindingActivity.INTENT_KEY_BINDINGVO);
        Log.i("SmsActivity",vo.toString());
        textViewDisplayName.setText(vo.getDisplayName());


    }


}
