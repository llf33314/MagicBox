package com.gt.magicbox.order;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.BaseConstant;
import com.gt.magicbox.base.recyclerview.BaseRecyclerAdapter;
import com.gt.magicbox.base.recyclerview.SimpleDividerDecoration;
import com.gt.magicbox.bean.KeyValueStringBean;
import com.gt.magicbox.order.widget.KeyValueAdapter;
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
    @BindView(R.id.printPaper)
    Button printPaper;
    @BindView(R.id.refund)
    Button refund;
    List<KeyValueStringBean> lists= new ArrayList<KeyValueStringBean>();
    private static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());


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
                break;
            case R.id.refund:
                break;
        }
    }
    private void initData(){
        Intent intent=this.getIntent();
        if (intent!=null){
            String orderNo=intent.getStringExtra("orderNo");
            long time=intent.getLongExtra("time",0);
            int payType=intent.getIntExtra("type",0);
            double money=intent.getDoubleExtra("money",0);
            if (!TextUtils.isEmpty(orderNo))
            lists.add(new KeyValueStringBean("订单号",orderNo));
            lists.add(new KeyValueStringBean("操作人","张三"));
            lists.add(new KeyValueStringBean("创建时间",TimeUtils.millis2String(time,DEFAULT_FORMAT)));
            lists.add(new KeyValueStringBean("支付方式", BaseConstant.PAY_TYPE[payType]));
            lists.add(new KeyValueStringBean("支付金额","¥"+money));



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
