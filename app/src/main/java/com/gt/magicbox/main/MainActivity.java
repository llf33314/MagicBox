package com.gt.magicbox.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.bean.UnpaidOrderBean;
import com.gt.magicbox.http.BaseObserver;
import com.gt.magicbox.http.BaseResponse;
import com.gt.magicbox.http.HttpCall;
import com.gt.magicbox.http.RxObservableUtils;
import com.gt.magicbox.pay.PaymentActivity;
import com.gt.magicbox.setting.printersetting.PrinterConnectSerivce;
import com.gt.magicbox.utils.RxBus;
import com.gt.magicbox.utils.SimpleObserver;
import com.gt.magicbox.utils.commonutil.PhoneUtils;
import com.gt.magicbox.utils.commonutil.SPUtils;
import com.gt.magicbox.webview.WebViewActivity;
import com.service.OrderPushService;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;

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
    private final int MSG_UPDATE_UI=0;

    //打印机连接
    public static Intent portIntent;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_UPDATE_UI:
                    Log.i("grid","msg.arg1="+msg.arg1+"  msg.arg2="+msg.arg2);

                    homeData.get(msg.arg1).setMessageCount(msg.arg2);
                    gridViewAdapter.setGridData(homeData);
                    break;
            }
        }
    };
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
        RxBus.get().toObservable(UnpaidOrderBean.class).subscribe(new SimpleObserver<UnpaidOrderBean>(new Consumer<UnpaidOrderBean>() {
            @Override
            public void accept(@NonNull UnpaidOrderBean unpaidOrderBean) throws Exception {
                Log.i("grid","unpaidOrderBean.count="+unpaidOrderBean.count);
                updateUnpaid(unpaidOrderBean);
            }
        }));
        handler.post(new Runnable() {
            @Override
            public void run() {
                getUnpaidOrderCount();
            }
        });


        portIntent=new Intent(this, PrinterConnectSerivce.class);
        startService(portIntent);
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
    private void updateUnpaid(UnpaidOrderBean unpaidOrderBean){
        Message msg=new Message();
        msg.what=MSG_UPDATE_UI;
        msg.arg1=1;
        msg.arg2=unpaidOrderBean.count;
        handler.sendMessage(msg);
    }
    private void showMoreDialog(){
        if (mMoreFunctionDialog==null){
            mMoreFunctionDialog=new MoreFunctionDialog(this,R.style.HttpRequestDialogStyle);
        }
        mMoreFunctionDialog.show();
    }
    private void getUnpaidOrderCount(){
        HttpCall.getApiService()
                .getUnpaidOrderCount(PhoneUtils.getIMEI(), SPUtils.getInstance().getString("token"))
                .compose(RxObservableUtils.<BaseResponse<UnpaidOrderBean>>applySchedulers())
                .subscribe(new BaseObserver<UnpaidOrderBean>(getApplicationContext(),false) {
                    @Override
                    public void onSuccess(UnpaidOrderBean data) {
                        Log.i(TAG,"UnpaidOrderBean onSuccess");
                        updateUnpaid(data);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        Log.i(TAG,"onFailure code="+code+"  msg="+msg);
                        super.onFailure(code, msg);
                    }
                });
    }

}
