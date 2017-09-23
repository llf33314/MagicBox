package com.gt.magicbox.member;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.bean.MemberCardBean;

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
            fanCoinNumber.setText(""+memberCardBean.fans_currency);
            integralNumber.setText(""+memberCardBean.integral);
            balanceNumber.setText("¥"+memberCardBean.money+"元");

        }
    }

    @OnClick({R.id.chose_pay, R.id.returnButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.chose_pay:
                break;
            case R.id.returnButton:
                break;
        }
    }
}
