package com.gt.magicbox.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Process;

import com.gt.magicbox.setting.printersetting.PrinterConnectSerivce;

/**
 * Created by wzb on 2017/7/11 0011.
 */

public class MyApplication extends Application {
    private static Context applicationContext;
    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext=getApplicationContext();
        Intent intent=new Intent(this, PrinterConnectSerivce.class);
        startService(intent);
    }
    public static Context getAppContext(){
        if (applicationContext==null){ //后台挂着 applicationContext被回收
            appExit();
        }
        return  applicationContext;
    }

    public static void appExit(){
        Process.killProcess(Process.myPid());
    }

}
