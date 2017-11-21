package com.gt.magicbox.custom_display;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.gt.magicbox.R;

/**
 * Description:
 * Created by jack-lin on 2017/11/21 0021.
 * Buddha bless, never BUG!
 */

public class IndicatorLayout extends LinearLayout {
    private int itemSize = 4;

    public IndicatorLayout(Context context) {
        super(context);
        initView(context);
    }

    public IndicatorLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public IndicatorLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public IndicatorLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(HORIZONTAL);
        for (int i = 0; i < itemSize; i++) {
            IndicatorItem indicatorItem = new IndicatorItem(context);
            indicatorItem.setIndex(i);
            indicatorItem.setLayoutParams(new LayoutParams((int) getResources().getDimension(R.dimen.dp_80), LayoutParams.MATCH_PARENT));
            if (i == 0) {
                indicatorItem.setFirst(true);
                indicatorItem.setStatus(IndicatorItem.STATUS_SELECTED);
            } else {
                if (i == itemSize - 1) {
                    indicatorItem.setEnd(true);
                }
                indicatorItem.setStatus(IndicatorItem.STATUS_UNSELECTED);
            }
            addView(indicatorItem);
        }
    }

    public void updateView(int index) {
        for (int i = 0; i < itemSize; i++) {
            IndicatorItem indicatorItem = (IndicatorItem) getChildAt(i);
            if (indicatorItem != null) {
                if (i < index) {
                    indicatorItem.setStatus(IndicatorItem.STATUS_FINISH);
                } else if (i == index) {
                    indicatorItem.setStatus(IndicatorItem.STATUS_SELECTED);
                } else if (i > index) {
                    indicatorItem.setStatus(IndicatorItem.STATUS_UNSELECTED);
                }
            }
        }
        invalidate();
    }

}
