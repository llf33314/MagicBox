package com.gt.magicbox.pos.bean.bankcardcollection.pay;

import java.util.List;

/**
 * Created by Administrator on 2017/3/9.
 */

public class ExtInfoBean {

    private String otherInfo; // 其他信息
    private List<LineBean> line; // line

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public List<LineBean> getLine() {
        return line;
    }

    public void setLine(List<LineBean> line) {
        this.line = line;
    }
}
