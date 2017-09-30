package com.gt.magicbox.exchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.bean.StaffBean;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.widget.WheelDialog;
import com.orhanobut.hawk.Hawk;
import com.wx.wheelview.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wzb on 2017/9/25 0025.
 */

public class ExchangeWorkActivity extends BaseActivity {
    public static final String STAFF = "staff";

    private StaffBean staffBean;
    private StaffChooseDialog chooseDialog;
    private String chooseName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_word);
        setToolBarTitle("交班");
        staffBean = (StaffBean) getIntent().getSerializableExtra(STAFF);
    }

    @OnClick(R.id.exchange_change)
    public void onViewClicked() {

        if (Hawk.get("shiftId",0)!=0){//已经选择过员工
            Intent intent=new Intent(this,ShiftExchangeActivity.class);
            this.startActivity(intent);
        }else{
            if (chooseDialog==null){
                chooseDialog=new StaffChooseDialog(this,R.style.HttpRequestDialogStyle,staffBean.getStaffList());
            }
            if (!chooseDialog.isShowing()){
                chooseDialog.show();
            }
        }

    }


}
