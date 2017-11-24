package com.gt.magicbox.coupon;

import android.content.Context;

import com.gt.magicbox.R;
import com.gt.magicbox.base.recyclerview.BaseRecyclerAdapter;
import com.gt.magicbox.base.recyclerview.BaseViewHolder;
import com.gt.magicbox.bean.DistributeCouponBean;
import com.gt.magicbox.bean.MemberCouponBean;

import java.util.List;

/**
 * Created by wzb on 2017/8/24 0024.
 */

public class HorizontalCouponAdapter extends BaseRecyclerAdapter<MemberCouponBean> {
    private int type;
    public static final int TYPE_COUPON=0;
    public static final int TYPE_FEN_COIN=1;

    public HorizontalCouponAdapter(Context context, List<MemberCouponBean> listBean,int type) {
        super(context, listBean);
        this.type=type;
    }

    @Override
    public int getLayoutId() {
        switch (type) {
            case TYPE_COUPON:
                return R.layout.item_small_coupon;
            case TYPE_FEN_COIN:
                return R.layout.item_fen_coin;
            default:
                return R.layout.item_small_coupon;

        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, MemberCouponBean bean, int position) {
        if (type==TYPE_COUPON) {
            holder.setText(R.id.item_name, ""+bean.getTitle());
        }
        else if (type==TYPE_FEN_COIN){
            holder.setText(R.id.item_name,"100 粉币");
        }
        holder.findView(R.id.item_name).setPressed(bean.isSelected());

    }
    public void setType(int type) {
        this.type = type;
    }
}
