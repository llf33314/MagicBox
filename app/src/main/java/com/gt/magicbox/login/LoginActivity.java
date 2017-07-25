package com.gt.magicbox.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.bean.LoginBean;
import com.gt.magicbox.http.BaseObserver;
import com.gt.magicbox.http.BaseResponse;
import com.gt.magicbox.http.HttpCall;
import com.gt.magicbox.http.RxObservableUtils;
import com.gt.magicbox.main.MainActivity;
import com.gt.magicbox.main.MoreActivity;
import com.gt.magicbox.setting.wificonnention.WifiConnectionActivity;
import com.gt.magicbox.utils.commonutil.PhoneUtils;
import com.gt.magicbox.utils.commonutil.RegexUtils;
import com.gt.magicbox.utils.commonutil.SPUtils;
import com.gt.magicbox.utils.commonutil.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:
 * Created by jack-lin on 2017/7/14 0014.
 */


public class LoginActivity extends BaseActivity implements ILoginView {
    private static final String TAG = "LoginActivity";
    @BindView(R.id.userEditText)
    EditText userEditText;
    @BindView(R.id.passwordEditText)
    EditText passwordEditText;
    @BindView(R.id.loginButton)
    Button loginButton;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.netSettingButton)
    Button netSettingButton;
    private ILoginPresenter loginPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginPresenter = new LoginPresenter(this);

    }

    @Override
    public void showProgress(boolean enable) {

    }

    @Override
    public void showLoginView() {
        SPUtils.getInstance().put("LoginSuccess", true);
        Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void login(String userName, String password) {
        HttpCall.getApiService()
                .userLogin(PhoneUtils.getIMEI(), userName, password)
                .compose(RxObservableUtils.<BaseResponse<LoginBean>>applySchedulers())//线程处理
                .compose(LoginActivity.this.<BaseResponse<LoginBean>>bindToLifecycle())//内存泄漏处理
                .subscribe(new BaseObserver<LoginBean>(LoginActivity.this, true) {
                    @Override
                    public void onSuccess(LoginBean data) {
                        Log.i(TAG, "onSuccess data=" + data.token);
                        showLoginView();
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        super.onFailure(code, msg);
                        Log.i(TAG, "onFailure code=" + code + "  msg=" + msg);

                    }
                });
    }

    @OnClick({R.id.loginButton, R.id.netSettingButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                String userName = userEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (TextUtils.isEmpty(userName)) {
                    ToastUtil.getInstance().showToast("账号不能为空");
                    return;
                } else if (TextUtils.isEmpty(password)){
                    ToastUtil.getInstance().showToast("密码不能为空");
                    return;
                }else if (RegexUtils.isZh(userName)||RegexUtils.isZh(password)){
                    ToastUtil.getInstance().showToast("账号密码不能包含汉字");
                    return;
                }
                login(userName,password);
                break;
            case R.id.netSettingButton:
                Intent intent=new Intent(LoginActivity.this,WifiConnectionActivity.class);
                startActivity(intent);
                break;
        }
    }
}
