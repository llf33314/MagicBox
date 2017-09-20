package com.gt.magicbox.member;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.bean.CardTypeInfoBean;
import com.gt.magicbox.bean.OrderListResultBean;
import com.gt.magicbox.http.BaseResponse;
import com.gt.magicbox.http.HttpRequestDialog;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.utils.commonutil.ToastUtil;
import com.gt.magicbox.widget.WheelDialog;
import com.gt.magicbox.widget.WheelViewDialog;
import com.orhanobut.hawk.Hawk;
import com.suke.widget.SwitchButton;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:
 * Created by jack-lin on 2017/9/19 0019.
 * Buddha bless, never BUG!
 */

public class AddMemberActivity extends BaseActivity {
    private static final String TAG=AddMemberActivity.class.getSimpleName();
    @BindView(R.id.textFollow)
    TextView textFollow;
    @BindView(R.id.switch_button)
    SwitchButton switchButton;
    @BindView(R.id.imgQRCode)
    ImageView imgQRCode;
    @BindView(R.id.textTip)
    TextView textTip;
    @BindView(R.id.followLayout)
    RelativeLayout followLayout;
    @BindView(R.id.memberTypeLayout)
    RelativeLayout memberTypeLayout;
    HttpRequestDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_add);
        initData();
        initView();
    }
    private void initData(){
        getMemberCardType();
    }
    private void getMemberCardType(){
        HttpCall.getApiService()
                .findMemberCardType((Integer) Hawk.get("busId"))
                .compose(ResultTransformer.<CardTypeInfoBean>transformer())//线程处理 预处理
                .subscribe(new BaseObserver<CardTypeInfoBean>(){

                    @Override
                    protected void onSuccess(CardTypeInfoBean bean) {
                        dialog.dismiss();
                        Log.d(TAG,"onSuccess");
                    }

                    @Override
                    protected void onFailure(int code, String msg) {
                        super.onFailure(code, msg);
                        dialog.dismiss();
                        Log.d(TAG,"onFailure");

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dialog.dismiss();
                        Log.d(TAG,"onError");


                    }
                });
    }
    private void initView() {
        dialog=new HttpRequestDialog();
        dialog.show();
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    imgQRCode.setVisibility(View.VISIBLE);
                    textTip.setVisibility(View.VISIBLE);
                } else {
                    imgQRCode.setVisibility(View.GONE);
                    textTip.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick(R.id.memberTypeLayout)
    public void onViewClicked() {
        showDialog();
    }
    public void showDialog() {
        WheelDialog wheelDialog=new WheelDialog(this, R.style.ShortcutMenuDialog);
        wheelDialog.setList(createArrays());
        wheelDialog.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                ToastUtil.getInstance().showToast("position="+position);
            }
        });
        wheelDialog.show();
    }
    private ArrayList<String> createArrays() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 4; i++) {
            list.add("折扣卡" + i);
        }
        return list;
    }
}
