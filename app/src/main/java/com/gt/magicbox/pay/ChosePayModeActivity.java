package com.gt.magicbox.pay;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.webview.WebViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:
 * Created by jack-lin on 2017/7/19 0019.
 */

public class ChosePayModeActivity extends BaseActivity {
    @BindView(R.id.pay_wechat)
    RelativeLayout payWechat;
    @BindView(R.id.pay_zfb)
    RelativeLayout payZfb;
    @BindView(R.id.pay_cash)
    RelativeLayout payCash;
    private double money;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_pay);
        setToolBarTitle("选择支付方式");
        if (this.getIntent()!=null){
            money=this.getIntent().getDoubleExtra("money",0);
        }
    }

    @OnClick({R.id.pay_wechat, R.id.pay_zfb, R.id.pay_cash})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pay_wechat:
                startERCodePay(0);
                break;
            case R.id.pay_zfb:
                startERCodePay(1);
                break;
            case R.id.pay_cash:
                break;
        }
    }
    private void startERCodePay(int type){
        Intent intent=new Intent(ChosePayModeActivity.this, WebViewActivity.class);
        intent.putExtra("webType",WebViewActivity.WEB_TYPE_PAY);
        intent.putExtra("money",money);
        intent.putExtra("payMode",type);
        startActivity(intent);
    }
}
