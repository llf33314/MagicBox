package com.gt.magicbox.order;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.bean.ReasonBean;
import com.gt.magicbox.setting.typewriting.TypewritingGuideActivity;
import com.gt.magicbox.utils.RxBus;
import com.gt.magicbox.utils.commonutil.ToastUtil;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:
 *
 * @author jack-lin
 * @date 2018/1/12 0012
 * Buddha bless, never BUG!
 */

public class AddReasonTagActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.clear)
    RelativeLayout clear;
    @BindView(R.id.confirmSave)
    Button confirmSave;
    @BindView(R.id.reasonEdit)
    EditText reasonEdit;
    @BindView(R.id.typewritingLink)
    TextView typewritingLink;

    public static final int TYPE_ADD = 0;
    public static final int TYPE_EDIT = 1;
    private int type = 0;
    private String content = "";
    private int index = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activirty_add_tag);
        ButterKnife.bind(this);
        type = this.getIntent().getIntExtra("type", 0);
        content = this.getIntent().getStringExtra("content");
        index = this.getIntent().getIntExtra("index", 0);
        initView();
    }

    private void initView() {
        reasonEdit.setText(content);
        reasonEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(reasonEdit.getEditableText().toString())) {
                    confirmSave.setEnabled(false);
                } else {
                    confirmSave.setEnabled(true);
                }
            }
        });
        typewritingLink.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        typewritingLink.getPaint().setAntiAlias(true);
    }

    @OnClick({R.id.clear, R.id.confirmSave, R.id.typewritingLink})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.clear:
                reasonEdit.setText("");
                break;
            case R.id.confirmSave:
                String editText = reasonEdit.getText().toString();
                if (!TextUtils.isEmpty(editText)) {
                    ArrayList<ReasonBean> reasonList = Hawk.get("reasonList", new ArrayList<ReasonBean>());
                    if (type == TYPE_ADD) {
                        reasonList.add(0, new ReasonBean(reasonEdit.getText().toString()));
                        Hawk.put("reasonList", reasonList);
                        RxBus.get().post(new ReasonBean(""));
                    } else if (type == TYPE_EDIT) {
                        reasonList.set(index, new ReasonBean(reasonEdit.getText().toString()));
                        Hawk.put("reasonList", reasonList);
                        RxBus.get().post(new ReasonBean(""));
                    }
                } else {
                    ToastUtil.getInstance().showToast("原因标签不能为空");
                }
                finish();
                break;
            case R.id.typewritingLink:
                Intent intent = new Intent(AddReasonTagActivity.this, TypewritingGuideActivity.class);
                startActivity(intent);
                break;
        }
    }
}
