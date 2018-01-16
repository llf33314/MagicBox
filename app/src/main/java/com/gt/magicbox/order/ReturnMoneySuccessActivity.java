package com.gt.magicbox.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.BaseConstant;
import com.gt.magicbox.bean.OrderListResultBean;
import com.gt.magicbox.bean.UpdateOrderListUIBean;
import com.gt.magicbox.utils.RxBus;
import com.gt.magicbox.utils.commonutil.AppManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:
 * Created by jack-lin on 2018/1/16 0016.
 * Buddha bless, never BUG!
 */

public class ReturnMoneySuccessActivity extends BaseActivity {
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.returnWay)
    TextView returnWay;
    @BindView(R.id.step02_next)
    Button step02Next;
    private double returnMoney;
    private int returnType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_success);
        initView();
    }

    private void initView() {
        returnMoney = getIntent().getDoubleExtra("returnMoney", 0);
        returnType = getIntent().getIntExtra("returnType", 0);
        if (returnType < BaseConstant.PAY_TYPE.length) {
            returnWay.setText("退款方式:"+BaseConstant.PAY_TYPE[returnType]);
        }
        money.setText("¥" + returnMoney);
    }

    @OnClick(R.id.step02_next)
    public void onViewClicked() {
        RxBus.get().post(new UpdateOrderListUIBean(1));
        AppManager.getInstance().finishActivity();
    }
}
