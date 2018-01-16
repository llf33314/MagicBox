package com.gt.magicbox.setting.typewriting;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.custom_display.NoScrollViewPager;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.gt.magicbox.utils.commonutil.ToastUtil;

/**
 * Description:
 * Created by jack-lin on 2017/11/21 0021.
 * Buddha bless, never BUG!
 */

@SuppressLint("ValidFragment")
public class TypewritingStep02 extends Fragment {
    private NoScrollViewPager viewPager;
    private Button nextButton;
    private Button settingButton;
    private TextView settingSuccess;
    private String GOOGLE_PINYIN = "com.android.inputmethod.latin/.LatinIME:com.android.inputmethod.pinyin/.PinyinIME";

    @SuppressLint("ValidFragment")
    public TypewritingStep02(NoScrollViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d("fragment", "Step01Fragment onCreate");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.typewriting_step_02, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nextButton = (Button) view.findViewById(R.id.step01_next);
        settingButton = (Button) view.findViewById(R.id.settingButton);
        settingSuccess = (TextView) view.findViewById(R.id.settingSuccess);
        nextButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (nextButton.isEnabled() == false) {
                    ToastUtil.getInstance().showToast(getString(R.string.typewriting01toast));
                }
                return false;
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showInputMethodPicker();
            }
        });
    }

    @Override
    public void onResume() {
        String ss = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.DEFAULT_INPUT_METHOD);
        if (ss.equals(GOOGLE_PINYIN)) {
            nextButton.setEnabled(true);
            settingSuccess.setVisibility(View.VISIBLE);
        } else {
            nextButton.setEnabled(false);
            settingSuccess.setVisibility(View.INVISIBLE);
        }
        LogUtils.d("imm=" + ss);
        super.onResume();
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.d("hidden="+isVisibleToUser);

        if(isVisibleToUser){
            //TODO now visible to user
        } else {
            //TODO now invisible to user
        }
    }

    @Override
    public void onAttach(Context context) {
        LogUtils.d("onAttach=");

        super.onAttach(context);
    }
}
