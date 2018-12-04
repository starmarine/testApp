package com.example.tmac.testapp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.tmac.testapp.R;
import com.example.tmac.testapp.activity.binding.SmallCaptureActivity;
import com.example.tmac.testapp.constants.Constants;
import com.example.tmac.testapp.dto.dto.TotpKeyDto;
import com.example.tmac.testapp.dto.vo.RestResult;
import com.example.tmac.testapp.dto.vo.TotpKeyVO;
import com.example.tmac.testapp.utils.DebugUtils;
import com.example.tmac.testapp.utils.ProfileUtils;
import com.example.tmac.testapp.utils.TOTPPasswordGenerator;
import com.example.tmac.testapp.utils.http.EncryptedHttpUtils;
import com.google.zxing.integration.android.IntentIntegrator;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;


public class TabMfaFragment extends Fragment {
    private static String TOTP_CODE = "totpCode";
    private static String PROGRESS = "progress";

    private TextView tvSuccess;
    private TextView tvTotpKey;
    private TextView tvTotp;
    private TextView tvProgress;
    private TextView totpCode0;
    private TextView totpCode1;
    private TextView totpCode2;
    private TextView totpCode3;
    private TextView totpCode4;
    private TextView totpCode5;

    LinearLayout mfaDebugLayout;

    private TabMfaFragment.TotpRefreshTask task;

    private Button btnScan;

    private Handler handler = new TabMfaFragment.UpdataUIHandler();

    public static TabMfaFragment newInstance(){
        Bundle arguments = new Bundle();
        TabMfaFragment tabContentFragment = new TabMfaFragment();
        tabContentFragment.setArguments(arguments);
        return tabContentFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_tab_mfa, null);

        tvSuccess = contentView.findViewById(R.id.tvSuccess);
        tvTotpKey = contentView.findViewById(R.id.tvTotpKey);
        tvTotp = contentView.findViewById(R.id.tvTotp);
        tvProgress = contentView.findViewById(R.id.tvProgress);

        totpCode0 = contentView.findViewById(R.id.totpCode0);
        totpCode1 = contentView.findViewById(R.id.totpCode1);
        totpCode2 = contentView.findViewById(R.id.totpCode2);
        totpCode3 = contentView.findViewById(R.id.totpCode3);
        totpCode4 = contentView.findViewById(R.id.totpCode4);
        totpCode5 = contentView.findViewById(R.id.totpCode5);

        DebugUtils.setMfaLayoutVisibility(contentView);
        //---------在这里update一下的作用是为了避免后台线程只能在时间整除60的情况下计算code，不在60的情况下就不会显示code
        updateTotp();
        tvSuccess.setText("绑定成功:"+ ProfileUtils.getDeviceCode() + "公钥:"+ProfileUtils.getPublicKey()+"私钥:"+ProfileUtils.getPrivateKey());

        btnScan = contentView.findViewById(R.id.btnScan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("","begin scan ");
                scanMarginScanner();
            }
        });
        return contentView;
    }

    public void scanMarginScanner() {
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setOrientationLocked(false);
        integrator.setCaptureActivity(SmallCaptureActivity.class);
        integrator.initiateScan();
    }



    private void updateTotp(){
        TotpKeyVO vo = ProfileUtils.getTotp();
        if(vo!=null && vo.getNewKey() != null){
            String totpCode = TOTPPasswordGenerator.generateOTP(vo.getNewKey(),vo.getTime(),vo.getStep());
            tvTotp.setText(totpCode);
            if(StringUtils.isNotBlank(totpCode) && totpCode.length() == 6){
                Log.i("ttt" , totpCode);
                updateTotpTextView(totpCode);
            }
        }
    }

    private void updateTotpTextView(String totpCode){
        totpCode0.setText(totpCode.substring(0,1));
        totpCode1.setText(totpCode.substring(1,2));
        totpCode2.setText(totpCode.substring(2,3));
        totpCode3.setText(totpCode.substring(3,4));
        totpCode4.setText(totpCode.substring(4,5));
        totpCode5.setText(totpCode.substring(5,6));
    }

    @Override
    public void onStart(){
        super.onStart();
        task = new TabMfaFragment.TotpRefreshTask(true);
        task.start();
    }

    @Override
    public void onStop(){
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
                    Toast.makeText(getActivity(),restResult.getMessage(),Toast.LENGTH_SHORT).show();
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
                updateTotpTextView(totpCode);
            }
            tvTotpKey.setText(ProfileUtils.getTotp().getNewKey());
            tvProgress.setText(progress);
        }
    }
}
