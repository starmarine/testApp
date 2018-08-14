package com.example.tmac.testapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.tmac.testapp.activity.AbstractBaseActivity;
import com.example.tmac.testapp.activity.QRCodeActivity;
import com.example.tmac.testapp.constants.Constants;
import com.example.tmac.testapp.dto.dto.ScanQrcodeDto;
import com.example.tmac.testapp.dto.vo.RestResult;
import com.example.tmac.testapp.dto.vo.ScanQrcodeVO;
import com.example.tmac.testapp.fragment.TabMfaFragment;
import com.example.tmac.testapp.fragment.TabUserLogFragment;
import com.example.tmac.testapp.fragment.TabUserSettingFragment;
import com.example.tmac.testapp.utils.LogUtil;
import com.example.tmac.testapp.utils.ProfileUtils;
import com.example.tmac.testapp.utils.http.EncryptedHttpUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AbstractBaseActivity {
    public static String INTENT_KEY_QRCODE_VO = "INTENT_KEY_QRCODE_VO";
    public static String INTENT_KEY_SCAN_QRCODE_DTO = "INTENT_KEY_SCAN_QRCODE_DTO";

    private TabLayout mTabTl;
    private ViewPager mContentVp;

    private List<String> tabIndicators;
    private List<Fragment> tabFragments;
    private ContentPagerAdapter contentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabTl = (TabLayout) findViewById(R.id.tl_tab);
        mContentVp = (ViewPager) findViewById(R.id.vp_content);

        initContent();
        initTab();
    }

    private void initTab(){
        mTabTl.setTabMode(TabLayout.MODE_FIXED);
        mTabTl.setSelectedTabIndicatorHeight(0);
        ViewCompat.setElevation(mTabTl, 10);
        mTabTl.setupWithViewPager(mContentVp);
        for (int i = 0; i < tabIndicators.size(); i++) {
            TabLayout.Tab itemTab = mTabTl.getTabAt(i);
            if (itemTab!=null){
                itemTab.setCustomView(R.layout.item_tab_layout_custom);
                TextView itemTv = (TextView) itemTab.getCustomView().findViewById(R.id.tv_menu_item);
                itemTv.setText(tabIndicators.get(i));
            }
        }
        mTabTl.getTabAt(0).getCustomView().setSelected(true);
    }

    private void initContent(){
        tabIndicators = new ArrayList<>();
        tabFragments = new ArrayList<>();
        for (int i = 0; i < 3; i++) {

            if(i == 0){
                tabIndicators.add("MFA");
                tabFragments.add(TabMfaFragment.newInstance());
            }else if(i == 1){
                tabIndicators.add("风险管理");
                tabFragments.add(TabUserLogFragment.newInstance());
            }else{
                tabIndicators.add("用户设置");
                tabFragments.add(TabUserSettingFragment.newInstance());
            }
        }
        contentAdapter = new ContentPagerAdapter(getSupportFragmentManager());
        mContentVp.setAdapter(contentAdapter);
    }

    class ContentPagerAdapter extends FragmentPagerAdapter{

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabFragments.get(position);
        }

        @Override
        public int getCount() {
            return tabIndicators.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabIndicators.get(position);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                LogUtil.i("Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                String authId = result.getContents();
                LogUtil.i("Scanned:" + authId);
                ScanQrcodeDto bindingDto = new ScanQrcodeDto(authId, ProfileUtils.getDeviceCode());
                Toast.makeText(this, "Scanned1: " + bindingDto, Toast.LENGTH_LONG).show();
                //-----------TODO验证dto是否符合格式---------
                ScanQrcodeTask task = new ScanQrcodeTask(bindingDto);
                task.execute();
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
            LogUtil.i("access server");

            String json = JSON.toJSONString(scanQrcodeDto);
            LogUtil.i(json);
            String body = EncryptedHttpUtils.post(Constants.generateURL(Constants.PATH_QRCODE_SCAN),json);
            LogUtil.i(body);
            RestResult restResult = JSON.parseObject(body, RestResult.class);
            Log.i("ScanQrcodeTask",restResult.toString());
            //-----------------TODO 要考虑restResult的httpStatus------------------------------------------------

            return restResult;
        }

        @Override
        protected void onPostExecute(RestResult restResult) {
            LogUtil.i("=======================");
            LogUtil.i(restResult.toString());

            if(restResult.isHttpStatusOK()){
                ScanQrcodeVO vo = JSON.parseObject(restResult.getData(),ScanQrcodeVO.class);
                LogUtil.i(vo.toString());
                jumpToConfirmPage(vo,scanQrcodeDto);
            }else{
                Toast.makeText(MainActivity.this,restResult.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void jumpToConfirmPage(ScanQrcodeVO vo,ScanQrcodeDto scanQrcodeDto){
        Intent intent = new Intent(MainActivity.this, QRCodeActivity.class);
        intent.putExtra(INTENT_KEY_QRCODE_VO, vo);
        intent.putExtra(INTENT_KEY_SCAN_QRCODE_DTO, scanQrcodeDto);
        startActivity(intent);
    }
}
