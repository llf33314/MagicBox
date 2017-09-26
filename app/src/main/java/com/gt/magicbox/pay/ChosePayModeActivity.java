package com.gt.magicbox.pay;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.bean.LoginBean;
import com.gt.magicbox.bean.MemberCardBean;
import com.gt.magicbox.bean.MemberSettlementBean;
import com.gt.magicbox.http.BaseResponse;
import com.gt.magicbox.http.HttpRequestDialog;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.DialogTransformer;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.main.MoreFunctionDialog;
import com.gt.magicbox.member.AddMemberActivity;
import com.gt.magicbox.setting.wificonnention.WifiConnectionActivity;
import com.gt.magicbox.utils.NetworkUtils;
import com.orhanobut.hawk.Hawk;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import retrofit2.http.Query;

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
        if (customerType == TYPE_MEMBER_PAY) {
            payMember.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.pay_wechat, R.id.pay_zfb, R.id.pay_cash, R.id.pay_member})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pay_wechat:
                startERCodePay(0);
                break;
            case R.id.pay_zfb:
                startERCodePay(1);
                break;
            case R.id.pay_member:
                if (memberCardBean!=null){
                    if (memberCardBean.money<money) {
                        if (dialog == null) {
                            dialog = new MoreFunctionDialog(ChosePayModeActivity.this, "您的会员卡余额不足，支付失败", R.style.HttpRequestDialogStyle);
                            dialog.getConfirmButton().setText("");
                            dialog.getConfirmButton().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                        }
                        dialog.show();
                    }else {
                        postMemberSettlement();
                    }
                }
                break;
            case R.id.pay_cash:
                memberRecharge(2);
                Intent intent = new Intent(ChosePayModeActivity.this, PaymentActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("orderMoney", money);
                startActivity(intent);
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
        if (memberCardBean != null)
            HttpCall.getApiService()
                    .postMemberSettlement(new MemberSettlementBean(memberCardBean.memberId, (int)money,
                            0, 0, 0, 0))
                    .compose(ResultTransformer.<BaseResponse>transformerNoData())//线程处理 预处理
                    .compose(new DialogTransformer().<BaseResponse>transformer())
                    .subscribe(new BaseObserver<BaseResponse>() {
                        @Override
                        public void onSuccess(BaseResponse data) {
                            Log.d(TAG, "postMemberSettlement onSuccess data=" + data.getData().toString());
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
    private void memberRecharge(int payType) {
        if (memberCardBean != null && customerType == TYPE_MEMBER_RECHARGE) {
            HttpCall.getApiService()
                    .memberRecharge(memberCardBean.memberId, money, payType, (Integer) Hawk.get("shopId"))
                    .compose(ResultTransformer.<BaseResponse>transformerNoData())//线程处理 预处理
                    .compose(new DialogTransformer().<BaseResponse>transformer())
                    .subscribe(new BaseObserver<BaseResponse>() {
                        @Override
                        public void onSuccess(BaseResponse data) {
                            Log.d(TAG, "memberRecharge onSuccess data=" + data.getData().toString());
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "memberRecharge onError e" + e.getMessage());
                            super.onError(e);
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            Log.d(TAG, "memberRecharge onFailure msg=" + msg);
                            super.onFailure(code, msg);
                        }
                    });
        }
    }
}
