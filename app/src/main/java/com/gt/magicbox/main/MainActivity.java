package com.gt.magicbox.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gt.magicbox.Constant;
import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.BaseConstant;
import com.gt.magicbox.base.MyApplication;
import com.gt.magicbox.bean.StaffBean;
import com.gt.magicbox.bean.UnpaidOrderBean;
import com.gt.magicbox.bean.UpdateMainBadgeBean;
import com.gt.magicbox.coupon.CouponChoseActivity;
import com.gt.magicbox.exchange.ExchangeWorkActivity;
import com.gt.magicbox.exchange.ShiftExchangeActivity;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.DialogTransformer;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.member.MemberChooseActivity;
import com.gt.magicbox.order.OrderListActivity;
import com.gt.magicbox.pay.PaymentActivity;
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
import com.synodata.codelib.decoder.CodeID;
import com.synodata.codelib.decoder.CodeUtils;
import com.synodata.codelib.decoder.CodeUtils$IActivateListener;

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
    private int[] mBarcodeList = {CodeID.CODEEAN13,CodeID.CODEEAN8,CodeID.CODE128,CodeID.CODE93,CodeID.QR};

    private ArrayList<GridItem> homeData = new ArrayList<>();
    private GridView home_grid;
    private HomeGridViewAdapter gridViewAdapter;
    private MoreFunctionDialog mMoreFunctionDialog;
    private MoreFunctionDialog networkDialog;
    private Intent intent;

    private final int MSG_UPDATE_UI=0;
    private static final int MSG_ACTIVE = 5;
    private static final int BUZZER_ON=11;
    private static final int BUZZER_OFF=22;
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
                case MSG_ACTIVE:
                    LogUtils.d("quck",(String)msg.obj);
                    break;

                case BUZZER_OFF:
                    //	HdxUtil.EnableBuzze(0);
                    LogUtils.d("quck","BUZZER_OFF1");
                case BUZZER_ON:
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
        if (Hawk.get("hadMatchCustomerDisplay",false)){
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
                        //showMoreDialog();
                        intent = new Intent(MainActivity.this,CouponChoseActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        if (NetworkUtils.isConnected()) {
                            getStaffInfoFromShopId();
                        }else {
                            showNetworkDisconnect();
                        }
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
                          showNetworkDisconnect();
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
        RxBus.get().toObservable(UnpaidOrderBean.class).subscribe(new Consumer<UnpaidOrderBean>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull UnpaidOrderBean unpaidOrderBean) throws Exception {
                if (unpaidOrderBean.count==-1){//没有获取网络数据 用于多出发送UnpaidOrderBean消息
                    getUnpaidOrderCount();
                }else{
                    updateUnpaid(unpaidOrderBean);
                }

            }
        });




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
    private void showNetworkDisconnect(){
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
    private void getUnpaidOrderCount(){
        HttpCall.getApiService()
                .getUnpaidOrderCount(Hawk.get("busId", 0),PhoneUtils.getIMEI())
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
    private void getStaffInfoFromShopId(){
        HttpCall.getApiService()
                .getStaffInfoFromShopId(1,100,Hawk.get("shopId",0))
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
                            Integer shiftId=Hawk.get("shiftId",0);
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
    }
    @Override
    protected void onResume() {
       getUnpaidOrderCount();
        LogUtils.d(TAG,"onResume  shopId="+ Hawk.get("shopId")+" eqId="+Hawk.get("eqId")+"  shiftId="+Hawk.get("shiftId")
        + "  product="+Constant.product);
        activeBarcode();
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
    //Active the Barcode before use it to scan and decode.
    //Decode can only return correct result when it's activated.

    private void activeBarcode()
    {
        CodeUtils mUtils = new CodeUtils(getApplicationContext());

        mUtils.tryActivateBarcode(new CodeUtils$IActivateListener(){
            //this function will be called during active process, and return the process messages
            @Override
            public void onActivateProcess(String msg) {
                // post the processing message
                LogUtils.d("quck","onActivateProcess ="+msg);

                handler.obtainMessage(MSG_ACTIVE, msg).sendToTarget();
            }
            // this function will be called after the active process.
            //result_code: CodeUtils.RESULT_SUCCESS means active success, others means fail
            // error: return the fail cause message.
            @Override
            public void onActivateResult(int result_code, String error) {
                // TODO Auto-generated method stub
                // post the result message
                LogUtils.d("quck","onActivateResult ="+result_code);

                handler.obtainMessage(MSG_ACTIVE, error).sendToTarget();
            }
            //Current Active state when calling active function.
            //if it's unactive state, this function will be returned when active process is done.
            @Override
            public void onActivateState(boolean bActivated) {
                // TODO Auto-generated method stub
                LogUtils.d("quck","onActivateState ="+bActivated);

                if(bActivated) // barcode is in activated state
                {
                    //config the barcode
                    configBarcode();
                    // show the jump button
                }
            }

        });
    }

    // config the supporting barcode types.
    //all the supported types are listed in CodeID.
    private void configBarcode()
    {
        CodeUtils mUtils = new CodeUtils(getApplicationContext());

        //clear old configs
        mUtils.enableAllFormats(false);
        //set new config, just enable the type in the list
		/*for(int i=0;i<mBarcodeList.length;i++)
			mUtils.enableCodeFormat(mBarcodeList[i]);
		*/
        //to set all the format enabled, open below code
        mUtils.enableAllFormats(true);

    }

}
