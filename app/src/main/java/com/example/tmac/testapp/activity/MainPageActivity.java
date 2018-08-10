package com.example.tmac.testapp.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.tmac.testapp.R;
import com.example.tmac.testapp.activity.binding.SmallCaptureActivity;
import com.example.tmac.testapp.constants.Constants;
import com.example.tmac.testapp.dto.dto.ScanQrcodeDto;
import com.example.tmac.testapp.dto.dto.TotpKeyDto;
import com.example.tmac.testapp.dto.vo.RestResult;
import com.example.tmac.testapp.dto.vo.ScanQrcodeVO;
import com.example.tmac.testapp.dto.vo.TotpKeyVO;
import com.example.tmac.testapp.utils.ProfileUtils;
import com.example.tmac.testapp.utils.TOTPPasswordGenerator;
import com.example.tmac.testapp.utils.http.EncryptedHttpUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class MainPageActivity extends AbstractBaseActivity {
    public static String INTENT_KEY_QRCODE_VO = "INTENT_KEY_QRCODE_VO";
    public static String INTENT_KEY_SCAN_QRCODE_DTO = "INTENT_KEY_SCAN_QRCODE_DTO";
    private static String TOTP_CODE = "totpCode";
    private static String PROGRESS = "progress";

    private TextView tvSuccess;
    private TextView tvTotpKey;
    private TextView tvTotp;
    private TextView tvProgress;
    private TotpRefreshTask task;

    private Button btnScan;

    private Handler handler = new UpdataUIHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        tvSuccess = findViewById(R.id.tvSuccess);
        tvTotpKey = findViewById(R.id.tvTotpKey);
        tvTotp = findViewById(R.id.tvTotp);
        tvProgress = findViewById(R.id.tvProgress);
        //---------在这里update一下的作用是为了避免后台线程只能在时间整除60的情况下计算code，不在60的情况下就不会显示code
        updateTotp();
        tvSuccess.setText("绑定成功:"+ProfileUtils.getDeviceCode() + "公钥:"+ProfileUtils.getPublicKey()+"私钥:"+ProfileUtils.getPrivateKey());

        btnScan = findViewById(R.id.btnScan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("","begin scan ");
                scanMarginScanner();
            }
        });
    }

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
                String authId = result.getContents();
                Log.i("Step12Activity", "Scanned:" + authId);
                ScanQrcodeDto bindingDto = new ScanQrcodeDto(authId,ProfileUtils.getDeviceCode());
                Toast.makeText(this, "Scanned1: " + bindingDto, Toast.LENGTH_LONG).show();
                //-----------TODO验证dto是否符合格式---------
                new ScanQrcodeTask(bindingDto).execute();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    class ScanQrcodeTask extends AsyncTask<Void, Integer, RestResult> {
        private ScanQrcodeDto scanQrcodeDto;

        public ScanQrcodeTask(ScanQrcodeDto dto){
            scanQrcodeDto = dto;
        }

        @Override
        protected RestResult doInBackground(Void... params) {
            Log.i("scan ","access server");

            String json = JSON.toJSONString(scanQrcodeDto);
            Log.i("ScanQrcodeTask",json);
            String body = EncryptedHttpUtils.post(Constants.generateURL(Constants.PATH_QRCODE_SCAN),json);
            Log.i("ScanQrcodeTask",body);
            RestResult restResult = JSON.parseObject(body, RestResult.class);
            Log.i("ScanQrcodeTask",restResult.toString());
            //-----------------TODO 要考虑restResult的httpStatus------------------------------------------------

            return restResult;
        }

        @Override
        protected void onPostExecute(RestResult restResult) {
            Log.i("ScanQrcodeTask","=======================");
            Log.i("ScanQrcodeTask",restResult.toString());

            if(restResult.isHttpStatusOK()){
                ScanQrcodeVO vo = JSON.parseObject(restResult.getData(),ScanQrcodeVO.class);
                Log.i("ScanQrcodeTask",vo.toString());
                jumpToConfirmPage(vo,scanQrcodeDto);
            }else{
                Toast.makeText(MainPageActivity.this,restResult.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void jumpToConfirmPage(ScanQrcodeVO vo,ScanQrcodeDto scanQrcodeDto){
        Intent intent = new Intent(MainPageActivity.this, QRCodeActivity.class);
        intent.putExtra(INTENT_KEY_QRCODE_VO, vo);
        intent.putExtra(INTENT_KEY_SCAN_QRCODE_DTO, scanQrcodeDto);
        startActivity(intent);
    }

    private void updateTotp(){
        TotpKeyVO vo = ProfileUtils.getTotp();
        if(vo!=null && vo.getNewKey() != null){
            String totpCode = TOTPPasswordGenerator.generateOTP(vo.getNewKey(),vo.getTime(),vo.getStep());
            tvTotp.setText(totpCode);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        task = new TotpRefreshTask(true);
        task.start();
    }

    @Override
    protected void onStop(){
        super.onStop();
        task.stopRunning();
        task = null;
    }

    class TotpRefreshTask extends Thread {
        private boolean isRunning = false;

        public TotpRefreshTask(boolean isRunning){
            this.isRunning = isRunning;
        }

        public void stopRunning(){
            this.isRunning = false;
        }

        @Override
        public void run() {
            while(isRunning){
                try{
                    syncTotpKey();
                    computeTotpAndRefreshUI();
                    sleep(1000);
                }catch(Exception ex){
                    Log.e("totp_exception",ex.getMessage(),ex);
                }
            }
        }

        private void syncTotpKey(){
            TotpKeyVO totpVO = ProfileUtils.getTotp();
            if(totpVO==null || totpVO.getNewKey() == null){
                String oldKey = null;
                TotpKeyDto dto = new TotpKeyDto(oldKey,ProfileUtils.getDeviceCode());
                String json = JSON.toJSONString(dto);
                Log.i("exchange totp",json);
                String body = EncryptedHttpUtils.post(Constants.generateURL(Constants.PATH_EXCHANGE_TOTP_KEY),json);
                Log.i("exchange totp",body);
                RestResult restResult = JSON.parseObject(body, RestResult.class);
                Log.i("exchange totp",restResult.toString());
                if(restResult.isHttpStatusOK()){
                    String data = restResult.getData();
                    TotpKeyVO vo = JSON.parseObject(data, TotpKeyVO.class);
                    //-------------计算与服务器的时间偏移---------
                    vo.setTime(new Date().getTime() - vo.getTime());
                    ProfileUtils.setTotp(vo);
                    Log.i("totp",vo.toString());
                }else{
                    Toast.makeText(MainPageActivity.this,restResult.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }else{
                Log.i("totp","have totpKey,no need to sync");
            }
        }

        /**
         * 计算TOTPCode并更新UI
         */
        private void computeTotpAndRefreshUI(){

            TotpKeyVO vo = ProfileUtils.getTotp();
            if(vo != null || vo.getNewKey() != null){

                String progress = TOTPPasswordGenerator.computeProgress(vo.getTime(),vo.getStep());
                //----------更新UI-------------
                Message msg = Message.obtain();
                Bundle data = new Bundle();
                if("0".equalsIgnoreCase(progress)){
                    Log.i("update totp","compute totp");
                    String totpCode = TOTPPasswordGenerator.generateOTP(vo.getNewKey(),vo.getTime(),vo.getStep());
                    data.putString(TOTP_CODE, totpCode);
                }
                data.putString(PROGRESS, progress);
                msg.setData(data);
                handler.sendMessage(msg);
            }
        }

    }

    class UpdataUIHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            String totpCode = msg.getData().getString(TOTP_CODE);
            String progress = msg.getData().getString(PROGRESS);
            if(StringUtils.isNotBlank(totpCode)){
                tvTotp.setText(totpCode);
            }
            tvTotpKey.setText(ProfileUtils.getTotp().getNewKey());
            tvProgress.setText(progress);
        }
    }

}
