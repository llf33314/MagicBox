package com.gt.magicbox.bean;

import com.gt.magicbox.utils.commonutil.TimeUtils;

/**
 * Created by wzb on 2017/9/27 0027.
 */

public class ShiftRecordsAllBean {

    /**
     * shiftRecords : {"id":39,"eqId":33,"staffId":108,"staffName":"008","staffCode":"008","startTime":1506562916000,"money":0,"alipayMoney":0,"wechatMoney":0,"cashMoney":0,"orderInNum":0,"orderOutNum":0,"refundMoney":0,"workNow":0,"shopId":31,"shopName":"谷通科技（惠州研发中心）"}
     */

    private ShiftRecordsBean shiftRecords;

    public ShiftRecordsBean getShiftRecords() {
        return shiftRecords;
    }

    public void setShiftRecords(ShiftRecordsBean shiftRecords) {
        this.shiftRecords = shiftRecords;
    }

    public static class ShiftRecordsBean {
        /**
         * id : 39
         * eqId : 33
         * staffId : 108
         * staffName : 008
         * staffCode : 008
         * startTime : 1506562916000
         * money : 0
         * alipayMoney : 0
         * wechatMoney : 0
         * cashMoney : 0
         * orderInNum : 0
         * orderOutNum : 0
         * refundMoney : 0
         * workNow : 0
         * shopId : 31
         * shopName : 谷通科技（惠州研发中心）
         */

        private int id;
        private int eqId;
        private int staffId;
        private String staffName;
        private String staffCode;
        private long startTime;
        private double money;
        private double alipayMoney;
        private double wechatMoney;
        private double cashMoney;
        private int orderInNum;
        private int orderOutNum;
        private double refundMoney;
        private int workNow;
        private int shopId;
        private String shopName;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getEqId() {
            return eqId;
        }

        public void setEqId(int eqId) {
            this.eqId = eqId;
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

        public String getStaffCode() {
            return staffCode;
        }

        public void setStaffCode(String staffCode) {
            this.staffCode = staffCode;
        }

        public String getStartTime() {
            return TimeUtils.millis2String(startTime);
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public double getAlipayMoney() {
            return alipayMoney;
        }

        public void setAlipayMoney(double alipayMoney) {
            this.alipayMoney = alipayMoney;
        }

        public double getWechatMoney() {
            return wechatMoney;
        }

        public void setWechatMoney(double wechatMoney) {
            this.wechatMoney = wechatMoney;
        }

        public double getCashMoney() {
            return cashMoney;
        }

        public void setCashMoney(double cashMoney) {
            this.cashMoney = cashMoney;
        }

        public int getOrderInNum() {
            return orderInNum;
        }

        public void setOrderInNum(int orderInNum) {
            this.orderInNum = orderInNum;
        }

        public int getOrderOutNum() {
            return orderOutNum;
        }

        public void setOrderOutNum(int orderOutNum) {
            this.orderOutNum = orderOutNum;
        }

        public double getRefundMoney() {
            return refundMoney;
        }

        public void setRefundMoney(double refundMoney) {
            this.refundMoney = refundMoney;
        }

        public int getWorkNow() {
            return workNow;
        }

        public void setWorkNow(int workNow) {
            this.workNow = workNow;
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
    }
}
