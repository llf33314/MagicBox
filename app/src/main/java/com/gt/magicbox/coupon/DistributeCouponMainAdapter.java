package com.gt.magicbox.coupon;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.gt.magicbox.R;
import com.gt.magicbox.base.recyclerview.BaseRecyclerAdapter;
import com.gt.magicbox.base.recyclerview.BaseViewHolder;
import com.gt.magicbox.bean.DistributeCouponMainBean;
import com.gt.magicbox.bean.DuofenCards;
import com.gt.magicbox.utils.commonutil.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by wzb on 2017/11/21 0021.
 */

public class DistributeCouponMainAdapter extends BaseRecyclerAdapter<DistributeCouponMainBean> {
    SimpleDateFormat format= new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
    private View.OnClickListener leftDetailListener;
    private View.OnClickListener rightDistributeListener;

    public DistributeCouponMainAdapter(final Context context, List<DistributeCouponMainBean> listBean) {
        super(context, listBean);
        leftDetailListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取点击Object 传到二维码页面
                Intent intent=new Intent(context,DistributeCouponActivity.class);
                DistributeCouponMainBean bean= (DistributeCouponMainBean) v.getTag();
              //  int receiveId=bean.getId();
                intent.putExtra("distributeCouponMainBean",bean);
               // intent.putExtra("receiveId",receiveId);
                context.startActivity(intent);
            }
        };

        rightDistributeListener=new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //获取点击Object 传到二维码页面
                Intent intent=new Intent(context,CouponQRActivity.class);
                DistributeCouponMainBean bean= (DistributeCouponMainBean) v.getTag();
                intent.putExtra("code",bean.getCode());
                intent.putExtra("brandName",bean.getCardsName());
                context.startActivity(intent);
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_distribute_coupon_main;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, DistributeCouponMainBean bean, int position) {
        holder.setText(R.id.distribute_coupon_main_name,bean.getCardsName())
                .setText(R.id.distribute_coupon_main_time,"领取结束时间:"+TimeUtils.millis2String(bean.getReceiveDate(),format))
                .setTag(R.id.distribute_coupon_main_ll,bean)
                .setTag(R.id.distribute_coupon_main_distribute,bean)
                .setClickListener(R.id.distribute_coupon_main_ll,leftDetailListener)
                .setClickListener(R.id.distribute_coupon_main_distribute,rightDistributeListener);
    }
}
