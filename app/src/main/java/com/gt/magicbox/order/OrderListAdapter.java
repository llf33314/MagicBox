package com.gt.magicbox.order;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.bean.OrderListResultBean;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.gt.magicbox.utils.commonutil.TimeUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
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
    private OrderItemViewHolder viewHolder;
    private HashMap<Integer,RelativeLayout> swipeLayoutMap=new HashMap<>();
    private List<OrderListResultBean.OrderItemBean> data = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;
    private String[] payStatus = {"未支付", "已支付", "已退款"};
    private Integer[] icons = {R.drawable.order_list_wechat, R.drawable.order_list_alipay, R.drawable.order_list_cash, R.drawable.order_list_member_pay
            , R.drawable.order_list_bank_card};
    private static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    private static final DateFormat ONLY_DATE_FORMAT = new SimpleDateFormat("yyyy年 MM月 dd日", Locale.getDefault());
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
        OrderListResultBean.OrderItemBean orderItemBean = data.get(position);

        switch (getItemViewType(position)) {
            case TYPE_HEAD_BUTTON:
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
                    viewHolder = new OrderItemViewHolder();
                    convertView = mInflater.inflate(R.layout.item_order, null);
                    viewHolder.money = (TextView) convertView.findViewById(R.id.money);
                    viewHolder.orderNo = (TextView) convertView.findViewById(R.id.valueOrderNo);
                    viewHolder.timeTitle = (TextView) convertView.findViewById(R.id.timeTitle);
                    viewHolder.orderStatus = (TextView) convertView.findViewById(R.id.order_status);
                    viewHolder.orderIcon = (ImageView) convertView.findViewById(R.id.item_icon);
                    viewHolder.swipeLayout=(RelativeLayout)convertView.findViewById(R.id.swipeLayout);
                    convertView.setTag(viewHolder);
                    LogUtils.d("TYPE_ORDER_ITEM position="+position+" orderItemBean.time="+orderItemBean.time);

                } else {
                    viewHolder = (OrderItemViewHolder) convertView.getTag();
                }
                if (swipeLayoutMap.get(position)==null) {
                    swipeLayoutMap.put(position, viewHolder.swipeLayout);
                }
                String timeTitle = TimeUtils.millis2String(orderItemBean.time, ONLY_DATE_FORMAT);
                viewHolder.timeTitle.setText(timeTitle);
                if (orderItemBean != null) {
                    if (orderItemBean.status >= 0 && orderItemBean.status < payStatus.length) {
                        viewHolder.orderStatus.setText(payStatus[orderItemBean.status]);
                    }
                    if (orderItemBean.type >= 0 && orderItemBean.type < icons.length) {
                        viewHolder.orderIcon.setImageResource(icons[orderItemBean.type]);
                    }

                    viewHolder.orderNo.setText(orderItemBean.order_no + "   " + TimeUtils.millis2String(orderItemBean.time, DEFAULT_FORMAT));
                    viewHolder.money.setText("¥ " + orderItemBean.money);
                }
                if (position == 0) {
                    viewHolder.timeTitle.setVisibility(View.VISIBLE);
                } else {
                    if (position > 0) {
                        OrderListResultBean.OrderItemBean previous = data.get(position - 1);
                        if (previous != null) {
                            String timeTitlePrevious = TimeUtils.millis2String(previous.time, ONLY_DATE_FORMAT);
                            if (timeTitle.equals(timeTitlePrevious)) {
                                viewHolder.timeTitle.setVisibility(View.GONE);
                            } else {
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

    public class OrderItemViewHolder {
        TextView orderNo;
        TextView money;
        TextView orderStatus;
        TextView timeTitle;
        ImageView orderIcon;
        public RelativeLayout swipeLayout;
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
    public OrderItemViewHolder getViewHolder() {
        return viewHolder;
    }

    public HashMap<Integer, RelativeLayout> getSwipeLayoutMap() {
        return swipeLayoutMap;
    }

}
