package com.gt.magicbox.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Created by jack-lin on 2017/9/14 0014.
 * Buddha bless, never BUG!
 */

public class OrderListResultBean {
    public String eqcode = "";
    public int pageCount;
    public int status;
    public List<OrderItemBean> orders;

    public static class OrderItemBean implements Serializable{
        public int id;
        public int eqId;
        public int busId;
        private int modelId;
        public int status;
        public String order_no = "";
        public double money;
        public long time;
        public int type;
        public int shiftId;
        public String staff_name="";
        public String businessUtilName = "";
    }
}
