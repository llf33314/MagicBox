package com.gt.magicbox.coupon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wzb on 2017/8/24 0024.
 */

public class CouponQRActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_qr);
        setToolBarTitle("派券");
    }

    @OnClick(R.id.coupon_ok)
    public void onViewClicked() {
        onBackPressed();
    }
}
