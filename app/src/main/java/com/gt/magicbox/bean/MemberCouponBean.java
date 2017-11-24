package com.gt.magicbox.bean;

/**
 * Description:
 * Created by jack-lin on 2017/11/24 0024.
 * Buddha bless, never BUG!
 */

public class MemberCouponBean {

    /**
     * cash_least_cost : 0
     * countId : 1
     * location_id_list : 33,34,43,45,
     * image : //image/3/goodtom/3/20171026/1E407E86CB81F7B9EC610C6D3371FA78.jpg
     * addUser : 0
     * code : 15114896608109052
     * endTime : 1572105600000
     * type : DATE_TYPE_FIX_TIME_RANGE
     * timeType : 1
     * brand_name : 多粉
     * discount : 8
     * startTime : 1509033600000
     * card_status : 2
     * gId : 3410
     * title : 折扣券
     * card_type : 0
     * color : #2c9f67
     * time_limit : [{"type":"MONDAY"},{"type":"TUESDAY"},{"type":"WEDNESDAY"},{"type":"THURSDAY"},{"type":"FRIDAY"},{"type":"SATURDAY"},{"type":"SUNDAY"}]
     * cId : 119
     * reduce_cost : 0
     */

    private int cash_least_cost;
    private int countId;
    private String location_id_list;
    private String image;
    private int addUser;
    private String code;
    private long endTime;
    private String type;
    private int timeType;
    private String brand_name;
    private double discount;
    private long startTime;
    private int card_status;
    private int gId;
    private String title;
    private int card_type;
    private String color;
    private String time_limit;
    private int cId;
    private int reduce_cost;


    private boolean isSelected;
    public void setCash_least_cost(int cash_least_cost) {
        this.cash_least_cost = cash_least_cost;
    }

    public void setCountId(int countId) {
        this.countId = countId;
    }

    public void setLocation_id_list(String location_id_list) {
        this.location_id_list = location_id_list;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAddUser(int addUser) {
        this.addUser = addUser;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTimeType(int timeType) {
        this.timeType = timeType;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setCard_status(int card_status) {
        this.card_status = card_status;
    }

    public void setGId(int gId) {
        this.gId = gId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCard_type(int card_type) {
        this.card_type = card_type;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setTime_limit(String time_limit) {
        this.time_limit = time_limit;
    }

    public void setCId(int cId) {
        this.cId = cId;
    }

    public void setReduce_cost(int reduce_cost) {
        this.reduce_cost = reduce_cost;
    }

    public int getCash_least_cost() {
        return cash_least_cost;
    }

    public int getCountId() {
        return countId;
    }

    public String getLocation_id_list() {
        return location_id_list;
    }

    public String getImage() {
        return image;
    }

    public int getAddUser() {
        return addUser;
    }

    public String getCode() {
        return code;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getType() {
        return type;
    }

    public int getTimeType() {
        return timeType;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public double getDiscount() {
        return discount;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getCard_status() {
        return card_status;
    }

    public int getGId() {
        return gId;
    }

    public String getTitle() {
        return title;
    }

    public int getCard_type() {
        return card_type;
    }

    public String getColor() {
        return color;
    }

    public String getTime_limit() {
        return time_limit;
    }

    public int getCId() {
        return cId;
    }

    public int getReduce_cost() {
        return reduce_cost;
    }
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}