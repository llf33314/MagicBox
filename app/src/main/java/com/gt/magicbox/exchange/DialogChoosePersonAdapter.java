package com.gt.magicbox.exchange;

import android.content.Context;

import com.gt.magicbox.R;
import com.gt.magicbox.base.recyclerview.BaseRecyclerAdapter;
import com.gt.magicbox.base.recyclerview.BaseViewHolder;
import com.gt.magicbox.bean.StaffBean;

import java.util.List;

/**
 * Created by wzb on 2017/9/25 0025.
 */

public class DialogChoosePersonAdapter extends BaseRecyclerAdapter<StaffBean.StaffListBean> {

    public DialogChoosePersonAdapter(Context context, List<StaffBean.StaffListBean> listBean) {
        super(context, listBean);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_recyclerview_tv;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, StaffBean.StaffListBean bean, int position) {
        holder.setText(R.id.recyclerview_tv,bean.getName());
    }
}
