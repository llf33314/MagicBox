package com.gt.magicbox.base.recyclerview;

import android.content.Context;

import com.gt.magicbox.R;

import java.util.List;

/**
 * Created by wzb on 2017/9/25 0025.
 */

public class TextViewRecyclerAdapter extends BaseRecyclerAdapter<String> {

    public TextViewRecyclerAdapter(Context context, List<String> listBean) {
        super(context, listBean);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_recyclerview_tv;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, String bean, int position) {
            holder.setText(R.id.recyclerview_tv,bean);
    }
}
