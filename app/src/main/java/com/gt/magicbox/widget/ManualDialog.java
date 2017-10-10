package com.gt.magicbox.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import com.gt.magicbox.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 */

public class ManualDialog extends Dialog {


    public ManualDialog(@NonNull Context context) {
        this(context, R.style.HttpRequestDialogStyle);
    }

    public ManualDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_manual);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.close)
    public void onViewClicked() {
        dismiss();
    }
}
