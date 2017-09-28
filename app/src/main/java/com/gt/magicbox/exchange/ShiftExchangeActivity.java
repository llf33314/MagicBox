package com.gt.magicbox.exchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.recyclerview.LineRecyclerAdapter;
import com.gt.magicbox.base.recyclerview.MapBean;
import com.gt.magicbox.bean.ShiftRecordsAllBean;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.DialogTransformer;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.main.MainActivity;
import com.gt.magicbox.setting.printersetting.PrinterConnectService;
import com.gt.magicbox.utils.commonutil.TimeUtils;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wzb on 2017/8/23 0023.
 */

public class ShiftExchangeActivity extends BaseActivity {
     @BindView(R.id.rv_shift_exchange)
     RecyclerView rvExchange;

    private ShiftRecordsAllBean.ShiftRecordsBean shiftRecordsBean;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_exchange);
        setToolBarTitle("交班");
        rvExchange.setLayoutManager(new LinearLayoutManager(this));
        init();
    }

    private void init(){

        setBackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMain();
            }
        });

        HttpCall.getApiService()
                .getNowSR((Integer)Hawk.get("shiftId"))
                .compose(ResultTransformer.<ShiftRecordsAllBean>transformer())
                .compose(new DialogTransformer().<ShiftRecordsAllBean>transformer())
                .subscribe(new BaseObserver<ShiftRecordsAllBean>() {
                    @Override
                    protected void onSuccess(ShiftRecordsAllBean shiftRecordsAllBean) {
                        shiftRecordsBean=shiftRecordsAllBean.getShiftRecords();
                        List<MapBean<String, String>>  listBean=getListBean(shiftRecordsBean);
                        rvExchange.setAdapter(new LineRecyclerAdapter(ShiftExchangeActivity.this,listBean));
                    }
                });
    }

    private  List<MapBean<String, String>>  getListBean(ShiftRecordsAllBean.ShiftRecordsBean shiftRecordsBean){
        List<MapBean<String, String>>  listBean=new ArrayList<MapBean<String, String>>();
        listBean.add( new MapBean<String ,String>("当班人：", shiftRecordsBean.getStaffName()));
        listBean.add( new MapBean<String ,String>("门店：", shiftRecordsBean.getShopName()));
        listBean.add( new MapBean<String ,String>("设备号：", shiftRecordsBean.getEqId()+""));
        listBean.add( new MapBean<String ,String>("上班时间：",shiftRecordsBean.getStartTime()));
        listBean.add( new MapBean<String ,String>("实际支付订单数：", shiftRecordsBean.getOrderInNum()+""));
        listBean.add( new MapBean<String ,String>("实际应收总额：", shiftRecordsBean.getMoney()+""));
        listBean.add( new MapBean<String ,String>("微信支付：", shiftRecordsBean.getWechatMoney()+""));
        listBean.add( new MapBean<String ,String>("支付宝：", shiftRecordsBean.getAlipayMoney()+""));
        listBean.add( new MapBean<String ,String>("现金支付：", shiftRecordsBean.getCashMoney()+""));
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
                Hawk.put("shiftId",0);
                Hawk.put("StaffListBean",null);

                if (shiftRecordsBean!=null){
                    PrinterConnectService.printEscExchange(shiftRecordsBean);
                }

                onBackPressed();
                break;
        }
    }

    private void startMain(){
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            startMain();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
