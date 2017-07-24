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
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.io.GpDevice;
import com.gprinter.io.PortParameters;
import com.gprinter.service.GpPrintService;
import com.gt.magicbox.R;
import com.gt.magicbox.main.PrintTestActivity;
import com.gt.magicbox.setting.printersetting.bluetooth.OpenPrinterPortMsg;
import com.gt.magicbox.utils.RxBus;
import com.gt.magicbox.utils.SimpleObserver;
import com.gt.magicbox.utils.commonutil.ToastUtil;

import org.apache.commons.lang.ArrayUtils;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by wzb on 2017/7/21 0021.
 * 还需很多优化 USB等
 */

public class PrinterConnectSerivce extends Service {

    BluetoothAdapter mBluetoothAdapter ;

    private static GpService mGpService = null;
    private PrinterServiceConnection conn = null;
    private static int mPrinterIndex = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBluetoothAdapter =BluetoothAdapter.getDefaultAdapter();
        //  mBluetoothAdapter.getProfileProxy()

        //打印
        RxBus.get().toObservable(String.class).subscribe(new SimpleObserver<String>(new Consumer<String>() {
            @Override
            public void accept(@NonNull String money) throws Exception {
              /* String printerStatus= getPrinterStatusClicked();
                if (!"打印机正常".equals(printerStatus)){
                    ToastUtil.getInstance().showToast(printerStatus,3000);
                    return;
                }*/
                printReceiptClicked(money);
            }
        }));

        //打开、关闭端口
        RxBus.get().toObservable(OpenPrinterPortMsg.class).subscribe(new SimpleObserver<OpenPrinterPortMsg>(new Consumer<OpenPrinterPortMsg>() {
            @Override
            public void accept(@NonNull OpenPrinterPortMsg openPrinterPortMsg) throws Exception {
                switch (openPrinterPortMsg.getBluetoothState()){
                    case OpenPrinterPortMsg.CLOSE_PROT:
                        closeProt();
                        break;
                    case OpenPrinterPortMsg.OPEN_PROT:
                        openProt();
                        break;
                }
            }
        }));

        registerBoothCloseBroadcast();

        // getPrinterStatusClicked();
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        connection();
        return super.onStartCommand(intent, flags, startId);
    }

    private void connection() {
        conn = new PrinterServiceConnection();
        Intent intent = new Intent(this, GpPrintService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService
    }


    public void closeProt(){
        try {
            mGpService.closePort(mPrinterIndex);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     * 打开端口连接
     */
    public void openProt() {
        Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();
        List<BluetoothDevice> devices=new ArrayList<BluetoothDevice>(bondedDevices);
        BluetoothDevice printDevice=null;
        for (BluetoothDevice b:devices){
            if (b.getType()==3){
                printDevice=b;
                break;
            }
        }
        if (printDevice==null){
            ToastUtil.getInstance().showToast("端口打开失败，请确认蓝牙是否已连接，或重启应用");
            return;
        }
        try {
            int rel = mGpService.openPort(mPrinterIndex, PortParameters.BLUETOOTH ,printDevice.getAddress(),0);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            //ToastUtil.getInstance().showToast("result :" + String.valueOf(r));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
            Log.i("ServiceConnection", "onServiceDisconnected() called");
            mGpService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService = GpService.Stub.asInterface(service);
            openProt();
        }
    }


    public boolean[] getConnectState() {
        boolean[] state = new boolean[GpPrintService.MAX_PRINTER_CNT];
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            state[i] = false;
        }
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            try {
                if (mGpService.getPrinterConnectStatus(i) == GpDevice.STATE_CONNECTED) {
                    state[i] = true;
                }
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return state;
    }

    /**
     * 打印机状态
     */
    public static String getPrinterStatus() {
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
                if ((byte) (status & GpCom.STATE_TIMES_OUT) > 0) {
                    str += "查询超时";
                }
            }
           // ToastUtil.getInstance().showToast("打印机：" + mPrinterIndex + " 状态：" + str);
        } catch (RemoteException e1) {
            // TODO Auto-generated catch block
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

    }


    /**
     * 打印调用的方法
     */
    public void printReceiptClicked(String money) {
        try {
            int type = mGpService.getPrinterCommandType(mPrinterIndex);
            if (type == GpCom.ESC_COMMAND) {
                sendReceipt(money);
//                int status = mGpService.queryPrinterStatus(mPrinterIndex, 500);
//                if (status == GpCom.STATE_NO_ERR) {
//                    sendReceipt();
//                } else {
//                    Toast.makeText(getApplicationContext(), "打印机错误！", Toast.LENGTH_SHORT).show();
//                }
            }
        } catch (RemoteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    void sendReceipt(String money) {
        EscCommand esc = new EscCommand();
        esc.addPrintAndFeedLines((byte) 1);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
        esc.addText("多粉餐厅（赛格）\n"); // 打印文字
        esc.addPrintAndLineFeed();

		/* 打印文字 */
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 取消倍高倍宽
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印左对齐
        esc.addText("--------------------------------\n");// 打印文字
        esc.addText("单号：12345678987445775\n"); // 打印文字
        esc.addText("--------------------------------\n");
        esc.addText("消费总额："+money+"\n");
        esc.addText("--------------------------------\n");
        esc.addText("抵扣方式：100粉币（-10.00）\n");
        esc.addText("实付金额：46.10\n");
        esc.addText("支付方式：微信支付\n");
        esc.addText("会员折扣：8.5\n");
        esc.addText("--------------------------------\n");
        esc.addText("开单时间：2017-07-21 14:23\n");
        esc.addText("收银员：多粉\n");
        esc.addText("--------------------------------\n");
        esc.addText("联系电话：0752-3851585\n");
        esc.addText("地址：惠州市惠城区赛格假日广场1007室\n");
        esc.addText("--------------------------------\n");
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
        esc.addText("欢迎再次光临！\n"); // 打印文字
        esc.addPrintAndFeedLines((byte)5);


        Vector<Byte> datas = esc.getCommand(); // 发送数据
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String sss = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rs;
        try {
            rs = mGpService.sendEscCommand(mPrinterIndex, sss);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rs];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                ToastUtil.getInstance().showToast(GpCom.getErrorText(r));
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void sendReceiptBmp(int i) {
        EscCommand esc = new EscCommand();
        /* 打印图片 */
        esc.addText("Print bitmap!\n"); // 打印文字openPortDialogueClicked
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.keyboard_0);
        esc.addRastBitImage(b, 384, 0); // 打印图片
        esc.addText("第 " + i + " 份\n"); // 打印文字

        Vector<Byte> datas = esc.getCommand(); // 发送数据
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = mGpService.sendEscCommand(mPrinterIndex, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                    ToastUtil.getInstance().showToast(GpCom.getErrorText(r));
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
