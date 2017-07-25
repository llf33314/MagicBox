package com.gt.magicbox.pay;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.utils.RxBus;
import com.gt.magicbox.utils.commonutil.ConvertUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:
 * Created by jack-lin on 2017/7/20 0020.
 */

public class PayResultActivity extends BaseActivity {
    @BindView(R.id.text_pay_success)
    TextView textPaySuccess;
    @BindView(R.id.payNumber)
    TextView payNumber;
    @BindView(R.id.confirmButton)
    Button confirmButton;
    @BindView(R.id.printButton)
    Button printButton;

    String message;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);
        setToolBarTitle("");
        initView();
    }
    private void initView(){
        if (this.getIntent()!=null){
            boolean success=getIntent().getBooleanExtra("success",true);
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
                RxBus.get().post(message);
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
}
