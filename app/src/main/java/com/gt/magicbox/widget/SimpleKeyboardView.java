package com.gt.magicbox.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.gt.magicbox.R;
import com.gt.magicbox.pay.KeyboardAdapter;
import com.gt.magicbox.pay.OnKeyboardDoListener;
import com.gt.magicbox.utils.commonutil.LogUtils;

/**
 * Description:
 * Created by jack-lin on 2017/11/20 0018.
 */

public class SimpleKeyboardView extends RelativeLayout implements View.OnClickListener, AdapterView.OnItemClickListener {
    Context context;
    private GridView gridView;
    private Button clear;
    private Button delete;
    private Button pay;
    private OnInputListener onInputListener;
    private StringBuffer numberString = new StringBuffer();
    private OnKeyboardDoListener onKeyboardDoListener;
    private int maxLength = 4;
    private boolean isDotLimit = false;

    public SimpleKeyboardView(Context context) {
        this(context, null);
    }

    public SimpleKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = View.inflate(context, R.layout.keyboard_simple, null);
        gridView = (GridView) view.findViewById(R.id.grid_keyboard);
        clear = (Button) view.findViewById(R.id.keyboard_clear);
        delete = (Button) view.findViewById(R.id.keyboard_delete);
        pay = (Button) view.findViewById(R.id.keyboard_pay);
        clear.setOnClickListener(this);
        delete.setOnClickListener(this);
        pay.setOnClickListener(this);
        KeyboardAdapter keyboardAdapter = new KeyboardAdapter(context);
        gridView.setOnItemClickListener(this);
        gridView.setAdapter(keyboardAdapter);
        addView(view);

    }

    public SimpleKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setOnKeyboardDoListener(OnKeyboardDoListener onKeyboardDoListener) {
        this.onKeyboardDoListener = onKeyboardDoListener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.keyboard_clear:
                clearAll();
                break;
            case R.id.keyboard_delete:
                backspace();
                break;
            case R.id.keyboard_pay:
                enter();
                break;
        }
        if (onInputListener != null) onInputListener.onInput(numberString.toString());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (numberString.length() < maxLength) {
            if (position <= 10 && numberString.toString().contains(".")
                    && numberString.toString().substring(numberString.toString().indexOf("."), numberString.toString().length()).length() > 2) {
                return;
            }
            if (position <= 8) {
                if (numberString.toString().equals("0")) {
                    numberString.deleteCharAt(0);
                }
                numberString.append("" + (position + 1));
            } else if (position == 9) {
                if (numberString.length() > 0 && !numberString.toString().equals("0"))
                    numberString.append("00");
            } else if (position == 10) {
                if (!numberString.toString().equals("0"))
                    numberString.append("0");
            } else if (position == 11 && !isDotLimit) {
                if (!numberString.toString().contains(".") && numberString.length() > 0)
                    numberString.append(".");
            }
            if (onInputListener != null) onInputListener.onInput(numberString.toString());
        }
    }


    public void input(String str) {
        LogUtils.i("keycode", "input numberString=" + numberString.toString());

        if (numberString.length() <= maxLength) {
            if (numberString.toString().equals("0") && !str.equals(".")) {
                LogUtils.i("keycode", "input numberString.deleteCharAt(0)=" + numberString.toString());
                numberString.deleteCharAt(0);
            }
            if (str.equals(".") && (numberString.toString().contains(".") && numberString.length() == 0))//已经有小数点了或者小数点不能作为开头
                return;
            if (numberString.toString().contains(".")
                    && numberString.toString().substring(numberString.toString().indexOf("."), numberString.toString().length()).length() > 2)
                return;
            numberString.append(str);
        }
    }

    public void clearAll() {
        numberString.setLength(0);
    }

    public void backspace() {
        if (numberString.length() > 0) {
            numberString.deleteCharAt(numberString.length() - 1);
        }
    }

    public void enter() {
        if (onKeyboardDoListener != null) {
            if (!TextUtils.isEmpty(numberString)) {
                onInputListener.onEnter(numberString.toString());
            }
        }
    }

    public interface OnInputListener {
        void onInput(String input);

        void onEnter(String endInput);
    }

    public void setOnInputListener(OnInputListener onInputListener) {
        this.onInputListener = onInputListener;
    }

}
