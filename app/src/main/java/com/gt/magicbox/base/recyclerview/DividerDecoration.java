package com.gt.magicbox.base.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gt.magicbox.R;

/**
 * Created by wzb on 2017/9/26 0026.
 */

public class DividerDecoration extends RecyclerView.ItemDecoration{

    private Paint mPaint;

    private Context mContext;

    private int colorId= R.color.divide_gray_color;
    /**
     * 第一个上面是否有横线
     */
    private boolean isTopDivider=false;
    /**
     * 最后一个是否有横线
     */
    private boolean isBottomDivider=true;
    /**
     * 默认1px
     */
    private int dividerHeight=1;

    public DividerDecoration(Context context) {
        this.mContext=context;
        mPaint=new Paint();
        mPaint.setColor(mContext.getResources().getColor(colorId));
    }

    public DividerDecoration setColor(@ColorRes int colorId){
        mPaint.setColor(mContext.getResources().getColor(colorId));
        return this;
    }

    public DividerDecoration setColorInt(@ColorInt int color){
            mPaint.setColor(color);
        return this;
    }

    public DividerDecoration setTopDivider(boolean topDivider) {
        isTopDivider = topDivider;
        return this;
    }

    public DividerDecoration setBottomDivider(boolean bottomDivider) {
        isBottomDivider = bottomDivider;
        return this;
    }

    public DividerDecoration setDividerHeight(int dividerHeight) {
        this.dividerHeight = dividerHeight;
        return this;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        LinearLayoutManager llm= (LinearLayoutManager) parent.getLayoutManager();
        int layoutDirection= llm.getOrientation();
        super.getItemOffsets(outRect, view, parent, state);
            if (state.getItemCount()!=0&&state.getItemCount()!=-1){//有数据
                //第一个并且有上横线
                if (layoutDirection==LinearLayoutManager.VERTICAL&&parent.getChildAdapterPosition(view)==0&&isTopDivider){
                    outRect.top=dividerHeight;
                }
                if (layoutDirection==LinearLayoutManager.HORIZONTAL&&parent.getChildAdapterPosition(view)==0&&isTopDivider){
                    outRect.left=dividerHeight;
                }
                //垂直布局 不是最后一个或者底部是有线的
                if (layoutDirection==LinearLayoutManager.VERTICAL&&(parent.getChildAdapterPosition(view)!=state.getItemCount()-1||isBottomDivider)){//间距
                    outRect.bottom=dividerHeight;
                }else if(layoutDirection==LinearLayoutManager.HORIZONTAL&&(parent.getChildAdapterPosition(view)!=state.getItemCount()-1||isBottomDivider)){
                    outRect.right=dividerHeight;
                }
            }

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        LinearLayoutManager llm= (LinearLayoutManager) parent.getLayoutManager();
        int layoutDirection= llm.getOrientation();

        int paddingLeft=parent.getPaddingLeft();
        int paddingTop=parent.getPaddingTop();
        int paddingRight=parent.getPaddingRight();
        int paddingBottom=parent.getPaddingBottom();

        for (int i = 0; i < state.getItemCount(); i++) {
            View view = parent.getChildAt(i);

            //第一条线
            if (layoutDirection==LinearLayoutManager.VERTICAL&&i==0&&isTopDivider){
                c.drawRect(paddingLeft,0, parent.getWidth()-paddingRight, dividerHeight, mPaint);
            }

            if (layoutDirection==LinearLayoutManager.HORIZONTAL&&i==0&&isTopDivider){
                c.drawRect(0,paddingTop, dividerHeight, paddingBottom, mPaint);
            }

            if (layoutDirection==LinearLayoutManager.VERTICAL){
                float top = view.getBottom();
                float bottom =view.getBottom() + dividerHeight;
                c.drawRect(paddingLeft,top,parent.getWidth()-paddingRight, bottom, mPaint);
            }
            if (layoutDirection==LinearLayoutManager.HORIZONTAL){
                c.drawRect(view.getRight(), paddingTop,view.getRight()+dividerHeight,parent.getHeight()-paddingRight, mPaint);
            }
        }

    }
}
