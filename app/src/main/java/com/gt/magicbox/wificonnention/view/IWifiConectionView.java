package com.gt.magicbox.wificonnention.view;

import com.gt.magicbox.wificonnention.model.WifiBean;

import java.util.List;

/**
 * Created by wzb on 2017/7/14 0014.
 */

public interface IWifiConectionView {
    void showScanWifi(List<WifiBean> wifiList);
    void joinNetwork(String ssidName);
}
