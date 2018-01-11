package com.gt.magicbox.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.bean.OrderListResultBean;
import com.gt.magicbox.bean.ReasonBean;
import com.gt.magicbox.order.widget.ReasonListDialog;
import com.gt.magicbox.utils.CashierInputFilter;
import com.gt.magicbox.utils.KeyboardUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description:
 * Created by jack-lin on 2018/1/9 0009.
 * Buddha bless, never BUG!
 */

public class ReturnMoneyActivity extends BaseActivity {
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.moneyLayout)
    RelativeLayout moneyLayout;
    @BindView(R.id.returnLayout)
    RelativeLayout returnLayout;
    @BindView(R.id.first)
    RadioButton first;
    @BindView(R.id.second)
    RadioButton second;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.confirmReturn)
    Button confirmReturn;
    @BindView(R.id.returnMoney)
    EditText returnMoney;
    @BindView(R.id.text_return)
    TextView textReturn;
    private String editLastString = "";
    private OrderListResultBean.OrderItemBean orderItemBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_money);
        ButterKnife.bind(this);
        orderItemBean = (OrderListResultBean.OrderItemBean) this.getIntent().getSerializableExtra("orderItemBean");
        initView();
    }

    private void initView() {
        money.setText("" + orderItemBean.money);
        returnMoney.setText("" + orderItemBean.money);
        setEditTextSelectionAtRight(returnMoney);
        editLastString = returnMoney.getEditableText().toString();
        InputFilter[] filters = {new CashierInputFilter(orderItemBean.money)};
        returnMoney.setFilters(filters);
        returnMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String nowEditText = returnMoney.getEditableText().toString();
                if (!TextUtils.isEmpty(editLastString) && !TextUtils.isEmpty(nowEditText)) {
                    if (Double.parseDouble(nowEditText) > orderItemBean.money) {
                        returnMoney.setText("" + editLastString);
                    } else {
                        editLastString = nowEditText;
                        setEditTextSelectionAtRight(returnMoney);
                    }

                }
            }
        });
    }

    private void setEditTextSelectionAtRight(EditText editText) {
        CharSequence text = editText.getText();
        if (text instanceof Spannable) {
            Spannable spanText = (Spannable) text;
            Selection.setSelection(spanText, text.length());
        }
    }


    @OnClick({R.id.text_return, R.id.reasonLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_return:
                setEditTextSelectionAtRight(returnMoney);
                KeyboardUtils.showInputMethod(returnMoney);
                break;
            case R.id.reasonLayout:
                List<ReasonBean> list = new ArrayList<>();
                for (int i = 0; i <= 4; i++) {
                    ReasonBean reasonBean = new ReasonBean();
                    reasonBean.reason = "不想买了";
                    list.add(reasonBean);
                }
                new ReasonListDialog(ReturnMoneyActivity.this, list).show();
                break;
        }
    }
}
