package com.gt.magicbox.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.pay.PaymentActivity;
import com.gt.magicbox.webview.WebViewActivity;
import com.service.OrderPushService;

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
    private MoreFunctionDialog mMoreFunctionDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolBarTitle("主页");
        initView();
        bindOrderService();
    }

    /**
     * 与服务器建立socket连接，监听订单的推送
     */
    private void bindOrderService(){
        Intent intent=new Intent(MainActivity.this, OrderPushService.class);
        startService(intent);
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
                    case 3:
                    case 4:
                        showMoreDialog();
                        break;
                    case 0:
                        intent = new Intent(MainActivity.this, PaymentActivity.class);
                        intent.putExtra("type",0);
                        startActivity(intent);
                        break;
                    case 1:
                         intent=new Intent(MainActivity.this, WebViewActivity.class);
                        intent.putExtra("webType",WebViewActivity.WEB_TYPE_ORDER);
                        intent.putExtra("status",0);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(MainActivity.this, MoreActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
       // new ShortcutMenuDialog(this,R.style.ShortcutMenuDialog).show();
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

    private void showMoreDialog(){
        if (mMoreFunctionDialog==null){
            mMoreFunctionDialog=new MoreFunctionDialog(this,R.style.HttpRequestDialogStyle);
        }
        mMoreFunctionDialog.show();
    }


}
