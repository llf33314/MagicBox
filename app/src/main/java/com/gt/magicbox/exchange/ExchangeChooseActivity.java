package com.gt.magicbox.exchange;

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

public class ExchangeChooseActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_choose);
        ButterKnife.bind(this);
        setToolBarTitle("交班");
    }

    @OnClick({R.id.exchange_choose_ok, R.id.exchange_choose_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.exchange_choose_ok:
                break;
            case R.id.exchange_choose_cancel:
                onBackPressed();
                break;
        }
    }
}
