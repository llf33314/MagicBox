package com.gt.magicbox.member;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.bean.CouponVerificationBean;
import com.gt.magicbox.bean.MemberCardBean;
import com.gt.magicbox.coupon.CouponVerificationSuccess;
import com.gt.magicbox.coupon.VerificationActivity;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.DialogTransformer;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.main.GridItem;
import com.gt.magicbox.main.HomeGridViewAdapter;
import com.gt.magicbox.main.MoreFunctionDialog;
import com.gt.magicbox.pay.PaymentActivity;
import com.gt.magicbox.utils.commonutil.AppManager;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.gt.magicbox.utils.commonutil.ToastUtil;
import com.gt.magicbox.widget.HintDismissDialog;
import com.orhanobut.hawk.Hawk;
import com.ums.upos.sdk.exception.CallServiceException;
import com.ums.upos.sdk.exception.SdkException;
import com.ums.upos.sdk.scanner.OnScanListener;
import com.ums.upos.sdk.scanner.ScannerConfig;
import com.ums.upos.sdk.scanner.ScannerManager;
import com.ums.upos.sdk.system.BaseSystemManager;
import com.ums.upos.sdk.system.OnServiceStatusListener;

import java.util.ArrayList;

import static com.gt.magicbox.pay.PaymentActivity.TYPE_COUPON_VERIFICATION;
import static com.gt.magicbox.pay.PaymentActivity.TYPE_MEMBER_PAY;
import static com.gt.magicbox.pay.PaymentActivity.TYPE_MEMBER_RECHARGE;

/**
 * Description:
 * 用于POS机
 * Created by jack-lin on 2017/7/18 0018.
 */

public class VerificationChoseActivity extends BaseActivity {
    public static final String TAG=VerificationChoseActivity.class.getSimpleName();
    private String[] itemNameArray = {"扫码识别", "卡号识别"};
    private Integer[] imageResArray = {R.drawable.pos_scan, R.drawable.pos_input};
    private int[] colorNormalArray = {0xfffdd451, 0xfffc7473};
    private int[] colorFocusedArray = {0x99fdd451, 0x99fc7473};
    private ArrayList<GridItem> homeData = new ArrayList<>();
    private ListView home_grid;
    private HomeGridViewAdapter gridViewAdapter;
    private double orderMoney;
    private ScannerManager scannerManager;
    private MoreFunctionDialog dialog;
    private int type;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_member_chose);
        setToolBarTitle("识别会员");
        orderMoney=this.getIntent().getDoubleExtra("orderMoney",0);
        type=this.getIntent().getIntExtra("type",0);
        initView();
    }

    private void initView() {
        initViewData();
        home_grid = (ListView) findViewById(R.id.listView);
        gridViewAdapter = new HomeGridViewAdapter(this, R.layout.home_grid_item, homeData, 2);
        home_grid.setAdapter(gridViewAdapter);
        home_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                switch (i) {
                    case 0:
                        loginDevicesAndScan();
                        break;
                    case 1:
                        if (type==TYPE_MEMBER_RECHARGE){
                            intent=new Intent(VerificationChoseActivity.this,PaymentActivity.class);
                            intent.putExtra("type", TYPE_MEMBER_RECHARGE);
                            startActivity(intent);
                        }else if (type==TYPE_MEMBER_PAY){
                            intent=new Intent(VerificationChoseActivity.this,PaymentActivity.class);
                            intent.putExtra("type", TYPE_MEMBER_PAY);
                            intent.putExtra("orderMoney",orderMoney);
                            startActivity(intent);
                        }else if (type==TYPE_COUPON_VERIFICATION){
                            intent=new Intent(VerificationChoseActivity.this,PaymentActivity.class);
                            intent.putExtra("type", TYPE_COUPON_VERIFICATION);
                            startActivity(intent);
                        }
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
    private void loginDevicesAndScan(){
        try {
            BaseSystemManager.getInstance().deviceServiceLogin(
                    VerificationChoseActivity.this, null, "99999998",
                    new OnServiceStatusListener() {
                        @Override
                        public void onStatus(int arg0) {
                            if (0 == arg0 || 2 == arg0 || 100 == arg0) {
                                startPosScan();
                            }
                        }
                    });
        } catch (SdkException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void startPosScan(){
        scannerManager = new ScannerManager();
        Bundle bundle = new Bundle();
        bundle.putInt(ScannerConfig.COMM_SCANNER_TYPE, 1);//1是前置摄像头
        bundle.putBoolean(ScannerConfig.COMM_ISCONTINUOUS_SCAN, false);
        try {
            scannerManager.stopScan();
            scannerManager.initScanner(bundle);
            scannerManager.startScan(30000, new OnScanListener() {
                @Override
                public void onScanResult(int i, byte[] bytes) {
                    //防止用户未扫描直接返回，导致bytes为空
                    if (bytes != null && !bytes.equals("")) {
                        //ToastUtil.getInstance().showToast(""+new String(bytes));
                        if (type==TYPE_MEMBER_PAY||type==TYPE_MEMBER_RECHARGE) {
                            findMemberCardByPhone(new String(bytes));
                        }else if (type==TYPE_COUPON_VERIFICATION){
                                String start = "code=";
                                String result = new String(bytes);
                                if (result.contains(start)) {
                                    String number = result.substring((result.indexOf(start) + start.length()), result.length());
                                    LogUtils.d("scanResult", "number=" + number);
                                    couponVerification(number);
                                } else {
                                    couponVerification(result);
                                }
                        }
                    }
                    logoutDevices();
                }
            });

        } catch (SdkException e) {
            e.printStackTrace();
        } catch (CallServiceException e) {
            e.printStackTrace();
        }
    }
    private void logoutDevices() {
        try {
            BaseSystemManager.getInstance().deviceServiceLogout();
        } catch (SdkException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void findMemberCardByPhone(final String numberString) {
        if (TextUtils.isEmpty(numberString)) {
            ToastUtil.getInstance().showToast("输入不能为空");
            return;
        }
        HttpCall.getApiService()
                .findMemberCardByPhone( Hawk.get("busId",0), numberString)
                .compose(ResultTransformer.<MemberCardBean>transformer())//线程处理 预处理
                .compose(new DialogTransformer().<MemberCardBean>transformer())
                .subscribe(new BaseObserver<MemberCardBean>() {

                    @Override
                    protected void onSuccess(MemberCardBean bean) {
                        if (bean != null) {
                            LogUtils.d(TAG, "findMemberCardByPhone onSuccess");

                            if (bean.ctName.equals("折扣卡") && type == TYPE_MEMBER_RECHARGE) {
                                dialog = new MoreFunctionDialog(getApplicationContext(), "折扣卡不可以进行充值", R.style.HttpRequestDialogStyle);
                                dialog.show();
                            } else {
                                Intent intent = null;
                                if (type == TYPE_MEMBER_RECHARGE)
                                    intent = new Intent(getApplicationContext(), MemberRechargeActivity.class);
                                else if (type == TYPE_COUPON_VERIFICATION || type == TYPE_MEMBER_PAY) {
                                    intent = new Intent(getApplicationContext(), VerificationActivity.class);
                                    intent.putExtra("orderMoney", orderMoney);
                                }
                                intent.putExtra("MemberCardBean", bean);
                                startActivity(intent);
                                AppManager.getInstance().finishActivity();
                            }
                        }
                    }

                    @Override
                    protected void onFailure(int code, String msg) {
                        super.onFailure(code, msg);
                        LogUtils.d(TAG, "findMemberCardByPhone onFailure msg=" + msg.toString());
                        if (!TextUtils.isEmpty(msg) && msg.equals("数据不存在")) {
                            new HintDismissDialog(VerificationChoseActivity.this, "该卡号不存在")
                                    .setDialogOnDismissListener(new DialogInterface.OnDismissListener() {
                                        public void onDismiss(DialogInterface dialog) {
                                        }
                                    })
                                    .setCancelText("确认")
                                    .show();
                        }

                    }

                });
    }
    private void couponVerification(final String numberString) {
        if (!TextUtils.isEmpty(numberString)) {
            HttpCall.getApiService()
                    .verificationCoupon(numberString, Hawk.get("shopId", 0))
                    .compose(ResultTransformer.<CouponVerificationBean>transformer())
                    .compose(new DialogTransformer().<CouponVerificationBean>transformer())
                    .subscribe(new BaseObserver<CouponVerificationBean>() {
                        @Override
                        public void onSuccess(CouponVerificationBean data) {
                            LogUtils.d(TAG, "couponVerification onSuccess ");
                            if (data != null) {
                                Intent intent = new Intent(VerificationChoseActivity.this, CouponVerificationSuccess.class);
                                intent.putExtra("couponVerificationBean", data);
                                startActivity(intent);
                                AppManager.getInstance().finishActivity();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtils.d(TAG, "memberRecharge onError e" + e.getMessage());
                            new HintDismissDialog(VerificationChoseActivity.this, "核销失败:"+e.getMessage())
                                    .setDialogOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                        }
                                    })
                                    .setCancelText("确认")
                                    .show();
                            super.onError(e);
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            LogUtils.d(TAG, "memberRecharge onFailure msg=" + msg);
                            super.onFailure(code, msg);
                        }
                    });
        }
    }
}
