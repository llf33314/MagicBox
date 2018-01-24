package com.gt.magicbox.member.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.bean.MemberRechargeHistoryBean.DataEntity.RechargeSEntity;
import com.gt.magicbox.bean.OrderListResultBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Created by jack-lin on 2017/9/14 0014.
 * Buddha bless, never BUG!
 */

public class RechargeOrderListAdapter extends BaseAdapter {
    private static final int TYPE_HEAD = 0;
    private static final int TYPE_ORDER_ITEM = 1;
    private HeadButtonViewHolder headButtonViewHolder;
    private RechargeOrderItemViewHolder viewHolder;
    private List<RechargeSEntity> data = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;

    public RechargeOrderListAdapter(Context context, List<RechargeSEntity> data) {
        this.context = context;
        this.data = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        if(TextUtils.isEmpty(data.get(position).getOrderCode())&&data.get(position).getDiscountAfterMoney()<=0){
            return TYPE_HEAD;
        }else{
            return TYPE_ORDER_ITEM;
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RechargeSEntity rechargeSEntity = data.get(position);

        switch (getItemViewType(position)) {
            case TYPE_HEAD:
                if (convertView == null) {
                    Log.e("lgx", " headButtonViewHolder=new HeadButtonViewHolder();");
                    headButtonViewHolder = new HeadButtonViewHolder();
                    convertView = mInflater.inflate(R.layout.order_head_view, null);
                    headButtonViewHolder.noPayOrder = (Button) convertView.findViewById(R.id.notPayButton);
                    headButtonViewHolder.payOrder = (Button) convertView.findViewById(R.id.payButton);
                    convertView.setTag(headButtonViewHolder);
                } else {
                    headButtonViewHolder = (HeadButtonViewHolder) convertView.getTag();
                }
                break;
            case TYPE_ORDER_ITEM:
                if (convertView == null) {
                    viewHolder = new RechargeOrderItemViewHolder();
                    convertView = mInflater.inflate(R.layout.item_member_recharge, null);
                    viewHolder.money = (TextView) convertView.findViewById(R.id.money);
                    viewHolder.orderNo = (TextView) convertView.findViewById(R.id.orderNo);
                    viewHolder.time = (TextView) convertView.findViewById(R.id.time);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (RechargeOrderItemViewHolder) convertView.getTag();
                }
                if (rechargeSEntity != null) {
                    viewHolder.time.setText(rechargeSEntity.getCreateDate());
                    viewHolder.orderNo.setText("订单号: " + rechargeSEntity.getOrderCode());
                    viewHolder.money.setText("+ " + rechargeSEntity.getDiscountAfterMoney());
                }

                break;
        }

        return convertView;
    }

    public void setData(List<RechargeSEntity> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    public class RechargeOrderItemViewHolder {
        TextView orderNo;
        TextView money;
        TextView time;
    }

    class HeadButtonViewHolder {
        Button payOrder;
        Button noPayOrder;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public HeadButtonViewHolder getHeadButtonViewHolder() {
        return headButtonViewHolder;
    }

    public RechargeOrderItemViewHolder getViewHolder() {
        return viewHolder;
    }

}
