package com.gt.magicbox.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;
import com.gt.magicbox.Constant;
import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.BaseConstant;
import com.gt.magicbox.base.MyApplication;
import com.gt.magicbox.bean.OrderPushBean;
import com.gt.magicbox.bean.StaffBean;
import com.gt.magicbox.bean.UnpaidOrderBean;
import com.gt.magicbox.bean.UpdateMainBadgeBean;
import com.gt.magicbox.exchange.ExchangeWorkActivity;
import com.gt.magicbox.exchange.ShiftExchangeActivity;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.DialogTransformer;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.member.MemberChooseActivity;
import com.gt.magicbox.order.OrderListActivity;
import com.gt.magicbox.pay.ChosePayModeActivity;
import com.gt.magicbox.pay.PaymentActivity;
import com.gt.magicbox.pay.QRCodePayActivity;
import com.gt.magicbox.setting.printersetting.PrinterConnectService;
import com.gt.magicbox.setting.wificonnention.WifiConnectionActivity;
import com.gt.magicbox.update.UpdateManager;
import com.gt.magicbox.utils.NetworkUtils;
import com.gt.magicbox.utils.RxBus;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.gt.magicbox.utils.commonutil.PhoneUtils;
import com.gt.magicbox.utils.commonutil.ScreenUtils;
import com.gt.magicbox.widget.HintDismissDialog;
import com.orhanobut.hawk.Hawk;
import com.service.CustomerDisplayService;
import com.service.OrderPushService;

import org.json.JSONException;
import org.json.JSONObject;

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
                    LogUtils.i("grid","msg.arg1="+msg.arg1+"  msg.arg2="+msg.arg2);

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
        bindOrderService();
        bindCustomerDisplayService();
        //requestUpdate();

        if (Constant.product.equals(BaseConstant.PRODUCTS[0])){
            ScreenUtils.setScreenBrightness(MainActivity.this,255);
        }
    }

    /**
     * 与服务器建立socket连接，监听订单的推送
     */
    private void bindOrderService(){
        Intent intent=new Intent(MainActivity.this, OrderPushService.class);
        startService(intent);
    }
    private void bindCustomerDisplayService(){
        if (Hawk.get("hadMatchCustomerDisplay",true)){
            Intent intent=new Intent(MainActivity.this, CustomerDisplayService.class);
            startService(intent);
        }
    }
    private void initView() {
        requestUpdate();
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
                        showMoreDialog();
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
                                            Integer shiftId=Hawk.get("shiftId");
                                            if (shiftId==null)shiftId=0;
                                            if (shiftId!=0){
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
                       // handlerOrderData();
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


        RxBus.get().toObservable(UpdateMainBadgeBean.class).subscribe(new Consumer<UpdateMainBadgeBean>() {
            @Override
            public void accept(UpdateMainBadgeBean updateMainBadgeBean) throws Exception {
                if (gridViewAdapter!=null){
                    if (updateMainBadgeBean.getPosition()<0|| updateMainBadgeBean.getPosition()>gridViewAdapter.getCount()-1){
                        return;
                    }
                    gridViewAdapter.updateBadge(updateMainBadgeBean);
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

            //App更新右上角上标提示
            if (i==5&&MyApplication.isNeedUpdateApp()){
                item.setMessageCount(1);
            }

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
                .getUnpaidOrderCount(PhoneUtils.getIMEI())
                .compose(ResultTransformer.<UnpaidOrderBean>transformer())
                .subscribe(new BaseObserver<UnpaidOrderBean>() {
                    @Override
                    public void onSuccess(UnpaidOrderBean data) {
                        LogUtils.d(TAG,"UnpaidOrderBean onSuccess");
                        updateUnpaid(data);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        LogUtils.d(TAG,"onFailure code="+code+"  msg="+msg);
                        super.onFailure(code, msg);
                    }
                });
    }

    @Override
    protected void onResume() {
       getUnpaidOrderCount();
        LogUtils.d(TAG,"onResume  shopId="+ Hawk.get("shopId")+" eqId="+Hawk.get("eqId")+"  shiftId="+Hawk.get("shiftId")
        + "  product="+Constant.product);
        super.onResume();
    }
    private void requestUpdate() {
        if (Constant.product.equals(BaseConstant.PRODUCTS[1]))return;
        boolean isShowUpdateDialog=Hawk.get("UPDATE_DIALOG-"+ Hawk.get("newestVersion",""),true);
        if (isShowUpdateDialog) {
            UpdateManager updateManager = new UpdateManager(this, "MagicBox",UpdateManager.UPDATE_BADGE_AND_DIALOG);
            updateManager.requestUpdate();
        }else {
            UpdateManager updateManager = new UpdateManager(this, "MagicBox",UpdateManager.UPDATE_BADGE);
            updateManager.requestUpdate();
        }
    }
    private void handlerOrderData(){
        //String retData= "{\"busId\":36,\"businessUtilName\":\"shop.deeptel.com.cn/shops/web/cashier/CF946E2B/payCallBack?id=ff8080815f629703015f6abdceaa006c\",\"eqCode\":\"865067034465453\",\"model\":53,\"money\":0.01,\"orderId\":1069,\"orderNo\":\"YD1509324344993\",\"pay_type\":0,\"status\":\"success\",\"time\":\"2017-10-30 08:45:47\",\"type\":1}";
        String retData=getResources().getString(R.string.order_push_data);
        if (!TextUtils.isEmpty(retData) && retData.startsWith("\"") && retData.endsWith("\"")) {
            retData = retData.trim().substring(1, retData.length() - 1);
        }
            LogUtils.d(TAG, "socketEvent retData="+retData);
            OrderPushBean orderPushBean=new Gson().fromJson(retData,OrderPushBean.class);
            //JSONObject orderObject= new JSONObject(retData);
            if (orderPushBean!=null) {
                int orderId = orderPushBean.getOrderId();
                double money =  orderPushBean.getMoney();
                String orderNo=orderPushBean.getOrderNo();
                LogUtils.d(TAG," money="+money);
                if (Constant.product.equals(BaseConstant.PRODUCTS[0])){
                    Intent intent = new Intent(getApplicationContext(), QRCodePayActivity.class);
                    intent.putExtra("type", QRCodePayActivity.TYPE_SERVER_PUSH);
                    intent.putExtra("orderId",orderId);
                    intent.putExtra("money", money);
                    intent.putExtra("orderNo",orderNo);
                    startActivity(intent);
                }else if (Constant.product.equals(BaseConstant.PRODUCTS[1])){
                    Intent intent = new Intent(getApplicationContext(), ChosePayModeActivity.class);
                    intent.putExtra("customerType", ChosePayModeActivity.TYPE_ORDER_PUSH);
                    intent.putExtra("orderNo",orderNo);
                    intent.putExtra("money", money);
                    startActivity(intent);
                }

            }
    }
}
