package com.gt.magicbox.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Html;
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

import com.gt.magicbox.Constant;
import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseActivity;
import com.gt.magicbox.base.BaseConstant;
import com.gt.magicbox.bean.OrderListResultBean;
import com.gt.magicbox.bean.ReasonBean;
import com.gt.magicbox.http.HttpConfig;
import com.gt.magicbox.http.socket.SocketIOManager;
import com.gt.magicbox.order.widget.ReasonCheckStateAdapter;
import com.gt.magicbox.order.widget.ReasonListDialog;
import com.gt.magicbox.utils.CashierInputFilter;
import com.gt.magicbox.utils.KeyboardUtils;
import com.gt.magicbox.utils.RxBus;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.gt.magicbox.utils.commonutil.PhoneUtils;
import com.gt.magicbox.utils.commonutil.ToastUtil;
import com.gt.magicbox.widget.ReturnMoneyQRCodeDialog;
import com.orhanobut.hawk.Hawk;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import io.socket.emitter.Emitter;

/**
 * Description:
 *
 * @author jack-lin
 * @date 2018/1/9 0009
 * Buddha bless, never BUG!
 */

public class ReturnMoneyActivity extends BaseActivity {
    public static final String TAG = ReturnMoneyActivity.class.getSimpleName();
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
    @BindView(R.id.selectReasonTextView)
    TextView selectReasonTextView;
    @BindView(R.id.firstDivider)
    View firstDivider;
    private String editLastString = "";
    private OrderListResultBean.OrderItemBean orderItemBean;
    private ReasonListDialog dialog;
    private int returnType;
    private double returnActuallyMoney = 0;
    private String reason = "";
    private SocketIOManager socketIOManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_money);
        orderItemBean = (OrderListResultBean.OrderItemBean) this.getIntent().getSerializableExtra("orderItemBean");
        returnType = orderItemBean.type;
        initView();
        RxBus.get().toObservable(ReasonBean.class).subscribe(new Consumer<ReasonBean>() {

            @Override
            public void accept(ReasonBean reasonBean) throws Exception {
                updateReasonDialog();
            }
        });
    }

    private void initView() {
        money.setText("¥ " + orderItemBean.money);
        returnMoney.setText("" + orderItemBean.money);
        setEditTextSelectionAtRight(returnMoney);
        editLastString = returnMoney.getEditableText().toString();
        InputFilter[] filters = {new CashierInputFilter(orderItemBean.money)};
        returnMoney.setFilters(filters);
        String content;
        if (orderItemBean.type == BaseConstant.PAY_ON_CASH) {
            content = BaseConstant.PAY_TYPE[orderItemBean.type];

        } else if (orderItemBean.type == BaseConstant.PAY_ON_ALIPAY) {
            content = BaseConstant.PAY_TYPE[orderItemBean.type] + "<font color=\"#999999\">" + "(魔盒端暂不支持)" + "</font>";
            first.setEnabled(false);
            returnType = BaseConstant.PAY_ON_CASH;
            second.setChecked(true);

        } else {
            content = BaseConstant.PAY_TYPE[orderItemBean.type] + "<font color=\"#999999\">" + "(原路退回)" + "</font>";
        }

        first.setText(Html.fromHtml(content));
        if (orderItemBean.type == BaseConstant.PAY_ON_CASH) {
            second.setVisibility(View.GONE);
            firstDivider.setVisibility(View.GONE);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.first:
                        returnType = orderItemBean.type;
                        break;
                    case R.id.second:
                        returnType = 2;
                        break;
                }
            }
        });
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
                    try {
                        if (Double.parseDouble(nowEditText) > orderItemBean.money) {
                            returnMoney.setText(editLastString);
                        } else {
                            editLastString = nowEditText;
                            setEditTextSelectionAtRight(returnMoney);
                        }
                    } catch (NumberFormatException e) {
                        returnMoney.setText("" + orderItemBean.money);
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


    @OnClick({R.id.text_return, R.id.reasonLayout, R.id.confirmReturn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_return:
                setEditTextSelectionAtRight(returnMoney);
                KeyboardUtils.showInputMethod(returnMoney);
                break;
            case R.id.reasonLayout:
                createReasonDialog();
                break;
            case R.id.confirmReturn:
                returnOrderMoney();
                break;
        }
    }

    private void createReasonDialog() {
        final ArrayList<ReasonBean> list = Hawk.get("reasonList", new ArrayList<ReasonBean>());
        dialog = new ReasonListDialog(ReturnMoneyActivity.this, list, R.style.ShortcutMenuDialog);
        dialog.show();
        dialog.setOnItemClickListener(new ReasonCheckStateAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, ReasonCheckStateAdapter.StateHolder holder, int position) {
                selectReasonTextView.setText(list.get(position).reason + " >");
            }
        });
    }

    private void updateReasonDialog() {
        final ArrayList<ReasonBean> list = Hawk.get("reasonList", new ArrayList<ReasonBean>());
        if (dialog != null) {
            dialog.update(list);
            dialog.setOnItemClickListener(new ReasonCheckStateAdapter.OnItemClickListener() {
                @Override
                public void OnItemClick(View view, ReasonCheckStateAdapter.StateHolder holder, int position) {
                    reason = list.get(position).reason;
                    selectReasonTextView.setText(list.get(position).reason + " >");
                    reason = selectReasonTextView.getText().toString();
                }
            });
        }
    }

    private void returnOrderMoney() {
        String returnMoneyString = returnMoney.getEditableText().toString();
        returnActuallyMoney = orderItemBean.money;
        if (!TextUtils.isEmpty(returnMoneyString)) {
            try {
                returnActuallyMoney = Double.parseDouble(returnMoneyString);

            } catch (NumberFormatException e) {
                ToastUtil.getInstance().showToast("输入退款金额有误");
                return;
            }
        }
        if (returnActuallyMoney == 0) {
            ToastUtil.getInstance().showToast("输入退款金额有误");
            return;
        }
        showQrCodeDialog();
    }

    private void showQrCodeDialog() {
        if (orderItemBean != null) {
            connectSocket();
            String content = Constant.YJ_BASE_URL + HttpConfig.CHECK_RELATED_WX + "?orderNo=" + orderItemBean.order_no
                    + "&totalFee=" + returnActuallyMoney + "&type=" + returnType + "&cause=" + reason + "&shiftId=" + Hawk.get("shiftId", 0);
            ReturnMoneyQRCodeDialog
                    returnMoneyQRCodeDialog = new ReturnMoneyQRCodeDialog(ReturnMoneyActivity.this, content);
            returnMoneyQRCodeDialog.show();
        }
    }

    private void connectSocket() {
        socketIOManager = new SocketIOManager(Constant.SOCKET_SERVER_URL);
        socketIOManager.setOnConnect(new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                LogUtils.d(SocketIOManager.TAG, "auth key : " + HttpConfig.SOCKET_RETURN_MONEY_AUTH_KEY + orderItemBean.order_no);
                socketIOManager.getSocket().emit(HttpConfig.SOCKET_ANDROID_AUTH, HttpConfig.SOCKET_RETURN_MONEY_AUTH_KEY + orderItemBean.order_no);
                LogUtils.d(SocketIOManager.TAG, "call: send android auth over");
            }
        });
        socketIOManager.setSocketEvent(new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                LogUtils.d(SocketIOManager.TAG, " args[0]=" + args[0]);

                String retData = null;
                try {
                    retData = data.get("message").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                    retData = "";
                }
                LogUtils.d(SocketIOManager.TAG, "retData=" + retData);
                if (retData.equals("success")) {

                }
            }
        });
        socketIOManager.connectSocket();
    }

    @Override
    protected void onStop() {
        if (socketIOManager != null) {
            socketIOManager.disSocket();
        }
        super.onStop();
    }
}
