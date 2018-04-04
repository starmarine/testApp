package com.example.tmac.testapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.tmac.testapp.activity.AbstractBaseActivity;
import com.example.tmac.testapp.test.TestJsonDto;
import com.example.tmac.testapp.utils.HttpUtils;

public class MainActivity extends AbstractBaseActivity {
    private TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = findViewById(R.id.textView);
        Button button = findViewById(R.id.netButton);
        updateUI("111111111111111111111111");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DownloadTask().execute();
            }
        });
    }

    public void updateUI(String subLemmaId){
        textView1.setText(subLemmaId);
    }

    class DownloadTask extends AsyncTask<Void, Integer, TestJsonDto> {

        @Override
        protected TestJsonDto doInBackground(Void... params) {
            Log.i("main","click netButton");
            String body = HttpUtils.get("http://baike.baidu.com/api/openapi/BaikeLemmaCardApi?scope=103&format=json&appid=379020&bk_key=test&bk_length=10",handler);
            System.out.println(body);
            TestJsonDto dto = JSON.parseObject(body, TestJsonDto.class);
            return dto;
        }

        @Override
        protected void onPostExecute(TestJsonDto dto) {
            updateUI(dto.getSubLemmaId());
        }
    }

}
