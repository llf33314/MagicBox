package com.gt.magicbox.http;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wzb on 2017/7/11 0011.
 * Http相关配置
 */

public class HttpCall {
    private static String token;

    private static ApiService mApiService;

    public static ApiService getApiService(){
        if (mApiService==null){
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(11, TimeUnit.SECONDS)
                   // .addNetworkInterceptor(mRequestInterceptor)
                    .addInterceptor(loggingInterceptor)
                   // .authenticator(mAuthenticator2)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(HttpConfig.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            mApiService = retrofit.create(ApiService.class);
        }
        return mApiService;
    }

}

