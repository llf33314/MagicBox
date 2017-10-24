package com.gt.magicbox.webview.service;

import android.content.Context;
import android.util.Log;


import com.gt.magicbox.utils.commonutil.LogUtils;
import com.gt.magicbox.webview.util.ObjectUtils;

import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2017/4/21.
 */

public class UUIDService {

    private static String TAG = "UUIDService";

    private static String unique_uuid = null;

    private static String unique_status = "0";

    private static Context context = null;

    public UUIDService(Context context){
        this.context = context;
    }

    public void initUUID(){
        saveOrUpdateUUID(true);
    }

    public static boolean saveUUID(String uuid) throws Exception{
        PreferencesService preferencesService = new PreferencesService(context);
        Map map = preferencesService.getPerferences("uuid");
        // 已经有unique_uuid
        if ("1".equals(unique_status)){
            unique_uuid = map.get("unique_uuid").toString();
            unique_status = map.get("unique_status").toString();
            LogUtils.d(TAG, "initUUID: has uuid " + unique_uuid);
            return false;
        }
        unique_status = "1";
        map.put("unique_uuid",uuid);
        map.put("unique_status",unique_status);
        preferencesService.save("uuid", map);
        unique_uuid = uuid;
        return true;
    }

    public static String saveOrUpdateUUID(boolean notUpdate){
        PreferencesService preferencesService = new PreferencesService(context);
        Map map = preferencesService.getPerferences("uuid");
        // 已经有unique_uuid
        if (notUpdate && map.containsKey("unique_uuid")){
            unique_uuid = map.get("unique_uuid").toString();
            unique_status = map.get("unique_status").toString();
            LogUtils.d(TAG, "initUUID: has uuid " + unique_uuid);
            return unique_uuid;
        }
        // 没有unique_uuid
        String rduuid = java.util.UUID.randomUUID().toString();
        LogUtils.d(TAG, "initUUID: rduuid --> " + rduuid);
        String serialNumber = android.os.Build.SERIAL;
        LogUtils.d(TAG, "initUUID: serialNumber --> " + serialNumber);

        String uuid = (serialNumber + PJWHash(rduuid)).toUpperCase();

        LogUtils.d(TAG, "initUUID: uuid --> " + uuid);
        map.put("unique_uuid",uuid);
        map.put("unique_status",unique_status);
        preferencesService.save("uuid", map);
        unique_uuid = uuid;
        return unique_uuid;
    }


    public static String getUniqueUuid(){
        if(ObjectUtils.isEmpty(unique_uuid)){
            return "";
        }
        return unique_uuid;
    }

    public static void replayStatus(){
        PreferencesService preferencesService = new PreferencesService(context);
        Map map = preferencesService.getPerferences("uuid");
        unique_status = "1";
        map.put("unique_status",unique_status);
        preferencesService.save("uuid", map);
    }

    public static String getUnique_status(){
        return unique_status;
    }

    // 重置status为0
    public static void resetUUID(){
        PreferencesService preferencesService = new PreferencesService(context);
        Map map = preferencesService.getPerferences("uuid");
        unique_status = "0";
        map.put("unique_status",unique_status);
        preferencesService.save("uuid", map);
    }

    /* PJWHash */
    public static long PJWHash(String str) {
        Random random=new Random();// 定义随机类
        int result=random.nextInt(1000);
        str = str + System.currentTimeMillis() + result;
        long BitsInUnsignedInt = (long) (4 * 8);
        long ThreeQuarters = (long) ((BitsInUnsignedInt * 3) / 4);
        long OneEighth = (long) (BitsInUnsignedInt / 8);
        long HighBits = (long) (0xFFFFFFFF) << (BitsInUnsignedInt - OneEighth);
        long hash = 0;
        long test = 0;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash << OneEighth) + str.charAt(i);
            if ((test = hash & HighBits) != 0)
                hash = ((hash ^ (test >> ThreeQuarters)) & (~HighBits));
        }
        return hash;
    }

}
