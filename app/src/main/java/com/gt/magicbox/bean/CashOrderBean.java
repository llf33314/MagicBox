package com.gt.magicbox.bean;

/**
 * Description:
 * Created by jack-lin on 2017/9/26 0026.
 * Buddha bless, never BUG!
 */

public class CashOrderBean {

    /**
     * magicBoxOrder : {"id":595,"eqId":37,"busId":33,"status":1,"orderNo":"MB1506407852571","money":108,"time":1506407852571,"modelId":40,"type":2,"businessUtilName":"magicBoxbusinessService","shiftId":0}
     */

    private MagicBoxOrderEntity magicBoxOrder;

    public void setMagicBoxOrder(MagicBoxOrderEntity magicBoxOrder) {
        this.magicBoxOrder = magicBoxOrder;
    }

    public MagicBoxOrderEntity getMagicBoxOrder() {
        return magicBoxOrder;
    }

    public static class MagicBoxOrderEntity {
        /**
         * id : 595
         * eqId : 37
         * busId : 33
         * status : 1
         * orderNo : MB1506407852571
         * money : 108.0
         * time : 1506407852571
         * modelId : 40
         * type : 2
         * businessUtilName : magicBoxbusinessService
         * shiftId : 0
         */

        private int id;
        private int eqId;
        private int busId;
        private int status;
        private String orderNo;
        private double money;
        private long time;
        private int modelId;
        private int type;
        private String businessUtilName;
        private int shiftId;

        public void setId(int id) {
            this.id = id;
        }

        public void setEqId(int eqId) {
            this.eqId = eqId;
        }

        public void setBusId(int busId) {
            this.busId = busId;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public void setModelId(int modelId) {
            this.modelId = modelId;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setBusinessUtilName(String businessUtilName) {
            this.businessUtilName = businessUtilName;
        }

        public void setShiftId(int shiftId) {
            this.shiftId = shiftId;
        }

        public int getId() {
            return id;
        }

        public int getEqId() {
            return eqId;
        }

        public int getBusId() {
            return busId;
        }

        public int getStatus() {
            return status;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public double getMoney() {
            return money;
        }

        public long getTime() {
            return time;
        }

        public int getModelId() {
            return modelId;
        }

        public int getType() {
            return type;
        }

        public String getBusinessUtilName() {
            return businessUtilName;
        }

        public int getShiftId() {
            return shiftId;
        }
    }
}
