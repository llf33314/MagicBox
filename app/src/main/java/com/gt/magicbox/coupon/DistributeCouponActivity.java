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
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(ConvertUtils.dp2px(10)));
        //访问网络返回来的ArrayList
        DistributeCouponAdapter adapter=new DistributeCouponAdapter(this,new ArrayList<DistributeCouponBean>());
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
