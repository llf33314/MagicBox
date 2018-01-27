package com.gt.magicbox.setting.printersetting;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.command.LabelCommand;
import com.gprinter.io.GpDevice;
import com.gprinter.io.PortParameters;
import com.gprinter.service.GpPrintService;
import com.gt.magicbox.R;
import com.gt.magicbox.base.MyApplication;
import com.gt.magicbox.bean.MemberCardBean;
import com.gt.magicbox.bean.ShiftRecordsAllBean;
import com.gt.magicbox.main.MoreActivity;
import com.gt.magicbox.main.MoreFunctionDialog;
import com.gt.magicbox.setting.printersetting.bluetooth.BluetoothUtil;
import com.gt.magicbox.setting.printersetting.bluetooth.OpenPrinterPortMsg;
import com.gt.magicbox.utils.RxBus;
import com.gt.magicbox.utils.commonutil.AppManager;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.gt.magicbox.utils.commonutil.ToastUtil;
import com.gt.magicbox.widget.HintDismissDialog;

import org.apache.commons.lang.ArrayUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by wzb on 2017/7/21 0021.
 * 蓝牙与USB能互相切换 主要通过广播操作
 */

public class PrinterConnectService extends Service {

    private static final String ACTION_USB_PERMISSION =
            "com.android.example.USB_PERMISSION";
    //不干胶打印机 ID 1280   名称 GP-58   其他打印机不知道是不是都是这样
    private static final int GP_PRODUCT_ID_BGJ=1280;
    private static final int GP_PRODUCT_ID_RM=512;

    public static final String CONNECTION_ACTION="action.connect.status";

    public static final int PRINTER_CONNECTING=14;
    public static final int PRINTER_NOT_INTI=15;

    BluetoothAdapter mBluetoothAdapter ;

    Intent intentGpPrintService;


    public static GpService mGpService = null;

    private PrinterServiceConnection conn = null;
    private static int mPrinterIndex = 0;

    UsbManager mUsbManager ;

    private static UsbDevice mUsbDevice;

    private  static HintDismissDialog hintDismissDialog;

    /**
     * 端口连接状态广播
     */
    private PortConnectionStateBroad mPortConnectionStateBroad;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        //打开、关闭端口
        RxBus.get().toObservable(OpenPrinterPortMsg.class).subscribe(new Consumer<OpenPrinterPortMsg>() {
            @Override
            public void accept(@NonNull OpenPrinterPortMsg openPrinterPortMsg) throws Exception {

                switch (openPrinterPortMsg.getBluetoothState()){
                    case OpenPrinterPortMsg.CLOSE_PROT:
                            closeProt();
                        break;
                    case OpenPrinterPortMsg.OPEN_PROT:

                        if (!isConnceted()){//usb没有连接
                            BluetoothDevice d=openPrinterPortMsg.getBluetoothDevice();
                            openBluetoothProtFromDevice(d);
                        }

                        break;
                }
            }
        });

        connection();

        mBluetoothAdapter =BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter==null){
            ToastUtil.getInstance().showToast("设备不支持蓝牙功能");
        }else{
            registerBoothCloseBroadcast();
        }

        registerUsbBroad();

    }

    private void connection() {
        if (mGpService==null){
        conn = new PrinterServiceConnection();
        intentGpPrintService = new Intent(this, GpPrintService.class);
        bindService(intentGpPrintService, conn, Context.BIND_AUTO_CREATE);
        }
    }

    public void closeProt(){
        try {
            int state=mGpService.getPrinterConnectStatus(mPrinterIndex);
            if (state==GpDevice.STATE_CONNECTING||state==GpDevice.STATE_CONNECTED ){
                mGpService.closePort(mPrinterIndex);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void openBluetoothProtFromDevice(BluetoothDevice bluetoothDevice){
        try {
            int rel = mGpService.openPort(mPrinterIndex, PortParameters.BLUETOOTH ,bluetoothDevice.getAddress(),0);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            //ToastUtil.getInstance().showToast("result :" + String.valueOf(r));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    /**
     * 打开端口连接
     * 优先选择蓝牙
     */
    public void openBluetoothOrUsbProt() {

        BluetoothDevice printDevice= BluetoothUtil.getConnectingBluetooth(mBluetoothAdapter);
        if (printDevice!=null){
            openBluetoothProtFromDevice(printDevice);
        }else if(isHasUsbDevice()){
            openUsbProt();
        }
    }

    private boolean isHasUsbDevice(){
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        LogUtils.d("usbTest","deviceList="+deviceList.size());

        while(deviceIterator.hasNext()){
            UsbDevice temp=deviceIterator.next();
            LogUtils.d("usbTest","temp id="+temp.getProductId()+"  name="+temp.getDeviceName());

            if (temp.getProductId()==GP_PRODUCT_ID_RM) {
                LogUtils.d("usbTest","GP_PRODUCT_ID_RM="+temp.getProductId()+"  name="+temp.getDeviceName());

                mUsbDevice = temp;
                break;
            }
        }
        return  mUsbDevice!=null;

    }




    private void registerBoothCloseBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        registerReceiver(mBluetoothReceiver, filter);
    }

    private final BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {

                try {
                    mGpService.closePort(mPrinterIndex);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {

                try {
                    mGpService.closePort(mPrinterIndex);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mGpService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService = GpService.Stub.asInterface(service);
            if (mPortConnectionStateBroad==null){
                mPortConnectionStateBroad=new PortConnectionStateBroad();
                IntentFilter intentFilter=new IntentFilter(CONNECTION_ACTION);
                PrinterConnectService.this.registerReceiver(mPortConnectionStateBroad,intentFilter);
            }
            //打开端口
            openBluetoothOrUsbProt();
        }
    }

    private static int openUsbProt(){
        if (mUsbDevice==null){
            ToastUtil.getInstance().showToast("请连接USB打印机");
            return -1;
        }

        int rel = -1;
        try {
            int state=mGpService.getPrinterConnectStatus(mPrinterIndex);
            if (state==GpDevice.STATE_NONE ||state==GpDevice.STATE_LISTEN ){
                rel = mGpService.openPort(mPrinterIndex, PortParameters.USB, mUsbDevice.getDeviceName(), 0);
                GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
             //  ToastUtil.getInstance().showToast("result :" + String.valueOf(r));
            }
            return rel;

        } catch (RemoteException e) {
            e.printStackTrace();
            return rel;
        }
    }



    /**
     * 打印机状态
     */
    public static String getPrinterStatus() {
        if (mGpService==null){
            return "打印机未连接";
        }
        String str = "";
        try {
            int status = mGpService.queryPrinterStatus(mPrinterIndex, 500);

            if (status == GpCom.STATE_NO_ERR) {
                str = "打印机正常";
            } else {
                str = "打印机 ";
                if ((byte) (status & GpCom.STATE_OFFLINE) > 0) {
                    str += "脱机";
                }
                if ((byte) (status & GpCom.STATE_PAPER_ERR) > 0) {
                    str += "缺纸";
                }
                if ((byte) (status & GpCom.STATE_COVER_OPEN) > 0) {
                    str += "打印机开盖";
                }
                if ((byte) (status & GpCom.STATE_ERR_OCCURS) > 0) {
                    str += "打印机出错";
                }
                if ((byte) (status & GpCom.STATE_TIMES_OUT) > 0) {//wzb 自己修改的 因为正常的时候也显示打印机超时
                    str = "正常";
                }
            }
           // ToastUtil.getInstance().showToast("打印机：" + mPrinterIndex + " 状态：" + str);
        } catch (RemoteException e1) {
            e1.printStackTrace();
            return "打印机状态未知错误";
        }
        return str;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (conn != null) {
            unbindService(conn); // unBindService
        }
        try {
            unregisterReceiver(mBluetoothReceiver);
        }catch (Exception e){

        }
        //关闭端口
        try {
            mGpService.closePort(mPrinterIndex);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (mPortConnectionStateBroad!=null){
            this.unregisterReceiver(mPortConnectionStateBroad);
        }

        this.unregisterReceiver(mUsbReceiver);

    }

    private static void showHintNotConnectDialog() {
            hintDismissDialog = new HintDismissDialog(MyApplication.getAppContext(), "打印机未连接请连接后再打印")
                    .setOkText("去设置").setCancelText("取消").setOnOkClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(AppManager.getInstance().currentActivity(), PrinterSettingActivity.class);
                            AppManager.getInstance().currentActivity().startActivity(intent);
                        }
                    }).setOnCancelClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
        hintDismissDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

        hintDismissDialog.show();
    }

    /**
     * 打印机是否连接 或者正在连接
     */
    private boolean isConnceted() {
        try {
            if (mGpService.getPrinterConnectStatus(mPrinterIndex) == GpDevice.STATE_CONNECTING
                    || mGpService.getPrinterConnectStatus(mPrinterIndex) == GpDevice.STATE_CONNECTED) {
                return true;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

        /**
     * 打印调用的方法
     */
    public static int  printReceiptClicked(String money) {

        if (mGpService==null){
            showHintNotConnectDialog();
            return PRINTER_NOT_INTI;
        }

        try {//拔插的时候这个sdk有毒  要这么处理
            int state=mGpService.getPrinterConnectStatus(mPrinterIndex);
           // ToastUtil.getInstance().showToast("state："+state);
            if (state==GpDevice.STATE_CONNECTING){
                ToastUtil.getInstance().showToast("打印机正在连接请稍后再试");
                return PRINTER_CONNECTING;
            }
            if (state==GpDevice.STATE_NONE){
                //mGpService.closePort(mPrinterIndex);
                if (mUsbDevice!=null){
                    openUsbProt();
                }else{//蓝牙跟USB都没连接
                    showHintNotConnectDialog();
                }
            }
            //这里很关键   打印机类型是ESC 还是TSC 暂时测试这么用
            int type = mGpService.getPrinterCommandType(mPrinterIndex);
            if (type == GpCom.ESC_COMMAND) {
              return sendESCReceipt(money);
            }else if (type == GpCom.LABEL_COMMAND){ //TSC
                return sendLabelReceipt();
            }
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
        return -1;
    }

    /**
     * 打印交班表
     * @return
     */
    public static int printEscExchange(ShiftRecordsAllBean.ShiftRecordsBean shiftBean){
        if (mGpService==null){
            showHintNotConnectDialog();
            return PRINTER_NOT_INTI;
        }
        try {//拔插的时候这个sdk有毒  要这么处理
            int state=mGpService.getPrinterConnectStatus(mPrinterIndex);
            // ToastUtil.getInstance().showToast("state："+state);
            if (state==GpDevice.STATE_CONNECTING){
                ToastUtil.getInstance().showToast("打印机正在连接请稍后再试");
                return PRINTER_CONNECTING;
            }
            if (state==GpDevice.STATE_NONE){
                //mGpService.closePort(mPrinterIndex);
                if (mUsbDevice!=null){
                    openUsbProt();
                }else{//蓝牙跟USB都没连接
                    showHintNotConnectDialog();
                }
            }
            //这里很关键   打印机类型是ESC 还是TSC 暂时测试这么用
            int printType = mGpService.getPrinterCommandType(mPrinterIndex);
            if (printType == GpCom.ESC_COMMAND) {
                EscCommand esc=PrintESCOrTSCUtil.getExChangeESC(shiftBean);
                return  sendEscDataToPrinter(esc);
            }else{
                ToastUtil.getInstance().showToast("请连接正确类型打印机");
            }
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
        return -1;
    }
    /**
     * 打印会员充值
     * @return
     */
    public static int printEscMemberRecharge(MemberCardBean memberCardBean, String orderNo
            , String money, int type,String balance) {
        if (mGpService == null) {
            showHintNotConnectDialog();
            return PRINTER_NOT_INTI;
        }
        try {//拔插的时候这个sdk有毒  要这么处理
            int state = mGpService.getPrinterConnectStatus(mPrinterIndex);
            // ToastUtil.getInstance().showToast("state："+state);
            if (state == GpDevice.STATE_CONNECTING) {
                ToastUtil.getInstance().showToast("打印机正在连接请稍后再试");
                return PRINTER_CONNECTING;
            }
            if (state == GpDevice.STATE_NONE) {
                //mGpService.closePort(mPrinterIndex);
                if (mUsbDevice != null) {
                    openUsbProt();
                } else {//蓝牙跟USB都没连接
                    showHintNotConnectDialog();
                }
            }
            //这里很关键   打印机类型是ESC 还是TSC 暂时测试这么用
            int printType = mGpService.getPrinterCommandType(mPrinterIndex);
            if (printType == GpCom.ESC_COMMAND) {
                EscCommand esc = PrintESCOrTSCUtil.getPrintMemberRecharge(memberCardBean,orderNo,money,type,balance);
                return sendEscDataToPrinter(esc);
            } else {
                ToastUtil.getInstance().showToast("请连接正确类型打印机");
            }
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
        return -1;
    }
    /**
     * 打印付款二维码
     * @return
     */
    public static int printQrCode(String orderNo ,String money, String time, String qrCodeUrl) {
        if (mGpService == null) {
            showHintNotConnectDialog();
            return PRINTER_NOT_INTI;
        }
        try {//拔插的时候这个sdk有毒  要这么处理
            int state = mGpService.getPrinterConnectStatus(mPrinterIndex);
            // ToastUtil.getInstance().showToast("state："+state);
            if (state == GpDevice.STATE_CONNECTING) {
                ToastUtil.getInstance().showToast("打印机正在连接请稍后再试");
                return PRINTER_CONNECTING;
            }
            if (state == GpDevice.STATE_NONE) {
                //mGpService.closePort(mPrinterIndex);
                if (mUsbDevice != null) {
                    openUsbProt();
                } else {//蓝牙跟USB都没连接
                    showHintNotConnectDialog();
                }
            }
            //这里很关键   打印机类型是ESC 还是TSC 暂时测试这么用
            int printType = mGpService.getPrinterCommandType(mPrinterIndex);
            if (printType == GpCom.ESC_COMMAND) {
                EscCommand esc = PrintESCOrTSCUtil.getQrCodeEsc(orderNo , money,  time, qrCodeUrl);
                return sendEscDataToPrinter(esc);
            } else {
                ToastUtil.getInstance().showToast("请连接正确类型打印机");
            }
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
        return -1;
    }
    public static int printEsc0829(String orderNo,String money,String staffName,int type,String time){
        if (mGpService==null){
            showHintNotConnectDialog();
            return PRINTER_NOT_INTI;
        }

        try {//拔插的时候这个sdk有毒  要这么处理
            int state=mGpService.getPrinterConnectStatus(mPrinterIndex);
            // ToastUtil.getInstance().showToast("state："+state);
            if (state==GpDevice.STATE_CONNECTING){
                ToastUtil.getInstance().showToast("打印机正在连接请稍后再试");
                return PRINTER_CONNECTING;
            }
            if (state==GpDevice.STATE_NONE){
                //mGpService.closePort(mPrinterIndex);
                if (mUsbDevice!=null){
                    openUsbProt();
                }else{//蓝牙跟USB都没连接
                    showHintNotConnectDialog();
                }
            }
            //这里很关键   打印机类型是ESC 还是TSC 暂时测试这么用
            int printType = mGpService.getPrinterCommandType(mPrinterIndex);
            if (printType == GpCom.ESC_COMMAND) {
                return printEsc(orderNo,money,staffName,type,time);
            }else{
                ToastUtil.getInstance().showToast("请连接正确类型打印机");
            }
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
        return -1;
    }



    private static int printEsc(String orderNo,String money,String staffName,int type,String time){
        EscCommand esc=PrintESCOrTSCUtil.getPrintEscTest(orderNo,money,staffName,type,time);
        return  sendEscDataToPrinter(esc);
    }

    //发送Esc命令到打印机
    private static int sendEscDataToPrinter(EscCommand esc){

        Vector<Byte> datas = esc.getCommand(); // 发送数据
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String sss = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rs=-1;
        try {
            rs = mGpService.sendEscCommand(mPrinterIndex, sss);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rs];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                // ToastUtil.getInstance().showToast(GpCom.getErrorText(r));
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * 最开始模板
     */
    private static int sendESCReceipt(String money) {
        EscCommand esc = PrintESCOrTSCUtil.getPrintEscCommand(money);
        return sendEscDataToPrinter(esc);
    }

    private static int sendLabelReceipt() {
         int LEFT=15;
        String name="超级英式奶茶";

        //总共320*240
        LabelCommand tsc = PrintESCOrTSCUtil.getTscCommand();

        Vector<Byte> datas = tsc.getCommand(); // 发送数据
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel=-1;
        try {
            rel = mGpService.sendLabelCommand(mPrinterIndex, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                //Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return  rel;
    }



    /*private static int sendLabelReceipt() {
        //总共320*240
        LabelCommand tsc = new LabelCommand();
        tsc.addSize(40, 30); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(3); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.FORWARD , LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addReference(0, 0);// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区
        // 绘制简体中文
        tsc.addText(60, 12, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                "多粉餐饮");
        tsc.addReverse(45,5,320-95,63);


        tsc.addText(15,75, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                "海贼王连锁店");
        tsc.addText(10, 130, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "加糖拿铁 小杯");
        tsc.addText(10, 160, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "价格：15.00元");
        tsc.addText(10, 195, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "外卖：123456");

        //二维码
        tsc.addQRCode(190, 130, LabelCommand.EEC.LEVEL_L, 4, LabelCommand.ROTATION.ROTATION_0, " http://www.duofriend.com/");


        // 绘制图片
     *//*   Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.gprinter);
        tsc.addBitmap(20, 50, BITMAP_MODE.OVERWRITE, b.getWidth() * 2, b);
*//*

        // 绘制一维条码
       // tsc.add1DBarcode(20, 250, LabelCommand.BARCODETYPE.CODE128, 100, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0, "Gprinter");
        tsc.addPrint(1, 1); // 打印标签
       // tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        //打印机打印密度
        tsc.addDensity(LabelCommand.DENSITY.DNESITY10);
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        Vector<Byte> datas = tsc.getCommand(); // 发送数据
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel=-1;
        try {
            rel = mGpService.sendLabelCommand(mPrinterIndex, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                //Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return  rel;
    }*/

    private class PortConnectionStateBroad extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
                    int type = intent.getIntExtra(GpPrintService.CONNECT_STATUS, 0);
                    switch (type){
                        case GpDevice. STATE_NONE: //连接断开
                         //   ToastUtil.getInstance().showToast("打印机连接断开");
                            //蓝牙连接状态断开后判断是否有usb打印机 连接usb打印机
                            if (isHasUsbDevice()){
                                openUsbProt();
                            }
                            break;
                        case GpDevice. STATE_LISTEN : //监听状态
                            break;
                        case  GpDevice. STATE_CONNECTING : //正在连接
                            break;
                        case  GpDevice. STATE_CONNECTED : //已连接
                            //ToastUtil.getInstance().showToast("已连接设备");
                             break;
                        case  GpDevice. STATE_INVALID_PRINTER : //无效的打印机
                         //  ToastUtil.getInstance().showToast("无效的打印机");
                              break;
                        case  GpDevice. STATE_VALID_PRINTER : //有效的打印机
                            ToastUtil.getInstance().showToast("已连接打印机");
                              break;

                    }
                }
        }

    private void registerUsbBroad(){
        IntentFilter intentFilter=new IntentFilter();
        //intentFilter.addAction(ACTION_USB_PERMISSION);
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        this.registerReceiver(mUsbReceiver,intentFilter);
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_USB_PERMISSION)) {
                //sdk这条广播会去连接  SB SDK
                synchronized (this) {
                    UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (usbDevice != null) {//申请权限成功 打开端口
                                ToastUtil.getInstance().showToast("usb端口打开中...");

                             /*   int rel = 0;
                                try {
                                    rel = mGpService.openPort(mPrinterIndex, PortParameters.USB, usbDevice.getDeviceName(), 0);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
*/
                        }else{
                            ToastUtil.getInstance().showToast("usb打印机连接失败");
                        }
                    } else {
                        ToastUtil.getInstance().showToast("usb打印机连接被拒绝");
                    }
                }
            } else if (action.equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                // ToastUtil.getInstance().showToast("usb打印机断开...");
                mUsbDevice=null;
                //如果蓝牙已经连接则打开蓝牙端口
                closeProt();
                BluetoothDevice device=BluetoothUtil.getConnectingBluetooth(mBluetoothAdapter);
                if (device!=null){
                    openBluetoothProtFromDevice(device);
                }

            }else if (action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)){
                    if (!isConnceted()){//如果打印机状态是蓝牙已经连接中 则什么都不干
                        UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        if (usbDevice.getProductId()!=GP_PRODUCT_ID_RM) {
                            return;
                        }
                        mUsbDevice=usbDevice;
                        openUsbProt();
                    }
            }
        }
    };
    }

