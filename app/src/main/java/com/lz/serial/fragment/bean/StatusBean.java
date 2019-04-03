package com.lz.serial.fragment.bean;

import java.io.Serializable;


/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/22 下午9:30
 * 描述     : 接收的数据共有结构体
 */
public class StatusBean implements Serializable {

    /**
     * 消息编码,0表示正确消息, 非零表示错误消息,
     */
    public int code;

    /**
     * 具体消息内容
     */
    public String content;

    /**
     * 说明
     */
    public String msg;

}
