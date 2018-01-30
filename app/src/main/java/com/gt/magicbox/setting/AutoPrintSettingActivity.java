package com.gt.magicbox.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.orhanobut.hawk.Hawk;
import com.suke.widget.SwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Description:
 *
 * @author jack-lin
 * @date 2018/1/26 0026
 * Buddha bless, never BUG!
 */

public class AutoPrintSettingActivity extends BaseActivity {
    @BindView(R.id.paySwitchButton)
    SwitchButton paySwitchButton;
    @BindView(R.id.exchangeSwitchButton)
    SwitchButton exchangeSwitchButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_print_setting);
        setToolBarTitle(getString(R.string.auto_print_setting));
        paySwitchButton.setChecked(Hawk.get("payAutoPrint", false));
        exchangeSwitchButton.setChecked(Hawk.get("exchangeAutoPrint", false));
//        paySwitchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
//                boolean payAutoPrint = Hawk.get("payAutoPrint", false);
//                LogUtils.d("setOnCheckedChangeListener  payAutoPrint="+payAutoPrint);
//
//                payAutoPrint = !payAutoPrint;
//                Hawk.put("payAutoPrint", payAutoPrint);
//
//            }
//        });
//        exchangeSwitchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
//                boolean exchangeAutoPrint = Hawk.get("exchangeAutoPrint", false);
//                exchangeAutoPrint = !exchangeAutoPrint;
//                Hawk.put("exchangeAutoPrint", exchangeAutoPrint);
//            }
//        });
    }

    @OnClick({R.id.payPrintSetting, R.id.exchangePrintSetting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.payPrintSetting:
                boolean payAutoPrint = Hawk.get("payAutoPrint", false);
                LogUtils.d("onViewClicked  payAutoPrint="+payAutoPrint);
                payAutoPrint = !payAutoPrint;
                Hawk.put("payAutoPrint", payAutoPrint);
                paySwitchButton.setChecked(payAutoPrint);
                break;
            case R.id.exchangePrintSetting:
                boolean exchangeAutoPrint = Hawk.get("exchangeAutoPrint", false);
                exchangeAutoPrint = !exchangeAutoPrint;
                Hawk.put("exchangeAutoPrint", exchangeAutoPrint);
                exchangeSwitchButton.setChecked(exchangeAutoPrint);
                break;
        }
    }

}
