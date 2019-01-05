package com.lz.base.protocol.common;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/1/4 下午1:31
 * 描述     :
 */
public final class RegisterSpace {

    public static final byte VERSION_R_1 = (byte) 0X00;//DGUS版本号,以BCD码表示,0x12表示V1.2
    public static final byte LED_SET_W_1 = (byte) 0X01;//背光亮度控制存储器,范围0x00-0x40
    public static final byte BZ_TIME_W_1 = (byte) 0X02;//蜂鸣器明教控制寄存器,单位10ms
    public static final byte PIC_ID_R_W_2 = (byte) 0X03;//读取当前显示页面id, 写入切换到制定id的页面
    public static final byte TP_FLAG_R_W_1 = (byte) 0X05;//0x5A=触摸屏坐标更新,其他=触摸屏坐标未更新,当用户读取数据后未清零这个标记,则触摸屏数据不在更新.
    public static final byte TP_STATUS_R_1 = (byte) 0X06;//0x01=第一次按下, 0x03=长按,0x02=抬起,其他=无效
    public static final byte TP_POSITION_R_4 = (byte) 0X07;//触摸屏按压的坐标位置:X_H:L Y_H:L
    public static final byte TPC_ENABLE_R_W_1 = (byte) 0X0B;//0x00=触控不启动, 其他=触控启动商店时默认为0xFF
    public static final byte RUN_TIME_R_4 = (byte) 0X0C;//0x0c -- 0x0f.上电运行时间,BCD码表示时分秒,其中小时为两个字节,最大9999:59:59
    public static final byte RO_RC_R_13 = (byte) 0X10;//0x10 -- 0x1c.SD卡配置寄存器的映射,串口只读,串口写无效.
    public static final byte LED_SET_R_1 = (byte) 0X1E;//背光亮度返回值
    public static final byte RTC_COM_ADJ_W_1 = (byte) 0X1F;//0x5a表示用户申请通过串口改写了RTC数据,DGUS修改RTC完成后清零.
    public static final byte RTC_NOW_R_W_16 = (byte) 0X20;//读写RTC: YY:MM:DD:WW:HH:MM:SS+农历YY:MM:DD+天干地支+生肖
    public static final byte EN_LIB_OP = (byte) 0X40;//0x5a表示用户申请进行读字库存储器操作,当DGUS操作完成后清零,每个DGUS周期执行一次读操作.
    public static final byte LIB_OP_MODE_W_1 = (byte) 0X41;//0xa0表示把制定字库控件的数据读入变量存储器控件
    public static final byte LIB_ID_W_1 = (byte) 0X42;//指定字库控件,范围0x40-0x7f,每个字库256KB,对应最大Flash空间16MB
    public static final byte LIB_ADDRESS_W_3 = (byte) 0X43;//制定字库控件的数据操作首(字)地址,范围0x00:00:00-0X01:ff:ff
    public static final byte VP_46_W_2 = (byte) 0X46;//指定变量存储器空间的数据操作首(字)地址,范围0x00:00-0x6f:ff
    public static final byte OP_LENGTH_48_W_2 = (byte) 0X48;//数据操作的(字)长度,范围0x00:01-0x6f:ff
    public static final byte TIMER_0_R_W_2 = (byte) 0X4A;//16bit软件定时器,单位4ms,自然减到0停止.
    public static final byte TIMER_1_R_W_1 = (byte) 0X4C;//8bit软件定时器,单位4ms,自然减到0停止.
    public static final byte TIMER_2_R_W_1 = (byte) 0X4D;//8bit软件定时器,单位4ms,自然减到0停止.
    public static final byte TIMER_3_R_W_1 = (byte) 0X4E;//8bit软件定时器,单位4ms,自然减到0停止.
    public static final byte KEY_CODE_W_1 = (byte) 0X4F;//用户键码,用于触发0x13触控文件:范围0x01-0xff,其中0X00表示无效;DGUS处理键码后会自动清零键码寄存器.
    public static final byte PLAY_MUSIC_SET_W_3 = (byte) 0X50;//格式为0x5a:play_start:play_num,播放制定音乐.play_start是播放起始段,play_num是连续播放段数(0x00将停止播放).
    public static final byte VOLUME_ADJ_W_2 = (byte) 0X53;//格式为0x5a:vol,调整播放音量,音量=VOL/64,上电默认值为0x40.
    public static final byte EN_DBL_OP_R_W_1 = (byte) 0X56;//0x5A表示用户申请进行数据库存储器操作,DGUS操作完成后清零.每个DGUS周期执行一次读或写操作.
    public static final byte OP_MODE_W_1 = (byte) 0X57;//0X50:把变量存储器空间数据写入数据库空间.0xA0:把数据库空间的数据读入变量存储器空间.
    //数据库空间字地址,范围0x00:00:00:00-1D:ff:ff:ff,最大480MW(960MB)数据空间,数据库从物理空间存储空间的第64MB
    //开始存储,和图片存储器空间有重合,每1byte数据库存储器占据2byte物理存储器,使用SD卡导出数据库时,每个字库大小
    //为128KB,编号从236开始,960MB读写时,DGUS会自动处理跨字库情况.
    public static final byte DBL_ADDRESS_W_4 = (byte) 0X58;
    public static final byte VP_5C_W_2 = (byte) 0X5C;//指定变量存储器空间的数据操作首(字)地址,范围0x00:00-0x6f
    public static final byte OP_LENGTH_5E_W_2 = (byte) 0X5E;//数据操作的(字)长度,范围0x00:01-0x6f:ff
    public static final byte SCAN_STATUS_R_1 = (byte) 0XE9;//0x01=触摸屏处于录入状态 0x00=触摸屏未处于录入状态
    public static final byte TPCAL_TRIGER_W_1 = (byte) 0XEA;//写入0x5A启动一次触摸校准,校准完成后会被DGUS清零.
    //写入特殊定义的数值以清除对应的曲线缓冲区数据.
    //0x55:清除全部8条曲线缓冲区数据
    //0x56-0x5d:分别清除CH0-CH7通道的曲线缓冲区数据.
    //曲线缓冲区数据清除完成后,DGUS会将本寄存器清零.
    public static final byte TRENDLINE_CLEAR_W_1 = (byte) 0XEB;
    public static final byte RESET_TRIGER_W_2 = (byte) 0XEE;//0XEE-0XFF,写入0X5AA5会使DGUS屏软件复位一次

}
