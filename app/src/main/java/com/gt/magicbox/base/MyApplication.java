package com.gt.magicbox.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Process;

import com.gt.magicbox.setting.printersetting.PrinterConnectSerivce;
import com.gt.magicbox.utils.commonutil.Utils;

/**
 * Created by wzb on 2017/7/11 0011.
 */

public class MyApplication extends Application {
    private static Intent portIntent;
    private static Context applicationContext;
    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext=getApplicationContext();
        Utils.init(applicationContext);
        portIntent=new Intent(this, PrinterConnectSerivce.class);
        startService(portIntent);
    }
    public static Context getAppContext(){
        if (applicationContext==null){ //后台挂着 applicationContext被回收
            appExit();
        }
        return  applicationContext;
    }

    public static void appExit(){
        getAppContext().stopService(portIntent);
        Process.killProcess(Process.myPid());

    }

}
