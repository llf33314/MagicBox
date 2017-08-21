package com.service;

import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.gt.magicbox.bean.OrderBean;
import com.gt.magicbox.bean.UnpaidOrderBean;
import com.gt.magicbox.http.BaseObserver;
import com.gt.magicbox.http.BaseResponse;
import com.gt.magicbox.http.HttpCall;
import com.gt.magicbox.http.HttpConfig;
import com.gt.magicbox.http.RxObservableUtils;
import com.gt.magicbox.pay.ChosePayModeActivity;
import com.gt.magicbox.utils.RxBus;
import com.gt.magicbox.utils.commonutil.PhoneUtils;
import com.gt.magicbox.utils.commonutil.SPUtils;
import com.gt.magicbox.webview.WebViewActivity;
import com.orhanobut.hawk.Hawk;

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

    private Ringtone mRingtone;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                initSocketHttp();
                connectSocket();
            }
        }).start();


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
                startERCodePay(orderBean.orderId);
                getUnpaidOrderCount();
            }
        }
    };
    private void getUnpaidOrderCount(){
        HttpCall.getApiService()
                .getUnpaidOrderCount(PhoneUtils.getIMEI(), null)
                .compose(RxObservableUtils.<BaseResponse<UnpaidOrderBean>>applySchedulers())
                .subscribe(new BaseObserver<UnpaidOrderBean>(getApplicationContext(),false) {
                    @Override
                    public void onSuccess(UnpaidOrderBean data) {
                        Log.i(TAG,"UnpaidOrderBean onSuccess");
                        RxBus.get().post(data);


                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        Log.i(TAG,"onFailure code="+code+"  msg="+msg);
                        super.onFailure(code, msg);
                    }
                });
    }
    /**
     * @param orderId 订单编号
     */
    private void startERCodePay(int orderId){
        playOrderSound();
        Intent intent=new Intent(getApplicationContext(), WebViewActivity.class);
        intent.putExtra("webType",WebViewActivity.WEB_TYPE_SERVER_PUSH);
        intent.putExtra("orderId",orderId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void playOrderSound(){
        if (mRingtone==null){
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mRingtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
        }
        mRingtone.play();

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
