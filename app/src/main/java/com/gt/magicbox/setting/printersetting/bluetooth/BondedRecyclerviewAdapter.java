package com.gt.magicbox.setting.printersetting.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.OnRecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wzb on 2017/7/20 0020.
 */

public class BondedRecyclerviewAdapter extends RecyclerView.Adapter<BondedRecyclerviewAdapter.ViewHolder> implements View.OnClickListener,View.OnLongClickListener{

    private List<BluetoothDevice> devices;

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemClickListener!= null) {
            mOnItemClickListener.onItemLongClick(v);
        }
        return false;
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public BondedRecyclerviewAdapter(List<BluetoothDevice> devices){
        if (devices==null){
            this.devices=new ArrayList<BluetoothDevice>();
        }else{
            this.devices=devices;

        }
    }

    public void notifiDevice(List<BluetoothDevice> devices){
        clearItem();
        for (BluetoothDevice b:devices){
            addItem(b);
        }
    }

    public void addItem(BluetoothDevice device){
        for (BluetoothDevice d:devices){//去除重复的蓝牙
            if (d.getAddress().equals(device.getAddress())){
                return;
            }
        }
        this.devices.add(device);
        notifyItemInserted(devices.size());
    }

    public void clearItem(){
        notifyItemRangeRemoved(0,devices.size()>0?devices.size():0);
        this.devices.clear();
    }

    public void updateStateStr(BluetoothDevice bluetoothDevice){
        int position=-1;
        for (int i=0;i<devices.size();i++){
         if(devices.get(i).getAddress().equals(bluetoothDevice.getAddress())){
             position=i;
             devices.set(position,bluetoothDevice);
             break;
         }
        }
        if (position!=-1){
            notifyItemChanged(position);
        }

    }

    public void removeDevice(BluetoothDevice device){
        for (int i=0;i<devices.size();i++){
           if(device.getAddress().equals(devices.get(i).getAddress())) {
               notifyItemRemoved(i);
            }
            break;
        }
    }

    public String getMacAddress(int position){
        return devices.get(position).getAddress();
    }
    public BluetoothDevice getBluetoothDevice(int position){
        return  devices.get(position);
    }

    @Override
    public BondedRecyclerviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bluetooth_info,parent,false);
        BondedRecyclerviewAdapter.ViewHolder vh = new BondedRecyclerviewAdapter.ViewHolder(view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(BondedRecyclerviewAdapter.ViewHolder holder, int position) {
        BluetoothDevice bluetoothDevice=devices.get(position);

        holder.name.setText(bluetoothDevice.getName());

        switch ( bluetoothDevice.getBondState()){
            case BluetoothDevice.BOND_NONE://蓝牙未打开？
                holder.connectionState.setText("未配对");
                break;
            case BluetoothDevice.BOND_BONDING://连接中？
                holder.connectionState.setText("正在配对....");
                break;
            case BluetoothDevice.BOND_BONDED://已配对？
                holder.connectionState.setText("已连接，点击可断开");
                break;
        }

    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView connectionState;
        public ViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.tv_item_bluetooth_name);
            connectionState = (TextView) view.findViewById(R.id.tv_item_bluetooth_conncetion_state);
        }
    }
}