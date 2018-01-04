package com.gt.magicbox.order;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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
    private HeadButtonViewHolder headButtonViewHolder;
    private List<OrderListResultBean.OrderItemBean> data = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;
    private static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private static final DateFormat ONLY_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public OrderListAdapter(Context context, List<OrderListResultBean.OrderItemBean> data) {
        this.context = context;
        this.data = data;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getItemViewType(int position) {
            return TYPE_ORDER_ITEM;
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
        OrderListResultBean.OrderItemBean orderItemBean = data.get(position);

        switch (getItemViewType(position)){
            case TYPE_HEAD_BUTTON:
                if (convertView==null) {
                    Log.e("lgx"," headButtonViewHolder=new HeadButtonViewHolder();");
                    headButtonViewHolder=new HeadButtonViewHolder();
                    convertView = mInflater.inflate(R.layout.order_head_view, null);
                    headButtonViewHolder.noPayOrder= (Button) convertView.findViewById(R.id.notPayButton);
                    headButtonViewHolder.payOrder=(Button)convertView.findViewById(R.id.payButton);
                    convertView.setTag(headButtonViewHolder);
                }else {
                    headButtonViewHolder= (HeadButtonViewHolder) convertView.getTag();
                }
                break;
            case TYPE_ORDER_ITEM:
                if (convertView == null) {
                    viewHolder = new OrderItemViewHolder();
                    convertView = mInflater.inflate(R.layout.item_order,null);
                    viewHolder.money= (TextView) convertView.findViewById(R.id.money);
                    viewHolder.orderNo=(TextView)convertView.findViewById(R.id.valueOrderNo);
                    viewHolder.time=(TextView)convertView.findViewById(R.id.valueTime);
                    viewHolder.timeTitle=(TextView) convertView.findViewById(R.id.timeTitle);
                    convertView.setTag(viewHolder);
                }else {
                    viewHolder= (OrderItemViewHolder) convertView.getTag();
                }
                String timeTitle=TimeUtils.millis2String(orderItemBean.time,ONLY_DATE_FORMAT);
                viewHolder.timeTitle.setText(timeTitle);
                viewHolder.time.setText(TimeUtils.millis2String(orderItemBean.time,DEFAULT_FORMAT));
                viewHolder.orderNo.setText(orderItemBean.order_no);
                viewHolder.money.setText(""+orderItemBean.money);
                if (position==0){
                    viewHolder.timeTitle.setVisibility(View.VISIBLE);
                }else {
                    if (position>0) {
                        OrderListResultBean.OrderItemBean previous = data.get(position - 1);
                        if (previous!=null){
                            String timeTitlePrevious = TimeUtils.millis2String(previous.time, ONLY_DATE_FORMAT);
                            if (timeTitle.equals(timeTitlePrevious)){
                                viewHolder.timeTitle.setVisibility(View.GONE);
                            }else {
                                viewHolder.timeTitle.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                }

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
        TextView timeTitle;

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
}
