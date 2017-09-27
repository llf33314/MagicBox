package com.gt.magicbox.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wzb on 2017/9/25 0025.
 */

public class StaffBean implements Serializable {

    /**
     * count : 1
     * staffList : [{"branid":75,"createPerson":33,"createTime":"2017-09-2118: 22: 52","email":"","gender":0,"id":108,"isdelect":0,"jobNumber":"008","loginName":"zwx008","name":"008","phone":"15602657228","positionid":70,"remark":"","shopId":31,"status":0}]
     */

    private int count;
    private List<StaffListBean> staffList;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<StaffListBean> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<StaffListBean> staffList) {
        this.staffList = staffList;
    }

    public static class StaffListBean implements Serializable{
        /**
         * branid : 75
         * createPerson : 33
         * createTime : 2017-09-2118: 22: 52
         * email :
         * gender : 0
         * id : 108
         * isdelect : 0
         * jobNumber : 008
         * loginName : zwx008
         * name : 008
         * phone : 15602657228
         * positionid : 70
         * remark :
         * shopId : 31
         * status : 0
         */

        private int branid;
        private int createPerson;
        private String createTime;
        private String email;
        private int gender;
        private int id;
        private int isdelect;
        private String jobNumber;
        private String loginName;
        private String name;
        private String phone;
        private int positionid;
        private String remark;
        private int shopId;
        private int status;

        public int getBranid() {
            return branid;
        }

        public void setBranid(int branid) {
            this.branid = branid;
        }

        public int getCreatePerson() {
            return createPerson;
        }

        public void setCreatePerson(int createPerson) {
            this.createPerson = createPerson;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIsdelect() {
            return isdelect;
        }

        public void setIsdelect(int isdelect) {
            this.isdelect = isdelect;
        }

        public String getJobNumber() {
            return jobNumber;
        }

        public void setJobNumber(String jobNumber) {
            this.jobNumber = jobNumber;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getPositionid() {
            return positionid;
        }

        public void setPositionid(int positionid) {
            this.positionid = positionid;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getShopId() {
            return shopId;
        }

        public void setShopId(int shopId) {
            this.shopId = shopId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
