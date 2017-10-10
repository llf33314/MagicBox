package com.gt.magicbox.pos.bean;

import com.gt.magicbox.PosConstant;

/**
 * Created by Administrator on 2017/5/19.
 */

public abstract class AbstractBean {

    protected String appId = PosConstant.APP_ID;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
