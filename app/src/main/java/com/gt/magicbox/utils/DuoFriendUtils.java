package com.gt.magicbox.utils;

import com.orhanobut.hawk.Hawk;

/**
 * Description:
 * Created by jack-lin on 2018/1/23 0023.
 * Buddha bless, never BUG!
 */

public class DuoFriendUtils {
    public static boolean isMainShop(){
        int busId= Hawk.get("busId",0);
        int childBusId= Hawk.get("childBusId",0);
        return busId != 0 && busId == childBusId;
    }
}
