package com.gt.magicbox.wificonnention;

import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.wificonnention.view.IWifiConectionView;
import com.isupatches.wisefy.WiseFy;

import java.util.List;

/**
 * Created by wzb on 2017/7/14 0014.
 */

public class WifiConnectionActivity extends BaseActivity implements IWifiConectionView{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_connention);
        setToolBartitle("无线网络");
    }

    @Override
    public void showScanWifi() {

    }

    @Override
    public void joinNetwork(String ssidName) {

    }
}
