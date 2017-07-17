package com.gt.magicbox.http;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.gt.magicbox.R;
import com.gt.magicbox.base.MyApplication;

import io.reactivex.disposables.Disposable;

/**
 * Created by wzb on 2017/7/12 0012.
 */

public class HttpRequestDialog extends Dialog{

    private final String TAG=HttpRequestDialog.class.getSimpleName();

    private  static HttpRequestDialog mHttpRequestDialog;

    private Disposable mDisposable;

    public static HttpRequestDialog getInstance(){
        synchronized (HttpRequestDialog.class){
            if (mHttpRequestDialog==null){
                mHttpRequestDialog=new HttpRequestDialog(MyApplication.getAppContext(), R.style.HttpRequestDialogStyle);
                mHttpRequestDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            }
            return  mHttpRequestDialog;
        }
    }

    private HttpRequestDialog(@NonNull Context context) {
        super(context);
    }

    private HttpRequestDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public void show(Disposable d){
        this.mDisposable=d;
        show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_http_request);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_BACK&&mDisposable!=null&&!mDisposable.isDisposed()){
            mDisposable.dispose();
            Log.i(TAG,"http request cancel");
        }

        return super.onKeyDown(keyCode, event);
    }

}
