package com.gt.magicbox.extension;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.extension.fixed_money.FixedMoneyNoDataActivity;
import com.gt.magicbox.extension.fixed_money.FixedMoneySettingActivity;
import com.gt.magicbox.main.GridItem;
import com.gt.magicbox.main.HomeGridViewAdapter;

import java.util.ArrayList;

/**
 * Description:
 *
 * @author jack-lin
 * @date 2018/1/24 0024
 * Buddha bless, never BUG!
 */

public class ExtensionActivity extends BaseActivity {
    private String[] itemNameArray = {"固定金额"};
    private Integer[] imageResArray = {R.drawable.entension_fixed_money};
    private int[] colorNormalArray = {0xfffc7473};
    private int[] colorFocusedArray = {0x99fc7473};
    private int[] widthAry = {46};
    private int[] heightAry = {46};

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
                        intent = new Intent(ExtensionActivity.this, FixedMoneyNoDataActivity.class);
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
