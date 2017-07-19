package com.gt.magicbox.setting.wificonnention.view;

import com.gt.magicbox.setting.wificonnention.model.WifiBean;

import java.util.List;

/**
 * Created by wzb on 2017/7/14 0014.
 */

public interface IWifiConectionView {
    void showScanWifi(List<WifiBean> wifiList);
    void joinNetwork(String ssidName);
}
