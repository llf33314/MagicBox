package com.gt.magicbox.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.bean.StaffBean;
import com.gt.magicbox.bean.UnpaidOrderBean;
import com.gt.magicbox.coupon.CouponChoseActivity;
import com.gt.magicbox.coupon.DistributeCouponActivity;
import com.gt.magicbox.exchange.ExchangeWorkActivity;
import com.gt.magicbox.exchange.ShiftExchangeActivity;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.DialogTransformer;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.member.MemberChooseActivity;
import com.gt.magicbox.order.OrderListActivity;
import com.gt.magicbox.pay.PaymentActivity;
import com.gt.magicbox.pay.QRCodePayActivity;
import com.gt.magicbox.setting.printersetting.PrinterConnectService;
import com.gt.magicbox.setting.wificonnention.WifiConnectionActivity;
import com.gt.magicbox.update.UpdateManager;
import com.gt.magicbox.utils.NetworkUtils;
import com.gt.magicbox.utils.RxBus;
import com.gt.magicbox.utils.commonutil.PhoneUtils;
import com.gt.magicbox.utils.commonutil.ScreenUtils;
import com.gt.magicbox.utils.commonutil.ToastUtil;
import com.gt.magicbox.webview.WebViewActivity;
import com.gt.magicbox.widget.HintDismissDialog;
import com.orhanobut.hawk.Hawk;
import com.service.OrderPushService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import io.reactivex.functions.Consumer;

import static com.gt.magicbox.exchange.ExchangeWorkActivity.STAFF;

public class MainActivity extends BaseActivity {
    private String TAG = "MainActivity";
    private String[] itemNameArray = {"收银", "订单", "会员", "优惠券", "交班", "更多"};
    private Integer[] imageResArray = {R.drawable.home_payment, R.drawable.home_order,
            R.drawable.home_member, R.drawable.home_card_verification, R.drawable.home_shift_exchange, R.drawable.home_more};
    private int[] colorNormalArray = {0xfffdd451, 0xffb177f2, 0xffff9a54, 0xff47d09c, 0xfffc7473, 0xff4db3ff};
    private int[] colorFocusedArray = {0x99fdd451, 0x99b177f2, 0x99ff9a54, 0x9947d09c, 0x99fc7473, 0x994db3ff};
    private ArrayList<GridItem> homeData = new ArrayList<>();
    private GridView home_grid;
    private HomeGridViewAdapter gridViewAdapter;
    private MoreFunctionDialog mMoreFunctionDialog;
    private MoreFunctionDialog networkDialog;
    private Intent intent;

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
        goneBack();
        initView();
        //bindOrderService();
        requestUpdate();

        ScreenUtils.setScreenBrightness(MainActivity.this,255);
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
        gridViewAdapter = new HomeGridViewAdapter(this, R.layout.home_grid_item, homeData,3);
        home_grid.setAdapter(gridViewAdapter);
        home_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 2:
                        intent =new Intent(MainActivity.this,MemberChooseActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent =new Intent(MainActivity.this,CouponChoseActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        HttpCall.getApiService()
                                .getStaffInfoFromShopId(1,100,(int)Hawk.get("shopId"))
                                .compose(ResultTransformer.<StaffBean>transformer())
                                .compose(new DialogTransformer().<StaffBean>transformer())
                                .subscribe(new BaseObserver<StaffBean>() {
                                    @Override
                                    protected void onSuccess(StaffBean staffBean) {

                                        if (staffBean.getCount()<=0){//没有员工
                                            new HintDismissDialog(MainActivity.this,"您还没创建自己的员工\n请先登录多粉后台进行创建")
                                                    .setOnCancelClickListener(null)
                                                    .setCancelText("确认")
                                                    .show();
                                        }else{

                                            if ((Integer)Hawk.get("shiftId")!=0){
                                                intent = new Intent(MainActivity.this, ShiftExchangeActivity.class);
                                            }else {
                                                intent = new Intent(MainActivity.this, ExchangeWorkActivity.class);
                                                intent.putExtra(STAFF,staffBean);
                                            }
                                            startActivity(intent);
                                        }
                                    }
                                });
                        break;
                    case 0:
                        intent = new Intent(MainActivity.this, PaymentActivity.class);
                        intent.putExtra("type",0);
                        startActivity(intent);
                        break;
                    case 1:
                        if (NetworkUtils.isConnected()) {
                            intent=new Intent(MainActivity.this, OrderListActivity.class);
                            startActivity(intent);
                        }else {
                            if (networkDialog==null){
                                networkDialog=new MoreFunctionDialog(MainActivity.this,"没有网络，请连接后重试",R.style.HttpRequestDialogStyle);
                                networkDialog.getConfirmButton().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        networkDialog.dismiss();
                                        intent=new Intent(MainActivity.this,WifiConnectionActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                            networkDialog.show();
                        }
                        break;
                    case 5:
                        intent = new Intent(MainActivity.this, MoreActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
       // new ShortcutMenuDialog(this,R.style.ShortcutMenuDialog).show();
//        RxBus.get().toObservable(UnpaidOrderBean.class).subscribe(new Consumer<UnpaidOrderBean>() {
//            @Override
//            public void accept(@io.reactivex.annotations.NonNull UnpaidOrderBean unpaidOrderBean) throws Exception {
//                if (unpaidOrderBean.count==-1){//没有获取网络数据 用于多出发送UnpaidOrderBean消息
//                    getUnpaidOrderCount();
//                }else{
//                    updateUnpaid(unpaidOrderBean);
//                }
//
//            }
//        });
//
//
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                getUnpaidOrderCount();
//            }
//        });


        portIntent=new Intent(this, PrinterConnectService.class);
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
                .getUnpaidOrderCount(PhoneUtils.getIMEI(), (String) Hawk.get("token"))
                .compose(ResultTransformer.<UnpaidOrderBean>transformer())
                .subscribe(new BaseObserver<UnpaidOrderBean>() {
                    @Override
                    public void onSuccess(UnpaidOrderBean data) {
                        Log.d(TAG,"UnpaidOrderBean onSuccess");
                        updateUnpaid(data);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        Log.d(TAG,"onFailure code="+code+"  msg="+msg);
                        super.onFailure(code, msg);
                    }
                });
    }

    @Override
    protected void onResume() {
      //  getUnpaidOrderCount();
       // Hawk.put("shiftId",0);
        Log.d(TAG,"onResume  shopId="+ Hawk.get("shopId")+" eqId="+Hawk.get("eqId")+"  shiftId="+Hawk.get("shiftId"));
        super.onResume();
    }
    private void requestUpdate() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = sDateFormat.format(new java.util.Date());
        String keyName="update-"+date;
        boolean onceDayRequestUpdate = Hawk.get(keyName, false);
        if (!onceDayRequestUpdate) {
            UpdateManager updateManager = new UpdateManager(this, "MagicBox");
            updateManager.requestUpdate();
            Hawk.put(keyName, true);
        }
    }
}
