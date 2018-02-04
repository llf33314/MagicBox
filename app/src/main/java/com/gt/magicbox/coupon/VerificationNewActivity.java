package com.gt.magicbox.coupon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.recyclerview.SpaceItemDecoration;
import com.gt.magicbox.bean.MemberCardBean;
import com.gt.magicbox.bean.MemberCouponBean;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.DialogTransformer;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.utils.DoubleCalcUtils;
import com.gt.magicbox.utils.commonutil.ConvertUtils;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.orhanobut.hawk.Hawk;
import com.suke.widget.SwitchButton;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description:
 *
 * @author jack-lin
 * @date 2017/8/29 0029
 */

public class VerificationNewActivity extends BaseActivity {
    private static final String TAG = VerificationNewActivity.class.getSimpleName();
    @BindView(R.id.nickName)
    TextView nickName;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.nickNameLayout)
    RelativeLayout nickNameLayout;
    @BindView(R.id.totalMoney)
    TextView totalMoney;
    @BindView(R.id.totalMoneyLayout)
    RelativeLayout totalMoneyLayout;
    @BindView(R.id.memberDiscount)
    TextView memberDiscount;
    @BindView(R.id.discountSwitchButton)
    SwitchButton discountSwitchButton;
    @BindView(R.id.discountLayout)
    RelativeLayout discountLayout;
    @BindView(R.id.couponTotal)
    TextView couponTotal;
    @BindView(R.id.coupon_view)
    RecyclerView couponView;
    @BindView(R.id.couponLayout)
    RelativeLayout couponLayout;
    @BindView(R.id.fenCoinTextView)
    TextView fenCoinTextView;
    @BindView(R.id.fenCoinSwitchButton)
    SwitchButton fenCoinSwitchButton;
    @BindView(R.id.fenCoinLayout)
    RelativeLayout fenCoinLayout;
    @BindView(R.id.integrateTextView)
    TextView integrateTextView;
    @BindView(R.id.integrateSwitchButton)
    SwitchButton integrateSwitchButton;
    @BindView(R.id.integrateLayout)
    RelativeLayout integrateLayout;
    @BindView(R.id.calcDiscountLeft)
    TextView calcDiscountLeft;
    @BindView(R.id.calcDiscountRight)
    TextView calcDiscountRight;
    @BindView(R.id.calcDiscountLayout)
    RelativeLayout calcDiscountLayout;
    @BindView(R.id.calcCouponRight)
    TextView calcCouponRight;
    @BindView(R.id.calcCouponLayout)
    RelativeLayout calcCouponLayout;
    @BindView(R.id.calcFenCoinRight)
    TextView calcFenCoinRight;
    @BindView(R.id.calcFenCoinLayout)
    RelativeLayout calcFenCoinLayout;
    @BindView(R.id.calcIntegrateRight)
    TextView calcIntegrateRight;
    @BindView(R.id.calcIntegrateLayout)
    RelativeLayout calcIntegrateLayout;
    @BindView(R.id.detailLayout)
    LinearLayout detailLayout;
    @BindView(R.id.text_paid_in_amount)
    TextView textPaidInAmount;
    @BindView(R.id.paidInAmountLayout)
    RelativeLayout paidInAmountLayout;
    @BindView(R.id.chose_pay)
    Button chosePay;
    @BindView(R.id.cancel)
    Button cancel;
    private MemberCardBean memberCardBean;
    private double orderMoney;
    private double paidInAmountMoney = 0;//实付金额
    private double discountMoney = 0;
    private MemberCouponBean memberCouponBean;
    private int lastPosition = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_new);
        ButterKnife.bind(this);
        setToolBarTitle("会员收银");
        if (this.getIntent() != null) {
            memberCardBean = (MemberCardBean) this.getIntent().getSerializableExtra("MemberCardBean");
            orderMoney = getIntent().getDoubleExtra("orderMoney", 0);
            getMemberAvailableCouponData();
        }
        initView();
    }

    private void initView() {
        if (memberCardBean != null) {
            if (!TextUtils.isEmpty(memberCardBean.ctName)) {
                if (getString(R.string.discount_card_name).equals(memberCardBean.ctName)) {
                    discountLayout.setVisibility(View.VISIBLE);
                    calcDiscountLayout.setVisibility(View.VISIBLE);
                    memberDiscount.setText("会员折扣(" + memberCardBean.discount + "折)");
                    calcDiscountLeft.setText("会员折扣(" + memberCardBean.discount + "折)");
                    calcDiscountRight.setText("-¥" + getMemberDiscount());
                    discountSwitchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                            if (isChecked) {
                                calcDiscountLayout.setVisibility(View.VISIBLE);
                                discountMoney = getMemberDiscount();
                            } else {
                                calcDiscountLayout.setVisibility(View.GONE);
                                discountMoney = 0;
                            }
                        }
                    });
                }
            }
        }
    }

    private double getMemberDiscount() {
        try {
            return DoubleCalcUtils.keepDecimalPoint(2, ((10 - memberCardBean.discount) / 10.0) * orderMoney);
        } catch (Exception e) {
            return 0;
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
                                    initCouponRecyclerView(couponView, data);
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

    private void initCouponRecyclerView(RecyclerView recyclerView, final List<MemberCouponBean> data) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        final CheckStateAdapter adapter = new CheckStateAdapter(getApplicationContext(), data);
        adapter.setOnItemClickListener(new CheckStateAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, CheckStateAdapter.StateHolder holder, int position) {
                LogUtils.d("lastPosition=" + lastPosition + "  position=" + position);
                if (lastPosition != position) {
                    memberCouponBean = data.get(position);
                    //calculateCoupon(data.get(position));
                    lastPosition = position;
                } else {
                    lastPosition = -1;
                    memberCouponBean = null;
                    //calculateCoupon(null);
                }
            }
        });
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (adapter != null) {
                    adapter.updateCurrentHolder();
                }
                return false;
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(ConvertUtils.dp2px(5), SpaceItemDecoration.SPACE_RIGHT));

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
}
