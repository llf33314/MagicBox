package com.gt.magicbox.http;


import com.gt.magicbox.bean.CardTypeInfoBean;
import com.gt.magicbox.bean.CreatedOrderBean;
import com.gt.magicbox.bean.LoginBean;
import com.gt.magicbox.bean.MemberBean;
import com.gt.magicbox.bean.OrderListResultBean;
import com.gt.magicbox.bean.PayCodeResultBean;
import com.gt.magicbox.bean.QRCodeBitmapBean;
import com.gt.magicbox.bean.UnpaidOrderBean;

import io.reactivex.Observable;
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
    Observable<BaseResponse<MemberBean>> memberQuery(
            @Query("login_name") String user,
            @Query("password") String pwd);

    @POST(HttpConfig.CASH_ORDER_URL)
    Observable<BaseResponse> createCashOrder(@Query("eqCode") String eqCode,
                                             @Query("money") String money,
                                             @Query("type") String type);

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
    Observable<BaseResponse> sendSMS(@Query("busId") int  busId,
                                        @Query("content") String content,
                                        @Query("mobiles") String mobiles);

    @POST(HttpConfig.FIND_MEMBER_CARD_TYPE)
    Observable<BaseResponse<CardTypeInfoBean>> findMemberCardType(
            @Query("busId")int  busId);
}
