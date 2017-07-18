package com.gt.magicbox.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.wificonnention.WifiConnectionActivity;

import java.util.ArrayList;

/**
 * Description:
 * Created by jack-lin on 2017/7/18 0018.
 */

public class MoreActivity extends BaseActivity {
    private String[] itemNameArray = {"打印设置", "网络设置", "设备管理"};
    private Integer[] imageResArray = {R.drawable.more_printer_setting, R.drawable.more_network_setting,
            R.drawable.more_devices_setting};
    private int[] colorNormalArray = {0xfffdd451, 0xffb177f2, 0xffff9a54};
    private int[] colorFocusedArray = {0x99fdd451, 0x99b177f2, 0x99ff9a54};
    private ArrayList<GridItem> homeData = new ArrayList<>();
    private GridView home_grid;
    private HomeGridViewAdapter gridViewAdapter;

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
                    case 2:
                        intent=new Intent(MoreActivity.this,DevicesMangerActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent=new Intent(MoreActivity.this,WifiConnectionActivity.class);
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
