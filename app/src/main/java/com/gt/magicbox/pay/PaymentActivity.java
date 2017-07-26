package com.gt.magicbox.pay;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.main.MainActivity;
import com.gt.magicbox.utils.commonutil.AppManager;
import com.gt.magicbox.webview.WebViewActivity;

/**
 * Description:
 * Created by jack-lin on 2017/7/18 0018.
 */

public class PaymentActivity extends BaseActivity {
    private GridView gridView;
    private KeyboardView keyboardView;
    private int type=0;
    private double orderMoney=0;
    public static final int TYPE_INPUT=0;
    public static final int TYPE_CALC=1;

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
                }
            }
        });
    }
}
