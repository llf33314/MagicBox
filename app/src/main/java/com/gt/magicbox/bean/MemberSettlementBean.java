package com.gt.magicbox.bean;

import retrofit2.http.Field;

/**
 * Description:
 * Created by jack-lin on 2017/9/25 0025.
 * Buddha bless, never BUG!
 */

public class MemberSettlementBean {
    public int memberId;
    public double totalMoney;
    public int useCoupon;
    public int useFenbi;
    public int userJifen;
    public int userLeague;

    public MemberSettlementBean(int memberId, double totalMoney, int useCoupon, int useFenbi, int userJifen, int userLeague) {
        this.memberId=memberId;
        this.totalMoney=totalMoney;
        this.useCoupon=useCoupon;
        this.useFenbi=useFenbi;
        this.userJifen=userJifen;
        this.userLeague=userLeague;

    }
}
