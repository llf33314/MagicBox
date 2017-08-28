package com.gt.magicbox.base.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by wzb on 2017/8/24 0024.
 */

public  class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    int mSpace;
    public SpaceItemDecoration(int space) {
        this.mSpace = space;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) !=state.getItemCount()-1) {
            outRect.right = mSpace;
        }
    }
    public int getSpace(){
        return this.mSpace;
    }
}

