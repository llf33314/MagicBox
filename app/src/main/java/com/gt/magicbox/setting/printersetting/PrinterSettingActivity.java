package com.gt.magicbox.setting.printersetting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.setting.printersetting.bluetooth.BluetoothSettingActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wzb on 2017/7/20 0020.
 */

public class PrinterSettingActivity extends BaseActivity {
    @BindView(R.id.tv_config_bt)
    TextView tvConfigBt;
    @BindView(R.id.tv_config_usb)
    TextView tvConfigUsb;
    @BindView(R.id.tv_config_show)
    TextView tvConfigShow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_setting);
        ButterKnife.bind(this);
        setToolBarTitle("打印设置");
    }

    @OnClick({R.id.tv_config_bt, R.id.tv_config_usb, R.id.tv_config_show})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_config_bt:
                intent=new Intent(PrinterSettingActivity.this, BluetoothSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_config_usb:
                break;
            case R.id.tv_config_show:
                break;
        }
    }
}
