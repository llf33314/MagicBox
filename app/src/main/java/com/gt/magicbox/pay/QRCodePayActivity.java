package com.gt.magicbox.pay;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gt.magicbox.Constant;
import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.BaseConstant;
import com.gt.magicbox.bean.CreatedOrderBean;
import com.gt.magicbox.bean.MemberCardBean;
import com.gt.magicbox.bean.PayCodeResultBean;
import com.gt.magicbox.bean.QRCodeBitmapBean;
import com.gt.magicbox.bean.ScanCodePayResultBean;
import com.gt.magicbox.http.BaseResponse;
import com.gt.magicbox.http.HttpConfig;
import com.gt.magicbox.http.HttpRequestDialog;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.http.socket.SocketIOManager;
import com.gt.magicbox.member.MemberDoResultActivity;
import com.gt.magicbox.utils.commonutil.AppManager;
import com.gt.magicbox.utils.commonutil.ConvertUtils;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.gt.magicbox.utils.commonutil.PhoneUtils;
import com.gt.magicbox.utils.commonutil.ToastUtil;
import com.gt.magicbox.widget.HintDismissDialog;
import com.gt.magicbox.widget.LoadingProgressDialog;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.obsessive.zbar.CameraManager;
import com.obsessive.zbar.CameraPreview;
import com.orhanobut.hawk.Hawk;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.socket.emitter.Emitter;

/**
 * Description:
 * Created by jack-lin on 2017/9/8 0008.
 * Buddha bless, never BUG!
 */

public class QRCodePayActivity extends BaseActivity {
    public static final String TAG = QRCodePayActivity.class.getSimpleName();
    @BindView(R.id.qrCode)
    ImageView qrCode;
    @BindView(R.id.customer_money)
    TextView customerMoney;
    @BindView(R.id.cashier_money)
    TextView cashierMoney;
    @BindView(R.id.capture_preview)
    FrameLayout scanPreview;
    @BindView(R.id.reChosePay)
    Button reChosePay;
    private String url;
    private double money;
    private int type;
    public final static int TYPE_PAY = 0;
    public final static int TYPE_SERVER_PUSH = 1;
    public final static int TYPE_CREATED_PAY = 2;//已生成的订单并且未支付
    public final static int TYPE_MEMBER_RECHARGE =3;//会员充值使用支付宝或者微信支付

    private LoadingProgressDialog dialog;
    private SocketIOManager socketIOManager;

    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;
    private CameraManager mCameraManager;
    private boolean previewing = true;
    private ImageScanner mImageScanner = null;
    private Rect fillRect = null;
    private String orderNo = "";
    private int payMode;
    private Integer shiftId;
    private boolean isCodePayRequesting = false;
    private MemberCardBean memberCardBean;
    static {
        System.loadLibrary("iconv");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_pay);
        ButterKnife.bind(this);
        dialog = new LoadingProgressDialog(QRCodePayActivity.this);
        dialog.show();
        init();
        payResultSocket();
    }


    private void init() {
        if (this.getIntent() != null) {
            type = this.getIntent().getIntExtra("type", 0);
            LogUtils.d(TAG,"type="+type);

            switch (type) {
                case TYPE_MEMBER_RECHARGE:
                case TYPE_PAY:
                    money = this.getIntent().getDoubleExtra("money", 0);
                    payMode = this.getIntent().getIntExtra("payMode", 0);
                    if (type==TYPE_MEMBER_RECHARGE){
                        memberCardBean = (MemberCardBean) this.getIntent().getSerializableExtra("MemberCardBean");
                        LogUtils.d(TAG,"memberCardBean="+memberCardBean.ctName);

                    }
                    shiftId = Hawk.get("shiftId");
                    if (shiftId == null || shiftId < 0) shiftId = 0;
                    showMoney(cashierMoney, "" + money);
                    showMoney(customerMoney, "" + money);
                    getQRCodeURL(money, payMode, shiftId);
                    break;
                case TYPE_SERVER_PUSH:
                    int orderId = this.getIntent().getIntExtra("orderId", 0);
                    money = this.getIntent().getDoubleExtra("money", 0);
                    orderNo = this.getIntent().getStringExtra("orderNo");

                    shiftId = Hawk.get("shiftId");
                    if (shiftId == null || shiftId < 0) shiftId = 0;
                    showMoney(cashierMoney, "" + money);
                    showMoney(customerMoney, "" + money);
                    reChosePay.setVisibility(View.GONE);
                    getCreatedQRCodeURL(orderId,shiftId);
                    break;
                case TYPE_CREATED_PAY:
                    orderId = this.getIntent().getIntExtra("orderId", 0);
                    money = this.getIntent().getDoubleExtra("money", 0);
                    orderNo = this.getIntent().getStringExtra("orderNo");

                    shiftId = Hawk.get("shiftId");
                    if (shiftId == null || shiftId < 0) shiftId = 0;
                    showMoney(cashierMoney, "" + money);
                    showMoney(customerMoney, "" + money);
                    reChosePay.setVisibility(View.GONE);
                    getCreatedQRCodeURL(orderId,shiftId);
                    break;
            }
            LogUtils.i(TAG, "Url=" + url);
        }
    }


    private void getQRCodeURL(double money, int type, int shiftId) {
        HttpCall.getApiService()
                .getQRCodeUrl(PhoneUtils.getIMEI(), money, type, shiftId)
                .compose(ResultTransformer.<QRCodeBitmapBean>transformer())//线程处理 预处理
                .subscribe(new BaseObserver<QRCodeBitmapBean>() {
                    @Override
                    public void onSuccess(QRCodeBitmapBean data) {
                        LogUtils.i(TAG, "onSuccess");

                        if (data != null && !TextUtils.isEmpty(data.qrUrl)) {
                            LogUtils.i(TAG, "data qrUrl=" + data.qrUrl);
                            showQRCodeView(data.qrUrl);
                            orderNo = data.orderNo;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        AppManager.getInstance().finishActivity(QRCodePayActivity.class);
                        super.onError(e);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        dialog.dismiss();
                        AppManager.getInstance().finishActivity(QRCodePayActivity.class);
                        super.onFailure(code, msg);
                    }
                });
    }

    private void getCreatedQRCodeURL(int orderId, int shiftId) {
        HttpCall.getApiService()
                .getCreatedQRCodeUrl(orderId, shiftId)
                .compose(ResultTransformer.<CreatedOrderBean>transformer())//线程处理 预处理
                .subscribe(new BaseObserver<CreatedOrderBean>() {
                    @Override
                    public void onSuccess(CreatedOrderBean data) {
                        LogUtils.i(TAG, "onSuccess");

                        if (data != null && !TextUtils.isEmpty(data.qrUrl)) {
                            LogUtils.i(TAG, "data qrUrl=" + data.qrUrl);
                            showQRCodeView(data.qrUrl);
                            if (type!=TYPE_SERVER_PUSH)
                            orderNo = data.orderNo;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        AppManager.getInstance().finishActivity(QRCodePayActivity.class);
                        super.onError(e);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        dialog.dismiss();
                        AppManager.getInstance().finishActivity(QRCodePayActivity.class);
                        super.onFailure(code, msg);
                    }
                });
    }

    private void getCodePayResult(String qrCode, String orderNo) {
        if (!TextUtils.isEmpty(orderNo)) {
            isCodePayRequesting = true;
            HttpCall.getApiService()
                    .scanCodePay(qrCode, (Integer) Hawk.get("busId"), orderNo, shiftId, money)
                    .compose(ResultTransformer.<PayCodeResultBean>transformer())//线程处理 预处理
                    .subscribe(new BaseObserver<PayCodeResultBean>() {
                        @Override
                        public void onSuccess(PayCodeResultBean data) {
                            LogUtils.i(TAG, "onSuccess");
                            if (data != null && data.code == 1) {
                                if (!TextUtils.isEmpty(data.msg) && data.msg.startsWith(getString(R.string.please_use_wechat_code))) {
                                    showCodePayFailDialog(getString(R.string.please_use_wechat_code));
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtils.d(TAG, "onError=");

                            isCodePayRequesting = false;
                            super.onError(e);
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            LogUtils.d(TAG, "onFailure code=" + code);
                            isCodePayRequesting = false;
                            if (code == 1) {
                                HintDismissDialog dismissDialog = new HintDismissDialog(QRCodePayActivity.this, msg);
                                dismissDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        isCodePayRequesting = true;
                                    }
                                });
                                dismissDialog.show();
                            }
                            super.onFailure(code, msg);
                        }
                    });
        }
    }
    private void getCodeAliPayResult(String qrCode, String orderNo) {
        if (!TextUtils.isEmpty(orderNo)) {
            isCodePayRequesting = true;
            HttpCall.getApiService()
                    .scanCodeAliPay(qrCode, (Integer) Hawk.get("busId"), orderNo, shiftId, money)
                    .compose(ResultTransformer.<PayCodeResultBean>transformer())//线程处理 预处理
                    .subscribe(new BaseObserver<PayCodeResultBean>() {
                        @Override
                        public void onSuccess(PayCodeResultBean data) {
                            LogUtils.i(TAG, "onSuccess");
                            if (data != null && data.code == 1) {
                                isCodePayRequesting=true;

                            }else if(data != null && data.code == -1){
                                showCodePayFailDialog(data.msg);

                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            isCodePayRequesting = false;
                            super.onError(e);
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            if (code==1) {
                                showCodePayFailDialog(msg);
                            }
                            super.onFailure(code, msg);
                        }
                    });
        }
    }
    private void showCodePayFailDialog(String msg){
        HintDismissDialog dismissDialog = new HintDismissDialog(QRCodePayActivity.this, msg);
        dismissDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isCodePayRequesting = false;
            }
        });
        dismissDialog.show();
    }
    private void showQRCodeView(String url) {
        // TODO Auto-generated method stub
        BitmapUtils bitmapUtils = new BitmapUtils(this);
        // 加载网络图片
        bitmapUtils.display(qrCode,
                url, new BitmapLoadCallBack<ImageView>() {
                    @Override
                    public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                        imageView.setImageBitmap(bitmap);
                        initCameraViews();
                        dialog.dismiss();
                    }

                    @Override
                    public void onLoadFailed(ImageView imageView, String s, Drawable drawable) {
                        dialog.dismiss();
                    }
                });
    }

    private void showMoney(TextView textView, String numberString) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        if (numberString.length() != 0) {
            spannableString.append("¥ " + numberString);
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(ConvertUtils.dp2px(25));
            spannableString.setSpan(absoluteSizeSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        textView.setText(spannableString);

    }

    private void payResultSocket() {
        socketIOManager = new SocketIOManager(Constant.SOCKET_SERVER_URL);
        socketIOManager.setOnConnect(new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String UUID = PhoneUtils.getIMEI();
                LogUtils.d(SocketIOManager.TAG, "auth key : " + HttpConfig.SOCKET_ANDROID_AUTH_KEY + UUID);
                socketIOManager.getSocket().emit(HttpConfig.SOCKET_ANDROID_AUTH, HttpConfig.SOCKET_ANDROID_AUTH_KEY + UUID);
                LogUtils.d(SocketIOManager.TAG, "call: send android auth over");
            }
        });
        socketIOManager.setSocketEvent(new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                String retData = null;
                try {
                    retData = data.get("message").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                    retData = "";
                }
                String json = retData.replace("\\", "");
                if (!TextUtils.isEmpty(json) && json.startsWith("\"") && json.endsWith("\"")) {
                    LogUtils.d(SocketIOManager.TAG, "startsWith---------");

                    json = json.substring(1, json.length() - 1);
                }
                LogUtils.d(SocketIOManager.TAG, "retData=" + retData);
                LogUtils.d(SocketIOManager.TAG, "json=" + json);
                ScanCodePayResultBean scanCodePayResultBean = new Gson().fromJson(json, ScanCodePayResultBean.class);
                if (scanCodePayResultBean != null) {
                    boolean success = (!TextUtils.isEmpty(scanCodePayResultBean.status)
                            && scanCodePayResultBean.status.equals("success")) ? true : false;
                    payResult(success, "" + money);
                }
            }
        });
        socketIOManager.connectSocket();
    }

    public void payResult(boolean success, String message) {
        if (success) {
            if (type == TYPE_MEMBER_RECHARGE) {
               memberRecharge();
            } else {
                Intent intent = new Intent(getApplicationContext(), PayResultActivity.class);
                intent.putExtra("success", success);
                intent.putExtra("message", message);
                intent.putExtra("payType", (int) Hawk.get("payType"));
                intent.putExtra("orderNo", orderNo);
                if (type == TYPE_CREATED_PAY) {
                    intent.putExtra("fromType", PayResultActivity.TYPE_FROM_ORDER_LIST);
                }

                startActivity(intent);
            }
            AppManager.getInstance().finishActivity(QRCodePayActivity.class);
            AppManager.getInstance().finishActivity(PaymentActivity.class);
            AppManager.getInstance().finishActivity(ChosePayModeActivity.class);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.d(SocketIOManager.TAG, "disSocket");
        releaseCamera();
        socketIOManager.disSocket();
    }

    @OnClick(R.id.reChosePay)
    public void onViewClicked() {
        AppManager.getInstance().finishActivity(QRCodePayActivity.class);
    }


    private void initCameraViews() {
        mImageScanner = new ImageScanner();
        mImageScanner.setConfig(0, Config.X_DENSITY, 3);
        mImageScanner.setConfig(0, Config.Y_DENSITY, 3);

        autoFocusHandler = new Handler();
        mCameraManager = new CameraManager(this);
        try {
            mCameraManager.openDriver();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCameraManager.openFlashLight();
        mCamera = mCameraManager.getCamera();

        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        scanPreview.addView(mPreview);

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

            fillRect = new Rect(0, 0, size.width, size.height);
            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);
            barcode.setCrop(fillRect.left, fillRect.top, fillRect.width(),
                    fillRect.height());

            int result = mImageScanner.scanImage(barcode);
            String resultStr = null;

            if (result != 0) {
                SymbolSet syms = mImageScanner.getResults();
                for (Symbol sym : syms) {
                    resultStr = sym.getData();
                }
            }

            if (!TextUtils.isEmpty(resultStr)) {
                if (!isCodePayRequesting) {
                    if (payMode== BaseConstant.PAY_ON_WECHAT) {
                        getCodePayResult(resultStr, orderNo);
                    }else if (payMode==BaseConstant.PAY_ON_ALIPAY){
                        getCodeAliPayResult(resultStr, orderNo);
                    }
                }
            }
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    private void memberRecharge() {
        LogUtils.d(TAG,"memberRecharge type="+type);

        if (memberCardBean != null) {
            LogUtils.d(TAG,"memberRecharge");
            HttpCall.getApiService()
                    .memberRecharge(memberCardBean.memberId, money, payMode, (Integer) Hawk.get("shopId"))
                    .compose(ResultTransformer.<BaseResponse>transformerNoData())//线程处理 预处理
                    .subscribe(new BaseObserver<BaseResponse>() {
                        @Override
                        public void onSuccess(BaseResponse data) {
                            LogUtils.d(TAG, "memberRecharge onSuccess " );
                            Intent intent=new Intent(getApplicationContext(), MemberDoResultActivity.class);
                            intent.putExtra("rechargeMoney",money);
                            intent.putExtra("MemberCardBean",memberCardBean);
                            intent.putExtra("orderNo","123");
                            intent.putExtra("payType",payMode);
                            intent.putExtra("balance",memberCardBean.money+money);
                            startActivity(intent);
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            LogUtils.d(TAG, "memberRecharge onError e" + e.getMessage().toString());
                            super.onError(e);
                        }

                        @Override
                        public void onFailure(int code, String msg) {
                            LogUtils.d(TAG, "memberRecharge onFailure msg=" + msg);
                            super.onFailure(code, msg);
                        }
                    });
        }
    }

}
