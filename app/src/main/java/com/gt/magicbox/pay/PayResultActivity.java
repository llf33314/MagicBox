package com.gt.magicbox.pay;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.bean.CardTypeInfoBean;
import com.gt.magicbox.bean.CashOrderBean;
import com.gt.magicbox.bean.LoginBean;
import com.gt.magicbox.http.BaseResponse;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.DialogTransformer;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.setting.printersetting.PrinterConnectService;
import com.gt.magicbox.utils.commonutil.ConvertUtils;
import com.gt.magicbox.utils.commonutil.PhoneUtils;
import com.orhanobut.hawk.Hawk;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Description:
 * Created by jack-lin on 2017/7/20 0020.
 */

public class PayResultActivity extends BaseActivity {
    private String TAG = "PayResultActivity";
    @BindView(R.id.text_pay_success)
    TextView textPaySuccess;
    @BindView(R.id.payNumber)
    TextView payNumber;
    @BindView(R.id.confirmButton)
    Button confirmButton;
    @BindView(R.id.printButton)
    Button printButton;
    String message;
    String orderNo;
    CashOrderBean cashOrderBean;
    public static final int TYPE_QRCODE_WECHAT = 0;
    public static final int TYPE_QRCODE_ALIPAY = 1;
    public static final int TYPE_CASH = 2;
    private int payType;
    private MediaPlayer mp = new MediaPlayer();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);
        playSound();
        setToolBarTitle("");
        initView();
        if (payType == TYPE_CASH) {
            createCashOrder(message);
        }
    }

    private void initView() {
        if (this.getIntent() != null) {
            boolean success = getIntent().getBooleanExtra("success", true);
            payType = getIntent().getIntExtra("payType", 0);
            message = getIntent().getStringExtra("message");
            orderNo=getIntent().getStringExtra("orderNo");
            showMoney(message);
        }
    }

    @OnClick({R.id.confirmButton, R.id.printButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.confirmButton:
                Intent intent = new Intent(PayResultActivity.this, PaymentActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.printButton:
                if (payType==TYPE_CASH){
                    if (cashOrderBean!=null)
                    orderNo=cashOrderBean.getMagicBoxOrder().getOrderNo();
                    else orderNo="";
                }
                PrinterConnectService.printEsc0829(orderNo,message+"元", payType);
                // RxBus.get().post(new PrintBean(message));
                break;
        }
    }

    private void showMoney(String numberString) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        if (numberString.length() != 0) {
            spannableString.append("¥ " + numberString);
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(ConvertUtils.dp2px(35));
            spannableString.setSpan(absoluteSizeSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        payNumber.setText(spannableString);

    }

    private void createCashOrder(String money) {
        Integer shiftId = Hawk.get("shiftId");
        if (shiftId == null || shiftId < 0) shiftId = 0;
        HttpCall.getApiService()
                .createCashOrder(PhoneUtils.getIMEI(), money, 2, shiftId)
                .compose(ResultTransformer.<CashOrderBean>transformer())//线程处理 预处理
                .compose(new DialogTransformer().<CashOrderBean>transformer()) //显示对话框
                .subscribe(new BaseObserver<CashOrderBean>()  {
                    @Override
                    protected void onSuccess(CashOrderBean bean) {
                        Log.i(TAG, "createCashOrder Success");
                        cashOrderBean=bean;
                    }

                    @Override
                    protected void onFailure(int code, String msg) {
                        Log.i(TAG, "onFailure code=" + code + "  msg=" + msg);
                    }
                });
    }

    private void playSound() {
        try {
            mp = MediaPlayer.create(getApplicationContext(), R.raw.success);
            mp.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }
}
