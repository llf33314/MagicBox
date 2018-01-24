package com.gt.magicbox.utils;

import com.gt.api.bean.sign.SignBean;
import com.gt.api.util.KeysUtil;

/**
 * Description:
 * Created by jack-lin on 2018/1/23 0023.
 * Buddha bless, never BUG!
 */

public class GT_API_Utils {
    /**
     * 签名
     *
     * @param signKey 签名密钥
     * @return SignBean 签名类JavaBean
     * @throws Exception
     */
    public static SignBean sign(String signKey, String param) throws Exception {
        KeysUtil keysUtil = new KeysUtil();
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String randNum = String.valueOf((int) ((Math.random() * 9 + 1) * 10000));
        String sign = keysUtil.getEncString(signKey + timeStamp + randNum + param);
        SignBean signBean = new SignBean(sign, timeStamp, randNum);
        return signBean;
    }
}
