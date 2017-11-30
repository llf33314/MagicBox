package com.gt.magicbox.coupon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.bean.CouponVerificationBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:
 * Created by jack-lin on 2017/11/30 0030.
 * Buddha bless, never BUG!
 */

public class CouponVerificationSuccess extends BaseActivity {
    @BindView(R.id.customer_balance)
    TextView customerBalance;
    @BindView(R.id.custom_recharge)
    TextView customRecharge;
    @BindView(R.id.customer_success_tip)
    TextView customerSuccessTip;
    @BindView(R.id.confirmButton)
    Button confirmButton;
    @BindView(R.id.cashier_balance)
    TextView cashierBalance;
    @BindView(R.id.cashier_recharge)
    TextView cashierRecharge;
    @BindView(R.id.cashier_success_tip)
    TextView cashierSuccessTip;
    private CouponVerificationBean couponVerificationBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_recharge_result);
        if (this.getIntent() != null) {
            couponVerificationBean = (CouponVerificationBean) getIntent().getSerializableExtra("couponVerificationBean");
        }
        initView();
    }

    private void initView() {
        customerSuccessTip.setVisibility(View.GONE);
        cashierSuccessTip.setVisibility(View.GONE);
        customerBalance.setText(R.string.verification_success);
        cashierBalance.setText(R.string.verification_success);
        if (couponVerificationBean != null && !TextUtils.isEmpty(couponVerificationBean.getCardName())) {
            cashierRecharge.setText(couponVerificationBean.getCardName());
            customRecharge.setText(couponVerificationBean.getCardName());
        }

    }

    @OnClick(R.id.confirmButton)
    public void onViewClicked() {
        finish();
    }
}
