package com.gt.magicbox.base;

/**
 * Description:
 *
 * @author jack-lin
 * @date 2017/9/19 0019
 * Buddha bless, never BUG!
 */

public class BaseConstant {
    public static final String [] PAY_TYPE={"微信","支付宝","现金","会员卡","银行卡"};
    public static final String [] PRODUCTS={"MagicBox","Pos"};
    public static boolean isCanSwipe=true;
    public static boolean isPrintLog=true;
    public static final int PAY_ON_WECHAT=0;
    public static final int PAY_ON_ALIPAY=1;
    public static final int PAY_ON_CASH=2;
    public static final int PAY_ON_MEMBER_CARD=3;
    public static final int PAY_ON_BANK_CARD=4;
    public static long clickTime;
    public static final int DEFAULT_LIMIT_MONEY =50000;
    /* 支持的订单类型*/
    public static final String [] ORDER_HEADER={"MB","YD","JYP","NO"};

}
