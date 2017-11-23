package com.gt.magicbox.custom_display;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.bean.BaudRateItemBean;
import com.gt.magicbox.main.MoreFunctionDialog;
import com.gt.magicbox.setting.wificonnention.WifiConnectionActivity;
import com.gt.magicbox.utils.RxBus;
import com.gt.magicbox.utils.SpannableStringUtils;
import com.gt.magicbox.utils.commonutil.ActivityUtils;
import com.gt.magicbox.utils.commonutil.AppManager;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.gt.magicbox.utils.commonutil.StringUtils;
import com.orhanobut.hawk.Hawk;
import com.service.CustomerDisplayService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;

/**
 * Description:
 * Created by jack-lin on 2017/11/21 0021.
 * Buddha bless, never BUG!
 */

@SuppressLint("ValidFragment")
public class Step03Fragment extends Fragment {
    @BindView(R.id.step03_bg)
    ImageView step03Bg;
    @BindView(R.id.step03_tip)
    TextView step03Tip;
    Unbinder unbinder;
    @BindView(R.id.countTimer)
    TextView countTimer;
    CustomerDisplayManager customerDisplayManager;
    private MoreFunctionDialog dialog;
    private NoScrollViewPager viewPager;
    private Button nextButton;
    private static final int COUNTING_TIME = 1;
    private static final int COUNTING_TIME_END = 2;
    private static final int NOT_MATCH_DIALOG_SHOW = 3;
    private static final int NOT_MATCH_DIALOG_DISMISS = 4;

    private CountDownTimer timer;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case COUNTING_TIME:
                    if (countTimer != null) {
                        countTimer.setText(SpannableStringUtils.diffTextSize(msg.arg1 + "秒", 35, 0, String.valueOf(msg.arg1).length()));
                    }
                    break;
                case COUNTING_TIME_END:
                    if (getActivity() != null && viewPager != null) {
                        viewPager.setCurrentItem(1);
                    }
                    break;
                case NOT_MATCH_DIALOG_SHOW:
                    if (getActivity() == null) return;
                    if (dialog == null) {
                        dialog = new MoreFunctionDialog(AppManager.getInstance().currentActivity(),
                                getResources().getString(R.string.mess_code), R.style.HttpRequestDialogStyle);
                    }
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    break;
                case NOT_MATCH_DIALOG_DISMISS:
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    break;
            }
        }
    };

    @SuppressLint("ValidFragment")
    public Step03Fragment(NoScrollViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d("fragment", "Step01Fragment onCreate");
        RxBus.get().toObservable(BaudRateItemBean.class).subscribe(new Consumer<BaudRateItemBean>() {
            @Override
            public void accept(final BaudRateItemBean baudRateItemBean) throws Exception {
                if (getActivity() == null) {
                    return;
                }
                if (step03Tip != null) {
                    step03Tip.setText("当前模式:" + baudRateItemBean.baudRate + "波特率");
                }
                startCountDownTime(120);
                customerDisplayManager = new CustomerDisplayManager();
                customerDisplayManager.openSerialPort(baudRateItemBean.baudRate);
                customerDisplayManager.setDisplayDataListener(new CustomerDisplayDataListener() {
                    @Override
                    public void onDataReceive(String data) {
                        LogUtils.d("data", "data=" + data + "----乱码=" + StringUtils.isMessyCode(data));
                        if (StringUtils.isMessyCode(data)) {
                            handler.sendEmptyMessage(NOT_MATCH_DIALOG_SHOW);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    handler.sendEmptyMessage(NOT_MATCH_DIALOG_DISMISS);
                                    viewPager.setCurrentItem(1);
                                }
                            }, 5000);
                        } else {
                            if (StringUtils.isContainNumber(data) && baudRateItemBean != null
                                    && getActivity() != null && ActivityUtils.getTopActivity(getActivity()).contains("MatchActivity")) {
                                Hawk.put("baud", baudRateItemBean.baudRate);
                                Hawk.put("hadMatchCustomerDisplay", true);
                                Intent intent = new Intent(getActivity(), MatchSuccessActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }
                    }
                });
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.step_03, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (customerDisplayManager != null) {
            customerDisplayManager.closeSerialPort();
        }
    }

    private void startCountDownTime(long time) {
        /**
         * 最简单的倒计时类，实现了官方的CountDownTimer类（没有特殊要求的话可以使用）
         * 即使退出activity，倒计时还能进行，因为是创建了后台的线程。
         * 有onTick，onFinish、cancel和start方法
         */
        timer = new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //每隔countDownInterval秒会回调一次onTick()方法
                Message msg = new Message();
                msg.what = COUNTING_TIME;
                msg.arg1 = (int) millisUntilFinished / 1000;
                if (getActivity() != null && viewPager.getCurrentIndex() == 2) {
                    handler.sendMessage(msg);
                } else {
                    LogUtils.d("onTick  cancel() viewPager.getCurrentIndex()=" + viewPager.getCurrentIndex());
                    cancel();
                }

            }

            @Override
            public void onFinish() {
                if (getActivity() != null) {
                    handler.sendEmptyMessage(COUNTING_TIME_END);
                }
            }
        };

        timer.start();// 开始计时
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
