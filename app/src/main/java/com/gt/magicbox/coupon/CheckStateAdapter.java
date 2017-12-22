package com.gt.magicbox.coupon;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.recyclerview.BaseViewHolder;
import com.gt.magicbox.bean.MemberCouponBean;
import com.gt.magicbox.utils.commonutil.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description:
 * Created by jack-lin on 2017/12/22 0022.
 * Buddha bless, never BUG!
 */

public class CheckStateAdapter extends RecyclerView.Adapter<CheckStateAdapter.StateHolder> {

    private Context context;
    private int selectedPosition = -5; //默认一个参数
    private List<MemberCouponBean> beans = new ArrayList<>();

    private StateHolder currentHolder;

    public CheckStateAdapter(Context context, List<MemberCouponBean> beans) {
        this.context = context;
        this.beans = beans;
    }

    @Override
    public StateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StateHolder(LayoutInflater.from(context).inflate(R.layout.item_small_coupon, parent, false));
    }

    @Override
    public void onBindViewHolder(final StateHolder holder, final int position) {
        LogUtils.d("onBindViewHolder  position=" + position + "  selectedPosition=" + selectedPosition);

        if (null == beans) {
            return;
        }
        holder.tvState.setPressed(selectedPosition == position);
        holder.tvState.setText(getCouponName(beans.get(position)));
        holder.tvState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.OnItemClick(v, holder, holder.getAdapterPosition());
                }
                if (position == selectedPosition) {
                    selectedPosition = -1;
                    currentHolder = null;
                } else {
                    selectedPosition = position; //选择的position赋值给参数，
                    currentHolder = holder;
                }
                notifyDataSetChanged();

                //  holder.tvState.setPressed(selectedPosition == position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return beans.size() == 0 ? 0 : beans.size();
    }


    public class StateHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_name)
        TextView tvState;

        public StateHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {  //定义接口，实现Recyclerview点击事件
        void OnItemClick(View view, StateHolder holder, int position);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {   //实现点击
        this.onItemClickListener = onItemClickListener;
    }

    private String getCouponName(MemberCouponBean memberCouponBean) {
        if (memberCouponBean.getDiscount() > 0) {
            return memberCouponBean.getDiscount() + " 折券";
        } else if (memberCouponBean.getCash_least_cost() > 0) {
            return "满" + memberCouponBean.getCash_least_cost()
                    + "元减" + memberCouponBean.getReduce_cost();
        } else {
            return "其他优惠券";
        }
    }


    public void updateCurrentHolder() {
        if (currentHolder != null) {
            currentHolder.tvState.setPressed(true);
        }
    }

}