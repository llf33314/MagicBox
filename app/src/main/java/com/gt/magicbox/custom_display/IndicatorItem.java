package com.gt.magicbox.custom_display;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gt.magicbox.R;

/**
 * Description:
 * Created by jack-lin on 2017/11/21 0021.
 * Buddha bless, never BUG!
 */

public class IndicatorItem extends LinearLayout {
    public static final int STATUS_UNSELECTED = 0;
    public static final int STATUS_SELECTED = 1;
    public static final int STATUS_FINISH = 2;
    private TextView numberTextView;
    private TextView tipTextView;
    private View leftLine;
    private View rightLine;
    private int status;
    private int index;
    private boolean isFirst=false;
    private boolean isEnd=false;

    private String[] tipArray = {"第一步", "第二步", "第三步", "第四步"};

    public IndicatorItem(Context context) {
        super(context);
        initView(context);
    }

    public IndicatorItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public IndicatorItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public IndicatorItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        setGravity(Gravity.CENTER_HORIZONTAL);
        View view = View.inflate(context, R.layout.item_indiactor, null);
        numberTextView = (TextView) view.findViewById(R.id.numberTextView);
        tipTextView = (TextView) view.findViewById(R.id.tipTextView);
        leftLine=view.findViewById(R.id.leftLine);
        rightLine=view.findViewById(R.id.rightLine);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        addView(view);
    }

    public void setIndex(int index) {
        if (index >= 0 && index < tipArray.length) {
            numberTextView.setText("" + (index + 1));
            tipTextView.setText(tipArray[index]);
        }
    }

    public void setStatus(int status) {
        this.status = status;
        changeViewByStatus(status);
    }

    public void setFirst(boolean first) {
        isFirst = first;
        if (leftLine!=null&&isFirst) {
            leftLine.setVisibility(GONE);
        }
    }

    public void setEnd(boolean end) {
        isEnd = end;
        if (rightLine!=null&&isEnd) {
            rightLine.setVisibility(GONE);
        }
    }

    private void changeViewByStatus(int status) {
        switch (status) {
            case STATUS_UNSELECTED:
                numberTextView.setBackgroundResource(R.drawable.match_indiactor_unselected);
                numberTextView.setTextColor(0xffbebebe);
                leftLine.setBackgroundColor(0xffbebebe);
                rightLine.setBackgroundColor(0xffbebebe);
                tipTextView.setVisibility(GONE);
                break;
            case STATUS_SELECTED:
                numberTextView.setBackgroundResource(R.drawable.match_indicator_selected);
                numberTextView.setTextColor(0xffffffff);
                tipTextView.setVisibility(VISIBLE);
                leftLine.setBackgroundColor(0xff20a0ff);
                rightLine.setBackgroundColor(0xffbebebe);
                break;
            case STATUS_FINISH:
                numberTextView.setBackgroundResource(R.drawable.match_indicator_selected);
                numberTextView.setTextColor(0xffffffff);
                tipTextView.setVisibility(GONE);
                rightLine.setBackgroundColor(0xff20a0ff);
                break;

        }
    }
}
