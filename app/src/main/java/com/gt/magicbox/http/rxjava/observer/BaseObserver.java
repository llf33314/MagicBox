package com.gt.magicbox.http.rxjava.observer;

import android.support.annotation.CallSuper;
import android.util.Log;


import com.gt.magicbox.http.HttpResponseException;
import com.gt.magicbox.utils.commonutil.ToastUtil;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;



public abstract class BaseObserver<T> implements Observer<T> {


    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {

        if (e instanceof HttpException) {
            ToastUtil.getInstance().showToast("网络异常");
        } else if (e instanceof SocketTimeoutException) {  //VPN open
            ToastUtil.getInstance().showToast("服务器响应超时");
        } else if (e instanceof ConnectException) {
            ToastUtil.getInstance().showToast("连接服务器异常");
        } else if (e instanceof UnknownHostException) {
            ToastUtil.getInstance().showToast("无网络连接，请检查网络是否开启");
        } else if (e instanceof UnknownServiceException) {
            ToastUtil.getInstance().showToast("未知的服务器错误");
        } else if (e instanceof IOException) {  //飞行模式等
            ToastUtil.getInstance().showToast("没有网络，请检查网络连接");
        }else if(e instanceof HttpResponseException){//自定义异常 状态码等
            HttpResponseException  responseException = (HttpResponseException) e;
            onFailure(responseException.getError(),responseException.getMessage());
        }else {//(e instanceof RuntimeException)
            Log.e("HTTP","程序异常"+e.getMessage());
        }

    }

    @Override
    public void onComplete() {
    }

    protected abstract void onSuccess(T t);

    /**
     * 简单提示 服务器返回信息 若需要处理 则重写
     */
    protected void onFailure(int code,String msg) {
        ToastUtil.getInstance().showNewShort(msg);
    }

}
