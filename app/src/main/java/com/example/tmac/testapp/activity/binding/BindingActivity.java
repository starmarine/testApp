package com.example.tmac.testapp.activity.binding;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.tmac.testapp.R;
import com.example.tmac.testapp.activity.AbstractBaseActivity;
import com.example.tmac.testapp.constants.Constants;
import com.example.tmac.testapp.dto.BindingDto;
import com.example.tmac.testapp.dto.dto.MicBindFacilityDto;
import com.example.tmac.testapp.dto.vo.DeviceBindingVO;
import com.example.tmac.testapp.dto.vo.RestResult;
import com.example.tmac.testapp.utils.ProfileUtils;
import com.example.tmac.testapp.utils.codec.KeyUtils;
import com.example.tmac.testapp.utils.http.HttpUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * 绑定分为4步:其中234步骤都需要传给服务器端ticket
 * 1、扫码获取host和ticket
 * 2、生成公私钥对，上传公钥，获取手机号      MicBindFacilityDto
 * 3、发送验证码      DevicePhoneNumberDto
 * 4、提交验证码做绑定   FinishBindDto
 */
public class BindingActivity extends AbstractBaseActivity {
    public static String INTENT_KEY_BINDINGVO= "deviceBindingVO";
    public static BindingDto bindingDto;

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
                Log.i("","begin scan ");
                scanMarginScanner();
            }
        });
    }

    /**
     * 调用扫码的activity
     */
    public void scanMarginScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.setCaptureActivity(SmallCaptureActivity.class);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("BindingActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                String content = result.getContents();
                Log.i("BindingActivity", "Scanned:" + content);
                bindingDto = JSON.parseObject(content,BindingDto.class);
                Toast.makeText(this, "Scanned1: " + bindingDto, Toast.LENGTH_LONG).show();
                //-----------TODO验证dto是否符合格式---------
                new Step2Task(bindingDto).execute();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void updateUI(DeviceBindingVO vo){
        //-----------TODO 验证dto是有效的,有效DTO才能转到SmsActivity-------------
        Intent intent = new Intent(BindingActivity.this, SmsActivity.class);
        intent.putExtra(INTENT_KEY_BINDINGVO, vo);
        startActivity(intent);
    }

    class Step2Task extends AsyncTask<Void, Integer, DeviceBindingVO> {
        private BindingDto bindingDto;

        public Step2Task(BindingDto dto){
            bindingDto = dto;
        }

        @Override
        protected DeviceBindingVO doInBackground(Void... params) {
            Log.i("BindingActivity","click netButton");
            String server = bindingDto.getServer();
            //------------TODO 这里需要修改,不能用在Constants中-----------
            Constants.updateHost(server);
            //--------------生成公私钥对-------------
            KeyUtils.Base64KeyPair keyPair = KeyUtils.generateKeyPair();
            ProfileUtils.setKeyPair(keyPair);
            Log.i("BindingActivity",keyPair.publicKey);
            Log.i("BindingActivity",keyPair.privateKey);
            MicBindFacilityDto bindFacilityDto = new MicBindFacilityDto(bindingDto.getTicket(),keyPair.publicKey);
            String json = JSON.toJSONString(bindFacilityDto);
            Log.i("BindingActivity",json);
            String body = HttpUtils.post(Constants.generateURL(Constants.PATH_BIND_INFO),json);
            Log.i("BindingActivity",body);
            RestResult restResult = JSON.parseObject(body, RestResult.class);
            Log.i("BindingActivity",restResult.toString());
            //-----------------TODO 要考虑restResult的httpStatus------------------------------------------------
            DeviceBindingVO vo = JSON.parseObject(restResult.getData(),DeviceBindingVO.class);
            Log.i("BindingActivity",vo.toString());
            return vo;
        }

        @Override
        protected void onPostExecute(DeviceBindingVO dto) {
            Log.i("BindingActivity","=======================");
            Log.i("BindingActivity",dto.toString());
            updateUI(dto);
        }
    }
}
