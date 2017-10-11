package com.gt.magicbox.http;


import com.gt.magicbox.bean.CardGradeInfoBean;
import com.gt.magicbox.bean.CardTypeInfoBean;
import com.gt.magicbox.bean.CashOrderBean;
import com.gt.magicbox.bean.CreatedOrderBean;
import com.gt.magicbox.bean.LoginBean;
import com.gt.magicbox.bean.MemberCardBean;
import com.gt.magicbox.bean.MemberCountMoneyBean;
import com.gt.magicbox.bean.MemberSettlementBean;
import com.gt.magicbox.bean.OrderListResultBean;
import com.gt.magicbox.bean.PayCodeResultBean;
import com.gt.magicbox.bean.QRCodeBitmapBean;
import com.gt.magicbox.bean.ShiftRecordsAllBean;
import com.gt.magicbox.bean.ShopInfoBean;
import com.gt.magicbox.bean.StaffBean;
import com.gt.magicbox.bean.StartWorkBean;
import com.gt.magicbox.bean.UnpaidOrderBean;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by wzb on 2017/7/11 0011.
 */

public interface ApiService {

    @POST(HttpConfig.LOGIN_URL)
    Observable<BaseResponse<LoginBean>> userLogin(@Query("eqCode") String eqCode,
                                                  @Query("login_name") String user,
                                                  @Query("password") String pwd);

    @POST(HttpConfig.MAGIC_BOX_MEMBER)
    Observable<BaseResponse<ShopInfoBean>> memberQuery(
            @Query("login_name") String user,
            @Query("password") String pwd);

    @POST(HttpConfig.CASH_ORDER_URL)
    Observable<BaseResponse<CashOrderBean>> createCashOrder(@Query("eqCode") String eqCode,
                                                            @Query("money") String money,
                                                            @Query("type") int type,
                                                            @Query("shiftId") int shiftId);

    @POST(HttpConfig.GET_UNPAID_ORDER_URL)
    Observable<BaseResponse<UnpaidOrderBean>> getUnpaidOrderCount(@Query("eqCode") String eqCode,
                                                                  @Query("token") String token);

    @POST(HttpConfig.CHANGE_BIND)
    Observable<BaseResponse> changeBind(@Query("eqCode") String eqCode,
                                        @Query("login_name") String user,
                                        @Query("password") String pwd);

    @GET("magicBoxMobile/{eqCode}/{money}/{type}/{shiftId}/79B4DE7C/payQR")
    Observable<BaseResponse<QRCodeBitmapBean>> getQRCodeUrl(@Path("eqCode") String eqCode, @Path("money") double money,
                                                            @Path("type") int type, @Path("shiftId") int shiftId);

    @POST(HttpConfig.SCAN_CODE_PAY)
    Observable<BaseResponse<PayCodeResultBean>> scanCodePay(@Query("auth_code") String auth_code, @Query("busId") int busId,
                                                            @Query("out_trade_no") String out_trade_no, @Query("shiftId") int shiftId
            , @Query("total_fee") double total_fee);

    @GET("magicBoxMobile/{eqCode}/{status}/79B4DE7C/getOrderList")
    Observable<BaseResponse<OrderListResultBean>> getOrderList(@Path("eqCode") String eqCode, @Path("status") int status
            , @Query("current") int current, @Query("size") int size);

    @POST(HttpConfig.DELETE_NOT_PAY_ORDER)
    Observable<BaseResponse> deleteNotPayOrder(@Query("eqId") int eqId, @Query("orderId") int orderId);

    @GET("magicBoxMobile/{orderId}/{shiftId}/79B4DE7C/payQR")
    Observable<BaseResponse<CreatedOrderBean>> getCreatedQRCodeUrl(@Path("orderId") int orderId, @Path("shiftId") int shiftId);

    @POST(HttpConfig.SEND_SMS)
    Observable<BaseResponse> sendSMS(@Query("busId") int busId,
                                     @Query("content") String content,
                                     @Query("mobiles") String mobiles);

    @POST(HttpConfig.FIND_MEMBER_CARD_TYPE)
    Observable<BaseResponse<CardTypeInfoBean>> findMemberCardType(
            @Query("busId") int busId);

    @POST(HttpConfig.FIND_MEMBER_GRADE_TYPE)
    Observable<BaseResponse<CardGradeInfoBean>> findMemberGradeType(
            @Query("busId") int busId, @Query("ctId") int ctId);


    @POST(HttpConfig.GET_WECHAT_SUBSCRIPTION_QR_CODE)
    Observable<BaseResponse> getWeChatSubscriptionQRCode(@Query("busId") int busId,
                                                         @Query("eqId") int eqId);

    @POST(HttpConfig.FIND_MEMBER_CARD)
    Observable<BaseResponse<MemberCardBean>> findMemberCardByPhone(
            @Query("busId") int busId, @Query("phone") String phone);

    @POST(HttpConfig.RECEIVE_MEMBER_CARD)
    Observable<BaseResponse<MemberCardBean>> receiveMemberCard(
            @Query("bit") int bit, @Query("busId") int busId,
            @Query("ctId") int ctId, @Query("gtId") int gtId,
            @Query("memberId") int memberId, @Query("phone") String phone,
            @Query("shopId") int shopId);

    @POST(HttpConfig.RECEIVE_MEMBER_CARD)
    Observable<BaseResponse<MemberCardBean>> receiveMemberCardWithoutMemberId(
            @Query("bit") int bit, @Query("busId") int busId,
            @Query("ctId") int ctId, @Query("gtId") int gtId,
            @Query("phone") String phone, @Query("shopId") int shopId);

    @POST(HttpConfig.MEMBER_SETTLEMENT)
    Observable<BaseResponse<MemberCountMoneyBean>> postMemberSettlement(
            @Query("memberId") int memberId, @Query("totalMoney") double totalMoney,
            @Query("useCoupon") int useCoupon, @Query("useFenbi") int useFenbi,
            @Query("userJifen") int userJifen, @Query("userLeague") int userLeague);

    @POST(HttpConfig.MEMBER_RECHARGE)
    Observable<BaseResponse> memberRecharge(@Query("memberId") int memberId,
                                            @Query("money") double money,
                                            @Query("paymentType") int paymentType,
                                            @Query("shopId") int shopId);

    @POST(HttpConfig.GET_STAFF_INFO)
    Observable<BaseResponse<StaffBean>> getStaffInfoFromShopId(
            @Query("page") int page, @Query("pageSize") int pageSize,
            @Query("shopId") int shopId);

    @FormUrlEncoded
    @POST(HttpConfig.CECORDS_NOW_EXCHANGE)
    Observable<BaseResponse<StartWorkBean>> cecordsNowExchange(
            @Field("eqId") int eqId,
            @Field("shopId") int shopId,
            @Field("shopName") String shopName,
            @Field("staffCode") String staffCode,
            @Field("staffId") int staffId,
            @Field("staffName") String staffName);

    @POST(HttpConfig.GET_NOW_SR)
    Observable<BaseResponse<ShiftRecordsAllBean>> getNowSR(@Query("shiftId") int shiftId);

    @POST(HttpConfig.MEMBER_PAY)
    Observable<BaseResponse> memberPay(@Query("discountMoney") double discountMoney,
                                       @Query("memberId") int memberId,
                                       @Query("orderCode") String orderCode,
                                       @Query("pay") double pay,
                                       @Query("payType") int payType,
                                       @Query("storeId") int storeId,
                                       @Query("totalMoney") double totalMoney,
                                       @Query("ucType") int ucType);
    @FormUrlEncoded
    @POST(HttpConfig.POS_ORDER)
    Observable<BaseResponse> posOrder(@Field("eqCode") String eqCode,
                                      @Field("orderNo") String orderNo,
                                      @Field("money") double money,
                                      @Field("type") int type,
                                      @Field("shiftId") int shiftId);

}
