package com.gt.magicbox;

/**
 * Description:
 *
 * @author jack-lin
 * @date 2017/9/30 0030
 * Buddha bless, never BUG!
 */

public class Constant {
    /**
     * 0-测试 || 1-堡垒 || 2-正式
     *( 切换环境APP需要重新登录)
     */
    public static final int ENV = 2;
    public static final String product = "MagicBox";

    /**
     * 硬件项目地址
     */
    public static final String[] YJ_BASE_URL_ARY = {
            "https://yj.deeptel.com.cn/",
            "https://nb.yj.deeptel.com.cn/",
            "https://yj.duofriend.com/"
    };
    public static final String YJ_BASE_URL = YJ_BASE_URL_ARY[ENV];


    /**
     * socket地址
     */
    public static final String[] SOCKET_SERVER_URL_ARY = {
            "https://socket.deeptel.com.cn",
            "https://socket1.duofriend.com",
            "https://socket1.duofriend.com"
    };
    public static final String SOCKET_SERVER_URL = SOCKET_SERVER_URL_ARY[ENV];


    /**
     * 会员地址
     */
    public static final String[] MEMBER_BASE_URL_ARY = {
            "https://member.deeptel.com.cn/",
            "https://nb.member.deeptel.com.cn/",
            "https://member.duofriend.com/"
    };
    public static final String MEMBER_BASE_URL = MEMBER_BASE_URL_ARY[ENV];


    /**
     * WXMP多粉项目地址
     */
    public static final String[] WXMP_BASE_URL_ARY = {
            "https://deeptel.com.cn/",
            "https://nb.deeptel.com.cn/",
            "https://duofriend.com/"
    };
    public static final String WXMP_BASE_URL = WXMP_BASE_URL_ARY[ENV];

}
