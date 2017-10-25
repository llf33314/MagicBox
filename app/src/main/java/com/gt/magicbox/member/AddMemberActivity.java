package com.gt.magicbox.member;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gt.magicbox.Constant;
import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.bean.CardGradeInfoBean;
import com.gt.magicbox.bean.CardTypeInfoBean;
import com.gt.magicbox.bean.FollowSocketBean;
import com.gt.magicbox.bean.MemberCardBean;
import com.gt.magicbox.http.BaseResponse;
import com.gt.magicbox.http.HttpConfig;
import com.gt.magicbox.http.HttpRequestDialog;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.DialogTransformer;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.http.socket.SocketIOManager;
import com.gt.magicbox.main.MoreFunctionDialog;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.gt.magicbox.utils.commonutil.PhoneUtils;
import com.gt.magicbox.utils.commonutil.ToastUtil;
import com.gt.magicbox.widget.HintDismissDialog;
import com.gt.magicbox.widget.LoadingProgressDialog;
import com.gt.magicbox.widget.WheelDialog;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.orhanobut.hawk.Hawk;
import com.suke.widget.SwitchButton;
import com.wx.wheelview.widget.WheelView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.socket.emitter.Emitter;

/**
 * Description:
 * Created by jack-lin on 2017/9/19 0019.
 * Buddha bless, never BUG!
 */

public class AddMemberActivity extends BaseActivity {
    private static final String TAG = AddMemberActivity.class.getSimpleName();
    @BindView(R.id.textFollow)
    TextView textFollow;
    @BindView(R.id.switch_button)
    SwitchButton switchButton;
    @BindView(R.id.imgQRCode)
    ImageView imgQRCode;
    @BindView(R.id.textTip)
    TextView textTip;
    @BindView(R.id.followLayout)
    RelativeLayout followLayout;
    private LoadingProgressDialog dialog;
    ArrayList<String> listCardName = new ArrayList<String>();
    @BindView(R.id.memberCardType)
    TextView memberCardType;
    @BindView(R.id.phoneEditText)
    EditText phoneEditText;
    @BindView(R.id.identifyingEditText)
    EditText identifyingEditText;
    @BindView(R.id.memberTypeLayout)
    RelativeLayout memberTypeLayout;
    @BindView(R.id.get_identifying_code)
    TextView getIdentifyingCode;
    private int selectPosition;
    private int gt_id;
    private int ct_id;
    private String phone = "";
    private CountDownTimer timer;
    private static final int MSG_UPDATE_COUNT_TIME = 0;
    private static final int MSG_UPDATE_SEND_CODE_ENABLE = 1;
    private static final int MSG_SHOW_FOLLOW_SUCCESS = 2;

    private boolean canSendCode = true;
    private String smsCode = "";
    private boolean isMemberCardInit = false;
    private boolean isQRCodeInit = false;
    private SocketIOManager socketIOManager;
    private boolean isPhoneRegistered;
    private MoreFunctionDialog mMoreFunctionDialog;
    private int bit = 1;
    private int memberId;
    private CardTypeInfoBean cardTypeInfoBean;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_COUNT_TIME:
                    getIdentifyingCode.setText(msg.arg1 + "秒后可重发");
                    getIdentifyingCode.setTextColor(0xffcccccc);
                    getIdentifyingCode.setEnabled(false);
                    break;
                case MSG_UPDATE_SEND_CODE_ENABLE:
                    getIdentifyingCode.setText("获取验证码");
                    getIdentifyingCode.setTextColor(0xfff04a4a);

                    getIdentifyingCode.setEnabled(true);
                    break;
                case MSG_SHOW_FOLLOW_SUCCESS:
                    new HintDismissDialog(AddMemberActivity.this,"粉丝关注成功").show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_add);
        ButterKnife.bind(this);
        initData();
        getWeChatSubscriptionQRCode();
        initView();
    }

    private void initData() {
        getMemberCardType();
        followSocket();
    }

    private void getMemberCardType() {
        HttpCall.getApiService()
                .findMemberCardType( Hawk.get("busId",0))
                .compose(ResultTransformer.<CardTypeInfoBean>transformer())//线程处理 预处理
                .subscribe(new BaseObserver<CardTypeInfoBean>() {

                    @Override
                    protected void onSuccess(CardTypeInfoBean bean) {
                        isMemberCardInit = true;
                        if (isMemberCardInit && isQRCodeInit)
                            dialog.dismiss();
                        if (bean != null) {
                            createArrays(bean);
                            cardTypeInfoBean = bean;
                        }

                        LogUtils.d(TAG, "onSuccess");
                    }

                    @Override
                    protected void onFailure(int code, String msg) {
                        super.onFailure(code, msg);
                        dialog.dismiss();
                        LogUtils.d(TAG, "onFailure");

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dialog.dismiss();
                        LogUtils.d(TAG, "onError");


                    }
                });
    }

    private void senSMS(String content, String mobiles) {
        HttpCall.getApiService()
                .sendSMS(Hawk.get("busId",0),
                        content, mobiles)
                .compose(ResultTransformer.<BaseResponse>transformerNoData())//线程处理 预处理
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        LogUtils.d(TAG, "senSMS onSuccess");

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.d(TAG, "senSMS onError");
                        super.onError(e);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        LogUtils.d(TAG, "senSMS onFailure");

                        super.onFailure(code, msg);
                    }
                });
    }

    private void getWeChatSubscriptionQRCode() {
        HttpCall.getApiService()
                .getWeChatSubscriptionQRCode( Hawk.get("busId",0),
                      Hawk.get("eqId",0))
                .compose(ResultTransformer.<BaseResponse>transformerNoData())//线程处理 预处理
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        LogUtils.d(TAG, "getWeChatSubscriptionQRCode onSuccess data=" + data.getData().toString());
                        if (data != null && !TextUtils.isEmpty(data.getData().toString())) {
                            showQRCodeView(data.getData().toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.d(TAG, "getWeChatSubscriptionQRCode onError");
                        super.onError(e);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        LogUtils.d(TAG, "getWeChatSubscriptionQRCode onFailure");

                        super.onFailure(code, msg);
                    }
                });
    }

    private void receiveMemberCard(int bit, int ctId, int gtId, int memberId, String phone) {
        HttpCall.getApiService()
                .receiveMemberCard(bit,  Hawk.get("busId",0),
                        ctId, gtId, memberId, phone,
                        Hawk.get("shopId",0))
                .compose(ResultTransformer.<BaseResponse>transformerNoData())//线程处理 预处理
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        LogUtils.d(TAG, "receiveMemberCard onSuccess ");
                        showResultDialog(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.d(TAG, "receiveMemberCard onError");
                        showResultDialog(false);

                        super.onError(e);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        LogUtils.d(TAG, "receiveMemberCard onFailure");
                        showResultDialog(false);

                    }
                });
    }

    private void receiveMemberCardWithoutMemberId(int bit, int ctId, int gtId, String phone) {
        HttpCall.getApiService()
                .receiveMemberCardWithoutMemberId(bit, Hawk.get("busId",0),
                        ctId, gtId, phone,
                        Hawk.get("shopId",0))
                .compose(ResultTransformer.<BaseResponse>transformerNoData())//线程处理 预处理
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        showResultDialog(true);

                        LogUtils.d(TAG, "receiveMemberCard onSuccess ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.d(TAG, "receiveMemberCard onError");
                        showResultDialog(false);

                        super.onError(e);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        LogUtils.d(TAG, "receiveMemberCard onFailure");
                        showResultDialog(false);

                        super.onFailure(code, msg);
                    }
                });
    }

    @Override
    protected void onStop() {
        socketIOManager.disSocket();
        super.onStop();
    }

    private void getMemberGradeType(int ctId) {
        HttpCall.getApiService()
                .findMemberGradeType(Hawk.get("busId",0), ctId)
                .compose(ResultTransformer.<CardGradeInfoBean>transformer())//线程处理 预处理
                .subscribe(new BaseObserver<CardGradeInfoBean>() {

                    @Override
                    protected void onSuccess(CardGradeInfoBean bean) {

                        if (bean != null && bean.gradeType != null && bean.gradeType.size() > 0) {
                            gt_id = bean.gradeType.get(0).gt_id;
                            LogUtils.d(TAG, "getMemberGradeType onSuccess bean.gt_id=");

                        }
                    }

                    @Override
                    protected void onFailure(int code, String msg) {
                        super.onFailure(code, msg);
                        LogUtils.d(TAG, "getMemberGradeType onFailure");

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        LogUtils.d(TAG, "getMemberGradeType onError e=" + e.getMessage());


                    }
                });
    }

    private void findMemberCardByPhone(final String phone) {
        HttpCall.getApiService()
                .findMemberCardByPhone(Hawk.get("busId",0), phone)
                .compose(ResultTransformer.<MemberCardBean>transformer())//线程处理 预处理
                .compose(new DialogTransformer().<MemberCardBean>transformer())
                .subscribe(new BaseObserver<MemberCardBean>() {

                    @Override
                    protected void onSuccess(MemberCardBean bean) {

                        LogUtils.d(TAG, "findMemberCardByPhone onSuccess");
                        mMoreFunctionDialog = new MoreFunctionDialog(AddMemberActivity.this, "该手机已领取过会员卡", R.style.HttpRequestDialogStyle);
                        mMoreFunctionDialog.show();
                    }

                    @Override
                    protected void onFailure(int code, String msg) {
                        super.onFailure(code, msg);
                        LogUtils.d(TAG, "findMemberCardByPhone onFailure msg=" + msg.toString());
                        if (!TextUtils.isEmpty(msg) && msg.equals("数据不存在")) {
                            if (bit == 0) {
                                if (memberId > 0) {
                                    receiveMemberCard(bit, ct_id, gt_id, memberId, phone);
                                } else ToastUtil.getInstance().showToast("请先关注微信号");
                            } else if (bit == 1) {
                                receiveMemberCardWithoutMemberId(bit, ct_id, gt_id, phone);
                            }
                        }

                    }

                });
    }

    private void initView() {
        dialog = new LoadingProgressDialog(AddMemberActivity.this );
        dialog.show();
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    bit = 0;
                    imgQRCode.setVisibility(View.VISIBLE);
                    textTip.setVisibility(View.VISIBLE);
                } else {
                    bit = 1;
                    imgQRCode.setVisibility(View.GONE);
                    textTip.setVisibility(View.GONE);
                }
            }
        });
    }


    public void showDialog() {
        final WheelDialog wheelDialog = new WheelDialog(this, R.style.ShortcutMenuDialog);
        wheelDialog.setList(listCardName);
        wheelDialog.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                selectPosition = position;
            }
        });
        wheelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.cancel:
                        wheelDialog.dismiss();
                        break;
                    case R.id.confirm:
                        wheelDialog.dismiss();
                        if (selectPosition != -1 && selectPosition < listCardName.size()) {
                            memberCardType.setText(listCardName.get(selectPosition));
                            if (cardTypeInfoBean != null && cardTypeInfoBean.getCardType().size() > 0) {
                                ct_id = cardTypeInfoBean.getCardType().get(selectPosition).getCtId();
                                getMemberGradeType(ct_id);
                            }
                        }
                        break;
                }
            }
        });
        wheelDialog.show();
    }

    private ArrayList<String> createArrays(CardTypeInfoBean cardTypeInfoBean) {
        listCardName.clear();
        if (cardTypeInfoBean != null && cardTypeInfoBean.getCardType() != null) {
            for (int i = 0; i < cardTypeInfoBean.getCardType().size(); i++) {
                if (!TextUtils.isEmpty(cardTypeInfoBean.getCardType().get(i).getCtName()))
                    listCardName.add(cardTypeInfoBean.getCardType().get(i).getCtName());

            }
        }
        return listCardName;
    }

    private String createIdentifyingCode() {
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int num = random.nextInt(10);
            stringBuffer.append("" + num);
        }

        return stringBuffer.toString();
    }

    @OnClick({R.id.memberTypeLayout, R.id.get_identifying_code, R.id.confirmButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.memberTypeLayout:
                showDialog();
                break;
            case R.id.get_identifying_code:
                phone = phoneEditText.getEditableText().toString();
                if (!TextUtils.isEmpty(phone)) {
                    if (phone.length() == 11) {
                        if (canSendCode) {
                            startCountDownTime(60);
                            smsCode = createIdentifyingCode();
                            senSMS("您正在办理会员业务，验证码:" + smsCode, phone);
                        }
                    } else ToastUtil.getInstance().showToast("请输入正确位数的手机号");
                }
                break;
            case R.id.confirmButton:
                String userInputCode = identifyingEditText.getEditableText().toString();
                phone = phoneEditText.getEditableText().toString();
                if (ct_id <= 0) {
                    ToastUtil.getInstance().showToast("请先选择会员卡类型");
                    break;
                }
                if (!TextUtils.isEmpty(userInputCode)) {
                    if (userInputCode.equals(smsCode)) {
                        findMemberCardByPhone(phone);
                    } else ToastUtil.getInstance().showToast("验证码错误");
                } else ToastUtil.getInstance().showToast("验证码不能为空");
                break;
        }
    }

    private void showQRCodeView(String url) {
        // TODO Auto-generated method stub
        BitmapUtils bitmapUtils = new BitmapUtils(this);
        // 加载网络图片
        bitmapUtils.display(imgQRCode,
                url, new BitmapLoadCallBack<ImageView>() {
                    @Override
                    public void onLoadCompleted(ImageView imageView, String s, Bitmap bitmap, BitmapDisplayConfig bitmapDisplayConfig, BitmapLoadFrom bitmapLoadFrom) {

                        imageView.setImageBitmap(bitmap);
                        isQRCodeInit = true;
                        if (isMemberCardInit && isQRCodeInit)
                            dialog.dismiss();
                    }

                    @Override
                    public void onLoadFailed(ImageView imageView, String s, Drawable drawable) {
                        dialog.dismiss();
                    }
                });
    }

    private void startCountDownTime(long time) {
        /**
         * 最简单的倒计时类，实现了官方的CountDownTimer类（没有特殊要求的话可以使用）
         * 即使退出activity，倒计时还能进行，因为是创建了后台的线程。
         * 有onTick，onFinish、cancel和start方法
         */
        canSendCode = false;
        timer = new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //每隔countDownInterval秒会回调一次onTick()方法
                LogUtils.d(TAG, "onTick  " + millisUntilFinished / 1000);
                Message msg = new Message();
                msg.what = MSG_UPDATE_COUNT_TIME;
                msg.arg1 = (int) millisUntilFinished / 1000;
                handler.sendMessage(msg);
            }

            @Override
            public void onFinish() {
                handler.sendEmptyMessage(MSG_UPDATE_SEND_CODE_ENABLE);
                canSendCode = true;
                LogUtils.d(TAG, "onFinish -- 倒计时结束");
            }
        };
        timer.start();// 开始计时
    }

    private void followSocket() {
        socketIOManager = new SocketIOManager(Constant.SOCKET_SERVER_URL);
        socketIOManager.setOnConnect(new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String UUID = PhoneUtils.getIMEI();
                LogUtils.d(SocketIOManager.TAG, "auth key : " + HttpConfig.SOCKET_FOLLOW_AUTH_KEY + Hawk.get("eqId",0));
                socketIOManager.getSocket().emit(HttpConfig.SOCKET_ANDROID_AUTH, HttpConfig.SOCKET_FOLLOW_AUTH_KEY + Hawk.get("eqId",0));
                LogUtils.d(SocketIOManager.TAG, "call: send android auth over");
            }
        });
        socketIOManager.setSocketEvent(new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                LogUtils.d(SocketIOManager.TAG, " args[0]=" + args[0]);

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
                FollowSocketBean followSocketBean = new Gson().fromJson(json, FollowSocketBean.class);
                if (followSocketBean != null) {
                    memberId = followSocketBean.memberId;
                    handler.sendEmptyMessage(MSG_SHOW_FOLLOW_SUCCESS);
                }
            }
        });
        socketIOManager.connectSocket();
    }

    private void showResultDialog(boolean isSuccess) {
        String msg = isSuccess ? "领取成功" : "领取失败";
        if (isSuccess) {
            phoneEditText.getEditableText().clear();
            identifyingEditText.getEditableText().clear();
            memberId = -1;
        }
        mMoreFunctionDialog = new MoreFunctionDialog(this, msg, R.style.HttpRequestDialogStyle);
        mMoreFunctionDialog.show();
    }

}
