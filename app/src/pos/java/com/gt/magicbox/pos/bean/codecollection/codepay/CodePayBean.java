package com.gt.magicbox.pos.bean.codecollection.codepay;


import com.gt.magicbox.pos.bean.AbstractBean;
import com.gt.magicbox.pos.util.ObjectUtils;

/**
 * Created by Administrator on 2017/5/18.
 */

public class CodePayBean extends AbstractBean {

    private String amt; // 交易金额
    private String extOrderNo; // 外部订单号（非必填）

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getExtOrderNo() {
        return extOrderNo;
    }

    public void setExtOrderNo(String extOrderNo) {
        this.extOrderNo = extOrderNo;
    }


    @Override
    public String toString() {
        return "CodePayBean{" +
                "amt='" + amt + '\'' +
                ", extOrderNo='" + extOrderNo + '\'' +
                '}';
    }

    /**
     * 转出json字符串
     * {'amt':'0.01','extOrderNo':'xxxx'}
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
        sb.append("'amt':");
        sb.append("'");
        sb.append(amt);
        sb.append("'");
        if (ObjectUtils.isNotEmpty(extOrderNo)){
            sb.append(",");
            sb.append("'extOrderNo':");
            sb.append("'");
            sb.append(extOrderNo);
            sb.append("'");
        }
        sb.append("}");
        return sb.toString();
    }
}
