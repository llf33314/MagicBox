package com.gt.magicbox.base;

import android.app.Application;
import android.content.Context;
import android.os.Process;

import com.gt.magicbox.main.MainActivity;
import com.gt.magicbox.utils.commonutil.Utils;
import com.orhanobut.hawk.Hawk;

/**
 * Created by wzb on 2017/7/11 0011.
 */

public class MyApplication extends Application {

    private static Context applicationContext;
    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext=getApplicationContext();
        Utils.init(applicationContext);
        Hawk.init(applicationContext).build();

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

}
