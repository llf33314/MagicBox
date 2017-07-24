package com.gt.magicbox.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.setting.printersetting.PrinterConnectSerivce;
import com.gt.magicbox.utils.commonutil.AppUtils;
import com.gt.magicbox.utils.commonutil.CameraUtils;
import com.gt.magicbox.utils.commonutil.DeviceUtils;
import com.gt.magicbox.utils.commonutil.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wzb on 2017/7/19 0019.
 */

public class DeviceInfoActivity extends BaseActivity {

    @BindView(R.id.tv_device_num)
    TextView tvDeviceNum;
    @BindView(R.id.tv_device_type)
    TextView tvDeviceType;
    @BindView(R.id.tv_device_version)
    TextView tvDeviceVersion;
    @BindView(R.id.tv_device_printer)
    TextView tvDevicePrinter;
    @BindView(R.id.tv_device_camera)
    TextView tvDeviceCamera;
    @BindView(R.id.tv_device_network)
    TextView tvDeviceNetwork;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        setToolBarTitle("设备状态");
        init();
    }

    private void init(){
        tvDeviceNum.setText("AAAAAA");
        tvDeviceType.setText(DeviceUtils.getModel());
        tvDeviceVersion.setText(AppUtils.getAppVersionName());

        tvDevicePrinter.setText(PrinterConnectSerivce.getPrinterStatus());

        tvDeviceCamera.setText(CameraUtils.hasBackFacingCamera()|CameraUtils.hasFrontFacingCamera()?"正常":"不能使用");
        tvDeviceNetwork.setText(NetworkUtils.isConnected()?"正常":"断开");
    }

}
