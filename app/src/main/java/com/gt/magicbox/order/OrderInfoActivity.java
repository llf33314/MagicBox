package com.gt.magicbox.order;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.gt.magicbox.Constant;
import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.BaseConstant;
import com.gt.magicbox.base.recyclerview.BaseRecyclerAdapter;
import com.gt.magicbox.base.recyclerview.SimpleDividerDecoration;
import com.gt.magicbox.bean.KeyValueStringBean;
import com.gt.magicbox.bean.OrderListResultBean;
import com.gt.magicbox.main.MoreFunctionDialog;
import com.gt.magicbox.order.widget.KeyValueAdapter;
import com.gt.magicbox.setting.printersetting.PrintManager;
import com.gt.magicbox.setting.printersetting.PrinterConnectService;
import com.gt.magicbox.utils.commonutil.ConvertUtils;
import com.gt.magicbox.utils.commonutil.TimeUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:
 * Created by jack-lin on 2017/9/19 0019.
 * Buddha bless, never BUG!
 */

public class OrderInfoActivity extends BaseActivity {
    @BindView(R.id.keyValueView)
    RecyclerView keyValueView;
    private OrderListResultBean.OrderItemBean orderItemBean;
    List<KeyValueStringBean> lists= new ArrayList<KeyValueStringBean>();
    private static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    MoreFunctionDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_info);
        initData();
        initRecyclerView(keyValueView);
    }

    @OnClick({R.id.printPaper, R.id.refund})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.printPaper:
                if (orderItemBean != null) {
                    if (Constant.product.equals(BaseConstant.PRODUCTS[1])) {
                        PrintManager printManager=new PrintManager(OrderInfoActivity.this);
                        printManager.startReceiptByText(orderItemBean.order_no, orderItemBean.money + "元",
                                orderItemBean.type,TimeUtils.millis2String(orderItemBean.time, DEFAULT_FORMAT)
                        , TextUtils.isEmpty(orderItemBean.staff_name)?"空":orderItemBean.staff_name);
                    } else {
                        PrinterConnectService.printEsc0829(orderItemBean.order_no, orderItemBean.money + "元",
                                TextUtils.isEmpty(orderItemBean.staff_name)?"空":orderItemBean.staff_name,
                                orderItemBean.type, TimeUtils.millis2String(orderItemBean.time, DEFAULT_FORMAT));

                    }
                }

                break;
            case R.id.refund:
                dialog = new MoreFunctionDialog(OrderInfoActivity.this, "退款功能正在开发中,敬请期待", R.style.HttpRequestDialogStyle);
                dialog.show();
                break;
        }
    }
    private void initData(){
        Intent intent=this.getIntent();
        if (intent!=null){
            orderItemBean= (OrderListResultBean.OrderItemBean) intent.getSerializableExtra("OrderItemBean");
            if (orderItemBean!=null) {
                lists.add(new KeyValueStringBean("订单号", orderItemBean.order_no));
                lists.add(new KeyValueStringBean("操作人",
                        TextUtils.isEmpty(orderItemBean.staff_name)?"空":orderItemBean.staff_name));
                lists.add(new KeyValueStringBean("创建时间", TimeUtils.millis2String(orderItemBean.time, DEFAULT_FORMAT)));
                if (orderItemBean.type<BaseConstant.PAY_TYPE.length)
                lists.add(new KeyValueStringBean("支付方式", BaseConstant.PAY_TYPE[orderItemBean.type]));
                lists.add(new KeyValueStringBean("支付金额", "¥" + orderItemBean.money));


            }
        }
    }
    private void initRecyclerView(RecyclerView recyclerView){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        final KeyValueAdapter adapter=new KeyValueAdapter(this,lists);
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, Object item, int position) {
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SimpleDividerDecoration(this,
                getResources().getColor(R.color.divide_gray_color),
                new Rect(ConvertUtils.dp2px(14),0,ConvertUtils.dp2px(14),0)));
    }
}
