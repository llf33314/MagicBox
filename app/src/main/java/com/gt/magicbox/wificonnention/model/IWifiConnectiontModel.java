package com.gt.magicbox.wificonnention.model;

import android.net.wifi.ScanResult;

import java.util.List;

/**
 * Created by wzb on 2017/7/14 0014.
 */

public interface IWifiConnectiontModel {
    List<ScanResult> scanWifi();
}
