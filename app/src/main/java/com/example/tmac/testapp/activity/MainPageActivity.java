package com.example.tmac.testapp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.tmac.testapp.R;
import com.example.tmac.testapp.constants.Constants;
import com.example.tmac.testapp.dto.dto.TotpKeyDto;
import com.example.tmac.testapp.dto.vo.RestResult;
import com.example.tmac.testapp.dto.vo.TotpKeyVO;
import com.example.tmac.testapp.utils.ProfileUtils;
import com.example.tmac.testapp.utils.TOTPPasswordGenerator;
import com.example.tmac.testapp.utils.http.EncryptedHttpUtils;

import java.util.Date;

public class MainPageActivity extends AbstractBaseActivity {
    private static String TOTP_CODE = "totpCode";
    private static String PROGRESS = "progress";

    private TextView tvSuccess;
    private TextView tvTotpKey;
    private TextView tvTotp;
    private TextView tvProgress;
    private TotpRefreshTask task;

    private Handler handler = new UpdataUIHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        tvSuccess = findViewById(R.id.tvSuccess);
        tvTotpKey = findViewById(R.id.tvTotpKey);
        tvTotp = findViewById(R.id.tvTotp);
        tvProgress = findViewById(R.id.tvProgress);



        tvSuccess.setText("绑定成功:"+ProfileUtils.getDeviceCode() + "公钥:"+ProfileUtils.getPublicKey()+"私钥:"+ProfileUtils.getPrivateKey());
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
                String totpCode = TOTPPasswordGenerator.generateOTP(vo.getNewKey(),vo.getTime(),vo.getStep());
                String progress = TOTPPasswordGenerator.computeProgress(vo.getTime(),vo.getStep());
                //----------更新UI-------------
                Message msg = Message.obtain();
                Bundle data = new Bundle();
                data.putString(TOTP_CODE, totpCode);
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
            tvTotp.setText(totpCode);
            tvTotpKey.setText(ProfileUtils.getTotp().getNewKey());
            tvProgress.setText(progress);
        }
    }

    private void updateUI(RestResult restResult){
        if(restResult.isHttpStatusOK()){
            String data = restResult.getData();
            TotpKeyVO vo = JSON.parseObject(data, TotpKeyVO.class);
            //-------------计算与服务器的便宜偏移---------
            vo.setTime(new Date().getTime() - vo.getTime());
            ProfileUtils.setTotp(vo);
            tvTotpKey.setText(vo.getNewKey());
            Log.i("totp",vo.toString());
            tvTotp.setText(TOTPPasswordGenerator.generateOTP(vo.getNewKey(),vo.getTime(),vo.getStep()));
        }else{
            Toast.makeText(MainPageActivity.this,restResult.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
