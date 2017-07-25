package com.gt.magicbox.utils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by wzb on 2017/7/25 0025.
 */

public class PressedImageView extends ImageView {

    private boolean pressed=false;
    public PressedImageView(Context context) {
        super(context);
       // setClickable(true);
    }

    public PressedImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
      //  setClickable(true);
    }

    public PressedImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
     //   setClickable(true);
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        this.pressed=pressed;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (pressed){
            canvas.drawARGB(20,0,0,0);
        }
    }
}
