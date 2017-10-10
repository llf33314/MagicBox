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

import com.gt.magicbox.Constant;
import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.BaseConstant;
import com.gt.magicbox.bean.LoginBean;
import com.gt.magicbox.bean.MemberBean;
import com.gt.magicbox.bean.ShopInfoBean;
import com.gt.magicbox.http.BaseResponse;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.DialogTransformer;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.main.MainActivity;
import com.gt.magicbox.main.MoreActivity;
import com.gt.magicbox.main.NormalDialog;
import com.gt.magicbox.setting.wificonnention.WifiConnectionActivity;
import com.gt.magicbox.utils.commonutil.PhoneUtils;
import com.gt.magicbox.utils.commonutil.RegexUtils;
import com.gt.magicbox.utils.commonutil.SPUtils;
import com.gt.magicbox.utils.commonutil.ToastUtil;
import com.gt.magicbox.widget.HintDismissDialog;
import com.gt.magicbox.widget.LoadingProgressDialog;
import com.gt.magicbox.widget.ManualDialog;
import com.orhanobut.hawk.Hawk;

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
    private String token="";
    private LoadingProgressDialog loadingProgressDialog;
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
        Hawk.put("token",token);
        Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void login(final String userName, final String password) {
        loadingProgressDialog =new LoadingProgressDialog(LoginActivity.this,"登录中...");
        HttpCall.getApiService()
                .userLogin(PhoneUtils.getIMEI(), userName, password)
                .compose(ResultTransformer.<LoginBean>transformer())//线程处理 预处理
                .subscribe(new BaseObserver<LoginBean>() {
                    @Override
                    public void onSuccess(LoginBean data) {
                        Log.i(TAG,"data check="+data.checkType);
                        if (data!=null){
                            if (data.checkType==0) {
                                Hawk.put("userName", userName);
                                Hawk.put("eqId", data.eqId);
                                memberQuery(userName, password);
                            }else if (data.checkType==1){
                                final NormalDialog dialog=new NormalDialog(LoginActivity.this,
                                        "该设备已绑定其他账号   \n是否进行改绑并登陆",R.style.HttpRequestDialogStyle);
                                dialog.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        changeDevicesBind(userName,password);
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        if (code==999){
                            new HintDismissDialog(LoginActivity.this,"账号或密码错误").show();
                        }
                        super.onFailure(code, msg);
                    }
                });
    }
    private void changeDevicesBind(final String userName, final String password){
        HttpCall.getApiService()
                .changeBind(PhoneUtils.getIMEI(),userName ,password)
                .compose(ResultTransformer.<BaseResponse>transformerNoData())//线程处理 预处理
                .compose(new DialogTransformer().<BaseResponse>transformer()) //显示对话框
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    protected void onSuccess(BaseResponse baseResponse) {
                        Log.i(TAG, "changeDevicesBind Success");
                        login(userName,password);

                    }

                    @Override
                    protected void onFailure(int code, String msg) {
                        Log.i(TAG, "onFailure code=" + code + "  msg=" + msg);
                    }
                });
    }
    private void memberQuery(String userName, String password) {
        HttpCall.getApiService()
                .memberQuery( userName, password)
                .compose(ResultTransformer.<ShopInfoBean>transformer())//线程处理 预处理
                .subscribe(new BaseObserver<ShopInfoBean>() {
                    @Override
                    public void onSuccess(ShopInfoBean data) {
                        if (data!=null){
                            Hawk.put("ShopInfoBean",data);
                            Hawk.put("busId",data.getBusId());
                            Hawk.put("shopName",data.getShopName());
                            Hawk.put("shopId",data.getShops().getId());
                            ShopInfoBean bean=(ShopInfoBean)Hawk.get("ShopInfoBean");
                            Log.i(TAG,"data name="+bean.getShopName());
                        }
                        if (loadingProgressDialog!=null)loadingProgressDialog.dismiss();
                        showLoginView();
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        super.onFailure(code, msg);
                    }
                });
    }
    
    @OnClick({R.id.loginButton, R.id.netSettingButton,R.id.manual})
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
            case R.id.manual:
                new ManualDialog(LoginActivity.this).show();
                break;
        }
    }
}
