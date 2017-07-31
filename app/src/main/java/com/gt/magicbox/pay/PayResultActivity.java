package com.gt.magicbox.pay;

import android.content.Intent;
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
import com.gt.magicbox.bean.LoginBean;
import com.gt.magicbox.bean.VoidBean;
import com.gt.magicbox.http.BaseObserver;
import com.gt.magicbox.http.BaseResponse;
import com.gt.magicbox.http.HttpCall;
import com.gt.magicbox.http.RxObservableUtils;
import com.gt.magicbox.login.LoginActivity;
import com.gt.magicbox.setting.printersetting.PrintBean;
import com.gt.magicbox.setting.printersetting.PrinterConnectSerivce;
import com.gt.magicbox.utils.RxBus;
import com.gt.magicbox.utils.SimpleObserver;
import com.gt.magicbox.utils.commonutil.ConvertUtils;
import com.gt.magicbox.utils.commonutil.PhoneUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Description:
 * Created by jack-lin on 2017/7/20 0020.
 */

public class PayResultActivity extends BaseActivity {
    private String TAG="PayResultActivity";
    @BindView(R.id.text_pay_success)
    TextView textPaySuccess;
    @BindView(R.id.payNumber)
    TextView payNumber;
    @BindView(R.id.confirmButton)
    Button confirmButton;
    @BindView(R.id.printButton)
    Button printButton;
    String message;
    public static final int TYPE_QRCODE=0;
    public static final int TYPE_CASH=1;
    private int payType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);
        setToolBarTitle("");
        initView();
        if (payType==TYPE_CASH){
            createCashOrder(message);
        }
    }
    private void initView(){
        if (this.getIntent()!=null){
            boolean success=getIntent().getBooleanExtra("success",true);
            payType=getIntent().getIntExtra("payType",0);
            message=getIntent().getStringExtra("message");
            showMoney(message);
        }
    }
    @OnClick({R.id.confirmButton, R.id.printButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.confirmButton:
                Intent intent=new Intent(PayResultActivity.this,PaymentActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.printButton:
                PrinterConnectSerivce.printReceiptClicked(message);
               // RxBus.get().post(new PrintBean(message));
                break;
        }
    }
    private void showMoney(String numberString){
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        if (numberString.length()!=0) {
            spannableString.append("¥ " + numberString);
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(ConvertUtils.dp2px(35));
            spannableString.setSpan(absoluteSizeSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        payNumber.setText(spannableString);

    }
    private void createCashOrder(String money) {
        HttpCall.getApiService()
                .createCashOrder(PhoneUtils.getIMEI(), money, "2")
                .compose(RxObservableUtils.<BaseResponse<VoidBean>>applySchedulers())//线程处理
                .compose(PayResultActivity.this.<BaseResponse<VoidBean>>bindToLifecycle())//内存泄漏处理
                .subscribe(new BaseObserver<VoidBean>(PayResultActivity.this, true) {
                    @Override
                    public void onSuccess(VoidBean data) {
                        Log.i(TAG, "createCashOrder Success");
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        super.onFailure(code, msg);
                        Log.i(TAG, "onFailure code=" + code + "  msg=" + msg);

                    }
                });
    }
}
