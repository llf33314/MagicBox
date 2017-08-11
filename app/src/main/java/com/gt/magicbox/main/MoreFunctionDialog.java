package com.gt.magicbox.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gt.magicbox.R;

/**
 * Created by wzb on 2017/7/24 0024.
 */

public class MoreFunctionDialog extends Dialog {

    private TextView tvCancel;

    private String msg="";

    private Button confirmButton;
    private TextView hintMsg;
    public MoreFunctionDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public MoreFunctionDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init();
    }
    public MoreFunctionDialog(@NonNull Context context,String msg, @StyleRes int themeResId) {
        super(context, themeResId);
        this.msg=msg;
        init();

    }

    private void init(){
        setContentView(R.layout.dialog_more_function);
        tvCancel= (TextView) this.findViewById(R.id.tv_more_function_cancel);
        hintMsg= (TextView) this.findViewById(R.id.hint_msg);
        confirmButton=(Button)this.findViewById(R.id.confirm);
        if (!TextUtils.isEmpty(msg)){
            hintMsg.setText(msg);
        }
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreFunctionDialog.this.dismiss();
            }
        });

    }

    public TextView getTvCancel() {
        return tvCancel;
    }
    public Button getConfirmButton() {
        confirmButton.setVisibility(View.VISIBLE);
        return confirmButton;
    }

}
