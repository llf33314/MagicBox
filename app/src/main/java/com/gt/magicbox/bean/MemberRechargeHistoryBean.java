package com.gt.magicbox.bean;

import java.util.List;

/**
 * Description:
 *
 * @author jack-lin
 * @date 2018/1/22 0022
 * Buddha bless, never BUG!
 */

public class MemberRechargeHistoryBean {

    /**
     * code : 0
     * msg : null
     * data : {"rechargeCount":10,"rechargeS":[{"discountAfterMoney":168,"ctId":3,"dataSource":"魔盒","busId":36,"orderCode":"ME1515417055288","changeFlow":0,"flowState":1,"mcId":15521,"id":6030,"balance":452.08,"discountMoney":0,"fukaCtId":0,"shopId":47,"recordType":1,"createDate":"2018-01-08 21:10:55.0","isendDate":1515417055000,"gtId":481,"cardType":-1,"isend":1,"payStatus":"已支付","uccount":0,"ischongzhi":1,"ucType":7,"memberId":1225565,"totalMoney":168},{"discountAfterMoney":59,"ctId":3,"dataSource":"魔盒","busId":36,"orderCode":"ME1514858281018","changeFlow":0,"flowState":1,"mcId":15521,"id":5970,"balance":274.08,"discountMoney":0,"fukaCtId":0,"shopId":43,"recordType":1,"createDate":"2018-01-02 09:58:01.0","isendDate":1514858281000,"gtId":481,"cardType":-1,"isend":1,"payStatus":"已支付","uccount":0,"ischongzhi":1,"ucType":7,"memberId":1225565,"totalMoney":59},{"discountAfterMoney":50,"ctId":3,"dataSource":"魔盒","busId":36,"orderCode":"ME1513753452035","changeFlow":0,"flowState":1,"mcId":15521,"id":5834,"balance":416.04,"discountMoney":0,"fukaCtId":0,"shopId":43,"recordType":1,"createDate":"2017-12-20 15:04:12.0","isendDate":1513753452000,"gtId":481,"cardType":-1,"isend":1,"payStatus":"已支付","uccount":0,"ischongzhi":1,"ucType":7,"memberId":1225565,"totalMoney":50},{"discountAfterMoney":50.96,"ctId":3,"dataSource":"魔盒","busId":36,"orderCode":"ME1513668262474","changeFlow":0,"flowState":1,"mcId":15521,"id":5818,"balance":366.04,"discountMoney":0,"fukaCtId":0,"shopId":43,"recordType":1,"createDate":"2017-12-19 15:24:22.0","isendDate":1513668262000,"gtId":481,"cardType":-1,"isend":1,"payStatus":"已支付","uccount":0,"ischongzhi":1,"ucType":7,"memberId":1225565,"totalMoney":50.96},{"discountAfterMoney":50,"ctId":3,"dataSource":"魔盒","busId":36,"orderCode":"ME1513667439113","changeFlow":0,"flowState":1,"mcId":15521,"id":5817,"balance":315.08,"discountMoney":0,"fukaCtId":0,"shopId":43,"recordType":1,"createDate":"2017-12-19 15:10:39.0","isendDate":1513667439000,"gtId":481,"cardType":-1,"isend":1,"payStatus":"已支付","uccount":0,"ischongzhi":1,"ucType":7,"memberId":1225565,"totalMoney":50},{"discountAfterMoney":50,"ctId":3,"dataSource":"魔盒","busId":36,"orderCode":"ME1513665301763","changeFlow":0,"flowState":1,"mcId":15521,"id":5812,"balance":466.08,"discountMoney":0,"fukaCtId":0,"shopId":43,"recordType":1,"createDate":"2017-12-19 14:35:01.0","isendDate":1513665301000,"gtId":481,"cardType":-1,"isend":1,"payStatus":"已支付","uccount":0,"ischongzhi":1,"ucType":7,"memberId":1225565,"totalMoney":50},{"discountAfterMoney":50,"ctId":3,"dataSource":"魔盒","busId":36,"orderCode":"ME1513665168038","changeFlow":0,"flowState":1,"mcId":15521,"id":5811,"balance":416.08,"discountMoney":0,"fukaCtId":0,"shopId":43,"recordType":1,"createDate":"2017-12-19 14:32:48.0","isendDate":1513665168000,"gtId":481,"cardType":-1,"isend":1,"payStatus":"已支付","uccount":0,"ischongzhi":1,"ucType":7,"memberId":1225565,"totalMoney":50},{"discountAfterMoney":10,"ctId":3,"dataSource":"魔盒","busId":36,"orderCode":"ME1513664671430","changeFlow":0,"flowState":1,"mcId":15521,"id":5809,"balance":366.08,"discountMoney":0,"fukaCtId":0,"shopId":43,"recordType":1,"createDate":"2017-12-19 14:24:31.0","isendDate":1513664671000,"gtId":481,"cardType":-1,"isend":1,"payStatus":"已支付","uccount":0,"ischongzhi":1,"ucType":7,"memberId":1225565,"totalMoney":10},{"discountAfterMoney":1,"ctId":3,"dataSource":"魔盒","busId":36,"orderCode":"ME1513664553917","changeFlow":0,"flowState":1,"mcId":15521,"id":5808,"balance":356.08,"discountMoney":0,"fukaCtId":0,"shopId":43,"recordType":1,"createDate":"2017-12-19 14:22:33.0","isendDate":1513664553000,"gtId":481,"cardType":-1,"isend":1,"payStatus":"已支付","uccount":0,"ischongzhi":1,"ucType":7,"memberId":1225565,"totalMoney":1},{"discountAfterMoney":100,"ctId":3,"dataSource":"魔盒","busId":36,"orderCode":"ME1513664095852","changeFlow":0,"flowState":1,"mcId":15521,"id":5807,"balance":355.08,"discountMoney":0,"fukaCtId":0,"shopId":43,"recordType":1,"createDate":"2017-12-19 14:14:55.0","isendDate":1513664095000,"gtId":481,"cardType":-1,"isend":1,"payStatus":"已支付","uccount":0,"ischongzhi":1,"ucType":7,"memberId":1225565,"totalMoney":100}],"rechargeTotalMoney":100}
     */

    private int code;
    private Object msg;
    private DataEntity data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public Object getMsg() {
        return msg;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * rechargeCount : 10
         * rechargeS : [{"discountAfterMoney":168,"ctId":3,"dataSource":"魔盒","busId":36,"orderCode":"ME1515417055288","changeFlow":0,"flowState":1,"mcId":15521,"id":6030,"balance":452.08,"discountMoney":0,"fukaCtId":0,"shopId":47,"recordType":1,"createDate":"2018-01-08 21:10:55.0","isendDate":1515417055000,"gtId":481,"cardType":-1,"isend":1,"payStatus":"已支付","uccount":0,"ischongzhi":1,"ucType":7,"memberId":1225565,"totalMoney":168},{"discountAfterMoney":59,"ctId":3,"dataSource":"魔盒","busId":36,"orderCode":"ME1514858281018","changeFlow":0,"flowState":1,"mcId":15521,"id":5970,"balance":274.08,"discountMoney":0,"fukaCtId":0,"shopId":43,"recordType":1,"createDate":"2018-01-02 09:58:01.0","isendDate":1514858281000,"gtId":481,"cardType":-1,"isend":1,"payStatus":"已支付","uccount":0,"ischongzhi":1,"ucType":7,"memberId":1225565,"totalMoney":59},{"discountAfterMoney":50,"ctId":3,"dataSource":"魔盒","busId":36,"orderCode":"ME1513753452035","changeFlow":0,"flowState":1,"mcId":15521,"id":5834,"balance":416.04,"discountMoney":0,"fukaCtId":0,"shopId":43,"recordType":1,"createDate":"2017-12-20 15:04:12.0","isendDate":1513753452000,"gtId":481,"cardType":-1,"isend":1,"payStatus":"已支付","uccount":0,"ischongzhi":1,"ucType":7,"memberId":1225565,"totalMoney":50},{"discountAfterMoney":50.96,"ctId":3,"dataSource":"魔盒","busId":36,"orderCode":"ME1513668262474","changeFlow":0,"flowState":1,"mcId":15521,"id":5818,"balance":366.04,"discountMoney":0,"fukaCtId":0,"shopId":43,"recordType":1,"createDate":"2017-12-19 15:24:22.0","isendDate":1513668262000,"gtId":481,"cardType":-1,"isend":1,"payStatus":"已支付","uccount":0,"ischongzhi":1,"ucType":7,"memberId":1225565,"totalMoney":50.96},{"discountAfterMoney":50,"ctId":3,"dataSource":"魔盒","busId":36,"orderCode":"ME1513667439113","changeFlow":0,"flowState":1,"mcId":15521,"id":5817,"balance":315.08,"discountMoney":0,"fukaCtId":0,"shopId":43,"recordType":1,"createDate":"2017-12-19 15:10:39.0","isendDate":1513667439000,"gtId":481,"cardType":-1,"isend":1,"payStatus":"已支付","uccount":0,"ischongzhi":1,"ucType":7,"memberId":1225565,"totalMoney":50},{"discountAfterMoney":50,"ctId":3,"dataSource":"魔盒","busId":36,"orderCode":"ME1513665301763","changeFlow":0,"flowState":1,"mcId":15521,"id":5812,"balance":466.08,"discountMoney":0,"fukaCtId":0,"shopId":43,"recordType":1,"createDate":"2017-12-19 14:35:01.0","isendDate":1513665301000,"gtId":481,"cardType":-1,"isend":1,"payStatus":"已支付","uccount":0,"ischongzhi":1,"ucType":7,"memberId":1225565,"totalMoney":50},{"discountAfterMoney":50,"ctId":3,"dataSource":"魔盒","busId":36,"orderCode":"ME1513665168038","changeFlow":0,"flowState":1,"mcId":15521,"id":5811,"balance":416.08,"discountMoney":0,"fukaCtId":0,"shopId":43,"recordType":1,"createDate":"2017-12-19 14:32:48.0","isendDate":1513665168000,"gtId":481,"cardType":-1,"isend":1,"payStatus":"已支付","uccount":0,"ischongzhi":1,"ucType":7,"memberId":1225565,"totalMoney":50},{"discountAfterMoney":10,"ctId":3,"dataSource":"魔盒","busId":36,"orderCode":"ME1513664671430","changeFlow":0,"flowState":1,"mcId":15521,"id":5809,"balance":366.08,"discountMoney":0,"fukaCtId":0,"shopId":43,"recordType":1,"createDate":"2017-12-19 14:24:31.0","isendDate":1513664671000,"gtId":481,"cardType":-1,"isend":1,"payStatus":"已支付","uccount":0,"ischongzhi":1,"ucType":7,"memberId":1225565,"totalMoney":10},{"discountAfterMoney":1,"ctId":3,"dataSource":"魔盒","busId":36,"orderCode":"ME1513664553917","changeFlow":0,"flowState":1,"mcId":15521,"id":5808,"balance":356.08,"discountMoney":0,"fukaCtId":0,"shopId":43,"recordType":1,"createDate":"2017-12-19 14:22:33.0","isendDate":1513664553000,"gtId":481,"cardType":-1,"isend":1,"payStatus":"已支付","uccount":0,"ischongzhi":1,"ucType":7,"memberId":1225565,"totalMoney":1},{"discountAfterMoney":100,"ctId":3,"dataSource":"魔盒","busId":36,"orderCode":"ME1513664095852","changeFlow":0,"flowState":1,"mcId":15521,"id":5807,"balance":355.08,"discountMoney":0,"fukaCtId":0,"shopId":43,"recordType":1,"createDate":"2017-12-19 14:14:55.0","isendDate":1513664095000,"gtId":481,"cardType":-1,"isend":1,"payStatus":"已支付","uccount":0,"ischongzhi":1,"ucType":7,"memberId":1225565,"totalMoney":100}]
         * rechargeTotalMoney : 100.0
         */

        private int rechargeCount;
        private double rechargeTotalMoney;
        private List<RechargeSEntity> rechargeS;

        public void setRechargeCount(int rechargeCount) {
            this.rechargeCount = rechargeCount;
        }

        public void setRechargeTotalMoney(double rechargeTotalMoney) {
            this.rechargeTotalMoney = rechargeTotalMoney;
        }

        public void setRechargeS(List<RechargeSEntity> rechargeS) {
            this.rechargeS = rechargeS;
        }

        public int getRechargeCount() {
            return rechargeCount;
        }

        public double getRechargeTotalMoney() {
            return rechargeTotalMoney;
        }

        public List<RechargeSEntity> getRechargeS() {
            return rechargeS;
        }

        public static class RechargeSEntity {
            /**
             * discountAfterMoney : 168.0
             * ctId : 3
             * dataSource : 魔盒
             * busId : 36
             * orderCode : ME1515417055288
             * changeFlow : 0
             * flowState : 1
             * mcId : 15521
             * id : 6030
             * balance : 452.08
             * discountMoney : 0.0
             * fukaCtId : 0
             * shopId : 47
             * recordType : 1
             * createDate : 2018-01-08 21:10:55.0
             * isendDate : 1515417055000
             * gtId : 481
             * cardType : -1
             * isend : 1
             * payStatus : 已支付
             * uccount : 0
             * ischongzhi : 1
             * ucType : 7
             * memberId : 1225565
             * totalMoney : 168.0
             */

            private double discountAfterMoney;
            private int ctId;
            private String dataSource;
            private int busId;
            private String orderCode;
            private int changeFlow;
            private int flowState;
            private int mcId;
            private int id;
            private double balance;
            private double discountMoney;
            private int fukaCtId;
            private int shopId;
            private int recordType;
            private String createDate;
            private long isendDate;
            private int gtId;
            private int cardType;
            private int isend;
            private String payStatus;
            private int uccount;
            private int ischongzhi;
            private int ucType;
            private int memberId;
            private double totalMoney;

            public void setDiscountAfterMoney(double discountAfterMoney) {
                this.discountAfterMoney = discountAfterMoney;
            }

            public void setCtId(int ctId) {
                this.ctId = ctId;
            }

            public void setDataSource(String dataSource) {
                this.dataSource = dataSource;
            }

            public void setBusId(int busId) {
                this.busId = busId;
            }

            public void setOrderCode(String orderCode) {
                this.orderCode = orderCode;
            }

            public void setChangeFlow(int changeFlow) {
                this.changeFlow = changeFlow;
            }

            public void setFlowState(int flowState) {
                this.flowState = flowState;
            }

            public void setMcId(int mcId) {
                this.mcId = mcId;
            }

            public void setId(int id) {
                this.id = id;
            }

            public void setBalance(double balance) {
                this.balance = balance;
            }

            public void setDiscountMoney(double discountMoney) {
                this.discountMoney = discountMoney;
            }

            public void setFukaCtId(int fukaCtId) {
                this.fukaCtId = fukaCtId;
            }

            public void setShopId(int shopId) {
                this.shopId = shopId;
            }

            public void setRecordType(int recordType) {
                this.recordType = recordType;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public void setIsendDate(long isendDate) {
                this.isendDate = isendDate;
            }

            public void setGtId(int gtId) {
                this.gtId = gtId;
            }

            public void setCardType(int cardType) {
                this.cardType = cardType;
            }

            public void setIsend(int isend) {
                this.isend = isend;
            }

            public void setPayStatus(String payStatus) {
                this.payStatus = payStatus;
            }

            public void setUccount(int uccount) {
                this.uccount = uccount;
            }

            public void setIschongzhi(int ischongzhi) {
                this.ischongzhi = ischongzhi;
            }

            public void setUcType(int ucType) {
                this.ucType = ucType;
            }

            public void setMemberId(int memberId) {
                this.memberId = memberId;
            }

            public void setTotalMoney(double totalMoney) {
                this.totalMoney = totalMoney;
            }

            public double getDiscountAfterMoney() {
                return discountAfterMoney;
            }

            public int getCtId() {
                return ctId;
            }

            public String getDataSource() {
                return dataSource;
            }

            public int getBusId() {
                return busId;
            }

            public String getOrderCode() {
                return orderCode;
            }

            public int getChangeFlow() {
                return changeFlow;
            }

            public int getFlowState() {
                return flowState;
            }

            public int getMcId() {
                return mcId;
            }

            public int getId() {
                return id;
            }

            public double getBalance() {
                return balance;
            }

            public double getDiscountMoney() {
                return discountMoney;
            }

            public int getFukaCtId() {
                return fukaCtId;
            }

            public int getShopId() {
                return shopId;
            }

            public int getRecordType() {
                return recordType;
            }

            public String getCreateDate() {
                return createDate;
            }

            public long getIsendDate() {
                return isendDate;
            }

            public int getGtId() {
                return gtId;
            }

            public int getCardType() {
                return cardType;
            }

            public int getIsend() {
                return isend;
            }

            public String getPayStatus() {
                return payStatus;
            }

            public int getUccount() {
                return uccount;
            }

            public int getIschongzhi() {
                return ischongzhi;
            }

            public int getUcType() {
                return ucType;
            }

            public int getMemberId() {
                return memberId;
            }

            public double getTotalMoney() {
                return totalMoney;
            }
        }
    }
}
