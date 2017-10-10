package com.gt.magicbox.pos.bean.sweepcollection;

/**
 * Created by Administrator on 2017/3/10.
 */

public enum CallBackEnum {

    SCANPAY("scanPayCallBackFn","扫一扫");

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
