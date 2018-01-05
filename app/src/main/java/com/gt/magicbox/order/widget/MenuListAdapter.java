package com.gt.magicbox.order.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gt.magicbox.R;

import java.util.List;

/**
 * Created by Fussen on 16/8/31.
 */

public class MenuListAdapter extends BaseAdapter {
    private Context context;

    private ViewHolder viewHolder;

    private List<String> list;
    private int selectedPosition = 0;

    public MenuListAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.act_item_popu_list, null);
        }

        viewHolder.text1 = (TextView) convertView.findViewById(R.id.textname);
        viewHolder.text1.setText(list.get(position));
        viewHolder.selectImg = (ImageView) convertView.findViewById(R.id.selectImg);
        if (position == selectedPosition) {
            viewHolder.selectImg.setVisibility(View.VISIBLE);
            viewHolder.text1.setTextColor(0xfff04a4a);
        } else {
            viewHolder.selectImg.setVisibility(View.GONE);
            viewHolder.text1.setTextColor(0xff333333);

        }
        return convertView;
    }

    private class ViewHolder {
        private TextView text1;
        private ImageView selectImg;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

}
