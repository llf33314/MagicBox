package com.gt.magicbox.pay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.bean.CashOrderBean;
import com.gt.magicbox.bean.MemberCardBean;
import com.gt.magicbox.bean.MemberCountMoneyBean;
import com.gt.magicbox.coupon.VerificationActivity;
import com.gt.magicbox.http.BaseResponse;
import com.gt.magicbox.http.HttpRequestDialog;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.main.MoreFunctionDialog;
import com.gt.magicbox.member.MemberDoResultActivity;
import com.gt.magicbox.pos.bean.AppNameEnum;
import com.gt.magicbox.pos.bean.bankcardcollection.pay.PayBean;
import com.gt.magicbox.pos.bean.codecollection.TransIdEnum;
import com.gt.magicbox.pos.bean.codecollection.codepay.CodePayBean;
import com.gt.magicbox.pos.bean.sweepcollection.scan.ScanBean;
import com.gt.magicbox.pos.util.ObjectUtils;
import com.gt.magicbox.pos.util.PromptUtils;
import com.gt.magicbox.setting.wificonnention.WifiConnectionActivity;
import com.gt.magicbox.utils.NetworkUtils;
import com.gt.magicbox.utils.commonutil.AppManager;
import com.gt.magicbox.utils.commonutil.PhoneUtils;
import com.gt.magicbox.utils.commonutil.ToastUtil;
import com.gt.magicbox.widget.LoadingProgressDialog;
import com.orhanobut.hawk.Hawk;
import com.ums.AppHelper;
import com.ums.upos.sdk.scanner.OnScanListener;
import com.ums.upos.sdk.scanner.ScannerConfig;
import com.ums.upos.sdk.scanner.ScannerManager;

import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:
 * Created by jack-lin on 2017/7/19 0019.
 */

public class ChosePayModeActivity extends BaseActivity {
    private static final String TAG = ChosePayModeActivity.class.getSimpleName();
    @BindView(R.id.pay_member)
    RelativeLayout payMember;
    private int customerType;
    public static final int TYPE_FIT_PAY = 0;
    public static final int TYPE_MEMBER_PAY = 1;
    public static final int TYPE_MEMBER_RECHARGE = 2;

    @BindView(R.id.pay_wechat)
    RelativeLayout payWechat;
    @BindView(R.id.pay_zfb)
    RelativeLayout payZfb;
    @BindView(R.id.pay_cash)
    RelativeLayout payCash;
    private double money;
    private MoreFunctionDialog dialog;
    private HttpRequestDialog httpRequestDialog;
    private MemberCardBean memberCardBean;
    private LoadingProgressDialog loadingProgressDialog;
    private CashOrderBean cashOrderBean;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_pay);
        setToolBarTitle("选择支付方式");
        if (this.getIntent() != null) {
            money = this.getIntent().getDoubleExtra("money", 0);
            customerType = this.getIntent().getIntExtra("customerType", 0);
            memberCardBean= (MemberCardBean) this.getIntent().getSerializableExtra("memberCardBean");
        }
        if (customerType == TYPE_MEMBER_PAY&&
                memberCardBean!=null&&memberCardBean.ctName.equals("储值卡")) {
            payMember.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.pay_wechat, R.id.pay_zfb, R.id.pay_cash, R.id.pay_member})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pay_wechat:
                sanPay(""+(int)(money*100),"");
              //  scanner();
                break;
            case R.id.pay_zfb:
                codePay(""+(int)(money*100),"");
                break;
            case R.id.pay_member:
                if (memberCardBean!=null){
                    if (memberCardBean.money<money) {
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
                    }else {
                        createCashOrder(""+money);
                    }
                }
                break;
            case R.id.pay_cash:
                bankCardPay(""+(int)(money*100),"");
                //createCashOrder(""+money);
                break;
        }
    }

    /**
     * @param type 0-微信，1-支付宝
     */
    private void startERCodePay(int type) {
        if (NetworkUtils.isConnected()) {

            Hawk.put("payType", type);
            Intent intent = new Intent(ChosePayModeActivity.this, QRCodePayActivity.class);
            if (customerType==TYPE_MEMBER_RECHARGE){
                intent.putExtra("type", QRCodePayActivity.TYPE_MEMBER_RECHARGE);
                intent.putExtra("MemberCardBean",memberCardBean);
            }else {
                intent.putExtra("type", QRCodePayActivity.TYPE_PAY);

            }
            intent.putExtra("money", money);
            intent.putExtra("payMode", type);
            startActivity(intent);
        } else {
            if (dialog == null) {
                dialog = new MoreFunctionDialog(ChosePayModeActivity.this, "没有网络，请连接后重试", R.style.HttpRequestDialogStyle);
                dialog.getConfirmButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Intent intent = new Intent(ChosePayModeActivity.this, WifiConnectionActivity.class);
                        startActivity(intent);
                    }
                });
            }
            dialog.show();
        }
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
                            Log.d(TAG, "postMemberSettlement onSuccess data=");
                            if (data != null)
                                memberPay(data.getBalanceMoney(), data.getBalanceMoney(), data.getTotalMoney(), 5);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "postMemberSettlement onError e" + e.getMessage());
                            super.onError(e);
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            Log.d(TAG, "postMemberSettlement onFailure msg=" + msg);
                            super.onFailure(code, msg);
                        }
                    });
        }
    }

    private void memberPay(double discountMoney, final double realMoney , double originMoney, int payType){
      //  String orderCode="MB"+Hawk.get("shopId",0)+System.currentTimeMillis();
        HttpCall.getApiService()
                .memberPay(discountMoney,memberCardBean.memberId, cashOrderBean.getMagicBoxOrder().getOrderNo(), realMoney,payType
                        ,(Integer) Hawk.get("shopId"),originMoney,113)
                .compose(ResultTransformer.<BaseResponse>transformerNoData())//线程处理 预处理
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        Log.d(TAG, "memberPay onSuccess " );
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
                        Log.d(TAG, "memberPay onError e" + e.getMessage());
                        super.onError(e);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        Log.d(TAG, "memberPay" +
                                " onFailure msg=" + msg);
                        super.onFailure(code, msg);
                    }
                });
    }
    private void createCashOrder(String money) {
        loadingProgressDialog=new LoadingProgressDialog(ChosePayModeActivity.this,"付款中...");
        loadingProgressDialog.show();
        HttpCall.getApiService()
                .createCashOrder(PhoneUtils.getIMEI(), money, 2, Hawk.get("shiftId", 0))
                .compose(ResultTransformer.<CashOrderBean>transformer())//线程处理 预处理
                .subscribe(new BaseObserver<CashOrderBean>() {
                    @Override
                    protected void onSuccess(CashOrderBean bean) {
                        Log.d(TAG, "createCashOrder Success");
                        cashOrderBean=bean;
                        postMemberSettlement();

                    }

                    @Override
                    protected void onFailure(int code, String msg) {
                        Log.d(TAG, "onFailure code=" + code + "  msg=" + msg);
                    }
                });
    }
    public boolean scanner(){
        Log.d(TAG, "scanner");
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
                    Log.d(TAG, "scanner result : " + i);
                    if (bytes != null && !bytes.equals("")) {
                        String res = new String(bytes);
                        Log.d(TAG, "string : " + res);
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
        Log.d(TAG, "codePay");
        try {
            String appName = AppNameEnum.CODEPAYCOLLECTION.getValue();
            String transId = "码上收";
            CodePayBean codePayBean = new CodePayBean();
            codePayBean.setAmt(amt);
            if (ObjectUtils.isNotEmpty(extOrderNo)){
                codePayBean.setExtOrderNo(extOrderNo);
            }
            Log.d(TAG, "codePay: " + codePayBean.toJsonString());
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
        Log.d(TAG, "sanPay");
        try {
            String transId = "扫一扫";
            String appName = AppNameEnum.SWEEPCOLLECTION.getValue();
            ScanBean scanPayBean = new ScanBean();
            scanPayBean.setAmt(amt);
            if (ObjectUtils.isNotEmpty(extOrderNo)){
                scanPayBean.setExtOrderNo(extOrderNo);
            }
            Log.d(TAG, "sanPay: " + scanPayBean.toJsonString());
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
        Log.d(TAG, "pay");
        try {
            String appName = AppNameEnum.BANKCARDCOLLECTION.getValue();
            String transId = "消费";
            PayBean payBean = new PayBean();
            payBean.setAmt(amt);
            if(ObjectUtils.isNotEmpty(extOrderNo)){
                payBean.setExtOrderNo(extOrderNo);
            }
            JSONObject transData = new JSONObject(payBean.toJsonString());
            Log.d(TAG, "pay: " + payBean.toJsonString());
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
            Log.d(TAG, "requestCode --> " + requestCode + "  resultCode --> " + requestCode + "  data --> " + data);
            if(AppHelper.TRANS_REQUEST_CODE == requestCode){
                if(Activity.RESULT_OK == resultCode && ObjectUtils.isNotEmpty(data)){
                    Map<String,String> map = AppHelper.filterTransResult(data);
                    JSONObject transData = new JSONObject(map.get(AppHelper.TRANS_DATA));
                    // 不同的transId调用不同的回调方法
                    String appName = map.get(AppHelper.TRANS_APP_NAME);
                    String transId = map.get(AppHelper.TRANS_BIZ_ID);
                   // String callBackFn = DuofenPayCallBackJSFactory.getInstance().callBackJS(appName, transId, transData.toString());
                    Log.d(TAG, "transData="+transData.toString());
                }else if (Activity.RESULT_CANCELED == resultCode){
                    PromptUtils.getInstance(this).showToastShort("取消操作");
                }else if (Activity.RESULT_FIRST_USER == resultCode){
                    PromptUtils.getInstance(this).showToastShort("操作错误");
                }else {
                    PromptUtils.getInstance(this).showToastLong("其他错误，请尝试再次操作！错误码：resultCode");
                }
            }else if (AppHelper.PRINT_REQUEST_CODE == requestCode){
                Log.d(TAG, "in print");
                Map<String,String> map = AppHelper.filterTransResult(data);
                for (String key : map.keySet()){
                    System.out.println(key + " : " + map.get(key));
                }
                Log.d(TAG, ObjectUtils.map2String(map));
            }else {
                PromptUtils.getInstance(this).showCallBackErrorLong();
            }
        }catch (Exception e){
            PromptUtils.getInstance(this).showSysErrorLong();
        }
    }
}
