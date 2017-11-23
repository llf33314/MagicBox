package com.gt.magicbox.bean;

/**
 * Created by wzb on 2017/11/21 0021.
 */

public class DistributeCouponMainBean {

    /**
     * id : 456
     * receiveDate : 1512057599000
     * cardsName : 008
     * code : am3the
     * backColor : rgb(198, 75, 46)
     */

    private int id;
    private long receiveDate;
    private String cardsName;
    private String code;
    private String backColor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(long receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getCardsName() {
        return cardsName;
    }

    public void setCardsName(String cardsName) {
        this.cardsName = cardsName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBackColor() {
        return backColor;
    }

    public void setBackColor(String backColor) {
        this.backColor = backColor;
    }
}
