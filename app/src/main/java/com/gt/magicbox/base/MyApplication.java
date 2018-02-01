package com.gt.magicbox.base;

import android.app.Application;
import android.content.Context;
import android.os.Process;

import com.gt.magicbox.Constant;
import com.gt.magicbox.main.MainActivity;
import com.gt.magicbox.utils.commonutil.Utils;
import com.orhanobut.hawk.Hawk;
import com.tendcloud.tenddata.TCAgent;

/**
 * Created by wzb on 2017/7/11 0011.
 */

public class MyApplication extends Application {

    private static Context applicationContext;

    //标记是否需要显示更新右上角
    private static boolean needUpdateApp=false;
    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext=getApplicationContext();
        Utils.init(applicationContext);
        Hawk.init(applicationContext).build();
        initTalkingData();
    }
    public static Context getAppContext(){
        if (applicationContext==null){ //后台挂着 applicationContext被回收
            appExit();
        }
        return  applicationContext;
    }

    public static void appExit(){
        getAppContext().stopService(MainActivity.portIntent);
        Process.killProcess(Process.myPid());

    }
    public static void logoutDeleteHawk(){
        Hawk.put("isLogin",false);
        Hawk.delete("busId");
        Hawk.delete("shiftId");
        Hawk.delete("ShopInfoBean");
        Hawk.delete("StaffListBean");
        Hawk.delete("deviceName");
        Hawk.delete("reasonList");
        Hawk.delete("fixedMoneyList");
    }
    public static boolean isNeedUpdateApp() {
        return needUpdateApp;
    }

    public static void setNeedUpdateApp(boolean needUpdateApp) {
        MyApplication.needUpdateApp = needUpdateApp;
    }
    private void initTalkingData(){
        TCAgent.LOG_ON=true;
        TCAgent.init(getAppContext(),"4B93C69A305C454F9B235CA067C01E54", Constant.product);
        TCAgent.setReportUncaughtExceptions(true);
    }
}
