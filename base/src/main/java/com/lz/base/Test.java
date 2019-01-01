package com.lz.base;

import android.util.Log;

import com.lz.base.log.LogUtils;
import com.lz.base.observe.LzObserve;
import com.lz.base.protocol.CrcUtils;
import com.lz.base.protocol.OrderMode;
import com.lz.base.protocol.SyMessage;
import com.lz.base.util.ConvertUtil;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 作者      : 刘朝
 * 创建日期  : 2018/12/22 下午3:31
 * 描述     :
 */
public class Test {


    private LzObserve observe;

    public Test() {
        resetTest();

        observe = new LzObserve();
    }

    /*被观察者*/


    enum LZParser {
        LZ_HEAD,//帧头
        LZ_LEN,//长度
        LZ_ORDER,//指令
        LZ_DATA,//数据
        LZ_CRC,//crc
        LZ_ERROR,//crc校验不通过
        LZ_COMPLETE,//完成
    }

    private LZParser lzParser = LZParser.LZ_LEN;

    private byte[] bHead = new byte[2];//存储帧头的
    private byte[] bCrc = new byte[2];//存储crc

    public byte[] bytes;//一包数据,包括帧头,长度,指令,数据,crc
    private byte[] bData ;//数据内容,这个数据内容需要初始化,初始化的地方有两个,第一个是在获取到长度的时候初始化, 第二个是在获取到第一个数据内容的时候

    private int index;//数据下标.这个值确定了当前过来的值要存放的位置.
    public byte[] unPack(byte b) {
        switch (lzParser) {
            case LZ_CRC:
                if(bCrc[1] == 0x00){
                    bCrc[1] = b;
                    LogUtils.i("LZ_CRC:crc1" + ConvertUtil.byteToHex(bCrc[1]));
                }else if(bCrc[0] == 0X00){
                    bCrc[0] = b;
                    LogUtils.i("LZ_CRC:crc0" + ConvertUtil.byteToHex(bCrc[0]));
                }

                if(bCrc[1] != 0x00 && bCrc[0] != 0x00){
                    //此时说明crc都有值了.可以开始校验了
                    //组装数据
                    System.out.println("LZ_CRC:获取到的crc " + ConvertUtil.bytesToHexString(bCrc));
                    LogUtils.i("LZ_CRC:bCrc " + ConvertUtil.bytesToHexString(bCrc));//33 e6
                    byte[] currentCrc = new byte[1 + bData.length];
                    currentCrc[0] = bytes[3];
                    System.arraycopy(bData,0,currentCrc, 1,bData.length);
                    LogUtils.i("LZ_CRC:currentCrc " + ConvertUtil.bytesToHexString(currentCrc));
                    byte[] cByteCrc = CrcUtils.calcCrc(currentCrc);//e6 33
                    LogUtils.i("LZ_CRC:cByteCrc " + ConvertUtil.bytesToHexString(cByteCrc));
                    boolean check = CrcUtils.checkCrc(cByteCrc, bCrc);
                    System.out.println("LZ_CRC:" + check);


                    LogUtils.i("LZ_CRC:" + check);
                    System.arraycopy(bCrc,0,bytes,bytes.length - 2,2);
                    System.out.println("LZ_CRC:" + ConvertUtil.bytesToHexString(bytes));
                    LogUtils.i("LZ_CRC:" + ConvertUtil.bytesToHexString(bytes));
                    if(!check){
                        lzParser = LZParser.LZ_ERROR;
                        unPack((byte) 0);//重新调一下这个函数,目的是为了给回到起点.
                    }else {
                        lzParser = LZParser.LZ_COMPLETE;
                        unPack((byte) 0);//重新调一下这个函数,目的是为了给回到起点.
                    }
                }
//                else {
//                    lzParser = LZParser.LZ_ERROR;
//                    unPack((byte) 0);//重新调一下这个函数,目的是为了给回到起点.
//                }
                break;
            case LZ_LEN://长度包括:指令,数据,校验
                //这里收到了长度的值了,判断这个长度范围
                // len = [3,255];最大不过0xff.最小三位,包括指令,一个字节; 地址,两个字节
                if (b < 0x03) {//当前长度值小于三个字节,那么此时认为这个包是有问题的
                    lzParser = LZParser.LZ_ERROR;//错误,跳转到错误状态
                    unPack((byte) 0);//重新调一下这个函数,目的是为了给回到起点.
                } else {//如果当前正确,跳转到指令数据
                    //2: 帧头. 1: 数据长度. b: 指令 + 数据内容
                    bytes = new byte[2 + 1 + b];
                    //将数据头添加到bytes数组中
                    System.arraycopy(bHead,0,bytes,0,2);
                    bytes[2] = b;//存储长度
                    System.out.println("LZ_LEN:帧头拼装 + 数据长度一起 "+ ConvertUtil.bytesToHexString(bytes));
                    LogUtils.i("LZ_LEN:帧头拼装 + 数据长度一起 "+ ConvertUtil.bytesToHexString(bytes));
                    //存储数据,在这里需要初始化数据长度, 一方面到了才能够确定数据有多少
                    //b:数据长度, -2 :数据中不包含crc校验(2个字节)值, -1 :数据中不包括指令(1个字节)值.
                    bData = new byte[b - 1 - 2];
                    lzParser = LZParser.LZ_ORDER;//状态跳转,到指令中
                }
                break;
            case LZ_DATA://数据,迪文屏数据格式包含5部分,
                // 数据是第四部分,此时数据的长度是根据之前获取的长度计算的.
                // 数据长度为 = 长度(bHead[2]) - 指令(1)
                if(index < bData.length - 1){//存储数据条件
                    bData[index++] = b;//存储
                }else if(index == bData.length - 1){//存储最后一条条件
                    bData[index] = b;//存储
                    System.arraycopy(bData,0,bytes,4,bData.length);
                    System.out.println("LZ_DATA:数据拼装 " + ConvertUtil.bytesToHexString(bytes));
                    lzParser = LZParser.LZ_CRC;//状态跳转,到crc校验中
                }else {//没有在这个范围内,说明异常了.
                    lzParser = LZParser.LZ_ERROR;
                    unPack((byte) 0);//重新调一下这个函数,目的是为了给回到起点.
                }
                break;
            case LZ_HEAD:
                if (b == (byte) 0x5A) {//判断是不是第一帧
                    bHead[0] = b;//第一次进来,赋值,
                } else if (b == (byte) 0xA5) {//判断还不是第二针
                    bHead[1] = b;//第二次进来,再次赋值
                }
                if (bHead[0] != (byte) 0x5A) {//如果第一帧就错了,那就跳转到错误状态
                    lzParser = LZParser.LZ_ERROR;
                    unPack((byte) 0);//重新调一下这个函数,目的是为了给回到起点.
                    break;
                }
                //第一帧是对的,进入第二帧,
                if (bHead[1] != (byte) 0x00) {//第二帧有值了
                    if (bHead[1] != (byte) 0xA5) {//判断第二帧的值,错误
                        lzParser = LZParser.LZ_ERROR;//错误,跳转到错误状态
                        unPack((byte) 0);//重新调一下这个函数,目的是为了给回到起点.
                    } else if (bHead[1] == (byte) 0xA5) {//第二帧的值是正确的
                        System.out.println("LZ_HEAD:帧头 " + ConvertUtil.bytesToHexString(bHead));
                        LogUtils.i("LZ_HEAD:帧头 " + ConvertUtil.bytesToHexString(bHead));
                        lzParser = LZParser.LZ_LEN;//正确,跳转到第三帧
                    }
                }

                break;
            case LZ_ERROR://反正遇到问题了
                System.out.println("LZ_ERROR:遇到问题了");
                LogUtils.i("LZ_ERROR:遇到问题了");
                resetTest();//清空数据,重新再来.
                break;
            case LZ_ORDER://指令
                //接收到的正确的指令
                if (b == (byte) OrderMode.READ_REGISTER
                        || b == (byte) OrderMode.WRITE_REGISTER
                        || b == (byte) OrderMode.READ_VARIABLES
                        || b == (byte) OrderMode.WRITE_VARIABLES
                        || b == (byte) OrderMode.WRITE_CURVE) {
                    bytes[3] = b;
                    System.out.println("LZ_ORDER:指令拼装 " + ConvertUtil.bytesToHexString(bytes));
                    LogUtils.i("LZ_ORDER:指令拼装 " + ConvertUtil.bytesToHexString(bytes));
                    //在这里需要做一件事, 就是再次初始化数据数组,条件是数据数组为空,并且长度大于3时才能够创建
                    if(bData == null && bytes[2] > 0x03){
                        bData = new byte[bytes[2] - 1 - 2];
                    }
                    lzParser = LZParser.LZ_DATA;
                } else {//没有接收到正确的指令.跳转到错误状态去.
                    lzParser = LZParser.LZ_ERROR;
                    unPack((byte) 0);//重新调一下这个函数,目的是为了给回到起点.
                }
                break;
            case LZ_COMPLETE://完成了
                //清空数据,将老数据丢到队列中去
                System.out.println("LZ_COMPLETE:完成的数据 "+ConvertUtil.bytes2String(bytes));
                LogUtils.i("LZ_COMPLETE:完成的数据 "+ConvertUtil.bytes2String(bytes));
                SyMessage message = new SyMessage();
                message.setOrder(bytes[3]);
                message.setCount(bytes[2]);
                message.setBytes(bData);
                message.setAdress(new byte[]{bData[0],bData[1]});
                System.out.println("解包:" + message.toString());
                LogUtils.i("LZ_COMPLETE:解包 " + message.toString());
                resetTest();
                break;
        }

        return null;
    }

    LinkedBlockingQueue queue = new LinkedBlockingQueue();


    public void resetTest(){
        index = 0;
        bytes = null;
        bData = null;
        bHead = new byte[2];
        bCrc = new byte[2];
        lzParser = LZParser.LZ_HEAD;
    }



    public interface IComplete{
        void unPack(byte[] bytes);
    }

    public static void main(String[] args) {
        Test test = new Test();
        byte[] bsrc = {0x5a, (byte) 0xa5,0x0b, (byte) 0x80,0x11,0x12,0x02,0x05,0x00,0x03, (byte) 0xff,0x00, (byte) 0xe5, (byte) 0xe5};
        //5a a5 0b 80 11 12 02 05 00 03 ff 00 e5 e5
        test.resetTest();
        for (byte b : bsrc) {
            test.unPack(b);
        }
        System.out.println("---------------------");
        Test t2 = new Test();
        byte[] b2 = {0x5a, (byte) 0xa5,0x0b, (byte) 0x80,0x11,0x12,0x02,0x05,0x00,0x03, (byte) 0xff,0x00, (byte) 0xe5, (byte) 0xe5,0x5a};
        //5a a5 0b 80 11 12 02 05 00 03 ff 00 e5 e5
        t2.resetTest();
        for (byte b : b2) {
            t2.unPack(b);
        }

    }

}
