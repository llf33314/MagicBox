package com.gt.magicbox.wificonnention;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.utils.commonutil.ToastUtil;
import com.gt.magicbox.wificonnention.model.WifiBean;
import com.gt.magicbox.wificonnention.presenter.WifiConnectionPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wzb on 2017/7/17 0017.
 */

public class JoinWifiDialog extends Dialog {
    @BindView(R.id.join_wifi_ssid)
    TextView joinWifiSsid;
    @BindView(R.id.join_wifi_psd)
    TextView joinWifiPsd;
    @BindView(R.id.join_wifi_cancel)
    Button joinWifiCancel;
    @BindView(R.id.join_wifi_join)
    Button joinWifiJoin;

    private WifiBean wifiBean;

    WifiConnectionPresenter presenter;

    public JoinWifiDialog(@NonNull Context context) {
        super(context);
    }

    public JoinWifiDialog(@NonNull Context context, @StyleRes int themeResId, WifiConnectionPresenter p) {
        super(context, themeResId);
        this.presenter=p;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_join_wifi);
        ButterKnife.bind(this);
    }

    public void show(WifiBean wifiBean){
        show();
        this.wifiBean=wifiBean;
        joinWifiSsid.setText(this.wifiBean.getName());
    }

    @OnClick({R.id.join_wifi_cancel, R.id.join_wifi_join})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.join_wifi_cancel:
                dismiss();
                break;
            case R.id.join_wifi_join:
                String psd=joinWifiPsd.getText().toString().trim();
                //-1 密码错误  3 成功  -1001 已经添加过  -1000账号密码为空
                int result=-2;

                switch (wifiBean.getLockType()){
                    case 1:
                        result= presenter.addWEPNetwork(wifiBean.getName(),psd);
                        break;
                    case 2:
                    case 3:
                        result= presenter.addWPA2Network(wifiBean.getName(),psd);
                        break;
                }
                switch (result){
                    case -1:
                    case -1001:
                    case -1000:
                        ToastUtil.getInstance().showToast("密码有误");
                        break;
                    case 3:
                        //这里会阻塞
                       boolean success=presenter.connectToNetwork(wifiBean.getName(),10*1000);
                     //   处理线程问题
                        ToastUtil.getInstance().showToast("success:"+success);
                        break;
                    default:
                        ToastUtil.getInstance().showToast("连接失败");
                        break;
                }

                dismiss();

                break;
        }
    }
}
