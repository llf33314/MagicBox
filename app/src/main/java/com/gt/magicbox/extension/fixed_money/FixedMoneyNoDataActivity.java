package com.gt.magicbox.extension.fixed_money;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gt.magicbox.Constant;
import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.bean.FixedMoneyBean;
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
import com.gt.magicbox.utils.commonutil.AppManager;
import com.gt.magicbox.utils.commonutil.ConvertUtils;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.gt.magicbox.utils.commonutil.PhoneUtils;
import com.gt.magicbox.utils.qr_code_util.QrCodeUtils;
import com.gt.magicbox.widget.CustomGridLayoutManager;
import com.gt.magicbox.widget.HintDismissDialog;
import com.gt.magicbox.widget.LoadingProgressDialog;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.orhanobut.hawk.Hawk;
import com.synodata.scanview.view.CodePreview;
import com.synodata.scanview.view.Preview$IDecodeListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.socket.emitter.Emitter;

/**
 * Description:
 *
 * @author jack-lin
 * @date 2018/1/24 0024
 * Buddha bless, never BUG!
 */

public class FixedMoneyNoDataActivity extends BaseActivity implements Preview$IDecodeListener {
    private static final String TAG = FixedMoneyNoDataActivity.class.getSimpleName();
    @BindView(R.id.noDataLayout)
    RelativeLayout noDataLayout;
    @BindView(R.id.supportLayout)
    RelativeLayout supportLayout;
    @BindView(R.id.push_customer_money)
    TextView pushCustomerMoney;
    @BindView(R.id.tip)
    TextView tip;
    @BindView(R.id.push_qrCode)
    ImageView pushQrCode;
    @BindView(R.id.confirmOrder)
    Button confirmOrder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.dataLayout)
    RelativeLayout dataLayout;
    @BindView(R.id.preview)
    CodePreview preview;
    private FixedMoneyGridAdapter fixedMoneyGridAdapter;
    private int lastPosition;
    private int currentPosition = 0;

    private LoadingProgressDialog dialog;
    private SocketIOManager socketIOManager;
    private int payMode = 0;
    private CodeCameraManager codeCameraManager;
    private String orderNo = "";
    private long orderId;
    private long lastOrderId;

    private boolean isCodePayRequesting = false;
    private boolean isSuccessPay = false;
    private int shiftId;
    private Integer[] tipVoices = {R.raw.wechat_h, R.raw.ali_h, R.raw.success};
    private MediaPlayer mp = new MediaPlayer();

    static {
        System.loadLibrary("iconv");
    }

    private Bitmap qrCodeBitmap;
    private String qrCodeUrl;
    private long clickTime = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixed_money_no_data);
        ButterKnife.bind(this);
        goneToolBar();
    }

    private void initView() {
        CustomGridLayoutManager layoutManager = null;
        ArrayList<FixedMoneyBean> fixedMoneyBeans = Hawk.get("fixedMoneyList", new ArrayList<FixedMoneyBean>());
        if (fixedMoneyBeans.size() == 0) {
            noDataLayout.setVisibility(View.VISIBLE);
            dataLayout.setVisibility(View.GONE);
        } else {
            dataLayout.setVisibility(View.VISIBLE);
            switch (fixedMoneyBeans.size()) {
                case 1:
                case 2:
                    layoutManager = new CustomGridLayoutManager(FixedMoneyNoDataActivity.this, 1);
                    break;

                case 4:
                    layoutManager = new CustomGridLayoutManager(FixedMoneyNoDataActivity.this, 2);
                    break;
                case 3:
                case 5:
                case 6:
                    layoutManager = new CustomGridLayoutManager(FixedMoneyNoDataActivity.this, 3);
                    break;
            }
            layoutManager.setScrollEnabled(false);
            recyclerView.setLayoutManager(layoutManager);
            noDataLayout.setVisibility(View.GONE);
            fixedMoneyGridAdapter = new FixedMoneyGridAdapter(this, fixedMoneyBeans);
            fixedMoneyGridAdapter.setOnItemClickListener(new FixedMoneyGridAdapter.OnItemClickListener() {
                @Override
                public void OnItemClick(View view, FixedMoneyGridAdapter.ViewHolder viewHolder, int position) {
                    LogUtils.d("lastPosition=" + lastPosition + "  position=" + position);
                    currentPosition = position;
                    if (lastPosition != position) {
                        lastPosition = position;
                        initPay();
                    } else {
                        lastPosition = -1;
                    }
                }
            });
            fixedMoneyGridAdapter.setSelectedPosition(currentPosition);
            recyclerView.setAdapter(fixedMoneyGridAdapter);
            recyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (fixedMoneyGridAdapter != null) {
                        fixedMoneyGridAdapter.updateCurrentHolder();
                    }
                    return false;
                }
            });
            initPay();
        }
    }

    @OnClick({R.id.addFixedMoney, R.id.confirmOrder, R.id.setting, R.id.fix_toolbar_back})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.addFixedMoney:
                intent = new Intent(FixedMoneyNoDataActivity.this, FixedMoneySettingActivity.class);
                startActivity(intent);
                break;
            case R.id.confirmOrder:
                getOrderStatus();
                break;
            case R.id.setting:
                intent = new Intent(FixedMoneyNoDataActivity.this, FixedMoneySettingActivity.class);
                startActivity(intent);
                break;
            case R.id.fix_toolbar_back:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        initView();
        super.onResume();
    }

    private void initPay() {
        double money = getCurrentMoney();
        if (money == 0) {
            noDataLayout.setVisibility(View.VISIBLE);
        } else {
            dialog = new LoadingProgressDialog(FixedMoneyNoDataActivity.this);
            dialog.show();
            shiftId = Hawk.get("shiftId", 0);
            showMoney(pushCustomerMoney, "" + money);
            getQRCodeURL(money, 0, shiftId);
        }
    }

    private double getCurrentMoney() {
        ArrayList<FixedMoneyBean> fixedMoneyBeans = Hawk.get("fixedMoneyList", new ArrayList<FixedMoneyBean>());
        if (fixedMoneyBeans.size() == 0) {
            return 0;
        } else {
            return fixedMoneyBeans.get(currentPosition).money;
        }
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

    private void getQRCodeURL(double money, int type, int shiftId) {
        HttpCall.getApiService()
                .getQRCodeUrl(PhoneUtils.getIMEI(), money, type, shiftId, Hawk.get("shopId", 0), Hawk.get("busId", 0)
                        , Hawk.get("shopName", ""))
                .compose(ResultTransformer.<QRCodeBitmapBean>transformer())//线程处理 预处理
                .subscribe(new BaseObserver<QRCodeBitmapBean>() {
                    @Override
                    public void onSuccess(QRCodeBitmapBean data) {
                        LogUtils.i(TAG, "onSuccess");

                        if (data != null && !TextUtils.isEmpty(data.qrUrl)) {
                            showQRCodeView(data.qrUrl);
                            orderNo = data.orderNo;
                            orderId = data.orderId;
                            payResultSocket(orderNo);
                            LogUtils.i(TAG, "data qrUrl=" + data.qrUrl + "  orderId=" + orderId);

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        AppManager.getInstance().finishActivity(FixedMoneyNoDataActivity.class);
                        super.onError(e);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        dialog.dismiss();
                        AppManager.getInstance().finishActivity(FixedMoneyNoDataActivity.class);
                        super.onFailure(code, msg);
                    }
                });
    }

    private void showQRCodeView(String url) {
        // TODO Auto-generated method stub
        BitmapUtils bitmapUtils = new BitmapUtils(this);
        // 加载网络图片
        ImageView imageView;
        imageView = pushQrCode;

        bitmapUtils.display(imageView,
                url, new BitmapLoadCallBack<ImageView>() {
                    @Override
                    public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {
                        imageView.setImageBitmap(bitmap);
                        qrCodeBitmap = bitmap;
                        qrCodeUrl = QrCodeUtils.scanningImage(qrCodeBitmap);
                        LogUtils.d("qrCodeUrl=" + qrCodeUrl);
                        //initCameraViews();
                        codeCameraManager = new CodeCameraManager(getApplicationContext(), preview, FixedMoneyNoDataActivity.this);
                        codeCameraManager.initCamera();
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onLoadFailed(ImageView imageView, String s, Drawable drawable) {
                        dialog.dismiss();
                    }
                });
    }

    private void payResultSocket(final String orderNo) {
        socketIOManager = new SocketIOManager(Constant.SOCKET_SERVER_URL);
        socketIOManager.setOnConnect(new Emitter.Listener() {
            @Override
            public void call(Object... args) {
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
                    if (scanCodePayResultBean.payType != -1) {
                        payMode = scanCodePayResultBean.payType;
                    }
                    payResult(success, "" + getCurrentMoney());
                    isSuccessPay = true;
                }
            }
        });
        socketIOManager.connectSocket();
    }

    @Override
    protected void onStop() {
        if (codeCameraManager != null) {
            codeCameraManager.releaseCamera();
        }
        if (socketIOManager != null) {
            socketIOManager.disSocket();
        }
        super.onStop();
    }

    private void getOrderStatus() {
        final LoadingProgressDialog dialog = new LoadingProgressDialog(FixedMoneyNoDataActivity.this, "查询中...");
        dialog.show();
        HttpCall.getApiService()
                .getOrderStatus(orderNo, Hawk.get("shiftId", 0))
                .compose(ResultTransformer.<BaseResponse>transformerNoData())//线程处理 预处理
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        try {
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                            if (data != null && data.getCode() == 0) {
                                payResult(true, "" + getCurrentMoney());
                            }
                        } catch (Exception e) {

                        }
                        LogUtils.d(TAG, "getOrderStatus onSuccess ");


                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.d(TAG, "getOrderStatus onError e" + e.getMessage());
                        if (dialog != null) {
                            dialog.dismiss();
                        }

                        super.onError(e);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        //该接口因没有data,所以调到这个位置
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        if (code == 0) {
                            payResult(true, "" + getCurrentMoney());
                        } else if (code == 1) {
                            showDialogTip("该订单未支付");
                        } else {
                            showDialogTip("查询失败");
                        }
                        LogUtils.d(TAG, "getOrderStatus onFailure msg=" + msg);
                        super.onFailure(code, msg);
                    }
                });
    }

    private void showDialogTip(String tip) {
        HintDismissDialog dismissDialog = new HintDismissDialog(FixedMoneyNoDataActivity.this, tip);
        dismissDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (fixedMoneyGridAdapter != null) {
                    fixedMoneyGridAdapter.notifyDataSetChanged();
                }
            }
        });
        dismissDialog.show();
    }

    @Override
    public void onDecodeResult(boolean bDecoded, String result, String type) {
        if (bDecoded && !TextUtils.isEmpty(result)) {
            LogUtils.e("quck", "onDecodeResult" + type + "    " + result);
            if (!isCodePayRequesting) {
                Uri notification = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.beep);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
                if (result.startsWith("1")) {
                    getCodePayResult(result, orderNo);
                } else if (result.startsWith("2") || result.startsWith("3")) {
                    getCodeAliPayResult(result, orderNo);
                }
            }
        }
    }

    private void getCodePayResult(String qrCode, String orderNo) {
        if (!TextUtils.isEmpty(orderNo)) {
            isCodePayRequesting = true;
            HttpCall.getApiService()
                    .scanCodePay(qrCode, (Integer) Hawk.get("busId"), orderNo, shiftId, getCurrentMoney())
                    .compose(ResultTransformer.<PayCodeResultBean>transformer())//线程处理 预处理
                    .subscribe(new BaseObserver<PayCodeResultBean>() {
                        @Override
                        public void onSuccess(PayCodeResultBean data) {
                            LogUtils.i(TAG, "onSuccess");
                            if (data != null && data.code == 1) {
                                isCodePayRequesting = false;
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
                                HintDismissDialog dismissDialog = new HintDismissDialog(FixedMoneyNoDataActivity.this, msg);
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
                    .scanCodeAliPay(qrCode, (Integer) Hawk.get("busId"), orderNo, shiftId, getCurrentMoney())
                    .compose(ResultTransformer.<PayCodeResultBean>transformer())//线程处理 预处理
                    .subscribe(new BaseObserver<PayCodeResultBean>() {
                        @Override
                        public void onSuccess(PayCodeResultBean data) {
                            LogUtils.i(TAG, "onSuccess");
                            isCodePayRequesting = false;

                            if (data != null && data.code == 1) {
                                isCodePayRequesting = false;

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
        HintDismissDialog dismissDialog = new HintDismissDialog(FixedMoneyNoDataActivity.this, msg);
        dismissDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isCodePayRequesting = false;
            }
        });
        dismissDialog.show();
    }

    public void payResult(boolean success, String message) {
        if (orderId == lastOrderId) {
            return;
        }
        if (success) {
            playSound();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initPay();
                }
            });
        }
        lastOrderId = orderId;
    }

    private void playSound() {
        try {
            Integer res;
            if (payMode < tipVoices.length) {
                res = tipVoices[payMode];
            } else {
                res = tipVoices[tipVoices.length - 1];

            }
            mp = MediaPlayer.create(getApplicationContext(), res);
            if (mp != null) {
                mp.start();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();

            }
        });
    }
}
