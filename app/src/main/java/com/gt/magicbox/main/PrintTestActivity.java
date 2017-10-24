package com.gt.magicbox.main;

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
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.command.LabelCommand;
import com.gprinter.io.GpDevice;
import com.gprinter.io.PortParameters;
import com.gprinter.service.GpPrintService;
import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.gt.magicbox.utils.commonutil.ToastUtil;

import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wzb on 2017/7/21 0021.
 */

public class PrintTestActivity extends BaseActivity {
    @BindView(R.id.btn_print_test)
    Button btnPrintTest;
    @BindView(R.id.btn_print_open_prot)
    Button btnPrintOpenProt;


    BluetoothAdapter mBluetoothAdapter ;

    private GpService mGpService = null;
    public static final String CONNECT_STATUS = "connect.status";
    private static final String DEBUG_TAG = "MainActivity";
    private PrinterServiceConnection conn = null;
    private int mPrinterIndex = 0;
    private int mTotalCopies = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_test);
        setToolBarTitle("打印测试");
        mBluetoothAdapter =BluetoothAdapter.getDefaultAdapter();
      //  mBluetoothAdapter.getProfileProxy()
       // connection();
      //  registerBoothCloseBroadcast();
        // getPrinterStatusClicked();
    }

    private void connection() {
        conn = new PrinterServiceConnection();
        Intent intent = new Intent(this, GpPrintService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService
    }

    @OnClick(R.id.btn_print_test)
    public void onViewClicked(View v) {
        printReceiptClicked(v);
    }

    @OnClick(R.id.btn_print_open_prot)
    public void onViewClicked() {
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
            ToastUtil.getInstance().showToast("未连接蓝牙打印机");
            return;
        }
        try {
            int rel = mGpService.openPort(mPrinterIndex, PortParameters.BLUETOOTH ,printDevice.getAddress(),0);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            ToastUtil.getInstance().showToast("result :" + String.valueOf(r));
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
            LogUtils.i("ServiceConnection", "onServiceDisconnected() called");
            mGpService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService = GpService.Stub.asInterface(service);
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


    public void printReceiptClicked(View view) {
        try {
            int type = mGpService.getPrinterCommandType(mPrinterIndex);
            if (type == GpCom.ESC_COMMAND) {
                sendReceipt();
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

    void sendReceipt() {
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
        esc.addText("消费总额：60.00\n");
        esc.addText("--------------------------------\n");
        esc.addText("会员折扣：8.5\n");
        esc.addText("抵扣方式：100粉币（-10.00）\n");
        esc.addText("实付金额：46.10\n");
        esc.addText("支付方式：微信支付\n");
        esc.addText("会员折扣：8.5\n");
        esc.addText("--------------------------------\n");
        esc.addText("开单时间：2017-07-21 14:23\n");
        esc.addText("收银员：\n");
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
