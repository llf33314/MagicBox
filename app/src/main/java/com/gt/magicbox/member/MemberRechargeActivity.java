package com.gt.magicbox.member;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.bean.MemberCardBean;
import com.gt.magicbox.pay.ChosePayModeActivity;
import com.gt.magicbox.utils.CashierInputFilter;
import com.gt.magicbox.utils.commonutil.AppManager;
import com.gt.magicbox.utils.commonutil.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:
 * Created by jack-lin on 2017/9/22 0022.
 * Buddha bless, never BUG!
 */

public class MemberRechargeActivity extends BaseActivity {
    @BindView(R.id.image_head)
    ImageView imageHead;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.card_type)
    TextView cardType;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.right)
    TextView right;
    @BindView(R.id.member_info_layout)
    RelativeLayout memberInfoLayout;
    @BindView(R.id.fanCoinNumber)
    TextView fanCoinNumber;
    @BindView(R.id.fenLayout)
    LinearLayout fenLayout;
    @BindView(R.id.integralNumber)
    TextView integralNumber;
    @BindView(R.id.integralLayout)
    LinearLayout integralLayout;
    @BindView(R.id.balanceNumber)
    TextView balanceNumber;
    @BindView(R.id.chose_pay)
    Button chosePay;
    @BindView(R.id.returnButton)
    Button returnButton;
    @BindView(R.id.rechargeMoney)
    EditText rechargeMoney;
    private MemberCardBean memberCardBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_recharge);
        ButterKnife.bind(this);
        if (this.getIntent() != null) {
            memberCardBean = (MemberCardBean) this.getIntent().getSerializableExtra("MemberCardBean");
        }
        initView();
    }

    private void initView() {
        if (memberCardBean != null) {
            if (!TextUtils.isEmpty(memberCardBean.nickName)) {
                name.setText(memberCardBean.nickName);
            }
            if (!TextUtils.isEmpty(memberCardBean.ctName)) {
                cardType.setText("会员卡:" + memberCardBean.ctName);
            }
            if (!TextUtils.isEmpty(memberCardBean.phone)) {
                phone.setText("手机号:" + memberCardBean.phone);
            }
            right.setVisibility(View.GONE);
            fanCoinNumber.setText("" + memberCardBean.fans_currency);
            integralNumber.setText("" + memberCardBean.integral);
            balanceNumber.setText("¥" + memberCardBean.money + "元");

        }
        InputFilter [] filters ={new CashierInputFilter()};
        rechargeMoney.setFilters(filters);
    }

    @OnClick({R.id.chose_pay, R.id.returnButton})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.chose_pay:
                String recharge=rechargeMoney.getEditableText().toString();
                if (!TextUtils.isEmpty(recharge)) {
                    double money =Double.parseDouble(recharge);
                    if (money>0) {
                        intent = new Intent(getApplicationContext(), ChosePayModeActivity.class);
                        intent.putExtra("customerType", ChosePayModeActivity.TYPE_MEMBER_RECHARGE);
                        intent.putExtra("money", money);
                        intent.putExtra("memberCardBean", memberCardBean);
                        startActivity(intent);
                    }else ToastUtil.getInstance().showToast("金额不能为0元");
                }else ToastUtil.getInstance().showToast("请输入充值金额");
                break;
            case R.id.returnButton:
                AppManager.getInstance().finishActivity();
                break;
        }
    }
}
