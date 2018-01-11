package com.gt.magicbox.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.gt.magicbox.utils.commonutil.Utils;

/**
 * Description:
 * Created by jack-lin on 2018/1/11 0011.
 * Buddha bless, never BUG!
 */

public class KeyboardUtils {
    /**
     * 显示键盘
     * @param view
     */
    public static void showInputMethod(View view) {
        InputMethodManager im = (InputMethodManager) Utils.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(view, 0);
    }
    //隐藏虚拟键盘
    public static void HideKeyboard(View v){
        InputMethodManager imm = ( InputMethodManager) v.getContext( ).getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow( v.getApplicationWindowToken() , 0 );
        }
    }
}
