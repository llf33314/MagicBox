package com.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.gt.magicbox.bean.OrderBean;
import com.gt.magicbox.http.HttpConfig;
import com.gt.magicbox.pay.ChosePayModeActivity;
import com.gt.magicbox.utils.commonutil.PhoneUtils;
import com.gt.magicbox.webview.WebViewActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Description:
 * Created by jack-lin on 2017/7/24 0024.
 */

public class OrderPushService extends Service {
    public static final String TAG = "OrderPushService";
    private Socket mSocket;
    private PushBinder binder=new PushBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                initSocketHttp();
                connectSocket();
            }
        }).start();

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"onDestroy");
            disSocket();
        super.onDestroy();
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
            OrderBean orderBean= new Gson().fromJson(retData,OrderBean.class);
            Log.d(TAG, "socket --> " + retData.toString()+"  orderBean.orderNo="+orderBean.pay_type+"  time="+orderBean.money);
            if (orderBean!=null){
                startERCodePay(orderBean.pay_type,Double.parseDouble(orderBean.money));
            }
        }
    };
    /**
     * @param type 0-微信，1-支付宝
     */
    private void startERCodePay(int type,double money){
        Intent intent=new Intent(getApplicationContext(), WebViewActivity.class);
        intent.putExtra("webType",WebViewActivity.WEB_TYPE_PAY);
        intent.putExtra("money",money);
        intent.putExtra("payMode",type);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
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

    /**
     * 初始化Socket,Http的连接方式
     */
    public void initSocketHttp() {
        try {
            mSocket = IO.socket(HttpConfig.SOCKET_SERVER_URL); // 初始化Socket
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Log.i(TAG,"initSocketHttp");

    }

    /**
     * 初始化socket，并建立连接
     */
    public void connectSocket() {

        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("chatevent", socketEvent);
        mSocket.connect();
    }
    /**
     * 关闭所有socket链接
     */
    public void disSocket() {
        mSocket.disconnect();
        mSocket.off("chatevent", socketEvent);
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
    }

    public class PushBinder extends Binder{

    }

}
