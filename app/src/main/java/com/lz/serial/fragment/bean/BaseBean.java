package com.lz.serial.fragment.bean;

import java.io.Serializable;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/20 下午2:25
 * 描述     : 基础类
 */
public abstract class BaseBean implements Serializable {

    //时间戳
    public long timeStamp = System.currentTimeMillis();



}
