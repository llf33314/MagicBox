package com.gt.magicbox.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.gt.magicbox.R;
import com.gt.magicbox.main.MoreFunctionDialog;
import com.gt.magicbox.pay.ChosePayModeActivity;
import com.gt.magicbox.pay.PaymentActivity;
import com.gt.magicbox.utils.NetworkUtils;
import com.gt.magicbox.utils.commonutil.ActivityUtils;
import com.gt.magicbox.utils.commonutil.AppManager;
import com.gt.magicbox.utils.commonutil.AppUtils;
import com.gt.magicbox.utils.commonutil.ToastUtil;
import com.gt.magicbox.utils.commonutil.Utils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by wzb on 2017/7/14 0014.
 */

public  class BaseActivity extends RxAppCompatActivity {
    private Toolbar mToolbar;
    private TextView toolBarTitle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // BarUtils.setStatusBarColor(this,getResources().getColor(R.color.toolbarBg));
        super.setContentView(R.layout.toolbar);
        mToolbar= (Toolbar) findViewById(R.id.toolbar);
        toolBarTitle= (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(mToolbar);
        AppManager.getInstance().addActivity(this);

    }

    @Override
    protected void onResume() {
        Log.d("activity","activity="+ ActivityUtils.getTopActivity(this));
        super.onResume();
    }

    @CallSuper
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View activityView= LayoutInflater.from(this).inflate(layoutResID,null,false);
        ViewGroup viewGroup= (ViewGroup) mToolbar.getParent();
        viewGroup.addView(activityView);
        //空出边距给toolbar
        FrameLayout.LayoutParams lp= (FrameLayout.LayoutParams) activityView.getLayoutParams();
        lp.setMargins(0,(int)this.getResources().getDimension(R.dimen.dp_50),0,0);
        ButterKnife.bind(this);
    }

    @CallSuper
    @Override
    public void setContentView(View view) {
        ViewGroup viewGroup= (ViewGroup) mToolbar.getParent();
        viewGroup.addView(view);
        FrameLayout.LayoutParams lp= (FrameLayout.LayoutParams) view.getLayoutParams();
        lp.setMargins(0,(int)this.getResources().getDimension(R.dimen.dp_50),0,0);
        ButterKnife.bind(this);
    }
    public void setToolBarTitle(String title){
        toolBarTitle.setText(title);
    }
    public void goneToolBar(){
        mToolbar.setVisibility(View.GONE);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //ToastUtil.getInstance().showToast(""+keyCode);
        if (((keyCode>=KeyEvent.KEYCODE_NUMPAD_0&&keyCode<=KeyEvent.KEYCODE_NUMPAD_RIGHT_PAREN)
                ||keyCode==KeyEvent.KEYCODE_DEL)
                &&!(ActivityUtils.getTopActivity(BaseActivity.this).contains("PaymentActivity"))
                &&!(ActivityUtils.getTopActivity(BaseActivity.this).contains("LoginActivity"))
                &&!(ActivityUtils.getTopActivity(BaseActivity.this).contains("ChosePayModeActivity"))){
            Intent intent=new Intent(BaseActivity.this,PaymentActivity.class);
            intent.putExtra("type",PaymentActivity.TYPE_INPUT);
            intent.putExtra("keyCode",keyCode);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

}
