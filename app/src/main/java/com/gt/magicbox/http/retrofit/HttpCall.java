package com.gt.magicbox.http.retrofit;

import com.gt.magicbox.BuildConfig;
import com.gt.magicbox.Constant;
import com.gt.magicbox.base.BaseConstant;
import com.gt.magicbox.http.ApiService;
import com.gt.magicbox.http.HttpConfig;
import com.gt.magicbox.http.retrofit.converter.string.StringConverterFactory;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HttpCall {

    private static String token;

    private static ApiService mApiService;

    public static ApiService getApiService(){
        if (mApiService==null){
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            if (BaseConstant.isPrintLog){
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            }else {
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
            }

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.YJ_BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(StringConverterFactory.create()) //String 转换
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .validateEagerly(true)
                    .build();

            mApiService = retrofit.create(ApiService.class);
        }
        return mApiService;
    }


}
