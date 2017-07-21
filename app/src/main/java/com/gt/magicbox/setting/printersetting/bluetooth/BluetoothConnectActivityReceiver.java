package com.gt.magicbox.setting.printersetting.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gt.magicbox.utils.commonutil.BluetoothUtil;

/**
 * Created by wzb on 2017/7/21 0021.
 */

public class BluetoothConnectActivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.bluetooth.device.action.PAIRING_REQUEST")) {
            BluetoothDevice mBluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (mBluetoothDevice.getType()==3){  //打印机type
                try {
                    abortBroadcast();//如果没有将广播终止，则会出现一个一闪而过的配对框。
                    //3.调用setPin方法进行配对...
                    boolean ret = BluetoothUtil.setPin(mBluetoothDevice.getClass(), mBluetoothDevice, "你需要设置的PIN码");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
