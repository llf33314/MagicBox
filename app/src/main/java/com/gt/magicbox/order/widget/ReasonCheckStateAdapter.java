package com.gt.magicbox.order.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.bean.MemberCouponBean;
import com.gt.magicbox.bean.ReasonBean;
import com.gt.magicbox.utils.commonutil.LogUtils;

import java.security.cert.CertPathValidatorException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description:
 * Created by jack-lin on 2017/12/22 0022.
 * Buddha bless, never BUG!
 */

public class ReasonCheckStateAdapter extends RecyclerView.Adapter<ReasonCheckStateAdapter.StateHolder> {

    private Context context;
    private int selectedPosition = -5; //默认一个参数
    private List<ReasonBean> beans = new ArrayList<>();

    private StateHolder currentHolder;
    private Integer[] resIcon = {R.drawable.radio_not_select, R.drawable.radio_select};

    public ReasonCheckStateAdapter(Context context, List<ReasonBean> beans) {
        this.context = context;
        this.beans = beans;
    }

    @Override
    public StateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StateHolder(LayoutInflater.from(context).inflate(R.layout.item_reason, parent, false));
    }

    @Override
    public void onBindViewHolder(final StateHolder holder, final int position) {
        LogUtils.d("onBindViewHolder  position=" + position + "  selectedPosition=" + selectedPosition);

        if (null == beans) {
            return;
        }
        if (!TextUtils.isEmpty(beans.get(position).reason)) {
            holder.reasonTextView.setText(beans.get(position).reason);
        }
        setSelected(holder.selectStatus, selectedPosition == position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
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
        @BindView(R.id.reasonTextView)
        TextView reasonTextView;
        @BindView(R.id.selectStatus)
        ImageView selectStatus;

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


    private void setSelected(ImageView imageView, boolean isSelect) {
        if (imageView != null) {
            imageView.setImageResource(!isSelect ? resIcon[0] : resIcon[1]);
        }
    }

    public void updateCurrentHolder() {
        if (currentHolder != null) {
            setSelected(currentHolder.selectStatus, true);
        }
    }
    public void updateAdapter(ArrayList<ReasonBean> beans){
        this.beans=beans;
        notifyDataSetChanged();
    }
}