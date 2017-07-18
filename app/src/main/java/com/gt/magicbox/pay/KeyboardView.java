package com.gt.magicbox.pay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gt.magicbox.R;

/**
 * Description:
 * Created by jack-lin on 2017/7/18 0018.
 */

public class KeyboardView extends RelativeLayout {
    Context context;
    private GridView gridView;

    public KeyboardView(Context context) {
        this(context, null);
    }

    public KeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = View.inflate(context, R.layout.keyboard, null);
        gridView = (GridView) view.findViewById(R.id.grid_keyboard);
        KeyboardAdapter keyboardAdapter=new KeyboardAdapter(context);
        gridView.setAdapter(keyboardAdapter);
        addView(view);

    }

    public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
