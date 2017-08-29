package com.gt.magicbox.setting.printersetting.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.OnRecyclerViewItemClickListener;
import com.gt.magicbox.setting.printersetting.PrinterConnectService;
import com.gt.magicbox.utils.RxBus;
import com.gt.magicbox.utils.commonutil.BluetoothUtil;
import com.gt.magicbox.utils.commonutil.ToastUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wzb on 2017/7/20 0020.
 */

public class BluetoothSettingActivity extends BaseActivity {
    private final String TAG=BluetoothSettingActivity.class.getSimpleName();

    @BindView(R.id.rv_bluetooth_bonded)
    RecyclerView rvBluetoothBonded;
    @BindView(R.id.rv_bluetooto_scan_result)
    RecyclerView rvBluetootoScanResult;
    @BindView(R.id.btn_scan_bluetooth)
    Button btnScanBluetooth;
    @BindView(R.id.sw_bluetooth)
    Switch swBluetooth;
    @BindView(R.id.tv_bluetooth_switch)
    TextView tvBluetoothSwitch;

    @BindView(R.id.bluetooth_scan_pb)
    ProgressBar scanProgressBar;
    public static final String PIN="0000";

    BluetoothAdapter mBluetoothAdapter;

    BondedRecyclerviewAdapter scanResultAdapter;
    BondedRecyclerviewAdapter mBondedRecyclerviewAdapter;

    List<BluetoothDevice> scanDevices=new ArrayList<>();

    IntentFilter filter;

    private BleBroadcastReceiver mFindBlueToothReceiver;


    private BluetoothSocket mBluetoothSocket;

    private final int REFRESH_BOND=1000;

    private final int REFRESH_BOND_SCAN=1001;

    private final int CONNECTIONED=1002;

    //android 固定的
    private final UUID MY_UUID=UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    //第三方提供的类库用于链接蓝牙
   // private PortParameters mPortParam;

    private Handler refreshBondedHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) { //配对成功
            super.handleMessage(msg);

            switch (msg.what){
                case REFRESH_BOND:
                    refreshBonded();
                    rvBluetoothBonded.requestLayout();
                    rvBluetootoScanResult.requestLayout();
                    break;
                case REFRESH_BOND_SCAN:
                    refreshBonded();
                   // scanResultAdapter.removeDevice((BluetoothDevice) msg.obj);
                    btnScanBluetooth.performClick();
                    rvBluetoothBonded.requestLayout();
                    break;
                case CONNECTIONED:
                    refreshBonded();
                    rvBluetoothBonded.requestLayout();
                   // scanResultAdapter.removeDevice((BluetoothDevice) msg.obj);
                    btnScanBluetooth.performClick();
                    break;

            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_setting);
        setToolBarTitle("蓝牙设置");
        init();
    }

    private void init() {
        rvBluetoothBonded.setLayoutManager(new LinearLayoutManager(this));
        rvBluetoothBonded.setHasFixedSize(true);
        rvBluetootoScanResult.setLayoutManager(new LinearLayoutManager(this));
        rvBluetootoScanResult.setHasFixedSize(true);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter.isEnabled()){
            swBluetooth.setChecked(true);
            tvBluetoothSwitch.setText("开启");

        }else{
            swBluetooth.setChecked(false);
            tvBluetoothSwitch.setText("关闭");
        }


        swBluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (!mBluetoothAdapter.isEnabled()){
                        mBluetoothAdapter.enable();
                    }
                }else{
                   if (mBluetoothAdapter.isDiscovering()){
                        mBluetoothAdapter.cancelDiscovery();
                    }
                    mBluetoothAdapter.disable();
                }
            }
        });

       // mPortParam = new PortParameters();
       // mPortParam.setPortType(PortParameters.BLUETOOTH);
        if (mBluetoothAdapter == null) {
            new AlertDialog.Builder(this)
                    .setMessage("设备蓝牙模块异常")
                    .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                           finish();
                     }
                   }).show();

        } else {
            if (mBluetoothAdapter.isEnabled()) {
                initBluetoothUi();
            }
         }
        regiesterBroad();
    }

    private void initBluetoothUi(){
        //已配对的蓝牙
        refreshBonded();

        rvBluetootoScanResult.setAdapter(scanResultAdapter=new BondedRecyclerviewAdapter(scanDevices));

        //点击断开连接
        mBondedRecyclerviewAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view) {
                int position=rvBluetoothBonded.getChildLayoutPosition(view);
                BluetoothDevice bluetoothDevice=mBondedRecyclerviewAdapter.getBluetoothDevice(position);
                try {
                    if (removeBond(BluetoothDevice.class,bluetoothDevice)){
                     //   ToastUtil.getInstance().showToast("蓝牙已断开");
                        //改为用广播更新Ui
                       /* refreshBondedHandler.postDelayed(new Runnable() {//不加延迟数据无法刷新 机制问题
                            @Override
                            public void run() {
                                refreshBondedHandler.sendEmptyMessage(REFRESH_BOND);
                            }
                        },300);*/

                       /* ViewGroup viewGroup= (ViewGroup) view;
                        TextView connectState= (TextView) viewGroup.findViewById(R.id.tv_item_bluetooth_conncetion_state);
                        connectState.setText("断开中...");*/

                    }else{
                        ToastUtil.getInstance().showToast("取消失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtil.getInstance().showToast("取消失败");
                }
            }

            @Override
            public void onItemLongClick(View view) {

            }
        });

        scanResultAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view) {

                String mac=scanResultAdapter.getMacAddress(rvBluetootoScanResult.getChildLayoutPosition(view));
                if (TextUtils.isEmpty(mac)){
                    ToastUtil.getInstance().showToast("目标蓝牙地址为空，无法连接");
                    return;
                }
                BluetoothDevice remoteDevice=mBluetoothAdapter.getRemoteDevice(mac);
                new Thread(new ConnectThread(remoteDevice)).start();
                mBluetoothAdapter.cancelDiscovery();
                //改用广播更新UI
                /*ViewGroup viewGroup= (ViewGroup) view;
                TextView connectState= (TextView) viewGroup.findViewById(R.id.tv_item_bluetooth_conncetion_state);
                connectState.setText("正在配对...");*/
            }

            @Override
            public void onItemLongClick(View view) {

            }
        });

    }

    private void regiesterBroad(){
            mFindBlueToothReceiver=new BleBroadcastReceiver();
            filter= new IntentFilter();
            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
            filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
            //   BOND_NONE = 10; //配对没有成功
            // BOND_BONDING = 11; //配对中
            // BOND_BONDED = 12; //配对成功
            filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED );
            //蓝牙连接状态发生改变广播   这条没用的？
            filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
            this.registerReceiver(mFindBlueToothReceiver, filter);
    }

     private boolean removeBond(Class btClass, BluetoothDevice device) throws Exception {
         Method removeBondMethod = btClass.getMethod("removeBond");
         Boolean returnValue = (Boolean) removeBondMethod.invoke(device);
         return returnValue.booleanValue();
     }

    private void refreshBonded(){
        Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();
        List<BluetoothDevice> devices=new ArrayList<BluetoothDevice>(bondedDevices);
        if (mBondedRecyclerviewAdapter==null){//第一次刷新界面
            rvBluetoothBonded.setAdapter(mBondedRecyclerviewAdapter=new BondedRecyclerviewAdapter(devices));
            return;
        }
       // ToastUtil.getInstance().showToast(+devices.size()+"");
        mBondedRecyclerviewAdapter.notifiDevice(devices);
        rvBluetoothBonded.requestLayout();
    }

    private class ConnectThread implements Runnable {
        private BluetoothDevice device;
        public ConnectThread(BluetoothDevice device) {
            this.device=device;
        }

        @Override
        public void run() {
            //未知原因connect报异常
            try {
                //device.getType();手机是1  打印机是3
                if (device.getType()==1){

                }else{

                  //  boolean ret = BluetoothUtil.setPin(BluetoothDevice.class, device, PIN);
                }
               if (!BluetoothUtil.createBond(BluetoothDevice.class,device)){ //这种方式连接失败
                   mBluetoothSocket=device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                   mBluetoothSocket.connect();
               }
               //ToastUtil.getInstance().showToast("ret:"+ret+"\n"+device.getType()+"\n"+ device.getUuids());
               // sendRefreshUi(device);
            } catch (IOException connectException) {
                //处理连接建立失败的异常
                try {
                    mBluetoothSocket= (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
                    mBluetoothSocket.connect();
                  //  sendRefreshUi(device);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }  catch (IOException e) {
                  //  sendRefreshUi(device);
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //  doSomething(mmSocket);
        }

        /*//关闭一个正在进行的连接
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFindBlueToothReceiver!=null){
            this.unregisterReceiver(mFindBlueToothReceiver);
        }
        if (mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    /**
     * 连接成功后需要去除UI
     * @param
     */
    private void sendRefreshUi(final BluetoothDevice d){
        refreshBondedHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Message msg=new Message();
                msg.what=REFRESH_BOND_SCAN;
                msg.obj=d;
                refreshBondedHandler.sendMessage(msg);
            }
        },300);
    }
    @OnClick(R.id.btn_scan_bluetooth)
    public void onViewClicked(View v) {

        if (!mBluetoothAdapter.isEnabled()){
            ToastUtil.getInstance().showToast("请开启蓝牙后再扫描");
            return;
        }

        scanResultAdapter.clearItem();
        // If we're already discovering, stop it
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        // Request discover from BluetoothAdapter
        scanProgressBar.setVisibility(View.VISIBLE);
        mBluetoothAdapter.startDiscovery();
    }


    //wzb 开启蓝牙扫描后 会返回执行这条广播

    private class BleBroadcastReceiver  extends  BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //蓝牙打开关闭
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,BluetoothAdapter.STATE_OFF);
                switch (state){
                    case BluetoothAdapter.STATE_ON:
                        tvBluetoothSwitch.setText("开启");
                        swBluetooth.setEnabled(true);
                        //初始化ui界面
                        initBluetoothUi();
                        //扫描蓝牙
                        onViewClicked(btnScanBluetooth);
                        //打开端口连接
                        Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();
                        Iterator<BluetoothDevice> i= bondedDevices.iterator();
                        while (i.hasNext()){
                            BluetoothDevice d=i.next();
                            if (d.getType()==BluetoothDevice.DEVICE_TYPE_DUAL){//打印机
                                //打开端口
                                OpenPrinterPortMsg rxMsg=new OpenPrinterPortMsg(OpenPrinterPortMsg.OPEN_PROT);
                                rxMsg.setBluetoothDevice(d);
                                RxBus.get().post(rxMsg);
                            }
                        }

                        break;
                    case BluetoothAdapter.STATE_OFF:
                        tvBluetoothSwitch.setText("关闭");
                        swBluetooth.setEnabled(true);
                        if (mBondedRecyclerviewAdapter!=null){
                            mBondedRecyclerviewAdapter.clearItem();
                            rvBluetoothBonded.requestLayout();
                        }
                        if (scanResultAdapter!=null){
                            scanResultAdapter.clearItem();
                        }
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF :
                        tvBluetoothSwitch.setText("关闭中...");
                        swBluetooth.setEnabled(false);
                        //关闭端口
                        RxBus.get().post(new OpenPrinterPortMsg(OpenPrinterPortMsg.CLOSE_PROT));
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON :
                        tvBluetoothSwitch.setText("开启中...");
                        swBluetooth.setEnabled(false);
                        break;
                }
            }else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed
                // already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED&&device.getBondState()!=BluetoothDevice.BOND_BONDING) {
                    scanResultAdapter.addItem(device);
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                scanProgressBar.setVisibility(View.INVISIBLE);
                //ToastUtil.getInstance().showToast("扫描完成");
            }else if ( BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)||
                BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)){//连接上蓝牙或者断开蓝牙
               // refreshBondedHandler.sendEmptyMessage(CONNECTIONED);
            }else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){//配对中
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                switch (intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)) {

                    case BluetoothDevice.BOND_BONDING:
                       // Log.i(TAG,"BOND_BONDING");
                        scanResultAdapter.updateStateStr(device);
                        RxBus.get().post(new OpenPrinterPortMsg(OpenPrinterPortMsg.CLOSE_PROT));
                        break;
                    case BluetoothDevice.BOND_NONE:
                        //Log.i(TAG,"BOND_NONE");
                        refreshBondedHandler.sendEmptyMessage(CONNECTIONED);
                        RxBus.get().post(new OpenPrinterPortMsg(OpenPrinterPortMsg.CLOSE_PROT));
                        break;
                    case BluetoothDevice.BOND_BONDED:
                      //  Log.i(TAG,"BOND_BONDED");
                        refreshBondedHandler.sendEmptyMessage(CONNECTIONED);
                        //打开端口
                        OpenPrinterPortMsg rxMsg=new OpenPrinterPortMsg(OpenPrinterPortMsg.OPEN_PROT);
                        rxMsg.setBluetoothDevice(device);
                        RxBus.get().post(rxMsg);
                        break;
                }
            }
        }
    }


}
