package com.gt.magicbox.pos.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/3/8.
 */

public class PromptUtils {

    private static PromptUtils promptUtils = new PromptUtils();

    private static Context context;

    private PromptUtils(){}

    public static PromptUtils getInstance(Context c){
        context = c;
        return promptUtils;
    }

    /**
     * 显示提示框，短时间
     * @param msg
     */
    public void showToastShort(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示提示框，长时间
     * @param msg
     */
    public void showToastLong(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 系统错误提示
     */
    public void showSysErrorLong(){
        this.showToastLong("系统错误，请尝试重启机器或者联系客服处理！错误码：sys");
    }

    /**
     * 接口回调错误
     */
    public void showCallBackErrorLong(){
        this.showToastLong("系统有误，请尝试重启机器或者联系客服处理！错误码：callback");
    }
}
