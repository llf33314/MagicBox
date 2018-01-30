package com.gt.magicbox.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.recyclerview.BaseRecyclerAdapter;
import com.gt.magicbox.bean.ReasonBean;
import com.gt.magicbox.order.widget.EditReasonListAdapter;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description:
 *
 * @author jack-lin
 * @date 2018/1/30 0030
 * Buddha bless, never BUG!
 */

public class EditReasonListActivity extends BaseActivity {
    @BindView(R.id.reasonRecyclerView)
    RecyclerView reasonRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reason_list);
        setToolBarTitle(getResources().getString(R.string.return_money_reason));
    }

    private void initView() {
        final ArrayList<ReasonBean> list = Hawk.get("reasonList", new ArrayList<ReasonBean>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(EditReasonListActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        reasonRecyclerView.setLayoutManager(layoutManager);
        final EditReasonListAdapter adapter = new EditReasonListAdapter(EditReasonListActivity.this, list);
        adapter.setDeleteOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, Object item, int position) {
                final ArrayList<ReasonBean> list = Hawk.get("reasonList", new ArrayList<ReasonBean>());
                if (position > -1 && position < list.size()) {
                    list.remove(position);
                    Hawk.put("reasonList", list);
                    adapter.updateData(list);
                }
            }
        });
        adapter.setEditOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, Object item, int position) {
                Intent intent = new Intent(EditReasonListActivity.this, AddReasonTagActivity.class);
                intent.putExtra("type", AddReasonTagActivity.TYPE_EDIT);
                intent.putExtra("index", position);
                intent.putExtra("content", adapter.getItemData(position).reason);
                startActivity(intent);
            }
        });
        reasonRecyclerView.setAdapter(adapter);

    }

    @OnClick(R.id.confirmAdd)
    public void onViewClicked() {
        Intent intent = new Intent(EditReasonListActivity.this, AddReasonTagActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }
}
