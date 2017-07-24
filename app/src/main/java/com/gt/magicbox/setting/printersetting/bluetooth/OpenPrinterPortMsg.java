package com.gt.magicbox.setting.printersetting.bluetooth;

/**
 * Created by wzb on 2017/7/24 0024.
 */

public class OpenPrinterPortMsg {
    public static final int CLOSE_PROT=0;
    public static final int OPEN_PROT=1;

    private int bluetoothState;
    public OpenPrinterPortMsg(int state){
        bluetoothState=state;
    }

    public int getBluetoothState() {
        return bluetoothState;
    }

    public void setBluetoothState(int bluetoothState) {
        this.bluetoothState = bluetoothState;
    }
}
