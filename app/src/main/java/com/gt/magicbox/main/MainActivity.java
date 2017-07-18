package com.gt.magicbox.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.bean.LoginBean;
import com.gt.magicbox.http.BaseObserver;
import com.gt.magicbox.http.BaseResponse;
import com.gt.magicbox.http.HttpCall;
import com.gt.magicbox.http.RxObservableUtils;
import com.gt.magicbox.pay.PaymentActivity;
import com.gt.magicbox.utils.commonutil.Utils;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private String TAG = "MainActivity";
    private String[] itemNameArray = {"收银", "订单", "会员", "卡券核销", "派券", "更多"};
    private Integer[] imageResArray = {R.drawable.home_payment, R.drawable.home_order,
            R.drawable.home_member, R.drawable.home_card_verification, R.drawable.home_send_coupon, R.drawable.home_more};
    private int[] colorNormalArray = {0xfffdd451, 0xffb177f2, 0xffff9a54, 0xff47d09c, 0xfffc7473, 0xff4db3ff};
    private int[] colorFocusedArray = {0x99fdd451, 0x99b177f2, 0x99ff9a54, 0x9947d09c, 0x99fc7473, 0x994db3ff};
    private ArrayList<GridItem> homeData = new ArrayList<>();
    private GridView home_grid;
    private HomeGridViewAdapter gridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolBarTitle("主页");
        Utils.init(this);
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
                        intent = new Intent(MainActivity.this, PaymentActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(MainActivity.this, MoreActivity.class);
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

    public void request(View view) {
        HttpCall.getApiService()
                .userLogin("123456", "jindao", "gt123456")
                .compose(RxObservableUtils.<BaseResponse<LoginBean>>applySchedulers())//线程处理
                .compose(MainActivity.this.<BaseResponse<LoginBean>>bindToLifecycle())//内存泄漏处理
                .subscribe(new BaseObserver<LoginBean>(MainActivity.this, true) {
                    @Override
                    public void onSuccess(LoginBean data) {
                        Log.i(TAG, "data=" + data.toString());
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        super.onFailure(code, msg);
                        Log.i(TAG, "code=" + code + "  msg=" + msg);

                    }
                });
    }
}
