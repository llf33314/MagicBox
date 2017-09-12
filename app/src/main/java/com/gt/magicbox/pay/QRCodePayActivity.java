package com.gt.magicbox.pay;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.bean.QRCodeBean;
import com.gt.magicbox.http.HttpConfig;
import com.gt.magicbox.http.HttpRequestDialog;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.http.socket.SocketIOManager;
import com.gt.magicbox.utils.commonutil.AppManager;
import com.gt.magicbox.utils.commonutil.ConvertUtils;
import com.gt.magicbox.utils.commonutil.PhoneUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.orhanobut.hawk.Hawk;

import org.json.JSONException;
import org.json.JSONObject;

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
    private String url;
    private double money;
    private int type;
    public final static int TYPE_PAY = 0;
    public final static int TYPE_SERVER_PUSH = 1;
    private HttpRequestDialog dialog;
    private SocketIOManager socketIOManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_pay);
        ButterKnife.bind(this);
        dialog = new HttpRequestDialog();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    AppManager.getInstance().finishActivity(QRCodePayActivity.class);
                    return false;

                }
                return true;
            }
        });
        dialog.show();
        combineURL();
        payResultSocket();
    }


    private void combineURL() {
        if (this.getIntent() != null) {
            type = this.getIntent().getIntExtra("type", 0);
            switch (type) {
                case TYPE_PAY:
                    money = this.getIntent().getDoubleExtra("money", 0);
                    int type = this.getIntent().getIntExtra("payMode", 0);
                    Integer shiftId = Hawk.get("shiftId");
                    if (shiftId == null || shiftId < 0) shiftId = 0;
                    showMoney(cashierMoney, "" + money);
                    showMoney(customerMoney, "" + money);
                    getQRCodeURL(money, type, shiftId);
                    break;
                case TYPE_SERVER_PUSH:
                    int orderId = this.getIntent().getIntExtra("orderId", 0);
                    if (orderId != 0)
                        url = HttpConfig.BASE_URL + PhoneUtils.getIMEI() + "/" + orderId + "/" + HttpConfig.PAYMENT_URL;
                    break;
            }
            Log.i(TAG, "Url=" + url);
        }
    }


    private void getQRCodeURL(double money, int type, int shiftId) {
        HttpCall.getApiService()
                .getQRCodeUrl(PhoneUtils.getIMEI(), money, type, shiftId)
                .compose(ResultTransformer.<QRCodeBean>transformer())//线程处理 预处理
                .subscribe(new BaseObserver<QRCodeBean>() {
                    @Override
                    public void onSuccess(QRCodeBean data) {
                        Log.i(TAG, "onSuccess");

                        if (data != null && !TextUtils.isEmpty(data.qrUrl)) {
                            Log.i(TAG, "data qrUrl=" + data.qrUrl);
                            showQRCodeView(data.qrUrl);
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

    private void showQRCodeView(String url) {
        // TODO Auto-generated method stub
        BitmapUtils bitmapUtils = new BitmapUtils(this);
        // 加载网络图片
        bitmapUtils.display(qrCode,
                url, new BitmapLoadCallBack<ImageView>() {
                    @Override
                    public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                        imageView.setImageBitmap(bitmap);
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
        socketIOManager = new SocketIOManager(HttpConfig.SOCKET_SERVER_URL);
        socketIOManager.setOnConnect(new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String UUID = PhoneUtils.getIMEI();
                Log.d(SocketIOManager.TAG, "auth key : " + HttpConfig.SOCKET_ANDROID_AUTH_KEY + UUID);
                socketIOManager.getSocket().emit(HttpConfig.SOCKET_ANDROID_AUTH, HttpConfig.SOCKET_ANDROID_AUTH_KEY + UUID);
                Log.d(SocketIOManager.TAG, "call: send android auth over");
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
                Log.d(SocketIOManager.TAG, "retData=" + retData);
                // OrderBean orderBean= new Gson().fromJson(retData,OrderBean.class);
            }
        });
        socketIOManager.connectSocket();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(SocketIOManager.TAG, "disSocket" );

        socketIOManager.disSocket();
    }

    @OnClick(R.id.reChosePay)
    public void onViewClicked() {
        AppManager.getInstance().finishActivity(QRCodePayActivity.class);
    }
}
