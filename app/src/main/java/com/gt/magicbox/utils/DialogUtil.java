package com.gt.magicbox.utils;

import android.app.Dialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.gt.magicbox.utils.commonutil.ScreenUtils;

/**
 * Created by wzb on 2017/9/27 0027.
 */

public class DialogUtil {

    public static void setBottom(Dialog dialog) {
        Window w = dialog.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.width = ScreenUtils.getScreenWidth();
        lp.height = ScreenUtils.getScreenHeight() / 2;
        lp.gravity = Gravity.BOTTOM;
        w.setAttributes(lp);
    }
}
