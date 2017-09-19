package com.gt.magicbox.order.widget;

import android.content.Context;

import com.gt.magicbox.R;
import com.gt.magicbox.base.recyclerview.BaseRecyclerAdapter;
import com.gt.magicbox.base.recyclerview.BaseViewHolder;
import com.gt.magicbox.bean.KeyValueStringBean;

import java.util.HashMap;
import java.util.List;

/**
 * Description:
 * Created by jack-lin on 2017/9/19 0019.
 * Buddha bless, never BUG!
 */

public class KeyValueAdapter extends BaseRecyclerAdapter<KeyValueStringBean> {
    private int type;

    public KeyValueAdapter(Context context, List<KeyValueStringBean> listBean) {
        super(context, listBean);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_key_value;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, KeyValueStringBean bean, int position) {
        holder.setText(R.id.keyTextView, bean.key);
        holder.setText(R.id.valueTextView, bean.value);
    }
}
