package com.example.tmac.testapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tmac.testapp.R;


public class TabUserSettingFragment extends Fragment {
    private TextView tvUserName;
    public static TabUserSettingFragment newInstance(){
        Bundle arguments = new Bundle();
        TabUserSettingFragment tabContentFragment = new TabUserSettingFragment();
        tabContentFragment.setArguments(arguments);
        return tabContentFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_tab_user_setting, null);

        tvUserName = contentView.findViewById(R.id.tvUserName);

        return contentView;
    }
}
