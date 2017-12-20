package com.gt.magicbox.utils.voice;


import java.util.Scanner;

public class PlaySound {


    /**
     * 截取字符串
     *
     * @param str  需要截取的字符串
     * @param idx1 开始位置
     * @param idx2 截止位置
     * @return 截取后的字符串
     */
    public static String subString(String str, int idx1, int idx2) {
        try {
            return str.substring(idx1, idx2);
        } catch (Exception ex) {
            return "";
        }
    }

    /**
     * 传递一个字符串参数，如果是null返回“”字符串，否则去除前后的空格。
     *
     * @param str 传入参数
     * @return 没有前后没有空格的字符串
     */
    public static final String trim(String str) {
        if (str == null) return "";
        else return str.trim();
    }

    /**
     * 把double类型数据转换成有格式的字符串
     *
     * @param d      需要转换的double类型数据
     * @param format 格式化方式
     * @return 有格式的字符串
     */
    public static String formatDoubleToString(double d, String format) {
        String doubleStr = String.valueOf(d);
        java.text.DecimalFormat decf = new java.text.DecimalFormat(format);
        String formatStr = decf.format(d);
        /**
         * 通过java保留小数了
         * 如果转换前的长度>转换后的长度，Java的转换就有可能出错
         */
        if (doubleStr.length() > formatStr.length()) {
            /**
             * 如果前面的都一致，但最后一位大于4就需要进位
             * 否则不进位
             */
            if (formatStr.equals(doubleStr.substring(0, formatStr.length()))) {
                /**
                 * 取转换前的后一位，
                 * 有可能是“.”
                 */
                String followStr = doubleStr.substring(formatStr.length(), formatStr.length() + 1);
                if (".".equals(followStr)) {
                    followStr = doubleStr.substring(formatStr.length() + 1, formatStr.length() + 2);
                }

                if (Integer.valueOf(followStr).intValue() > 4) {
                    /**
                     * 这个时候Java没有进位
                     */
                    formatStr = decf.format(Double.valueOf(formatStr).doubleValue() + Double.valueOf(format.substring(0, format.length() - 1) + "1").
                            doubleValue());
                }
            }
        }

        return formatStr;
    }

    /**
     * 把一个都money转换成大写的money
     *
     * @param d 需要转换的money
     * @return 换成大写的money
     */
    public static String capitalValueOf(double d) {
        String lowStr;
        int strLen;
        String currentStr;
        String upperPart;
        String upperStr = "";
        int index = 0;
        int findCount;
        String chns = "零壹贰叁肆伍陆柒捌玖";
        String units = "分角  拾佰仟万拾佰仟亿拾佰仟万";

        if (d >= 100000000 || d < 0) {
            return "";
        }
        if (d == 0) {
            return "零元整";
        }
        lowStr = trim(formatDoubleToString(d, "0.00"));
        strLen = lowStr.length();
        if (strLen == 0) {
            return "";
        }
        while (index < strLen) {
            currentStr = subString(lowStr, strLen - index - 1, strLen - index);
            if (".".equals(currentStr)) {
                upperPart = "元";
            } else {
                upperPart = subString(chns, Integer.valueOf(currentStr).intValue(), Integer.valueOf(currentStr).intValue() + 1);
            }
            upperPart += trim(subString(units, index, index + 1));
            upperStr = upperPart + upperStr;
            index += 1;
        }
        for (; ; ) {
            findCount = 0;
            if (upperStr.indexOf("拾零万零仟") < 0) {
                if (upperStr.indexOf("拾零万") >= 0) {
                    if ("仟".equals(subString(upperStr, upperStr.indexOf("拾零万") + 4, upperStr.indexOf("拾零万") + 5))) {
                        findCount++;
                        upperStr = upperStr.replaceFirst("拾零万", "拾万零");
                    }
                }
            }
            if (upperStr.indexOf("零元") >= 0) {
                findCount++;
                upperStr = upperStr.replaceAll("零元", "元");
            }
            if (upperStr.indexOf("零拾") >= 0) {
                findCount++;
                upperStr = upperStr.replaceAll("零拾", "零");
            }
            if (upperStr.indexOf("零佰") >= 0) {
                findCount++;
                upperStr = upperStr.replaceAll("零佰", "零");
            }
            if (upperStr.indexOf("零仟") >= 0) {
                findCount++;
                upperStr = upperStr.replaceAll("零仟", "零");
            }
            if (upperStr.indexOf("零万") >= 0) {
                findCount++;
                upperStr = upperStr.replaceAll("零万", "万");
            }
            if (upperStr.indexOf("零亿") >= 0) {
                findCount++;
                upperStr = upperStr.replaceAll("零亿", "亿");
            }
            if (upperStr.indexOf("零零") >= 0) {
                findCount++;
                upperStr = upperStr.replaceAll("零零", "零");
            }
            if (upperStr.indexOf("零角零分") >= 0) {
                findCount++;
                upperStr = upperStr.replaceAll("零角零分", "整");
            }
            if (upperStr.indexOf("零分") >= 0) {
                findCount++;
                upperStr = upperStr.replaceAll("零分", "整");
            }
            if (upperStr.indexOf("零角") >= 0) {
                findCount++;
                upperStr = upperStr.replaceAll("零角", "零");
            }
            if (upperStr.indexOf("零亿零万零元") >= 0) {
                findCount++;
                upperStr = upperStr.replaceAll("零亿零万零元", "亿元");
            }
            if (upperStr.indexOf("亿零万零元") >= 0) {
                findCount++;
                upperStr = upperStr.replaceAll("亿零万零元", "亿元");
            }
            if (upperStr.indexOf("零亿零万") >= 0) {
                findCount++;
                upperStr = upperStr.replaceAll("零亿零万", "亿");
            }
            if (upperStr.indexOf("零万零元") >= 0) {
                findCount++;
                upperStr = upperStr.replaceAll("零万零元", "万元");
            }
            if (upperStr.indexOf("万零元") >= 0) {
                findCount++;
                upperStr = upperStr.replaceAll("万零元", "万元");
            }
            if (findCount == 0) {
                break;
            }
        }
        while ("元".equals(subString(upperStr, 0, 1)) || "零".equals(subString(upperStr, 0, 1)) || "角".equals(subString(upperStr, 0, 1)) || "分".equals(subString(upperStr, 0, 1)) || "整".equals(subString(upperStr, 0, 1))) {
            strLen = upperStr.length();
            upperStr = subString(upperStr, 1, strLen);
        }
        return upperStr;
    }

    public static String getCapitalValueOf(double d) {
        String[] digit = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖", "点"};
        String[] unit = {"", "", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿",
                "拾", "佰", "仟", "万"};
        String numberString = Double.toString(d);
        char[] chArr = numberString.toCharArray();
        StringBuilder sb = new StringBuilder("");
        String s, s2 = null;
        String indexString;
        int lengthDotLeft;
        if (numberString.contains(".")) {
            String leftNumber = numberString.substring(0, (numberString.indexOf(".")));
            String rightNumber = numberString.substring((numberString.indexOf(".") + 1), numberString.length());

            if (rightNumber.equals("00") || rightNumber.equals("0")) {
                chArr = leftNumber.toCharArray();
            }
            lengthDotLeft = leftNumber.length();
        } else {
            lengthDotLeft = numberString.length();
        }
        for (int i = 0; i < chArr.length; i++) {
            indexString = "" + chArr[i];
            if (indexString.equals(".")) {
                s = digit[10];
            } else {
                s = digit[Integer.parseInt(String.valueOf(chArr[i]))];
            }
            if (lengthDotLeft >= i) {
                s2 = unit[lengthDotLeft - i];
            } else {
                s2 = "";
            }
            sb.append(s).append(s2);
        }
        sb.append(unit[0]);
        sb.append("元");
        String str = sb.toString();
        str = change(str);
        return str;
    }

    public static void main(String[] args) {

        printTest(100.5);
        printTest(1000);
        printTest(1000.5);
        printTest(10000.5);
        printTest(40000);
        printTest(4000);
        printTest(400);
        printTest(40);
        printTest(4);

        printTest(0.05);
        printTest(0.10);
        printTest(1.10);
        printTest(10.10);
        printTest(11.10);





    }
    private static void printTest(double num){
        System.out.print(num +"---");
        System.out.println(getCapitalValueOf(num));

    }
    private static String change(String str) {
        String s = str.replaceAll("零[仟佰拾]", "零");
        s = s.replaceAll("零+", "零").replaceAll("零亿", "亿").replaceAll("零万", "万");
        if (s.startsWith("壹拾")) {
            s = s.replace("壹拾", "拾");
        }
        if (!s.startsWith("零点")) {
            s = s.replace("零点", "点");
        }
        s = s.replaceAll("零圆", "圆").replace("亿万", "亿").
                replace("拾零", "拾").replace("零元","元");
        return s;
    }

}
