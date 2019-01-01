package com.lz.base.protocol;

import com.lz.base.util.ConvertUtil;

import java.util.Arrays;

/**
 * 作者      : 刘朝
 * 创建日期  : 2018/12/28 下午2:14
 * 描述     :
 */
public class SyMessage {

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



    public SyMessage(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * 组包
     * @return 返回组号包的数组
     */
    public byte[] pack(){

        //数据包结构, 帧头, 数据长度, 指令, 数据, CRC校验
        //数据内容
        if(bytes == null ){
            throw new CheckDataException("数据内容异常");
        }
        if(order == 0){
            throw new CheckDataException("指令不能为空");
        }
        if(adress == null){
            throw new CheckDataException("数据地址不能为空");
        }

        byte[] dataValue = getDataValue(bytes);
        System.out.println("组装,crc校验数据" + ConvertUtil.bytesToHexString(dataValue));
        if(dataValue == null){
            throw new CheckDataException("数据内容为空");
        }
        byte[] crcValue = getCrcValue(dataValue);
        System.out.println("组包,crc值: "+ConvertUtil.bytes2String(crcValue));
        if(crcValue.length != 2){
            throw new CheckDataException("Crc校验异常");
        }
        int count = 1 + 2 + bytes.length + 2;
        System.out.println("数据长度 " + count);
        if(count < 5){
            throw new CheckDataException("数据包结构错误");
        }
        byte[] bs = new byte[2 + 1 + count];
        System.arraycopy(head,0,bs,0,head.length);
        System.out.println("组装, 添加包头:" + ConvertUtil.bytesToHexString(bs));
        bs[2] = (byte)(0xFF & count);
        System.out.println("组装, 添加数据长度:" + ConvertUtil.bytesToHexString(bs));
        System.arraycopy(dataValue,0,bs, 3,dataValue.length);
        System.arraycopy(crcValue,0,bs,bs.length - 2, crcValue.length);
        System.out.println("组包: " + ConvertUtil.bytesToHexString(bs));
        return bs;
    }

    public SyMessage() {
    }

    /**
     * 解包
     * @param msg 数据包
     * @return 返回对象
     */
    public SyMessage unPack(byte[] msg){
        if(msg.length < 8){
            throw new CheckDataException("数据包不完整");
        }
        if (msg[0] != head[0] && msg[1] != head[1]) {
            throw new CheckDataException("帧头错误");
        }
        byte count = msg[2];//9
        byte order = msg[3];
        byte[] adress = new byte[2];
        System.arraycopy(msg, 5,adress,0,2);
        byte[] dataValue = new byte[msg.length - 5];//
        System.out.println("解包数据长度: " + dataValue.length);
        System.arraycopy(msg,3,dataValue,0,msg.length - 5);
        System.out.println(ConvertUtil.bytes2String(dataValue));
        byte[] crcValue = getCrcValue(dataValue);//计算的crc值
        byte[] msgCrc = new byte[2];
        System.arraycopy(msg,msg.length-2,msgCrc,0,2);
        System.out.println(ConvertUtil.bytes2String(crcValue) + " " + ConvertUtil.bytesToHexString(msgCrc));
        if(!CrcUtils.checkCrc(msgCrc,crcValue)){
            throw new CheckDataException("数据校验不通过");
        }
        SyMessage message = new SyMessage();
        message.setOrder(order);
        message.setBytes(dataValue);
        message.setCount(count);
        message.setAdress(adress);
        return message;
    }


    public byte[] getDataValue(byte[] data){
        byte[] bs = new byte[1 + 2 + data.length];
        if(getOrder() == 0){
            return null;
        }
        if(getAdress() == null){
            return null;
        }

        switch (getOrder()) {
            case OrderMode.WRITE_REGISTER:
                bs[0] = (byte) OrderMode.WRITE_REGISTER;
                break;
            case OrderMode.READ_REGISTER:
                bs[0] = (byte) OrderMode.READ_REGISTER;
                break;
            case OrderMode.READ_VARIABLES:
                bs[0] = (byte) OrderMode.READ_VARIABLES;
                break;
            case OrderMode.WRITE_CURVE:
                bs[0] = (byte) OrderMode.WRITE_CURVE;
                break;
            case OrderMode.WRITE_VARIABLES:
                bs[0] = (byte) OrderMode.WRITE_VARIABLES;
                break;
        }
        System.arraycopy(getAdress(),0, bs,1,2);
        System.arraycopy(data,0,bs,3,data.length);
        System.out.println("加上指令的数据: " + ConvertUtil.bytes2String(bs));
        System.out.println("------");
        return bs;
    }

    public byte[] getCrcValue(byte[] bs) {
        byte[] b = ConvertUtil.intToByteArray(CrcUtils.calcCrc16(bs));
        System.out.println("crc校验后的值" + ConvertUtil.bytes2String(b));
        byte[] bCrc = new byte[2];
        System.arraycopy(b,2,bCrc,0,b.length - 2);
        System.out.println("crcValue: "+ConvertUtil.bytes2String(bCrc));

        return bCrc;
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
        return count;
    }

    public void setCount(byte count) {
        this.count = count;
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
    }

    @Override
    public String toString() {
        return "SyMessage{" +
                "head=" + Arrays.toString(head) +
                ", count=" + count +
                ", order=" + order +
                ", bytes=" + Arrays.toString(bytes) +
                ", adress=" + Arrays.toString(adress) +
                '}';
    }
}
