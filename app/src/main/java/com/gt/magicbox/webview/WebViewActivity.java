package com.gt.magicbox.webview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.http.HttpConfig;
import com.gt.magicbox.utils.commonutil.PhoneUtils;
import com.gt.magicbox.webview.jsinterface.DuofenJSBridge;
import com.gt.magicbox.webview.service.UUIDService;
import com.gt.magicbox.webview.util.ObjectUtils;
import com.gt.magicbox.webview.util.PromptUtils;
import com.gt.magicbox.webview.util.RootUtils;
import com.zbar.scan.ScanCaptureAct;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Description:
 * Created by jack-lin on 2017/7/19 0019.
 */

public class WebViewActivity extends BaseActivity{
    private static final String TAG = "WebViewActivity";

    /*定义组件*/
    private WebView web;
    private ProgressBar bar;
    public final static int WEB_TYPE_PAY=0;
    public final static int WEB_TYPE_ORDER=1;
    private int webType;
    private Socket mSocket; // socket

    {
        try {
            mSocket = IO.socket(HttpConfig.SOCKET_SERVER_URL);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static int viewPage = 0; // 缓存页数
    private static boolean flag = true; // 返回控制flag

    // 定义变量
    private String webUrl ="";

    private String nowUrl = webUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (RootUtils.getRootAhth()) {
            PromptUtils.getInstance(WebViewActivity.this).showToastLong("检测到设备开启了ROOT权限!");
        }
        setContentView(R.layout.activity_webview);
        combineURL();
        if (webType == WEB_TYPE_PAY) {
            setToolBarTitle("");
        }
        // 获取组件
        web = (WebView) findViewById(R.id.web);
        bar = (ProgressBar) findViewById(R.id.bar);
        initSocket();

        //其他初始化
        initUUID();
        // webView初始化事件
        initMainWebView();
        // socket 初始化由js控制
        web.loadUrl(webUrl);
        // 这个将Java对象注入到webView，会允许JavaScript可以访问这个对象中的注解为@JavascriptInterface且是public的方法
        web.addJavascriptInterface(new DuofenJSBridge(WebViewActivity.this), "dfmb");
        web.removeJavascriptInterface("searchBoxJavaBridge_");
        if (webType == WEB_TYPE_PAY) {
            scanCode();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: ");
        getCode(intent);
    }

    // 初始化socket服务
    private void initSocket() {
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("chatevent", socketEvent);
        mSocket.connect();
    }
    private void combineURL() {
        if (this.getIntent() != null) {
            webType = this.getIntent().getIntExtra("webType", 0);
            switch (webType) {
                case WEB_TYPE_PAY:
                    double money = this.getIntent().getDoubleExtra("money", 0);
                    int type = this.getIntent().getIntExtra("payMode", 0);
                    webUrl = HttpConfig.BASE_URL + PhoneUtils.getIMEI()+ "/" + money + "/" + type + "/" + HttpConfig.PAYMENT_URL;
                    break;
                case WEB_TYPE_ORDER:
                    int status = this.getIntent().getIntExtra("status", 0);
                    webUrl = HttpConfig.BASE_URL + PhoneUtils.getIMEI()+"/"  + status + "/" + HttpConfig.ORDER_URL;
                    break;
            }
            Log.i(TAG,"webUrl="+webUrl);
        }
    }
    // 初始化UUID
    private void initUUID() {
        Log.d(TAG, "initUUID: ");
        UUIDService uuidService = new UUIDService(WebViewActivity.this);
        uuidService.initUUID();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disSocket();
    }

    /**
     * 关闭所有socket链接
     */
    private void disSocket() {
        mSocket.disconnect();
        mSocket.off("chatevent", socketEvent);
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
    }

    /**
     * 重启socket
     */
    public void reloadSocket() {
        Log.d(TAG, "reloadSocket: ");
        disSocket();
        initSocket();
    }
    public void finishWebview(){
        if (ObjectUtils.isNotEmpty(web))
            web.destroy();
        finish();
    }
    /**
     * 扫码
     * 调用zbar
     */
    public void scanCode() {
        Log.d(TAG, "scanCode: " + nowUrl);
        Intent intent = new Intent(WebViewActivity.this,ScanCaptureAct.class);
        startActivity(intent);
    }

    // 扫码返回
    private void getCode(Intent intent){
        Log.d(TAG, "getCode: in");
        if (intent != null){
            Log.d(TAG, "getCode: intent not null");
            String result = intent.getStringExtra("RQ_CODE");
            if (result != null){
                viewPage--;
                Log.d(TAG, "getCode: " + result);
                webLoadJS("scanCallBack", result);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 监听是不是按了返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "keyCode --> " + keyCode);
        /** 按下键盘上返回按钮 */
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (webType == WebViewActivity.WEB_TYPE_PAY) {
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("确定要退出支付吗？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                finishWebview();
                            }
                        }).show();
            }else {
                finishWebview();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 初始化WebView
     */
    private void initMainWebView() {
        // web设置
        WebSettings webSettings = web.getSettings();
        // 运行webview运行JavaScript脚本
        webSettings.setJavaScriptEnabled(true);
        // 设置缓存策略
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                addViewPage();
                nowUrl = url;
               // scanCode();
                super.onPageFinished(view, url);
            }
        });

        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                bar.setVisibility(View.VISIBLE);
                bar.setProgress(newProgress);
                if (newProgress >= 100) {
                    bar.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }

            // 处理javascript中的alert
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setTitle("温馨提示")
                        .setMessage(message)
                        .setPositiveButton("确定", null);

                // 禁止响应按back键的事件
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
                result.confirm();//  因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
                return true;// 不需要绑定按键事件
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
    }

    // webView返回
    public boolean webViewGoBack() {
        if (viewPage > 1) {
            viewPage--;
            flag = false;
            Log.d("viewPage", viewPage + "");
            web.goBack();
            return true;
        }
        return false;
    }

    // webView返回
    public void webBack() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewPage--;
                flag = false;
                Log.d("viewPage", viewPage + "");
                web.goBack();
            }
        });
    }

    // webView页面跳转
    private static void addViewPage() {
        if (flag) {
            viewPage++;
        }
        flag = true;
        Log.d("viewPage", viewPage + "");
    }

    // webView页面刷新
    public void webReload() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewPage--;
                web.reload();
            }
        });
    }

    /**
     * js回调
     *
     * @param jsFn
     * @param jsParam
     */
    public void webLoadJS(final String jsFn, final String jsParam) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StringBuilder stringBuilder = new StringBuilder(JSContent.JAVASCRIPT);
                stringBuilder.append(JSContent.DFMAGICBOXBACK);
                stringBuilder.append(jsFn);
                stringBuilder.append("('");
                stringBuilder.append(jsParam);
                stringBuilder.append("')");
                Log.d(TAG, stringBuilder.toString());
                web.loadUrl(stringBuilder.toString());
            }
        });
    }

    // socket连接
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "onConnect");
            String UUID = PhoneUtils.getIMEI();
            Log.d(TAG, "auth key : " + HttpConfig.SOCKET_ANDROID_AUTH_KEY + UUID);
            mSocket.emit(HttpConfig.SOCKET_ANDROID_AUTH, HttpConfig.SOCKET_ANDROID_AUTH_KEY + UUID);
            Log.d(TAG, "call: send android auth over");
        }
    };

    // 接收推送事件
    private Emitter.Listener socketEvent = new Emitter.Listener() {
        @Override
        public void call(Object... objects) {
            Log.d(TAG, "socketEvent");
            JSONObject data = (JSONObject) objects[0];
            String retData = null;
            try {
                retData = data.get("message").toString();
            } catch (JSONException e) {
                e.printStackTrace();
                retData = "";
            }
            Log.d(TAG, "socket --> " + retData.toString());
            webLoadJS("socketCallBack", retData.toString());
        }
    };

    // socket disConnect
    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "diconnected");
        }
    };

    // socket connectError
    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "Error connecting");
        }
    };

}
