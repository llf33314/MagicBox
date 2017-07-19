package com.gt.magicbox.http;


import com.gt.magicbox.bean.LoginBean;

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
   @POST("79B4DE7C/checkLogin.do")
   Observable<BaseResponse<LoginBean>> userLogin(@Query("eqCode")String eqCode,
                                                 @Query("userName")String user,
                                                 @Query("passWord")String pwd);
   @GET("79B4DE7C/checkLogin.do")
   Observable<BaseResponse<LoginBean>> pay(@Query("eqCode")String eqCode,
                                                 @Query("money")double money,
                                                 @Query("type")int type);
}
