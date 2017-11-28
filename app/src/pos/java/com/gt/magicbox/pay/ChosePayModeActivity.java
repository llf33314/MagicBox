package com.gt.magicbox.pay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.bean.CashOrderBean;
import com.gt.magicbox.bean.MemberCardBean;
import com.gt.magicbox.bean.MemberCountMoneyBean;
import com.gt.magicbox.bean.PosRequestBean;
import com.gt.magicbox.bean.UpdateOrderListUIBean;
import com.gt.magicbox.coupon.VerificationActivity;
import com.gt.magicbox.http.BaseResponse;
import com.gt.magicbox.http.HttpRequestDialog;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.DialogTransformer;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.main.GridItem;
import com.gt.magicbox.main.HomeGridViewAdapter;
import com.gt.magicbox.main.MoreFunctionDialog;
import com.gt.magicbox.member.AddMemberActivity;
import com.gt.magicbox.member.MemberChooseActivity;
import com.gt.magicbox.member.MemberDoResultActivity;
import com.gt.magicbox.member.MemberRechargeActivity;
import com.gt.magicbox.order.OrderListActivity;
import com.gt.magicbox.pos.bean.AppNameEnum;
import com.gt.magicbox.pos.bean.bankcardcollection.cancel.CancelBean;
import com.gt.magicbox.pos.bean.bankcardcollection.pay.PayBean;
import com.gt.magicbox.pos.bean.codecollection.TransIdEnum;
import com.gt.magicbox.pos.bean.codecollection.codepay.CodePayBean;
import com.gt.magicbox.pos.bean.sweepcollection.scan.ScanBean;
import com.gt.magicbox.pos.util.ObjectUtils;
import com.gt.magicbox.pos.util.PromptUtils;
import com.gt.magicbox.setting.wificonnention.WifiConnectionActivity;
import com.gt.magicbox.utils.NetworkUtils;
import com.gt.magicbox.utils.RxBus;
import com.gt.magicbox.utils.commonutil.AppManager;
import com.gt.magicbox.utils.commonutil.ConvertUtils;
import com.gt.magicbox.utils.commonutil.FileHelper;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.gt.magicbox.utils.commonutil.PhoneUtils;
import com.gt.magicbox.utils.commonutil.ToastUtil;
import com.gt.magicbox.widget.LoadingProgressDialog;
import com.orhanobut.hawk.Hawk;
import com.ums.AppHelper;
import com.ums.upos.sdk.scanner.OnScanListener;
import com.ums.upos.sdk.scanner.ScannerConfig;
import com.ums.upos.sdk.scanner.ScannerManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:
 * Created by jack-lin on 2017/7/19 0019.
 */

public class ChosePayModeActivity extends BaseActivity {
    private String[] itemNameArray = {"会员卡","扫一扫", "码上收","银行卡","现金"};
    private Integer[] imageResArray = {R.drawable.home_member,R.drawable.chose_scan, R.drawable.chose_code,
            R.drawable.chose_bank_card,R.drawable.chose_cash};
    private int[] colorNormalArray = {0xfff04a4a,0xfffdd451, 0xffa871e6,0xfffc7473,0xff4db3ff};
    private int[] colorFocusedArray = {0x99f04a4a,0x99fdd451, 0x99a871e6,0x99fc7473,0x994db3ff};
    private ArrayList<GridItem> homeData = new ArrayList<>();
    private ListView home_grid;
    private HomeGridViewAdapter gridViewAdapter;

    private static final String TAG = ChosePayModeActivity.class.getSimpleName();
    RelativeLayout payMember;
    private int customerType;
    public static final int TYPE_FIT_PAY = 0;
    public static final int TYPE_MEMBER_PAY = 1;
    public static final int TYPE_MEMBER_RECHARGE = 2;
    public static final int TYPE_ORDER_PUSH = 3;

    private double money;
    private String orderNo="";
    private MoreFunctionDialog dialog;
    private HttpRequestDialog httpRequestDialog;
    private MemberCardBean memberCardBean;
    private LoadingProgressDialog loadingProgressDialog;
    private CashOrderBean cashOrderBean;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_chose);
        setToolBarTitle("选择支付方式");
        if (this.getIntent() != null) {
            money = this.getIntent().getDoubleExtra("money", 0);
            customerType = this.getIntent().getIntExtra("customerType", 0);
            memberCardBean= (MemberCardBean) this.getIntent().getSerializableExtra("memberCardBean");
            orderNo=this.getIntent().getStringExtra("orderNo");
        }
        initView();
    }
    private void initView() {
        initViewData();
        home_grid = (ListView) findViewById(R.id.listView);
        int itemCount=4;
        if (haveMemberPay()){
            itemCount=5;
        }
        gridViewAdapter = new HomeGridViewAdapter(this, R.layout.home_grid_item, homeData, itemCount);
        if (haveMemberPay()){
            gridViewAdapter.setLogoSize(ConvertUtils.dp2px(30),ConvertUtils.dp2px(30));
        }else gridViewAdapter.setLogoSize(ConvertUtils.dp2px(40),ConvertUtils.dp2px(40));
        home_grid.setAdapter(gridViewAdapter);
        home_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                if (!haveMemberPay() || customerType == TYPE_ORDER_PUSH) {
                    i++;
                }
                switch (i) {
                    case 0:
                        if (memberCardBean != null) {
                            if (memberCardBean.money < money) {
                                if (dialog == null) {
                                    dialog = new MoreFunctionDialog(ChosePayModeActivity.this, "您的会员卡余额不足，支付失败", R.style.HttpRequestDialogStyle);
                                    dialog.getConfirmButton().setText("确认");
                                    dialog.getConfirmButton().setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.dismiss();
                                        }
                                    });
                                }
                                dialog.show();
                            } else {
                                createCashOrder("" + money);
                            }
                        }
                        break;
                    case 1:
                        sanPay("" + (int) (money * 100), "");
                        break;
                    case 2:
                        codePay("" + (int) (money * 100), "");

                        break;
                    case 3:
                        bankCardPay("" + (int) (money * 100), "");

                        break;
                    case 4:
                        intent = new Intent(ChosePayModeActivity.this, PaymentActivity.class);

                        if (customerType == TYPE_MEMBER_RECHARGE) {
                            intent.putExtra("type", PaymentActivity.TYPE_MEMBER_CALC);
                            intent.putExtra("MemberCardBean", memberCardBean);
                            intent.putExtra("orderMoney", money);
                        } else {
                            intent.putExtra("type", 1);
                            intent.putExtra("orderMoney", money);
                        }
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
        if (!haveMemberPay()) {
            homeData.remove(0);
        }
        if (customerType==TYPE_ORDER_PUSH){
            homeData.remove(3);
        }
    }
    private boolean haveMemberPay() {
        if ((customerType == TYPE_MEMBER_PAY &&
                memberCardBean != null && memberCardBean.ctName.equals("储值卡"))) {
            return true;
        } else return false;
    }

    private void postMemberSettlement() {
        if (memberCardBean != null) {
            HttpCall.getApiService()
                    .postMemberSettlement(memberCardBean.memberId, money,
                            0, 0, 0, 0)
                    .compose(ResultTransformer.<MemberCountMoneyBean>transformer())//线程处理 预处理
                    .subscribe(new BaseObserver<MemberCountMoneyBean>() {
                        @Override
                        public void onSuccess(MemberCountMoneyBean data) {
                            LogUtils.d(TAG, "postMemberSettlement onSuccess data=");
                            if (data != null)
                                memberPay(data.getBalanceMoney(), data.getBalanceMoney(), data.getTotalMoney(), 5);
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtils.d(TAG, "postMemberSettlement onError e" + e.getMessage());
                            super.onError(e);
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            LogUtils.d(TAG, "postMemberSettlement onFailure msg=" + msg);
                            super.onFailure(code, msg);
                        }
                    });
        }
    }

    private void memberPay(double discountMoney, final double realMoney , double originMoney, int payType){
      //  String orderCode="MB"+Hawk.get("shopId",0)+System.currentTimeMillis();
        HttpCall.getApiService()
                .memberPayWithoutCoupon(discountMoney,memberCardBean.memberId, cashOrderBean.getMagicBoxOrder().getOrderNo(), realMoney,payType
                , Hawk.get("shiftId",0), Hawk.get("shopId",0),originMoney,3,113,0)
                .compose(ResultTransformer.<BaseResponse>transformerNoData())//线程处理 预处理
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        LogUtils.d(TAG, "memberPay onSuccess " );
                        if (loadingProgressDialog!=null)loadingProgressDialog.dismiss();
                        AppManager.getInstance().finishActivity(VerificationActivity.class);
                        Intent intent=new Intent(getApplicationContext(), MemberDoResultActivity.class);
                        intent.putExtra("type",MemberDoResultActivity.TYPE_MEMBER_PAY);
                        intent.putExtra("balance",memberCardBean.money-realMoney);
                        intent.putExtra("realMoney",realMoney);

                        startActivity(intent);

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.d(TAG, "memberPay onError e" + e.getMessage());
                        super.onError(e);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        LogUtils.d(TAG, "memberPay" +
                                " onFailure msg=" + msg);
                        super.onFailure(code, msg);
                    }
                });
    }
    private void posOrder(String orderNo,  double money , int payType){
        if (customerType==TYPE_MEMBER_RECHARGE){//充值不生成订单
            memberRecharge(payType);
            payFinish();
            return;
        }
        if (customerType==TYPE_ORDER_PUSH){
            orderPushPayCallBack(payType);
            return;
        }
        final LoadingProgressDialog dialog =new LoadingProgressDialog(ChosePayModeActivity.this,"付款成功，生成订单中...");
        dialog.show();
        HttpCall.getApiService()
                .posOrder(PhoneUtils.getIMEI(),orderNo, money,payType
                        ,Hawk.get("shiftId",0))
                .compose(ResultTransformer.<BaseResponse>transformerNoData())//线程处理 预处理
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        LogUtils.d(TAG, "posOrder onSuccess data=" );
                        if (dialog!=null)dialog.dismiss();
                        payFinish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.d(TAG, "posOrder onError e" + e.getMessage());
                        if (dialog!=null)dialog.dismiss();
                        payFinish();
                        super.onError(e);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        LogUtils.d(TAG, "posOrder" +
                                " onFailure msg=" + msg);
                        super.onFailure(code, msg);
                    }
                });
    }
    private void payFinish() {
        AppManager.getInstance().finishActivity();
        AppManager.getInstance().finishActivity(PaymentActivity.class);
        if (customerType == TYPE_MEMBER_RECHARGE) {
            AppManager.getInstance().finishActivity(MemberRechargeActivity.class);
            AppManager.getInstance().finishActivity(MemberChooseActivity.class);
        } else if (customerType == TYPE_MEMBER_PAY) {
            Intent intent = new Intent(ChosePayModeActivity.this, PaymentActivity.class);
            startActivity(intent);
        }

    }
    private void orderPushPayCallBack(int payType){
        final LoadingProgressDialog dialog =new LoadingProgressDialog(ChosePayModeActivity.this,"付款成功，生成订单中...");
        PosRequestBean posRequestBean= new PosRequestBean();
        posRequestBean.payType=payType;
        HttpCall.getApiService()
                .posPayCallBack(orderNo,Hawk.get("shiftId",0),posRequestBean)
                .compose(ResultTransformer.<BaseResponse>transformerNoData())//线程处理 预处理
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        LogUtils.d(TAG, "orderPushPayCallBack onSuccess ");
                        orderPushPayFinish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.d(TAG, "orderPushPayCallBack onError");
                        orderPushPayFinish();
                        super.onError(e);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        LogUtils.d(TAG, "orderPushPayCallBack onFailure");
                        orderPushPayFinish();
                        super.onFailure(code, msg);
                    }
                });
    }
    private void orderPushPayFinish(){
        RxBus.get().post(new UpdateOrderListUIBean());
        payFinish();
        if (dialog!=null) {
            dialog.dismiss();
        }
    }
    private void createCashOrder(String money) {
        loadingProgressDialog=new LoadingProgressDialog(ChosePayModeActivity.this,"付款中...");
        loadingProgressDialog.show();
        HttpCall.getApiService()
                .createCashOrder(PhoneUtils.getIMEI(), money, 3, Hawk.get("shiftId", 0))
                .compose(ResultTransformer.<CashOrderBean>transformer())//线程处理 预处理
                .subscribe(new BaseObserver<CashOrderBean>() {
                    @Override
                    protected void onSuccess(CashOrderBean bean) {
                        LogUtils.d(TAG, "createCashOrder Success");
                        cashOrderBean=bean;
                        postMemberSettlement();

                    }

                    @Override
                    protected void onFailure(int code, String msg) {
                        LogUtils.d(TAG, "onFailure code=" + code + "  msg=" + msg);
                    }
                });
    }
    public boolean scanner(){
        LogUtils.d(TAG, "scanner");
        ScannerManager scannerManager = new ScannerManager();
        Bundle bundle = new Bundle();
        bundle.putInt(ScannerConfig.COMM_SCANNER_TYPE, 1);
        bundle.putBoolean(ScannerConfig.COMM_ISCONTINUOUS_SCAN, false);
        try{
            scannerManager.initScanner(bundle);
            scannerManager.startScan(600, new OnScanListener() {
                @Override
                public void onScanResult(int i, byte[] bytes) {
                    if(i == -2001){
                        ToastUtil.getInstance().showToast("操作取消");
                    }
                    LogUtils.d(TAG, "scanner result : " + i);
                    if (bytes != null && !bytes.equals("")) {
                        String res = new String(bytes);
                        LogUtils.d(TAG, "string : " + res);
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean codePay(String amt, String extOrderNo) {
        LogUtils.d(TAG, "codePay");
        try {
            String appName = AppNameEnum.CODEPAYCOLLECTION.getValue();
            String transId = "码上收";
            CodePayBean codePayBean = new CodePayBean();
            codePayBean.setAmt(amt);
            if (ObjectUtils.isNotEmpty(extOrderNo)){
                codePayBean.setExtOrderNo(extOrderNo);
            }
            LogUtils.d(TAG, "codePay: " + codePayBean.toJsonString());
            JSONObject transData = new JSONObject(codePayBean.toJsonString());
            AppHelper.callTrans(ChosePayModeActivity.this, appName, transId, transData);
            return true;
        }catch (Exception e){
            PromptUtils.getInstance(getApplicationContext()).showSysErrorLong();
            e.printStackTrace();
        }
        return false;
    }
    public boolean sanPay(String amt, String extOrderNo) {
        LogUtils.d(TAG, "sanPay");
        try {
            String transId = "扫一扫";
            String appName = AppNameEnum.SWEEPCOLLECTION.getValue();
            ScanBean scanPayBean = new ScanBean();
            scanPayBean.setAmt(amt);
            if (ObjectUtils.isNotEmpty(extOrderNo)){
                scanPayBean.setExtOrderNo(extOrderNo);
            }
            LogUtils.d(TAG, "sanPay: " + scanPayBean.toJsonString());
            JSONObject transData = new JSONObject(scanPayBean.toJsonString());
            AppHelper.callTrans(ChosePayModeActivity.this, appName, transId, transData);
            return true;
        }catch (Exception e){
            PromptUtils.getInstance(getApplicationContext()).showSysErrorLong();
            e.printStackTrace();
        }
        return false;
    }
    public boolean bankCardPay(String amt, String extOrderNo) {
        LogUtils.d(TAG, "pay");
        try {
            String appName = AppNameEnum.BANKCARDCOLLECTION.getValue();
            String transId = "消费";
            PayBean payBean = new PayBean();
            payBean.setAmt(amt);
            if(ObjectUtils.isNotEmpty(extOrderNo)){
                payBean.setExtOrderNo(extOrderNo);
            }
            JSONObject transData = new JSONObject(payBean.toJsonString());
            LogUtils.d(TAG, "pay: " + payBean.toJsonString());
            AppHelper.callTrans(ChosePayModeActivity.this, appName, transId, transData);
            return true;
        }catch (Exception e){
            PromptUtils.getInstance(getApplicationContext()).showSysErrorLong();
            e.printStackTrace();
        }
        return false;
    }
    public boolean returnMoney(String amt, String refNo,String date) {
        LogUtils.d(TAG, "pay");
        try {
            String appName = AppNameEnum.SWEEPCOLLECTION.getValue();
            String transId = "退货";
            CancelBean cancelBean = new CancelBean();
            cancelBean.setAmt(amt);
            cancelBean.setRefNo(refNo);
            cancelBean.setDate(date);
            JSONObject transData = new JSONObject(cancelBean.toJsonString());
            LogUtils.d(TAG, "pay: " + cancelBean.toJsonString());
            AppHelper.callTrans(ChosePayModeActivity.this, appName, transId, transData);
            return true;
        }catch (Exception e){
            PromptUtils.getInstance(getApplicationContext()).showSysErrorLong();
            e.printStackTrace();
        }
        return false;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            LogUtils.d(TAG, "requestCode --> " + requestCode + "  resultCode --> " + requestCode + "  data --> " + data);
            if(AppHelper.TRANS_REQUEST_CODE == requestCode){
                if(Activity.RESULT_OK == resultCode && ObjectUtils.isNotEmpty(data)){
                    Map<String,String> map = AppHelper.filterTransResult(data);
                    JSONObject transData = new JSONObject(map.get(AppHelper.TRANS_DATA));
                    // 不同的transId调用不同的回调方法
                    String appName = map.get(AppHelper.TRANS_APP_NAME);
                    String transId = map.get(AppHelper.TRANS_BIZ_ID);
                    if (appName.equals("POS 通")) {
                        String resDesc = transData.getString("resDesc");
                        if (!TextUtils.isEmpty(resDesc) && resDesc.equals("交易成功")) {
                            String memInfo = transData.getString("memInfo");
                            JSONObject memInfoObject = new JSONObject(memInfo);
                            String refNo = transData.getString("refNo");
                            String amt = transData.getString("amt");
                            String channelName = memInfoObject.getString("channelName");
                            int payType = 0;
                            if (!TextUtils.isEmpty(channelName)) {
                                if (channelName.contains("微信")) payType = 0;
                                else if (channelName.contains("支付宝")) payType = 1;
                            }

                            LogUtils.d(TAG, "memInfo=" + memInfo.toString());
                            if (!TextUtils.isEmpty(amt))
                                posOrder(refNo, Double.parseDouble(amt), payType);
                        }
                    }else  if (appName.equals("POS通码上收")) {
                        String resDesc = transData.getString("resDesc");
                        if (!TextUtils.isEmpty(resDesc) && resDesc.equals("交易成功")) {
                            String memInfo = transData.getString("memInfo");
                            JSONObject memInfoObject = new JSONObject(memInfo);
                            String refNo = transData.getString("refNo");
                            String amt = transData.getString("amt");
                            String targetSys = memInfoObject.getString("targetSys");
                            int payType = 0;
                            if (!TextUtils.isEmpty(targetSys)) {
                                if (targetSys.contains("WXPay")) payType = 0;
                                else if (targetSys.contains("Alipay")) payType = 1;
                            }

                            LogUtils.d(TAG, "memInfo=" + memInfo.toString());
                            if (!TextUtils.isEmpty(amt))
                                posOrder(refNo, Double.parseDouble(amt), payType);
                        }
                    }else  if (appName.equals("银行卡收款")){
                        String resDesc = transData.getString("resDesc");
                        LogUtils.d(TAG, "银行卡收款 resDesc=" + resDesc.toString());
                        if (!TextUtils.isEmpty(resDesc) && resDesc.equals("交易成功")) {
                            String refNo = transData.getString("refNo");
                            String amt = transData.getString("amt");
                            int payType = 4;
                            if (!TextUtils.isEmpty(amt))
                                posOrder(refNo, Double.parseDouble(amt), payType);
                        }
                    }
                   // String callBackFn = DuofenPayCallBackJSFactory.getInstance().callBackJS(appName, transId, transData.toString());
                    LogUtils.d(TAG, "transData="+transData.toString());
                    LogUtils.d(TAG, "map="+map.toString());

                }else if (Activity.RESULT_CANCELED == resultCode){
                    PromptUtils.getInstance(this).showToastShort("取消操作");
                }else if (Activity.RESULT_FIRST_USER == resultCode){
                    PromptUtils.getInstance(this).showToastShort("操作错误");
                }else {
                    PromptUtils.getInstance(this).showToastLong("其他错误，请尝试再次操作！错误码：resultCode");
                }
            }else if (AppHelper.PRINT_REQUEST_CODE == requestCode){
                LogUtils.d(TAG, "in print");
                Map<String,String> map = AppHelper.filterTransResult(data);
                for (String key : map.keySet()){
                    System.out.println(key + " : " + map.get(key));
                }
                LogUtils.d(TAG, ObjectUtils.map2String(map));
            }else {
                PromptUtils.getInstance(this).showCallBackErrorLong();
            }
        }catch (Exception e){
            PromptUtils.getInstance(this).showSysErrorLong();
        }
    }
    private void memberRecharge(final int payType) {
        LogUtils.d(TAG,"memberRecharge type="+payType);

        if (memberCardBean != null) {
            LogUtils.d(TAG,"memberRecharge");
            HttpCall.getApiService()
                    .memberRecharge(memberCardBean.memberId, money, payType, (Integer) Hawk.get("shopId"))
                    .compose(ResultTransformer.<BaseResponse>transformerNoData())//线程处理 预处理
                    .compose(new DialogTransformer().<BaseResponse>transformer())
                    .subscribe(new BaseObserver<BaseResponse>() {
                        @Override
                        public void onSuccess(BaseResponse data) {
                            LogUtils.d(TAG, "memberRecharge onSuccess " );
                            Intent intent=new Intent(getApplicationContext(), MemberDoResultActivity.class);
                            intent.putExtra("rechargeMoney",money);
                            intent.putExtra("MemberCardBean",memberCardBean);
                            intent.putExtra("orderNo","123");
                            intent.putExtra("payType",payType);
                            intent.putExtra("balance",memberCardBean.money+money);
                            startActivity(intent);
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            LogUtils.d(TAG, "memberRecharge onError e" + e.getMessage().toString());
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
