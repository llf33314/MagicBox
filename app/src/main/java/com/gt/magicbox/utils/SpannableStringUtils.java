package com.gt.magicbox.utils;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;

import com.gt.magicbox.utils.commonutil.ConvertUtils;

/**
 * Description:
 * Created by jack-lin on 2017/7/21 0021.
 */

public class SpannableStringUtils {
    /**
     * @param target
     * @param size 字体大小dp
     * @param start
     * @param end
     * @return
     */
    public static SpannableStringBuilder diffTextSize(String target,int size,int start,int end) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        if (!TextUtils.isEmpty(target)&&target.length()>end) {
            spannableString.append(target);
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(ConvertUtils.dp2px(size));
            spannableString.setSpan(absoluteSizeSpan, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return spannableString;
    }
}
