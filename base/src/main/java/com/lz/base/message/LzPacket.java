package com.lz.base.message;

import java.io.Serializable;

/**
 * 作者      : 刘朝
 * 创建日期  : 2018/12/30 上午11:08
 * 描述     :
 */
public class LzPacket implements Serializable, Cloneable {

    private final static String TAG = "LzPacket";
    private static final long serialVersionUID = 2095947771227815314L;

    public static final int MAVLINK_STX = 23205;//消息起始标记,5AA5
    public int orderId;//消息指令id
    public int adressId;//消息地址id
    public int lenId;//消息长度
    public LzPayload payload;

    public LzPacket(int payloadLength){
        lenId = payloadLength;
        payload = new LzPayload(payloadLength);
    }

    @Override
    protected LzPacket clone()  {

        try{
        LzPacket packet = (LzPacket) super.clone();
        packet.payload = payload.clone();
        return packet;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("CloneNotSupportedException found.", e);
        }

    }

    /**
     * Check if the size of the Payload is equal to the "len" byte
     * 检测数据长度
     */
    public boolean payloadIsFilled() {
        return payload.size() >= lenId;
    }


}
