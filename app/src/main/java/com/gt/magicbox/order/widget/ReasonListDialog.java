package com.gt.magicbox.order.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.gt.magicbox.bean.ReasonBean;
import com.gt.magicbox.order.AddReasonTagActivity;
import com.gt.magicbox.order.EditReasonListActivity;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.gt.magicbox.utils.commonutil.ScreenUtils;

import java.util.ArrayList;
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

    private ReasonCheckStateAdapter adapter;
    private ReasonCheckStateAdapter.OnItemClickListener onItemClickListener;

    public ReasonListDialog(@NonNull Context context, List<ReasonBean> list) {
        super(context);
        this.context = context;
        this.list = list;
    }

    public ReasonListDialog(@NonNull Context context, List<ReasonBean> list, @StyleRes int themeResId) {
        super(context, themeResId);
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
        lp.height = (int) getContext().getResources().getDimension(R.dimen.dp_266);
        lp.width = ScreenUtils.getScreenWidth();
        lp.y = (int) getContext().getResources().getDimension(R.dimen.dp_266);
        window.setAttributes(lp);
        initReasonRecyclerView(reasonRecyclerView, list);
    }

    private void initReasonRecyclerView(RecyclerView recyclerView, final List<ReasonBean> data) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReasonCheckStateAdapter(context, data);
        adapter.setOnItemClickListener(new ReasonCheckStateAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, ReasonCheckStateAdapter.StateHolder holder, int position) {
                if (onItemClickListener != null) {
                    onItemClickListener.OnItemClick(view, holder, position);
                }
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

    }


    public void setOnItemClickListener(ReasonCheckStateAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @OnClick({R.id.addReason, R.id.close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close:
                if (isShowing()) {
                    dismiss();
                }
                break;
            case R.id.addReason:
                Intent intent = new Intent(context, EditReasonListActivity.class);
                context.startActivity(intent);
                break;
        }

    }

    public void update(ArrayList<ReasonBean> list) {
        adapter.updateAdapter(list);
    }
}
