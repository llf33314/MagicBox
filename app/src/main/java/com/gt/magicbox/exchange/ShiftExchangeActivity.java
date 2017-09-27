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
        rvExchange.setAdapter(new LineRecyclerAdapter(this,getMenu()));
    }

    private List<MapBean<String,String>> getMenu(){
        final List<MapBean<String,String>> lists=new ArrayList<>();
        Observable.range(0,7).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                lists.add(new MapBean("门店：","多粉平台"));
            }
        });
        return lists;

    }

    @OnClick(R.id.staff_dialog_out_work)
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.staff_dialog_out_work:
                onBackPressed();
                break;
        }
    }
}
