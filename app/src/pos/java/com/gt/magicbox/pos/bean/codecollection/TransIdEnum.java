package com.gt.magicbox.pos.bean.codecollection;

/**
 * Created by Administrator on 2017/3/9.
 */

public enum TransIdEnum {

    CODEPAY("码上收");

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
