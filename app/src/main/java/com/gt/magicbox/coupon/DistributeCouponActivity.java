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
import com.gt.magicbox.utils.commonutil.ConvertUtils;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_recycler_view);
        setToolBarTitle("派发优惠券");
        init();

    }
    private void init(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        /*mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);*/

        //访问网络返回来的ArrayList
        final List<DistributeCouponBean> lists= new ArrayList<DistributeCouponBean>();
        Observable.range(0,900).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                lists.add(new DistributeCouponBean("¥100满减券（满500减100）:"+integer));
            }
        });

        DistributeCouponAdapter adapter=new DistributeCouponAdapter(this,lists);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(ConvertUtils.dp2px(5),SpaceItemDecoration.SPACE_BOTTOM));

        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, Object item, int position) {
                //获取点击Object 传到二维码页面
                Intent intent=new Intent(DistributeCouponActivity.this,CouponQRActivity.class);
                startActivity(intent);
            }
        });
    }
}
