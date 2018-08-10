package com.example.tmac.testapp.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.tmac.testapp.R;
import com.example.tmac.testapp.constants.Constants;
import com.example.tmac.testapp.dto.dto.ScanQrcodeDto;
import com.example.tmac.testapp.dto.vo.RestResult;
import com.example.tmac.testapp.dto.vo.ScanQrcodeVO;
import com.example.tmac.testapp.utils.http.EncryptedHttpUtils;

public class QRCodeActivity extends AbstractBaseActivity {

    private TextView tvAppName;
    private TextView tvAccountName;
    private TextView tvMessage;
    private Button btnConfirm;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        tvAppName = findViewById(R.id.tvAppName);
        tvAccountName = findViewById(R.id.tvAccountName);
        tvMessage = findViewById(R.id.tvMessage);
        btnConfirm = findViewById(R.id.btnConfirmQrcode);
        btnCancel = findViewById(R.id.btnCancelQrcode);

        ScanQrcodeVO scanQrcodeVO = (ScanQrcodeVO) getIntent().getSerializableExtra(MainPageActivity.INTENT_KEY_QRCODE_VO);
        final ScanQrcodeDto scanQrcodeDto = (ScanQrcodeDto) getIntent().getSerializableExtra(MainPageActivity.INTENT_KEY_SCAN_QRCODE_DTO);
        Log.i("ScanQrcodeVO",scanQrcodeVO.toString());
        Log.i("scanQrcodeDto",scanQrcodeDto.toString());
        tvAppName.setText(scanQrcodeVO.getAppName());
        tvAccountName.setText(scanQrcodeVO.getAccountName());
        tvMessage.setText(scanQrcodeVO.getMessage());

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("","begin scan ");
                new ConfirmQrcodeTask(scanQrcodeDto).execute();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpBackToMainPage();
            }
        });
    }

    class ConfirmQrcodeTask extends AsyncTask<Void, Integer, RestResult> {
        private ScanQrcodeDto scanQrcodeDto;

        public ConfirmQrcodeTask(ScanQrcodeDto dto){
            scanQrcodeDto = dto;
        }

        @Override
        protected RestResult doInBackground(Void... params) {
            String json = JSON.toJSONString(scanQrcodeDto);
            Log.i("ConfirmQrcodeTask",json);
            String body = EncryptedHttpUtils.post(Constants.generateURL(Constants.PATH_QRCODE_CONFIRM),json);
            Log.i("ConfirmQrcodeTask",body);
            RestResult restResult = JSON.parseObject(body, RestResult.class);
            Log.i("ConfirmQrcodeTask",restResult.toString());

            return restResult;
        }

        @Override
        protected void onPostExecute(RestResult restResult) {
            Log.i("ConfirmQrcodeTask","=======================");
            Log.i("ConfirmQrcodeTask",restResult.toString());

            if(restResult.isHttpStatusOK()){
                Toast.makeText(QRCodeActivity.this,"操作成功",Toast.LENGTH_SHORT).show();
                jumpBackToMainPage();
            }else{
                Toast.makeText(QRCodeActivity.this,restResult.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void jumpBackToMainPage(){
        finish();
    }
}
