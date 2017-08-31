package com.gt.magicbox.http;

/**
 * Created by wzb on 2017/7/11 0011.
 */

public class HttpConfig {
    public static final int SUCCESS_CODE=200;
    public static final int NOT_TOKEN=-1;
    //public static final String BASE_URL="http://deeptel.com.cn/magicBoxMobile/";

    //正式
    public static final String BASE_URL="http://duofriend.com/magicBoxMobile/";
    public static final String APP_ID="MagicBox";

    //堡垒地址
    //public static final String BASE_URL="http://nb.deeptel.com.cn/magicBoxMobile/";
   // public static final String BASE_URL="http:/192.168.3.44:8080/magicBoxMobile/";
   // public static final String BASE_URL=" http://hz1.yifriend.net/magicBoxMobile/";

    public static final String SOCKET_SERVER_URL = "http://14.29.79.226:8881";
    //堡垒地址
   // public static final String SOCKET_SERVER_URL = "http://183.47.242.2:8881";
    // socket auth 链接socket时的key值
    public static final String SOCKET_ANDROID_AUTH = "androidAuth";
    // socket auth key 发送socket请求或接收socket请求的key值
    public static final String SOCKET_ANDROID_AUTH_KEY = "dfmagicboxsocket_home";
    public static final String PAYMENT_URL="79B4DE7C/payQR.do";
    public static final String ORDER_URL="79B4DE7C/order.do";
    public static final String CASH_ORDER_URL="79B4DE7C/customOrder.do";
    public static final String LOGIN_URL="79B4DE7C/checkLogin.do";
    public static final String GET_UNPAID_ORDER_URL="79B4DE7C/getNotPayOrderNum.do";


}
