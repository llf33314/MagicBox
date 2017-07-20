package com.gt.magicbox.pay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gt.magicbox.R;
import com.gt.magicbox.utils.commonutil.ConvertUtils;

/**
 * Description:
 * Created by jack-lin on 2017/7/18 0018.
 */

public class KeyboardView extends RelativeLayout implements View.OnClickListener, AdapterView.OnItemClickListener {
    Context context;
    private GridView gridView;
    private Button clear;
    private Button delete;
    private Button pay;
    private TextView showNumber;
    private StringBuffer numberString = new StringBuffer();
    private OnKeyboardDoListener onKeyboardDoListener;
    private int maxLength = 15;
    private int keyboardType;
    public static final int TYPE_INPUT_MONEY=0;
    public static final int TYPE_CHARGE=1;
    public KeyboardView(Context context) {
        this(context, null);
    }
    public KeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = View.inflate(context, R.layout.keyboard, null);
        gridView = (GridView) view.findViewById(R.id.grid_keyboard);
        clear = (Button) view.findViewById(R.id.keyboard_clear);
        delete = (Button) view.findViewById(R.id.keyboard_delete);
        pay = (Button) view.findViewById(R.id.keyboard_pay);
        showNumber = (TextView) view.findViewById(R.id.showNumber);
        clear.setOnClickListener(this);
        delete.setOnClickListener(this);
        pay.setOnClickListener(this);
        KeyboardAdapter keyboardAdapter = new KeyboardAdapter(context);
        gridView.setOnItemClickListener(this);
        gridView.setAdapter(keyboardAdapter);
        addView(view);

    }

    public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GridView getGridView() {
        return gridView;
    }

    public TextView getShowNumber() {
        return showNumber;
    }

    public void setOnKeyboardDoListener(OnKeyboardDoListener onKeyboardDoListener) {
        this.onKeyboardDoListener = onKeyboardDoListener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.keyboard_clear:
                numberString.setLength(0);
                showMoney();
                break;
            case R.id.keyboard_delete:
                if (numberString.length() > 0) {
                    numberString.deleteCharAt(numberString.length() - 1);
                    showMoney();
                }
                break;
            case R.id.keyboard_pay:
                if (onKeyboardDoListener != null) {
                    if (!TextUtils.isEmpty(numberString)) {
                        double money = Double.parseDouble(numberString.toString());
                        if (money != 0) {
                            onKeyboardDoListener.onPay(money);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (numberString.length() <= maxLength) {
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
            } else if (position == 11) {
                if (!numberString.toString().contains(".") && numberString.length() > 0)
                    numberString.append(".");
            }
            showMoney();
        }
    }
    private void showMoney(){
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        if (numberString.length()!=0) {
            spannableString.append("¥ " + numberString);
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(ConvertUtils.dp2px(20));
            spannableString.setSpan(absoluteSizeSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        showNumber.setText(spannableString);

    }

    public void setKeyboardType(int keyboardType) {
        this.keyboardType = keyboardType;
    }

}
