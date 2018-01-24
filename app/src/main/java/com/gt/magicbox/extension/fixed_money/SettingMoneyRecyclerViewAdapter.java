package com.gt.magicbox.extension.fixed_money;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.gt.magicbox.R;
import com.gt.magicbox.base.recyclerview.BaseRecyclerAdapter;
import com.gt.magicbox.base.recyclerview.BaseViewHolder;
import com.gt.magicbox.bean.BaudRateItemBean;
import com.gt.magicbox.bean.FixedMoneyBean;
import com.gt.magicbox.utils.CashierInputFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lgx on 2017/11/22 0024.
 */

public class SettingMoneyRecyclerViewAdapter extends BaseRecyclerAdapter<FixedMoneyBean> {
    private OnItemClickListener deleteOnItemClickListener;
    private OnItemClickListener editOnItemClickListener;
    private EditText editText;
    private RelativeLayout editMoneyLayout;
    InputFilter[] filters;
    private boolean isEdit = false;

    public SettingMoneyRecyclerViewAdapter(Context context, List<FixedMoneyBean> listBean) {
        super(context, listBean);
        filters = new InputFilter[]{new CashierInputFilter(50000)};

    }

    @Override
    public int getLayoutId() {
        return R.layout.item_setting_money;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final FixedMoneyBean bean, final int position) {
        editText = (EditText) holder.findView(R.id.editMoney);
        editMoneyLayout = (RelativeLayout) holder.findView(R.id.editMoneyLayout);
        if (isEdit) {
            editMoneyLayout.setVisibility(View.VISIBLE);
        } else {
            editMoneyLayout.setVisibility(View.GONE);
        }
        editText.setFilters(filters);
        if (bean.money > 0) {
            editText.setText("" + bean.money);
        } else if (bean.money == 0) {
            editText.setText("");

        }
        holder.setText(R.id.title, mContext.getString(R.string.fixed_money) + (position + 1));
        holder.setText(R.id.money, "¥" + bean.money);
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
        if (bean.money ==-1) {
            holder.findView(R.id.noData).setVisibility(View.VISIBLE);
        }
    }

    public void setDeleteOnItemClickListener(OnItemClickListener deleteOnItemClickListener) {
        this.deleteOnItemClickListener = deleteOnItemClickListener;
    }

    public void setEditOnItemClickListener(OnItemClickListener editOnItemClickListener) {
        this.editOnItemClickListener = editOnItemClickListener;
    }

    public EditText getEditText() {
        return editText;
    }

    public void setEditMode(ArrayList<FixedMoneyBean> list) {
        this.listBean = list;
        isEdit = true;
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<FixedMoneyBean> list) {
        this.listBean = list;
        isEdit = false;
        notifyDataSetChanged();
    }
}
