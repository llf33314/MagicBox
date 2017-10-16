package com.gt.magicbox.setting.printersetting;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gprinter.command.EscCommand;
import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseConstant;
import com.gt.magicbox.bean.MemberCardBean;
import com.gt.magicbox.bean.ShiftRecordsAllBean;
import com.gt.magicbox.bean.ShopInfoBean;
import com.gt.magicbox.utils.commonutil.TimeUtils;
import com.gt.magicbox.widget.LoadingProgressDialog;
import com.orhanobut.hawk.Hawk;
import com.ums.AppHelper;
import com.ums.upos.sdk.exception.CallServiceException;
import com.ums.upos.sdk.exception.SdkException;
import com.ums.upos.sdk.printer.BoldEnum;
import com.ums.upos.sdk.printer.FontConfig;
import com.ums.upos.sdk.printer.FontSizeEnum;
import com.ums.upos.sdk.printer.OnPrintResultListener;
import com.ums.upos.sdk.printer.PrinterManager;
import com.ums.upos.sdk.system.BaseSystemManager;
import com.ums.upos.sdk.system.OnServiceStatusListener;

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
    LoadingProgressDialog dialog;
    private static final int SHOW_DIALOG=0;
    private static final int DISMISS_DIALOG=1;
    private boolean isLoginPos;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SHOW_DIALOG:
                    dialog=new LoadingProgressDialog(activity,"打印中");
                    dialog.show();
                    break;
                case DISMISS_DIALOG:
                    if (dialog!=null)dialog.dismiss();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public PrintManager( Activity activity){
        this.activity=activity;
        this.context=activity.getApplicationContext();
    }
    public void startPrintReceiptByText(final String orderNo, final String money, final int type, final String time, final String cashier) {
        try {
            BaseSystemManager.getInstance().deviceServiceLogin(
                    activity, null, "99999998",//设备ID，生产找后台配置
                    new OnServiceStatusListener() {
                        @Override
                        public void onStatus(int arg0) {//arg0可见ServiceResult.java
                            if (0 == arg0 || 2 == arg0 || 100 == arg0) {//0：登录成功，有相关参数；2：登录成功，无相关参数；100：重复登录。
                                printReceiptByText(orderNo, money, type, time, cashier);
                            }
                        }
                    });
        } catch (SdkException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void startPrintExchangeByText(final ShiftRecordsAllBean.ShiftRecordsBean shiftBean) {
        try {
            BaseSystemManager.getInstance().deviceServiceLogin(
                    activity, null, "99999998",//设备ID，生产找后台配置
                    new OnServiceStatusListener() {
                        @Override
                        public void onStatus(int arg0) {//arg0可见ServiceResult.java
                            if (0 == arg0 || 2 == arg0 || 100 == arg0) {//0：登录成功，有相关参数；2：登录成功，无相关参数；100：重复登录。
                                printExchangeByText(shiftBean);
                            }
                        }
                    });
        } catch (SdkException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void startPrintMemberRechargeByText(final MemberCardBean memberCardBean, final String orderNo, final String money, final int type, final String balance) {
        try {
            BaseSystemManager.getInstance().deviceServiceLogin(
                    activity, null, "99999998",//设备ID，生产找后台配置
                    new OnServiceStatusListener() {
                        @Override
                        public void onStatus(int arg0) {//arg0可见ServiceResult.java
                            if (0 == arg0 || 2 == arg0 || 100 == arg0) {//0：登录成功，有相关参数；2：登录成功，无相关参数；100：重复登录。
                                printMemberRechargeByText(memberCardBean, orderNo, money, type, balance);
                            }
                        }
                    });
        } catch (SdkException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void printMemberRechargeByText(MemberCardBean memberCardBean, String orderNo, String money, int type, String balance) {
        ShopInfoBean shopInfoBean = Hawk.get("ShopInfoBean");
        PrinterManager printer = new PrinterManager();
        try {
            printer.initPrinter();
            FontConfig fontConfig = new FontConfig();
            fontConfig.setBold(BoldEnum.BOLD);
            fontConfig.setSize(FontSizeEnum.BIG);
            printer.setPrnText(shopInfoBean.getShopName() + "\n", fontConfig); // 打印文字
            fontConfig.setBold(BoldEnum.NOT_BOLD);
            fontConfig.setSize(FontSizeEnum.MIDDLE);
            // 打印文字 *//*
            printer.setPrnText("会员卡充值\n", fontConfig);// 打印文字
            printer.setPrnText("订单号：" + orderNo + "\n\n\n", fontConfig); // 打印文字

            printer.setPrnText("会员昵称：" + memberCardBean.nickName + "\n", fontConfig);
            printer.setPrnText("会员卡号：" + memberCardBean.cardNo + "\n\n", fontConfig);
            printer.setBitmap(createLineBitmap(20, 1));

            printer.setPrnText("充值金额：" + money + "元" + "\n", fontConfig);
            printer.setPrnText("充值方式：" + BaseConstant.PAY_TYPE[type] + "\n", fontConfig);
            printer.setPrnText("充值时间：" + TimeUtils.getNowString() + "\n\n", fontConfig);
            printer.setBitmap(createLineBitmap(20, 1));
            printer.setPrnText("当前卡内余额：          " + balance + "元\n\n\n", fontConfig);
            printer.setPrnText(" 技术支持 ·多粉 400-889-4522", fontConfig);
            printer.startPrint(new OnPrintResultListener() {

                @Override
                public void onPrintResult(int arg0) {//arg0可见ServiceResult.java
                    try {
                        BaseSystemManager.getInstance().deviceServiceLogout();
                    } catch (SdkException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        } catch (SdkException e) {

        } catch (CallServiceException e) {
            e.printStackTrace();
        }
    }
    private void printExchangeByText(ShiftRecordsAllBean.ShiftRecordsBean shiftBean){
        PrinterManager printer = new PrinterManager();
        try {
            printer.initPrinter();
            FontConfig fontConfig = new FontConfig();
            fontConfig.setBold(BoldEnum.BOLD);
            fontConfig.setSize(FontSizeEnum.BIG);
            printer.setPrnText("          交班表\n",fontConfig);
            fontConfig.setBold(BoldEnum.NOT_BOLD);
            fontConfig.setSize(FontSizeEnum.MIDDLE);
            if(shiftBean!=null){
                printer.setPrnText("门店："+shiftBean.getShopName()+"\n",fontConfig);
                printer.setPrnText("设备号："+shiftBean.getEqId()+"\n",fontConfig);
                printer.setPrnText("当班人："+shiftBean.getStaffName()+"\n",fontConfig);
                printer.setBitmap(createLineBitmap(20,1));
                printer.setPrnText("上班时间："+shiftBean.getStartTime()+"\n",fontConfig);
                printer.setPrnText("下班时间："+TimeUtils.getNowString()+"\n",fontConfig);
                printer.setBitmap(createLineBitmap(20,1));
                printer.setPrnText("实际支付订单数："+shiftBean.getOrderInNum()+"\n",fontConfig);
                printer.setPrnText("实际应收金额："+shiftBean.getMoney()+"\n",fontConfig);
                printer.setPrnText("微信支付："+shiftBean.getWechatMoney()+"\n",fontConfig);
                printer.setPrnText("支付宝："+shiftBean.getAlipayMoney()+"\n",fontConfig);
                printer.setPrnText("现金支付："+shiftBean.getCashMoney()+"\n",fontConfig);
                printer.setPrnText("会员卡："+shiftBean.getMemberMoney()+"\n",fontConfig);
                printer.setPrnText("银行卡："+shiftBean.getBankMoney()+"\n",fontConfig);

                printer.setBitmap(createLineBitmap(20,1));
                printer.setPrnText("当班人签名：\n\n",fontConfig);
                printer.setBitmap(createEmptyBitmap(1));
                printer.setPrnText("接班人签名：\n\n",fontConfig);
                printer.setBitmap(createEmptyBitmap(1));
                fontConfig.setBold(BoldEnum.BOLD);
                fontConfig.setSize(FontSizeEnum.BIG);
                printer.setPrnText("       欢迎再次光临\n",fontConfig);
                fontConfig.setBold(BoldEnum.NOT_BOLD);
                fontConfig.setSize(FontSizeEnum.MIDDLE);
                printer.setPrnText(" 技术支持 ·多粉 400-889-4522",fontConfig);
            }
                printer.startPrint(new OnPrintResultListener() {

                @Override
                public void onPrintResult(int arg0) {//arg0可见ServiceResult.java
                    try {
                        BaseSystemManager.getInstance().deviceServiceLogout();
                    } catch (SdkException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }catch (SdkException e){

        } catch (CallServiceException e) {
            e.printStackTrace();
        }
    }
    private void printReceiptByText(String orderNo,String money,int type,String time,String cashier) {
        PrinterManager printer = new PrinterManager();
        try {
            printer.initPrinter();
            FontConfig fontConfig = new FontConfig();
            fontConfig.setBold(BoldEnum.BOLD);
            fontConfig.setSize(FontSizeEnum.BIG);
            ShopInfoBean shopInfoBean = Hawk.get("ShopInfoBean");
            if (shopInfoBean != null && !TextUtils.isEmpty(shopInfoBean.getShopName()))
                printer.setPrnText(shopInfoBean.getShopName() + "\n", fontConfig);
            printer.setBitmap(createLineBitmap(20,1));
            fontConfig.setBold(BoldEnum.NOT_BOLD);
            fontConfig.setSize(FontSizeEnum.MIDDLE);
            if (!TextUtils.isEmpty(orderNo))
                printer.setPrnText("订单号: " + orderNo + "\n", fontConfig);
            if (!TextUtils.isEmpty(time))
                printer.setPrnText("开单时间: " + time + "\n", fontConfig);
            if (!TextUtils.isEmpty(cashier))
                printer.setPrnText("收银员:" + cashier + "\n", fontConfig);
            printer.setPrnText("支付方式: " + BaseConstant.PAY_TYPE[type] + "\n", fontConfig);
            printer.setBitmap(createLineBitmap(20,1));

            if (!TextUtils.isEmpty(money))
                printer.setPrnText("订单金额: " + money + "\n", fontConfig);
            printer.setBitmap(createLineBitmap(20,1));

            if (shopInfoBean != null && !TextUtils.isEmpty(shopInfoBean.getShops().getTelephone()))
                printer.setPrnText("联系电话: " + shopInfoBean.getShops().getTelephone() + "\n", fontConfig);
            if (shopInfoBean != null && !TextUtils.isEmpty(shopInfoBean.getShops().getAddress()))
                printer.setPrnText("地址: " + shopInfoBean.getShops().getAddress() + "\n", fontConfig);
            printer.setBitmap(createLineBitmap(20,1));
            fontConfig.setBold(BoldEnum.BOLD);
            fontConfig.setSize(FontSizeEnum.BIG);
            printer.setPrnText("       欢迎再次光临\n",fontConfig);
            fontConfig.setBold(BoldEnum.NOT_BOLD);
            fontConfig.setSize(FontSizeEnum.MIDDLE);
            printer.setPrnText(" 技术支持 ·多粉 400-889-4522",fontConfig);
            printer.startPrint(new OnPrintResultListener() {

                @Override
                public void onPrintResult(int arg0) {//arg0可见ServiceResult.java
                    try {
                        BaseSystemManager.getInstance().deviceServiceLogout();
                    } catch (SdkException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        } catch (SdkException e) {
            e.printStackTrace();
        } catch (CallServiceException e) {
            e.printStackTrace();
        }
    }
    public View printReceiptByImage(String orderNo,String money,int type,String time,String cashier){
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
            orderNoTextView.setText("订单号: "+orderNo);
        if (!TextUtils.isEmpty(time))
            timeTextView.setText("开单时间: "+time);
        if (!TextUtils.isEmpty(cashier))
            cashierTextView.setText("收银员:"+cashier);
        payType.setText("支付方式: "+BaseConstant.PAY_TYPE[type]);
        if (!TextUtils.isEmpty(money))
            orderMoney.setText("订单金额: "+money);
        if (shopInfoBean!=null&&!TextUtils.isEmpty(shopInfoBean.getShops().getTelephone()))
            phone.setText("联系电话: "+shopInfoBean.getShops().getTelephone());
        if (shopInfoBean!=null&&!TextUtils.isEmpty(shopInfoBean.getShops().getAddress()))
            address.setText("地址: "+shopInfoBean.getShops().getAddress());
       // new PrintViewTask().execute(receiptView);
        return receiptView;
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
        }finally {
        }

        return filePic.getAbsolutePath();
    }
    private  Bitmap loadBitmapFromView(View v)
    {
        v.measure(View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED));
        //这个方法也非常重要，设置布局的尺寸和位置
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }
    private  Bitmap createEmptyBitmap(int emptyLineCount)
    {
        View v=new View(context);
        v.setBackgroundColor(0xffffffff);
        //这个方法也非常重要，设置布局的尺寸和位置
        Bitmap b = Bitmap.createBitmap(50,emptyLineCount*2, Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }
    private  Bitmap createLineBitmap(int emptyTopHeight,int height)
    {
        View v=new View(context);
        Bitmap b = Bitmap.createBitmap(200,height+emptyTopHeight, Bitmap.Config.ARGB_8888);
        Paint p=new Paint();
        p.setColor(0xff000000);
        p.setStrokeWidth(height);
        Canvas c = new Canvas(b);
        c.drawLine(0,emptyTopHeight,200,emptyTopHeight,p);
        v.draw(c);
        return b;
    }
    private class PrintViewTask extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            handler.sendEmptyMessage(SHOW_DIALOG);
            Bitmap bitmap=loadBitmapFromView((View) objects[0]);
            if (bitmap!=null)
                AppHelper.callPrint(activity,  saveBitmap(context,bitmap));
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            handler.sendEmptyMessageDelayed(DISMISS_DIALOG,2000);
            super.onPostExecute(o);
        }
    }
}
