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
        int screenHeight = ScreenUtils.getScreenHeight();
        int toolbarHeight = ConvertUtils.px2dp(context.getResources().getDimension(R.dimen.toolbar_height));
        displayAreaHeight = screenHeight - toolbarHeight - ScreenUtils.getBottomStatusHeight();
        itemHeight = displayAreaHeight / 3 + ConvertUtils.dp2px(2);

        Log.i("test", "screenHeight=" + screenHeight + "  toolbarHeight=" + toolbarHeight + "   BottomStatusHeight()=" + ScreenUtils.getBottomStatusHeight() +
                "  getStatusHeight=" + ScreenUtils.getStatusHeight() + "  itemHeight=" + itemHeight);
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
            holder.textView = (TextView) convertView.findViewById(R.id.name_item);
            holder.imageView = (ImageView) convertView.findViewById(R.id.img_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GridItem item = mGridData.get(position);
        if (item != null) {
            convertView.setBackgroundDrawable(addStateDrawable(item.getNormalColor(), item.getFocusedColor(),
                    item.getFocusedColor()));
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

    private StateListDrawable addStateDrawable(int colorNormal, int colorPressed, int colorFocused) {
        StateListDrawable sd = new StateListDrawable();
        Drawable normal = colorNormal == -1 ? null : new ColorDrawable(colorNormal);
        Drawable pressed = colorPressed == -1 ? null : new ColorDrawable(colorPressed);
        Drawable focus = colorFocused == -1 ? null : new ColorDrawable(colorFocused);
        //注意该处的顺序，只要有一个状态与之相配，背景就会被换掉
        //所以不要把大范围放在前面了，如果sd.addState(new[]{},normal)放在第一个的话，就没有什么效果了
        sd.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, focus);
        sd.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        sd.addState(new int[]{android.R.attr.state_focused}, focus);
        sd.addState(new int[]{android.R.attr.state_pressed}, pressed);
        sd.addState(new int[]{android.R.attr.state_enabled}, normal);
        sd.addState(new int[]{}, normal);
        return sd;
    }
}
