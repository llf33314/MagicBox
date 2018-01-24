package com.gt.magicbox.member;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.gt.api.bean.sign.SignBean;
import com.gt.api.util.sign.SignHttpUtils;
import com.gt.magicbox.Constant;
import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.BaseConstant;
import com.gt.magicbox.bean.MemberRechargeHistoryBean;
import com.gt.magicbox.bean.OrderListResultBean;
import com.gt.magicbox.bean.ShopBean;
import com.gt.magicbox.bean.ShopInfoBean;
import com.gt.magicbox.http.HttpConfig;
import com.gt.magicbox.member.widget.RechargeOrderListAdapter;
import com.gt.magicbox.order.OrderInfoActivity;
import com.gt.magicbox.order.OrderListActivity;
import com.gt.magicbox.order.OrderListAdapter;
import com.gt.magicbox.order.widget.DropDownMenu;
import com.gt.magicbox.order.widget.MenuListAdapter;
import com.gt.magicbox.order.widget.pulltorefresh.PullToRefreshBase;
import com.gt.magicbox.order.widget.pulltorefresh.PullToRefreshSwipeListView;
import com.gt.magicbox.order.widget.swipmenulistview.SwipeMenuListView;
import com.gt.magicbox.pay.ChosePayModeActivity;
import com.gt.magicbox.pay.QRCodePayActivity;
import com.gt.magicbox.utils.DuoFriendUtils;
import com.gt.magicbox.utils.GT_API_Utils;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.gt.magicbox.widget.LoadingProgressDialog;
import com.gt.magicbox.widget.SearchView;
import com.orhanobut.hawk.Hawk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import com.gt.magicbox.bean.MemberRechargeHistoryBean.DataEntity.*;

/**
 * Description:
 * 查询会员充值记录
 *
 * @author jack-lin
 * @date 2018/1/22 0022
 * Buddha bless, never BUG!
 */

public class RechargeHistoryActivity extends BaseActivity {
    @BindView(R.id.dropDownMenu)
    DropDownMenu dropDownMenu;
    MenuListAdapter mMenuAdapter1;
    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.searchToolbar)
    RelativeLayout searchToolbar;
    @BindView(R.id.listView)
    PullToRefreshSwipeListView pullToRefreshSwipeListView;

    ExecutorService cachedThreadPool;

    private int curPage = 1;
    private ListView listView1;
    private List<View> popupViews = new ArrayList<>();
    private String[] headers = {"全部门店"};
    private String[] shopFilter = {"全部门店"};
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client;
    String SIGNKEY = "WXMP2017";
    private ShopInfoBean currentShop;
    List<RechargeSEntity> data = new ArrayList<>();
    private RechargeOrderListAdapter rechargeOrderListAdapter;
    private SwipeMenuListView swipeMenuListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_recharge_history);
        cachedThreadPool = Executors.newCachedThreadPool();
        initOkHttp();
        initOrderListView();
        initShop();
        queryWxShopByBusId(Hawk.get("busId", 0));
        runRechargeLog(1);
    }

    private void runRechargeLog(final int curPage) {
        cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                rechargeLog(Hawk.get("busId", 0), curPage, 1511107200);
            }
        });
    }

    private void initShop() {
        currentShop = Hawk.get("ShopInfoBean");
        if (!DuoFriendUtils.isMainShop()) {
            headers = new String[]{currentShop.getShopName()};
            shopFilter = new String[]{currentShop.getShopName()};
        }
        initDropMenuView();
    }

    private void initDropMenuView() {
        popupViews.clear();
        dropDownMenu.clearTabChildView();
        //这里是每个下拉菜单之后的布局,目前只是简单的listview作为展示
        listView1 = new ListView(RechargeHistoryActivity.this);

        listView1.setDividerHeight(0);

        mMenuAdapter1 = new MenuListAdapter(RechargeHistoryActivity.this, Arrays.asList(shopFilter));

        listView1.setAdapter(mMenuAdapter1);

        popupViews.add(listView1);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mMenuAdapter1.setSelectedPosition(position);
                dropDownMenu.setTabText(shopFilter[position]);
                dropDownMenu.closeMenu();
                switch (position) {
                    default:
                        break;
                }
            }
        });


        //这里添加 内容显示区域,可以是任何布局

        dropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews);

    }

    private void initOkHttp() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        if (BaseConstant.isPrintLog) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build();
    }


    public void queryWxShopByBusId(int busId) {
        try {

            Map<String, Object> json = new HashMap<String, Object>();
            json.put("reqdata", busId);
            SignBean signBean = null;
            String newMsg = "";
            try {
                newMsg = new String(JSONObject.toJSONString(json).getBytes("utf-8"), "utf-8");
                signBean = GT_API_Utils.sign(SIGNKEY, newMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }

            RequestBody requestBodyPost = RequestBody.create(JSON, JSONObject.toJSONString(json));

            Request requestPost = new Request.Builder()
                    .url(Constant.WXMP_BASE_URL + HttpConfig.QUERY_WX_SHOP_BY_BUS_ID)
                    .addHeader("sign", JSONObject.toJSONString(signBean))
                    .post(requestBodyPost)
                    .build();
            client.newCall(requestPost).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("queryWxShopByBusId", "IOException=" + e.toString());

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String string = response.body().string();
                    ShopBean shopBean = new Gson().fromJson(string, ShopBean.class);
                    Log.d("queryWxShopByBusId", "onResponse string=" + string);

                    if (shopBean != null && shopBean.getData() != null) {
                        Log.d("queryWxShopByBusId", "onResponse shopBean=" + shopBean.getData().size());
                        String[] ary = new String[shopBean.getData().size() + 1];
                        ary[0] = "全部门店";
                        for (int i = 1; i < shopBean.getData().size() + 1; i++) {
                            ary[i] = shopBean.getData().get(i - 1).getBusinessName();
                        }
                        shopFilter = ary;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initDropMenuView();
                            }
                        });
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rechargeLog(int busId, final int curPage, long startTime) {
        try {
            Log.d("rechargeLog", "start");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("busId", busId);
            jsonObject.put("curPage", curPage);
            jsonObject.put("startTime", startTime);
            jsonObject.put("pageSize", 10);
            String result = SignHttpUtils.WxmppostByHttp(Constant.MEMBER_BASE_URL + HttpConfig.RECHARGE_LOG, jsonObject,
                    "MV8MMFQUMU1HJ6F2GNH40ZFJJ7Q8LNVM");
            MemberRechargeHistoryBean memberRechargeHistoryBean = new Gson().fromJson(result, MemberRechargeHistoryBean.class);
            if (memberRechargeHistoryBean != null&&memberRechargeHistoryBean.getData()!=null) {
                LogUtils.d("rechargeLog memberRechargeHistoryBean=" + result);

                LogUtils.d("rechargeLog memberRechargeHistoryBean.size=" + memberRechargeHistoryBean.getData().getRechargeS().size());

                data.addAll(memberRechargeHistoryBean.getData().getRechargeS());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rechargeOrderListAdapter.setData(data);
                        if (curPage == 1) {
                        } else if (curPage > 1)
                            pullToRefreshSwipeListView.onPullUpRefreshComplete();
                    }
                });
            } else {
                if (curPage == 1) {
                } else if (curPage > 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pullToRefreshSwipeListView.onPullUpRefreshComplete();
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.cancel, R.id.searchToolbar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                break;
            case R.id.searchToolbar:
                break;
        }
    }

    private void initOrderListView() {
        if (getSearch() != null) {
            getSearch().setVisibility(View.VISIBLE);
            getSearch().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(RechargeHistoryActivity.this, OrderListActivity.class);
//                    intent.putExtra("type", TYPE_ORDER_SEARCH);
//                    startActivity(intent);
                }
            });
        }
        BaseConstant.isCanSwipe = false;
//        dialog = new LoadingProgressDialog(OrderListActivity.this);
//        dialog.show();
        initSwipeListView();
        pullToRefreshSwipeListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<SwipeMenuListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {
                curPage++;
                runRechargeLog(curPage);
            }
        });
    }

    private void initSwipeListView() {
        BaseConstant.isCanSwipe = true;
        pullToRefreshSwipeListView.setPullLoadEnabled(true);
        pullToRefreshSwipeListView.setScrollLoadEnabled(true);
        pullToRefreshSwipeListView.setPullRefreshEnabled(false);
        swipeMenuListView = pullToRefreshSwipeListView.getRefreshableView();
        swipeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                OrderListResultBean.OrderItemBean orderItemBean = orderItemBeanList.get(position);
//                if (orderItemBean != null) {
//                    if (orderItemBean.id > 0 && orderItemBean.status == 0) {
//                        if (Constant.product.equals(BaseConstant.PRODUCTS[1])) {
//                            Intent intent = new Intent(getApplicationContext(), ChosePayModeActivity.class);
//                            intent.putExtra("customerType", ChosePayModeActivity.TYPE_ORDER_PUSH);
//                            intent.putExtra("orderNo", orderItemBean.order_no);
//                            intent.putExtra("money", orderItemBean.money);
//                            startActivity(intent);
//                        } else if (Constant.product.equals(BaseConstant.PRODUCTS[0])) {
//                            Intent intent = new Intent(getApplicationContext(), QRCodePayActivity.class);
//                            intent.putExtra("type", QRCodePayActivity.TYPE_CREATED_PAY);
//                            intent.putExtra("orderId", orderItemBean.id);
//                            intent.putExtra("money", orderItemBean.money);
//                            intent.putExtra("payMode", orderItemBean.type);
//                            startActivityForResult(intent, RESULT_FROM_PAY);
//                        }
//
//                    } else if (orderItemBean.status == 1) {
//                        Intent intent = new Intent(getApplicationContext(), OrderInfoActivity.class);
//                        intent.putExtra("OrderItemBean", orderItemBean);
//                        startActivity(intent);
//                    } else if (orderItemBean.status == 2) {
//                        Intent intent = new Intent(getApplicationContext(), OrderInfoActivity.class);
//                        intent.putExtra("OrderItemBean", orderItemBean);
//                        startActivity(intent);
//                    }

//                }
            }
        });
        swipeMenuListView.setDivider(null);
        rechargeOrderListAdapter = new RechargeOrderListAdapter(getApplicationContext(), data);
        swipeMenuListView.setAdapter(rechargeOrderListAdapter);

    }

}