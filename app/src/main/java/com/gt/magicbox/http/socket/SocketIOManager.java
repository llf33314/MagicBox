package com.gt.magicbox.http.socket;

import android.util.Log;

import com.google.gson.Gson;
import com.gt.magicbox.bean.OrderBean;
import com.gt.magicbox.http.HttpConfig;
import com.gt.magicbox.utils.commonutil.PhoneUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Description:
 * Created by jack-lin on 2017/9/12 0012.
 */

public class SocketIOManager {
    public static final String TAG = "socket.io";

    private Socket mSocket;

    public SocketIOManager(String serverUrl) {
        initSocketHttp(serverUrl);
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
    public void initSocketHttp(String url) {
        try {
            mSocket = IO.socket(url); // 初始化Socket
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "initSocketHttp");

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

    // socket连接
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "onConnect");
        }
    };

    // 接收推送事件
    private Emitter.Listener socketEvent = new Emitter.Listener() {
        @Override
        public void call(Object... objects) {
            Log.d(TAG, "socketEvent");

        }
    };

    public void setOnDisconnect(Emitter.Listener onDisconnect) {
        this.onDisconnect = onDisconnect;
    }

    public void setOnConnectError(Emitter.Listener onConnectError) {
        this.onConnectError = onConnectError;
    }

    public void setOnConnect(Emitter.Listener onConnect) {
        this.onConnect = onConnect;
    }

    public void setSocketEvent(Emitter.Listener socketEvent) {
        this.socketEvent = socketEvent;
    }
    public Socket getSocket() {
        return mSocket;
    }

}
