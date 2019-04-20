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

    public static final String IP = "192.168.1.109";
    public static final int PORT = 8888;
    public static final long IDLE_TIMEOUT_SCHEDULE_JOB = 10;

    public static final String UNIT_V = "V";
    public static final String UNIT_A = "A";
    public static final String UNIT_Ah = "Ah";
    public static final String UNIT_HOURS = "";
    public static final String UNIT_PER = "%";
    public static final String UNIT_O = "mΩ";
    public static final String FORWARD = "$1,0,0,0,0,0,0,0,0,0#";
    public static final String BACK_OFF = "$2,0,0,0,0,0,0,0,0,0#";
    public static final String LEFT_TURN = "$3,0,0,0,0,0,0,0,0,0#";
    public static final String RIGHT_TURN = "$4,0,0,0,0,0,0,0,0,0#";
    public static final String STOP = "$0,0,0,0,0,0,0,0,0,0#";

    public static final int VALUE_COLOR = R.color.lz_blue;
    public static final int VALUE_COLOR_EXCEPTION = R.color.lz_red;

    public static String url = "http://192.168.0.1:8080/?action=snapshot";

    public static final int VIDEO = 0x0001;
}
