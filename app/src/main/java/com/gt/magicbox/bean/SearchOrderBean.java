package com.gt.magicbox.bean;

import java.util.List;

/**
 * Description:
 * Created by jack-lin on 2018/1/6 0006.
 * Buddha bless, never BUG!
 */

public class SearchOrderBean {

    /**
     * offset : 0
     * limit : 2147483647
     * total : 1
     * size : 10
     * pages : 1
     * current : 1
     * searchCount : true
     * openSort : true
     * records : [{"id":7611,"eqId":69,"busId":35,"status":1,"orderNo":"MB1514194821833","money":50000,"time":1514194821000,"modelId":40,"type":2,"businessUtilName":"magicBoxbusinessService","shiftId":0,"source":0,"erpType":0,"shopId":776,"shopName":"深圳兰光科技园","bitDuofriend":1,"returnMoney":0}]
     * offsetCurrent : 0
     * asc : true
     */

    private int offset;
    private int limit;
    private int total;
    private int size;
    private int pages;
    private int current;
    private boolean searchCount;
    private boolean openSort;
    private int offsetCurrent;
    private boolean asc;
    private List<OrderListResultBean.OrderItemBean> records;

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }

    public void setOpenSort(boolean openSort) {
        this.openSort = openSort;
    }

    public void setOffsetCurrent(int offsetCurrent) {
        this.offsetCurrent = offsetCurrent;
    }

    public void setAsc(boolean asc) {
        this.asc = asc;
    }

    public void setRecords(List<OrderListResultBean.OrderItemBean> records) {
        this.records = records;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public int getTotal() {
        return total;
    }

    public int getSize() {
        return size;
    }

    public int getPages() {
        return pages;
    }

    public int getCurrent() {
        return current;
    }

    public boolean getSearchCount() {
        return searchCount;
    }

    public boolean getOpenSort() {
        return openSort;
    }

    public int getOffsetCurrent() {
        return offsetCurrent;
    }

    public boolean getAsc() {
        return asc;
    }

    public List<OrderListResultBean.OrderItemBean> getRecords() {
        return records;
    }


}
