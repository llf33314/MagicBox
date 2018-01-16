package com.gt.magicbox.setting;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.http.BaseResponse;
import com.gt.magicbox.http.retrofit.HttpCall;
import com.gt.magicbox.http.rxjava.observable.ResultTransformer;
import com.gt.magicbox.http.rxjava.observer.BaseObserver;
import com.gt.magicbox.pay.QRCodePayActivity;
import com.gt.magicbox.setting.printersetting.PrinterConnectService;
import com.gt.magicbox.utils.commonutil.AppUtils;
import com.gt.magicbox.utils.commonutil.CameraUtils;
import com.gt.magicbox.utils.commonutil.DeviceUtils;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.gt.magicbox.utils.commonutil.NetworkUtils;
import com.gt.magicbox.utils.commonutil.PhoneUtils;
import com.gt.magicbox.utils.commonutil.ToastUtil;
import com.gt.magicbox.widget.ChangeDeviceNameDialog;
import com.gt.magicbox.widget.HintDismissDialog;
import com.gt.magicbox.widget.LoadingProgressDialog;
import com.orhanobut.hawk.Hawk;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wzb on 2017/7/19 0019.
 */

public class DeviceInfoActivity extends BaseActivity {
    public static final String TAG = DeviceInfoActivity.class.getSimpleName();
    @BindView(R.id.tv_device_num)
    TextView tvDeviceNum;
    @BindView(R.id.tv_device_type)
    TextView tvDeviceType;
    @BindView(R.id.tv_device_version)
    TextView tvDeviceVersion;
    @BindView(R.id.tv_device_printer)
    TextView tvDevicePrinter;
    @BindView(R.id.tv_device_network)
    TextView tvDeviceNetwork;
    @BindView(R.id.tv_device_camera)
    TextView tvDeviceCamera;
    @BindView(R.id.camera_layout)
    FrameLayout cameraLayout;
    @BindView(R.id.tv_device_name)
    TextView tvDeviceName;
    @BindView(R.id.device_info_list)
    LinearLayout deviceInfoList;
    private ChangeDeviceNameDialog deviceNameDialog;
    private String updateName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        ButterKnife.bind(this);
        setToolBarTitle("设备状态");
        init();
    }

    private void init() {
        tvDeviceNum.setText(PhoneUtils.getIMEI());
        tvDeviceType.setText(DeviceUtils.getModel());
        tvDeviceVersion.setText(AppUtils.getAppVersionName());

        tvDevicePrinter.setText(PrinterConnectService.getPrinterStatus());

        tvDeviceCamera.setText(CameraUtils.hasBackFacingCamera() | CameraUtils.hasFrontFacingCamera() ? "正常" : "不能使用");
        tvDeviceNetwork.setText(NetworkUtils.isConnected() ? "正常" : "断开");
        String deviceName = Hawk.get("deviceName", "");
        if (!TextUtils.isEmpty(deviceName)) {
            tvDeviceName.setText(deviceName);
        } else {
            tvDeviceName.setText(R.string.not_set);
        }
    }


    @OnClick({R.id.tv_device_network, R.id.deviceNameLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_device_network:
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                break;
            case R.id.deviceNameLayout:
                if (deviceNameDialog == null) {
                    deviceNameDialog = new ChangeDeviceNameDialog(DeviceInfoActivity.this);
                }
                deviceNameDialog.show();
                deviceNameDialog.confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (deviceNameDialog != null && deviceNameDialog.nameEdit != null) {
                            String name = deviceNameDialog.nameEdit.getEditableText().toString();
                            if (!TextUtils.isEmpty(name)) {
                                updateName = name;
                                revampEqName(name);
                                if (deviceNameDialog != null) {
                                    deviceNameDialog.dismiss();
                                }
                            } else {
                                ToastUtil.getInstance().showToast(getString(R.string.cannot_input_nothing));
                            }

                        }

                    }
                });
                String deviceName = Hawk.get("deviceName", "");
                deviceNameDialog.nameEdit.setText(deviceName);
                break;
        }

    }

    private void revampEqName(String name) {
        final LoadingProgressDialog dialog = new LoadingProgressDialog(DeviceInfoActivity.this, getString(R.string.saving));
        dialog.show();
        HttpCall.getApiService()
                .revampEqName(name, Hawk.get("eqId", 0))
                .compose(ResultTransformer.<BaseResponse>transformerNoData())//线程处理 预处理
                .subscribe(new BaseObserver<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse data) {
                        LogUtils.d(TAG, "revampEqName onSuccess ");
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        if (data != null && data.getCode() == 0) {
                            Hawk.put("deviceName", updateName);
                            ToastUtil.getInstance().showToast("修改成功");
                            tvDeviceName.setText(updateName);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.d(TAG, "revampEqName onError e" + e.getMessage());
                        if (dialog != null) {
                            dialog.dismiss();
                        }

                        super.onError(e);
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                        LogUtils.d(TAG, "getOrderStatus onFailure msg=" + msg);
                        super.onFailure(code, msg);
                    }
                });
    }
}
