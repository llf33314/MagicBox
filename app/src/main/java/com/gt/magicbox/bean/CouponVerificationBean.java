package com.gt.magicbox.bean;

import java.io.Serializable;

/**
 * Description:
 * Created by jack-lin on 2017/11/30 0030.
 * Buddha bless, never BUG!
 */

public class CouponVerificationBean implements Serializable{

    /**
     * cardId : 129
     * cardName : 666666
     */

    private int cardId;
    private String cardName;

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public int getCardId() {
        return cardId;
    }

    public String getCardName() {
        return cardName;
    }
}
