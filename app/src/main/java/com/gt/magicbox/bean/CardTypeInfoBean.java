package com.gt.magicbox.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Created by jack-lin on 2017/9/20 0020.
 * Buddha bless, never BUG!
 */

public class CardTypeInfoBean {
    public List<GradeType> gradeTypes = new ArrayList<>();
    public List<CardType> cardType = new ArrayList<>();

    public static class GradeType {
        public int buyMoney;
        public String gt_name = "";
        public int applyType;
        public int gr_discount;
    }

    public static class CardType {
        public String ctName = "";
        public int ctId;
        public int buyMoney;
        public int isleft;
        public int isCheck;
        public int applyType;
        public int is_publish;

    }
}
