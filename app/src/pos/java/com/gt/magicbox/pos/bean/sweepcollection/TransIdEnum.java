package com.gt.magicbox.pos.bean.sweepcollection;

/**
 * Created by Administrator on 2017/3/9.
 */

public enum TransIdEnum {

    SCAN("扫一扫");

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
