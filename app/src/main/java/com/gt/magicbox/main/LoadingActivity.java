package com.gt.magicbox.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.gt.magicbox.R;
import com.gt.magicbox.login.LoginActivity;
import com.gt.magicbox.utils.commonutil.SPUtils;
import com.orhanobut.hawk.Hawk;

import static java.lang.Thread.sleep;

/**
 * Description:
 * Created by jack-lin on 2017/7/17 0017.
 */

public class LoadingActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                    Integer busId = Hawk.get("busId");
                    Intent intent;
                    intent =(busId!=null&&busId>0)? new Intent(LoadingActivity.this, MainActivity.class)
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
