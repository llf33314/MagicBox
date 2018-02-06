package com.gt.magicbox.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gt.magicbox.Constant;
import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.BaseConstant;
import com.gt.magicbox.base.recyclerview.BaseRecyclerAdapter;
import com.gt.magicbox.bean.KeyValueStringBean;
import com.gt.magicbox.bean.OrderListResultBean;
import com.gt.magicbox.bean.QueryOrderBean;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.main.MoreFunctionDialog;
import com.gt.magicbox.order.widget.KeyValueAdapter;
import com.gt.magicbox.setting.printersetting.PrintManager;
import com.gt.magicbox.setting.printersetting.PrinterConnectService;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.gt.magicbox.utils.commonutil.TimeUtils;
import com.orhanobut.hawk.Hawk;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:
 *
 * @author jack-lin
 * @date 2017/9/19 0019
 * Buddha bless, never BUG!
 */

public class OrderInfoActivity extends BaseActivity {
    private static final String TAG = OrderInfoActivity.class.getSimpleName();
    @BindView(R.id.keyValueView)
    RecyclerView keyValueView;
    @BindView(R.id.infoIcon)
    ImageView infoIcon;
    @BindView(R.id.payType)
    TextView payType;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.refund)
    Button refund;
    @BindView(R.id.returnMoneyView)
    RecyclerView returnMoneyView;
    @BindView(R.id.printPaper)
    Button printPaper;
    private OrderListResultBean.OrderItemBean orderItemBean;
    private QueryOrderBean queryOrderBean;
    List<KeyValueStringBean> lists = new ArrayList<KeyValueStringBean>();
    List<KeyValueStringBean> returnMoneyLists = new ArrayList<KeyValueStringBean>();

    private static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private Integer[] icons = {R.drawable.order_list_wechat, R.drawable.order_list_alipay, R.drawable.order_list_cash, R.drawable.order_list_member_pay
            , R.drawable.order_list_bank_card};
    MoreFunctionDialog dialog;
    private String[] payStatus = {"未支付", "已支付", "已退款"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        setToolBarTitle(getString(R.string.order_info));
        initData();
        initView(keyValueView);
    }

    @OnClick({R.id.printPaper, R.id.refund})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.printPaper:
                if (orderItemBean != null) {
                    if (Constant.product.equals(BaseConstant.PRODUCTS[1])) {
                        PrintManager printManager = new PrintManager(OrderInfoActivity.this);
                        printManager.startPrintReceiptByText(orderItemBean.order_no, orderItemBean.money + "元",
                                orderItemBean.type, TimeUtils.millis2String(orderItemBean.time, DEFAULT_FORMAT)
                                , TextUtils.isEmpty(orderItemBean.staff_name) ? "空" : orderItemBean.staff_name);
                    } else {
                        PrinterConnectService.printEsc0829(orderItemBean.order_no, orderItemBean.money + "元",
                                TextUtils.isEmpty(orderItemBean.staff_name) ? "空" : orderItemBean.staff_name,
                                orderItemBean.type, TimeUtils.millis2String(orderItemBean.time, DEFAULT_FORMAT));

                    }
                }

                break;
            case R.id.refund:
                Intent intent = new Intent(OrderInfoActivity.this, ReturnMoneyActivity.class);
                intent.putExtra("orderItemBean", orderItemBean);
                startActivity(intent);
                break;
        }
    }

    private void initData() {
        Intent intent = this.getIntent();
        if (intent != null) {
            orderItemBean = (OrderListResultBean.OrderItemBean) intent.getSerializableExtra("OrderItemBean");
            queryOrderBean = (QueryOrderBean) intent.getSerializableExtra("queryOrderBean");
            if (orderItemBean != null) {
                if (orderItemBean.status >= 0 && orderItemBean.status < payStatus.length) {
                    lists.add(new KeyValueStringBean("订单状态", payStatus[orderItemBean.status]));
                }
                lists.add(new KeyValueStringBean("订单号", orderItemBean.order_no));

                lists.add(new KeyValueStringBean("操作人",
                        TextUtils.isEmpty(orderItemBean.staff_name) ? "空" : orderItemBean.staff_name));
                lists.add(new KeyValueStringBean("创建时间", TimeUtils.millis2String(orderItemBean.time, DEFAULT_FORMAT)));
                if (orderItemBean.status == 2) {
                    refund.setVisibility(View.GONE);
                    printPaper.setVisibility(View.GONE);
                    getSearchOrderByNo(orderItemBean.order_no);
                }
                if (orderItemBean.type == BaseConstant.PAY_ON_MEMBER_CARD) {
                    refund.setVisibility(View.GONE);
                }

            }
        }
    }

    private void initView(RecyclerView recyclerView) {
        if (lists != null) {
            if (orderItemBean.type >= 0 && orderItemBean.type < icons.length) {
                infoIcon.setImageResource(icons[orderItemBean.type]);
            }
            if (orderItemBean.type < BaseConstant.PAY_TYPE.length) {
                payType.setText(BaseConstant.PAY_TYPE[orderItemBean.type]);
            }
            money.setText("¥" + orderItemBean.money);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            final KeyValueAdapter adapter = new KeyValueAdapter(this, lists);
            adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onClick(View view, Object item, int position) {
                }
            });
            recyclerView.setAdapter(adapter);
        }
    }

    private void getSearchOrderByNo(String orderNo) {
        HttpCall.getApiService()
                .searchOrderByNo(orderNo, Hawk.get("childBusId", 0))
                .compose(ResultTransformer.<QueryOrderBean>transformer())
                .subscribe(new BaseObserver<QueryOrderBean>() {
                    @Override
                    public void onSuccess(QueryOrderBean data) {
                        LogUtils.d(TAG, "getSearchOrderByNo onSuccess  ");
                        if (data != null) {
                            updateReturnView(data);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.d(TAG, "getSearchOrderByNo onError");
                        super.onError(e);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        LogUtils.d(TAG, "getSearchOrderByNo onFailure busId=" + Hawk.get("busId", 0));
                        super.onFailure(code, msg);
                    }
                });
    }

    private void updateReturnView(QueryOrderBean queryOrderBean) {
        if (queryOrderBean != null) {
            returnMoneyLists.add(new KeyValueStringBean("退款金额", "" + queryOrderBean.getReturn_money()));
            if (orderItemBean.type < BaseConstant.PAY_TYPE.length) {
                returnMoneyLists.add(new KeyValueStringBean("退款方式", "" + BaseConstant.PAY_TYPE[queryOrderBean.getReturn_type()]));
            }
            if (!TextUtils.isEmpty(queryOrderBean.getReturn_cause())) {
                returnMoneyLists.add(new KeyValueStringBean("退款原因", queryOrderBean.getReturn_cause()));
            }
            returnMoneyLists.add(new KeyValueStringBean("退款时间", TimeUtils.millis2String(queryOrderBean.getTime(), DEFAULT_FORMAT)));
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            returnMoneyView.setLayoutManager(layoutManager);
            final KeyValueAdapter adapter = new KeyValueAdapter(this, returnMoneyLists);
            returnMoneyView.setAdapter(adapter);
            returnMoneyView.setVisibility(View.VISIBLE);
            printPaper.setVisibility(View.VISIBLE);
        }
    }
}
