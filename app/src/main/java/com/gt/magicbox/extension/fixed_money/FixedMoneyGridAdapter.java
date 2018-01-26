package com.gt.magicbox.extension.fixed_money;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.gt.magicbox.R;
import com.gt.magicbox.bean.FixedMoneyBean;
import com.gt.magicbox.utils.commonutil.ConvertUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description:
 * Created by jack-lin on 2017/7/17 0017.
 */

public class FixedMoneyGridAdapter extends RecyclerView.Adapter<FixedMoneyGridAdapter.ViewHolder> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<FixedMoneyBean> mGridData = new ArrayList<>();
    private ViewHolder firstHolder;

    private int selectedPosition = -5; //默认一个参数
    private ViewHolder currentHolder;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public FixedMoneyGridAdapter(Context context, ArrayList<FixedMoneyBean> data) {
        this.context = context;
        this.mGridData = data;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_fixed_moeny, parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (null == mGridData) {
            return;
        }
        if (position == 0) {
            firstHolder = holder;
        }
        holder.button.setPressed(selectedPosition == position);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.button.getLayoutParams();
        switch (getItemCount()) {
            case 1:
            case 2:
                layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
                layoutParams.height = ConvertUtils.dp2px(65);
                layoutParams.setMargins(ConvertUtils.dp2px(0), ConvertUtils.dp2px(20)
                        , ConvertUtils.dp2px(0), ConvertUtils.dp2px(0));
                break;
            case 3:
            case 5:
            case 6:
                layoutParams.width = ConvertUtils.dp2px(100);
                layoutParams.height = ConvertUtils.dp2px(65);
                layoutParams.setMargins(ConvertUtils.dp2px(6), ConvertUtils.dp2px(10)
                        , ConvertUtils.dp2px(6), ConvertUtils.dp2px(0));
                break;
            case 4:
                layoutParams.width = ConvertUtils.dp2px(150);
                layoutParams.height = ConvertUtils.dp2px(65);
                layoutParams.setMargins(ConvertUtils.dp2px(6), ConvertUtils.dp2px(10)
                        , ConvertUtils.dp2px(6), ConvertUtils.dp2px(0));
                break;

        }
        holder.button.setLayoutParams(layoutParams);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.OnItemClick(v, holder, position);
                }
                    selectedPosition = position; //选择的position赋值给参数，
                    currentHolder = holder;
                notifyDataSetChanged();

            }
        });
        if (mGridData.get(position).money > 0) {
            holder.button.setText("¥" + mGridData.get(position).money);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return mGridData.size() == 0 ? 0 : mGridData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name_item)
        Button button;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public void setGridData(ArrayList<FixedMoneyBean> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClick(View view, ViewHolder viewHolder, int position);
    }

    public void updateCurrentHolder() {
        if (currentHolder != null) {
            currentHolder.button.setPressed(true);
        }
    }
    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

}
