package com.gt.magicbox.wificonnention.model;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;

import com.isupatches.wisefy.WiseFy;

import java.util.List;

/**
 * Created by wzb on 2017/7/14 0014.
 */

public class WifiConnectionModel implements IWifiConnectiontModel{
    private Context mContext ;
    private  WiseFy mWiseFy;;
    public WifiConnectionModel(Context context){
        this.mContext=context;

        mWiseFy = new WiseFy.withContext(mContext).getSmarts();
    }



    @Override
    public List<ScanResult> scanWifi() {
        List<ScanResult> nearbyAccessPoints = mWiseFy.getNearbyAccessPoints(false);
        return nearbyAccessPoints;
    }
}
