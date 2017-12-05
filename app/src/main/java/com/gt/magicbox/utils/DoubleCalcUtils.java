package com.gt.magicbox.utils;

import java.math.BigDecimal;

/**
 * Description:
 * Created by jack-lin on 2017/12/5 0005.
 * Buddha bless, never BUG!
 */

public class DoubleCalcUtils {
    /**
     * double型的减法运算
     *
     * @param scaleLength 保留位数
     * @param d1
     * @param d2
     * @return
     */
    public double subtract(int scaleLength, double d1, double d2) {

        BigDecimal bd1 = new BigDecimal(Double.toString(d1));

        BigDecimal bd2 = new BigDecimal(Double.toString(d2));

        return bd1.subtract(bd2).setScale(scaleLength, BigDecimal.ROUND_HALF_UP).doubleValue();

    }

    /**
     * double型的乘法运算
     *
     * @param scaleLength 保留位数
     * @param d1
     * @param d2
     * @return
     */
    public double multiply(int scaleLength, double d1, double d2) {

        BigDecimal bd1 = new BigDecimal(Double.toString(d1));

        BigDecimal bd2 = new BigDecimal(Double.toString(d2));

        return bd1.multiply(bd2).setScale(scaleLength, BigDecimal.ROUND_HALF_UP).doubleValue();

    }

    /**
     * double型的加法运算
     *
     * @param scaleLength 保留位数
     * @param d1
     * @param d2
     * @return
     */
    public double add(int scaleLength, double d1, double d2) {

        BigDecimal bd1 = new BigDecimal(Double.toString(d1));

        BigDecimal bd2 = new BigDecimal(Double.toString(d2));

        return bd1.add(bd2).setScale(scaleLength, BigDecimal.ROUND_HALF_UP).doubleValue();

    }

    /**
     * double型的除法运算
     *
     * @param scaleLength 保留位数
     * @param d1
     * @param d2
     * @return
     */
    public double divide(int scaleLength, double d1, double d2) {

        BigDecimal bd1 = new BigDecimal(Double.toString(d1));

        BigDecimal bd2 = new BigDecimal(Double.toString(d2));

        return bd1.divide(bd2).setScale(scaleLength, BigDecimal.ROUND_HALF_UP).doubleValue();

    }
}
