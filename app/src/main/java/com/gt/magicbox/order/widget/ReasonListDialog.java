package com.gt.magicbox.order.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.gt.magicbox.R;
import com.gt.magicbox.base.recyclerview.SpaceItemDecoration;
import com.gt.magicbox.bean.MemberCouponBean;
import com.gt.magicbox.bean.ReasonBean;
import com.gt.magicbox.utils.commonutil.ConvertUtils;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.gt.magicbox.utils.commonutil.ScreenUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wzb on 2017/7/25 0025.
 */

public class ReasonListDialog extends Dialog {

    @BindView(R.id.reasonRecyclerView)
    RecyclerView reasonRecyclerView;
    private Context context;
    private List<ReasonBean> list;
    private int lastPosition = -1;

    public ReasonListDialog(@NonNull Context context, List<ReasonBean> list) {
        super(context);
        this.context = context;
        this.list = list;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reason_dialog);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.height = ScreenUtils.getScreenHeight() / 2;
        lp.width = ScreenUtils.getScreenWidth();
        lp.y = (int) getContext().getResources().getDimension(R.dimen.dp_200);
        window.setAttributes(lp);
        initReasonRecyclerView(reasonRecyclerView,list);
    }

    private void initReasonRecyclerView(RecyclerView recyclerView, final List<ReasonBean> data) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        final ReasonCheckStateAdapter adapter = new ReasonCheckStateAdapter(context, data);
        adapter.setOnItemClickListener(new ReasonCheckStateAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, ReasonCheckStateAdapter.StateHolder holder, int position) {
                LogUtils.d("lastPosition=" + lastPosition + "  position=" + position);
                if (lastPosition != position) {
                    lastPosition = position;
                } else {
                    lastPosition = -1;
                }
            }
        });
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (adapter != null) {
                    adapter.updateCurrentHolder();
                }
                return false;
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(ConvertUtils.dp2px(5), SpaceItemDecoration.SPACE_RIGHT));

    }

    @OnClick(R.id.close)
    public void onViewClicked() {
    }
}
