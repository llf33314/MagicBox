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

public class PayFinishToast {
    private Toast mToast;

    private PayFinishToast(Context context, CharSequence text, int duration) {
        View v = LayoutInflater.from(context).inflate(R.layout.fixed_money_pay_finish, null);
        TextView textView = (TextView) v.findViewById(R.id.finishText);
        textView.setText(text);
        mToast = new Toast(context);
        mToast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        mToast.setDuration(duration);
        mToast.setView(v);
    }

    public static PayFinishToast makeText(Context context, CharSequence text, int duration) {
        return new PayFinishToast(context, text, duration);
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
