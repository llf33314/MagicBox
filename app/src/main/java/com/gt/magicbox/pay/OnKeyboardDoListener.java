package com.gt.magicbox.pay;

/**
 * Description:
 * Created by jack-lin on 2017/7/19 0019.
 */

public interface OnKeyboardDoListener {
    void onPay(double money);
    void onMemberPay(double money);
    void onNumberInput(String num);

}
