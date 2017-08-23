package com.gt.magicbox.setting.printersetting.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by wzb on 2017/8/23 0023.
 */

public class BluetoothUtil {
    /**
     * 没有更好的方法获取当前蓝牙的连接状态了
     * @return
     */
    public static BluetoothDevice getConnectingBluetooth(BluetoothAdapter mBluetoothAdapter){

        if (!mBluetoothAdapter.isEnabled()){
            return null;
        }
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();

        for(BluetoothDevice device : devices){
            //测试用  因为0D30显示的类型不是打印机？
            //只取第一个蓝牙连接
            //过滤掉内置打印机
            if (/*device.getType()==3&&*/!"88:D1:31:71:2D:10".equals(device.getAddress())){
                return device;
            }
        }
        return null;
    }

    public static boolean isHasBluetoothDevice(BluetoothAdapter mBluetoothAdapter){

        if (!mBluetoothAdapter.isEnabled()){
            return false;
        }

        Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();
        List<BluetoothDevice> devices=new ArrayList<BluetoothDevice>(bondedDevices);
        BluetoothDevice printDevice=null;
        for (BluetoothDevice b:devices){
            if (b.getType()==3){
                printDevice=b;
                break;
            }
        }
        return  printDevice!=null;
    }
}
