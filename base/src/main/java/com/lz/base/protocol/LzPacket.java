package com.lz.base.protocol;

import com.lz.base.log.LogUtils;
import com.lz.base.observe.LzObserve;
import com.lz.base.observe.Observer;
import com.lz.base.util.ConvertUtil;

/**
 * 作者      : 刘朝
 * 创建日期  : 2018/12/22 下午3:31
 * 描述     : 数据解析工具,单例的数据结构
 */
public class LzPacket {

    private static LzPacket mInstance;

    public static synchronized LzPacket getmInstance() {
        if (mInstance == null) {
            synchronized (LzPacket.class) {
                if (mInstance == null) {
                    mInstance = new LzPacket();
                }
            }
        }
        return mInstance;
    }


    private LzObserve observe;

    private LzPacket() {
        resetTest();
        observe = new LzObserve();
    }

    public void registMessage(Observer observer) {
        observe.registerObserver(observer);
    }

    public void unRegistMessage(Observer observer) {
        observe.removeObserver(observer);
    }

    public void notifyMessage() {
        observe.notifyObservers();
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

    private LZParser lzParser = LZParser.LZ_HEAD;
    private byte[] bHead = new byte[2];//存储帧头的
    private byte[] bCrc = new byte[2];//存储crc
    public byte[] bytes;//一包数据,包括帧头,长度,指令,数据,crc
    private byte[] bData;//数据内容,这个数据内容需要初始化,初始化的地方有两个,第一个是在获取到长度的时候初始化, 第二个是在获取到第一个数据内容的时候
    private int index;//数据下标.这个值确定了当前过来的值要存放的位置.

    /**
     * 不管是一个数据包完成了还是错误了,此时我们必须把存储清除干净,否则会影响下一包数据.
     */
    public void resetTest() {
        index = 0;
        bytes = null;
        bData = null;
        bHead = new byte[2];
        bCrc = new byte[2];
        lzParser = LZParser.LZ_HEAD;
    }

    private void unPackError(LZParser parser) {
        lzParser = parser;//错误,跳转到错误状态
        unPack((byte) 0);//重新调一下这个函数,目的是为了给回到起点.
    }

    public void unPack(byte b) {
        switch (lzParser) {
            case LZ_HEAD:
                if (b == (byte) 0x5A) {//判断是不是第一帧
                    bHead[0] = b;//第一次进来,赋值,
                } else if (b == (byte) 0xA5) {//判断还不是第二针
                    bHead[1] = b;//第二次进来,再次赋值
                }
                if (bHead[0] != (byte) 0x5A) {//如果第一帧就错了,那就跳转到错误状态
                    unPackError(LZParser.LZ_ERROR);
                    break;
                }//第一帧是对的,进入第二帧,
                if (bHead[1] != (byte) 0x00) {//第二帧有值了
                    if (bHead[1] != (byte) 0xA5) {//判断第二帧的值,错误
                        unPackError(LZParser.LZ_ERROR);
                    } else if (bHead[1] == (byte) 0xA5) {//第二帧的值是正确的
                        lzParser = LZParser.LZ_LEN;//正确,跳转到第三帧
                    }
                }
                break;
            case LZ_LEN://长度包括:指令,数据,校验
                //这里收到了长度的值了,判断这个长度范围
                // len = [3,255];最大不过0xff.最小三位,包括指令,一个字节; 地址,两个字节
                // TODO b < 0x03 有待商榷
                if (b < 0x03) {//当前长度值小于三个字节,那么此时认为这个包是有问题的
                    unPackError(LZParser.LZ_ERROR);
                } else {//如果当前正确,跳转到指令数据;2: 帧头. 1: 数据长度. b: 指令 + 数据内容
                    bytes = new byte[2 + 1 + b];
                    //将数据头添加到bytes数组中
                    System.arraycopy(bHead, 0, bytes, 0, 2);
                    bytes[2] = b;//存储长度
                    //存储数据,在这里需要初始化数据长度, 一方面到了才能够确定数据有多少;b:数据长度, -2 :数据中不包含crc校验(2个字节)值, -1 :数据中不包括指令(1个字节)值.
                    bData = new byte[b - 1 - 2];
                    lzParser = LZParser.LZ_ORDER;//状态跳转,到指令中
                }
                break;
            case LZ_ORDER://指令//接收到的正确的指令
                if(isOrder(b)){
                    LogUtils.i("获取到的指令是 " + b);
                    if (bData == null && bytes[2] > 0x03) {
                        bData = new byte[bytes[2] - 1 - 2];
                    }
                    lzParser = LZParser.LZ_DATA;
                }else {
                    //没有接收到正确的指令.跳转到错误状态去.
                    unPackError(LZParser.LZ_ERROR);
                }
                break;
            case LZ_DATA://数据,迪文屏数据格式包含5部分,
                // 数据是第四部分,此时数据的长度是根据之前获取的长度计算的.数据长度为 = 长度(bHead[2]) - 指令(1)
                if (index < bData.length - 1) {//存储数据条件
                    bData[index++] = b;//存储
                } else if (index == bData.length - 1) {//存储最后一条条件
                    bData[index] = b;//存储
                    System.arraycopy(bData, 0, bytes, 4, bData.length);
                    lzParser = LZParser.LZ_CRC;//状态跳转,到crc校验中
                } else {//没有在这个范围内,说明异常了.
                    unPackError(LZParser.LZ_ERROR);
                }
                break;
            case LZ_CRC:
                if (bCrc[1] == 0x00) {
                    bCrc[1] = b;
                } else if (bCrc[0] == 0X00) {
                    bCrc[0] = b;
                }
                if (bCrc[1] != 0x00 && bCrc[0] != 0x00) {
                    //此时说明crc都有值了.可以开始校验了
                    //组装数据 指令.length == 1 , 地址+value == bData
                    byte[] currentCrc = new byte[1 + bData.length];
                    //把指令保存到新数组中
                    currentCrc[0] = bytes[3];
                    //把其他数据保存到新数组中
                    System.arraycopy(bData, 0, currentCrc, 1, bData.length);
                    //计算获取到的数据的crc值
                    byte[] cByteCrc = LzCrcUtils.calcCrc(currentCrc);//e6 33
                    //把计算出来的crc值和获取到的crc值进行比较
                    boolean check = LzCrcUtils.checkCrc(cByteCrc, bCrc);
                    if (!check) {
                        unPackError(LZParser.LZ_ERROR);
                    } else {
                        //如果校验通过,把数据补全.
                        System.arraycopy(bCrc, 0, bytes, bytes.length - 2, 2);
                        unPackError(LZParser.LZ_COMPLETE);
                    }
                }
                break;
            case LZ_COMPLETE://完成了 清空数据,将老数据丢到队列中去
                LzParser message = new LzParser();
                message.setOrder(ConvertUtil.byteToInt(bytes[3]));
                message.setBytes(bData);
                message.setCrc(bCrc);
                if (observe != null) {
                    observe.setMessage(message);
                    notifyMessage();
                }
                LogUtils.i("Lz_COMPLETE " + message.toString());
                resetTest();
                break;
            case LZ_ERROR://反正遇到问题了
                resetTest();//清空数据,重新再来.
                break;
        }
    }

    private boolean isOrder(byte b){
        switch (b) {
            case (byte) LzOrderMode.READ_REGISTER:
                bytes[3] = (byte) LzOrderMode.READ_REGISTER;//在这里需要做一件事, 就是再次初始化数据数组,条件是数据数组为空,并且长度大于3时才能够创建
                return true;
            case (byte) LzOrderMode.WRITE_REGISTER:
                bytes[3] = (byte) LzOrderMode.WRITE_REGISTER;//在这里需要做一件事, 就是再次初始化数据数组,条件是数据数组为空,并且长度大于3时才能够创建
                return true;
            case (byte) LzOrderMode.READ_VARIABLES:
                bytes[3] = (byte) LzOrderMode.READ_VARIABLES;//在这里需要做一件事, 就是再次初始化数据数组,条件是数据数组为空,并且长度大于3时才能够创建
                return true;
            case (byte) LzOrderMode.WRITE_VARIABLES:
                bytes[3] = (byte) LzOrderMode.WRITE_VARIABLES;//在这里需要做一件事, 就是再次初始化数据数组,条件是数据数组为空,并且长度大于3时才能够创建
                return true;
            case (byte) LzOrderMode.WRITE_CURVE:
                bytes[3] = (byte) LzOrderMode.WRITE_CURVE;//在这里需要做一件事, 就是再次初始化数据数组,条件是数据数组为空,并且长度大于3时才能够创建
                return true;
            default:
                return false;
        }
    }
    public byte[] pack(LzParser parser) {
        if (parser == null) {
            return null;
        }
        System.out.println("pack " + parser.toString());
        byte len = parser.getCount();//数据长度
        byte[] bytes = new byte[2 + 1 + len];//创建一个
        //帧头
        byte[] head = parser.getHead();
        System.arraycopy(head, 0, bytes, 0, head.length);//帧头
        //长度
        bytes[2] = len;
        //指令 + 数据
        byte[] data = new byte[len - 2];
        //指令
        int order = parser.getOrder();
        if (order == LzOrderMode.READ_REGISTER
                || order == LzOrderMode.WRITE_REGISTER
                || order == LzOrderMode.READ_VARIABLES
                || order == LzOrderMode.WRITE_VARIABLES
                || order == LzOrderMode.WRITE_CURVE) {
            data[0] = (byte) order;
        } else {
            //组包过程中,如果指令不合适,那么失败
            return null;
        }

        byte[] value = parser.getBytes();
        if (value != null) {
            System.arraycopy(value, 0, data, 1, value.length);
        }
        System.arraycopy(data, 0, bytes, 3, data.length);
        //crc 校准
        byte[] crc = parser.getCrc();
        System.arraycopy(crc, 0, bytes, bytes.length - 2, 2);
        System.out.println("pack " + ConvertUtil.bytesToHexString(bytes));
        return bytes;
    }


    public static void main(String[] args) {
        LzParser parser = new LzParser();
        parser.setOrder(0x83);
        parser.setBytes(new byte[]{(byte) 0x11, (byte) 0x01,(byte) 0x32, (byte) 0x32});
        LzPacket.getmInstance().pack(parser);

    }
}
