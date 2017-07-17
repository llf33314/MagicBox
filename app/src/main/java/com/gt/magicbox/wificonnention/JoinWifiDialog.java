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
import com.gt.magicbox.wificonnention.model.WifiBean;

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

    public JoinWifiDialog(@NonNull Context context) {
        super(context);
    }

    public JoinWifiDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
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
                break;
        }
    }
}
