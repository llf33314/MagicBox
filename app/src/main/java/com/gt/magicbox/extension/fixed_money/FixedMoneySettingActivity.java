package com.gt.magicbox.extension.fixed_money;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.recyclerview.BaseRecyclerAdapter;
import com.gt.magicbox.base.recyclerview.SpaceItemDecoration;
import com.gt.magicbox.bean.FixedMoneyBean;
import com.gt.magicbox.utils.commonutil.ConvertUtils;
import com.gt.magicbox.utils.commonutil.KeyboardUtils;
import com.gt.magicbox.widget.XCRecyclerView;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Description:
 *
 * @author jack-lin
 * @date 2018/1/24 0024
 * Buddha bless, never BUG!
 */

public class FixedMoneySettingActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    XCRecyclerView recyclerView;
    private Button footerButton;
    private final int MAX_COUNT = 6;
    public static final int FOOTER_MODE_ADD = 0;
    public static final int FOOTER_MODE_SAVE = 1;

    private int mode = 0;
    private int currentIndex = 0;
    private ArrayList<FixedMoneyBean> originList = new ArrayList<>();
    private ArrayList<FixedMoneyBean> editList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixed_money_setting);
        initRecycleView();
        updateFooterView();

    }


    private void initRecycleView() {
        final ArrayList<FixedMoneyBean> list = Hawk.get("fixedMoneyList", new ArrayList<FixedMoneyBean>());
        if (list.size() == 0) {
            mode = FOOTER_MODE_ADD;
            list.add(new FixedMoneyBean(0));
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(FixedMoneySettingActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        final SettingMoneyRecyclerViewAdapter adapter = new SettingMoneyRecyclerViewAdapter(FixedMoneySettingActivity.this, list);
        adapter.setEditOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, Object item, int position) {
                footerButton.setText(getResources().getText(R.string.save));
                footerButton.setVisibility(View.VISIBLE);
                originList = Hawk.get("fixedMoneyList", new ArrayList<FixedMoneyBean>());
                editList.clear();
                editList.add(originList.get(position));
                adapter.setEditMode(editList);
                mode = FOOTER_MODE_SAVE;
                currentIndex = position;
            }
        });
        adapter.setDeleteOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, Object item, int position) {
                originList = Hawk.get("fixedMoneyList", new ArrayList<FixedMoneyBean>());
                if (position > -1 && position < originList.size()) {
                    originList.remove(position);
                    Hawk.put("fixedMoneyList", originList);
                    adapter.updateData(originList);
                    mode = FOOTER_MODE_ADD;

                    updateFooterView();
                }
            }
        });
        recyclerView.addItemDecoration(new SpaceItemDecoration(ConvertUtils.dp2px(10), SpaceItemDecoration.SPACE_BOTTOM));
        RelativeLayout mFooterView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.fixed_money_footer, null);
        if (mFooterView != null) {
            recyclerView.addFooterView(mFooterView);
            footerButton = (Button) mFooterView.findViewById(R.id.add);
            footerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (mode) {
                        case FOOTER_MODE_ADD:
                            footerButton.setText(getResources().getText(R.string.save));
                            footerButton.setVisibility(View.VISIBLE);
                            editList.clear();
                            editList.add(new FixedMoneyBean(0));
                            adapter.setEditMode(editList);
                            mode = FOOTER_MODE_SAVE;
                            currentIndex = -1;
                            break;
                        case FOOTER_MODE_SAVE:
                            if (adapter.getEditText() != null) {
                                ArrayList<FixedMoneyBean> arrayList = Hawk.get("fixedMoneyList", new ArrayList<FixedMoneyBean>());
                                String editTextString = adapter.getEditText().getEditableText().toString();
                                if (!TextUtils.isEmpty(editTextString)) {
                                    try {
                                        Double money = Double.parseDouble(editTextString);
                                        if (currentIndex > -1 && currentIndex < arrayList.size()) {
                                            arrayList.set(currentIndex, new FixedMoneyBean(money));
                                            Hawk.put("fixedMoneyList", arrayList);
                                            adapter.updateData(arrayList);
                                            KeyboardUtils.hideSoftInput(v);
                                        } else if (currentIndex == -1) {
                                            arrayList.add(new FixedMoneyBean(money));
                                            Hawk.put("fixedMoneyList", arrayList);
                                            adapter.updateData(arrayList);
                                            KeyboardUtils.hideSoftInput(v);
                                        }
                                    } catch (NumberFormatException e) {

                                    }
                                }
                                mode = FOOTER_MODE_ADD;
                                updateFooterView();
                            }
                            break;
                    }
                }
            });
        }
        recyclerView.setAdapter(adapter);

    }

    private void updateFooterView() {
        final ArrayList<FixedMoneyBean> list = Hawk.get("fixedMoneyList", new ArrayList<FixedMoneyBean>());
        if (list.size() >= MAX_COUNT) {
            footerButton.setVisibility(View.GONE);
        } else {
            footerButton.setVisibility(View.VISIBLE);
            footerButton.setText(getResources().getText(R.string.add));
        }
    }
}
