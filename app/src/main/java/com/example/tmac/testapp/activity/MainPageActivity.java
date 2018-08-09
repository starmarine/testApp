package com.example.tmac.testapp.activity;

import android.os.AsyncTask;
import android.os.Bundle;
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
    private TextView tvSuccess;
    private TextView tvTotpKey;
    private TextView tvTotp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        tvSuccess = findViewById(R.id.tvSuccess);
        tvTotpKey = findViewById(R.id.tvTotpKey);
        tvTotp = findViewById(R.id.tvTotp);

        new TotpRefreshTask().execute();

        tvSuccess.setText("绑定成功:"+ProfileUtils.getDeviceCode() + "公钥:"+ProfileUtils.getPublicKey()+"私钥:"+ProfileUtils.getPrivateKey());
    }

    class TotpRefreshTask extends AsyncTask<Void, Integer, RestResult> {

        public TotpRefreshTask(){
        }

        @Override
        protected RestResult doInBackground(Void... params) {
            TotpKeyVO vo = ProfileUtils.getTotp();
            String oldKey = vo == null? null:vo.getNewKey();
            TotpKeyDto dto = new TotpKeyDto(oldKey,ProfileUtils.getDeviceCode());
            String json = JSON.toJSONString(dto);
            Log.i("exchange totp",json);
            String body = EncryptedHttpUtils.post(Constants.generateURL(Constants.PATH_EXCHANGE_TOTP_KEY),json);
            Log.i("exchange totp",body);
            RestResult restResult = JSON.parseObject(body, RestResult.class);
            Log.i("exchange totp",restResult.toString());
            return restResult;
        }

        @Override
        protected void onPostExecute(RestResult restResult) {
            updateUI(restResult);
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
