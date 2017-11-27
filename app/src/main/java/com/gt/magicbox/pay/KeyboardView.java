package com.gt.magicbox.pay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gt.magicbox.Constant;
import com.gt.magicbox.R;
import com.gt.magicbox.base.BaseConstant;
import com.gt.magicbox.utils.SpannableStringUtils;
import com.gt.magicbox.utils.commonutil.ConvertUtils;
import com.gt.magicbox.utils.commonutil.LogUtils;
import com.gt.magicbox.utils.commonutil.ToastUtil;

import java.math.BigDecimal;

/**
 * Description:
 * Created by jack-lin on 2017/7/18 0018.
 */

public class KeyboardView extends RelativeLayout implements View.OnClickListener, AdapterView.OnItemClickListener {
    Context context;
    private GridView gridView;
    private Button clear;
    private Button delete;
    private Button pay;
    private Button member_pay;
    private Button fit_pay;
    private ImageView inputCursor;
    private OnInputListener onInputListener;
    private TextView showNumber;
    private StringBuffer numberString = new StringBuffer();
    private OnKeyboardDoListener onKeyboardDoListener;
    private RelativeLayout chargeLayout;
    private RelativeLayout tipLayout;
    private TextView aimTip;
    private TextView should_pay;
    private TextView charge;
    private TextView text_paid_in_amount;
    private int maxLength = 4;
    private int keyboardType = 0;
    private double chargeMoney;
    private double realPay;
    private View inputBg;
    private String tipContent="";
    private double orderMoney;
    public static final int TYPE_INPUT_MONEY = 0;
    public static final int TYPE_CHARGE = 1;
    public static final int TYPE_MEMBER = 2;
    public static final int TYPE_COUPON_VERIFICATION=3;
    public static final int TYPE_MEMBER_RECHARGE=4;
    public static final int TYPE_MEMBER_RECHARGE_CASH=5;

    public KeyboardView(Context context) {
        this(context, null);
    }

    public KeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = View.inflate(context, R.layout.keyboard, null);
        gridView = (GridView) view.findViewById(R.id.grid_keyboard);
        clear = (Button) view.findViewById(R.id.keyboard_clear);
        delete = (Button) view.findViewById(R.id.keyboard_delete);
        pay = (Button) view.findViewById(R.id.keyboard_pay);
        member_pay = (Button) view.findViewById(R.id.keyboard_member_pay);
        fit_pay = (Button) view.findViewById(R.id.keyboard_fit_pay);
        inputCursor=(ImageView)view.findViewById(R.id.inputCursor) ;
        inputBg=view.findViewById(R.id.inputBg);
        showNumber = (TextView) view.findViewById(R.id.showNumber);
        should_pay = (TextView) view.findViewById(R.id.should_pay);
        charge = (TextView) view.findViewById(R.id.charge);
        text_paid_in_amount = (TextView) view.findViewById(R.id.text_paid_in_amount);
        chargeLayout = (RelativeLayout) view.findViewById(R.id.chargeLayout);
        tipLayout = (RelativeLayout) view.findViewById(R.id.tip_layout);
        aimTip=(TextView)view.findViewById(R.id.tip_aim);
        clear.setOnClickListener(this);
        delete.setOnClickListener(this);
        pay.setOnClickListener(this);
        fit_pay.setOnClickListener(this);
        member_pay.setOnClickListener(this);
        KeyboardAdapter keyboardAdapter = new KeyboardAdapter(context);
        gridView.setOnItemClickListener(this);
        gridView.setAdapter(keyboardAdapter);
        addView(view);
        setFlickerAnimation(inputCursor);

    }

    public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GridView getGridView() {
        return gridView;
    }

    public TextView getShowNumber() {
        return showNumber;
    }

    public void setOnKeyboardDoListener(OnKeyboardDoListener onKeyboardDoListener) {
        this.onKeyboardDoListener = onKeyboardDoListener;
    }

    @Override
    public void onClick(View view) {
        if (onInputListener!=null)onInputListener.onInput();
        switch (view.getId()) {
            case R.id.keyboard_clear:
                clearAll();
                break;
            case R.id.keyboard_delete:
                backspace();
                break;
            case R.id.keyboard_fit_pay:
                enter();
              break;
            case R.id.keyboard_pay:
                enter();
                break;
            case R.id.keyboard_member_pay:
                memberPay();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (onInputListener!=null)onInputListener.onInput();
        if (keyboardType==TYPE_INPUT_MONEY||keyboardType==TYPE_CHARGE
                ||keyboardType==TYPE_MEMBER_RECHARGE_CASH){
            if (numberString.toString().contains(".")||position==11)maxLength=7;
            else maxLength=4;
        }
        if (numberString.length() <maxLength) {
            if (position <= 10&&numberString.toString().contains(".")
                    &&numberString.toString().substring(numberString.toString().indexOf("."),numberString.toString().length()).length()>2){
                return;
            }
            if (position <= 8) {
                if (numberString.toString().equals("0")) {
                    numberString.deleteCharAt(0);
                }
                numberString.append("" + (position + 1));
            } else if (position == 9) {
                if (numberString.length() > 0 && !numberString.toString().equals("0"))
                    numberString.append("00");
            } else if (position == 10) {
                if (!numberString.toString().equals("0"))
                    numberString.append("0");
            } else if (position == 11&&keyboardType!=TYPE_MEMBER&&keyboardType!=TYPE_COUPON_VERIFICATION
                    &&keyboardType!=TYPE_MEMBER_RECHARGE) {
                if (!numberString.toString().contains(".") && numberString.length() > 0)
                    numberString.append(".");
            }
             showMoney();
        }
    }

    private void showMoney() {
        if (numberString.length() != 0) {
            if (keyboardType == TYPE_CHARGE||keyboardType==TYPE_MEMBER_RECHARGE_CASH) {
                realPay = Double.parseDouble(numberString.toString());
              //  chargeMoney = realPay - orderMoney;
                chargeMoney =sub(realPay,orderMoney);
                if (chargeMoney >= 0) {
                    charge.setText(SpannableStringUtils.diffTextSize("¥ " + chargeMoney, 16, 0, 1));
                } else charge.setText("");
            }
        }
        if (keyboardType==TYPE_MEMBER||keyboardType==TYPE_COUPON_VERIFICATION
                ||keyboardType==TYPE_MEMBER_RECHARGE){
            showNumber.setText(numberString);
            showNumber.setTextColor(getResources().getColor(R.color.black));
            if (TextUtils.isEmpty(numberString.toString())) {
                showNumber.setText(tipContent);
                showNumber.setTextColor(getResources().getColor(R.color.hint_text));
            }
        }else  showNumber.setText(SpannableStringUtils.diffTextSize("¥ " + numberString, 20, 0, 1));

    }

    public double sub(double d1,double d2){

        BigDecimal bd1 = new BigDecimal(Double.toString(d1));

        BigDecimal bd2 = new BigDecimal(Double.toString(d2));

        return bd1.subtract(bd2).doubleValue();

    }

    public void setKeyboardType(int keyboardType) {
        this.keyboardType = keyboardType;
        if (keyboardType == TYPE_CHARGE|| keyboardType == TYPE_MEMBER_RECHARGE_CASH) {
            chargeLayout.setVisibility(VISIBLE);
            text_paid_in_amount.setVisibility(VISIBLE);
            showNumber.setTextColor(getResources().getColor(R.color.white));
            should_pay.setText(SpannableStringUtils.diffTextSize("¥ " + orderMoney, 14, 0, 1));
            pay.setText("确认");
            pay.setVisibility(VISIBLE);
            member_pay.setVisibility(GONE);
            fit_pay.setVisibility(GONE);
            inputBg.setVisibility(GONE);
        }else if (keyboardType==TYPE_MEMBER||keyboardType==TYPE_MEMBER_RECHARGE){
            if (Constant.product == BaseConstant.PRODUCTS[1]) tipLayout.setVisibility(INVISIBLE);
            else tipLayout.setVisibility(VISIBLE);
            pay.setText("确认");
            pay.setVisibility(VISIBLE);
            member_pay.setVisibility(GONE);
            fit_pay.setVisibility(GONE);
            tipContent=getResources().getText(R.string.please_input_member_or_phone).toString();
            showNumber.setText(tipContent);
            showNumber.setTextSize(TypedValue.COMPLEX_UNIT_DIP,26);
            showNumber.setTextColor(getResources().getColor(R.color.hint_text));
            RelativeLayout.LayoutParams params= (LayoutParams) showNumber.getLayoutParams();
            params.setMargins(0,0,ConvertUtils.dp2px(getResources().getDimension(R.dimen.dp_8))
                    ,ConvertUtils.dp2px(getResources().getDimension(R.dimen.dp_5)));
            showNumber.setLayoutParams(params);
            maxLength=20;
            setCursorLayout(inputCursor);
            setNumberLayout(showNumber);
        }else if (keyboardType==TYPE_COUPON_VERIFICATION){
            if (Constant.product == BaseConstant.PRODUCTS[1]) tipLayout.setVisibility(GONE);
            else tipLayout.setVisibility(VISIBLE);
            pay.setText("确认");
            pay.setVisibility(VISIBLE);
            member_pay.setVisibility(GONE);
            fit_pay.setVisibility(GONE);
            tipContent=getResources().getText(R.string.please_input_coupon_number).toString();
            showNumber.setText(tipContent);
            showNumber.setTextColor(getResources().getColor(R.color.hint_text));
            aimTip.setText(getResources().getText(R.string.please_aim_coupon_tip));
            showNumber.setTextSize(TypedValue.COMPLEX_UNIT_DIP,25);
            RelativeLayout.LayoutParams params= (LayoutParams) showNumber.getLayoutParams();
            params.setMargins(0,0,ConvertUtils.dp2px(getResources().getDimension(R.dimen.dp_8))
                    ,ConvertUtils.dp2px(getResources().getDimension(R.dimen.dp_5)));
            showNumber.setLayoutParams(params);
            setCursorLayout(inputCursor);
            setNumberLayout(showNumber);
            maxLength=11;
        }
    }

    public void setOrderMoney(double orderMoney) {
        this.orderMoney = orderMoney;
    }
    public void input(String str) {
        LogUtils.i("keycode", "input numberString=" + numberString.toString());

            if (numberString.length() <= maxLength) {
                if (numberString.toString().equals("0") && !str.equals(".")) {
                    LogUtils.i("keycode", "input numberString.deleteCharAt(0)=" + numberString.toString());
                    numberString.deleteCharAt(0);
                }
                if (str.equals(".") && (numberString.toString().contains(".") && numberString.length() == 0))//已经有小数点了或者小数点不能作为开头
                    return;
                if (numberString.toString().contains(".")
                        && numberString.toString().substring(numberString.toString().indexOf("."), numberString.toString().length()).length() > 2)
                    return;
                numberString.append(str);
                showMoney();
            }
    }
    public void clearAll(){
        numberString.setLength(0);
        showMoney();
        charge.setText("");
        showNumber.setText("");
        if (keyboardType==TYPE_MEMBER||keyboardType==TYPE_COUPON_VERIFICATION
                ||keyboardType==TYPE_MEMBER_RECHARGE) {
            showNumber.setText(tipContent);
        }
    }
    public void backspace(){
        if (numberString.length() > 0) {
            numberString.deleteCharAt(numberString.length() - 1);
            showMoney();
        }
    }
    public void enter(){
        if (onKeyboardDoListener != null) {
            if (keyboardType == TYPE_INPUT_MONEY) {
                if (!TextUtils.isEmpty(numberString)) {
                    double money = Double.parseDouble(numberString.toString());
                    if (money != 0) {
                        onKeyboardDoListener.onPay(money);
                    }
                }
            } else if (keyboardType == TYPE_CHARGE || keyboardType == TYPE_MEMBER_RECHARGE_CASH) {
                if (chargeMoney >= 0 ) {
                    onKeyboardDoListener.onPay(orderMoney);
                }
            }
            else if (keyboardType == TYPE_CHARGE || keyboardType == TYPE_MEMBER_RECHARGE_CASH) {
                if (chargeMoney >= 0 && realPay > 0) {
                    onKeyboardDoListener.onPay(orderMoney);
                }
            }else if (keyboardType == TYPE_MEMBER||keyboardType==TYPE_MEMBER_RECHARGE) {
                if (!TextUtils.isEmpty(numberString)) {
                    onKeyboardDoListener.onNumberInput(numberString.toString());
                }
            }

        }
    }

    public void memberPay() {
        if (onKeyboardDoListener != null) {
            if (keyboardType == TYPE_INPUT_MONEY) {
                if (!TextUtils.isEmpty(numberString)) {
                    double money = Double.parseDouble(numberString.toString());
                    if (money != 0) {
                        onKeyboardDoListener.onMemberPay(money);
                    }
                }
            }
        }
    }
    public interface OnInputListener{
        void onInput();
    }

    public void setOnInputListener(OnInputListener onInputListener) {
        this.onInputListener = onInputListener;
    }
    private void setFlickerAnimation(ImageView imageView) {
        final Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(420);//闪烁时间间隔
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        imageView.setAnimation(animation);
    }
    private void setCursorLayout(ImageView imageView){
        RelativeLayout.LayoutParams layoutParams= (LayoutParams) imageView.getLayoutParams();
        layoutParams.setMargins(0,(int) getResources().getDimension(R.dimen.dp_4),
                (int) getResources().getDimension(R.dimen.dp_14),(int) getResources().getDimension(R.dimen.dp_20));
        imageView.setLayoutParams(layoutParams);
    }
    private void setNumberLayout(TextView textView){
        RelativeLayout.LayoutParams layoutParams= (LayoutParams) textView.getLayoutParams();
        layoutParams.setMargins(0,0,
                (int) getResources().getDimension(R.dimen.dp_14),(int) getResources().getDimension(R.dimen.dp_20));
        textView.setLayoutParams(layoutParams);
    }
}
