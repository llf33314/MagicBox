package com.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.gt.magicbox.Constant;
import com.gt.magicbox.base.BaseConstant;
import com.gt.magicbox.bean.OrderBean;
import com.gt.magicbox.bean.OrderPushBean;
import com.gt.magicbox.bean.UnpaidOrderBean;
import com.gt.magicbox.http.BaseResponse;
import com.gt.magicbox.http.HttpConfig;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.DialogTransformer;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.http.socket.SocketIOManager;
import com.gt.magicbox.pay.ChosePayModeActivity;
import com.gt.magicbox.pay.QRCodePayActivity;
import com.gt.magicbox.utils.RxBus;
import com.gt.magicbox.utils.commonutil.FileHelper;
import com.gt.magicbox.utils.commonutil.FileUtils;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.gt.magicbox.utils.commonutil.PhoneUtils;
import com.gt.magicbox.utils.commonutil.SPUtils;
import com.gt.magicbox.webview.WebViewActivity;
import com.orhanobut.hawk.Hawk;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
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
        LogUtils.i(TAG,"onDestroy");
            disSocket();
        super.onDestroy();
    }



    // socket连接
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.d(TAG, "onConnect");
            String UUID = PhoneUtils.getIMEI();
            LogUtils.d(TAG, "auth key : " + HttpConfig.SOCKET_ORDER_AUTH_KEY + UUID);
            mSocket.emit(HttpConfig.SOCKET_ANDROID_AUTH, HttpConfig.SOCKET_ORDER_AUTH_KEY + UUID);
            LogUtils.d(TAG, "call: send android auth over");
        }
    };

    private Emitter.Listener socketEvent = new Emitter.Listener() {
        @Override
        public void call(Object... objects) {
            LogUtils.d(TAG, "socketEvent");
            JSONObject data = (JSONObject) objects[0];
            String retData = null;
            try {
                retData = data.get("message").toString();
                LogUtils.d(TAG, "socketEvent retData="+retData);
                handlerOrderData(retData);
            } catch (JSONException e) {
                e.printStackTrace();
                retData = "";
            }catch (Exception e){
               e.printStackTrace();
            }


        }
    };
    private void getUnpaidOrderCount(){
        HttpCall.getApiService()
                .getUnpaidOrderCount(PhoneUtils.getIMEI())
                .compose(ResultTransformer.<UnpaidOrderBean>transformer())//线程处理 预处理
                .subscribe(new BaseObserver<UnpaidOrderBean>() {
                    @Override
                    public void onSuccess(UnpaidOrderBean data) {
                        LogUtils.i(TAG,"UnpaidOrderBean onSuccess");
                        RxBus.get().post(data);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        LogUtils.i(TAG,"onFailure code="+code+"  msg="+msg);
                        super.onFailure(code, msg);
                    }
                });
    }

    /**
     * @param json
     *
     * String   retData="{\"busId\":36,\"businessUtilName\":\"shops.yifriend.net:711/shops/web/cashier/CF946E2B/payCallBack?id=ff8080815f58b4ed015f58cb308f003b\"," +
     *                "\"eqCode\":\"865067034465453\",\"model\":53,\"money\":150,\"orderId\":1047," +
     *                "\"orderNo\":\"YD1509023232136\",\"pay_type\":0,\"status\":\"success\",\"time\":\"2017-10-27 17:40:24\",\"type\":1}";
     */
    private void handlerOrderData(String json){
        json=convertStandardJSONString(json);
        LogUtils.d(TAG, "handlerOrderData retData="+json);
        playOrderSound();
        try {
            json=json.replaceAll("\\\\","");
            JSONObject orderObject= new JSONObject(json);
            if (orderObject!=null) {
                int orderId = orderObject.getInt("orderId");
                double money =  orderObject.getDouble("money");
                String orderNo=orderObject.getString("orderNo");
                LogUtils.d(TAG," money="+money);
                if (Constant.product.equals(BaseConstant.PRODUCTS[0])){
                    Intent intent = new Intent(getApplicationContext(), QRCodePayActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("type", QRCodePayActivity.TYPE_SERVER_PUSH);
                    intent.putExtra("orderId",orderId);
                    intent.putExtra("money", money);
                    intent.putExtra("orderNo",orderNo);
                    startActivity(intent);
                }else if (Constant.product.equals(BaseConstant.PRODUCTS[1])){
                    Intent intent = new Intent(getApplicationContext(), ChosePayModeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("customerType", ChosePayModeActivity.TYPE_ORDER_PUSH);
                    intent.putExtra("orderNo",orderNo);
                    intent.putExtra("money", money);
                    startActivity(intent);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public static String convertStandardJSONString(String data_json) {
        data_json = data_json.replaceAll("\\\\r\\\\n", "");
        data_json = data_json.replace("\"{", "{");
        data_json = data_json.replace("}\",", "},");
        data_json = data_json.replace("}\"", "}");
        return data_json;
    }
    private void playOrderSound(){
        if (mRingtone==null){
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mRingtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
        }
        mRingtone.play();

    }
    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.d(TAG, "diconnected");
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogUtils.d(TAG, "Error connecting");
        }
    };

    /**
     * 初始化Socket,Http的连接方式
     */
    public void initSocketHttp() {
        try {
            mSocket = IO.socket(Constant.SOCKET_SERVER_URL); // 初始化Socket
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        LogUtils.i(TAG,"initSocketHttp");

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
