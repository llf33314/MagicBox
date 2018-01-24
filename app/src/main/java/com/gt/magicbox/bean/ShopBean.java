package com.gt.magicbox.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Created by jack-lin on 2018/1/22 0022.
 * Buddha bless, never BUG!
 */

public class ShopBean  {

    /**
     * code : 0
     * data : [{"adder":"兰光科技园","address":"广东省深圳市南山区兰光科技园102","availableState":0,"avgPrice":1,"branchName":"","busId":36,"businessName":"广东谷通科技","categories":"25","city":"2163","createTime":"2017-11-08 11:05:53.0","detail":"102","district":"2167","endTime":"23:00","id":33,"imageUrl":"/image/3/goodtom/3/20170424/B1FC69B4154D856F3CC3C146656F84BC.png","introduction":"","latitude":"22.53332","longitude":"113.93041","mainShop":0,"offsetType":1,"poiid":"","province":"2136","recommend":"","sid":"df1493029756127","special":"2","startTime":"9:00","status":0,"telephone":"0755236","threeCategories":"0","twoCategories":"28","updateStatus":0},{"adder":"兰光科技园","address":"广东省深圳市南山区兰光科技园c栋513","availableState":2,"avgPrice":75,"branchName":"","busId":36,"businessName":"谷通科技深圳研发","categories":"370","city":"2163","createTime":"2017-11-08 11:05:50.0","detail":"c栋513","district":"2167","endTime":"23:00","id":34,"imageUrl":"/image/3/goodtom/3/20170424/B1FC69B4154D856F3CC3C146656F84BC.png","introduction":"","latitude":"22.55412","longitude":"113.94172","mainShop":0,"offsetType":1,"poiid":"","province":"2136","recommend":"","sid":"df1493030666075","special":"免费wifi","startTime":"9:00","status":0,"telephone":"0755-26609632","threeCategories":"0","twoCategories":"371","updateStatus":0},{"adder":"赛格大厦10楼","address":"广东省惠州市惠城区赛格大厦10楼0752","availableState":2,"avgPrice":1,"branchName":"","busId":36,"businessName":"广东谷通科技有限公司","categories":"370","city":"2230","createTime":"2018-01-05 19:10:22.0","detail":"0752","district":"2232","endTime":"23:00","id":43,"imageUrl":"/image/3/goodtom/3/20170516/48B64A5A1D7B8AE2F0BF43BDCF795644.png","introduction":"1","latitude":"23.08383","longitude":"114.38273","mainShop":0,"offsetType":1,"poiid":"","province":"2136","recommend":"1","sid":"df1497251896880","special":"1","startTime":"9:00","status":0,"telephone":"15915012812","threeCategories":"0","twoCategories":"373","updateStatus":0},{"adder":"惠州市惠城区委(新联路北)","address":"广东省惠州市惠城区惠州市惠城区委(新联路北)1004","availableState":0,"avgPrice":100,"branchName":"","busId":36,"businessName":"多粉公司","categories":"25","city":"2230","createTime":"2017-12-04 11:55:52.0","detail":"1004","district":"2232","endTime":"23:00","id":45,"imageUrl":"/image/3/goodtom/3/20170823/F84E83AC119D56C6F19EC25033673422.jpg","introduction":"地方的","latitude":"23.08383","longitude":"114.38273","mainShop":0,"offsetType":1,"poiid":"","province":"2136","recommend":"fdfdf","sid":"df1503650721353","special":"发的","startTime":"9:00","status":0,"telephone":"13502284564","threeCategories":"0","twoCategories":"28","updateStatus":0},{"adder":"惠州大道20好赛格大厦10楼1007室","address":"广东省惠州市惠城区惠州大道20好赛格大厦10楼1007室1007","availableState":0,"avgPrice":123,"branchName":"","busId":36,"businessName":"多粉","categories":"370","city":"2230","createTime":"2018-01-17 14:52:39.0","detail":"1007","district":"2232","endTime":"23:00","id":47,"imageUrl":"/image/3/goodtom/3/20171101/522DD4AD61540ADFA26CDF5004E2EE1B.jpg","introduction":"弃我而去而且","latitude":"23.08869","longitude":"114.43724","mainShop":0,"offsetType":1,"poiid":"","province":"2136","recommend":"企鹅权威","sid":"df1509962680037","special":"请问","startTime":"9:00","status":0,"telephone":"15111188481","threeCategories":"0","twoCategories":"371","updateStatus":0},{"adder":"味浓味淡主题餐厅(赛格店)","address":"广东省惠州市惠城区味浓味淡主题餐厅(赛格店)","availableState":0,"avgPrice":1000,"branchName":"","busId":36,"businessName":"小盼教育（东平教学点）","categories":"361","city":"2230","createTime":"2018-01-05 19:10:22.0","detail":"","district":"2232","endTime":"23:00","id":49,"imageUrl":"/image/3/goodtom/3/20180104/E79F16C89D7BAB67B8FD5C7F54FE69DB.png","introduction":"小盼教育测试","latitude":"23.088327","longitude":"114.437008","mainShop":0,"offsetType":1,"poiid":"","province":"2136","recommend":"","sid":"df1515054743138","special":"中小初课外辅导","startTime":"9:00","status":0,"telephone":"0752-2331123","threeCategories":"0","twoCategories":"362","updateStatus":0},{"adder":"东辉广告","address":"山西省大同市城区东辉广告","availableState":0,"avgPrice":1000,"branchName":"","busId":36,"businessName":"测试不能删除","categories":"361","city":"247","createTime":"2018-01-05 19:10:22.0","detail":"","district":"249","endTime":"23:00","id":50,"imageUrl":"/image/3/goodtom/3/20180104/C60E598EE98480B9990F9F6A38729A93.jpg","introduction":"","latitude":"40.076762","longitude":"113.300129","mainShop":0,"offsetType":1,"poiid":"","province":"234","recommend":"","sid":"df1515071594977","special":"weds","startTime":"9:00","status":0,"telephone":"0752-2502576","threeCategories":"0","twoCategories":"362","updateStatus":0},{"adder":"２２２２２","address":"辽宁省鞍山市铁西区２２２２２","availableState":0,"avgPrice":1,"branchName":"","busId":36,"businessName":"车车别哪 这个开单","categories":"329","city":"527","createTime":"2018-01-08 14:22:42.0","detail":"","district":"530","endTime":"23:00","id":51,"imageUrl":"/image/3/goodtom/3/20180104/F1BA99C6880A3B06C4CD896E1E82B9E0.png","introduction":"","latitude":"41.119885","longitude":"122.969630","mainShop":0,"offsetType":1,"poiid":"","province":"499","recommend":"","sid":"df1515211032832","special":"32445","startTime":"9:00","status":0,"telephone":"0750-26609631","threeCategories":"0","twoCategories":"331","updateStatus":0},{"adder":"佛山君安公寓细窖大道店","address":"广东省佛山市顺德区佛山君安公寓细窖大道店1002","availableState":0,"avgPrice":98,"branchName":"","busId":36,"businessName":"酒店测试","categories":"374","city":"2185","createTime":"2018-01-08 15:06:11.0","detail":"1002","district":"2189","endTime":"23:00","id":53,"imageUrl":"/image/3/goodtom/3/20171227/BB32B2CFE0EC794622A8C3E1387F3DEE.jpg","introduction":"c","latitude":"22.804113","longitude":"113.294365","mainShop":0,"offsetType":1,"poiid":"","province":"2136","recommend":"b","sid":"df1515392650356","special":"a","startTime":"9:00","status":0,"telephone":"15013990984","threeCategories":"0","twoCategories":"376","updateStatus":0},{"adder":"东平赛格大厦","address":"广东省惠州市惠城区东平赛格大厦","availableState":0,"avgPrice":2000,"branchName":"","busId":36,"businessName":"惠州多粉","categories":"361","city":"2230","createTime":"2018-01-10 16:40:08.0","detail":"","district":"2232","endTime":"23:00","id":55,"imageUrl":"/image/3/goodtom/3/20180110/ED3044CBF18EA7EFDE6AD6DF783E4910.png","introduction":"","latitude":"23.084122","longitude":"114.382541","mainShop":0,"offsetType":1,"poiid":"","province":"2136","recommend":"","sid":"df1515573608968","special":"小学培训","startTime":"9:00","status":0,"telephone":"15011011101","threeCategories":"0","twoCategories":"364","updateStatus":0},{"adder":"赛格大厦1208","address":"广东省惠州市惠城区赛格大厦1208","availableState":0,"avgPrice":100,"branchName":"","busId":36,"businessName":"狗狗屋","categories":"361","city":"2230","createTime":"2018-01-10 19:06:10.0","detail":"","district":"2232","endTime":"23:00","id":56,"imageUrl":"/image/3/goodtom/3/20180110/1EF369751B8E066E210DE87314012059.jpg","introduction":"","latitude":"23.084122","longitude":"114.382541","mainShop":0,"offsetType":1,"poiid":"","province":"2136","recommend":"","sid":"df1515582370029","special":"教育培训","startTime":"9:00","status":0,"telephone":"18088804626","threeCategories":"0","twoCategories":"364","updateStatus":0},{"adder":"家福房产","address":"广西壮族自治区贵港市港北区家福房产","availableState":0,"avgPrice":14,"branchName":"","busId":36,"businessName":"百度","categories":"6","city":"2371","createTime":"2018-01-17 14:52:39.0","detail":"","district":"2373","endTime":"23:00","id":57,"imageUrl":"/image/3/goodtom/3/20180104/E79F16C89D7BAB67B8FD5C7F54FE69DB.png","introduction":"设法回溯法","latitude":"23.111530","longitude":"109.598926","mainShop":1,"offsetType":1,"poiid":"","province":"2298","recommend":"忒假","sid":"df1516171766685","special":"免费WiFi","startTime":"9:00","status":0,"telephone":"0775-4585287","threeCategories":"0","twoCategories":"7","updateStatus":0}]
     * msg : success
     * success : true
     */

    private int code;
    private String msg;
    private boolean success;
    private List<DataEntity> data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public boolean getSuccess() {
        return success;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * adder : 兰光科技园
         * address : 广东省深圳市南山区兰光科技园102
         * availableState : 0
         * avgPrice : 1
         * branchName :
         * busId : 36
         * businessName : 广东谷通科技
         * categories : 25
         * city : 2163
         * createTime : 2017-11-08 11:05:53.0
         * detail : 102
         * district : 2167
         * endTime : 23:00
         * id : 33
         * imageUrl : /image/3/goodtom/3/20170424/B1FC69B4154D856F3CC3C146656F84BC.png
         * introduction :
         * latitude : 22.53332
         * longitude : 113.93041
         * mainShop : 0
         * offsetType : 1
         * poiid :
         * province : 2136
         * recommend :
         * sid : df1493029756127
         * special : 2
         * startTime : 9:00
         * status : 0
         * telephone : 0755236
         * threeCategories : 0
         * twoCategories : 28
         * updateStatus : 0
         */

        private String adder;
        private String address;
        private int availableState;
        private int avgPrice;
        private String branchName;
        private int busId;
        private String businessName;
        private String categories;
        private String city;
        private String createTime;
        private String detail;
        private String district;
        private String endTime;
        private int id;
        private String imageUrl;
        private String introduction;
        private String latitude;
        private String longitude;
        private int mainShop;
        private int offsetType;
        private String poiid;
        private String province;
        private String recommend;
        private String sid;
        private String special;
        private String startTime;
        private int status;
        private String telephone;
        private String threeCategories;
        private String twoCategories;
        private int updateStatus;

        public void setAdder(String adder) {
            this.adder = adder;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setAvailableState(int availableState) {
            this.availableState = availableState;
        }

        public void setAvgPrice(int avgPrice) {
            this.avgPrice = avgPrice;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public void setBusId(int busId) {
            this.busId = busId;
        }

        public void setBusinessName(String businessName) {
            this.businessName = businessName;
        }

        public void setCategories(String categories) {
            this.categories = categories;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public void setMainShop(int mainShop) {
            this.mainShop = mainShop;
        }

        public void setOffsetType(int offsetType) {
            this.offsetType = offsetType;
        }

        public void setPoiid(String poiid) {
            this.poiid = poiid;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public void setRecommend(String recommend) {
            this.recommend = recommend;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public void setSpecial(String special) {
            this.special = special;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public void setThreeCategories(String threeCategories) {
            this.threeCategories = threeCategories;
        }

        public void setTwoCategories(String twoCategories) {
            this.twoCategories = twoCategories;
        }

        public void setUpdateStatus(int updateStatus) {
            this.updateStatus = updateStatus;
        }

        public String getAdder() {
            return adder;
        }

        public String getAddress() {
            return address;
        }

        public int getAvailableState() {
            return availableState;
        }

        public int getAvgPrice() {
            return avgPrice;
        }

        public String getBranchName() {
            return branchName;
        }

        public int getBusId() {
            return busId;
        }

        public String getBusinessName() {
            return businessName;
        }

        public String getCategories() {
            return categories;
        }

        public String getCity() {
            return city;
        }

        public String getCreateTime() {
            return createTime;
        }

        public String getDetail() {
            return detail;
        }

        public String getDistrict() {
            return district;
        }

        public String getEndTime() {
            return endTime;
        }

        public int getId() {
            return id;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getIntroduction() {
            return introduction;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public int getMainShop() {
            return mainShop;
        }

        public int getOffsetType() {
            return offsetType;
        }

        public String getPoiid() {
            return poiid;
        }

        public String getProvince() {
            return province;
        }

        public String getRecommend() {
            return recommend;
        }

        public String getSid() {
            return sid;
        }

        public String getSpecial() {
            return special;
        }

        public String getStartTime() {
            return startTime;
        }

        public int getStatus() {
            return status;
        }

        public String getTelephone() {
            return telephone;
        }

        public String getThreeCategories() {
            return threeCategories;
        }

        public String getTwoCategories() {
            return twoCategories;
        }

        public int getUpdateStatus() {
            return updateStatus;
        }
    }
}
