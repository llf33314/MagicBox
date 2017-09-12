package com.gt.magicbox.http;


import com.gt.magicbox.bean.LoginBean;
import com.gt.magicbox.bean.MemberBean;
import com.gt.magicbox.bean.QRCodeBean;
import com.gt.magicbox.bean.UnpaidOrderBean;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by wzb on 2017/7/11 0011.
 */

public interface ApiService {
  
   @POST(HttpConfig.LOGIN_URL)
   Observable<BaseResponse<LoginBean>> userLogin(@Query("eqCode")String eqCode,
                                                 @Query("login_name")String user,
                                                 @Query("password")String pwd);
   @POST(HttpConfig.MAGIC_BOX_MEMBER)
   Observable<BaseResponse<MemberBean>> memberQuery(
                                                 @Query("login_name")String user,
                                                 @Query("password")String pwd);

   @POST(HttpConfig.CASH_ORDER_URL)
   Observable<BaseResponse> createCashOrder(@Query("eqCode") String eqCode,
                                                      @Query("money") String money,
                                                      @Query("type") String type);
   @POST(HttpConfig.GET_UNPAID_ORDER_URL)
   Observable<BaseResponse<UnpaidOrderBean>> getUnpaidOrderCount(@Query("eqCode") String eqCode,
                                                             @Query("token") String token);
   @POST(HttpConfig.CHANGE_BIND)
   Observable<BaseResponse> changeBind(@Query("eqCode")String eqCode,
                                                        @Query("login_name")String user,
                                                        @Query("password")String pwd);
   @GET("magicBoxMobile/{eqCode}/{money}/{type}/{shiftId}/79B4DE7C/payQR")
   Observable<BaseResponse<QRCodeBean>> getQRCodeUrl(@Path("eqCode")String eqCode,@Path("money")double money,
                                                     @Path("type")int type,@Path("shiftId")int shiftId);
}
