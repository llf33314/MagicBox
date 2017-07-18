package com.gt.magicbox.wificonnention;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.http.RxObservableUtils;
import com.gt.magicbox.utils.commonutil.ToastUtil;
import com.gt.magicbox.wificonnention.model.WifiBean;
import com.gt.magicbox.wificonnention.presenter.WifiConnectionPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by wzb on 2017/7/17 0017.
 */

public class JoinWifiDialog extends Dialog {

    private final String TAG=JoinWifiDialog.class.getSimpleName();

    @BindView(R.id.join_wifi_ssid)
    TextView joinWifiSsid;
    @BindView(R.id.join_wifi_psd)
    EditText joinWifiPsd;
    @BindView(R.id.join_wifi_cancel)
    Button joinWifiCancel;
    @BindView(R.id.join_wifi_join)
    Button joinWifiJoin;

    private WifiBean wifiBean;

    WifiConnectionPresenter presenter;

    /**
     * 0:已连接  1：已保存 2：未连接
     */
    private int uiType=0;

    public JoinWifiDialog(@NonNull Context context) {
        super(context);
    }

    public JoinWifiDialog(@NonNull Context context, @StyleRes int themeResId, WifiConnectionPresenter p) {
        super(context, themeResId);
        this.presenter=p;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_join_wifi);
        ButterKnife.bind(this);

        joinWifiPsd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                joinWifiJoin.setEnabled(s.length() >= 8);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //
    public void show(WifiBean wifiBean,int uiType){
        show();
        this.uiType=uiType;
        this.wifiBean=wifiBean;
        joinWifiSsid.setText(this.wifiBean.getName());

        switch (uiType){
            case 0:
                joinWifiPsd.setVisibility(View.GONE);
                joinWifiCancel.setText("取消");
                joinWifiCancel.setTextColor(getContext().getResources().getColor(R.color.theme_orange));
                joinWifiCancel.setBackgroundResource(R.drawable.btn_orange_borrer_and_white);
                joinWifiJoin.setText("取消保存");
                joinWifiJoin.setEnabled(true);
                break;
            case 1:
                joinWifiPsd.setVisibility(View.GONE);
                joinWifiCancel.setText("取消保存");
                joinWifiCancel.setTextColor(getContext().getResources().getColor(R.color.white));
                joinWifiCancel.setBackgroundResource(R.drawable.selector_red_button);
                joinWifiJoin.setText("连接");
                joinWifiJoin.setEnabled(true);
                break;
            case 2:
                joinWifiPsd.setVisibility(View.VISIBLE);
                joinWifiCancel.setText("取消");
                joinWifiCancel.setTextColor(getContext().getResources().getColor(R.color.theme_orange));
                joinWifiCancel.setBackgroundResource(R.drawable.btn_orange_borrer_and_white);
                joinWifiJoin.setEnabled(joinWifiPsd.getText().length()>=8);
                joinWifiJoin.setText("确定");
                break;
        }
    }

    @OnClick({R.id.join_wifi_cancel, R.id.join_wifi_join})
    public void onViewClicked(View view) {
        String viewText=((Button)view).getText().toString();
        switch (view.getId()) {
            case R.id.join_wifi_cancel:
                if ("取消保存".equals(viewText)){ //已保存未连接
                    presenter.removeNetworkConfig(wifiBean.getName());
                }
                break;
            case R.id.join_wifi_join:

                if("取消保存".equals(viewText)){ //已连接
                    presenter.removeNetworkAndDisConnect(wifiBean.getName());
                }else if("连接".equals(viewText)){ //已保存未连接
                    conncetionWifi();
                }else if("确定".equals(viewText)){

                    String psd=joinWifiPsd.getText().toString().trim();

                    int result=-2;

                    switch (wifiBean.getLockType()){
                        case 1:
                            result= presenter.addWEPNetwork(wifiBean.getName(),psd);
                            break;
                        case 2:
                        case 3:
                            result= presenter.addWPA2Network(wifiBean.getName(),psd);
                            break;
                    }
                    //-1 密码错误 0 密码错误跟密码正确都是0   3 成功  -1001 已经添加过  -1000账号密码为空   1又是什么鬼 2正确错误都是2貌似已经在连接的时候请求就是2
                    presenter.disconnectFromCurrentNetwork();
                    conncetionWifi();

                    /*switch (result){
                        case -1001:
                            ToastUtil.getInstance().showToast("已保存");
                            break;
                        case -1:
                        case -1000:
                            ToastUtil.getInstance().showToast("密码有误");
                            break;
                        case 0:
                        case 2:   //这里都去连接
                        case 3:
                            Log.i(TAG," 0,跑到3了");
                            conncetionWifi();
                            break;
                        default:
                            ToastUtil.getInstance().showToast("连接失败! "+result);
                            break;
                    }*/
                }
                break;
        }
        //刷新wifi界面
        presenter.scanWifi();
        dismiss();

    }

    private void conncetionWifi(){

        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(presenter.connectToNetwork(wifiBean.getName(),30*1000));
            }
        })
                .compose(RxObservableUtils.<Boolean>applySchedulers())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Boolean s) {
                      //  presenter.scanWifi();
                        //调试使用 不要弹出
                        //ToastUtil.getInstance().showToast(s?"已连上"+wifiBean.getName():"连接"+wifiBean.getName()+"失败");
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
