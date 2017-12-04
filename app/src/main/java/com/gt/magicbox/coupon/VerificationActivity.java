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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.recyclerview.BaseRecyclerAdapter;
import com.gt.magicbox.base.recyclerview.SpaceItemDecoration;
import com.gt.magicbox.bean.MemberCardBean;
import com.gt.magicbox.bean.MemberCouponBean;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.DialogTransformer;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.pay.ChosePayModeActivity;
import com.gt.magicbox.utils.commonutil.AppManager;
import com.gt.magicbox.utils.commonutil.ConvertUtils;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.orhanobut.hawk.Hawk;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:
 * Created by jack-lin on 2017/8/29 0029.
 */

public class VerificationActivity extends BaseActivity {
    private static final String TAG = VerificationActivity.class.getSimpleName();
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
    @BindView(R.id.discountInfo)
    TextView discountInfo;
    @BindView(R.id.couponInfo)
    TextView couponInfo;
    @BindView(R.id.couponLayout)
    LinearLayout couponLayout;
    private MemberCardBean memberCardBean;
    private double orderMoney;
    private double paidInAmountMoney = 0;//实付金额
    private double discountMoney = 0;
    private MemberCouponBean memberCouponBean;
    private int lastPosition = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        setToolBarTitle("会员收银");
        if (this.getIntent() != null) {
            memberCardBean = (MemberCardBean) this.getIntent().getSerializableExtra("MemberCardBean");
            orderMoney = getIntent().getDoubleExtra("orderMoney", 0);
            getMemberAvailableCouponData();
        }
        initView();
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

    private void initCouponRecyclerView(RecyclerView recyclerView, int type, final List<MemberCouponBean> data) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        final HorizontalCouponAdapter adapter = new HorizontalCouponAdapter(this, data, type);
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, Object item, int position) {
                LogUtils.d("lastPosition=" + lastPosition + "  position=" + position);
                if (lastPosition != position) {
                    memberCouponBean = data.get(position);
                    calculateCoupon(data.get(position));
                    adapter.setCurrentItem(position);
                    adapter.notifyDataSetChanged();
                    lastPosition = position;
                } else {
                    lastPosition = -1;
                    adapter.setCurrentItem(-1);
                    adapter.notifyDataSetChanged();
                    memberCouponBean = null;
                    calculateCoupon(null);
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(ConvertUtils.dp2px(5), SpaceItemDecoration.SPACE_RIGHT));

    }

    private void calculateCoupon(MemberCouponBean memberCouponBean) {
        if (memberCouponBean != null) {
            if (memberCouponBean.getDiscount() > 0) {
                paidInAmountMoney = multiply(orderMoney / 10, memberCouponBean.getDiscount());
                discountMoney = subtract(orderMoney, paidInAmountMoney);
                discountInfo.setText("抵扣金额: 优惠券-" + discountMoney + "元");
                textPaidInAmount.setText("实收金额:¥" + paidInAmountMoney + "元");
            } else if (memberCouponBean.getReduce_cost() > 0 && orderMoney >= memberCouponBean.getReduce_cost()) {
                discountInfo.setText("抵扣金额: 优惠券-" + memberCouponBean.getCash_least_cost() + "元");
                paidInAmountMoney = subtract(orderMoney, memberCouponBean.getCash_least_cost());
                textPaidInAmount.setText("实收金额:¥" + paidInAmountMoney + "元");
                discountMoney = memberCouponBean.getReduce_cost();
            } else {
                discountInfo.setText("抵扣金额: 优惠券-0元");
                paidInAmountMoney = orderMoney;
                textPaidInAmount.setText("实收金额:¥" + paidInAmountMoney + "元");
                discountMoney = 0;
            }
            couponInfo.setText(memberCouponBean.getTitle());
        } else {
            paidInAmountMoney = orderMoney;
            textPaidInAmount.setText("实收金额:¥" + paidInAmountMoney + "元");
            discountMoney = 0;
            couponInfo.setText("");
            discountInfo.setText("");
        }

    }

    public double subtract(double d1, double d2) {

        BigDecimal bd1 = new BigDecimal(Double.toString(d1));

        BigDecimal bd2 = new BigDecimal(Double.toString(d2));

        return bd1.subtract(bd2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

    }

    public double multiply(double d1, double d2) {

        BigDecimal bd1 = new BigDecimal(Double.toString(d1));

        BigDecimal bd2 = new BigDecimal(Double.toString(d2));

        return bd1.multiply(bd2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

    }

    @OnClick({R.id.chose_pay, R.id.cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.chose_pay:
                Intent intent = new Intent(getApplicationContext(), ChosePayModeActivity.class);
                intent.putExtra("customerType", ChosePayModeActivity.TYPE_MEMBER_PAY);
                intent.putExtra("money", paidInAmountMoney);
                intent.putExtra("discountAfterMoney", paidInAmountMoney);
                intent.putExtra("discountMoney", discountMoney);

                intent.putExtra("memberCardBean", memberCardBean);
                if (memberCouponBean != null) {
                    intent.putExtra("memberCouponBean", memberCouponBean);
                }
                startActivity(intent);
                AppManager.getInstance().finishActivity();
                break;
            case R.id.cancel:
                AppManager.getInstance().finishActivity();
                break;
        }
    }

    private void getMemberAvailableCouponData() {
        if (null != memberCardBean) {
            HttpCall.getApiService()
                    .getMemberAvailableCoupon(memberCardBean.memberId, orderMoney, Hawk.get("shopId", 0))
                    .compose(ResultTransformer.<List<MemberCouponBean>>transformer())//线程处理 预处理
                    .compose(new DialogTransformer().<List<MemberCouponBean>>transformer())
                    .subscribe(new BaseObserver<List<MemberCouponBean>>() {
                        @Override
                        public void onSuccess(List<MemberCouponBean> data) {
                            LogUtils.i(TAG, "onSuccess");
                            if (data != null) {
                                LogUtils.i(TAG, "onSuccess size=" + data.size());
                                if (data.size() > 0) {
                                    initCouponRecyclerView(couponView, HorizontalCouponAdapter.TYPE_COUPON, data);
                                } else if (data.size() == 0) {
                                    couponLayout.setVisibility(View.GONE);
                                }

                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            couponLayout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            super.onFailure(code, msg);
                            couponLayout.setVisibility(View.GONE);
                        }
                    });
        }
    }
}
