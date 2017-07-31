package com.gt.magicbox.webview.jsinterface;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.gt.magicbox.bean.UnpaidOrderBean;
import com.gt.magicbox.pay.ChosePayModeActivity;
import com.gt.magicbox.pay.PayResultActivity;
import com.gt.magicbox.pay.PaymentActivity;
import com.gt.magicbox.utils.RxBus;
import com.gt.magicbox.utils.commonutil.AppManager;
import com.gt.magicbox.utils.commonutil.PhoneUtils;
import com.gt.magicbox.webview.WebViewActivity;
import com.gt.magicbox.webview.service.UUIDService;
import com.gt.magicbox.webview.util.PromptUtils;


/**
 * Created by Administrator on 2017/3/8.
 */

public class DuofenJSBridge {

    private static final String TAG = "DuofenJSBridge";

    private Context context; // 传进来一个context，便于访问各种资源

    // 构造器
    public DuofenJSBridge(Context context){
        this.context = context;
    }

    /**
     * 显示提示框，短时间
     * @param msg
     */
    @JavascriptInterface
    public void showToastShort(String msg){
        PromptUtils.getInstance(context).showToastShort(msg);
    }

    /**
     * 显示提示框，长时间
     * @param msg
     */
    @JavascriptInterface
    public void showToastLong(String msg){
        PromptUtils.getInstance(context).showToastLong(msg);
    }

    /**
     * 获取设备号
     * @return
     */
    @JavascriptInterface
    public String getAppUUID(){
        String unqiueId = UUIDService.getUniqueUuid();
        String unqiueStatus = UUIDService.getUnique_status();
        if ("1".equals(unqiueStatus)){
            ((WebViewActivity) context).reloadSocket();
        }
        String json = "{'uuid':'" + PhoneUtils.getIMEI()+"','status':'" + unqiueStatus + "'}";
        Log.d(TAG, "getUniqueUuId" + json);
        return json;
    }

    /**
     * 返回上一页
     * @return
     */
    @JavascriptInterface
    public boolean webBack(){
        Log.d(TAG, "webBack");
        ((WebViewActivity) context).webBack();
        return true;
    }

    /**
     * 重新加载页面
     * @return
     */
    @JavascriptInterface
    public boolean webReload(){
        Log.d(TAG, "webBack");
        ((WebViewActivity) context).webReload();
        return true;
    }

    /**
     * 保存设备号
     * @param uuid
     * @return
     */
    @JavascriptInterface
    public boolean saveUUID(String uuid){
        Log.d(TAG, "saveUUID");
        boolean flag = false;
        try {
            flag = UUIDService.saveUUID(uuid);
            if (flag){
                UUIDService.replayStatus();
            }
        } catch (Exception e) {
            return false;
        }
        return flag;
    }
    /**
     * 重新选择支付方式
     */
    @JavascriptInterface
    public void reselection(){
        ((WebViewActivity) context).finish();

    }
    @JavascriptInterface
    public void payResult(boolean success,String message){
        if (success) {
            Intent intent=new Intent(context, PayResultActivity.class);
            intent.putExtra("success",success);
            intent.putExtra("message",message);
            context.startActivity(intent);

            AppManager.getInstance().finishActivity(WebViewActivity.class);
            AppManager.getInstance().finishActivity(PaymentActivity.class);
            AppManager.getInstance().finishActivity(ChosePayModeActivity.class);
        }

    }

    /**
     * 重置UUID
     */
    @JavascriptInterface
    public void resetUUID(){
        Log.d(TAG, "resetUUID");
     //   UUIDService.resetUUID();
    }

    /**
     * 开启扫码
     * @return
     */
    @JavascriptInterface
    public boolean scanCode(){
        Log.d(TAG, "scanCode");
        ((WebViewActivity) context).scanCode();
        return true;
    }

    /**
     * 删除订单后刷新主页订单数量
     */
    @JavascriptInterface
    public void refreshHomeOrder(){
        RxBus.get().post(new UnpaidOrderBean());
        //((WebViewActivity) context).scanCode();
    }

}
