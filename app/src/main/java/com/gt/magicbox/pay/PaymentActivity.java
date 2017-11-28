package com.gt.magicbox.pay;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.GridView;

import com.gt.magicbox.Constant;
import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.BaseConstant;
import com.gt.magicbox.bean.MemberCardBean;
import com.gt.magicbox.camera.CodeCameraManager;
import com.gt.magicbox.camera.ZBarCameraManager;
import com.gt.magicbox.coupon.VerificationActivity;
import com.gt.magicbox.http.BaseResponse;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.DialogTransformer;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.main.MoreFunctionDialog;
import com.gt.magicbox.member.MemberDoResultActivity;
import com.gt.magicbox.member.MemberRechargeActivity;
import com.gt.magicbox.member.VerificationChoseActivity;
import com.gt.magicbox.utils.commonutil.AppManager;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.gt.magicbox.utils.commonutil.ToastUtil;
import com.gt.magicbox.widget.HintDismissDialog;
import com.orhanobut.hawk.Hawk;
import com.synodata.scanview.view.CodePreview;
import com.synodata.scanview.view.Preview$IDecodeListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description:
 *
 * @author jack-lin
 * @date 2017/7/18 0018
 */

public class PaymentActivity extends BaseActivity implements Preview$IDecodeListener {
    private final String TAG = PaymentActivity.class.getSimpleName();
    @BindView(R.id.preview)
    CodePreview preview;
    private GridView gridView;
    private KeyboardView keyboardView;
    private int type = 0;
    private double orderMoney = 0;
    private ZBarCameraManager zBarCameraManager;
    private MemberCardBean memberCardBean;
    public static final int TYPE_INPUT = 0;
    public static final int TYPE_CALC = 1;
    public static final int TYPE_MEMBER_PAY = 2;
    public static final int TYPE_COUPON_VERIFICATION = 3;
    public static final int TYPE_MEMBER_RECHARGE = 4;
    public static final int TYPE_MEMBER_CALC = 5;
    private Handler handler = new Handler();
    private MoreFunctionDialog dialog;
    private int code;
    private boolean isRequestingData = false;
    private Runnable handlerRunnable;
    private CodeCameraManager codeCameraManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initView();
    }

    private void initView() {
        if (getIntent() != null) {
            type = getIntent().getIntExtra("type", 0);
            orderMoney = getIntent().getDoubleExtra("orderMoney", 0);
            code = getIntent().getIntExtra("keyCode", 0);
            memberCardBean = (MemberCardBean) getIntent().getSerializableExtra("MemberCardBean");
        }
        if (type == TYPE_CALC || type == TYPE_MEMBER_CALC) {
            setToolBarTitle("现金支付");
        } else if (type == TYPE_INPUT) {
            setToolBarTitle("收银");
        } else if (type == TYPE_MEMBER_PAY) {
            setToolBarTitle("会员收银");
            if (Constant.product == BaseConstant.PRODUCTS[0])
                initCodeCamera();
        } else if (type == TYPE_COUPON_VERIFICATION) {
            setToolBarTitle("优惠券核销");
        } else if (type == TYPE_MEMBER_RECHARGE) {
            setToolBarTitle("会员卡充值");
            if (Constant.product == BaseConstant.PRODUCTS[0]) {
                initCodeCamera();
            }
        }
        keyboardView = (KeyboardView) findViewById(R.id.keyboard);
        keyboardView.setOrderMoney(orderMoney);
        keyboardView.setKeyboardType(type);
        keyboardView.setOnInputListener(new KeyboardView.OnInputListener() {
            @Override
            public void onInput() {
                if (codeCameraManager != null) {
                    if (handlerRunnable != null) {
                        handler.removeCallbacks(handlerRunnable);
                    }
                    codeCameraManager.stopScanningCamera();
                    handlerRunnable = new Runnable() {
                        @Override
                        public void run() {
                            codeCameraManager.startScanningCamera();
                        }
                    };
                    handler.postDelayed(handlerRunnable, 10000);
                }
            }
        });
        keyboardView.setOnKeyboardDoListener(new OnKeyboardDoListener() {
            @Override
            public void onPay(double money) {
                Intent intent;
                if (type == TYPE_INPUT) {
                    intent = new Intent(PaymentActivity.this, ChosePayModeActivity.class);
                    intent.putExtra("money", money);
                    startActivity(intent);
                } else if (type == TYPE_CALC) {
                    intent = new Intent(PaymentActivity.this, PayResultActivity.class);
                    intent.putExtra("success", true);
                    intent.putExtra("message", "" + money);
                    intent.putExtra("payType", PayResultActivity.TYPE_CASH);
                    startActivity(intent);
                    AppManager.getInstance().finishActivity(PaymentActivity.class);
                    AppManager.getInstance().finishActivity(ChosePayModeActivity.class);
                } else if (type == TYPE_MEMBER_CALC) {
                    memberRecharge(BaseConstant.PAY_ON_CASH);
                }
            }

            @Override
            public void onMemberPay(double money) {
                if (Constant.product == BaseConstant.PRODUCTS[0]) {
                    Intent intent = new Intent(PaymentActivity.this, PaymentActivity.class);
                    intent.putExtra("type", 2);
                    intent.putExtra("orderMoney", money);
                    startActivity(intent);
                } else if (Constant.product == BaseConstant.PRODUCTS[1]) {
                    Intent intent = new Intent(PaymentActivity.this, VerificationChoseActivity.class);
                    intent.putExtra("type", 2);
                    intent.putExtra("orderMoney", money);
                    startActivity(intent);
                }

            }

            @Override
            public void onNumberInput(String num) {
                if (type == TYPE_MEMBER_RECHARGE || type == TYPE_MEMBER_PAY || type == TYPE_COUPON_VERIFICATION) {
                    findMemberCardByPhone(num);
                }
            }
        });
        if (code > 0) onKeyDown(code, null);

    }

    @Override
    protected void onPause() {
        if (codeCameraManager != null) {
            codeCameraManager.releaseCamera();
        }
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyboardView != null)
            if (keyCode >= KeyEvent.KEYCODE_NUMPAD_0 && keyCode <= KeyEvent.KEYCODE_NUMPAD_9) {
                keyboardView.input("" + (keyCode - 144));
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_NUMPAD_DOT) {
                keyboardView.input(".");
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_DEL) {
                keyboardView.backspace();
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER) {
                keyboardView.enter();
                return true;
            }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initCodeCamera() {
        codeCameraManager = new CodeCameraManager(getApplicationContext(), preview, PaymentActivity.this);
        codeCameraManager.initCamera();
    }

    private void findMemberCardByPhone(final String numberString) {
        if (TextUtils.isEmpty(numberString)) {
            ToastUtil.getInstance().showToast("输入不能为空");
            return;
        }
        HttpCall.getApiService()
                .findMemberCardByPhone(Hawk.get("busId", 0), numberString)
                .compose(ResultTransformer.<MemberCardBean>transformer())//线程处理 预处理
                .compose(new DialogTransformer().<MemberCardBean>transformer())
                .subscribe(new BaseObserver<MemberCardBean>() {

                    @Override
                    protected void onSuccess(MemberCardBean bean) {
                        if (bean != null) {
                            if ((bean.ctName.equals("折扣卡") || bean.ctName.equals("积分卡"))
                                    && type == TYPE_MEMBER_RECHARGE) {
                                dialog = new MoreFunctionDialog(PaymentActivity.this, "此类型的卡暂不支持充值", R.style.HttpRequestDialogStyle);
                                dialog.show();
                            } else {
                                Intent intent = null;
                                if (type == TYPE_MEMBER_RECHARGE)
                                    intent = new Intent(getApplicationContext(), MemberRechargeActivity.class);
                                else if (type == TYPE_COUPON_VERIFICATION || type == TYPE_MEMBER_PAY) {
                                    intent = new Intent(getApplicationContext(), VerificationActivity.class);
                                    intent.putExtra("orderMoney", orderMoney);
                                }
                                intent.putExtra("MemberCardBean", bean);
                                startActivity(intent);
                                AppManager.getInstance().finishActivity();
                            }
                        }
                    }

                    @Override
                    protected void onFailure(int code, String msg) {
                        super.onFailure(code, msg);
                        LogUtils.d(TAG, "findMemberCardByPhone onFailure msg=" + msg.toString());
                        if (!TextUtils.isEmpty(msg) && getString(R.string.data_is_not_exit).equals(msg)) {
                            new HintDismissDialog(PaymentActivity.this, "该卡号不存在")
                                    .setDialogOnDismissListener(new DialogInterface.OnDismissListener() {
                                        public void onDismiss(DialogInterface dialog) {
                                            isRequestingData = false;
                                        }
                                    })
                                    .setCancelText("确认")
                                    .show();
                        }

                    }

                });
    }

    private void memberRecharge(final int payType) {
        LogUtils.d(TAG, "memberRecharge type=" + type);

        if (memberCardBean != null && type == TYPE_MEMBER_CALC) {
            LogUtils.d(TAG, "memberRecharge");
            HttpCall.getApiService()
                    .memberRecharge(memberCardBean.memberId, orderMoney, 10, Hawk.get("shopId", 0))
                    .compose(ResultTransformer.<BaseResponse>transformerNoData())//线程处理 预处理
                    .compose(new DialogTransformer().<BaseResponse>transformer())
                    .subscribe(new BaseObserver<BaseResponse>() {
                        @Override
                        public void onSuccess(BaseResponse data) {
                            LogUtils.d(TAG, "memberRecharge onSuccess ");
                            Intent intent = new Intent(getApplicationContext(), MemberDoResultActivity.class);
                            intent.putExtra("rechargeMoney", orderMoney);
                            intent.putExtra("MemberCardBean", memberCardBean);
                            intent.putExtra("orderNo", "123");
                            intent.putExtra("payType", payType);
                            intent.putExtra("balance", memberCardBean.money + orderMoney);
                            startActivity(intent);
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtils.d(TAG, "memberRecharge onError e" + e.getMessage());
                            new HintDismissDialog(PaymentActivity.this, "会员卡充值失败").show();
                            super.onError(e);
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            LogUtils.d(TAG, "memberRecharge onFailure msg=" + msg);
                            super.onFailure(code, msg);
                        }
                    });
        }
    }

    @Override
    protected void onStop() {
        if (codeCameraManager != null) {
            codeCameraManager.releaseCamera();
        }
        super.onStop();
    }

    @Override
    public void onDecodeResult(boolean bDecoded, String result, String typeInfo) {

        if (bDecoded && !TextUtils.isEmpty(result)) {
            LogUtils.d("scanResult", "result=" + result);
            if (type == TYPE_MEMBER_RECHARGE || type == TYPE_MEMBER_PAY || type == TYPE_COUPON_VERIFICATION) {
                if (!isRequestingData) {
                    isRequestingData = true;
                    findMemberCardByPhone(result);
                }
            }
        }
    }
}
