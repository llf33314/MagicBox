package com.gt.magicbox.pos.bean.bankcardcollection;

/**
 * Created by Administrator on 2017/3/10.
 */

public enum CallBackEnum {

    PAY("payCallBackFn","消费"),REVOKE("revokeCallBackFn","撤销"),QUERY("queryCallBackFn","余额查询"),
    CANCEL("cancelCallBackFn","退货"), SIGN("signCallBackFn","签到"),BALANCE("balanceCallBackFn","结算"),
    REPRINT("reprintCallBackFn","交易明细"), TOTALPRINT("totalPrintCallBackFn","打印交易汇总"),PWDPAY("pwdPayCallBackFn","凭密消费");

    private String callBackFn;
    private String describe;

    CallBackEnum(String callBackFn, String describe){
        this.callBackFn = callBackFn;
        this.describe = describe;
    }

    public String getCallBackFn() {
        return callBackFn;
    }

    public void setCallBackFn(String callBackFn) {
        this.callBackFn = callBackFn;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
