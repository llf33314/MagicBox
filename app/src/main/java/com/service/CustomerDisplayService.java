package com.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseConstant;
import com.gt.magicbox.bean.SerialPortDataBean;
import com.gt.magicbox.custom_display.CustomerDisplayDataListener;
import com.gt.magicbox.http.BaseResponse;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.main.MoreFunctionDialog;
import com.gt.magicbox.pay.ChosePayModeActivity;
import com.gt.magicbox.pay.QRCodePayActivity;
import com.gt.magicbox.setting.wificonnention.WifiConnectionActivity;
import com.gt.magicbox.utils.DeferredHandler;
import com.gt.magicbox.utils.NetworkUtils;
import com.gt.magicbox.utils.commonutil.AppManager;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.gt.magicbox.utils.commonutil.ToastUtil;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import android_serialport_api.SerialPort;

/**
 * Description:
 *
 * @author jack-lin
 * @date 2017/11/17 0017
 * Buddha bless, never BUG!
 */

public class CustomerDisplayService extends Service {
    private boolean isExit;
    private static final int SHOW_DIALOG = 0;
    private CustomerDisplayBinder binder = new CustomerDisplayBinder();
    private CustomerDisplayDataListener customerDisplayDataListener;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_DIALOG:
                    if (dialog == null) {
                        dialog = new MoreFunctionDialog(AppManager.getInstance().currentActivity(), "没有网络，请连接后重试", R.style.HttpRequestDialogStyle);
                        dialog.getConfirmButton().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                    }
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private DeferredHandler deferredHandler=new DeferredHandler();
    private ArrayList<SerialPortDataBean> listQA = new ArrayList<>();
    protected SerialPort mSerialPort;
    protected InputStream mInputStream;
    protected OutputStream mOutputStream;
    private Thread receiveThread;
    private String port = "ttyS0";
    private final String TAG = "SerialPort";
    MoreFunctionDialog dialog;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        LogUtils.d("service","CustomerDisplayService onCreate");

        openSerialPort(Hawk.get("baud", 2400));
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        LogUtils.d("service","CustomerDisplayService onDestroy");
        closeSerialPort();
        super.onDestroy();
    }

    private void openSerialPort(int baudRate) {
        try {
            mSerialPort = new SerialPort(new File("/dev/" + port), baudRate,
                    0);
            mInputStream = mSerialPort.getInputStream();
            mOutputStream = mSerialPort.getOutputStream();
            receiveThread();
            LogUtils.d("SerialPort","打开串口成功 " + port+" baudRate="+baudRate);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.i(TAG, "打开失败");
            e.printStackTrace();
        }
    }

    private void receiveThread() {
        // 接收
        receiveThread = new Thread() {
            @Override
            public void run() {
                while (!isExit) {
                    int size;
                    try {
                        byte[] buffer = new byte[1024];
                        if (mInputStream == null)
                            return;
                        size = mInputStream.read(buffer);
                        if (size > 0) {
                            String info = new String(buffer, 0,
                                    size);
                            if (customerDisplayDataListener != null) {
                                customerDisplayDataListener.onDataReceive(info);
                            }
                            if (!TextUtils.isEmpty(info) && info.contains("QA")) {
                                String qaStr = info.substring(info.indexOf("A") + 1);
                                Log.e(TAG, "接收到串口信息: qaStr" + qaStr);
                                listQA.add(new SerialPortDataBean(System.currentTimeMillis(),
                                        qaStr));
                            }
                            if (listQA.size() >= 1) {
                                int sizeQA = listQA.size();
                                SerialPortDataBean last = listQA.get(sizeQA - 1);
                                Log.e(TAG, "生成订单 last=" + last.data);
                                double money=Double.parseDouble(last.data);
                                if (money>0) {
                                    startERCodePay(BaseConstant.PAY_ON_WECHAT, money);
                                }
                                listQA.clear();
                            }
                            Log.e(TAG, "接收到串口信息:" + info);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        receiveThread.start();
    }
    /**
     * @param type 0-微信，1-支付宝
     */
    private void startERCodePay(int type, double money) {
        if (NetworkUtils.isConnected()) {
            Hawk.put("payType", type);
            Intent intent = new Intent(CustomerDisplayService.this, QRCodePayActivity.class);
            intent.putExtra("type", QRCodePayActivity.TYPE_CUSTOMER_DISPLAY_PAY);
            intent.putExtra("money", money);
            intent.putExtra("payMode", type);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            handler.sendEmptyMessage(SHOW_DIALOG);
        }
    }

    /**
     * 关闭串口
     */
    public void closeSerialPort() {
        isExit = true;
        if (mSerialPort != null) {
            mSerialPort.close();
        }
        if (mInputStream != null) {
            try {
                mInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mOutputStream != null) {
            try {
                mOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public boolean deleteUnPaidOrderTask(final long orderId){
        deferredHandler.post(new Runnable() {
            @Override
            public void run() {
                deleteOrder(orderId);
            }
        });
        return false;
    }
    private void deleteOrder(final long orderId){
        HttpCall.getApiService()
                .deleteNotPayOrder(Hawk.get("eqId",0), orderId)
                .compose(ResultTransformer.<BaseResponse>transformerNoData())//线程处理 预处理
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        LogUtils.d(TAG, "deleteNotPayOrder onSuccess");
                        if (data != null) {
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.d(TAG, "deleteNotPayOrder onError");
                        super.onError(e);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        LogUtils.d(TAG, "deleteNotPayOrder onFailure");
                        super.onFailure(code, msg);
                    }
                });
    }
    public class CustomerDisplayBinder extends Binder {
        public CustomerDisplayService getService() {
            // Return this instance of LocalService so clients can call public methods
            return CustomerDisplayService.this;
        }
    }


}
