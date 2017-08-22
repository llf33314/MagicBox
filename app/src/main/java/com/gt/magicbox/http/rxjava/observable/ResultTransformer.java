package com.gt.magicbox.http.rxjava.observable;



import com.gt.magicbox.http.BaseResponse;
import com.gt.magicbox.http.HttpResponseException;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


public class ResultTransformer {

    /**
     * 正常格式流程
     * 已经切换线程
     */
    public static <T> ObservableTransformer<BaseResponse<T>, T> transformer() {
        return new ObservableTransformer<BaseResponse<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<BaseResponse<T>> upstream) {
                    return upstream
                        .flatMap(ResultTransformer.<T>flatMap())
                        .compose(SchedulerTransformer.<T>transformer());
            }
        };
    }

    /**
     * 正常格式流程
     */
    public static <T> ObservableTransformer<BaseResponse<T>, T> rxActivityTransformer(final RxAppCompatActivity activity) {

        return new ObservableTransformer<BaseResponse<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<BaseResponse<T>> upstream) {
                return upstream
                        .flatMap(ResultTransformer.<T>flatMap())
                        .compose(activity.<T>bindToLifecycle())
                        .compose(SchedulerTransformer.<T>transformer());
            }
        };
    }



    private static <T> Function<BaseResponse<T>, ObservableSource<T>> flatMap() {
        return new Function<BaseResponse<T>, ObservableSource<T>>() {
            @Override
            public ObservableSource<T> apply(@NonNull final BaseResponse<T> tBaseResponse) throws Exception {
                return new Observable<T>() {
                    @Override
                    protected void subscribeActual(Observer<? super T> observer) {
                        if (tBaseResponse.isSuccess()) {
                            observer.onNext(tBaseResponse.getData());
                            observer.onComplete();
                        } else {
                            observer.onError(new HttpResponseException(tBaseResponse.getMsg(), tBaseResponse.getCode()));
                        }
                    }
                };
            }
        };
    }

    /**
     * 无data BaseResponse  也会预处理  处理后返回BaseResponse
     * @param <
     * @return
     */
    public static ObservableTransformer<BaseResponse,BaseResponse> transformerNoData(){
        return new ObservableTransformer<BaseResponse, BaseResponse>() {

            @Override
            public ObservableSource<BaseResponse> apply(@NonNull final Observable<BaseResponse> upstream) {
                return upstream
                        .flatMap(ResultTransformer.<BaseResponse>mapNoDta())
                        .compose(SchedulerTransformer.<BaseResponse>transformer());
            }
        };
    }

    private static Function<BaseResponse,ObservableSource<BaseResponse>> mapNoDta(){
        return new Function<BaseResponse, ObservableSource<BaseResponse>>() {
            @Override
            public ObservableSource<BaseResponse> apply(@NonNull final BaseResponse baseResponse) throws Exception {
                return new Observable<BaseResponse>() {
                    @Override
                    protected void subscribeActual(Observer<? super BaseResponse> observer) {
                        if (baseResponse.isSuccess()) {
                            observer.onNext(baseResponse);
                            observer.onComplete();
                        } else {
                            observer.onError(new HttpResponseException(baseResponse.getMsg(), baseResponse.getCode()));
                        }
                    }
                };
            }
        };
    }
}
