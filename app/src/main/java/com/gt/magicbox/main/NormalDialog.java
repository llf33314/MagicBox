package com.gt.magicbox.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gt.magicbox.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:
 * Created by jack-lin on 2017/9/6 0006.
 * Buddha bless, never BUG!
 */

public class NormalDialog extends Dialog {
    private View.OnClickListener onOkClickListener;
    private View.OnClickListener onCancelClickListener;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.confirm)
    Button confirm;
    private String titleStr;

    public NormalDialog(@NonNull Context context, String titleStr, @StyleRes int themeResId) {
        super(context,themeResId);
        this.titleStr = titleStr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login_exit);
        ButterKnife.bind(this);
        if (!TextUtils.isEmpty(titleStr))
            title.setText(titleStr);
    }

    @OnClick({R.id.cancel, R.id.confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                if (onCancelClickListener != null) {
                    onCancelClickListener.onClick(view);
                }
                dismiss();
                break;
            case R.id.confirm:
                if (onOkClickListener != null) {
                    onOkClickListener.onClick(view);
                }
                break;
        }
    }

    public void setOnOkClickListener(View.OnClickListener onClickListener) {
        this.onOkClickListener = onClickListener;
    }
    public void setOnCancelClickListener(View.OnClickListener onClickListener) {
        this.onCancelClickListener = onClickListener;
    }
}

