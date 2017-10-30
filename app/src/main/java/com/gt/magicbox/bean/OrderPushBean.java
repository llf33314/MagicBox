package com.gt.magicbox.bean;

/**
 * Description:
 * Created by jack-lin on 2017/10/30 0030.
 * Buddha bless, never BUG!
 */

public class OrderPushBean {

    /**
     * busId : 36
     * businessUtilName : shop.deeptel.com.cn/shops/web/cashier/CF946E2B/payCallBack?id=ff8080815f629703015f6abdceaa006c
     * eqCode : 865067034465453
     * model : 53
     * money : 0.01
     * orderId : 1069
     * orderNo : YD1509324344993
     * pay_type : 0
     * status : success
     * time : 2017-10-30 08:45:47
     * type : 1
     */

    private int busId;
    private String businessUtilName;
    private String eqCode;
    private int model;
    private double money;
    private int orderId;
    private String orderNo;
    private int pay_type;
    private String status;
    private String time;
    private int type;

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public void setBusinessUtilName(String businessUtilName) {
        this.businessUtilName = businessUtilName;
    }

    public void setEqCode(String eqCode) {
        this.eqCode = eqCode;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBusId() {
        return busId;
    }

    public String getBusinessUtilName() {
        return businessUtilName;
    }

    public String getEqCode() {
        return eqCode;
    }

    public int getModel() {
        return model;
    }

    public double getMoney() {
        return money;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public int getPay_type() {
        return pay_type;
    }

    public String getStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }

    public int getType() {
        return type;
    }
}
