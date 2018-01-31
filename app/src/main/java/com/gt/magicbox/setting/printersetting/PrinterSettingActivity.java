package com.gt.magicbox.setting.printersetting;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.custom_display.MatchActivity;
import com.gt.magicbox.main.MainActivity;
import com.gt.magicbox.setting.AutoPrintSettingActivity;
import com.gt.magicbox.setting.printersetting.bluetooth.BluetoothSettingActivity;
import com.gt.magicbox.setting.printersetting.usb.RxBusUsbMsg;
import com.gt.magicbox.utils.RxBus;
import com.gt.magicbox.utils.commonutil.AppManager;

import butterknife.BindView;
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
        setToolBarTitle("硬件设置");
    }

    @OnClick({R.id.tv_config_bt, R.id.tv_config_usb, R.id.tv_config_show, R.id.tv_auto_print_config})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_config_bt:
                intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivity(intent);
                break;
            case R.id.tv_config_usb:
                RxBus.get().post(new RxBusUsbMsg());
                break;
            case R.id.tv_config_show:
                AppManager.getInstance().finishActivity(MainActivity.class);
                intent = new Intent(PrinterSettingActivity.this, MatchActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_auto_print_config:
                intent = new Intent(PrinterSettingActivity.this, AutoPrintSettingActivity.class);
                startActivity(intent);
                break;
        }
    }


}
