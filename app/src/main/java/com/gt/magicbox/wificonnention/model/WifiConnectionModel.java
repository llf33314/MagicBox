package com.gt.magicbox.wificonnention.model;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;

import com.isupatches.wisefy.WiseFy;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

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
    public Observable<List<ScanResult>> scanWifi() {

        return Observable.create(new ObservableOnSubscribe<List<ScanResult>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<ScanResult>> e) throws Exception {
                e.onNext(mWiseFy.getNearbyAccessPoints(false));
            }
        });
    }

    @Override
    public WiseFy getWiseFy() {
        return mWiseFy;
    }
}
