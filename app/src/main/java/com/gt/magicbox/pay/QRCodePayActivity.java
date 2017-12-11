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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gt.magicbox.Constant;
import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.BaseConstant;
import com.gt.magicbox.bean.CreatedOrderBean;
import com.gt.magicbox.bean.MemberCardBean;
import com.gt.magicbox.bean.MemberCouponBean;
import com.gt.magicbox.bean.PayCodeResultBean;
import com.gt.magicbox.bean.QRCodeBitmapBean;
import com.gt.magicbox.bean.ScanCodePayResultBean;
import com.gt.magicbox.camera.CodeCameraManager;
import com.gt.magicbox.http.BaseResponse;
import com.gt.magicbox.http.HttpConfig;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.http.socket.SocketIOManager;
import com.gt.magicbox.member.MemberDoResultActivity;
import com.gt.magicbox.setting.printersetting.PrinterConnectService;
import com.gt.magicbox.utils.commonutil.AppManager;
import com.gt.magicbox.utils.commonutil.ConvertUtils;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.gt.magicbox.utils.commonutil.PhoneUtils;
import com.gt.magicbox.utils.commonutil.TimeUtils;
import com.gt.magicbox.utils.qr_code_util.QrCodeUtils;
import com.gt.magicbox.widget.HintDismissDialog;
import com.gt.magicbox.widget.LoadingProgressDialog;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.obsessive.zbar.CameraManager;
import com.obsessive.zbar.CameraPreview;
import com.orhanobut.hawk.Hawk;
import com.synodata.scanview.view.CodePreview;
import com.synodata.scanview.view.Preview$IDecodeListener;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.socket.emitter.Emitter;

/**
 * Description:
 * Created by jack-lin on 2017/9/8 0008.
 * Buddha bless, never BUG!
 */

public class QRCodePayActivity extends BaseActivity implements Preview$IDecodeListener {
    public static final String TAG = QRCodePayActivity.class.getSimpleName();
    @BindView(R.id.qrCode)
    ImageView qrCode;
    @BindView(R.id.customer_money)
    TextView customerMoney;
    @BindView(R.id.cashier_money)
    TextView cashierMoney;
    @BindView(R.id.reChosePay)
    Button reChosePay;
    @BindView(R.id.wechatPay)
    ImageView wechatPay;
    @BindView(R.id.aliPay)
    ImageView aliPay;
    @BindView(R.id.normalPayLayout)
    LinearLayout normalPayLayout;
    @BindView(R.id.qrCodeBg)
    ImageView qrCodeBg;
    @BindView(R.id.push_customer_money)
    TextView pushCustomerMoney;
    @BindView(R.id.push_qrCode)
    ImageView pushQrCode;
    @BindView(R.id.push_cashier_money)
    TextView pushCashierMoney;
    @BindView(R.id.pushLayout)
    RelativeLayout pushLayout;
    @BindView(R.id.preview)
    CodePreview preview;
    private String url;
    private double money;
    private int type;
    public final static int TYPE_PAY = 0;
    public final static int TYPE_SERVER_PUSH = 1;
    public final static int TYPE_CREATED_PAY = 2;//已生成的订单并且未支付
    public final static int TYPE_MEMBER_RECHARGE = 3;//会员充值使用支付宝或者微信支付
    public final static int TYPE_CUSTOMER_DISPLAY_PAY = 4;//客显
    private static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());


    private static final int MSG_RESULT = 1;
    private static final int MSG_SCENE = 4;
    private static final int BUZZER_ON = 166661;
    private static final int BUZZER_OFF = 2266666;

    private LoadingProgressDialog dialog;
    private SocketIOManager socketIOManager;

    private CodeCameraManager codeCameraManager;
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
    private MemberCouponBean memberCouponBean;

    static {
        System.loadLibrary("iconv");
    }

    private Bitmap qrCodeBitmap;
    private String qrCodeUrl;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_pay);
        dialog = new LoadingProgressDialog(QRCodePayActivity.this);
        dialog.show();
        init();
    }


    private void init() {
        if (this.getIntent() != null) {
            type = this.getIntent().getIntExtra("type", 0);
            LogUtils.d(TAG, "type=" + type);

            switch (type) {
                case TYPE_CUSTOMER_DISPLAY_PAY:
                case TYPE_MEMBER_RECHARGE:
                case TYPE_PAY:
                    money = this.getIntent().getDoubleExtra("money", 0);
                    payMode = this.getIntent().getIntExtra("payMode", 0);
                    memberCouponBean= (MemberCouponBean) this.getIntent().getSerializableExtra("memberCouponBean");
                    if (type == TYPE_MEMBER_RECHARGE) {
                        memberCardBean = (MemberCardBean) this.getIntent().getSerializableExtra("MemberCardBean");
                        LogUtils.d(TAG, "memberCardBean=" + memberCardBean.ctName);

                    }
                    shiftId = Hawk.get("shiftId");
                    if (shiftId == null || shiftId < 0) shiftId = 0;

                    if (type == TYPE_CUSTOMER_DISPLAY_PAY
                            || type == TYPE_SERVER_PUSH) {
                        pushLayout.setVisibility(View.VISIBLE);
                        normalPayLayout.setVisibility(View.GONE);
                        showMoney(pushCashierMoney, "" + money);
                        showMoney(pushCustomerMoney, "" + money);
                    } else {
                        showMoney(cashierMoney, "" + money);
                        showMoney(customerMoney, "" + money);
                    }
                    getQRCodeURL(money, payMode, shiftId);
                    break;
                case TYPE_SERVER_PUSH:
                    int orderId = this.getIntent().getIntExtra("orderId", 0);
                    money = this.getIntent().getDoubleExtra("money", 0);
                    orderNo = this.getIntent().getStringExtra("orderNo");
                    payResultSocket(orderNo);
                    shiftId = Hawk.get("shiftId");
                    if (shiftId == null || shiftId < 0) shiftId = 0;
                    showMoney(cashierMoney, "" + money);
                    showMoney(customerMoney, "" + money);
                    reChosePay.setVisibility(View.GONE);
                    getCreatedQRCodeURL(orderId, shiftId);
                    break;
                case TYPE_CREATED_PAY:
                    orderId = this.getIntent().getIntExtra("orderId", 0);
                    money = this.getIntent().getDoubleExtra("money", 0);
                    orderNo = this.getIntent().getStringExtra("orderNo");

                    shiftId = Hawk.get("shiftId");
                    if (shiftId == null || shiftId < 0) shiftId = 0;
                    pushLayout.setVisibility(View.VISIBLE);
                    normalPayLayout.setVisibility(View.GONE);
                    showMoney(pushCashierMoney, "" + money);
                    showMoney(pushCustomerMoney, "" + money);
                    getCreatedQRCodeURL(orderId, shiftId);
                    break;
            }
            LogUtils.i(TAG, "Url=" + url);
        }
    }


    private void getQRCodeURL(double money, int type, int shiftId) {
        HttpCall.getApiService()
                .getQRCodeUrl(PhoneUtils.getIMEI(), money, type, shiftId,Hawk.get("shopId",0),Hawk.get("busId",0)
                ,Hawk.get("shopName",""))
                .compose(ResultTransformer.<QRCodeBitmapBean>transformer())//线程处理 预处理
                .subscribe(new BaseObserver<QRCodeBitmapBean>() {
                    @Override
                    public void onSuccess(QRCodeBitmapBean data) {
                        LogUtils.i(TAG, "onSuccess");

                        if (data != null && !TextUtils.isEmpty(data.qrUrl)) {
                            LogUtils.i(TAG, "data qrUrl=" + data.qrUrl);
                            showQRCodeView(data.qrUrl);
                            orderNo = data.orderNo;
                            payResultSocket(orderNo);
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
                .getCreatedQRCodeUrl(orderId, shiftId,Hawk.get("busId",0))
                .compose(ResultTransformer.<CreatedOrderBean>transformer())//线程处理 预处理
                .subscribe(new BaseObserver<CreatedOrderBean>() {
                    @Override
                    public void onSuccess(CreatedOrderBean data) {
                        LogUtils.i(TAG, "onSuccess");

                        if (data != null && !TextUtils.isEmpty(data.qrUrl)) {
                            LogUtils.i(TAG, "data qrUrl=" + data.qrUrl);
                            showQRCodeView(data.qrUrl);
                            if (type != TYPE_SERVER_PUSH) {
                                orderNo = data.orderNo;
                                payResultSocket(orderNo);
                            }

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
                                        isCodePayRequesting = false;
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
                                isCodePayRequesting = true;

                            } else if (data != null && data.code == -1) {
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
                            if (code == 1) {
                                showCodePayFailDialog(msg);
                            }
                            super.onFailure(code, msg);
                        }
                    });
        }
    }

    private void showCodePayFailDialog(String msg) {
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
        ImageView imageView;
        if (type == TYPE_CUSTOMER_DISPLAY_PAY
                || type == TYPE_CREATED_PAY
                || type == TYPE_SERVER_PUSH) {
            imageView = pushQrCode;
        } else {
            imageView = qrCode;
        }
        bitmapUtils.display(imageView,
                url, new BitmapLoadCallBack<ImageView>() {
                    @Override
                    public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                        imageView.setImageBitmap(bitmap);
                        qrCodeBitmap=bitmap;
                        //qrCodeUrl= QrCodeUtils.scanningImage(qrCodeBitmap);
                        // LogUtils.d("qrCodeUrl="+qrCodeUrl);
                        //initCameraViews();
                        codeCameraManager =new CodeCameraManager(getApplicationContext(),preview,QRCodePayActivity.this);
                        codeCameraManager.initCamera();
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

    private void payResultSocket(final String orderNo) {
        socketIOManager = new SocketIOManager(Constant.SOCKET_SERVER_URL);
        socketIOManager.setOnConnect(new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                //String UUID = PhoneUtils.getIMEI();
                String UUID = orderNo;
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
                intent.putExtra("payType", payMode);
                intent.putExtra("orderNo", orderNo);
                if (type == TYPE_CREATED_PAY) {
                    intent.putExtra("fromType", PayResultActivity.TYPE_FROM_ORDER_LIST);
                }
                if (memberCouponBean!=null){
                    intent.putExtra("memberCouponBean", memberCouponBean);
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
        if (codeCameraManager!=null) {
            codeCameraManager.releaseCamera();
        }
        if (socketIOManager!=null) {
            socketIOManager.disSocket();
        }
    }

    @Override
    protected void onPause() {
        if (codeCameraManager!=null) {
            codeCameraManager.releaseCamera();
        }
        super.onPause();
    }

    @OnClick(R.id.reChosePay)
    public void onViewClicked() {
        AppManager.getInstance().finishActivity(QRCodePayActivity.class);
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
                    if (payMode == BaseConstant.PAY_ON_WECHAT) {
                        getCodePayResult(resultStr, orderNo);
                    } else if (payMode == BaseConstant.PAY_ON_ALIPAY) {
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
        LogUtils.d(TAG, "memberRecharge type=" + type);

        if (memberCardBean != null) {
            LogUtils.d(TAG, "memberRecharge");
            HttpCall.getApiService()
                    .memberRecharge(memberCardBean.memberId, money, payMode, (Integer) Hawk.get("shopId"))
                    .compose(ResultTransformer.<BaseResponse>transformerNoData())//线程处理 预处理
                    .subscribe(new BaseObserver<BaseResponse>() {
                        @Override
                        public void onSuccess(BaseResponse data) {
                            LogUtils.d(TAG, "memberRecharge onSuccess ");
                            Intent intent = new Intent(getApplicationContext(), MemberDoResultActivity.class);
                            intent.putExtra("rechargeMoney", money);
                            intent.putExtra("MemberCardBean", memberCardBean);
                            intent.putExtra("orderNo", "123");
                            intent.putExtra("payType", payMode);
                            intent.putExtra("balance", memberCardBean.money + money);
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

    @OnClick({R.id.wechatPay, R.id.aliPay,R.id.printQrCode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.wechatPay:
                wechatPay.setImageResource(R.drawable.wechat_selected);
                aliPay.setImageResource(R.drawable.alipay_unselected);
                payMode = 0;
                break;
            case R.id.aliPay:
                wechatPay.setImageResource(R.drawable.wechat_unselected);
                aliPay.setImageResource(R.drawable.alipay_selected);
                payMode = 1;
                break;
            case R.id.printQrCode:

                //PrinterConnectService.printQrCode(orderNo, ""+money, TimeUtils.millis2String(System.currentTimeMillis(), DEFAULT_FORMAT), qrCodeBitmap);
                break;
        }
    }

    @Override
    public void onDecodeResult(boolean bDecoded,String result, String type) {
        if (bDecoded&&!TextUtils.isEmpty(result)){
            LogUtils.e("quck","onDecodeResult"+type+"    "+result);
                if (!isCodePayRequesting) {
                    if (payMode == BaseConstant.PAY_ON_WECHAT) {
                        getCodePayResult(result, orderNo);
                    } else if (payMode == BaseConstant.PAY_ON_ALIPAY) {
                        getCodeAliPayResult(result, orderNo);
                    }
                }
        }
    }

}
