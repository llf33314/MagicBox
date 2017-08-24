package com.gt.magicbox.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.http.HttpConfig;
import com.gt.magicbox.setting.printersetting.PrinterSettingActivity;
import com.gt.magicbox.setting.wificonnention.WifiConnectionActivity;
import com.gt.magicbox.update.OnTaskFinishListener;
import com.gt.magicbox.update.UpdateManager;
import com.gt.magicbox.utils.commonutil.ToastUtil;

import java.util.ArrayList;

/**
 * Description:
 * Created by jack-lin on 2017/7/18 0018.
 */

public class MoreActivity extends BaseActivity {
    private String[] itemNameArray = {"打印设置", "网络设置", "设备管理","版本更新"};
    private Integer[] imageResArray = {R.drawable.more_printer_setting, R.drawable.more_network_setting,
            R.drawable.more_devices_setting,R.drawable.icon_update};
    private int[] colorNormalArray = {0xff4db3ff, 0xff47d09c, 0xffff9a54,0xfffdd451};
    private int[] colorFocusedArray = {0x994db3ff, 0x9947d09c, 0x99ff9a54,0x99fdd451};
    private ArrayList<GridItem> homeData = new ArrayList<>();
    private GridView home_grid;
    private HomeGridViewAdapter gridViewAdapter;
    private long clickTime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setToolBarTitle("更多应用");
        initView();
    }

    private void initView() {
        initViewData();
        home_grid = (GridView) findViewById(R.id.gird);
        gridViewAdapter = new HomeGridViewAdapter(this, R.layout.home_grid_item, homeData);
        home_grid.setAdapter(gridViewAdapter);
        home_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                switch (i) {
                    case 0:
                        intent=new Intent(MoreActivity.this,PrinterSettingActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent=new Intent(MoreActivity.this,WifiConnectionActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent=new Intent(MoreActivity.this,DevicesMangerActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        checkUpdate();
                        break;
                }
            }
        });
    }
    private void checkUpdate(){
        if (SystemClock.uptimeMillis()-clickTime<1500)return;
            clickTime= SystemClock.uptimeMillis();
        UpdateManager updateManager=new UpdateManager(this, HttpConfig.APP_ID);
        updateManager.requestUpdate();
        updateManager.setOnTaskFinishListener(new OnTaskFinishListener() {
            @Override
            public void onTaskResult(boolean result) {
                if (!result){
                    ToastUtil.getInstance().showToast("当前已是最新版本");
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
}
