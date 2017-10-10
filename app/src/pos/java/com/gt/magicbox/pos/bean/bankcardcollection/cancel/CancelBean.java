package com.gt.magicbox.pos.bean.bankcardcollection.cancel;

import com.gt.magicbox.pos.bean.AbstractBean;
import com.gt.magicbox.pos.util.ObjectUtils;

/**
 * 退货
 * Created by Administrator on 2017/3/9.
 */
public class CancelBean extends AbstractBean {

    private String amt; // 交易金额
    private String refNo; // 参考号
    private String date; // 日期（MMDD）
    private String extOrderNo; // 外部订单号（非必需）

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExtOrderNo() {
        return extOrderNo;
    }

    public void setExtOrderNo(String extOrderNo) {
        this.extOrderNo = extOrderNo;
    }

    @Override
    public String toString() {
        return "CancelBean{" +
                "amt='" + amt + '\'' +
                ", refNo='" + refNo + '\'' +
                ", date='" + date + '\'' +
                ", extOrderNo='" + extOrderNo + '\'' +
                '}';
    }

    /**
     * 转出json字符串
     * {'orgTraceNo':'123456','extOrderNo':'xxxx'}
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
        if (ObjectUtils.isNotEmpty(refNo)){
            sb.append(",");
            sb.append("'refNo':");
            sb.append("'");
            sb.append(refNo);
            sb.append("'");
        }
        if (ObjectUtils.isNotEmpty(date)){
            sb.append(",");
            sb.append("'date':");
            sb.append("'");
            sb.append(date);
            sb.append("'");
        }
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
