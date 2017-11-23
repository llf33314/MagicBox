package com.gt.magicbox.coupon;

import android.content.Context;

import com.gt.magicbox.R;
import com.gt.magicbox.base.recyclerview.BaseRecyclerAdapter;
import com.gt.magicbox.base.recyclerview.BaseViewHolder;
import com.gt.magicbox.bean.DistributeCouponMainBean;
import com.gt.magicbox.utils.commonutil.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by wzb on 2017/11/21 0021.
 */

public class DistributeCouponMainAdapter extends BaseRecyclerAdapter<DistributeCouponMainBean> {
    SimpleDateFormat format= new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

    public DistributeCouponMainAdapter(Context context, List<DistributeCouponMainBean> listBean) {
        super(context, listBean);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_distribute_coupon_main;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, DistributeCouponMainBean bean, int position) {
        holder.setText(R.id.distribute_coupon_main_name,bean.getCardsName())
                .setText(R.id.distribute_coupon_main_time,"领取结束时间:"+TimeUtils.millis2String(bean.getReceiveDate(),format));
    }
}
