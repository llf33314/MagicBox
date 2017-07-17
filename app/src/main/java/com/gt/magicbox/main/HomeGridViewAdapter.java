package com.gt.magicbox.main;

import android.app.Activity;
import android.content.Context;
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
    public HomeGridViewAdapter(@NonNull Context context, int resource, ArrayList<GridItem> objects) {
        super(context, resource);
        this.mContext = context;
        this.layoutResourceId = resource;
        this.mGridData = objects;
        int  screenHeight= ScreenUtils.getScreenHeight();
        int  toolbarHeight= ConvertUtils.px2dp(context.getResources().getDimension(R.dimen.toolbar_height));
        displayAreaHeight=screenHeight-toolbarHeight-ScreenUtils.getBottomStatusHeight();
        itemHeight=displayAreaHeight/3+3;

        Log.i("test","screenHeight="+screenHeight+"  toolbarHeight="+toolbarHeight+"   BottomStatusHeight()=" +ScreenUtils.getBottomStatusHeight()+
                "  getStatusHeight="+ScreenUtils.getStatusHeight()+"  itemHeight="+itemHeight);
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
            AbsListView.LayoutParams params= (AbsListView.LayoutParams) convertView.getLayoutParams();
            params.height=itemHeight;
            convertView.setLayoutParams(params);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.name_item);
            holder.imageView = (ImageView) convertView.findViewById(R.id.img_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GridItem item = mGridData.get(position);
        if (item != null) {
            convertView.setBackgroundColor(item.getColor());
            holder.textView.setText(item.getName());
            holder.imageView.setBackgroundResource(item.getImgRes());
        }
        return convertView;

    }

    private class ViewHolder {
        TextView textView;
        ImageView imageView;
    }

    public void setGridData(ArrayList<GridItem> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }
}
