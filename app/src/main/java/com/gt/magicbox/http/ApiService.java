package com.gt.magicbox.http;


import com.gt.magicbox.bean.LoginBean;
import com.gt.magicbox.bean.UnpaidOrderBean;
import com.gt.magicbox.bean.VoidBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by wzb on 2017/7/11 0011.
 */

public interface ApiService {
   /* @POST
    Observable<BaseResponse<TestBean>> getTextData(@Query("test") String s);

    @GET("https://news-at.zhihu.com/api/4/themes")
    Observable<BaseResponse<TestBean>> getZhihu();
    */
   @POST(HttpConfig.LOGIN_URL)
   Observable<BaseResponse<LoginBean>> userLogin(@Query("eqCode")String eqCode,
                                                 @Query("userName")String user,
                                                 @Query("passWord")String pwd);

   @POST(HttpConfig.CASH_ORDER_URL)
   Observable<BaseResponse<VoidBean>> createCashOrder(@Query("eqCode") String eqCode,
                                                      @Query("money") String money,
                                                      @Query("type") String type);
   @POST(HttpConfig.GET_UNPAID_ORDER_URL)
   Observable<BaseResponse<UnpaidOrderBean>> getUnpaidOrderCount(@Query("eqCode") String eqCode,
                                                             @Query("token") String token);
}
