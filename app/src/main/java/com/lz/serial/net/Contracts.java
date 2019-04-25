package com.lz.serial.net;

import com.lz.serial.R;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/19 上午9:29
 * 描述     : tcp配置常量类.
 */
public class Contracts {

    //高德地图
    //E0:09:5D:6A:1D:D7:E9:05:66:F7:7F:3F:7F:CD:4E:93:08:85:AC:15

    public static String IP = "192.168.1.106";
    public static int PORT = 30401;
    public static final long IDLE_TIMEOUT_SCHEDULE_JOB = 10;


    public static final int HEX = 0x001;
    public static final String UNIT_V = "V";
    public static final String UNIT_A = "A";
    public static final String UNIT_Ah = "Ah";
    public static final String UNIT_HOURS = "";
    public static final String UNIT_PER = "%";
    public static final String UNIT_O = "mΩ";
    //前
    public static final String FORWARD = "$1,0,0,0,0,0,0,0,0,0#";
    //后
    public static final String BACK_OFF = "$2,0,0,0,0,0,0,0,0,0#";
    //左转
    public static final String LEFT_TURN = "$3,0,0,0,0,0,0,0,0,0#";
    //右转
    public static final String RIGHT_TURN = "$4,0,0,0,0,0,0,0,0,0#";
    //向前左转
    public static final String FORWARD_LEFT_TURN = "$1,1,0,0,0,0,0,0,0,0#";
    //向前右转
    public static final String FORWARD_RIGHT_TURN = "$1,2,0,0,0,0,0,0,0,0#";

    //向后左转
    public static final String BACK_OFF_LEFT_TURN = "$2,1,0,0,0,0,0,0,0,0#";
    //向后右转
    public static final String BACK_OFF_RIGHT_TURN = "$2,2,0,0,0,0,0,0,0,0#";

    //加速
    public static final String ACCELERATE = "$0,0,0,1,0,0,0,0,0,0#";

    //减速
    public static final String SLOW_DOWN = "$0,0,0,2,0,0,0,0,0,0#";

    public static final String STOP = "$0,0,0,0,0,0,0,0,0,0#";

    public static final int VALUE_COLOR = R.color.lz_blue;

    public static final int VALUE_COLOR_EXCEPTION = R.color.lz_red;

    public static String url = "http://192.168.0.1:8080/?action=snapshot";

    public static final int VIDEO = 0x0001;
    public static final String SAVE_IP = "saveIp";
    public static final String SAVE_PORT = "savePort";


}
