package com.gt.magicbox.wificonnention.presenter;

import android.content.Context;

import com.gt.magicbox.wificonnention.model.IWifiConnectiontModel;
import com.gt.magicbox.wificonnention.model.WifiConnectionModel;
import com.gt.magicbox.wificonnention.view.IWifiConectionView;

/**
 * Created by wzb on 2017/7/14 0014.
 */

public class WifiConnectionPresenter {
    IWifiConectionView wifiConectionView;
    IWifiConnectiontModel wifiConnectiontModel;

    public WifiConnectionPresenter(IWifiConectionView wifiConectionView){
        this.wifiConectionView=wifiConectionView;
        wifiConnectiontModel=new WifiConnectionModel((Context) this.wifiConectionView);
    }

}
