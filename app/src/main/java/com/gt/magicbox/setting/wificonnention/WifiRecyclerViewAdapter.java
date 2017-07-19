package com.gt.magicbox.setting.wificonnention;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.setting.wificonnention.model.WifiBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wzb on 2017/7/17 0017.
 */

public class WifiRecyclerViewAdapter extends RecyclerView.Adapter<WifiRecyclerViewAdapter.ViewHolder> implements View.OnClickListener,View.OnLongClickListener{

    private List<WifiBean> wifiList;

    private final int [] WIFI_SIGN_IOCN={R.drawable.wifi_sign_1,R.drawable.wifi_sign_2,R.drawable.wifi_sign_3,R.drawable.wifi_sign_4};

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

    //自定义监听事件
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view);
        void onItemLongClick(View view);
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public WifiRecyclerViewAdapter(List<WifiBean> wifiList){
        if (wifiList==null){
            this.wifiList=new ArrayList<WifiBean>();
        }else{
            this.wifiList=wifiList;
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wifi_connection,parent,false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return vh;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WifiBean wifiBean=wifiList.get(position);
        holder.ssidName.setText(wifiBean.getName());
        String connectStateStr="";
        switch (wifiBean.getConnectState()){
            case 0:
                connectStateStr=wifiBean.isSave()?"已保存":"未连接";
                break;
            case 1:
                connectStateStr="已连接";
                break;
            case 2:
                connectStateStr="连接中...";
                break;
        }
        holder.connectionState.setText(connectStateStr);
        if (wifiBean.getLockType()==0){
            holder.lock.setVisibility(View.INVISIBLE);
        }else{
            holder.lock.setVisibility(View.VISIBLE);
        }
        if (wifiBean.getSignLevel()>=1&&wifiBean.getSignLevel()<=4){
            holder.sign.setImageResource(WIFI_SIGN_IOCN[wifiBean.getSignLevel()-1]);
        }

    }

    @Override
    public int getItemCount() {
        return wifiList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
         TextView ssidName;
         TextView connectionState;
         ImageView lock;
         ImageView sign;
        public ViewHolder(View view){
            super(view);
            ssidName = (TextView) view.findViewById(R.id.tv_wifi_ssid);
            connectionState = (TextView) view.findViewById(R.id.tv_wifi_state);
            lock = (ImageView) view.findViewById(R.id.iv_wifi_lock);
            sign = (ImageView) view.findViewById(R.id.iv_wifi_sign);

        }
    }
}
