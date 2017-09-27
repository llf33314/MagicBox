package com.gt.magicbox.member;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.pay.ChosePayModeActivity;
import com.gt.magicbox.pay.PaymentActivity;
import com.gt.magicbox.utils.commonutil.AppManager;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:
 * Created by jack-lin on 2017/9/26 0026.
 * Buddha bless, never BUG!
 */

public class MemberRechargeResultActivity extends BaseActivity {
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
    }

    private void initData() {

        if (this.getIntent() != null) {
            rechargeMoney = getIntent().getDoubleExtra("rechargeMoney", 0);
            balance = getIntent().getDoubleExtra("balance", 0);
            BigDecimal bg = new BigDecimal(rechargeMoney);
            bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            bg = new BigDecimal(balance);
            bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        }
    }
    private void initView(){
        customerBalance.setText("当前账户余额:"+balance+"元");
        cashierBalance.setText("当前账户余额:"+balance+"元");
        cashierRecharge.setText("充值金额:"+rechargeMoney+"元");
        customRecharge.setText("充值金额:"+rechargeMoney+"元");

    }
    @OnClick(R.id.confirmButton)
    public void onViewClicked() {
        finish();
    }
}
