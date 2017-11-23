package com.gt.magicbox.custom_display;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:
 * Created by jack-lin on 2017/11/23 0023.
 * Buddha bless, never BUG!
 */

public class MatchSuccessActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_finish);
    }

    @OnClick(R.id.step02_next)
    public void onViewClicked() {
        finish();
    }
}
