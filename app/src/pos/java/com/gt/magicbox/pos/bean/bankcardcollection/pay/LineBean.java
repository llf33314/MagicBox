package com.gt.magicbox.pos.bean.bankcardcollection.pay;

/**
 * Created by Administrator on 2017/3/9.
 */

public class LineBean {

    private String value; // 内容
    private String type; // 为text直接使用，barcode为需POS将text转换为条形码，qrcode为需POS将text转换为二维码

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
