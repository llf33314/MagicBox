package com.gt.magicbox.order.widget;

import android.content.Context;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.recyclerview.BaseRecyclerAdapter;
import com.gt.magicbox.base.recyclerview.BaseViewHolder;
import com.gt.magicbox.bean.FixedMoneyBean;
import com.gt.magicbox.bean.ReasonBean;
import com.gt.magicbox.utils.CashierInputFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lgx
 * @date 2017/11/22 0024
 */

public class EditReasonListAdapter extends BaseRecyclerAdapter<ReasonBean> {
    private OnItemClickListener deleteOnItemClickListener;
    private OnItemClickListener editOnItemClickListener;
    private TextView reasonText;

    public EditReasonListAdapter(Context context, List<ReasonBean> listBean) {
        super(context, listBean);

    }

    @Override
    public int getLayoutId() {
        return R.layout.item_reason_list_edit;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final ReasonBean bean, final int position) {
        reasonText = (TextView) holder.findView(R.id.reasonItem);
        if (!TextUtils.isEmpty(bean.reason)) {
            reasonText.setText(bean.reason);
        } else {
            reasonText.setText("");
        }
        if (deleteOnItemClickListener != null) {
            holder.findView(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteOnItemClickListener.onClick(v, bean, position);
                }
            });
        }
        if (editOnItemClickListener != null) {
            holder.findView(R.id.edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editOnItemClickListener.onClick(v, bean, position);
                }
            });
        }
    }

    public void setDeleteOnItemClickListener(OnItemClickListener deleteOnItemClickListener) {
        this.deleteOnItemClickListener = deleteOnItemClickListener;
    }

    public void setEditOnItemClickListener(OnItemClickListener editOnItemClickListener) {
        this.editOnItemClickListener = editOnItemClickListener;
    }

    public void updateData(ArrayList<ReasonBean> list) {
        this.listBean = list;
        notifyDataSetChanged();
    }

    public ReasonBean getItemData(int position) {
        return listBean.get(position);
    }
}
