package com.gt.magicbox.bean;

/**
 * Created by wzb on 2017/10/18 0018.
 */

public class UpdateBadgeBean {
    private int messageCount;
    private int position;

    public UpdateBadgeBean(int messageCount, int position) {
        this.messageCount = messageCount;
        this.position = position;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
