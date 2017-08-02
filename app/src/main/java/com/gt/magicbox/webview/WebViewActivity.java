package com.gt.magicbox.webview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dtr.zbar.build.ZBarDecoder;
import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.http.HttpConfig;
import com.gt.magicbox.utils.commonutil.PhoneUtils;
import com.gt.magicbox.utils.commonutil.ToastUtil;
import com.gt.magicbox.webview.jsinterface.DuofenJSBridge;
import com.gt.magicbox.webview.service.UUIDService;
import com.gt.magicbox.webview.util.ObjectUtils;
import com.gt.magicbox.webview.util.PromptUtils;
import com.gt.magicbox.webview.util.RootUtils;
import com.zbar.scan.CameraManager;
import com.zbar.scan.CameraPreview;
import com.zbar.scan.ScanCaptureAct;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;

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
    public final static int WEB_TYPE_SERVER_PUSH=2;
    private int webType;
    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;
    private CameraManager mCameraManager;
    private FrameLayout scanPreview;
    private Button scanRestart;
    private RelativeLayout scanContainer;
    private RelativeLayout scanCropView;
    //private ImageView scanLine;

    private Rect mCropRect = null;
    private boolean barcodeScanned = false;
    private boolean previewing = true;


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
        if (webType == WEB_TYPE_PAY||webType ==WEB_TYPE_SERVER_PUSH) {
            setToolBarTitle("");
        }
        // 获取组件
        web = (WebView) findViewById(R.id.web);
        bar = (ProgressBar) findViewById(R.id.bar);

        //其他初始化
        initUUID();
        // webView初始化事件
        initMainWebView();
        // socket 初始化由js控制
        web.loadUrl(webUrl);
        // 这个将Java对象注入到webView，会允许JavaScript可以访问这个对象中的注解为@JavascriptInterface且是public的方法
        web.addJavascriptInterface(new DuofenJSBridge(WebViewActivity.this), "dfmb");
        web.removeJavascriptInterface("searchBoxJavaBridge_");
        if (webType == WEB_TYPE_PAY||webType ==WEB_TYPE_SERVER_PUSH) {
            initCameraViews();
        }else if (webType == WEB_TYPE_ORDER){
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: ");
        getCode(intent);
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
                case WEB_TYPE_SERVER_PUSH:
                    int orderId = this.getIntent().getIntExtra("orderId",0);
                    if (orderId!=0)
                    webUrl = HttpConfig.BASE_URL + PhoneUtils.getIMEI()+ "/" + orderId +  "/" + HttpConfig.PAYMENT_URL;
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
      //  disSocket();
    }

    /**
     * 关闭所有socket链接
     */
    private void disSocket() {
//        mSocket.disconnect();
//        mSocket.off("chatevent", socketEvent);
//        mSocket.off(Socket.EVENT_CONNECT, onConnect);
//        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
//        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
//        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
    }

    /**
     * 重启socket
     */
    public void reloadSocket() {
        Log.d(TAG, "reloadSocket: ");
        disSocket();
       // initSocket();
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
            if (webType == WEB_TYPE_PAY||webType ==WEB_TYPE_SERVER_PUSH) {
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

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                ToastUtil.getInstance().showToast(""+errorCode);
                web.loadUrl("file:///android_asset/retry.html");
                web.addJavascriptInterface(new DuofenJSBridge(WebViewActivity.this), "dfmb");

            }
        });

        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                bar.setVisibility(View.VISIBLE);
                Log.i(TAG,"newProgress="+newProgress);
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

    private void initCameraViews() {
        scanContainer=(RelativeLayout)findViewById(R.id.container);
        scanPreview = (FrameLayout) findViewById(R.id.capture_preview);
        scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);

        autoFocusHandler = new Handler();
        mCameraManager = new CameraManager(this);
        try {
            mCameraManager.openDriver();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Display display = this.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        RelativeLayout.LayoutParams linearParams =  (RelativeLayout.LayoutParams)scanCropView.getLayoutParams();
        linearParams.height = (int) (width*0.8);
        linearParams.width = (int) (width*0.8);
        scanCropView.setLayoutParams(linearParams);


        mCamera = mCameraManager.getCamera();
        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);

        scanPreview.addView(mPreview);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webType == WEB_TYPE_PAY||webType ==WEB_TYPE_SERVER_PUSH){
            releaseCamera();
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Size size = camera.getParameters().getPreviewSize();

            byte[] rotatedData = new byte[data.length];
            for (int y = 0; y < size.height; y++) {
                for (int x = 0; x < size.width; x++)
                    rotatedData[x * size.height + size.height - y - 1] = data[x + y * size.width];
            }

            int tmp = size.width;
            size.width = size.height;
            size.height = tmp;

            initCrop();
            ZBarDecoder zBarDecoder = new ZBarDecoder();
            String result = zBarDecoder.decodeCrop(rotatedData, size.width, size.height, mCropRect.left, mCropRect.top, mCropRect.width(), mCropRect.height());

            if (!TextUtils.isEmpty(result)) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                releaseCamera();
                barcodeScanned = true;
                Log.d(TAG, "getCode: " + result);
                webLoadJS("scanCallBack", result);

            }
        }
    };

    private void initCrop() {
        int cameraWidth = mCameraManager.getCameraResolution().y;
        int cameraHeight = mCameraManager.getCameraResolution().x;

        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        int x = cropLeft * cameraWidth / containerWidth;
        int y = cropTop * cameraHeight / containerHeight;

        int width = cropWidth * cameraWidth / containerWidth;
        int height = cropHeight * cameraHeight / containerHeight;

        mCropRect = new Rect(x, y, width + x, height + y);
    }
    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };
    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void reload() {
        Log.i(TAG,"getUrl="+webUrl);

        web.loadUrl(webUrl);
    }
}
