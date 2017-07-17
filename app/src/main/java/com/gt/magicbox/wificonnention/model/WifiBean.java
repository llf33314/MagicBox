package com.gt.magicbox.wificonnention.model;

/**
 * Created by wzb on 2017/7/17 0017.
 */

public class WifiBean {
    private String name;
    private boolean connecting;
    private boolean save;
    private int signLevel;
    /**
     * 加密类型  0：不加密
     *           1：WEP
     *           2：PSK
     *           3:EAP
     */
    private int lockType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isConnecting() {
        return connecting;
    }

    public void setConnecting(boolean connecting) {
        this.connecting = connecting;
    }

    public boolean isSave() {
        return save;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    public int getSignLevel() {
        return signLevel;
    }

    public void setSignLevel(int signLevel) {
        this.signLevel = signLevel;
    }

    public int getLockType() {
        return lockType;
    }

    public void setLockType(int lockType) {
        this.lockType = lockType;
    }
}
