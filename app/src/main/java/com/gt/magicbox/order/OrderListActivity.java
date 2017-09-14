package com.gt.magicbox.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.bean.OrderListResultBean;
import com.gt.magicbox.bean.QRCodeBitmapBean;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.order.widget.pulltorefresh.PullToRefreshSwipeListView;
import com.gt.magicbox.order.widget.swipmenulistview.SwipeMenuListView;
import com.gt.magicbox.pay.QRCodePayActivity;
import com.gt.magicbox.utils.commonutil.AppManager;
import com.gt.magicbox.utils.commonutil.PhoneUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description:
 * Created by jack-lin on 2017/9/13 0013.
 */

public class OrderListActivity extends BaseActivity {
    private static final String TAG=OrderListActivity.class.getSimpleName();
    private SwipeMenuListView swipeMenuListView;
    private OrderListAdapter orderListAdapter;
    @BindView(R.id.listView)
    PullToRefreshSwipeListView pullToRefreshSwipeListView;
    private List<OrderListResultBean.OrderItemBean> orderItemBeanList=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        getOrderList();
        initView();
    }
    private void initView(){
        pullToRefreshSwipeListView.setScrollLoadEnabled(true);
        pullToRefreshSwipeListView.setPullRefreshEnabled(false);
        swipeMenuListView=pullToRefreshSwipeListView.getRefreshableView();
        swipeMenuListView.setDivider(null);
        orderListAdapter=new OrderListAdapter(getApplicationContext(),orderItemBeanList);
        swipeMenuListView.setAdapter(orderListAdapter);
    }
    private void getOrderList() {
        HttpCall.getApiService()
                .getOrderList(PhoneUtils.getIMEI(), 0, 0, 10)
                .compose(ResultTransformer.<OrderListResultBean>transformer())//线程处理 预处理
                .subscribe(new BaseObserver<OrderListResultBean>() {
                    @Override
                    public void onSuccess(OrderListResultBean data) {
                        Log.i(TAG, "onSuccess");

                        if (data != null ) {
                            Log.i(TAG, "onSuccess  data.orders.size()="+data.orders.size());
                            orderListAdapter.setData(data.orders);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        super.onFailure(code, msg);
                    }
                });
    }
}
