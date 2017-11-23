package com.gt.magicbox.bean;

import java.util.List;

/**
 * Created by wzb on 2017/11/23 0023.
 */

public class DuofenCards {

    /**
     * duofencards : [{"summary":"领券即可使用领券即可使用","dateTimeSet":null,"phone":null,"gift":null,"cashLeastCost":0,"passTime":1509461790000,"addUser":0,"textImageList":"[{\"image_url\":\"//image/3/goodtom/3/20171031/C1FDB9540B497CB613C4CA3DEB147E85.jpg\",\"text\":\"领券即可使用\"}]","type":"DATE_TYPE_FIX_TERM","timeType":1,"brandName":"车小算9.8折折扣券","fixedTerm":20,"dealDetail":null,"id":123,"subTitle":null,"canShare":0,"title":"车小算折扣券","description":"领券即可使用","beginTimestamp":null,"userId":36,"defaultDetail":null,"servicePhone":"13477996663","quantity":0,"leastCost":null,"fixedBeginTerm":1,"notice":"领券即可使用","canGiveFriend":0,"timeLimit":"[{\"type\":\"MONDAY\"},{\"type\":\"TUESDAY\"},{\"type\":\"WEDNESDAY\"},{\"type\":\"THURSDAY\"},{\"type\":\"FRIDAY\"},{\"type\":\"SATURDAY\"},{\"type\":\"SUNDAY\"}]","cardId":"nduxmhduk9ep","locationIdList":"33,43,","image":"//image/3/goodtom/3/20171031/C1FDB9540B497CB613C4CA3DEB147E85.jpg","logoUrl":"//image/3/goodtom/3/20171031/C1FDB9540B497CB613C4CA3DEB147E85.jpg","iconUrlList":"//image/3/goodtom/3/20171031/C1FDB9540B497CB613C4CA3DEB147E85.jpg","iddelete":0,"ctime":1509461790000,"reduceCost":0,"cardType":0,"cardStatus":2,"examineId":0,"discount":9.8,"isCallSMS":0,"color":"#2c9f67","publicId":null,"endTimestamp":null},{"summary":"领券即可使用","dateTimeSet":null,"phone":null,"gift":null,"cashLeastCost":10,"passTime":1509461787000,"addUser":0,"textImageList":"[{\"image_url\":\"//image/3/goodtom/3/20171031/A2010E33068C783E4CC5780A4A38BDB0.jpg\",\"text\":\"领券即可使用\"}]","type":"DATE_TYPE_FIX_TERM","timeType":1,"brandName":"车小算满100减10代金券","fixedTerm":20,"dealDetail":null,"id":124,"subTitle":null,"canShare":0,"title":"车小算代金券","description":"领券即可使用","beginTimestamp":null,"userId":36,"defaultDetail":null,"servicePhone":"13799663341","quantity":0,"leastCost":null,"fixedBeginTerm":1,"notice":"领券即可使用","canGiveFriend":0,"timeLimit":"[{\"type\":\"MONDAY\"},{\"type\":\"TUESDAY\"},{\"type\":\"WEDNESDAY\"},{\"type\":\"THURSDAY\"},{\"type\":\"FRIDAY\"},{\"type\":\"SATURDAY\"},{\"type\":\"SUNDAY\"}]","cardId":"ctl1evftu3or","locationIdList":"33,43,","image":"//image/3/goodtom/3/20171031/A2010E33068C783E4CC5780A4A38BDB0.jpg","logoUrl":"//image/3/goodtom/3/20171031/A2010E33068C783E4CC5780A4A38BDB0.jpg","iconUrlList":"//image/3/goodtom/3/20171031/A2010E33068C783E4CC5780A4A38BDB0.jpg","iddelete":0,"ctime":1509461787000,"reduceCost":100,"cardType":1,"cardStatus":2,"examineId":0,"discount":0,"isCallSMS":0,"color":"#2c9f67","publicId":null,"endTimestamp":null}]
     * receives : {"cardImage":"//image/3/goodtom/3/20171101/3B78558F6AEDB11AB48C6743C475D1FE.jpg","receiveDate":1512057599000,"threeMallId":0,"busId":36,"giveJifen":0,"state":1,"titleCard":"车小算代金券,车小算折扣券,","numlimit":0,"id":456,"lqId":0,"cardIds":"124,123,","fenbi":0,"buyMoney":0,"backColor":"rgb(198, 75, 46)","maxNumType":0,"threeNum":0,"deliveryType1":0,"cardsName":"008","ctIds":"0","isrecommend":0,"deliveryType":1,"cardMessage":"[{\"cardId\":\"124\",\"num\":0,\"cardName\":\"车小算代金券\"},{\"cardId\":\"123\",\"num\":0,\"cardName\":\"车小算折扣券\"}]","giveFlow":0,"giveMoney":0,"code":"am3the","gtIds":"0","cardType":0,"mobilePhone":null,"publicId":null,"isCallSms":0,"deliveryAddr":0,"classifyId":0,"giveFenbi":0,"maxNum":0,"jifen":0}
     */

    private ReceivesBean receives;
    private List<DuofencardsBean> duofencards;

    public ReceivesBean getReceives() {
        return receives;
    }

    public void setReceives(ReceivesBean receives) {
        this.receives = receives;
    }

    public List<DuofencardsBean> getDuofencards() {
        return duofencards;
    }

    public void setDuofencards(List<DuofencardsBean> duofencards) {
        this.duofencards = duofencards;
    }

    public static class ReceivesBean {
        /**
         * cardImage : //image/3/goodtom/3/20171101/3B78558F6AEDB11AB48C6743C475D1FE.jpg
         * receiveDate : 1512057599000
         * threeMallId : 0
         * busId : 36
         * giveJifen : 0
         * state : 1
         * titleCard : 车小算代金券,车小算折扣券,
         * numlimit : 0
         * id : 456
         * lqId : 0
         * cardIds : 124,123,
         * fenbi : 0
         * buyMoney : 0
         * backColor : rgb(198, 75, 46)
         * maxNumType : 0
         * threeNum : 0
         * deliveryType1 : 0
         * cardsName : 008
         * ctIds : 0
         * isrecommend : 0
         * deliveryType : 1
         * cardMessage : [{"cardId":"124","num":0,"cardName":"车小算代金券"},{"cardId":"123","num":0,"cardName":"车小算折扣券"}]
         * giveFlow : 0
         * giveMoney : 0
         * code : am3the
         * gtIds : 0
         * cardType : 0
         * mobilePhone : null
         * publicId : null
         * isCallSms : 0
         * deliveryAddr : 0
         * classifyId : 0
         * giveFenbi : 0
         * maxNum : 0
         * jifen : 0
         */

        private String cardImage;
        private long receiveDate;
        private int threeMallId;
        private int busId;
        private int giveJifen;
        private int state;
        private String titleCard;
        private int numlimit;
        private int id;
        private int lqId;
        private String cardIds;
        private int fenbi;
        private int buyMoney;
        private String backColor;
        private int maxNumType;
        private int threeNum;
        private int deliveryType1;
        private String cardsName;
        private String ctIds;
        private int isrecommend;
        private int deliveryType;
        private String cardMessage;
        private int giveFlow;
        private int giveMoney;
        private String code;
        private String gtIds;
        private int cardType;
        private Object mobilePhone;
        private Object publicId;
        private int isCallSms;
        private int deliveryAddr;
        private int classifyId;
        private int giveFenbi;
        private int maxNum;
        private int jifen;

        public String getCardImage() {
            return cardImage;
        }

        public void setCardImage(String cardImage) {
            this.cardImage = cardImage;
        }

        public long getReceiveDate() {
            return receiveDate;
        }

        public void setReceiveDate(long receiveDate) {
            this.receiveDate = receiveDate;
        }

        public int getThreeMallId() {
            return threeMallId;
        }

        public void setThreeMallId(int threeMallId) {
            this.threeMallId = threeMallId;
        }

        public int getBusId() {
            return busId;
        }

        public void setBusId(int busId) {
            this.busId = busId;
        }

        public int getGiveJifen() {
            return giveJifen;
        }

        public void setGiveJifen(int giveJifen) {
            this.giveJifen = giveJifen;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getTitleCard() {
            return titleCard;
        }

        public void setTitleCard(String titleCard) {
            this.titleCard = titleCard;
        }

        public int getNumlimit() {
            return numlimit;
        }

        public void setNumlimit(int numlimit) {
            this.numlimit = numlimit;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLqId() {
            return lqId;
        }

        public void setLqId(int lqId) {
            this.lqId = lqId;
        }

        public String getCardIds() {
            return cardIds;
        }

        public void setCardIds(String cardIds) {
            this.cardIds = cardIds;
        }

        public int getFenbi() {
            return fenbi;
        }

        public void setFenbi(int fenbi) {
            this.fenbi = fenbi;
        }

        public int getBuyMoney() {
            return buyMoney;
        }

        public void setBuyMoney(int buyMoney) {
            this.buyMoney = buyMoney;
        }

        public String getBackColor() {
            return backColor;
        }

        public void setBackColor(String backColor) {
            this.backColor = backColor;
        }

        public int getMaxNumType() {
            return maxNumType;
        }

        public void setMaxNumType(int maxNumType) {
            this.maxNumType = maxNumType;
        }

        public int getThreeNum() {
            return threeNum;
        }

        public void setThreeNum(int threeNum) {
            this.threeNum = threeNum;
        }

        public int getDeliveryType1() {
            return deliveryType1;
        }

        public void setDeliveryType1(int deliveryType1) {
            this.deliveryType1 = deliveryType1;
        }

        public String getCardsName() {
            return cardsName;
        }

        public void setCardsName(String cardsName) {
            this.cardsName = cardsName;
        }

        public String getCtIds() {
            return ctIds;
        }

        public void setCtIds(String ctIds) {
            this.ctIds = ctIds;
        }

        public int getIsrecommend() {
            return isrecommend;
        }

        public void setIsrecommend(int isrecommend) {
            this.isrecommend = isrecommend;
        }

        public int getDeliveryType() {
            return deliveryType;
        }

        public void setDeliveryType(int deliveryType) {
            this.deliveryType = deliveryType;
        }

        public String getCardMessage() {
            return cardMessage;
        }

        public void setCardMessage(String cardMessage) {
            this.cardMessage = cardMessage;
        }

        public int getGiveFlow() {
            return giveFlow;
        }

        public void setGiveFlow(int giveFlow) {
            this.giveFlow = giveFlow;
        }

        public int getGiveMoney() {
            return giveMoney;
        }

        public void setGiveMoney(int giveMoney) {
            this.giveMoney = giveMoney;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getGtIds() {
            return gtIds;
        }

        public void setGtIds(String gtIds) {
            this.gtIds = gtIds;
        }

        public int getCardType() {
            return cardType;
        }

        public void setCardType(int cardType) {
            this.cardType = cardType;
        }

        public Object getMobilePhone() {
            return mobilePhone;
        }

        public void setMobilePhone(Object mobilePhone) {
            this.mobilePhone = mobilePhone;
        }

        public Object getPublicId() {
            return publicId;
        }

        public void setPublicId(Object publicId) {
            this.publicId = publicId;
        }

        public int getIsCallSms() {
            return isCallSms;
        }

        public void setIsCallSms(int isCallSms) {
            this.isCallSms = isCallSms;
        }

        public int getDeliveryAddr() {
            return deliveryAddr;
        }

        public void setDeliveryAddr(int deliveryAddr) {
            this.deliveryAddr = deliveryAddr;
        }

        public int getClassifyId() {
            return classifyId;
        }

        public void setClassifyId(int classifyId) {
            this.classifyId = classifyId;
        }

        public int getGiveFenbi() {
            return giveFenbi;
        }

        public void setGiveFenbi(int giveFenbi) {
            this.giveFenbi = giveFenbi;
        }

        public int getMaxNum() {
            return maxNum;
        }

        public void setMaxNum(int maxNum) {
            this.maxNum = maxNum;
        }

        public int getJifen() {
            return jifen;
        }

        public void setJifen(int jifen) {
            this.jifen = jifen;
        }
    }

    public static class DuofencardsBean {
        /**
         * summary : 领券即可使用领券即可使用
         * dateTimeSet : null
         * phone : null
         * gift : null
         * cashLeastCost : 0
         * passTime : 1509461790000
         * addUser : 0
         * textImageList : [{"image_url":"//image/3/goodtom/3/20171031/C1FDB9540B497CB613C4CA3DEB147E85.jpg","text":"领券即可使用"}]
         * type : DATE_TYPE_FIX_TERM
         * timeType : 1
         * brandName : 车小算9.8折折扣券
         * fixedTerm : 20
         * dealDetail : null
         * id : 123
         * subTitle : null
         * canShare : 0
         * title : 车小算折扣券
         * description : 领券即可使用
         * beginTimestamp : null
         * userId : 36
         * defaultDetail : null
         * servicePhone : 13477996663
         * quantity : 0
         * leastCost : null
         * fixedBeginTerm : 1
         * notice : 领券即可使用
         * canGiveFriend : 0
         * timeLimit : [{"type":"MONDAY"},{"type":"TUESDAY"},{"type":"WEDNESDAY"},{"type":"THURSDAY"},{"type":"FRIDAY"},{"type":"SATURDAY"},{"type":"SUNDAY"}]
         * cardId : nduxmhduk9ep
         * locationIdList : 33,43,
         * image : //image/3/goodtom/3/20171031/C1FDB9540B497CB613C4CA3DEB147E85.jpg
         * logoUrl : //image/3/goodtom/3/20171031/C1FDB9540B497CB613C4CA3DEB147E85.jpg
         * iconUrlList : //image/3/goodtom/3/20171031/C1FDB9540B497CB613C4CA3DEB147E85.jpg
         * iddelete : 0
         * ctime : 1509461790000
         * reduceCost : 0
         * cardType : 0
         * cardStatus : 2
         * examineId : 0
         * discount : 9.8
         * isCallSMS : 0
         * color : #2c9f67
         * publicId : null
         * endTimestamp : null
         */

        private String summary;
        private Object dateTimeSet;
        private Object phone;
        private Object gift;
        private int cashLeastCost;
        private long passTime;
        private int addUser;
        private String textImageList;
        private String type;
        private int timeType;
        private String brandName;
        private int fixedTerm;
        private Object dealDetail;
        private int id;
        private Object subTitle;
        private int canShare;
        private String title;
        private String description;
        private Object beginTimestamp;
        private int userId;
        private Object defaultDetail;
        private String servicePhone;
        private int quantity;
        private Object leastCost;
        private int fixedBeginTerm;
        private String notice;
        private int canGiveFriend;
        private String timeLimit;
        private String cardId;
        private String locationIdList;
        private String image;
        private String logoUrl;
        private String iconUrlList;
        private int iddelete;
        private long ctime;
        private int reduceCost;
        private int cardType;
        private int cardStatus;
        private int examineId;
        private double discount;
        private int isCallSMS;
        private String color;
        private Object publicId;
        private Object endTimestamp;

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public Object getDateTimeSet() {
            return dateTimeSet;
        }

        public void setDateTimeSet(Object dateTimeSet) {
            this.dateTimeSet = dateTimeSet;
        }

        public Object getPhone() {
            return phone;
        }

        public void setPhone(Object phone) {
            this.phone = phone;
        }

        public Object getGift() {
            return gift;
        }

        public void setGift(Object gift) {
            this.gift = gift;
        }

        public int getCashLeastCost() {
            return cashLeastCost;
        }

        public void setCashLeastCost(int cashLeastCost) {
            this.cashLeastCost = cashLeastCost;
        }

        public long getPassTime() {
            return passTime;
        }

        public void setPassTime(long passTime) {
            this.passTime = passTime;
        }

        public int getAddUser() {
            return addUser;
        }

        public void setAddUser(int addUser) {
            this.addUser = addUser;
        }

        public String getTextImageList() {
            return textImageList;
        }

        public void setTextImageList(String textImageList) {
            this.textImageList = textImageList;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getTimeType() {
            return timeType;
        }

        public void setTimeType(int timeType) {
            this.timeType = timeType;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public int getFixedTerm() {
            return fixedTerm;
        }

        public void setFixedTerm(int fixedTerm) {
            this.fixedTerm = fixedTerm;
        }

        public Object getDealDetail() {
            return dealDetail;
        }

        public void setDealDetail(Object dealDetail) {
            this.dealDetail = dealDetail;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(Object subTitle) {
            this.subTitle = subTitle;
        }

        public int getCanShare() {
            return canShare;
        }

        public void setCanShare(int canShare) {
            this.canShare = canShare;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Object getBeginTimestamp() {
            return beginTimestamp;
        }

        public void setBeginTimestamp(Object beginTimestamp) {
            this.beginTimestamp = beginTimestamp;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public Object getDefaultDetail() {
            return defaultDetail;
        }

        public void setDefaultDetail(Object defaultDetail) {
            this.defaultDetail = defaultDetail;
        }

        public String getServicePhone() {
            return servicePhone;
        }

        public void setServicePhone(String servicePhone) {
            this.servicePhone = servicePhone;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public Object getLeastCost() {
            return leastCost;
        }

        public void setLeastCost(Object leastCost) {
            this.leastCost = leastCost;
        }

        public int getFixedBeginTerm() {
            return fixedBeginTerm;
        }

        public void setFixedBeginTerm(int fixedBeginTerm) {
            this.fixedBeginTerm = fixedBeginTerm;
        }

        public String getNotice() {
            return notice;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }

        public int getCanGiveFriend() {
            return canGiveFriend;
        }

        public void setCanGiveFriend(int canGiveFriend) {
            this.canGiveFriend = canGiveFriend;
        }

        public String getTimeLimit() {
            return timeLimit;
        }

        public void setTimeLimit(String timeLimit) {
            this.timeLimit = timeLimit;
        }

        public String getCardId() {
            return cardId;
        }

        public void setCardId(String cardId) {
            this.cardId = cardId;
        }

        public String getLocationIdList() {
            return locationIdList;
        }

        public void setLocationIdList(String locationIdList) {
            this.locationIdList = locationIdList;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }

        public String getIconUrlList() {
            return iconUrlList;
        }

        public void setIconUrlList(String iconUrlList) {
            this.iconUrlList = iconUrlList;
        }

        public int getIddelete() {
            return iddelete;
        }

        public void setIddelete(int iddelete) {
            this.iddelete = iddelete;
        }

        public long getCtime() {
            return ctime;
        }

        public void setCtime(long ctime) {
            this.ctime = ctime;
        }

        public int getReduceCost() {
            return reduceCost;
        }

        public void setReduceCost(int reduceCost) {
            this.reduceCost = reduceCost;
        }

        public int getCardType() {
            return cardType;
        }

        public void setCardType(int cardType) {
            this.cardType = cardType;
        }

        public int getCardStatus() {
            return cardStatus;
        }

        public void setCardStatus(int cardStatus) {
            this.cardStatus = cardStatus;
        }

        public int getExamineId() {
            return examineId;
        }

        public void setExamineId(int examineId) {
            this.examineId = examineId;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public int getIsCallSMS() {
            return isCallSMS;
        }

        public void setIsCallSMS(int isCallSMS) {
            this.isCallSMS = isCallSMS;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public Object getPublicId() {
            return publicId;
        }

        public void setPublicId(Object publicId) {
            this.publicId = publicId;
        }

        public Object getEndTimestamp() {
            return endTimestamp;
        }

        public void setEndTimestamp(Object endTimestamp) {
            this.endTimestamp = endTimestamp;
        }
    }
}
