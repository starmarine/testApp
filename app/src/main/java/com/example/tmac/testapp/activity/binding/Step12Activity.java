package com.example.tmac.testapp.activity.binding;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
public class Step12Activity extends AbstractBaseActivity {
    public static String INTENT_KEY_BINDINGVO= "deviceBindingVO";
    public static BindingDto bindingDto;

    private TextView textView1;
    private Button scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//remove notification bar

        setContentView(R.layout.activity_step12);
//        ActionBar actionBar = getActionBar();
//        actionBar.hide();
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
                Log.d("Step12Activity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                String content = result.getContents();
                Log.i("Step12Activity", "Scanned:" + content);
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
        Intent intent = new Intent(Step12Activity.this, Step34Activity.class);
        intent.putExtra(INTENT_KEY_BINDINGVO, vo);
        startActivity(intent);
    }

    class Step2Task extends AsyncTask<Void, Integer, RestResult> {
        private BindingDto bindingDto;

        public Step2Task(BindingDto dto){
            bindingDto = dto;
        }

        @Override
        protected RestResult doInBackground(Void... params) {
            Log.i("Step12Activity","click netButton");
            String server = bindingDto.getServer();
            //------------TODO 这里需要修改,不能用在Constants中-----------
            Constants.updateHost(server);
            //--------------生成公私钥对-------------
            KeyUtils.Base64KeyPair keyPair = KeyUtils.generateKeyPair();
            ProfileUtils.setKeyPair(keyPair);
            Log.i("Step12Activity",keyPair.publicKey);
            Log.i("Step12Activity",keyPair.privateKey);
            MicBindFacilityDto bindFacilityDto = new MicBindFacilityDto(bindingDto.getTicket(),keyPair.publicKey);
            String json = JSON.toJSONString(bindFacilityDto);
            Log.i("Step12Activity",json);
            String body = HttpUtils.post(Constants.generateURL(Constants.PATH_BIND_INFO),json);
            Log.i("Step12Activity",body);
            RestResult restResult = JSON.parseObject(body, RestResult.class);
            Log.i("Step12Activity",restResult.toString());
            //-----------------TODO 要考虑restResult的httpStatus------------------------------------------------

            return restResult;
        }

        @Override
        protected void onPostExecute(RestResult restResult) {
            Log.i("Step12Activity","=======================");
            Log.i("Step12Activity",restResult.toString());

            if(restResult.isHttpStatusOK()){
                DeviceBindingVO vo = JSON.parseObject(restResult.getData(),DeviceBindingVO.class);
                Log.i("Step12Activity",vo.toString());
                updateUI(vo);
            }else{
                Toast.makeText(Step12Activity.this,restResult.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }
}
