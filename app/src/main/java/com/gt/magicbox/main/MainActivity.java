package com.gt.magicbox.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.bean.LoginBean;
import com.gt.magicbox.http.BaseObserver;
import com.gt.magicbox.http.BaseResponse;
import com.gt.magicbox.http.HttpCall;
import com.gt.magicbox.http.RxObservableUtils;

public class MainActivity extends BaseActivity {
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolBarTitle("主页");

    }

    public void request(View view) {
        HttpCall.getApiService()
                .userLogin("123456", "jindao", "gt123456")
                .compose(RxObservableUtils.<BaseResponse<LoginBean>>applySchedulers())//线程处理
                .compose(MainActivity.this.<BaseResponse<LoginBean>>bindToLifecycle())//内存泄漏处理
                .subscribe(new BaseObserver<LoginBean>(MainActivity.this,true) {
                    @Override
                    public void onSuccess(LoginBean data) {
                        Log.i(TAG, "data=" + data.toString());
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        super.onFailure(code, msg);
                        Log.i(TAG, "code=" + code + "  msg=" + msg);
                    }
                });
    }
}
