package com.gt.magicbox.extension.fixed_money;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.extension.ExtensionActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:
 * Created by jack-lin on 2018/1/24 0024.
 * Buddha bless, never BUG!
 */

public class FixedMoneyNoDataActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixed_money_no_data);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.addFixedMoney)
    public void onViewClicked() {
        Intent intent = new Intent(FixedMoneyNoDataActivity.this, FixedMoneySettingActivity.class);
        startActivity(intent);
    }
}
