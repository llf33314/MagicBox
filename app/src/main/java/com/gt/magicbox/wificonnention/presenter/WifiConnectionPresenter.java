package com.gt.magicbox.wificonnention.presenter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.util.Log;
import android.widget.Toast;

import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.http.RxObservableUtils;
import com.gt.magicbox.wificonnention.model.IWifiConnectiontModel;
import com.gt.magicbox.wificonnention.model.WifiBean;
import com.gt.magicbox.wificonnention.model.WifiConnectionModel;
import com.gt.magicbox.wificonnention.view.IWifiConectionView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by wzb on 2017/7/14 0014.
 */

public class WifiConnectionPresenter {
    private final String TAG=WifiConnectionPresenter.class.getSimpleName();
    IWifiConectionView wifiConectionView;
    IWifiConnectiontModel wifiConnectiontModel;

    public WifiConnectionPresenter(IWifiConectionView wifiConectionView){
        this.wifiConectionView=wifiConectionView;
        wifiConnectiontModel=new WifiConnectionModel((Context) this.wifiConectionView);
    }

    public void  scanWifi(){
       /* wifiConnectiontModel.scanWifi()
                .compose(RxObservableUtils.<List<ScanResult>>applySchedulers())
                .compose(((BaseActivity)wifiConectionView).<List<ScanResult>>bindToLifecycle())
                .flatMap(new Function<List<ScanResult>, ObservableSource<List<WifiBean>>>() {
                    @Override
                    public ObservableSource<List<WifiBean>> apply(@NonNull List<ScanResult> scanResults) throws Exception {
                        WifiInfo wifiInfo=wifiConnectiontModel.getWiseFy().getCurrentNetwork();
                        //当ssid="";会有问题？
                        String currentSsid= wifiInfo==null?"":wifiInfo.getSSID();

                        List<WifiConfiguration> savedNetworks =wifiConnectiontModel.getWiseFy().getSavedNetworks();


                        List<WifiBean> resultWfifs=new ArrayList<WifiBean>();
                        for (ScanResult s:scanResults){
                            WifiBean wifiBean=new WifiBean();
                            wifiBean.setName(s.SSID);
                            wifiBean.setConnecting(currentSsid==null?false:currentSsid.equals(s.SSID));
                            *//**
                             * 加密类型  0：不加密
                             *           1：WEP
                             *           2：PSK
                             *           3:EAP
                             *//*
                            if (s.capabilities.contains("WEP")){
                                wifiBean.setLockType(1);
                            }else if (s.capabilities.contains("PSK")){
                                wifiBean.setLockType(2);
                            }else if (s.capabilities.contains("EAP")){
                                wifiBean.setLockType(3);
                            }else{
                                wifiBean.setLockType(0);
                            }

                            for (WifiConfiguration saved:savedNetworks){
                                if (s.SSID!=null&&saved.SSID!=null){ //静态coding 判断
                                    if( s.SSID.equals(saved.SSID)){
                                        wifiBean.setSave(true);
                                        break;
                                    }
                                }else{
                                    wifiBean.setSave(false);
                                    break;
                                }
                            }
                           // int signLevel=wifiConnectiontModel.getWiseFy().
                           // wifiBean.setSignLevel();



                        }
                        return null;
                    }
                })
                .subscribe((Observer<? super List<WifiBean>>) new Observer<List<ScanResult>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<ScanResult> scanResults) {
                        // Toast.makeText((Context) wifiConectionView,scanResults.size()+"",Toast.LENGTH_LONG).show();
                        //ToastUtils.showShort(scanResults.size()+"");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG,e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                    }
                });
*/
    }

}
