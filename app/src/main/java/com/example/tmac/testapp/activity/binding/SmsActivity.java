package com.example.tmac.testapp.activity.binding;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.tmac.testapp.R;
import com.example.tmac.testapp.activity.AbstractBaseActivity;
import com.example.tmac.testapp.activity.MainPageActivity;
import com.example.tmac.testapp.constants.Constants;
import com.example.tmac.testapp.dto.dto.DeviceInfo;
import com.example.tmac.testapp.dto.dto.DevicePhoneNumberDto;
import com.example.tmac.testapp.dto.dto.FinishBindDto;
import com.example.tmac.testapp.dto.vo.DeviceBindingVO;
import com.example.tmac.testapp.dto.vo.RestResult;
import com.example.tmac.testapp.utils.ProfileUtils;
import com.example.tmac.testapp.utils.http.HttpUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class SmsActivity extends AbstractBaseActivity {
    private TextView textViewDisplayName;
    private TextView textViewUserName;
    private EditText phoneInput;
    private EditText verifyCodeInput;
    private Button sendSmsBtn;
    private Button bindBtn;
    private DeviceBindingVO deviceBindingVO;
    //--------------TODO 是否应该放在一个统一的地方，一次性清空deviceCode,公私钥，publicKey等??---------
    private String deviceCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("SmsActivity","into SmsActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        textViewDisplayName = findViewById(R.id.textViewDisplayName);
        textViewUserName = findViewById(R.id.textViewUserName);
        phoneInput = findViewById(R.id.phoneInput);
        verifyCodeInput = findViewById(R.id.verifyCodeInput);
        deviceBindingVO = (DeviceBindingVO) getIntent().getSerializableExtra(BindingActivity.INTENT_KEY_BINDINGVO);
        Log.i("SmsActivity",deviceBindingVO.toString());
        textViewDisplayName.setText(deviceBindingVO.getDisplayName());
        textViewUserName.setText(deviceBindingVO.getUserId());
        phoneInput.setText(deviceBindingVO.getPhone());
        sendSmsBtn = findViewById(R.id.sendSmsBtn);
        bindBtn = findViewById(R.id.bindBtn);

        sendSmsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DevicePhoneNumberDto dto = new DevicePhoneNumberDto(BindingActivity.bindingDto.getTicket(),phoneInput.getText().toString());
                new Step3Task(dto).execute();
            }
        });

        bindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String verifyCode = verifyCodeInput.getText().toString();
                if(StringUtils.isNotBlank(verifyCode)){
                    FinishBindDto dto = new FinishBindDto();
                    dto.setVerifyCode(verifyCode);
                    deviceCode = UUID.randomUUID().toString();
                    dto.setDeviceCode(deviceCode);
                    dto.setPublicKey(ProfileUtils.getPublicKey());
                    dto.setTicket(BindingActivity.bindingDto.getTicket());
                    DeviceInfo deviceInfo = new DeviceInfo("android","type",30);
                    dto.setDeviceInfo(deviceInfo);

                    new Step4Task(dto).execute();
                }else{
                    Toast.makeText(SmsActivity.this,"请填写验证码",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class Step3Task extends AsyncTask<Void, Integer, RestResult> {

        private DevicePhoneNumberDto devicePhoneNumberDto;

        public Step3Task(DevicePhoneNumberDto dto){
            this.devicePhoneNumberDto = dto;
        }

        @Override
        protected RestResult doInBackground(Void... params) {
            Log.i("SmsActivity","click netButton");
            String json = JSON.toJSONString(devicePhoneNumberDto);
            Log.i("SmsActivity",json);
            String body = HttpUtils.post(Constants.generateURL(Constants.PATH_SEND_VERIFY_CODE),json);
            Log.i("SmsActivity",body);
            RestResult restResult = JSON.parseObject(body, RestResult.class);
            Log.i("SmsActivity",restResult.toString());
            return restResult;
        }

        @Override
        protected void onPostExecute(RestResult restResult) {
            updateUI(restResult);
        }
    }

    private void updateUI(RestResult result){
        if("OK".equalsIgnoreCase(result.getHttpStatus())){
            Toast.makeText(SmsActivity.this,"发送短信成功",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(SmsActivity.this,"发送短信失败",Toast.LENGTH_SHORT).show();
        }
    }

    class Step4Task extends AsyncTask<Void, Integer, RestResult> {

        private FinishBindDto finishBindDto;

        public Step4Task(FinishBindDto dto){
            this.finishBindDto = dto;
        }

        @Override
        protected RestResult doInBackground(Void... params) {
            Log.i("FinishBindTask","click netButton");
            String json = JSON.toJSONString(finishBindDto);
            Log.i("FinishBindTask",json);
            String body = HttpUtils.post(Constants.generateURL(Constants.PATH_FINISH_BIND),json);
            Log.i("FinishBindTask",body);
            RestResult restResult = JSON.parseObject(body, RestResult.class);
            Log.i("FinishBindTask",restResult.toString());
            return restResult;
        }

        @Override
        protected void onPostExecute(RestResult restResult) {
            doFinishBind(restResult);
        }
    }

    private void doFinishBind(RestResult result){
        if("OK".equalsIgnoreCase(result.getHttpStatus())){
            Toast.makeText(SmsActivity.this,"绑定成功",Toast.LENGTH_SHORT).show();
            //-----------------TODO 需要跳转到新的页面，展示动态码和扫码的页面------------------------

            //--------设置deviceCode标志已经绑定---------------
            ProfileUtils.setDeviceCode(deviceCode);

            Intent intent = new Intent(SmsActivity.this, MainPageActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(SmsActivity.this,"绑定失败",Toast.LENGTH_SHORT).show();
        }
    }

}
