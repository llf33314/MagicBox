package com.gt.magicbox.setting.wificonnention;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.OnRecyclerViewItemClickListener;
import com.gt.magicbox.setting.wificonnention.model.WifiBean;
import com.gt.magicbox.setting.wificonnention.presenter.WifiConnectionPresenter;
import com.gt.magicbox.setting.wificonnention.view.IWifiConectionView;
import com.gt.magicbox.utils.commonutil.ToastUtil;

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
    @BindView(R.id.sw_wifi)
    Switch swWifi;
    @BindView(R.id.tv_wifi_switch)
    TextView tvWifi;

    WifiConnectionPresenter presenter;
    JoinWifiDialog dialog;

    private WifiRecyclerViewAdapter rvAdapter;
    private OnRecyclerViewItemClickListener rvListener;

    private ConnectionChangeBroadCast broadCast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_connention);
        setToolBarTitle("无线网络");
        init();
    }

    private void init(){
        presenter=new WifiConnectionPresenter(this);
        rvWifiResult.setLayoutManager(new LinearLayoutManager(this));
        rvWifiResult.setHasFixedSize(true);
        //presenter.scanWifi();
        if (presenter.isWifiEnabled()){
            presenter.startLoopScanWifi();
        }
        swWifi.setChecked(presenter.isWifiEnabled());
        swWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.setWifiEnable(isChecked);
            }
        });

        setBoradCast();
    }

    private void setBoradCast(){
        broadCast=new ConnectionChangeBroadCast();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        this.registerReceiver(broadCast,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(broadCast);
        presenter.cancelScan();
    }

    @Override
    public void showScanWifi(List<WifiBean> wifiList) {
        //这样写不好  但是问题不大
        rvWifiResult.setAdapter(rvAdapter=new WifiRecyclerViewAdapter(wifiList));
        if (rvListener==null){
            rvListener=new OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view) {
                    int position=rvWifiResult.getChildLayoutPosition(view);
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
        if (!presenter.isWifiEnabled()){
            ToastUtil.getInstance().showToast("请打开Wifi再进行扫描");
            return;
        }
        presenter.scanWifi( );
    }

    private class ConnectionChangeBroadCast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)){
                presenter.scanWifi();
            }else if(WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)){
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
                switch (wifiState){
                    case WifiManager.WIFI_STATE_DISABLED:
                        tvWifi.setText("关闭");
                        break;
                    case WifiManager.WIFI_STATE_DISABLING:
                        tvWifi.setText("关闭中...");
                        presenter.cancelScan();
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        tvWifi.setText("开启");
                        presenter.startLoopScanWifi();

                        break;
                    case WifiManager.WIFI_STATE_ENABLING:
                        tvWifi.setText("开启中...");
                        break;

                }
            }
        }
    }
}
