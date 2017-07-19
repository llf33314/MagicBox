package com.gt.magicbox.setting.wificonnention;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.setting.wificonnention.model.WifiBean;
import com.gt.magicbox.setting.wificonnention.presenter.WifiConnectionPresenter;
import com.gt.magicbox.setting.wificonnention.view.IWifiConectionView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wzb on 2017/7/14 0014.
 */

public class WifiConnectionActivity extends BaseActivity implements IWifiConectionView {


    @BindView(R.id.btn_scan_wifi)
    Button btnScanWifi;
    @BindView(R.id.rv_wifi_result)
    RecyclerView rvWifiResult;

    WifiConnectionPresenter presenter;
    JoinWifiDialog dialog;

    private WifiRecyclerViewAdapter rvAdapter;
    private WifiRecyclerViewAdapter.OnRecyclerViewItemClickListener rvListener;

    private ConnectionChangeBroadCast broadCast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_connention);
        presenter=new WifiConnectionPresenter(this);
        setToolBarTitle("无线网络");
        rvWifiResult.setLayoutManager(new LinearLayoutManager(this));
        rvWifiResult.setHasFixedSize(true);

        //presenter.scanWifi();
        presenter.startLoopScanWifi();
        setBoradCase();
    }

    private void setBoradCase(){
        broadCast=new ConnectionChangeBroadCast();
        IntentFilter intentFilter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(broadCast,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(broadCast);
    }

    @Override
    public void showScanWifi(List<WifiBean> wifiList) {
        rvWifiResult.setAdapter(rvAdapter=new WifiRecyclerViewAdapter(wifiList));
        if (rvListener==null){
            rvListener=new WifiRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view) {
                    int position=rvWifiResult.getChildPosition(view);
                    WifiBean bean =presenter.getWifiBeanFormPosition(position);
                    if (bean!=null){
                        if (bean.getLockType()==0){//无加锁 wifi
                            presenter.addOpenNetwork(bean.getName());
                        }else {
                            if (dialog==null){
                                dialog=new JoinWifiDialog(WifiConnectionActivity.this,R.style.HttpRequestDialogStyle,presenter);
                            }

                            if (bean.getConnectState()==1||bean.getConnectState()==2){//已连接或者连接中
                                dialog.show(bean,0);
                            }else if((bean.getConnectState()==0)&&bean.isSave()){//已保存未连接
                                dialog.show(bean,1);
                            }else{//未连接
                                dialog.show(bean,2);
                            }
                        }
                    }
                }

                @Override
                public void onItemLongClick(View view) {

                }
            };
        }
        rvAdapter.setOnItemClickListener(rvListener);
    }

    @Override
    public void joinNetwork(String ssidName) {

    }

    @OnClick(R.id.btn_scan_wifi)
    public void onViewClicked(View v) {
        presenter.scanWifi();
    }

    private class ConnectionChangeBroadCast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
                presenter.scanWifi();
            }
        }
    }


}
