package com.gt.magicbox.custom_display;

import android.content.Context;

import com.gt.magicbox.R;
import com.gt.magicbox.base.recyclerview.BaseRecyclerAdapter;
import com.gt.magicbox.base.recyclerview.BaseViewHolder;
import com.gt.magicbox.bean.BaudRateItemBean;

import java.util.List;

/**
 * Created by lgx on 2017/11/22 0024.
 */

public class BaudRecyclerViewAdapter extends BaseRecyclerAdapter<BaudRateItemBean> {

    public BaudRecyclerViewAdapter(Context context, List<BaudRateItemBean> listBean) {
        super(context, listBean);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_baud;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, BaudRateItemBean bean, int position) {
        holder.setText(R.id.item_name, "" + bean.baudRate);
        holder.findView(R.id.item_name).setPressed(bean.isSelected);

    }
}
