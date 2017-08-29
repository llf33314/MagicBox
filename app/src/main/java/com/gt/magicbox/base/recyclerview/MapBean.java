package com.gt.magicbox.base.recyclerview;

/**
 * 不使用原生Map
 */

public class MapBean<String,T> {
    private String key;
    private T value;

    public MapBean(String key, T value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
