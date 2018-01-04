package com.gt.magicbox.order;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.gt.magicbox.Constant;
import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.BaseConstant;
import com.gt.magicbox.bean.OrderListResultBean;
import com.gt.magicbox.bean.UpdateOrderListUIBean;
import com.gt.magicbox.http.BaseResponse;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.order.widget.DropDownMenu;
import com.gt.magicbox.order.widget.MenuListAdapter;
import com.gt.magicbox.order.widget.pulltorefresh.PullToRefreshBase;
import com.gt.magicbox.order.widget.pulltorefresh.PullToRefreshSwipeListView;
import com.gt.magicbox.order.widget.swipmenulistview.SwipeMenu;
import com.gt.magicbox.order.widget.swipmenulistview.SwipeMenuCreator;
import com.gt.magicbox.order.widget.swipmenulistview.SwipeMenuItem;
import com.gt.magicbox.order.widget.swipmenulistview.SwipeMenuListView;
import com.gt.magicbox.pay.ChosePayModeActivity;
import com.gt.magicbox.pay.QRCodePayActivity;
import com.gt.magicbox.utils.RxBus;
import com.gt.magicbox.utils.commonutil.ConvertUtils;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.gt.magicbox.utils.commonutil.PhoneUtils;
import com.gt.magicbox.widget.LoadingProgressDialog;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * Description:
 * Created by jack-lin on 2017/9/13 0013.
 */

public class OrderListActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = OrderListActivity.class.getSimpleName();
    @BindView(R.id.dropDownMenu)
    DropDownMenu dropDownMenu;
    private SwipeMenuListView swipeMenuListView;
    private OrderListAdapter orderListAdapter;
    @BindView(R.id.listView)
    PullToRefreshSwipeListView pullToRefreshSwipeListView;
    private List<OrderListResultBean.OrderItemBean> orderItemBeanList = new ArrayList<>();
    private int page = 1;
    private int updatePage;
    LoadingProgressDialog dialog;
    private int status = 0;
    private final static int RESULT_FROM_PAY = 1;
    public final static int RESULT_FROM_PAY_SUCCESS = 101;
    private Handler handler = new Handler();
    private String headers[] = {"全部时间", "未支付"};
    private String timeFilter[] = {"全部时间", "今天", "最近7天", "最近15天"};
    private String payStatus[] = {"未支付", "已支付", "已退款"};
    private List<View> popupViews = new ArrayList<>();
    private ListView listView1;
    private ListView listView2;
    private MenuListAdapter mMenuAdapter1;
    private MenuListAdapter mMenuAdapter2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        getOrderList(0, 10);
        initView();
        registerUpdateUI();
    }

    private void initView() {
        BaseConstant.isCanSwipe = true;
        dialog = new LoadingProgressDialog(OrderListActivity.this);
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
                getOrderList(status, 10);
            }
        });
        swipeMenuListView = pullToRefreshSwipeListView.getRefreshableView();
        swipeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderListResultBean.OrderItemBean orderItemBean = orderItemBeanList.get(position);
                if (orderItemBean != null) {
                    if (orderItemBean.id > 0 && status == 0) {
                        if (Constant.product.equals(BaseConstant.PRODUCTS[1])) {
                            Intent intent = new Intent(getApplicationContext(), ChosePayModeActivity.class);
                            intent.putExtra("customerType", ChosePayModeActivity.TYPE_ORDER_PUSH);
                            intent.putExtra("orderNo", orderItemBean.order_no);
                            intent.putExtra("money", orderItemBean.money);
                            startActivity(intent);
                        } else if (Constant.product.equals(BaseConstant.PRODUCTS[0])) {
                            Intent intent = new Intent(getApplicationContext(), QRCodePayActivity.class);
                            intent.putExtra("type", QRCodePayActivity.TYPE_CREATED_PAY);
                            intent.putExtra("orderId", orderItemBean.id);
                            intent.putExtra("money", orderItemBean.money);
                            intent.putExtra("payMode", orderItemBean.type);
                            startActivityForResult(intent, RESULT_FROM_PAY);
                        }

                    } else if (status == 1) {
                        Intent intent = new Intent(getApplicationContext(), OrderInfoActivity.class);
                        intent.putExtra("OrderItemBean", orderItemBean);
                        startActivity(intent);
                    }

                }
            }
        });
        swipeMenuListView.setDivider(null);
        orderListAdapter = new OrderListAdapter(getApplicationContext(), orderItemBeanList);
        swipeMenuListView.setAdapter(orderListAdapter);
        setSwipeMenu();
        initDropMenuView();
    }

    private void setSwipeMenu() {
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
                        OrderListResultBean.OrderItemBean orderItemBean = orderItemBeanList.get(position);
                        if (orderItemBean != null) {
                            deleteNotPayOrder(orderItemBean.id, position);
                        }
                        break;
                }
            }
        });
    }

    private void getOrderList(final int status, int size) {
        HttpCall.getApiService()
                .getOrderList(PhoneUtils.getIMEI(), status, page, size)
                .compose(ResultTransformer.<OrderListResultBean>transformer())//线程处理 预处理
                .subscribe(new BaseObserver<OrderListResultBean>() {
                    @Override
                    public void onSuccess(OrderListResultBean data) {
                        LogUtils.d(TAG, "onSuccess");
                        if (data != null && orderListAdapter != null) {
                            if (data.orders != null && data.orders.size() > 0) {
                                LogUtils.d(TAG, "onSuccess  data.orders.size()=" + data.orders.size());
                                orderItemBeanList.addAll(data.orders);
                                orderListAdapter.setData(orderItemBeanList);
                                if (page == 1) {
                                    if (dialog != null) {
                                        dialog.dismiss();
                                    }
                                } else if (page > 1)
                                    pullToRefreshSwipeListView.onPullUpRefreshComplete();
                            } else {
                                if (page == 1) {
                                    if (dialog != null) {
                                        dialog.dismiss();
                                    }
                                } else if (page > 1) {
                                    pullToRefreshSwipeListView.onPullUpRefreshComplete();
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.d(TAG, "onError");
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        super.onError(e);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        LogUtils.d(TAG, "onFailure");
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        super.onFailure(code, msg);
                    }
                });
    }

    private void deleteNotPayOrder(int orderId, final int position) {
        HttpCall.getApiService()
                .deleteNotPayOrder(Hawk.get("eqId", 0), orderId)
                .compose(ResultTransformer.<BaseResponse>transformerNoData())//线程处理 预处理
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        LogUtils.d(TAG, "deleteNotPayOrder onSuccess");

                        if (data != null) {
                            orderItemBeanList.remove(position);
                            orderListAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.d(TAG, "deleteNotPayOrder onError");
                        super.onError(e);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        LogUtils.d(TAG, "deleteNotPayOrder onFailure");

                        super.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.payButton:
                setButtonSelected(orderListAdapter.getHeadButtonViewHolder().noPayOrder, false);
                setButtonSelected(orderListAdapter.getHeadButtonViewHolder().payOrder, true);
                page = 1;
                status = 1;
                orderItemBeanList.clear();
                orderItemBeanList.add(new OrderListResultBean.OrderItemBean());
                dialog = new LoadingProgressDialog(OrderListActivity.this);
                dialog.show();
                orderListAdapter.setData(orderItemBeanList);
                BaseConstant.isCanSwipe = false;
                getOrderList(1, 10);
                break;
            case R.id.notPayButton:
                setButtonSelected(orderListAdapter.getHeadButtonViewHolder().noPayOrder, true);
                setButtonSelected(orderListAdapter.getHeadButtonViewHolder().payOrder, false);
                page = 1;
                status = 0;
                orderItemBeanList.clear();
                dialog = new LoadingProgressDialog(OrderListActivity.this);
                dialog.show();
                orderItemBeanList.add(new OrderListResultBean.OrderItemBean());
                BaseConstant.isCanSwipe = true;
                orderListAdapter.setData(orderItemBeanList);
                getOrderList(0, 10);

                break;
        }

    }

    private void setButtonSelected(Button button, boolean selected) {
        if (button != null) {
            button.setEnabled(!selected);
            int textColor = selected ? 0xffffffff : 0xfff04a4a;
            button.setTextColor(textColor);
        }
    }

    private void registerUpdateUI() {
        RxBus.get().toObservable(UpdateOrderListUIBean.class).subscribe(new Consumer<UpdateOrderListUIBean>() {
            @Override
            public void accept(UpdateOrderListUIBean updateOrderListUIBean) throws Exception {
                orderItemBeanList.clear();
                orderItemBeanList.add(new OrderListResultBean.OrderItemBean());
                initView();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getOrderList(0, 10);
                    }
                }, 500);
            }
        });
    }
    private void initDropMenuView() {

        //这里是每个下拉菜单之后的布局,目前只是简单的listview作为展示
        listView1 = new ListView(OrderListActivity.this);
        listView2 = new ListView(OrderListActivity.this);

        listView1.setDividerHeight(0);
        listView2.setDividerHeight(0);

        mMenuAdapter1 = new MenuListAdapter(OrderListActivity.this, Arrays.asList(timeFilter));
        mMenuAdapter2 = new MenuListAdapter(OrderListActivity.this, Arrays.asList(payStatus));

        listView1.setAdapter(mMenuAdapter1);
        listView2.setAdapter(mMenuAdapter2);

        popupViews.add(listView1);
        popupViews.add(listView2);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                dropDownMenu.setTabText(timeFilter[position]);
                dropDownMenu.closeMenu();
            }
        });

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                dropDownMenu.setTabText(payStatus[position]);
                dropDownMenu.closeMenu();
            }
        });



        //这里添加 内容显示区域,可以是任何布局


        dropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews,pullToRefreshSwipeListView);

    }
}
