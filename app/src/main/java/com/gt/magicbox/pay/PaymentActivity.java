package com.gt.magicbox.pay;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.bean.MemberCardBean;
import com.gt.magicbox.coupon.VerificationActivity;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.main.MainActivity;
import com.gt.magicbox.member.MemberRechargeActivity;
import com.gt.magicbox.utils.commonutil.AppManager;
import com.gt.magicbox.utils.commonutil.ToastUtil;
import com.gt.magicbox.webview.WebViewActivity;
import com.orhanobut.hawk.Hawk;

/**
 * Description:
 * Created by jack-lin on 2017/7/18 0018.
 */

public class PaymentActivity extends BaseActivity {
    private final String TAG=PaymentActivity.class.getSimpleName();
    private GridView gridView;
    private KeyboardView keyboardView;
    private int type=0;
    private double orderMoney=0;
    public static final int TYPE_INPUT=0;
    public static final int TYPE_CALC=1;
    public static final int TYPE_MEMBER_PAY=2;
    public static final int TYPE_COUPON_VERIFICATION=3;
    public static final int TYPE_MEMBER_RECHARGE=4;

    private int code;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initView();
    }

    private void initView() {
        if (getIntent()!=null){
            type=getIntent().getIntExtra("type",0);
            orderMoney=getIntent().getDoubleExtra("orderMoney",0);
            code=getIntent().getIntExtra("keyCode",0);
        }
        if (type == TYPE_CALC) {
            setToolBarTitle("现金支付");
        } else if (type == TYPE_INPUT) {
            setToolBarTitle("收银");
        } else if (type == TYPE_MEMBER_PAY) {
            setToolBarTitle("会员收银");
        }else if (type==TYPE_COUPON_VERIFICATION){
            setToolBarTitle("优惠券核销");
        }else if (type==TYPE_MEMBER_RECHARGE){
            setToolBarTitle("会员卡充值");
        }
        keyboardView = (KeyboardView) findViewById(R.id.keyboard);
        keyboardView.setOrderMoney(orderMoney);
        keyboardView.setKeyboardType(type);
        keyboardView.setOnKeyboardDoListener(new OnKeyboardDoListener() {
            @Override
            public void onPay(double money) {
                Intent intent;
                if (type == TYPE_INPUT) {
                    intent = new Intent(PaymentActivity.this, ChosePayModeActivity.class);
                    intent.putExtra("money", money);
                    startActivity(intent);
                } else if (type == TYPE_CALC) {
                        intent=new Intent(PaymentActivity.this, PayResultActivity.class);
                        intent.putExtra("success",true);
                        intent.putExtra("message",""+money);
                        intent.putExtra("payType",PayResultActivity.TYPE_CASH);
                        startActivity(intent);
                        AppManager.getInstance().finishActivity(PaymentActivity.class);
                        AppManager.getInstance().finishActivity(ChosePayModeActivity.class);
                }else if (type==TYPE_MEMBER_PAY){
                    intent=new Intent(PaymentActivity.this, VerificationActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onMemberPay(double money) {
                Intent intent=new Intent(PaymentActivity.this,PaymentActivity.class);
                intent.putExtra("type",2);
                intent.putExtra("money",money);
                startActivity(intent);
            }

            @Override
            public void onNumberInput(String num) {
                if (type == TYPE_MEMBER_RECHARGE) {
                    findMemberCardByPhone(num);
                }
            }
        });
        if (code>0)onKeyDown(code,null);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyboardView!=null)
        if (keyCode>=KeyEvent.KEYCODE_NUMPAD_0&&keyCode<=KeyEvent.KEYCODE_NUMPAD_9){
            keyboardView.input(""+(keyCode-144));
            return true;
        }else if (keyCode==KeyEvent.KEYCODE_NUMPAD_DOT){
            keyboardView.input(".");
            return true;
        }else if (keyCode==KeyEvent.KEYCODE_DEL){
            keyboardView.backspace();
            return true;
        }else if (keyCode==KeyEvent.KEYCODE_NUMPAD_ENTER){
            keyboardView.enter();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void findMemberCardByPhone(final String phone) {
        if (phone.length()!=11){
            ToastUtil.getInstance().showToast("请输入11位手机号码");
            return;
        }
        HttpCall.getApiService()
                .findMemberCardByPhone((Integer) Hawk.get("busId"), phone)
                .compose(ResultTransformer.<MemberCardBean>transformer())//线程处理 预处理
                .subscribe(new BaseObserver<MemberCardBean>() {

                    @Override
                    protected void onSuccess(MemberCardBean bean) {

                        Log.d(TAG, "findMemberCardByPhone onSuccess");
                        ToastUtil.getInstance().showToast("该手机已领取过会员卡");
                        Intent intent=new Intent(getApplicationContext(), MemberRechargeActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    protected void onFailure(int code, String msg) {
                        super.onFailure(code, msg);
                        Log.d(TAG, "findMemberCardByPhone onFailure msg=" + msg.toString());
                        if (!TextUtils.isEmpty(msg) && msg.equals("数据不存在")) {
                        }

                    }

                });
    }
}
