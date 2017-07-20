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

/**
 * Description:
 * Created by jack-lin on 2017/7/18 0018.
 */

public class PaymentActivity extends BaseActivity {
    private GridView gridView;
    private KeyboardView keyboardView;
    private int type=0;
    public static final int TYPE_INPUT=0;
    public static final int TYPE_CALC=1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initView();
    }

    private void initView() {
        keyboardView = (KeyboardView) findViewById(R.id.keyboard);
        keyboardView.setOnKeyboardDoListener(new OnKeyboardDoListener() {
            @Override
            public void onPay(double money) {
               Intent intent = new Intent(PaymentActivity.this, ChosePayModeActivity.class);
               intent.putExtra("money",money);
               startActivity(intent);
            }
        });
    }
}
