package com.gt.magicbox.bean;

import retrofit2.http.Field;

/**
 * Created by wzb on 2017/9/27 0027.
 */

public class CecordsNowRequestBean {
   private   int eqId;
    private int shopId;
    private String shopName;
    private String staffCode;
    private  int staffId;
    private String staffName;

    public CecordsNowRequestBean(int eqId, int shopId, String shopName, String staffCode, int staffId, String staffName) {
        this.eqId = eqId;
        this.shopId = shopId;
        this.shopName = shopName;
        this.staffCode = staffCode;
        this.staffId = staffId;
        this.staffName = staffName;
    }

    public int getEqId() {
        return eqId;
    }

    public void setEqId(int eqId) {
        this.eqId = eqId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
}
