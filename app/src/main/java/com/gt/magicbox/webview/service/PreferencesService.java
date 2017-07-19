package com.gt.magicbox.webview.service;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by Administrator on 2017/4/21.
 */

public class PreferencesService {

    private Context context;

    public PreferencesService(Context context) {
        this.context = context;
    }

    /**
     * 保存参数到SharedPreferences
     * @param perferencesName
     * @param params
     */
    public void save(String perferencesName, Map<String, String> params) {
        //获得SharedPreferences对象
        SharedPreferences preferences = context.getSharedPreferences(perferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        for (String key : params.keySet()){
            editor.putString(key, params.get(key));
        }
        editor.commit();
    }

    /**
     * 获取各项参数
     * @return
     */
    public Map<String, ?> getPerferences(String perferencesName) {
        SharedPreferences preferences = context.getSharedPreferences(perferencesName, Context.MODE_PRIVATE);
        Map<String, ?> params = preferences.getAll();
        return params;
    }

}
