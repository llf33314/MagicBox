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
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.OnRecyclerViewItemClickListener;
import com.gt.magicbox.utils.commonutil.ToastUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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

    @BindView(R.id.bluetooth_scan_pb)
    ProgressBar scanProgressBar;

    BluetoothAdapter mBluetoothAdapter;

    BondedRecyclerviewAdapter scanResultAdapter;
    BondedRecyclerviewAdapter mBondedRecyclerviewAdapter;

    List<BluetoothDevice> scanDevices=new ArrayList<>();

    IntentFilter filter;
    private BleBroadcastReceiver mFindBlueToothReceiver;

    private BluetoothSocket mBluetoothSocket;

    private final int REFRESH_BOND=1000;

    private final int REFRESH_BOND_SCAN=1001;

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
            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
            }
            //已配对的蓝牙
            refreshBonded();


        rvBluetootoScanResult.setAdapter(scanResultAdapter=new BondedRecyclerviewAdapter(scanDevices));
        scanResultAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view) {
                mBluetoothAdapter.cancelDiscovery();
                String mac=scanResultAdapter.getMacAddress(rvBluetootoScanResult.getChildLayoutPosition(view));
                if (TextUtils.isEmpty(mac)){
                    ToastUtil.getInstance().showToast("目标蓝牙地址为空，无法连接");
                    return;
                }
                BluetoothDevice remoteDevice=mBluetoothAdapter.getRemoteDevice(mac);
                new Thread(new ConnectThread(remoteDevice)).start();
                ViewGroup viewGroup= (ViewGroup) view;
                TextView connectState= (TextView) viewGroup.findViewById(R.id.tv_item_bluetooth_conncetion_state);
                connectState.setText("正在配对...");
            }

            @Override
            public void onItemLongClick(View view) {

            }
        });


        mBondedRecyclerviewAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view) {
                int position=rvBluetoothBonded.getChildLayoutPosition(view);
                BluetoothDevice bluetoothDevice=mBondedRecyclerviewAdapter.getBluetoothDevice(position);
                try {
                    if (removeBond(BluetoothDevice.class,bluetoothDevice)){
                        refreshBondedHandler.postDelayed(new Runnable() {//不加延迟数据无法刷新 机制问题
                            @Override
                            public void run() {
                                refreshBondedHandler.sendEmptyMessage(REFRESH_BOND);
                            }
                        },300);

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
    }
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
                mBluetoothSocket=device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                mBluetoothSocket.connect();
                sendRefreshUi(device);
            } catch (IOException connectException) {
                //处理连接建立失败的异常
                try {
                    mBluetoothSocket= (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
                    mBluetoothSocket.connect();
                    sendRefreshUi(device);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }  catch (IOException e) {
                    sendRefreshUi(device);
                    e.printStackTrace();
                }
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

        if (mFindBlueToothReceiver==null){ //第一次点击
            mFindBlueToothReceiver=new BleBroadcastReceiver();
            filter= new IntentFilter();
            filter.addAction(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            this.registerReceiver(mFindBlueToothReceiver, filter);
        }else{
            scanResultAdapter.clearItem();
        }
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
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed
                // already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    scanResultAdapter.addItem(device);
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                scanProgressBar.setVisibility(View.INVISIBLE);
              ToastUtil.getInstance().showToast("扫描完成");
            }
        }
    };
}
