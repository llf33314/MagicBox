package com.gt.magicbox.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Created by jack-lin on 2017/9/22 0022.
 * Buddha bless, never BUG!
 */

public class CardGradeInfoBean {
    public List<GradeType> gradeType = new ArrayList<>();
    public static class GradeType {
        public double buyMoney;
        public String gt_name = "";
        public int applyType;
        public int gr_discount;
        public int gt_id;
    }

}
