package com.lz.base.protocol;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/1/4 上午10:13
 * 描述     :
 */
public enum LzType {

    REGISTER_SPACE_WRITE,//寄存器写
    REGISTER_SPACE_READ,//寄存器读
    VARIABLE_SPACE_WRITE,//变量写
    VARIABLE_SPACE_READ,//变量读
    CURVE_SPACE,//曲线绘制
    ERROR_SPACE,//错误地址
}
