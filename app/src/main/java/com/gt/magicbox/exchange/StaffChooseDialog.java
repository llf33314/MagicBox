package com.gt.magicbox.exchange;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.gt.magicbox.R;
import com.gt.magicbox.base.MyApplication;
import com.gt.magicbox.bean.ShopInfoBean;
import com.gt.magicbox.bean.StaffBean;
import com.gt.magicbox.bean.StartWorkBean;
import com.gt.magicbox.http.BaseResponse;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.DialogTransformer;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.setting.printersetting.PrinterSettingActivity;
import com.gt.magicbox.utils.DialogUtil;
import com.gt.magicbox.utils.commonutil.AppManager;
import com.gt.magicbox.widget.HintDismissDialog;
import com.orhanobut.hawk.Hawk;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wzb on 2017/9/27 0027.
 */

public class StaffChooseDialog extends Dialog{

    private List<StaffBean.StaffListBean> staffList;

    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.confirm)
    TextView confirm;
    @BindView(R.id.wheelView)
    WheelView wheelView;
    @BindView(R.id.staff_dialog_viewswitch)
    ViewSwitcher statffVs;
    @BindView(R.id.staff_name_ll)
    LinearLayout nameLl;
    @BindView(R.id.staff_dialog_choose_name)
    TextView staffName;


    List<String> namsList;
    private WheelView.WheelViewStyle mStyle;

    private View.OnClickListener onClickListener;


    public StaffChooseDialog(@NonNull Context context, @StyleRes int themeResId, List<StaffBean.StaffListBean> staffList) {
        super(context, themeResId);
        this.staffList=staffList;
        namsList=getNameList(staffList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_staff_choose);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        DialogUtil.setBottom(this);
        wheelView.setSkin(WheelView.Skin.None);
        wheelView.setWheelAdapter(new ArrayWheelAdapter(getContext()));
        mStyle = new WheelView.WheelViewStyle();
        mStyle.textColor = Color.GRAY;
        mStyle.selectedTextZoom = 1.2f;
        wheelView.setStyle(mStyle);

        if (namsList!=null&&namsList.size()>0){
            wheelView.setWheelData(namsList);
        }

        wheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener(){

                @Override
                public void onItemSelected(int i, Object o) {
                    staffName.setText(o.toString());
                }
            });

    }

    @OnClick({R.id.cancel,R.id.confirm,R.id.staff_name_ll,R.id.staff_dialog_start})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.cancel:
                StaffChooseDialog.this.dismiss();
                break;
            case R.id.confirm:
                statffVs.setDisplayedChild(1);
                break;
            case R.id.staff_name_ll:
                statffVs.setDisplayedChild(0);
                break;
            case R.id.staff_dialog_start:
                String name=staffName.getText().toString();

                final StaffBean.StaffListBean staff=getStaff(staffList,name);
                ShopInfoBean shopInfoBean=Hawk.get("ShopInfoBean");
                HttpCall.getApiService()
                        .cecordsNowExchange( Hawk.get("eqId",0),staff.getShopId(),shopInfoBean.getShopName(),staff.getJobNumber(),staff.getId(),staff.getName())
                        .compose(ResultTransformer.<StartWorkBean>transformer())
                        .compose(new DialogTransformer().<StartWorkBean>transformer())
                        .subscribe(new BaseObserver<StartWorkBean>() {
                            @Override
                            protected void onSuccess(StartWorkBean staffBean) {
                                Hawk.put("shiftId",staffBean.getShiftId());
                                Hawk.put("StaffListBean",staff);
                                StaffChooseDialog.this.dismiss();
                                AppManager.getInstance().finishActivity();
                                showSuccessDialog();
                            }
                        });

                break;

        }
    }
    private static void showSuccessDialog() {
        HintDismissDialog hintDismissDialog = new HintDismissDialog(MyApplication.getAppContext(), "当班成功");
        hintDismissDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        hintDismissDialog.show();
    }
    private int getIdFromStaffList(List<StaffBean.StaffListBean> staffList,String name){
        for (StaffBean.StaffListBean s:staffList){
            if (name.equals(s.getName())){
                return s.getId();
            }
        }
        return -1;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private StaffBean.StaffListBean getStaff(List<StaffBean.StaffListBean> staffList,String staffName){
        for (StaffBean.StaffListBean s:staffList){
          if (staffName.equals(s.getName())){
              return s;
          }
        }
        return new StaffBean.StaffListBean();
    }

    private List<String> getNameList(List<StaffBean.StaffListBean> staffList){
        List<String> nameList=new ArrayList<>();
        for (StaffBean.StaffListBean s:staffList){
            nameList.add(s.getName());
        }
        return nameList;
    }

}
