package com.gt.magicbox.bean;

/**
 * Created by wzb on 2017/8/24 0024.
 */

public class DistributeCouponBean {
    private String name;

    private boolean isSelected;

    public DistributeCouponBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
