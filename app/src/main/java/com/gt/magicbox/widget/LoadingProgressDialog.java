package com.gt.magicbox.widget;

import android.content.Context;

import com.bigkoo.svprogresshud.SVProgressHUD;

/**
 * Description:
 * Created by jack-lin on 2017/9/29 0029.
 * Buddha bless, never BUG!
 */

public class LoadingProgressDialog  extends SVProgressHUD{
    public LoadingProgressDialog(Context context) {
        super(context);
        this.showWithStatus("加载中...");
    }
}
