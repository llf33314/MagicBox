package com.gt.magicbox.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.widget.ImageView;

import com.gt.magicbox.R;
import com.gt.magicbox.utils.qr_code_util.QrCodeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author lgx
 */

public class ReturnMoneyQRCodeDialog extends Dialog {
    @BindView(R.id.qrImageView)
    ImageView qrImageView;
    private String code = "";
    private Context context;

    public ReturnMoneyQRCodeDialog(@NonNull Context context, String qrCode) {
        this(context, R.style.HttpRequestDialogStyle);
        this.code = qrCode;
        this.context = context;
    }

    public ReturnMoneyQRCodeDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_retrun_money);
        ButterKnife.bind(this);
        if (!TextUtils.isEmpty(code)) {
            Bitmap codeBitmap = QrCodeUtils.generateBitmap(code, (int) context.getResources().getDimension(R.dimen.dp_226),
                    (int) context.getResources().getDimension(R.dimen.dp_226));
            if (codeBitmap != null) {
                qrImageView.setImageBitmap(codeBitmap);
            }
        }
    }

}