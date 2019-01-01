package com.lz.base.message;

import java.io.Serializable;

/**
 * 作者      : 刘朝
 * 创建日期  : 2018/12/30 上午11:06
 * 描述     : 消息处理工具
 */
public abstract class LzMessage implements Serializable {

    private static final long serialVersionUID = -7754622750478538539L;

    public int adressId;//地址
    public int orderId;//指令
    public int lenId;//消息长度

    public abstract LzPacket pack();//组包
    public abstract void unPack(LzPayload payload);//解包


}
