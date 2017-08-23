package com.gt.magicbox.exchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wzb on 2017/8/23 0023.
 */

public class ShiftExchangeActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_exchange);
        setToolBarTitle("交班");
    }

    @OnClick({R.id.exchange_next, R.id.exchange_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.exchange_next:
                Intent intent=new Intent(ShiftExchangeActivity.this,ExchangeChooseActivity.class);
                startActivity(intent);
                break;
            case R.id.exchange_cancel:
                onBackPressed();
                break;
        }
    }
}
