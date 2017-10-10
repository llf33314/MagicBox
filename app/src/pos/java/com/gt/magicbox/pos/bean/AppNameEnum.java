package com.gt.magicbox.pos.bean;

/**
 * Created by Administrator on 2017/3/9.
 */

public enum AppNameEnum {

    BANKCARDCOLLECTION("银行卡收款"),SWEEPCOLLECTION("POS 通"),CODEPAYCOLLECTION("POS通码上收");

    private String value;

    private AppNameEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
