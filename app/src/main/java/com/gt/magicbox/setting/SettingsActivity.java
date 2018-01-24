package com.gt.magicbox.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.http.HttpConfig;
import com.gt.magicbox.main.GridItem;
import com.gt.magicbox.main.HomeGridViewAdapter;
import com.gt.magicbox.setting.printersetting.PrinterSettingActivity;
import com.gt.magicbox.update.OnTaskFinishListener;
import com.gt.magicbox.update.UpdateManager;
import com.gt.magicbox.utils.commonutil.ToastUtil;

import java.util.ArrayList;

/**
 * Description:
 * Created by jack-lin on 2018/1/24 0024.
 * Buddha bless, never BUG!
 */

public class SettingsActivity extends BaseActivity {
    private String[] itemNameArray = {"硬件设置", "网络设置", "音量设置", "设备状态", "版本更新"};
    private Integer[] imageResArray = {R.drawable.settings_hardware, R.drawable.setting_network
            , R.drawable.setting_volume, R.drawable.setting_device_status, R.drawable.setting_app_update};
    private int[] colorNormalArray = {0xff4db3ff, 0xffff9a54, 0xffa871e6, 0xff47d09c, 0xfffdd451};
    private int[] colorFocusedArray = {0x994db3ff, 0x99ff9a54, 0x99a871e6, 0x9947d09c, 0x99fdd451};
    private int[] widthAry = {45, 50, 44, 41, 48};
    private int[] heightAry = {45, 45, 40, 35, 48};

    private ArrayList<GridItem> homeData = new ArrayList<>();
    private GridView home_grid;
    private HomeGridViewAdapter gridViewAdapter;
    private long clickTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setToolBarTitle(getResources().getString(R.string.settings));
        initView();
    }

    private void initView() {
        initViewData();
        home_grid = (GridView) findViewById(R.id.gird);
        gridViewAdapter = new HomeGridViewAdapter(this, R.layout.home_grid_item, homeData, 3);
        gridViewAdapter.setLogoHeightAry(heightAry);
        gridViewAdapter.setLogoWidthAry(widthAry);
        home_grid.setAdapter(gridViewAdapter);
        home_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                switch (i) {
                    case 0:
                        intent = new Intent(SettingsActivity.this, PrinterSettingActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(SettingsActivity.this, VolumeSettingActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(SettingsActivity.this, DeviceInfoActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        checkUpdate();
                        break;
                }
            }
        });

    }

    private void initViewData() {
        for (int i = 0; i < itemNameArray.length; i++) {
            GridItem item = new GridItem();
            item.setNormalColor(colorNormalArray[i]);
            item.setFocusedColor(colorFocusedArray[i]);
            item.setImgRes(imageResArray[i]);
            item.setName(itemNameArray[i]);
            homeData.add(item);
        }
    }

    private void checkUpdate() {
        if (SystemClock.uptimeMillis() - clickTime < 1500) return;
        clickTime = SystemClock.uptimeMillis();
        UpdateManager updateManager = new UpdateManager(this, HttpConfig.APP_ID, UpdateManager.UPDATE_DIALOG);
        updateManager.requestUpdate();
        updateManager.setOnTaskFinishListener(new OnTaskFinishListener() {
            @Override
            public void onTaskResult(boolean result) {
                if (!result) {
                    ToastUtil.getInstance().showToast("当前已是最新版本");
                }
            }
        });
    }

}