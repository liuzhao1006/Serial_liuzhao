package com.lz.base.protocol;

/**
 * 作者      : 刘朝
 * 创建日期  : 2018/12/28 下午3:36
 * 描述     :
 */
public final class LzOrderMode {

    public static final int WRITE_REGISTER = 0x80;//写入寄存器
    public static final int READ_REGISTER =  0x81;//读取寄存器
    public static final int WRITE_VARIABLES =  0x82;//写入变量
    public static final int READ_VARIABLES =  0x83;//读取变量
    public static final int WRITE_CURVE =  0x84;//写入曲线

}
