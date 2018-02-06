package com.gt.magicbox.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gt.magicbox.R;

/**
 * Description:
 * Created by jack-lin on 2018/1/27 0027.
 * Buddha bless, never BUG!
 */

public class TextDialogToast {
    private Toast mToast;

    private TextDialogToast(Context context, CharSequence text, int duration) {
        View v = LayoutInflater.from(context).inflate(R.layout.toast_tip_dialog, null);
        TextView textView = (TextView) v.findViewById(R.id.dialog_dismiss_msg);
        textView.setText(text);
        mToast = new Toast(context);
        mToast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        mToast.setDuration(duration);
        mToast.setView(v);
    }

    public static TextDialogToast makeText(Context context, CharSequence text, int duration) {
        return new TextDialogToast(context, text, duration);
    }

    public void show() {
        if (mToast != null) {
            mToast.show();
        }
    }

    public void setGravity(int gravity, int xOffset, int yOffset) {
        if (mToast != null) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }
    }
}
