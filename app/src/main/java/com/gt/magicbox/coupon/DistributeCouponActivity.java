package com.gt.magicbox.coupon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.recyclerview.BaseRecyclerAdapter;
import com.gt.magicbox.base.recyclerview.SpaceItemDecoration;
import com.gt.magicbox.bean.DistributeCouponBean;
import com.gt.magicbox.bean.DistributeCouponMainBean;
import com.gt.magicbox.bean.DuofenCards;
import com.gt.magicbox.http.BaseResponse;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.DialogTransformer;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.utils.commonutil.ConvertUtils;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by wzb on 2017/8/24 0024.
 */

public class DistributeCouponActivity extends BaseActivity {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private DistributeCouponMainBean distributeCouponMainBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_recycler_view);
        setToolBarTitle("派发优惠券");
        init();
    }
    private void init(){
        distributeCouponMainBean= (DistributeCouponMainBean)getIntent().getSerializableExtra("distributeCouponMainBean");
        int receiveId=distributeCouponMainBean.getId();

        /*mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);*/



        HttpCall.getApiService().getDistributeCoupon(receiveId)
                .compose(ResultTransformer.<DuofenCards>rxActivityTransformer(this))
                .compose(new DialogTransformer().<DuofenCards>transformer())
                .subscribe(new BaseObserver<DuofenCards>() {
                    @Override
                    protected void onSuccess(final DuofenCards duofenCards) {

                        LinearLayoutManager layoutManager = new LinearLayoutManager(DistributeCouponActivity.this);
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        mRecyclerView.setLayoutManager(layoutManager);

                        DistributeCouponAdapter adapter=new DistributeCouponAdapter(DistributeCouponActivity.this,duofenCards.getDuofencards());
                        mRecyclerView.setAdapter(adapter);
                        mRecyclerView.addItemDecoration(new SpaceItemDecoration(ConvertUtils.dp2px(10),SpaceItemDecoration.SPACE_BOTTOM));
                        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                            @Override
                            public void onClick(View view, Object item, int position) {
                                //获取点击Object 传到二维码页面
                                Intent intent=new Intent(DistributeCouponActivity.this,CouponQRActivity.class);
                                intent.putExtra("code",duofenCards.getReceives().getCode());
                                intent.putExtra("brandName",distributeCouponMainBean.getCardsName());
                                startActivity(intent);
                            }
                        });
                    }
                });
    }
}
