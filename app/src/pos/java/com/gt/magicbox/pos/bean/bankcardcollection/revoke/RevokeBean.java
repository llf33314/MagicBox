package com.gt.magicbox.pos.bean.bankcardcollection.revoke;


import com.gt.magicbox.pos.bean.AbstractBean;
import com.gt.magicbox.pos.util.ObjectUtils;

/**
 * 消费撤销
 * Created by Administrator on 2017/3/9.
 */
public class RevokeBean extends AbstractBean {

    private String orgTraceNo; // 原交易凭证号
    private String extOrderNo; // 外部订单号（非必需）

    public String getOrgTraceNo() {
        return orgTraceNo;
    }

    public void setOrgTraceNo(String orgTraceNo) {
        this.orgTraceNo = orgTraceNo;
    }

    public String getExtOrderNo() {
        return extOrderNo;
    }

    public void setExtOrderNo(String extOrderNo) {
        this.extOrderNo = extOrderNo;
    }

    @Override
    public String toString() {
        return "RevokeBean{" +
                "orgTraceNo='" + orgTraceNo + '\'' +
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
        sb.append("'orgTraceNo':");
        sb.append("'");
        sb.append(orgTraceNo);
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
