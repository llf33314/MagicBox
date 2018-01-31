package com.gt.magicbox.bean;

import java.io.Serializable;

/**
 * Description:
 *
 * @author jack-lin
 * @date 2018/1/31 0031
 * Buddha bless, never BUG!
 */

public class QueryOrderBean implements Serializable{

    /**
     * bit_duofriend : 1
     * bus_id : 36
     * shop_id : 57
     * status : 2
     * shift_id : 0
     * return_type : 2
     * type : 2
     * order_no : MB1517298321386
     * shop_name : 百度
     * return_shift_id : 0
     * id : 2818
     * business_util_name : magicBoxbusinessService
     * return_cause :
     * time : 1517298321000
     * source : 0
     * model_id : 40
     * return_money : 5.01
     * money : 5.01
     * eq_id : 113
     * erp_type : 0
     */

    private int bit_duofriend;
    private int bus_id;
    private int shop_id;
    private int status;
    private int shift_id;
    private int return_type;
    private int type;
    private String order_no;
    private String shop_name;
    private int return_shift_id;
    private int id;
    private String business_util_name;
    private String return_cause;
    private long time;
    private int source;
    private int model_id;
    private double return_money;
    private double money;
    private int eq_id;
    private int erp_type;

    public void setBit_duofriend(int bit_duofriend) {
        this.bit_duofriend = bit_duofriend;
    }

    public void setBus_id(int bus_id) {
        this.bus_id = bus_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setShift_id(int shift_id) {
        this.shift_id = shift_id;
    }

    public void setReturn_type(int return_type) {
        this.return_type = return_type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public void setReturn_shift_id(int return_shift_id) {
        this.return_shift_id = return_shift_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBusiness_util_name(String business_util_name) {
        this.business_util_name = business_util_name;
    }

    public void setReturn_cause(String return_cause) {
        this.return_cause = return_cause;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public void setModel_id(int model_id) {
        this.model_id = model_id;
    }

    public void setReturn_money(double return_money) {
        this.return_money = return_money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void setEq_id(int eq_id) {
        this.eq_id = eq_id;
    }

    public void setErp_type(int erp_type) {
        this.erp_type = erp_type;
    }

    public int getBit_duofriend() {
        return bit_duofriend;
    }

    public int getBus_id() {
        return bus_id;
    }

    public int getShop_id() {
        return shop_id;
    }

    public int getStatus() {
        return status;
    }

    public int getShift_id() {
        return shift_id;
    }

    public int getReturn_type() {
        return return_type;
    }

    public int getType() {
        return type;
    }

    public String getOrder_no() {
        return order_no;
    }

    public String getShop_name() {
        return shop_name;
    }

    public int getReturn_shift_id() {
        return return_shift_id;
    }

    public int getId() {
        return id;
    }

    public String getBusiness_util_name() {
        return business_util_name;
    }

    public String getReturn_cause() {
        return return_cause;
    }

    public long getTime() {
        return time;
    }

    public int getSource() {
        return source;
    }

    public int getModel_id() {
        return model_id;
    }

    public double getReturn_money() {
        return return_money;
    }

    public double getMoney() {
        return money;
    }

    public int getEq_id() {
        return eq_id;
    }

    public int getErp_type() {
        return erp_type;
    }
}
