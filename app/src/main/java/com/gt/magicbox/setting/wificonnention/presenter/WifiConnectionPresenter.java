package com.gt.magicbox.setting.wificonnention.presenter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.text.TextUtils;
import android.util.Log;

import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.http.RxObservableUtils;
import com.gt.magicbox.setting.wificonnention.model.IWifiConnectiontModel;
import com.gt.magicbox.setting.wificonnention.model.WifiBean;
import com.gt.magicbox.setting.wificonnention.model.WifiConnectionModel;
import com.gt.magicbox.setting.wificonnention.view.IWifiConectionView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
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

    private List<WifiBean> resultWfifs=new ArrayList<WifiBean>();

    private  List<WifiConfiguration> savedNetworks=new ArrayList<WifiConfiguration>();

    public WifiConnectionPresenter(IWifiConectionView wifiConectionView){
        this.wifiConectionView=wifiConectionView;
        wifiConnectiontModel=new WifiConnectionModel((Context) this.wifiConectionView);
    }

    private Disposable loopDisposable;

    Comparator<WifiBean> comparator=new Comparator<WifiBean>() {
        @Override
        public int compare(WifiBean o2, WifiBean o1) {
            if (o1.getConnectState()-o2.getConnectState()!=0){
                return o1.getConnectState()-o2.getConnectState();
            }
            if (o1.isSave()&&o2.isSave()){
                return 0;
            }
            if (o1.isSave()&&!o2.isSave()){
                return 1;
            }
            if (!o1.isSave()&&o2.isSave()){
                return -1;
            }
            if (o1.getSignLevel()==o2.getSignLevel()){
                return 0;
            }
            return o1.getSignLevel()>o2.getSignLevel()?1:-1;
        }
    };

    public void  scanWifi(){
        wifiConnectiontModel.scanWifi()
                .compose(RxObservableUtils.<List<ScanResult>>applySchedulers())
                .compose(((BaseActivity)wifiConectionView).<List<ScanResult>>bindToLifecycle())
                .map(new Function<List<ScanResult>, List<WifiBean>>() {

                    @Override
                    public List<WifiBean> apply(@NonNull List<ScanResult> scanResults) throws Exception {
                        //当前连接的网络wifi
                        WifiInfo wifiInfo=wifiConnectiontModel.getWiseFy().getCurrentNetwork();
                        //当ssid="";会有问题？
                        String currentSsid= wifiInfo==null?"":wifiInfo.getBSSID();


                        resultWfifs.clear();
                        //已保存过的wifi
                        if (savedNetworks==null){
                            savedNetworks=new ArrayList<WifiConfiguration>();
                        }
                        savedNetworks.clear();
                        savedNetworks =wifiConnectiontModel.getWiseFy().getSavedNetworks();

                        for (ScanResult s:scanResults){
                            WifiBean wifiBean=new WifiBean();
                            if (TextUtils.isEmpty(s.SSID)){ //去除SSID为空字符的WIFI？  setting是这样子
                                continue;
                            }
                            wifiBean.setName(s.SSID);
                            if (TextUtils.isEmpty(currentSsid)?false:currentSsid.equals(s.BSSID)){
                                if (wifiConnectiontModel.getWiseFy().isDeviceConnectedToWifiNetwork()){//设备是否已经连接到wifi网络  用于区别连接中
                                    wifiBean.setConnectState(1);
                                }else{
                                    wifiBean.setConnectState(2);
                                }
                            }else{
                                wifiBean.setConnectState(0);
                            }

                            /**
                             * 加密类型  0：不加密
                             *           1：WEP
                             *           2：PSK
                             *           3:EAP
                             */
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
                                    if(("\""+s.SSID+"\"").equals(saved.SSID)){ //debug 发现会多个双引号？
                                        wifiBean.setSave(true);
                                        break;
                                    }
                                }else{
                                    wifiBean.setSave(false);
                                }
                            }
                            int signLevel=wifiConnectiontModel.getWiseFy().calculateBars(s.level,4);
                            wifiBean.setSignLevel(signLevel);
                            resultWfifs.add(wifiBean);
                        }
                        //排序wifi
                        Collections.sort(resultWfifs,comparator);

                        return resultWfifs;
                    }
                })
                .subscribe(new Observer<List<WifiBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<WifiBean> resultWfifs) {
                       // Toast.makeText((Context) wifiConectionView,resultWfifs.size()+"\n"+resultWfifs.get(0).getName(),Toast.LENGTH_LONG).show();
                        wifiConectionView.showScanWifi(resultWfifs);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG,e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public WifiBean getWifiBeanFormPosition(int position){
        if (resultWfifs.size()>0){
            return  resultWfifs.get(position);
        }
        return null;
    }

    public int addOpenNetwork(String ssid){
        return wifiConnectiontModel.getWiseFy().addOpenNetwork(ssid);
    }

    public int addWEPNetwork(String ssid,String psd){
        return wifiConnectiontModel.getWiseFy().addWEPNetwork(ssid,psd);
    }

    public int addWPA2Network(String ssid,String psd){
        return wifiConnectiontModel.getWiseFy().addWPA2Network(ssid,psd);
    }

    public boolean connectToNetwork(String ssid,int timeOut){
        return wifiConnectiontModel.getWiseFy().connectToNetwork(ssid,timeOut);
    }

    public boolean removeNetworkAndDisConnect(String ssid){
        return wifiConnectiontModel.getWiseFy().removeNetwork(ssid);
    }
    public boolean removeNetworkConfig(String ssid){
        for (WifiConfiguration saved:savedNetworks){
        if(("\""+ssid+"\"").equals(saved.SSID)){
           return wifiConnectiontModel.getWiseFy().mWifiManager.removeNetwork(saved.networkId);
        }
        }
        return false;
    }
    public boolean disconnectFromCurrentNetwork(){
        return wifiConnectiontModel.getWiseFy().disconnectFromCurrentNetwork();
    }

    /**
     * 每隔3秒更新页面
     */
    public void startLoopScanWifi(){
        Observable.interval(300,3000, TimeUnit.MILLISECONDS)
                .compose(((BaseActivity)wifiConectionView).<Long>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        loopDisposable=d;
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        scanWifi();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    public void cancelScan(){
        if (loopDisposable!=null&&!loopDisposable.isDisposed()){
            loopDisposable.dispose();
        }
    }

    public boolean isWifiEnabled(){
        return wifiConnectiontModel.getWiseFy().isWifiEnabled();
    }

    public void setWifiEnable(boolean enable){
        if (enable){
            wifiConnectiontModel.getWiseFy().enableWifi();
        }else{
            wifiConnectiontModel.getWiseFy().disableWifi();
        }
    }

}
