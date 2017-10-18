package com.gt.magicbox.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Created by jack-lin on 2017/9/22 0022.
 * Buddha bless, never BUG!
 */

public class MemberCardBean implements Serializable{
    public String cardNo = "";
    public String cardDate = "";
    public String ctName = "";
    public boolean result;
    public String gradeName = "";
    public int ctId;
    public String phone = "";
    public double fans_currency;
    public String nickName = "";
    public List<Recharge> recharges = new ArrayList<>();
    public double money;
    public int memberId;
    public int frequency;
    public int integral;
    public int discount;


    public static class Recharge implements Serializable{
        public int id;
        public int ctId;
        public int busId;
        public double money;
        public int giveCount;
        public int publicId;
        public int number;
        public int gr_id;
        public int isDate;

    }

}
