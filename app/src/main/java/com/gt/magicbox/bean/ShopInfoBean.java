package com.gt.magicbox.bean;

/**
 * Description:
 * Created by jack-lin on 2017/9/26 0026.
 * Buddha bless, never BUG!
 */

public class ShopInfoBean {

    /**
     * busId : 33
     * shops : {"mainShop":1,"createTime":"2017-09-07 11:23:03.0","businessName":"谷通科技（惠州研发中心）","detail":"","busId":33,"imageUrl":"","availableState":4,"endTime":"21:00","recommend":"","city":"2230","startTime":"9:00","id":31,"poiid":"464809640","province":"2136","twoCategories":"其它公司企业","offsetType":1,"longitude":"114.437011719","district":"2232","sid":"df1491567684402","status":0,"updateStatus":0,"adder":"赛格假日广场1006","threeCategories":"","avgPrice":10,"address":"广东省惠州市惠城区赛格假日广场1006","branchName":"","special":"免费停车","categories":"公司企业","latitude":"23.0880393982","telephone":"0752-7193383","introduction":""}
     * shopName : 谷通科技（惠州研发中心）
     */

    private int busId;
    private ShopsEntity shops;
    private String shopName;

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public void setShops(ShopsEntity shops) {
        this.shops = shops;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getBusId() {
        return busId;
    }

    public ShopsEntity getShops() {
        return shops;
    }

    public String getShopName() {
        return shopName;
    }

    public static class ShopsEntity {
        /**
         * mainShop : 1
         * createTime : 2017-09-07 11:23:03.0
         * businessName : 谷通科技（惠州研发中心）
         * detail :
         * busId : 33
         * imageUrl :
         * availableState : 4
         * endTime : 21:00
         * recommend :
         * city : 2230
         * startTime : 9:00
         * id : 31
         * poiid : 464809640
         * province : 2136
         * twoCategories : 其它公司企业
         * offsetType : 1
         * longitude : 114.437011719
         * district : 2232
         * sid : df1491567684402
         * status : 0
         * updateStatus : 0
         * adder : 赛格假日广场1006
         * threeCategories :
         * avgPrice : 10
         * address : 广东省惠州市惠城区赛格假日广场1006
         * branchName :
         * special : 免费停车
         * categories : 公司企业
         * latitude : 23.0880393982
         * telephone : 0752-7193383
         * introduction :
         */

        private int mainShop;
        private String createTime;
        private String businessName;
        private String detail;
        private int busId;
        private String imageUrl;
        private int availableState;
        private String endTime;
        private String recommend;
        private String city;
        private String startTime;
        private int id;
        private String poiid;
        private String province;
        private String twoCategories;
        private int offsetType;
        private String longitude;
        private String district;
        private String sid;
        private int status;
        private int updateStatus;
        private String adder;
        private String threeCategories;
        private int avgPrice;
        private String address;
        private String branchName;
        private String special;
        private String categories;
        private String latitude;
        private String telephone;
        private String introduction;

        public void setMainShop(int mainShop) {
            this.mainShop = mainShop;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public void setBusinessName(String businessName) {
            this.businessName = businessName;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public void setBusId(int busId) {
            this.busId = busId;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public void setAvailableState(int availableState) {
            this.availableState = availableState;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public void setRecommend(String recommend) {
            this.recommend = recommend;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setPoiid(String poiid) {
            this.poiid = poiid;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public void setTwoCategories(String twoCategories) {
            this.twoCategories = twoCategories;
        }

        public void setOffsetType(int offsetType) {
            this.offsetType = offsetType;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setUpdateStatus(int updateStatus) {
            this.updateStatus = updateStatus;
        }

        public void setAdder(String adder) {
            this.adder = adder;
        }

        public void setThreeCategories(String threeCategories) {
            this.threeCategories = threeCategories;
        }

        public void setAvgPrice(int avgPrice) {
            this.avgPrice = avgPrice;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public void setSpecial(String special) {
            this.special = special;
        }

        public void setCategories(String categories) {
            this.categories = categories;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public int getMainShop() {
            return mainShop;
        }

        public String getCreateTime() {
            return createTime;
        }

        public String getBusinessName() {
            return businessName;
        }

        public String getDetail() {
            return detail;
        }

        public int getBusId() {
            return busId;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public int getAvailableState() {
            return availableState;
        }

        public String getEndTime() {
            return endTime;
        }

        public String getRecommend() {
            return recommend;
        }

        public String getCity() {
            return city;
        }

        public String getStartTime() {
            return startTime;
        }

        public int getId() {
            return id;
        }

        public String getPoiid() {
            return poiid;
        }

        public String getProvince() {
            return province;
        }

        public String getTwoCategories() {
            return twoCategories;
        }

        public int getOffsetType() {
            return offsetType;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getDistrict() {
            return district;
        }

        public String getSid() {
            return sid;
        }

        public int getStatus() {
            return status;
        }

        public int getUpdateStatus() {
            return updateStatus;
        }

        public String getAdder() {
            return adder;
        }

        public String getThreeCategories() {
            return threeCategories;
        }

        public int getAvgPrice() {
            return avgPrice;
        }

        public String getAddress() {
            return address;
        }

        public String getBranchName() {
            return branchName;
        }

        public String getSpecial() {
            return special;
        }

        public String getCategories() {
            return categories;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getTelephone() {
            return telephone;
        }

        public String getIntroduction() {
            return introduction;
        }
    }
}
