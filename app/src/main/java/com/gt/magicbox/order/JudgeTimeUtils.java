package com.gt.magicbox.order;

import com.gt.magicbox.utils.commonutil.TimeUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Description:
 * Created by jack-lin on 2018/1/4 0004.
 * Buddha bless, never BUG!
 */

public class JudgeTimeUtils {
    private static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static final DateFormat DEFAULT_FORMAT1 = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
    private static final long oneDayMills=86400000L;
    public static boolean isSameDate(long time1, long time2) {
        String time1String = TimeUtils.millis2String(time1, DEFAULT_FORMAT);
        String time2String = TimeUtils.millis2String(time2, DEFAULT_FORMAT);
        if (time1String.equals(time2String)) {
            return true;
        } else {
            return false;
        }
    }
    public static long getTimeFromCurrentToLimit(long currentTime,int limitDay){
        String currentDayString = TimeUtils.millis2String(currentTime, DEFAULT_FORMAT1);
        long millionSeconds=0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        try {
             millionSeconds = sdf.parse(currentDayString+"000000").getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millionSeconds-limitDay*oneDayMills;
    }
}
