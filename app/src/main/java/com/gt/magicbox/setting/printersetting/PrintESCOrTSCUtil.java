package com.gt.magicbox.setting.printersetting;

import android.text.TextUtils;

import com.gprinter.command.EscCommand;
import com.gprinter.command.LabelCommand;
import com.gt.magicbox.base.BaseConstant;
import com.gt.magicbox.bean.MemberCardBean;
import com.gt.magicbox.bean.ShiftRecordsAllBean;
import com.gt.magicbox.bean.ShopInfoBean;
import com.gt.magicbox.bean.StaffBean;
import com.gt.magicbox.utils.commonutil.TimeUtils;
import com.orhanobut.hawk.Hawk;

import java.math.BigDecimal;

/**
 * Created by wzb on 2017/8/16 0016.
 */

public class PrintESCOrTSCUtil {
    private static long num_no=223578914;

    public static final String [] PAY_TYPE={"微信支付","支付宝","现金支付"};

    public static EscCommand getPrintEscCommand(String money){
        EscCommand esc = new EscCommand();
        esc.addPrintAndFeedLines((byte) 1);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
        esc.addText("多粉餐厅（赛格）\n"); // 打印文字
        esc.addPrintAndLineFeed();

        // 打印文字 *//*
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 取消倍高倍宽
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印左对齐
        esc.addText("--------------------------------\n");// 打印文字
        esc.addText("单号：12345678987445775\n"); // 打印文字
        esc.addText("--------------------------------\n");
        esc.addText("消费总额："+money+"\n");
        esc.addText("--------------------------------\n");
        esc.addText("抵扣方式：100粉币（-10.00）\n");
        esc.addText("实付金额：46.10\n");
        esc.addText("支付方式：微信支付\n");
        esc.addText("会员折扣：8.5\n");
        esc.addText("--------------------------------\n");
        esc.addText("开单时间：2017-07-21 14:23\n");
        esc.addText("收银员：多粉\n");
        esc.addText("--------------------------------\n");
        esc.addText("联系电话：0752-3851585\n");
        esc.addText("地址：惠州市惠城区赛格假日广场1007室\n");
        esc.addText("--------------------------------\n");
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
        esc.addText("欢迎再次光临！\n"); // 打印文字
        esc.addPrintAndFeedLines((byte)5);
        return esc;
    }

    public static LabelCommand getTscCommand(){
        int LEFT=15;
        String name="超级杯奶茶";

        //总共320*240
        LabelCommand tsc = new LabelCommand();
        tsc.addSize(40, 30); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(3); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.FORWARD , LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addReference(0, 0);// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区
        // 绘制简体中文

        if (name.length()<=6){//中文的小于等于6 则字体变大打印一行
            tsc.addText(LEFT,LEFT , LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                    "0279");
            tsc.addText(LEFT,75, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                    name);
            tsc.addText(LEFT, 140, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                    "大杯，热 x1");
            tsc.addText(LEFT, 170, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                    "备注：少冰，加糖，果粒");
        }else{

            String oneLine=name.substring(0,6);
            String twoLine=name.substring(6,12);//最多只能打12个字
            tsc.addText(LEFT,LEFT , LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                    "0279");
            tsc.addText(LEFT,70, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                    oneLine);
            tsc.addText(LEFT,125, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                    twoLine);
            tsc.addText(LEFT,180, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                    "大杯，热 x1");
            tsc.addText(LEFT, 210, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                    "备注：123456");
        }

      /*  Bitmap b=view2Bitmap();
        tsc.addBitmap(0, 0, LabelCommand.BITMAP_MODE.OVERWRITE, b.getWidth(), b);*/
        //打印机打印密度

        tsc.addDensity(LabelCommand.DENSITY.DNESITY10);

        tsc.addPrint(1, 1); // 打印标签
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        return tsc;
    }

    public static EscCommand getPrintEscTest(String orderNo,String money,String staffName,int type,String time){
        ShopInfoBean shopInfoBean= Hawk.get("ShopInfoBean");
        EscCommand esc = new EscCommand();
        esc.addPrintAndFeedLines((byte) 1);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
        esc.addText(shopInfoBean.getShopName()+"\n"); // 打印文字
        esc.addPrintAndLineFeed();

        // 打印文字 *//*
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 取消倍高倍宽
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印左对齐
        esc.addText("--------------------------------\n");// 打印文字
        esc.addText("订单号："+orderNo+"\n"); // 打印文字
        if (!TextUtils.isEmpty(time)){
            esc.addText("开单时间："+time +"\n");

        }else
        esc.addText("开单时间："+ TimeUtils.getNowString()+"\n");
        if ( !TextUtils.isEmpty(staffName)){
            esc.addText("收银员："+staffName+"\n");

        }else {
           // esc.addText("收银员：张震\n");

        }
        if (type>=0&&type<BaseConstant.PAY_TYPE.length){
            esc.addText("支付方式："+BaseConstant.PAY_TYPE[type]+"\n");
        }else{
            esc.addText("支付方式：未知\n");
        }

        esc.addText("--------------------------------\n");
        esc.addText("订单金额："+money+"\n");
        esc.addText("--------------------------------\n");
        if (null!=shopInfoBean)
        esc.addText("联系电话："+shopInfoBean.getShops().getTelephone()+"\n");
        if (null!=shopInfoBean)
            esc.addText("地址:"+shopInfoBean.getShops().getAddress()+"\n");
        esc.addText("--------------------------------\n");

        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
        esc.addText("欢迎再次光临\n"); // 打印文字
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        esc.addPrintAndFeedLines((byte) 1);
        esc.addText("技术支持·多粉 400-889-4522");
        esc.addPrintAndFeedLines((byte)5);
        return esc;
    }
    public static EscCommand getPrintMemberRecharge(MemberCardBean memberCardBean, String orderNo, String money, int type,String balance){
        ShopInfoBean shopInfoBean= Hawk.get("ShopInfoBean");

        EscCommand esc = new EscCommand();
        esc.addPrintAndFeedLines((byte) 1);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
        esc.addText(shopInfoBean.getShopName()+"\n"); // 打印文字
        esc.addPrintAndLineFeed();

        // 打印文字 *//*
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 取消倍高倍宽
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印左对齐
        esc.addText("会员卡充值\n");// 打印文字
        esc.addText("订单号："+orderNo+"\n\n\n"); // 打印文字

        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印左对齐
        esc.addText("会员昵称："+ memberCardBean.nickName+"\n");
        esc.addText("会员卡号："+ memberCardBean.cardNo+"\n\n");
        esc.addText("--------------------------------\n\n");

        esc.addText("充值金额："+ money+"元"+"\n");
        esc.addText("充值方式："+ BaseConstant.PAY_TYPE[type]+"\n");
        esc.addText("充值时间："+ TimeUtils.getNowString()+"\n\n");
        esc.addText("");
        esc.addText("--------------------------------\n\n");
        esc.addText("当前卡内余额：          "+balance+"元\n\n\n");
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addText("技术支持·多粉 400-889-4522");
        esc.addPrintAndFeedLines((byte)5);
        return esc;
    }
    /**
     * 交班
     * @return
     */
    public static EscCommand getExChangeESC(ShiftRecordsAllBean.ShiftRecordsBean shiftBean){
        EscCommand esc = new EscCommand();
        esc.addPrintAndFeedLines((byte) 1);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
        esc.addText("交班表\n"); // 打印文字
        esc.addPrintAndLineFeed();

        // 打印文字 *//*
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 取消倍高倍宽
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印左对齐

        esc.addText("门店："+shiftBean.getShopName()+"\n");
        esc.addText("设备号："+shiftBean.getEqId()+"\n");
        esc.addText("当班人："+shiftBean.getStaffName()+"\n");
        esc.addText("…………………………………………\n\n");
        esc.addText("上班时间："+shiftBean.getStartTime()+"\n");
        esc.addText("下班时间："+TimeUtils.getNowString()+"\n");
        esc.addText("…………………………………………\n\n");
        esc.addText("实际支付订单数："+shiftBean.getOrderInNum()+"\n");
        esc.addText("实际应收金额："+shiftBean.getMoney()+"\n");
        esc.addText("微信支付："+shiftBean.getWechatMoney()+"\n");
        esc.addText("支付宝："+shiftBean.getAlipayMoney()+"\n");
        esc.addText("现金支付："+shiftBean.getCashMoney()+"\n");
        esc.addText("--------------------------------\n\n");
        esc.addText("当班人签名：\n\n");
        esc.addText("接班人签名：\n\n");
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addText("技术支持·多粉 400-889-4522");
        esc.addPrintAndFeedLines((byte)5);
        return esc;
    }

  /*private static int sendLabelReceipt() {
        //总共320*240
        LabelCommand tsc = new LabelCommand();
        tsc.addSize(40, 30); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(3); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.FORWARD , LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addReference(0, 0);// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区
        // 绘制简体中文
        tsc.addText(60, 12, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                "多粉餐饮");
        tsc.addReverse(45,5,320-95,63);


        tsc.addText(15,75, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                "海贼王连锁店");
        tsc.addText(10, 130, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "加糖拿铁 小杯");
        tsc.addText(10, 160, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "价格：15.00元");
        tsc.addText(10, 195, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "外卖：123456");

        //二维码
        tsc.addQRCode(190, 130, LabelCommand.EEC.LEVEL_L, 4, LabelCommand.ROTATION.ROTATION_0, " http://www.duofriend.com/");


        // 绘制图片
     *//*   Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.gprinter);
        tsc.addBitmap(20, 50, BITMAP_MODE.OVERWRITE, b.getWidth() * 2, b);
*//*

        // 绘制一维条码
       // tsc.add1DBarcode(20, 250, LabelCommand.BARCODETYPE.CODE128, 100, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0, "Gprinter");
        tsc.addPrint(1, 1); // 打印标签
       // tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        //打印机打印密度
        tsc.addDensity(LabelCommand.DENSITY.DNESITY10);
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        Vector<Byte> datas = tsc.getCommand(); // 发送数据
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel=-1;
        try {
            rel = mGpService.sendLabelCommand(mPrinterIndex, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                //Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return  rel;
    }*/
}
