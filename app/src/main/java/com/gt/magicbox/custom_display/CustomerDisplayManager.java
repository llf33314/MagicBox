package com.gt.magicbox.custom_display;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.gt.magicbox.base.BaseConstant;
import com.gt.magicbox.bean.SerialPortDataBean;
import com.gt.magicbox.utils.commonutil.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android_serialport_api.SerialPort;

/**
 * Description:
 * Created by jack-lin on 2017/11/23 0023.
 * Buddha bless, never BUG!
 */

public class CustomerDisplayManager {
    private boolean isExit=false;
    private Context context;
    private CustomerDisplayDataListener displayDataListener;
    private ArrayList<SerialPortDataBean> listQA = new ArrayList<>();
    protected SerialPort mSerialPort;
    protected InputStream mInputStream;
    protected OutputStream mOutputStream;
    private Thread receiveThread;
    private String port = "ttyS0";
    private final String TAG = "SerialPort";
    public void openSerialPort(int baudRate) {
        try {
            mSerialPort = new SerialPort(new File("/dev/" + port), baudRate,
                    0);
            mInputStream = mSerialPort.getInputStream();
            mOutputStream = mSerialPort.getOutputStream();
            receiveThread();
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
                        if (mInputStream == null) {
                            return;
                        }
                        size = mInputStream.read(buffer);
                        if (size > 0) {
                            String info = new String(buffer, 0,
                                    size);
                            if (!TextUtils.isEmpty(info)&&displayDataListener!=null){
                                displayDataListener.onDataReceive(info);
                            }
                            if (!TextUtils.isEmpty(info) && info.contains("QA")) {
                                String qaStr = info.substring(info.indexOf("A") + 1);
                                Log.e(TAG, "接收到串口信息: qaStr" + qaStr);

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
     * 关闭串口
     */
    public void closeSerialPort() {
        isExit=true;
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
    public void setDisplayDataListener(CustomerDisplayDataListener displayDataListener) {
        this.displayDataListener = displayDataListener;
    }
}
