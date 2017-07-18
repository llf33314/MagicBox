package com.gt.magicbox.main;

/**
 * Description:
 * Created by jack-lin on 2017/7/17 0017.
 */

public class GridItem {
    private String name = "";
    private Integer imgRes;
    private int messageCount;
    private int normalColor;
    private int focusedColor;

    public int getFocusedColor() {
        return focusedColor;
    }

    public void setFocusedColor(int focusedColor) {
        this.focusedColor = focusedColor;
    }

    public int getNormalColor() {
        return normalColor;
    }

    public void setNormalColor(int normalColor) {
        this.normalColor = normalColor;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public String getName() {
        return name;
    }

    public Integer getImgRes() {
        return imgRes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImgRes(Integer imgRes) {
        this.imgRes = imgRes;
    }
}
