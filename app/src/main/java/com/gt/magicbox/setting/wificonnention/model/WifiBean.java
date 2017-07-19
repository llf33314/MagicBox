package com.gt.magicbox.setting.wificonnention.model;

/**
 * Created by wzb on 2017/7/17 0017.
 */

public class WifiBean {
    private String name;
    /**
     * 加密类型  0：不加密
     *           1：WEP
     *           2：PSK
     *           3:EAP
     */
    private int lockType;
    private int signLevel;
    private boolean save;

    public int getConnectState() {
        return connectState;
    }

    public void setConnectState(int connectState) {
        this.connectState = connectState;
    }

    /**
     * 0:未连接 1：已连接 2：连接中
     */
    private int connectState;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLockType() {
        return lockType;
    }

    public void setLockType(int lockType) {
        this.lockType = lockType;
    }

    public int getSignLevel() {
        return signLevel;
    }

    public void setSignLevel(int signLevel) {
        this.signLevel = signLevel;
    }

    public boolean isSave() {
        return save;
    }

    public void setSave(boolean save) {
        this.save = save;
    }


}
