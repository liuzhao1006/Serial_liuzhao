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
     * 数据,包括 变量地址,数据,不包含crc校验码
     */
    private byte[] bytes = null;

    private byte[] crc = new byte[2];

    public byte[] getCrc() {
        if (order != 0 || getBytes() != null) {
            byte[] gCrc = new byte[1 + bytes.length];
            gCrc[0] = (byte) getOrder();
            System.arraycopy(bytes, 0, gCrc, 1, bytes.length);
            crc = LzCrcUtils.calcCrc(gCrc);
            byte temp;
            temp = crc[0];
            crc[0] = crc[1];
            crc[1] = temp;
        }
        return crc;
    }

    public void setCrc(byte[] crc) {
        this.crc = crc;
    }

    /**
     * 访问类型, 主要是设置数据地址的长度,目前如果是访问变量地址两个字节.
     */
    private LzType type;

    /**
     * 获取指令类型.根据指令类型来判断变量地址
     *
     * @return
     */
    public LzType getType() {
        switch (getOrder()) {
            case LzOrderMode.WRITE_REGISTER:
                type = LzType.REGISTER_SPACE_WRITE;
                break;
            case LzOrderMode.READ_REGISTER:
                type = LzType.REGISTER_SPACE_READ;
                break;
            case LzOrderMode.READ_VARIABLES:
                type = LzType.VARIABLE_SPACE_READ;
                break;
            case LzOrderMode.WRITE_VARIABLES:
                type = LzType.VARIABLE_SPACE_WRITE;
                break;
            case LzOrderMode.WRITE_CURVE:
                type = LzType.CURVE_SPACE;
                break;
            default:
                type = LzType.ERROR_SPACE;
                break;
        }
        return type;
    }

    /**
     * 地址
     */
    private byte[] address;

    /**
     * 获取地址
     *
     * @return 返回地址
     */
    public byte[] getAddress() {
        switch (getType()) {
            case CURVE_SPACE:
                address = new byte[2];
                break;
            case ERROR_SPACE:
                address = new byte[1];
                break;
            case REGISTER_SPACE_READ:
                address = new byte[1];
                break;
            case REGISTER_SPACE_WRITE:
                address = new byte[1];
                break;
            case VARIABLE_SPACE_READ:
                address = new byte[2];
                break;
            case VARIABLE_SPACE_WRITE:
                address = new byte[2];
                break;

        }
        System.arraycopy(bytes, 0, address, 0, address.length);
        return address;
    }

    public LzParser() {
    }

    public byte[] getHead() {
        return head;
    }

    public void setHead(byte[] head) {
        this.head = head;
    }

    public byte getCount() {
        //指令(1) + 数据(N) + crc(2)
        if (getBytes() != null)
            count = (byte) (1 + getBytes().length + 2);
        return count;
    }

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

        getAddress();
    }


    @Override
    public String toString() {
        return "LzParser{" +
                "head=" + Arrays.toString(head) +
                ", count=" + count +
                ", order=" + order +
                ", bytes=" + Arrays.toString(bytes) +
                ", crc=" + Arrays.toString(crc) +
                ", type=" + type +
                ", address=" + Arrays.toString(address) +
                '}';
    }
}
