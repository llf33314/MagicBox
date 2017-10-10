package com.gt.magicbox.pos.bean.bankcardcollection.balance;


import com.gt.magicbox.pos.bean.AbstractBean;

/**
 * 结算
 * Created by Administrator on 2017/3/9.
 */
public class BalanceBean extends AbstractBean {

    private String isNeedPrintReceipt; // 当该值为true表示POS不打单，当该值为false或者不存在时表示由POS打单

    public String getIsNeedPrintReceipt() {
        return isNeedPrintReceipt;
    }

    public void setIsNeedPrintReceipt(String isNeedPrintReceipt) {
        this.isNeedPrintReceipt = isNeedPrintReceipt;
    }

    @Override
    public String toString() {
        return "BalanceBean{" +
                "isNeedPrintReceipt='" + isNeedPrintReceipt + '\'' +
                '}';
    }

    /**
     * 转出json字符串
     * {'isNeedPrintReceipt':'123456'}
     * @return
     */
    public String toJsonString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("'appId':");
        sb.append("'");
        sb.append(appId);
        sb.append("'");
        sb.append(",");
        sb.append("'isNeedPrintReceipt':");
        sb.append("'");
        sb.append(isNeedPrintReceipt);
        sb.append("'");
        sb.append("}");
        return sb.toString();
    }
}
