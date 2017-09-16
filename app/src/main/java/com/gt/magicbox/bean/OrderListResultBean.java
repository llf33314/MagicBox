package com.gt.magicbox.bean;

import java.util.List;

/**
 * Description:
 * Created by jack-lin on 2017/9/14 0014.
 * Buddha bless, never BUG!
 */

public class OrderListResultBean {
    public String eqcode="";
    public int pageCount;
    public int status;
    public List<OrderItemBean> orders;
    public static class OrderItemBean{
        public int id;
        public int status;
        public double money;
        public long time;
        public int type;
        public String orderNo="";
    }
}
