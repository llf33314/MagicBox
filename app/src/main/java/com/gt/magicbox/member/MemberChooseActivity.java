package com.gt.magicbox.member;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.gt.magicbox.Constant;
import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.BaseConstant;
import com.gt.magicbox.bean.MemberRechargeHistoryBean;
import com.gt.magicbox.coupon.CouponChoseActivity;
import com.gt.magicbox.main.GridItem;
import com.gt.magicbox.main.HomeGridViewAdapter;
import com.gt.magicbox.pay.PaymentActivity;
import com.gt.magicbox.setting.DeviceInfoActivity;
import com.gt.magicbox.setting.VolumeSettingActivity;

import java.util.ArrayList;

/**
 * Description:
 * Created by jack-lin on 2017/7/18 0018.
 */

public class MemberChooseActivity extends BaseActivity {
    private String[] itemNameArray = {"会员充值", "新增会员"};
    private Integer[] imageResArray = {R.drawable.member_recharge,R.drawable.member_add};
    private int[] colorNormalArray = {0xfffdd451, 0xfffc7473};
    private int[] colorFocusedArray = {0x99fdd451, 0x99fc7473};
    private ArrayList<GridItem> homeData = new ArrayList<>();
    private ListView home_grid;
    private HomeGridViewAdapter gridViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_member_chose);
        setToolBarTitle("会员");
        initView();
    }

    private void initView() {
        initViewData();
        home_grid = (ListView) findViewById(R.id.listView);
        gridViewAdapter = new HomeGridViewAdapter(this, R.layout.home_grid_item, homeData, 2);
        home_grid.setAdapter(gridViewAdapter);
        gridViewAdapter.setLogoSize((int)getResources().getDimension(R.dimen.dp_50),(int)getResources().getDimension(R.dimen.dp_50));
        home_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                switch (i) {
                    case 0:
                        if (Constant.product == BaseConstant.PRODUCTS[0]) {
                            intent = new Intent(MemberChooseActivity.this, PaymentActivity.class);
                            intent.putExtra("type", 4);
                            startActivity(intent);
                        } else if (Constant.product == BaseConstant.PRODUCTS[1]) {
                            intent = new Intent(MemberChooseActivity.this, VerificationChoseActivity.class);
                            intent.putExtra("type", 4);
                            startActivity(intent);
                        }
                        break;
                    case 1:
                        intent = new Intent(MemberChooseActivity.this, AddMemberActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
//                        intent = new Intent(MemberChooseActivity.this, AddMemberActivity.class);
//                        startActivity(intent);
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
