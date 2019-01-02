package com.lz.base.protocol;

import java.util.Arrays;

/**
 * 作者      : 刘朝
 * 创建日期  : 2018/12/28 下午2:14
 * 描述     :
 */
public class LzParser {

    /**
     * 帧头
     */
    private byte[] head = {(byte) 0x5A, (byte) 0xA5};

    /**
     * 数据长度
     */
    private byte count;

    /**
     * 指令 80, 81, 82, 83, 84
     */
    private int order;

    /**
     * 数据
     */
    private byte[] bytes = null;

    /**
     * 数据地址
     */
    private byte[] adress = null;


    public LzParser(byte[] bytes) {
        this.bytes = bytes;
    }


    public LzParser() {
    }


    public byte[] getAdress() {
        return adress;
    }

    public void setAdress(byte[] adress) {
        this.adress = adress;
    }

    public byte[] getHead() {
        return head;
    }

    public void setHead(byte[] head) {
        this.head = head;
    }

    public byte getCount() {
        //指令(1) + 数据(N) + crc(2)
        if (getAdress() != null && getBytes() != null)
            count = (byte) (1 + getAdress().length + getBytes().length + 2);
        return count;
    }

//    public void setCount(byte count) {
//        this.count = count;
//    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        return "LzParser{" +
                "head=" + Arrays.toString(head) +
                ", count=" + getCount() +
                ", order=" + order +
                ", bytes=" + Arrays.toString(bytes) +
                ", adress=" + Arrays.toString(adress) +
                '}';
    }
}
