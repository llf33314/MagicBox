package com.gt.magicbox.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gt.magicbox.R;
import com.gt.magicbox.login.LoginActivity;

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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent=new Intent(LoadingActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }).start();
    }
}
