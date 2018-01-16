package com.gt.magicbox.http;

/**
 * Created by wzb on 2017/7/11 0011.
 */

public class HttpConfig {
    public static final int SUCCESS_CODE=0;
    public static final int NOT_TOKEN=-1;
    //public static final String BASE_URL="http://deeptel.com.cn/magicBoxMobile/";

    //正式
  //  public static final String BASE_URL="http://duofriend.com/magicBoxMobile/";
     public static final String BASE_URL="https://yj.deeptel.com.cn/";
    //public static final String BASE_URL="http://nb.yj.deeptel.com.cn/";

    public static final String APP_ID="MagicBox";

    //堡垒地址
    //public static final String BASE_URL="http://nb.deeptel.com.cn/magicBoxMobile/";
   // public static final String BASE_URL="http:/192.168.3.44:8080/magicBoxMobile/";
   // public static final String BASE_URL=" http://hz1.yifriend.net/magicBoxMobile/";

   // public static final String SOCKET_SERVER_URL = "http://113.106.202.51:8881";//测试
   //public static final String SOCKET_SERVER_URL = "https://socket.deeptel.com.cn";//

     public static final String SOCKET_SERVER_URL = "http://183.47.242.2:8881";//正式socket

    //堡垒地址
   // public static final String SOCKET_SERVER_URL = "http://183.47.242.2:8881";
    // socket auth 链接socket时的key值
    public static final String SOCKET_ANDROID_AUTH = "androidAuth";
    // socket auth key 发送socket请求或接收socket请求的key值
    public static final String SOCKET_ANDROID_AUTH_KEY = "dfmagicboxsocket_";
    public static final String SOCKET_ORDER_AUTH_KEY = "dfmagicboxsocket_order";
    public static final String SOCKET_FOLLOW_AUTH_KEY = "MagicBox_Key_";

    public static final String PAYMENT_URL="magicBoxMobile/79B4DE7C/payQR.do";
    public static final String ORDER_URL="magicBoxMobile/79B4DE7C/order.do";
    public static final String CASH_ORDER_URL="magicBoxMobile/79B4DE7C/customOrder.do";
    public static final String LOGIN_URL="magicBoxMobile/79B4DE7C/checkLogin";
    public static final String GET_UNPAID_ORDER_URL= "magicBoxMobile/79B4DE7C/getNotPayOrderNum.do";
    public static final String MAGIC_BOX_MEMBER="magicBoxMember/queryWxShopByBusId";
    public static final String DELETE_NOT_PAY_ORDER="magicBoxMobile/79B4DE7C/delNotPayOrder";
    public static final String CHANGE_BIND="magicBoxMobile/79B4DE7C/changeBinding";
    public static final String SCAN_CODE_PAY="magicBoxMobile/79B4DE7C/scanQR";
    public static final String SCAN_CODE_ALI_PAY="magicBoxMobile/79B4DE7C/aliPayScanQR";
    public static final String SEND_SMS="magicBoxMember/sendSmsOld";
    public static final String FIND_MEMBER_CARD_TYPE="magicBoxMember/findMemberCardType";
    public static final String FIND_MEMBER_GRADE_TYPE="magicBoxMember/findMemberGradeType";
    public static final String RECEIVE_MEMBER_CARD="magicBoxMember/receiveMemberCard";
    public static final String GET_WECHAT_SUBSCRIPTION_QR_CODE="magicBoxMember/newqrcodeCreateFinal";
    public static final String FIND_MEMBER_CARD="magicBoxMember/findMemberCard";
    public static final String MEMBER_SETTLEMENT="magicBoxMember/publicMemberCountMoney";
    public static final String MEMBER_RECHARGE="magicBoxMember/successChongZhi";
    public static final String GET_STAFF_INFO="magicBoxMember/getStaffListShopId";
    public static final String CECORDS_NOW_EXCHANGE="magicBoxMobile/79B4DE7C/recordsNow";
    public static final String GET_NOW_SR="magicBoxMobile/79B4DE7C/getNowSR";
    public static final String MEMBER_PAY="magicBoxMember/paySuccess";
    public static final String POS_ORDER="magicBoxMobile/79B4DE7C/cpOrder";
    public static final String FIND_DUOFEN_BY_MAINFEI ="magicBoxMember/findDuofenByMianfei";
    public static final String FIND_CARD_BY_RECEIVE_ID ="magicBoxMember/findCardByReceiveId";
    public static final String VERIFICATION_CARD_RETURN_NAME="magicBoxMember/verificationCardReturnName";
    public static final String FIND_DUOFEN_CARD_BY_MEMBER_ID_AND_MONEY ="magicBoxMember/findDuofenCardByMemberIdAndMoney";
    public static final String GET_ORDER_STATUS="magicBoxMobile/79B4DE7C/getOrderStatus";
    public static final String SELECT_ORDER_BY_LAST_FOUR="magicBoxMobile/79B4DE7C/selectOrderByLastFour";
    public static final String REVAMP_EQ_NAME="magicBoxMobile/79B4DE7C/revampEqName";
    public static final String WXMEMBER_PAY_REFUND="magicBoxMobile/79B4DE7C/wxmemberPayRefund";


}
