package com.gt.magicbox.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.MyApplication;
import com.gt.magicbox.bean.UpdateMoreBadgeBean;
import com.gt.magicbox.http.HttpConfig;
import com.gt.magicbox.setting.printersetting.PrinterSettingActivity;
import com.gt.magicbox.setting.wificonnention.WifiConnectionActivity;
import com.gt.magicbox.update.OnTaskFinishListener;
import com.gt.magicbox.update.UpdateManager;
import com.gt.magicbox.utils.RxBus;
import com.gt.magicbox.utils.commonutil.AppManager;
import com.gt.magicbox.utils.commonutil.ToastUtil;
import com.gt.magicbox.widget.HintDismissDialog;
import com.gt.magicbox.widget.ManualDialog;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Description:
 * Created by jack-lin on 2017/7/18 0018.
 */

public class MoreActivity extends BaseActivity {
    private String[] itemNameArray = {"硬件设置", "网络设置", "设备管理", "使用手册", "版本更新", "退出账号"};
    private Integer[] imageResArray = {R.drawable.more_printer_setting, R.drawable.more_network_setting,
            R.drawable.more_devices_setting, R.drawable.more_guide, R.drawable.icon_update,
            R.drawable.more_exit};
    private int[] colorNormalArray = {0xff4db3ff, 0xff47d09c, 0xffff9a54, 0xffb177f2, 0xfffdd451, 0xfffc7473};
    private int[] colorFocusedArray = {0x994db3ff, 0x9947d09c, 0x99ff9a54, 0x99b177f2, 0x99fdd451, 0x99fc7473};
    private ArrayList<GridItem> homeData = new ArrayList<>();
    private GridView home_grid;
    private HomeGridViewAdapter gridViewAdapter;
    private long clickTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setToolBarTitle("更多应用");
        initView();
    }

    private void initView() {
        initViewData();
        home_grid = (GridView) findViewById(R.id.gird);
        gridViewAdapter = new HomeGridViewAdapter(this, R.layout.home_grid_item, homeData, 3);
        home_grid.setAdapter(gridViewAdapter);
        home_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent;
                switch (i) {
                    case 0:
                        intent = new Intent(MoreActivity.this, PrinterSettingActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(MoreActivity.this, DevicesMangerActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        new ManualDialog(MoreActivity.this).show();
                        break;
                    case 4:
                        checkUpdate();
                        break;
                    case 5:
                        showExitDialog();
                        break;
                }
            }
        });

        RxBus.get().toObservable(UpdateMoreBadgeBean.class).subscribe(new Consumer<UpdateMoreBadgeBean>() {
            @Override
            public void accept(@NonNull UpdateMoreBadgeBean updateMoreBadgeBean) throws Exception {
                if (gridViewAdapter != null) {
                    if (updateMoreBadgeBean.getPosition() < 0 || updateMoreBadgeBean.getPosition() > gridViewAdapter.getCount() - 1) {
                        return;
                    }
                    gridViewAdapter.updateBadge(updateMoreBadgeBean);
                }
            }
        });

    }

    private void checkUpdate() {
        if (SystemClock.uptimeMillis() - clickTime < 1500) return;
        clickTime = SystemClock.uptimeMillis();
        UpdateManager updateManager = new UpdateManager(this, HttpConfig.APP_ID, UpdateManager.UPDATE_DIALOG);
        updateManager.requestUpdate();
        updateManager.setOnTaskFinishListener(new OnTaskFinishListener() {
            @Override
            public void onTaskResult(boolean result) {
                if (!result) {
                    ToastUtil.getInstance().showToast("当前已是最新版本");
                }
            }
        });
    }

    private void initViewData() {
        for (int i = 0; i < itemNameArray.length; i++) {
            GridItem item = new GridItem();
            item.setNormalColor(colorNormalArray[i]);
            item.setFocusedColor(colorFocusedArray[i]);
            item.setImgRes(imageResArray[i]);
            item.setName(itemNameArray[i]);
            //App更新右上角上标提示
            if (i == 4 && MyApplication.isNeedUpdateApp()) {
                item.setMessageCount(1);
            }
            homeData.add(item);
        }
    }

    private void showExitDialog() {
        HintDismissDialog hintDismissDialog = new HintDismissDialog(MoreActivity.this,
                getResources().getString(R.string.confirm_exit))
                .setOkText("确定").setCancelText("取消").setOnOkClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyApplication.logoutDeleteHawk();
                        Intent intent = new Intent(MoreActivity.this, LoadingActivity.class);
                        startActivity(intent);
                    }
                }).setOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
        hintDismissDialog.show();
    }

}
