package com.gt.magicbox.utils;

import android.content.Context;
import android.text.TextUtils;


import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import retrofit2.HttpException;

/**
 * 订阅者
 */
public class SimpleObserver<T> implements Observer<T> {

    private static final String TAG = SimpleObserver.class.getSimpleName();
    private Consumer onNext;
    private Consumer onError;
    private Action onComplate;
    private Disposable disposable;

    private SimpleObserver(){
    }

    public SimpleObserver(Consumer<T> onNext){
        this.onNext = onNext;
    }

    public SimpleObserver( Consumer<T> onNext, Consumer onError){
        this.onNext = onNext;
        this.onError = onError;
    }

    public SimpleObserver( Consumer<T> onNext, Action onComplate){
        this.onNext = onNext;
        this.onComplate = onComplate;
    }

    public SimpleObserver( Consumer<T> onNext, Action onComplate, Consumer onError){
        this.onNext = onNext;
        this.onError = onError;
        this.onComplate = onComplate;
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(T value) {
        try {
            onNext.accept(value);
        } catch (Exception e) {
            onError(e);
        }
    }

    @Override
    public void onError(Throwable e) {

            try {
                if (e instanceof SocketTimeoutException) {
                   // ToastUtils.showShortToast(R.string.network_out_time);
                } else if (e instanceof ConnectException) {
                   // ToastUtils.showShortToast(R.string.network_break_off);
                } else if (e instanceof UnknownHostException || e instanceof HttpException) {
                  //  ToastUtils.showShortToast(R.string.network_service_off);
                }else if ( e instanceof HttpException) {
                    if (!TextUtils.isEmpty(e.getMessage())){
                      //  ToastUtils.showShortToast(e.getMessage());
                    }else {
                     //   ToastUtils.showShortToast(R.string.network_service_off);
                    }
                } else {//未知错误
                   // ToastUtils.showShortToast(e.getMessage());
                }
                if (onError != null){
                    onError.accept(e) ;
                }
                onComplete();
            } catch (Exception e1) {
                onError(e1);
            }

    }

    @Override
    public void onComplete() {
        if (onComplate != null) try {
            onComplate.run();
        } catch (Exception e) {
            onError(e);
        }
    }
}
