package com.gt.magicbox.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.utils.commonutil.ScreenUtils;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.*;
import com.wx.wheelview.widget.WheelViewDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wzb on 2017/7/25 0025.
 */

public class WheelDialog extends Dialog {

    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.confirm)
    TextView confirm;


    @BindView(R.id.wheelView)
    WheelView wheelView;


    List<String> list;
    private WheelView.WheelViewStyle mStyle;

    private View.OnClickListener onClickListener;

    private WheelView.OnWheelItemSelectedListener  onWheelItemSelectedListener;

    public WheelDialog(@NonNull Context context) {
        super(context);
    }

    public WheelDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_wheel_view);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.height = ScreenUtils.getScreenHeight() / 2;
        lp.width = ScreenUtils.getScreenWidth();
        lp.y = (int) getContext().getResources().getDimension(R.dimen.dp_200);
        window.setAttributes(lp);

        wheelView.setSkin(WheelView.Skin.None);
        wheelView.setWheelAdapter(new ArrayWheelAdapter(getContext()));
        mStyle = new WheelView.WheelViewStyle();
        mStyle.textColor = Color.GRAY;
        mStyle.selectedTextZoom = 1.2f;
        wheelView.setStyle(mStyle);

        if (list!=null&&list.size()>0){
            wheelView.setWheelData(list);
        }
        if (onWheelItemSelectedListener!=null){
            wheelView.setOnWheelItemSelectedListener(onWheelItemSelectedListener);
        }

        if (onClickListener!=null){
            cancel.setOnClickListener(onClickListener);
            confirm.setOnClickListener(onClickListener);
        }

    }


    public void setOnWheelItemSelectedListener(WheelView.OnWheelItemSelectedListener onWheelItemSelectedListener) {
        this.onWheelItemSelectedListener = onWheelItemSelectedListener;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
