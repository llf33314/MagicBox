package com.gt.magicbox.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.setting.DeviceInfoActivity;
import com.gt.magicbox.setting.VolumeSettingActivity;

import java.util.ArrayList;

/**
 * Description:
 * Created by jack-lin on 2017/7/18 0018.
 */

public class DevicesMangerActivity extends BaseActivity {
    private String[] itemNameArray = {"音量设置","设备状态"};
    private Integer[] imageResArray = {R.drawable.devices_volume_setting, R.drawable.devices_status};
    private int[] colorNormalArray = {0xffff9a54, 0xff4db3ff};
    private int[] colorFocusedArray = {0x99ff9a54, 0x994db3ff};
    private ArrayList<GridItem> homeData = new ArrayList<>();
    private GridView home_grid;
    private HomeGridViewAdapter gridViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setToolBarTitle("设备管理");
        initView();
    }

    private void initView() {
        initViewData();
        home_grid = (GridView) findViewById(R.id.gird);
        gridViewAdapter = new HomeGridViewAdapter(this, R.layout.home_grid_item, homeData,3);
        home_grid.setAdapter(gridViewAdapter);
        home_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                switch (i) {
                    case 0:
                        intent=new Intent(DevicesMangerActivity.this, VolumeSettingActivity.class);
                        startActivity(intent);
                            break;
                    case 1:
                        intent=new Intent(DevicesMangerActivity.this, DeviceInfoActivity.class);
                        startActivity(intent);
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
}
