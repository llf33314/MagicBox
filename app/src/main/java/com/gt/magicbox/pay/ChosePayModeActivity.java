package com.gt.magicbox.pay;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.main.MainActivity;
import com.gt.magicbox.main.MoreFunctionDialog;
import com.gt.magicbox.setting.wificonnention.WifiConnectionActivity;
import com.gt.magicbox.utils.NetworkUtils;
import com.gt.magicbox.webview.WebViewActivity;
import com.orhanobut.hawk.Hawk;

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
    private MoreFunctionDialog dialog;
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
                Intent intent=new Intent(ChosePayModeActivity.this,PaymentActivity.class);
                intent.putExtra("type",1);
                intent.putExtra("orderMoney",money);
                startActivity(intent);
                break;
        }
    }

    /**
     * @param type 0-微信，1-支付宝
     */
    private void startERCodePay(int type){
        if (NetworkUtils.isConnected()) {
            Hawk.put("payType",type);
            Intent intent = new Intent(ChosePayModeActivity.this, QRCodePayActivity.class);
            intent.putExtra("money", money);
            intent.putExtra("payMode", type);
            startActivity(intent);
        }else {
            if (dialog==null){
                dialog=new MoreFunctionDialog(ChosePayModeActivity.this,"没有网络，请连接后重试",R.style.HttpRequestDialogStyle);
                dialog.getConfirmButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Intent intent=new Intent(ChosePayModeActivity.this,WifiConnectionActivity.class);
                        startActivity(intent);
                    }
                });
            }
            dialog.show();
        }
    }
}
