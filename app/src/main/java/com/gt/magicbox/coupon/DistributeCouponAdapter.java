package com.gt.magicbox.coupon;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;

import com.gt.magicbox.R;
import com.gt.magicbox.base.recyclerview.BaseRecyclerAdapter;
import com.gt.magicbox.base.recyclerview.BaseViewHolder;
import com.gt.magicbox.bean.DistributeCouponBean;
import com.gt.magicbox.bean.DuofenCards;
import com.gt.magicbox.utils.SpannableStringUtils;
import com.gt.magicbox.utils.commonutil.StringUtils;
import com.gt.magicbox.utils.commonutil.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

/**
 * Created by wzb on 2017/8/24 0024.
 */

public class DistributeCouponAdapter extends BaseRecyclerAdapter<DuofenCards.DuofencardsBean> {
    private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());

    public DistributeCouponAdapter(Context context, List<DuofenCards.DuofencardsBean> listBean) {
        super(context, listBean);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_distribute_coupon;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, DuofenCards.DuofencardsBean bean, int position) {

        //根据接口返回数据 bean绑定holder
       // holder.setText(R.id.distribute_coupon_name,StringUtils.getIntegerFlagSpann(2.5f,bean.getName()));
        //没有剩余
        holder.setText(R.id.distribute_coupon_name,bean.getBrandName())
                .setText(R.id.distribute_coupon_time, "有效期至："+TimeUtils.millis2String(bean.getPassTime(),format));

    }
}
