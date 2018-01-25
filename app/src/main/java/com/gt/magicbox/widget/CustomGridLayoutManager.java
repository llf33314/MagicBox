package com.gt.magicbox.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * Description:
 * Created by jack-lin on 2018/1/25 0025.
 * Buddha bless, never BUG!
 */

public class CustomGridLayoutManager extends GridLayoutManager {
    private boolean isScrollEnabled = true;

    public CustomGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public CustomGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }


    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}