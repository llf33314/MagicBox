package com.gt.magicbox.wificonnention;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.wificonnention.presenter.WifiConnectionPresenter;
import com.gt.magicbox.wificonnention.view.IWifiConectionView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wzb on 2017/7/14 0014.
 */

public class WifiConnectionActivity extends BaseActivity implements IWifiConectionView {


    @BindView(R.id.btn_scan_wifi)
    Button btnScanWifi;
    @BindView(R.id.rv_wifi_result)
    RecyclerView rvWifiResult;

    WifiConnectionPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_connention);
        ButterKnife.bind(this);
        presenter=new WifiConnectionPresenter(this);
        setToolBarTitle("无线网络");
        rvWifiResult.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void showScanWifi() {

    }

    @Override
    public void joinNetwork(String ssidName) {

    }

    @OnClick(R.id.btn_scan_wifi)
    public void onViewClicked(View v) {
        presenter.scanWifi();
    }
}
