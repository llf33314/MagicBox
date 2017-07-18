package com.gt.magicbox.wificonnention.presenter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.text.TextUtils;
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

import io.reactivex.Observable;
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
    private List<WifiBean> resultWfifs=new ArrayList<WifiBean>();

    public WifiConnectionPresenter(IWifiConectionView wifiConectionView){
        this.wifiConectionView=wifiConectionView;
        wifiConnectiontModel=new WifiConnectionModel((Context) this.wifiConectionView);
    }

    public void  scanWifi(){
        wifiConnectiontModel.scanWifi()
                .compose(RxObservableUtils.<List<ScanResult>>applySchedulers())
                .compose(((BaseActivity)wifiConectionView).<List<ScanResult>>bindToLifecycle())
                .flatMap(new Function<List<ScanResult>, ObservableSource<List<WifiBean>>>() {
                    @Override
                    public ObservableSource<List<WifiBean>> apply(@NonNull List<ScanResult> scanResults) throws Exception {
                        //当前连接的网络wifi
                        WifiInfo wifiInfo=wifiConnectiontModel.getWiseFy().getCurrentNetwork();
                        //当ssid="";会有问题？
                        String currentSsid= wifiInfo==null?"":wifiInfo.getBSSID();

                        //已保存过的wifi
                        List<WifiConfiguration> savedNetworks =wifiConnectiontModel.getWiseFy().getSavedNetworks();

                        resultWfifs.clear();

                        for (ScanResult s:scanResults){
                            WifiBean wifiBean=new WifiBean();
                            if (TextUtils.isEmpty(s.SSID)){ //去除SSID为空字符的WIFI？  setting是这样子
                                continue;
                            }
                            wifiBean.setName(s.SSID);
                            wifiBean.setConnecting(TextUtils.isEmpty(currentSsid)?false:currentSsid.equals(s.BSSID));
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

                        return Observable.just(resultWfifs);
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

    public boolean connectToNetwork(String ssid,int number){
        return wifiConnectiontModel.getWiseFy().connectToNetwork(ssid,number);
    }


}
