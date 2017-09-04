package com.gt.magicbox.coupon;

import android.content.Context;

import com.gt.magicbox.R;
import com.gt.magicbox.base.recyclerview.BaseRecyclerAdapter;
import com.gt.magicbox.base.recyclerview.BaseViewHolder;
import com.gt.magicbox.bean.DistributeCouponBean;

import java.util.List;

/**
 * Created by wzb on 2017/8/24 0024.
 */

public class HorizontalCouponAdapter extends BaseRecyclerAdapter<DistributeCouponBean> {

    public HorizontalCouponAdapter(Context context, List<DistributeCouponBean> listBean) {
        super(context, listBean);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_small_coupon;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, DistributeCouponBean bean, int position) {
        holder.setText(R.id.item_name,"满¥100减¥20");
        //根据接口返回数据 bean绑定holder
    }
}
