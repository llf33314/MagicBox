package com.gt.magicbox.coupon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.recyclerview.BaseRecyclerAdapter;
import com.gt.magicbox.base.recyclerview.SpaceItemDecoration;
import com.gt.magicbox.bean.DistributeCouponBean;
import com.gt.magicbox.bean.MemberCardBean;
import com.gt.magicbox.pay.ChosePayModeActivity;
import com.gt.magicbox.utils.commonutil.AppManager;
import com.gt.magicbox.utils.commonutil.ConvertUtils;
import com.gt.magicbox.utils.commonutil.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Description:
 * Created by jack-lin on 2017/8/29 0029.
 */

public class VerificationActivity extends BaseActivity {
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
    @BindView(R.id.order_money)
    TextView orderMoneyTextView;
    @BindView(R.id.coupon_view)
    RecyclerView couponView;
    @BindView(R.id.fen_coin_view)
    RecyclerView fenCoinView;
    @BindView(R.id.text_paid_in_amount)
    TextView textPaidInAmount;
    @BindView(R.id.chose_pay)
    Button chosePay;
    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.money)
    TextView money;
    private MemberCardBean memberCardBean;
    private double orderMoney;
    private double paidInAmountMoney = 0;//实付金额

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        setToolBarTitle("会员收银");
        if (this.getIntent() != null) {
            memberCardBean = (MemberCardBean) this.getIntent().getSerializableExtra("MemberCardBean");
            orderMoney = getIntent().getDoubleExtra("orderMoney", 0);
        }
        initView();
        initRecyclerView(couponView, HorizontalCouponAdapter.TYPE_COUPON);
        initRecyclerView(fenCoinView, HorizontalCouponAdapter.TYPE_FEN_COIN);
        calculateMoneyInAmount();
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
            if (memberCardBean.ctName.equals("储值卡")) {
                money.setText("卡内余额:¥" + memberCardBean.money + "元");
            }
            orderMoneyTextView.setText("订单金额¥" + orderMoney + "元");
        }
    }

    private void calculateMoneyInAmount() {
        if (memberCardBean != null) {
                paidInAmountMoney = orderMoney;
                textPaidInAmount.setText("实收金额:¥" + paidInAmountMoney + "元");
        }
    }

    private void initRecyclerView(RecyclerView recyclerView, int type) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        final List<DistributeCouponBean> lists = new ArrayList<DistributeCouponBean>();
        Observable.range(0, 9).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                lists.add(new DistributeCouponBean("name:" + integer));
            }
        });
        final HorizontalCouponAdapter adapter = new HorizontalCouponAdapter(this, lists, type);
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, Object item, int position) {
                ToastUtil.getInstance().showToast("position=" + position);
                for (DistributeCouponBean bean : lists) {
                    bean.setSelected(false);
                }
                lists.get(position).setSelected(true);
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(ConvertUtils.dp2px(5), SpaceItemDecoration.SPACE_RIGHT));

    }

    @OnClick({R.id.chose_pay, R.id.cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.chose_pay:
                Intent intent = new Intent(getApplicationContext(), ChosePayModeActivity.class);
                intent.putExtra("customerType", ChosePayModeActivity.TYPE_MEMBER_PAY);
                intent.putExtra("money", paidInAmountMoney);
                intent.putExtra("memberCardBean",memberCardBean);
                startActivity(intent);
                AppManager.getInstance().finishActivity();
                break;
            case R.id.cancel:
                AppManager.getInstance().finishActivity();
                break;
        }
    }
}
