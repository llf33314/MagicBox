package com.gt.magicbox.main;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.utils.commonutil.ConvertUtils;
import com.gt.magicbox.utils.commonutil.DrawableUtils;
import com.gt.magicbox.utils.commonutil.ScreenUtils;

import java.util.ArrayList;

/**
 * Description:
 * Created by jack-lin on 2017/7/17 0017.
 */

public class HomeGridViewAdapter extends ArrayAdapter<GridItem> {
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<GridItem> mGridData = new ArrayList<GridItem>();
    private int itemHeight;
    private int displayAreaHeight;

    public HomeGridViewAdapter(@NonNull Context context, int resource, ArrayList<GridItem> objects,int heightCount) {
        super(context, resource);
        this.mContext = context;
        this.layoutResourceId = resource;
        this.mGridData = objects;
        int screenHeight = mContext.getResources().getDisplayMetrics().heightPixels;
        int toolbarHeight =(int) mContext.getResources().getDimension(R.dimen.toolbar_height);
        displayAreaHeight = screenHeight - toolbarHeight -ScreenUtils.getStatusHeight();
        itemHeight = displayAreaHeight / heightCount ;

    }

    @Override
    public int getCount() {
        return mGridData.size();
    }

    @Override
    public GridItem getItem(int i) {
        return mGridData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            AbsListView.LayoutParams params = (AbsListView.LayoutParams) convertView.getLayoutParams();
            params.height = itemHeight;
            convertView.setLayoutParams(params);
            holder = new ViewHolder();
            holder.badge = (TextView) convertView.findViewById(R.id.badge);
            holder.textView = (TextView) convertView.findViewById(R.id.name_item);

            holder.imageView = (ImageView) convertView.findViewById(R.id.img_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GridItem item = mGridData.get(position);
        if (item != null) {
            convertView.setBackgroundDrawable(DrawableUtils.addColorStateDrawable(item.getNormalColor(), item.getFocusedColor(),
                    item.getFocusedColor()));
            holder.textView.setText(item.getName());
            holder.imageView.setBackgroundResource(item.getImgRes());
            if (item.getMessageCount()>0){
                holder.badge.setVisibility(View.VISIBLE);
                holder.badge.setText(""+item.getMessageCount());
            }else {
                holder.badge.setVisibility(View.INVISIBLE);

            }
        }
        return convertView;

    }

    private class ViewHolder {
        TextView badge;
        TextView textView;
        ImageView imageView;
    }

    public void setGridData(ArrayList<GridItem> mGridData) {
        Log.i("grid","setGridData");
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }


}
