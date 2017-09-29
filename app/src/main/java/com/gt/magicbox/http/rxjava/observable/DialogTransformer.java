package com.gt.magicbox.http.rxjava.observable;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.gt.magicbox.utils.commonutil.AppManager;
import com.gt.magicbox.widget.LoadingProgressDialog;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


public class DialogTransformer {

    //这个对话框、网络加载能否取消  默认能
    private boolean cancelable=true;

    public DialogTransformer() {
        this(true);
    }

    public DialogTransformer(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public <T> ObservableTransformer<T, T> transformer() {
        return new ObservableTransformer<T, T>() {
            private LoadingProgressDialog httpRequestDialog;
            @Override
            public ObservableSource<T> apply(final Observable<T> upstream) {

                return  upstream.doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull final Disposable disposable) throws Exception {

                        httpRequestDialog = new LoadingProgressDialog(AppManager.getInstance().currentActivity());
                        httpRequestDialog.show();
                        if (cancelable) {
                            httpRequestDialog.setOnDismissListener(new OnDismissListener() {
                                @Override
                                public void onDismiss(SVProgressHUD svProgressHUD) {
                                    disposable.dispose();
                                }
                            });
                        }
                    }
                }).doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (httpRequestDialog.isShowing()) {
                            httpRequestDialog.dismiss();
                        }
                    }
                });
            }
        };
    }
}
