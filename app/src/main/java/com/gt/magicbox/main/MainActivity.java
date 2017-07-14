package com.gt.magicbox.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gt.magicbox.R;
import com.gt.magicbox.http.BaseObserver;
import com.gt.magicbox.http.BaseResponse;
import com.gt.magicbox.http.HttpCall;
import com.gt.magicbox.http.RxObservableUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*HttpCall.getApiService()
                .getZhihu()
                .compose(RxObservableUtils.<BaseResponse<TestBean>>applySchedulers())//线程处理
                .compose(MainActivity.this.<BaseResponse<TestBean>>bindToLifecycle())//内存泄漏处理
                .subscribe(new BaseObserver<TestBean>(MainActivity.this,true) {
                    @Override
                    public void onSuccess(TestBean data) {
                        tv_hello.setText(data.toString());
                    }
                });*/
    }
}
