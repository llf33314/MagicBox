package com.gt.magicbox.custom_display;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gt.magicbox.R;

/**
 * Description:
 * Created by jack-lin on 2017/11/21 0021.
 * Buddha bless, never BUG!
 */

public class Step01Fragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.step_01,container,false);
    }
}
