package com.gt.magicbox.bean;

import java.util.List;

/**
 * Description:
 * Created by jack-lin on 2017/9/20 0020.
 * Buddha bless, never BUG!
 */

public class CardTypeInfoBean {
    /**
     * gradeTypes : [{"buyMoney":0,"gt_name":"积分卡vip1","gt_id":4127,"applyType":0,"gr_discount":100}]
     * cardType : [{"ctName":"积分卡","ctId":1,"buyMoney":0,"isleft":0,"isCheck":1,"applyType":0,"is_publish":1},{"ctName":"折扣卡","ctId":2,"buyMoney":0.01,"isleft":0,"isCheck":1,"applyType":3,"is_publish":1},{"ctName":"储值卡","ctId":3,"buyMoney":1,"isleft":0,"isCheck":1,"applyType":2,"is_publish":1}]
     */

    private List<GradeTypesEntity> gradeTypes;
    private List<CardTypeEntity> cardType;

    public void setGradeTypes(List<GradeTypesEntity> gradeTypes) {
        this.gradeTypes = gradeTypes;
    }

    public void setCardType(List<CardTypeEntity> cardType) {
        this.cardType = cardType;
    }

    public List<GradeTypesEntity> getGradeTypes() {
        return gradeTypes;
    }

    public List<CardTypeEntity> getCardType() {
        return cardType;
    }

    public static class GradeTypesEntity {
        /**
         * buyMoney : 0.0
         * gt_name : 积分卡vip1
         * gt_id : 4127
         * applyType : 0
         * gr_discount : 100
         */

        private double buyMoney;
        private String gt_name;
        private int gt_id;
        private int applyType;
        private int gr_discount;

        public void setBuyMoney(double buyMoney) {
            this.buyMoney = buyMoney;
        }

        public void setGt_name(String gt_name) {
            this.gt_name = gt_name;
        }

        public void setGt_id(int gt_id) {
            this.gt_id = gt_id;
        }

        public void setApplyType(int applyType) {
            this.applyType = applyType;
        }

        public void setGr_discount(int gr_discount) {
            this.gr_discount = gr_discount;
        }

        public double getBuyMoney() {
            return buyMoney;
        }

        public String getGt_name() {
            return gt_name;
        }

        public int getGt_id() {
            return gt_id;
        }

        public int getApplyType() {
            return applyType;
        }

        public int getGr_discount() {
            return gr_discount;
        }
    }

    public static class CardTypeEntity {
        /**
         * ctName : 积分卡
         * ctId : 1
         * buyMoney : 0.0
         * isleft : 0
         * isCheck : 1
         * applyType : 0
         * is_publish : 1
         */

        private String ctName;
        private int ctId;
        private double buyMoney;
        private int isleft;
        private int isCheck;
        private int applyType;
        private int is_publish;

        public void setCtName(String ctName) {
            this.ctName = ctName;
        }

        public void setCtId(int ctId) {
            this.ctId = ctId;
        }

        public void setBuyMoney(double buyMoney) {
            this.buyMoney = buyMoney;
        }

        public void setIsleft(int isleft) {
            this.isleft = isleft;
        }

        public void setIsCheck(int isCheck) {
            this.isCheck = isCheck;
        }

        public void setApplyType(int applyType) {
            this.applyType = applyType;
        }

        public void setIs_publish(int is_publish) {
            this.is_publish = is_publish;
        }

        public String getCtName() {
            return ctName;
        }

        public int getCtId() {
            return ctId;
        }

        public double getBuyMoney() {
            return buyMoney;
        }

        public int getIsleft() {
            return isleft;
        }

        public int getIsCheck() {
            return isCheck;
        }

        public int getApplyType() {
            return applyType;
        }

        public int getIs_publish() {
            return is_publish;
        }
    }
//    public List<CardType> cardType = new ArrayList<>();
//
//    public static class CardType {
//        public String ctName = "";
//        public int ctId;
//        public int buyMoney;
//        public int isleft;
//        public int isCheck;
//        public int applyType;
//        public int is_publish;
//
//    }


}
