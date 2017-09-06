package com.gt.magicbox.coupon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.recyclerview.BaseRecyclerAdapter;
import com.gt.magicbox.base.recyclerview.SpaceItemDecoration;
import com.gt.magicbox.bean.DistributeCouponBean;
import com.gt.magicbox.utils.commonutil.ConvertUtils;
import com.gt.magicbox.utils.commonutil.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Description:
 * Created by jack-lin on 2017/8/29 0029.
 */

public class VerificationActivity extends BaseActivity {
    @BindView(R.id.image_head)
    ImageView imageHead;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.card_type)
    TextView cardType;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.right)
    TextView right;
    @BindView(R.id.member_info_layout)
    RelativeLayout memberInfoLayout;
    @BindView(R.id.order_money)
    TextView orderMoney;
    @BindView(R.id.coupon_view)
    RecyclerView couponView;
    @BindView(R.id.fen_coin_view)
    RecyclerView fenCoinView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        ButterKnife.bind(this);
        initRecyclerView(couponView,HorizontalCouponAdapter.TYPE_COUPON);
        initRecyclerView(fenCoinView,HorizontalCouponAdapter.TYPE_FEN_COIN);

    }
    private void initRecyclerView(RecyclerView recyclerView,int type){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        final List<DistributeCouponBean> lists= new ArrayList<DistributeCouponBean>();
        Observable.range(0,9).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                lists.add(new DistributeCouponBean("name:"+integer));
            }
        });
        final HorizontalCouponAdapter adapter=new HorizontalCouponAdapter(this,lists,type);
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, Object item, int position) {
                ToastUtil.getInstance().showToast("position="+position);
                        for(DistributeCouponBean bean:lists){
                            bean.setSelected(false);
                }
                lists.get(position).setSelected(true);
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(ConvertUtils.dp2px(5),SpaceItemDecoration.SPACE_RIGHT));

    }
}
