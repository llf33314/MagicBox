package com.gt.magicbox.base.recyclerview;

import android.content.Context;

import com.gt.magicbox.R;

import java.util.List;
import java.util.Map;

/**
 * Created by wzb on 2017/8/28 0028.
 */

public class LineRecyclerAdapter extends BaseRecyclerAdapter<MapBean<String,String>>{

    public LineRecyclerAdapter(Context context, List<MapBean<String, String>> listBean) {
        super(context, listBean);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_shift_change;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, MapBean<String, String> bean, int position) {
            holder.setText(R.id.tv_exchange_key,bean.getKey())
                    .setText(R.id.tv_exchange_value,bean.getValue());
    }
}
