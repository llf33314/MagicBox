package com.gt.magicbox.pay;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.gt.magicbox.R;
import com.gt.magicbox.utils.commonutil.ConvertUtils;
import com.gt.magicbox.utils.commonutil.DrawableUtils;
import com.gt.magicbox.utils.commonutil.ScreenUtils;
import com.gt.magicbox.utils.commonutil.Utils;

import java.util.ArrayList;

/**
 * Description:
 * Created by jack-lin on 2017/7/18 0018.
 */

public class KeyboardAdapter extends BaseAdapter {
    private Context mContext;
    private int count = 3 * 4;
    private int itemHeight;
    private int displayAreaHeight;
    private Integer[] itemNormalRes = {R.drawable.keyboard_1, R.drawable.keyboard_2, R.drawable.keyboard_3
            , R.drawable.keyboard_4, R.drawable.keyboard_5, R.drawable.keyboard_6
            , R.drawable.keyboard_7, R.drawable.keyboard_8, R.drawable.keyboard_9
            , R.drawable.keyboard_00, R.drawable.keyboard_0, R.drawable.keyboard_dot};
    private Integer[] itemPressedRes = {R.drawable.keyboard_1_pressed, R.drawable.keyboard_2_pressed, R.drawable.keyboard_3_pressed
            , R.drawable.keyboard_4_pressed, R.drawable.keyboard_5_pressed, R.drawable.keyboard_6_pressed
            , R.drawable.keyboard_7_pressed, R.drawable.keyboard_8_pressed, R.drawable.keyboard_9_pressed
            , R.drawable.keyboard_00_pressed, R.drawable.keyboard_0_pressed, R.drawable.keyboard_dot_pressed};

    public KeyboardAdapter(Context context) {
        this.mContext = context;
        int screenHeight = ScreenUtils.getScreenHeight();
        int toolbarHeight = ConvertUtils.px2dp(context.getResources().getDimension(R.dimen.toolbar_height));
        displayAreaHeight = screenHeight - toolbarHeight - ScreenUtils.getBottomStatusHeight();
        itemHeight = displayAreaHeight / 6;

    }


    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parentView) {
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.keyboard_item, parentView, false);
            AbsListView.LayoutParams params = (AbsListView.LayoutParams) convertView.getLayoutParams();
            params.height = itemHeight;
            convertView.setLayoutParams(params);
            convertView.setTag(convertView);
        } else {
            convertView = (View) convertView.getTag();
        }
        convertView.setBackgroundDrawable(DrawableUtils.addImageStateDrawable(itemNormalRes[position]
                , itemPressedRes[position], itemPressedRes[position]));
        return convertView;
    }

}
