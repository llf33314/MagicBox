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
import android.widget.TextView;

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
        if (position <= 8) {
            ((TextView)convertView).setText("" + (position + 1));
        } else if (position == 9) {
            ((TextView)convertView).setText("00");
        } else if (position == 10) {
            ((TextView)convertView).setText("0");
        } else if (position == 11) {
            ((TextView)convertView).setText("·");
        }
//        convertView.setBackgroundDrawable(DrawableUtils.addImageStateDrawable(itemNormalRes[position]
//                , itemPressedRes[position], itemPressedRes[position]));
        return convertView;
    }

}
