package com.gt.magicbox.pos.bean.bankcardcollection.reprint;


import com.gt.magicbox.pos.bean.AbstractBean;
import com.gt.magicbox.pos.util.ObjectUtils;

/**
 * 重打印
 * Created by Administrator on 2017/3/9.
 */
public class ReprintBean extends AbstractBean {

    // （外部订单号与交易凭证号至少一个非空，若两个参数都不为空，则以这两个参数为查询条件进行联合查询）
    private String traceNo; // 交易凭证号（若为000000，则重打印最后一笔交易信息）
    private String extOrderNo; // 外部订单号（非必需）
    private String isNeedPrintReceipt; // 当该值为true表示POS不打单，当该值为false或者不存在时表示由POS打单

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getExtOrderNo() {
        return extOrderNo;
    }

    public void setExtOrderNo(String extOrderNo) {
        this.extOrderNo = extOrderNo;
    }

    public String getIsNeedPrintReceipt() {
        return isNeedPrintReceipt;
    }

    public void setIsNeedPrintReceipt(String isNeedPrintReceipt) {
        this.isNeedPrintReceipt = isNeedPrintReceipt;
    }

    @Override
    public String toString() {
        return "ReprintBean{" +
                "traceNo='" + traceNo + '\'' +
                ", extOrderNo='" + extOrderNo + '\'' +
                ", isNeedPrintReceipt='" + isNeedPrintReceipt + '\'' +
                '}';
    }

    /**
     * 转出json字符串
     * {'traceNo':'123456','extOrderNo':'xxxx','isNeedPrintReceipt':'ooo'}
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
        sb.append("'traceNo':");
        sb.append("'");
        sb.append(traceNo);
        sb.append("'");
        if (ObjectUtils.isNotEmpty(extOrderNo)){
            sb.append(",");
            sb.append("'extOrderNo':");
            sb.append("'");
            sb.append(extOrderNo);
            sb.append("'");
        }
        if (ObjectUtils.isNotEmpty(isNeedPrintReceipt)){
            sb.append(",");
            sb.append("'isNeedPrintReceipt':");
            sb.append("'");
            sb.append(isNeedPrintReceipt);
            sb.append("'");
        }
        sb.append("}");
        return sb.toString();
    }
}
