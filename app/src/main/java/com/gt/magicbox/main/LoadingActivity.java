package com.gt.magicbox.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.gt.magicbox.R;
import com.gt.magicbox.login.LoginActivity;
import com.gt.magicbox.utils.commonutil.SPUtils;

import static java.lang.Thread.sleep;

/**
 * Description:
 * Created by jack-lin on 2017/7/17 0017.
 */

public class LoadingActivity extends Activity{
    private boolean isCreate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!isCreate)
                    sleep(2000);
                    isCreate=true;
                    String token = SPUtils.getInstance().getString("token", "");
                    Intent intent;
                    intent = !TextUtils.isEmpty(token) ? new Intent(LoadingActivity.this, MainActivity.class)
                            : new Intent(LoadingActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
