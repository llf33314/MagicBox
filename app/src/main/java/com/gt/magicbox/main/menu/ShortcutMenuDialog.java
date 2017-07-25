package com.gt.magicbox.main.menu;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.gt.magicbox.R;
import com.gt.magicbox.setting.DeviceInfoActivity;
import com.gt.magicbox.setting.VolumeSettingActivity;
import com.gt.magicbox.setting.printersetting.PrinterSettingActivity;
import com.gt.magicbox.setting.wificonnention.WifiConnectionActivity;
import com.gt.magicbox.utils.commonutil.ScreenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wzb on 2017/7/25 0025.
 */

public class ShortcutMenuDialog extends Dialog {
    @BindView(R.id.ll_shortcut_wifi)
    LinearLayout llShortcutWifi;
    @BindView(R.id.ll_shortcut_printer)
    LinearLayout llShortcutPrinter;
    @BindView(R.id.ll_shortcut_volume)
    LinearLayout llShortcutVolume;
    @BindView(R.id.ll_shortcut_device)
    LinearLayout llShortcutDevice;

    public ShortcutMenuDialog(@NonNull Context context) {
        super(context);
    }

    public ShortcutMenuDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_shortcut_menu);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.height = ScreenUtils.getScreenHeight() / 2;
        lp.width = ScreenUtils.getScreenWidth();
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);


    }

    @OnClick({R.id.ll_shortcut_wifi, R.id.ll_shortcut_printer, R.id.ll_shortcut_volume, R.id.ll_shortcut_device})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.ll_shortcut_wifi:
                intent=new Intent(ShortcutMenuDialog.this.getContext(), WifiConnectionActivity.class);
                break;
            case R.id.ll_shortcut_printer:
                intent=new Intent(ShortcutMenuDialog.this.getContext(), PrinterSettingActivity.class);
                break;
            case R.id.ll_shortcut_volume:
                intent=new Intent(ShortcutMenuDialog.this.getContext(), VolumeSettingActivity.class);
                break;
            case R.id.ll_shortcut_device:
                intent=new Intent(ShortcutMenuDialog.this.getContext(), DeviceInfoActivity.class);
                break;
        }
        ShortcutMenuDialog.this.getContext().startActivity(intent);
    }
}
