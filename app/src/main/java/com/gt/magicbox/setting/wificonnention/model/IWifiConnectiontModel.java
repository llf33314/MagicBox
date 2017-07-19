package com.gt.magicbox.setting.wificonnention.model;

import android.net.wifi.ScanResult;

import com.isupatches.wisefy.WiseFy;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by wzb on 2017/7/14 0014.
 */

public interface IWifiConnectiontModel {
    Observable<List<ScanResult>> scanWifi();
    WiseFy getWiseFy();
}
