package com.gt.magicbox.setting.printersetting;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseConstant;
import com.gt.magicbox.bean.ShopInfoBean;
import com.orhanobut.hawk.Hawk;
import com.ums.AppHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Description:
 * Created by jack-lin on 2017/10/10 0010.
 * Buddha bless, never BUG!
 */

public class PrintManager {
    private Context context;
    private Activity activity;

    public PrintManager( Activity activity){
        this.activity=activity;
        this.context=activity.getApplicationContext();
    }

    public void printReceipt(String orderNo,String money,int type,String time,String cashier){
        LayoutInflater layoutInflater= LayoutInflater.from(context);
        LinearLayout receiptView = (LinearLayout) layoutInflater.inflate(R.layout.print_receipt,null);
        TextView address = (TextView) receiptView.findViewById(R.id.address);
        TextView phone = (TextView) receiptView.findViewById(R.id.phone);
        TextView orderMoney = (TextView) receiptView.findViewById(R.id.orderMoney);
        TextView payType = (TextView) receiptView.findViewById(R.id.payType);
        TextView cashierTextView = (TextView) receiptView.findViewById(R.id.cashier);
        TextView timeTextView = (TextView) receiptView.findViewById(R.id.time);
        TextView orderNoTextView = (TextView) receiptView.findViewById(R.id.orderNo);
        TextView shopName = (TextView) receiptView.findViewById(R.id.shopName);
        ShopInfoBean shopInfoBean= Hawk.get("ShopInfoBean");
        if (shopInfoBean!=null&&!TextUtils.isEmpty(shopInfoBean.getShopName()))
            shopName.setText(shopInfoBean.getShopName());
        if (!TextUtils.isEmpty(orderNo))
            orderNoTextView.setText(shopInfoBean.getShopName());
        if (!TextUtils.isEmpty(time))
            timeTextView.setText(time);
        if (!TextUtils.isEmpty(cashier))
            cashierTextView.setText(cashier);
        payType.setText(BaseConstant.PAY_TYPE[type]);
        if (!TextUtils.isEmpty(money))
            orderMoney.setText(money);
        if (shopInfoBean!=null&&!TextUtils.isEmpty(shopInfoBean.getShops().getTelephone()))
            phone.setText(shopInfoBean.getShops().getTelephone());
        if (shopInfoBean!=null&&!TextUtils.isEmpty(shopInfoBean.getShops().getAddress()))
            address.setText(shopInfoBean.getShops().getAddress());

        receiptView.setDrawingCacheEnabled(true);
        Log.d("receiptView","receiptView width="+receiptView.getWidth()+"  height="+receiptView.getHeight());
        //调用下面这个方法非常重要，如果没有调用这个方法，得到的bitmap为null
        receiptView.measure(View.MeasureSpec.makeMeasureSpec(800, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(800, View.MeasureSpec.EXACTLY));
        //这个方法也非常重要，设置布局的尺寸和位置
        receiptView.layout(0, 0, receiptView.getMeasuredWidth(), receiptView.getMeasuredHeight());
        //获得绘图缓存中的Bitmap
        receiptView.buildDrawingCache();
        Bitmap bitmap = receiptView.getDrawingCache();
        AppHelper.callPrint(activity,  saveBitmap(context,bitmap));
    }
    private static final String SD_PATH = "/sdcard/duo/pic/";
    private static final String IN_PATH = "/duo/pic/";
    /**
     * 保存bitmap到本地
     *
     * @param context
     * @param mBitmap
     * @return
     */
    private  String saveBitmap(Context context, Bitmap mBitmap) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = SD_PATH;
        } else {
            savePath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath()
                    + IN_PATH;
        }
        try {
            filePic = new File(savePath  + "test.png");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return filePic.getAbsolutePath();
    }
}
