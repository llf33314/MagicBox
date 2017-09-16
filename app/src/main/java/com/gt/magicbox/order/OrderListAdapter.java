package com.gt.magicbox.order;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.bean.OrderListResultBean;
import com.gt.magicbox.utils.commonutil.TimeUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Description:
 * Created by jack-lin on 2017/9/14 0014.
 * Buddha bless, never BUG!
 */

public class OrderListAdapter extends BaseAdapter {
    private static final int TYPE_HEAD_BUTTON = 0;
    private static final int TYPE_ORDER_ITEM = 1;

    private List<OrderListResultBean.OrderItemBean> data = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;
    private static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public OrderListAdapter(Context context, List<OrderListResultBean.OrderItemBean> data) {
        this.context = context;
        this.data = data;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getItemViewType(int position) {
        if(TextUtils.isEmpty(data.get(position).orderNo)){
            return TYPE_HEAD_BUTTON;
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
        OrderItemViewHolder viewHolder;
        HeadButtonViewHolder headButtonViewHolder;
        OrderListResultBean.OrderItemBean orderItemBean = data.get(position);

        switch (getItemViewType(position)){
            case TYPE_HEAD_BUTTON:
                    convertView = mInflater.inflate(R.layout.order_head_view,null);
                break;
            case TYPE_ORDER_ITEM:
                if (convertView == null) {
                    viewHolder = new OrderItemViewHolder();
                    convertView = mInflater.inflate(R.layout.item_order,null);
                    viewHolder.money= (TextView) convertView.findViewById(R.id.money);
                    viewHolder.orderNo=(TextView)convertView.findViewById(R.id.valueOrderNo);
                    viewHolder.time=(TextView)convertView.findViewById(R.id.valueTime);
                    convertView.setTag(viewHolder);
                }else {
                    viewHolder= (OrderItemViewHolder) convertView.getTag();
                }
                viewHolder.time.setText(TimeUtils.millis2String(orderItemBean.time,DEFAULT_FORMAT));
                viewHolder.orderNo.setText(orderItemBean.orderNo);
                viewHolder.money.setText(""+orderItemBean.money);
                break;
        }

        return convertView;
    }

    public void setData(List<OrderListResultBean.OrderItemBean> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }

    class OrderItemViewHolder {
        TextView orderNo;
        TextView time;
        TextView money;
    }
    class HeadButtonViewHolder {
        Button payOrder;
        Button noPayOrder;
    }
    @Override
    public int getViewTypeCount() {
        return 2;
    }

}
