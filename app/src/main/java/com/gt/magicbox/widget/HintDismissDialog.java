package com.gt.magicbox.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gt.magicbox.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wzb on 2017/9/25 0025.
 * 简单提示对话框
 */

public class HintDismissDialog extends Dialog {
    @BindView(R.id.dialog_dismiss_msg)
    TextView tvMsg;

    @BindView(R.id.dialog_dismiss_cancel)
    Button btnCancel;
    @BindView(R.id.dialog_dismiss_ok)
    Button btnOk;


    View.OnClickListener onOkClickListener;
    View.OnClickListener onCancelClickListener;

    public boolean isShowBtnOk=false;
    public boolean isShowBtnCancel=false;

    private String okText;
    private String cancelText;

    private String msg="";

    private HintDismissDialog(@NonNull Context context) {
        super(context);
    }

    private HintDismissDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }
    public HintDismissDialog(@NonNull Context context,String msg) {
        this(context,R.style.HttpRequestDialogStyle,msg);
    }

    public HintDismissDialog(@NonNull Context context, @StyleRes int themeResId,String msg) {
        super(context, themeResId);
        this.msg=msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_dismiss_hint);
        ButterKnife.bind(this);
        if (!TextUtils.isEmpty(msg)){
            tvMsg.setText(msg);
        }
        if (isShowBtnOk){
            btnOk.setVisibility(View.VISIBLE);
        }
        if (isShowBtnCancel){
            btnCancel.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(okText)){
            btnOk.setText(okText);
        }
        if (!TextUtils.isEmpty(cancelText)){
            btnCancel.setText(cancelText);
        }
    }

    @OnClick({R.id.dialog_dismiss_ok,R.id.dialog_dismiss_cancel})
    public void onViewClicked(View v) {
        switch (v.getId()){
            case R.id.dialog_dismiss_ok:
                if (onOkClickListener!=null){
                    onOkClickListener.onClick(v);
                }else{
                    this.dismiss();
                }
                break;
            case R.id.dialog_dismiss_cancel:
                if (onCancelClickListener!=null){
                    onCancelClickListener.onClick(v);
                }else{
                    this.dismiss();
                }
                break;
        }

    }

    public HintDismissDialog setOnOkClickListener(@Nullable View.OnClickListener onOkClickListener) {
        isShowBtnOk=true;
        this.onOkClickListener = onOkClickListener;
        if (btnOk!=null){
            btnOk.setVisibility(View.VISIBLE);
        }

        return this;
    }

    public HintDismissDialog setOnCancelClickListener(@Nullable View.OnClickListener onCancelClickListener) {
        isShowBtnCancel=true;
        this.onCancelClickListener = onCancelClickListener;
        if (btnCancel!=null){//没有执行show之前
            btnCancel.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public HintDismissDialog setOkText(String text){
        okText=text;
        if (btnOk!=null){
            btnOk.setText(text);
        }
        return this;
    }
    public HintDismissDialog setCancelText(String text){
        cancelText=text;
        if (btnCancel!=null){
            btnCancel.setText(text);
        }
        return this;
    }
}
