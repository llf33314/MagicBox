package com.gt.magicbox.exchange;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.recyclerview.BaseRecyclerAdapter;
import com.gt.magicbox.base.recyclerview.DividerDecoration;
import com.gt.magicbox.base.recyclerview.SimpleDividerDecoration;
import com.gt.magicbox.base.recyclerview.SpaceItemDecoration;
import com.gt.magicbox.bean.LoginBean;
import com.gt.magicbox.bean.StaffBean;
import com.gt.magicbox.utils.commonutil.ScreenUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.gt.magicbox.base.recyclerview.SpaceItemDecoration.SPACE_BOTTOM;

/**
 * Created by wzb on 2017/9/25 0025.
 */
@Deprecated
public class ExchangeChooseDialog extends Dialog {

    @BindView(R.id.exchange_dialog_choose_cancel)
    TextView cancel;
    @BindView(R.id.exchange_dialog_choose_name)
    TextView chooseName;
    @BindView(R.id.exchange_dialog_choose_go)
    ImageView chooseGo;
    @BindView(R.id.exchange_dialog_choose_rv)
    RecyclerView chooseRv;
    private List<StaffBean.StaffListBean> staffList;

    public ExchangeChooseDialog(@NonNull Context context) {
        super(context);
    }

    public ExchangeChooseDialog(@NonNull Context context, @StyleRes int themeResId, List<StaffBean.StaffListBean> staffList) {
        super(context, themeResId);
        this.staffList = staffList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_exchange_choose);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        setBottom();
        if (staffList==null||staffList.size()<1){
            return;
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chooseRv.setLayoutManager(layoutManager);

        DialogChoosePersonAdapter adapter=new DialogChoosePersonAdapter(this.getContext(),staffList);

        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, Object item, int position) {
                chooseName.setText(((StaffBean.StaffListBean)item).getName());
            }
        });

        chooseRv.setAdapter(adapter);
        chooseRv.addItemDecoration(new DividerDecoration(this.getContext()).setTopDivider(true).setColor(R.color.divide_gray_color));
    }

    private void setBottom() {
        Window w = this.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.width = ScreenUtils.getScreenWidth();
        lp.height = ScreenUtils.getScreenHeight() / 2;
        lp.gravity = Gravity.BOTTOM;
        w.setAttributes(lp);
    }

    @OnClick({R.id.exchange_dialog_choose_go,R.id.exchange_dialog_choose_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.exchange_dialog_choose_cancel:
                this.dismiss();
                break;
            case R.id.exchange_dialog_choose_go:
                break;
        }
    }
}
