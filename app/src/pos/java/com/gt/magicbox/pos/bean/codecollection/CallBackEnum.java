package com.gt.magicbox.pos.bean.codecollection;

/**
 * Created by Administrator on 2017/3/10.
 */

public enum CallBackEnum {

    CODEPAY("codePayCallBackFn","码上收");

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
