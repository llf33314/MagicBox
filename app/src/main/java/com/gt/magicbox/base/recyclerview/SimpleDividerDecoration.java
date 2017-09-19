package com.gt.magicbox.base.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gt.magicbox.R;

/**
 * Description:
 * Created by jack-lin on 2017/9/19 0019.
 * Buddha bless, never BUG!
 */

public class SimpleDividerDecoration extends RecyclerView.ItemDecoration {

    private int dividerHeight;
    private Paint dividerPaint;
    private Rect margin;

    public SimpleDividerDecoration(Context context,int dividerColor,Rect margin) {
        dividerPaint = new Paint();
        dividerPaint.setColor(dividerColor);
        dividerHeight = context.getResources().getDimensionPixelSize(R.dimen.dp_1);
        this.margin=margin;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = dividerHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - margin.right;

        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + dividerHeight;
            c.drawRect(margin.left, top,right, bottom, dividerPaint);
        }
    }
}
