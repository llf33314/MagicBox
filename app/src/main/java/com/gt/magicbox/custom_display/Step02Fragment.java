package com.gt.magicbox.custom_display;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.recyclerview.BaseRecyclerAdapter;
import com.gt.magicbox.base.recyclerview.SpaceItemDecoration;
import com.gt.magicbox.bean.BaudRateItemBean;
import com.gt.magicbox.bean.DistributeCouponBean;
import com.gt.magicbox.coupon.HorizontalCouponAdapter;
import com.gt.magicbox.utils.RxBus;
import com.gt.magicbox.utils.commonutil.ConvertUtils;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.gt.magicbox.utils.commonutil.ToastUtil;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Description:
 * Created by jack-lin on 2017/11/21 0021.
 * Buddha bless, never BUG!
 */

@SuppressLint("ValidFragment")
public class Step02Fragment extends Fragment {
    private NoScrollViewPager viewPager;
    private Button nextButton;
    private Button baudOther;
    private Button baud2400;
    private TextView baudTextView;
    private RecyclerView baudRecyclerView;
    private RelativeLayout layout2400;
    private RelativeLayout layoutOther;
    private int currentBaudRate = 0;
    private int[] baudRates = new int[]{1200, 4800, 9600, 19200, 38400, 43000};

    @SuppressLint("ValidFragment")
    public Step02Fragment(NoScrollViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d("fragment", "Step02Fragment onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.step_02, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nextButton = (Button) view.findViewById(R.id.step02_next);
        baudOther = (Button) view.findViewById(R.id.baudOther);
        baud2400 = (Button) view.findViewById(R.id.baud2400);
        baudRecyclerView = (RecyclerView) view.findViewById(R.id.baudRecyclerView);
        baudTextView = (TextView) view.findViewById(R.id.selected_baud);
        layout2400 = (RelativeLayout) view.findViewById(R.id.layout2400);
        layoutOther = (RelativeLayout) view.findViewById(R.id.layoutOther);

        initRecyclerView(baudRecyclerView);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentBaudRate > 0) {
                    viewPager.setCurrentItem(2);
                    BaudRateItemBean baudRateItemBean = new BaudRateItemBean();
                    baudRateItemBean.baudRate = currentBaudRate;
                    RxBus.get().post(baudRateItemBean);
                } else {
                    ToastUtil.getInstance().showToast("请先选择一个波特率");
                }
            }
        });
        baudOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBaudRate = 0;
                layout2400.setVisibility(View.GONE);
                layoutOther.setVisibility(View.VISIBLE);
            }
        });
        baud2400.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) return true;
                if (event.getAction() != MotionEvent.ACTION_UP) return false;

                currentBaudRate = 2400;
                baud2400.setPressed(true);
                Hawk.put("debugBaudRate", currentBaudRate);
                return true;
            }
        });
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);
        final ArrayList<BaudRateItemBean> list = initRecyclerViewData();
        final BaudRecyclerViewAdapter adapter = new BaudRecyclerViewAdapter(getActivity(), list);
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, Object item, int position) {
                for (BaudRateItemBean bean : list) {
                    bean.isSelected = false;
                }
                list.get(position).isSelected = true;
                baudTextView.setText("" + list.get(position).baudRate);
                currentBaudRate = list.get(position).baudRate;
                Hawk.put("debugBaudRate", currentBaudRate);
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(ConvertUtils.dp2px(5), SpaceItemDecoration.SPACE_RIGHT));

    }

    private ArrayList<BaudRateItemBean> initRecyclerViewData() {
        ArrayList<BaudRateItemBean> baudRateItemBeans = new ArrayList<>();
        for (int i = 0; i < baudRates.length; i++) {
            BaudRateItemBean baudRateItemBean = new BaudRateItemBean();
            baudRateItemBean.baudRate = baudRates[i];
            baudRateItemBean.isSelected = false;
            baudRateItemBeans.add(baudRateItemBean);
        }
        return baudRateItemBeans;
    }
}