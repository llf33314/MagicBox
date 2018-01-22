package com.gt.magicbox.coupon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gt.magicbox.Constant;
import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.http.BaseResponse;
import com.gt.magicbox.http.HttpConfig;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.DialogTransformer;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.utils.GlideUtils;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.orhanobut.hawk.Hawk;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wzb on 2017/8/24 0024.
 */

public class CouponQRActivity extends BaseActivity {

    @BindView(R.id.distribute_coupon_qrcode_iv)
    ImageView qrCodeIv;
    @BindView(R.id.distribute_coupon_qr_name)
    TextView qrName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_qr);
        setToolBarTitle("派券");
        init();
    }

    private void init(){
        Intent intent=getIntent();
        int busId= Hawk.get("busId");
        String code=intent.getStringExtra("code");
        qrName.setText(intent.getStringExtra("brandName"));
        String imageUrl= Constant.YJ_BASE_URL+"magicBoxMember/"+busId+"/"+code+"/getQRCode";
        LogUtils.d("imageUrl="+imageUrl);
        //默认是有缓存
        Glide.with(this).load(imageUrl).apply(GlideUtils.getInstance().noneCacheOpt()).into(qrCodeIv);

    }

    @OnClick(R.id.coupon_ok)
    public void onViewClicked() {
        onBackPressed();
    }
}
