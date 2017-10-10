package com.gt.magicbox.pos.bean.bankcardcollection;

/**
 * Created by Administrator on 2017/3/9.
 */

public enum TransIdEnum {

    PAY("消费"),REVOKE("撤销"),QUERY("余额查询"),CANCEL("退货"),SIGN("签到"),BALANCE("结算"),REPRINT("交易明细"),
    TOTALPRINT("打印交易汇总"),PWDPAY("凭密消费");

    private String value;

    TransIdEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
