package com.gt.magicbox.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.login.LoginActivity;
import com.gt.magicbox.main.MainActivity;
import com.gt.magicbox.main.menu.ShortcutMenuDialog;
import com.gt.magicbox.pay.PaymentActivity;
import com.gt.magicbox.utils.commonutil.ActivityUtils;
import com.gt.magicbox.utils.commonutil.AppManager;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.orhanobut.hawk.Hawk;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;

import static com.gt.magicbox.base.BaseConstant.clickTime;

/**
 * Created by wzb on 2017/7/14 0014.
 */

public class BaseActivity extends RxAppCompatActivity {
    private RelativeLayout mToolbar;
    private TextView toolBarTitle;
    private ImageView toolBarBack;
    private ImageView search;
    private ShortcutMenuDialog shortcutMenuDialog;
    private View activityView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // BarUtils.setStatusBarColor(this,getResources().getColor(R.color.toolbarBg));
        super.setContentView(R.layout.toolbar);
        init();
    }

    private void init() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mToolbar = (RelativeLayout) findViewById(R.id.base_toolbar);
        toolBarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolBarBack = (ImageView) findViewById(R.id.toolbar_back);
        search=(ImageView) findViewById(R.id.search);
        AppManager.getInstance().addActivity(this);
        toolBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void goneBack() {
        toolBarBack.setVisibility(View.GONE);
    }

    @CallSuper
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        activityView = LayoutInflater.from(this).inflate(layoutResID, null, false);
        ViewGroup viewGroup = (ViewGroup) mToolbar.getParent();
        viewGroup.addView(activityView);
        //空出边距给toolbar
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) activityView.getLayoutParams();
        lp.setMargins(0, (int) this.getResources().getDimension(R.dimen.toolbar_height), 0, 0);
        ButterKnife.bind(this);
    }

    @CallSuper
    @Override
    public void setContentView(View view) {
        ViewGroup viewGroup = (ViewGroup) mToolbar.getParent();
        viewGroup.addView(view);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();
        lp.setMargins(0, (int) this.getResources().getDimension(R.dimen.toolbar_height), 0, 0);
        ButterKnife.bind(this);
    }

    public void setToolBarTitle(String title) {
        toolBarTitle.setText(title);
    }

    public void goneToolBar() {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) activityView.getLayoutParams();
        lp.setMargins(0, 0, 0, 0);
        activityView.setLayoutParams(lp);
        mToolbar.setVisibility(View.GONE);
    }

    public void setBackOnClickListener(View.OnClickListener l){
        toolBarBack.setOnClickListener(l);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtils.d("keyCode", "BaseActivity keyCode --> " + keyCode);

        if (((keyCode >= KeyEvent.KEYCODE_NUMPAD_0 && keyCode <= KeyEvent.KEYCODE_NUMPAD_RIGHT_PAREN))
                && !(ActivityUtils.getTopActivity(BaseActivity.this).contains("PaymentActivity"))
                && !(ActivityUtils.getTopActivity(BaseActivity.this).contains("LoginActivity"))
                && !(ActivityUtils.getTopActivity(BaseActivity.this).contains("ChosePayModeActivity"))) {
            Intent intent = new Intent(BaseActivity.this, PaymentActivity.class);
            intent.putExtra("type", PaymentActivity.TYPE_INPUT);
            intent.putExtra("keyCode", keyCode);
            startActivity(intent);
        } else if (keyCode == 250) {
            if (Hawk.get("isLogin",false)){
                Intent intent = new Intent(BaseActivity.this, MainActivity.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            return false;
        } else if (keyCode == 251) {
            if (shortcutMenuDialog == null)
                shortcutMenuDialog = new ShortcutMenuDialog(this, R.style.ShortcutMenuDialog);

            shortcutMenuDialog.show();

            return false;
        } else if (keyCode == KeyEvent.KEYCODE_BACK ) {
            if ( (ActivityUtils.getTopActivity(BaseActivity.this).contains("MainActivity"))
                    || (ActivityUtils.getTopActivity(BaseActivity.this).contains("LoginActivity")))
            return false;
            else {
                if (SystemClock.uptimeMillis()-clickTime<500){
                    return false;
                }
                clickTime= SystemClock.uptimeMillis();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    public ImageView getSearch() {
        return search;
    }
}
