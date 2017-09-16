package com.gt.magicbox.order;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.bean.OrderListResultBean;
import com.gt.magicbox.http.BaseResponse;
import com.gt.magicbox.http.HttpRequestDialog;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.order.widget.pulltorefresh.PullToRefreshBase;
import com.gt.magicbox.order.widget.pulltorefresh.PullToRefreshSwipeListView;
import com.gt.magicbox.order.widget.swipmenulistview.SwipeMenu;
import com.gt.magicbox.order.widget.swipmenulistview.SwipeMenuCreator;
import com.gt.magicbox.order.widget.swipmenulistview.SwipeMenuItem;
import com.gt.magicbox.order.widget.swipmenulistview.SwipeMenuListView;
import com.gt.magicbox.utils.commonutil.ConvertUtils;
import com.gt.magicbox.utils.commonutil.PhoneUtils;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Description:
 * Created by jack-lin on 2017/9/13 0013.
 */

public class OrderListActivity extends BaseActivity {
    private static final String TAG = OrderListActivity.class.getSimpleName();
    private SwipeMenuListView swipeMenuListView;
    private OrderListAdapter orderListAdapter;
    @BindView(R.id.listView)
    PullToRefreshSwipeListView pullToRefreshSwipeListView;
    private List<OrderListResultBean.OrderItemBean> orderItemBeanList = new ArrayList<>();
    private int page = 1;
    private int updatePage;
    HttpRequestDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        orderItemBeanList.add(new OrderListResultBean.OrderItemBean());
        getOrderList(0, 10);
        initView();
    }

    private void initView() {
        dialog = new HttpRequestDialog();
        dialog.show();
        pullToRefreshSwipeListView.setPullLoadEnabled(true);
        pullToRefreshSwipeListView.setScrollLoadEnabled(true);
        pullToRefreshSwipeListView.setPullRefreshEnabled(false);
        pullToRefreshSwipeListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<SwipeMenuListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {
                page++;
                getOrderList(0, 10);
            }
        });
        swipeMenuListView = pullToRefreshSwipeListView.getRefreshableView();
        swipeMenuListView.setDivider(null);
        orderListAdapter = new OrderListAdapter(getApplicationContext(), orderItemBeanList);
        swipeMenuListView.setAdapter(orderListAdapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {//这里可以再添加按钮，可以自己添加
                SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
                openItem.setBackground(new ColorDrawable(Color.rgb(0xf0, 0x4a, 0x4a)));
                openItem.setWidth(ConvertUtils.dp2px(61));
                openItem.setTitle("删除");
                openItem.setTitleSize(15);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);
            }
        };
        swipeMenuListView.setMenuCreator(creator);
        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        OrderListResultBean.OrderItemBean orderItemBean=orderItemBeanList.get(position);
                        if (orderItemBean!=null) {
                            deleteNotPayOrder(orderItemBean.id, position);
                        }
                        break;
                }
            }
        });
    }

    private void getOrderList(int status, int size) {
        HttpCall.getApiService()
                .getOrderList(PhoneUtils.getIMEI(), status, page, size)
                .compose(ResultTransformer.<OrderListResultBean>transformer())//线程处理 预处理
                .subscribe(new BaseObserver<OrderListResultBean>() {
                    @Override
                    public void onSuccess(OrderListResultBean data) {
                        Log.i(TAG, "onSuccess");

                        if (data != null) {
                            if (data.orders != null && data.orders.size() > 0) {
                                Log.i(TAG, "onSuccess  data.orders.size()=" + data.orders.size());
                                orderItemBeanList.addAll(data.orders);
                                orderListAdapter.setData(orderItemBeanList);
                                if (page == 1) {
                                    dialog.dismiss();
                                } else if (page > 1)
                                    pullToRefreshSwipeListView.onPullUpRefreshComplete();
                            } else {
                                if (page > 1) {
                                    pullToRefreshSwipeListView.onPullUpRefreshComplete();
                                    //pullToRefreshSwipeListView.setHasMoreData(false);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError");
                        super.onError(e);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        Log.i(TAG, "onFailure");

                        super.onFailure(code, msg);
                    }
                });
    }
    private void deleteNotPayOrder(int orderId, final int position) {
        HttpCall.getApiService()
                .deleteNotPayOrder((Integer) Hawk.get("eqId"), orderId)
                .compose(ResultTransformer.<BaseResponse>transformerNoData())//线程处理 预处理
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        Log.d(TAG, "deleteNotPayOrder onSuccess");

                        if (data != null) {
                                orderItemBeanList.remove(position);
                                orderListAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "deleteNotPayOrder onError");
                        super.onError(e);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        Log.d(TAG, "deleteNotPayOrder onFailure");

                        super.onFailure(code, msg);
                    }
                });
    }
}
