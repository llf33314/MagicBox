package com.gt.magicbox.exchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.recyclerview.LineRecyclerAdapter;
import com.gt.magicbox.base.recyclerview.MapBean;
import com.gt.magicbox.bean.ShiftRecordsBean;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.DialogTransformer;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.utils.commonutil.TimeUtils;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by wzb on 2017/8/23 0023.
 */

public class ShiftExchangeActivity extends BaseActivity {
     @BindView(R.id.rv_shift_exchange)
     RecyclerView rvExchange;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_exchange);
        setToolBarTitle("交班");
        rvExchange.setLayoutManager(new LinearLayoutManager(this));
        init();
    }

    private void init(){
        HttpCall.getApiService()
                .getNowSR((Integer) Hawk.get("eqId"),(Integer)Hawk.get("shiftId"))
                .compose(ResultTransformer.<ShiftRecordsBean>transformer())
                .compose(new DialogTransformer().<ShiftRecordsBean>transformer())
                .subscribe(new BaseObserver<ShiftRecordsBean>() {
                    @Override
                    protected void onSuccess(ShiftRecordsBean shiftRecordsBean) {
                        List<MapBean<String, String>>  listBean=getListBean(shiftRecordsBean);
                        rvExchange.setAdapter(new LineRecyclerAdapter(ShiftExchangeActivity.this,listBean));

                    }
                });
    }

    private  List<MapBean<String, String>>  getListBean(ShiftRecordsBean shiftRecordsBean){
        List<MapBean<String, String>>  listBean=new ArrayList<MapBean<String, String>>();
        listBean.add( new MapBean<String ,String>("当班人",shiftRecordsBean.getStaffName()));
        listBean.add( new MapBean<String ,String>("门店",shiftRecordsBean.getShopName()));
        listBean.add( new MapBean<String ,String>("设备号",shiftRecordsBean.getEqId()+""));
        listBean.add( new MapBean<String ,String>("接班时间", TimeUtils.getNowString()));
        listBean.add( new MapBean<String ,String>("实际支付订单数",shiftRecordsBean.getOrderInNum()+""));
        listBean.add( new MapBean<String ,String>("实际应收总额",shiftRecordsBean.getMoney()+""));
        listBean.add( new MapBean<String ,String>("微信支付",shiftRecordsBean.getWechatMoney()+""));
        listBean.add( new MapBean<String ,String>("支付宝",shiftRecordsBean.getAlipayMoney()+""));
        listBean.add( new MapBean<String ,String>("现金支付",shiftRecordsBean.getCashMoney()+""));
        return listBean;
    }


   /* private List<MapBean<String,String>> getMenu(){
        final List<MapBean<String,String>> lists=new ArrayList<>();
        Observable.range(0,7).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                lists.add(new MapBean("门店：","多粉平台"));
            }
        });
        return lists;
    }*/

    @OnClick(R.id.staff_dialog_out_work)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.staff_dialog_out_work:
                //打印下班单 并且清空shithId

                onBackPressed();

                break;
        }
    }
}
