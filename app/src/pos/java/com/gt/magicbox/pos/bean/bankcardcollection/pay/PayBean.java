package com.gt.magicbox.pos.bean.bankcardcollection.pay;


import com.gt.magicbox.pos.bean.AbstractBean;
import com.gt.magicbox.pos.util.ObjectUtils;

import java.util.List;

/**
 * 消费
 * Created by Administrator on 2017/3/9.
 */
public class PayBean extends AbstractBean {

    private String amt; // 交易金额
    private String extOrderNo; // 外部订单号（非必填）
    private String otherInfo; // 外部信息类-其他信息
    private List<LineBean> lines; // 外部信息类-数组信息

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

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public List<LineBean> getLines() {
        return lines;
    }

    public void setLines(List<LineBean> lines) {
        this.lines = lines;
    }

    @Override
    public String toString() {
        return "PayBean{" +
                "amt='" + amt + '\'' +
                ", extOrderNo='" + extOrderNo + '\'' +
                ", otherInfo='" + otherInfo + '\'' +
                ", lines=" + lines +
                '}';
    }

    /**
     * 转出json字符串
     * "{'amt':'1','extOrderNo':'123456','extInfo':{'otherInfo':'hhh','jsonArray':[{'line':{'value':'xxx','type':'xxx'}}]}}"
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
        if (ObjectUtils.isNotEmptyOne(otherInfo, lines)){
            sb.append(",");
            sb.append("'extInfo':{");
            int num = 0;
            if (ObjectUtils.isNotEmpty(otherInfo)){
                num = 1;
                sb.append("'otherInfo':");
                sb.append("'");
                sb.append(otherInfo);
                sb.append("'");
            }
            if (ObjectUtils.isNotEmpty(lines)){
                if (num > 0)
                    sb.append(",");
                sb.append("'jsonArray':[");
                for (int i = 0; i < lines.size(); i++){
                    LineBean line = lines.get(i);
                    if (i > 0)
                        sb.append(",");
                    sb.append("{'line':{");
                    sb.append("'value':");
                    sb.append("'");
                    sb.append(line.getValue());
                    sb.append("',");
                    sb.append("'type':");
                    sb.append("'");
                    sb.append(line.getType());
                    sb.append("'");
                    sb.append("}}");
                }
                sb.append("]");
            }
            sb.append("}");
        }
        sb.append("}");
        return sb.toString();
    }

}
