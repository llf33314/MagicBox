package com.gt.magicbox.member;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gt.magicbox.Constant;
import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.BaseConstant;
import com.gt.magicbox.bean.CashOrderBean;
import com.gt.magicbox.bean.MemberCardBean;
import com.gt.magicbox.bean.StaffBean;
import com.gt.magicbox.exchange.ShiftExchangeActivity;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.DialogTransformer;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.pay.ChosePayModeActivity;
import com.gt.magicbox.pay.PaymentActivity;
import com.gt.magicbox.setting.printersetting.PrintManager;
import com.gt.magicbox.setting.printersetting.PrinterConnectService;
import com.gt.magicbox.utils.commonutil.AppManager;
import com.gt.magicbox.utils.commonutil.PhoneUtils;
import com.gt.magicbox.utils.commonutil.TimeUtils;
import com.gt.magicbox.utils.voice.PlaySound;
import com.gt.magicbox.utils.voice.VoiceUtils;
import com.orhanobut.hawk.Hawk;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:
 * Created by jack-lin on 2017/9/26 0026.
 * Buddha bless, never BUG!
 */

public class MemberDoResultActivity extends BaseActivity {
    private String TAG = MemberDoResultActivity.class.getSimpleName();
    @BindView(R.id.customer_success_tip)
    TextView customerSuccessTip;
    @BindView(R.id.cashier_success_tip)
    TextView cashierSuccessTip;
    private int type = 0;
    public static final int TYPE_MEMBER_RECHARGE = 0;
    public static final int TYPE_MEMBER_PAY = 1;
    private static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    @BindView(R.id.customer_balance)
    TextView customerBalance;
    @BindView(R.id.custom_recharge)
    TextView customRecharge;
    @BindView(R.id.confirmButton)
    Button confirmButton;
    @BindView(R.id.cashier_balance)
    TextView cashierBalance;
    @BindView(R.id.cashier_recharge)
    TextView cashierRecharge;
    private double rechargeMoney;
    private double balance;
    private double realMoney;

    private CashOrderBean cashOrderBean;


    private MemberCardBean memberCardBean;
    private String orderNo;
    private int payType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_recharge_result);
        ButterKnife.bind(this);
        initData();
        initView();
        AppManager.getInstance().finishActivity(ChosePayModeActivity.class);
        AppManager.getInstance().finishActivity(MemberRechargeActivity.class);
        AppManager.getInstance().finishActivity(PaymentActivity.class);
        AppManager.getInstance().finishActivity(VerificationChoseActivity.class);
    }

    private void initData() {

        if (this.getIntent() != null) {
            type = getIntent().getIntExtra("type", 0);
            payType = getIntent().getIntExtra("payType", 0);
            orderNo = getIntent().getStringExtra("orderNo");
            memberCardBean = (MemberCardBean) getIntent().getSerializableExtra("MemberCardBean");
            rechargeMoney = getIntent().getDoubleExtra("rechargeMoney", 0);
            balance = getIntent().getDoubleExtra("balance", 0);
            realMoney = getIntent().getDoubleExtra("realMoney", 0);
            BigDecimal bg = new BigDecimal(rechargeMoney);
            rechargeMoney = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            bg = new BigDecimal(balance);
            balance = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
    }

    private void initView() {
        customerBalance.setText("当前账户余额:" + balance + "元");
        cashierBalance.setText("当前账户余额:" + balance + "元");
        cashierRecharge.setText("充值金额:" + rechargeMoney + "元");
        customRecharge.setText("充值金额:" + rechargeMoney + "元");
        if (type == TYPE_MEMBER_PAY) {
            cashierRecharge.setVisibility(View.GONE);
            customRecharge.setVisibility(View.GONE);
            customerSuccessTip.setText("支付成功");
            cashierSuccessTip.setText("支付成功");
            playMemberPayVoice();
        } else if (type == TYPE_MEMBER_RECHARGE) {
            playMemberRechargeVoice();
        }

    }

    private void playMemberPayVoice() {
        VoiceUtils.with(getApplicationContext()).playMergeWavFile("$" + PlaySound.getCapitalValueOf(realMoney), 5)
                .setAppendListener(new VoiceUtils.AppendListener() {
                    @Override
                    public void append() {
                        VoiceUtils.with(getApplicationContext()).playMergeWavFile("$" + PlaySound.getCapitalValueOf(balance), 7)
                                .setAppendListener(null);
                    }
                });

    }

    private void playMemberRechargeVoice() {
        VoiceUtils.with(getApplicationContext()).playMergeWavFile("$" + PlaySound.getCapitalValueOf(rechargeMoney), 6)
                .setAppendListener(new VoiceUtils.AppendListener() {
                    @Override
                    public void append() {
                        VoiceUtils.with(getApplicationContext()).playMergeWavFile("$" + PlaySound.getCapitalValueOf(balance), 7)
                                .setAppendListener(null);
                    }
                });
    }

    @OnClick(R.id.confirmButton)
    public void onViewClicked() {
        if (type == TYPE_MEMBER_RECHARGE) {
            if (Constant.product.equals(BaseConstant.PRODUCTS[1])) {
                PrintManager printManager = new PrintManager(MemberDoResultActivity.this);
                printManager.startPrintMemberRechargeByText(memberCardBean, orderNo, "" + rechargeMoney, payType, "" + balance);
            } else if (Constant.product.equals(BaseConstant.PRODUCTS[0])) {
                PrinterConnectService.printEscMemberRecharge(memberCardBean, orderNo, "" + rechargeMoney, payType, "" + balance);
            }
        } else if (type == TYPE_MEMBER_PAY) {
            StaffBean.StaffListBean staffListBean = Hawk.get("StaffListBean");
            if (Constant.product.equals(BaseConstant.PRODUCTS[1])) {
                PrintManager printManager = new PrintManager(MemberDoResultActivity.this);
                printManager.startPrintReceiptByText(orderNo, realMoney + "元",
                        3, TimeUtils.millis2String(System.currentTimeMillis(), DEFAULT_FORMAT)
                        , staffListBean != null && !TextUtils.isEmpty(staffListBean.getName()) ? staffListBean.getName() : "空");
            } else if (Constant.product.equals(BaseConstant.PRODUCTS[0])) {
                PrinterConnectService.printEsc0829(orderNo, realMoney + "元",
                        staffListBean != null && !TextUtils.isEmpty(staffListBean.getName()) ? staffListBean.getName() : "空"
                        , 3, "");
            }
        }
        finish();

    }

}
