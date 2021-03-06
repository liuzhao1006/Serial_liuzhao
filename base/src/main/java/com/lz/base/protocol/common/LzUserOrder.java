package com.lz.base.protocol.common;

import com.lz.base.protocol.LzCrcUtils;
import com.lz.base.protocol.LzOrderMode;
import com.lz.base.util.ConvertUtil;

import java.io.UnsupportedEncodingException;
import java.security.PublicKey;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/1/4 下午3:07
 * 描述     : 关于屏幕的常用指令
 */
public class LzUserOrder {

    /**
     * 读取背光亮度值
     *
     * @return
     */
    public static byte[] getLedLight() {
        byte[] bytes = {(byte) 0X5A, (byte) 0XA5, (byte) 0X05, (byte) 0X81, (byte) 0X01, (byte) 0X01, (byte) 0x00, (byte) 0x00};
        return getCrc(bytes);
    }

    /**
     * 发送设置背光亮度
     *
     * @param b 0x00-0x40
     * @return 返回发送的指令
     */
    public static byte[] setLedLight(byte b) {

        byte[] bytes = {(byte) 0X5A, (byte) 0XA5, (byte) 0X05, (byte) 0X81, (byte) 0X01, b, (byte) 0x00, (byte) 0x00};

        return getCrc(bytes);
    }

    /**
     * 发送读取当前页面编号指令
     *
     * @return 返回发送的指令 ：5a a5 05 81 03 02 a0 d9
     * 应答： 5A A5 07 81 03 02 00 02 38 5B
     * 头：5A A5
     * 数据长度：07
     * 指令：81
     * 地址：03 02
     * 数据（页面编号）：00 02
     * crc校验：38 5B
     */
    public static byte[] getPageId() {
        byte[] bytes = {(byte) 0X5A, (byte) 0XA5, (byte) 0X05, (byte) 0X81, (byte) 0X03, (byte) 0X02, (byte) 0x00, (byte) 0x00};
        return getCrc(bytes);
    }

    /**
     * 发送设置页面切换指令
     *
     * @param b 需要切换的页面
     * @return 返回发送的指令
     */
    public static byte[] setPageId(byte b) {
        byte[] bytes = {(byte) 0X5A, (byte) 0XA5, (byte) 0X06, (byte) 0X80, (byte) 0X03, (byte) 0X00, b, (byte) 0x00, (byte) 0x00};
        return getCrc(bytes);
    }

    /**
     * 启动蜂鸣器
     *
     * @param b 鸣笛时长
     * @return 返回发送的指令
     */
    public static byte[] setBuzzer(byte b) {
        byte[] bytes = {(byte) 0X5A, (byte) 0XA5, (byte) 0X05, (byte) 0X80, (byte) 0X02, b, (byte) 0x00, (byte) 0x00};
        return getCrc(bytes);
    }

    /**
     * 复位操作
     *
     * @return 返回发送的指令
     */
    public static byte[] setReset() {
        byte[] bytes = {(byte) 0X5A, (byte) 0XA5, (byte) 0X06, (byte) 0X80, (byte) 0XEE, (byte) 0X5A, (byte) 0XA5, (byte) 0x00, (byte) 0x00};
        return getCrc(bytes);
    }

    /**
     * 触摸屏校准
     *
     * @return 返回发送的指令
     */
    public static byte[] setCalibration() {
        return getCrc(new byte[]{(byte) 0X5A, (byte) 0XA5, (byte) 0X05, (byte) 0X80, (byte) 0XEA, (byte) 0X5A, (byte) 0x00, (byte) 0x00});
    }

    /**
     * 触摸功能
     *
     * @param b 0表示关闭触摸功能,1表示开启触摸功能.
     * @return 返回发送的指令
     */
    public static byte[] setTouchFunction(byte b) {
        return getCrc(new byte[]{(byte) 0X5A, (byte) 0XA5, (byte) 0X05, (byte) 0X80, (byte) 0X0B, b, (byte) 0x00, (byte) 0x00});
    }

    /**
     * 弹窗显示
     *
     * @param b 弹窗的id
     * @return 返回发送的指令
     */
    public static byte[] setPopupWindow(byte b) {
        byte[] bytes = new byte[]{(byte) 0X5A, (byte) 0XA5, (byte) 0X05, (byte) 0X80, (byte) 0X4F, b, (byte) 0x00, (byte) 0x00};
        return getCrc(bytes);
    }

    /**
     * 获取迪文屏幕软件的版本号
     *
     * @return 获取迪文屏幕软件版本号命令 ： 5a a5 05 81 00 01 e0 28
     */
    public static byte[] getDGUSVersion() {
        return getCrc(new byte[]{(byte) 0X5A, (byte) 0XA5, (byte) 0X05, (byte) 0X81, (byte) 0X00, (byte) 0X01, (byte) 0x00, (byte) 0x00});
    }

    /**
     * 读取0x20寄存器中所保存的当前RTC值（年月日，星期，时分秒）。
     *
     * @return 指令命令 ：5a a5 05 81 20 07 79 ea
     * <p>
     * 返回数据格式为：5A A5 0C 81 20 07 19 01 20 00 22 16 13 19 7D
     * 头：5A A5
     * 数据长度：0C
     * 指令：81
     * 地址：20 07
     * 时间：19（年） 01（月） 20（日） 00（星期） 22（时） 16（分） 13（秒） 19 7D（crc校验）
     */
    public static byte[] getCurrentTime() {
        return getCrc(new byte[]{(byte) 0X5A, (byte) 0XA5, (byte) 0X05, (byte) 0X81, (byte) 0X20, (byte) 0X07, (byte) 0x00, (byte) 0x00});
    }


    /**
     * 设置int类型值
     *
     * @param order   发送指令
     * @param address 发送地址
     * @param number  int类型值
     * @return 返回发送的数据，包括crc校验
     */
    public static byte[] setNumbericIntType(byte order, byte[] address, int number) {
        byte[] value = ConvertUtil.intToSubByteArray(number);
        return setValue(order, address, value);
    }


    /**
     * 设置int类型值
     *
     * @param order   发送指令
     * @param address 发送地址
     * @param number  float类型值
     * @return 返回发送的数据，包括crc校验
     */
    public static byte[] setNumbericFloatType(byte order, byte[] address, float number) {
        int numberInt = Float.floatToIntBits(number);
        byte[] value = ConvertUtil.intToSubByteArray(numberInt);
        return setValue(order, address, value);
    }

    /**
     * 读取地址下的值
     * @param address 地址
     * @return 返回报文
     * 示例
     * 发送指令：5a a5 06 83 10 00 01 e9 a5
     * 接收指令：5A A5 08 83 10 00 01 00 0F CF EF
     */
    public static byte[] getNumbericValue(byte[] address){
        int count =  1 + address.length + 1 + 2;
        byte[] bytes = new byte[2+ 1 +count];
        bytes[0] = LzConstants.HEAD_PER;
        bytes[1] = LzConstants.HEAD_END;
        bytes[2] = ConvertUtil.intToByte(count);
        bytes[3] = ConvertUtil.intToByte(LzOrderMode.READ_VARIABLES);
        System.arraycopy(address, 0, bytes, 4, address.length);
        bytes[4 + address.length] = ConvertUtil.intToByte(0x01);
        return getCrc(bytes);
    }


    /**
     * 设置文本变量
     *
     * @param text 文本内容
     * @return 返回报文
     */
    public static byte[] setTextType(byte order, byte[] address, String text) {
        try {
            return setValue(order, address, text.getBytes("GBK"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 设置变量颜色
     *
     * @param address 描述指针 地址
     * @param color   颜色值
     * @return 返回报文
     */
    public static byte[] setTextColor(byte order, byte[] address, byte[] color) {
        return setColorValue(order, address, color);
    }

    /**
     * 获取变量的值
     *
     * @param address 变量地址
     * @return 返回报文
     */
    public static byte[] getTextValue(byte[] address) {

        return null;
    }

    /**
     * 操作哪个变量,需要跳转到哪个页面
     *
     * @param address  点击的地址
     * @return 返回报文
     */
    public static byte[] showDialog(byte address) {
        byte[] bytes = new byte[]{(byte)0x5a,(byte)0xa5,(byte)0x05, (byte) LzOrderMode.WRITE_REGISTER,(byte)0x4f,address,(byte)0x00,(byte)0x00};
        return getCrc(bytes);
    }

    /**
     * 隐藏菜单功能，这个做法很骚情，一般人还真想不到。
     * @param address 发送键值，键控
     * @return
     */
    public static byte[] hideDialog( byte address) {
        byte[] bytes = new byte[]{(byte)0x5a,(byte)0xa5,(byte)0x05, (byte) LzOrderMode.WRITE_REGISTER,(byte)0x4f,address,(byte)0x00,(byte)0x00};
        return getCrc(bytes);
    }


    /**
     * 显示矩形，并填充颜色。 有点小问题。
     * @return
     */
    public static byte[] showRectangle(){
        byte[] bytes = new byte[]{
                (byte)0x5a,(byte)0xa5,//针头
                (byte)0x15,(byte)0x82,//长度，写入页面指令
                (byte)0x19,(byte)0x01,//变量地址
                (byte)0x00,(byte)0x04,//矩形
                (byte)0x02,(byte)0x12,//
                (byte)0x00,(byte)0x6d,//左上坐标
                (byte)0x02,(byte)0x85,
                (byte)0x00,(byte)0x59,//右下坐标
                (byte)0x00,(byte)0x6d,
                (byte)0x02,(byte)0x85,//显示坐标
                (byte)0xf8,(byte)0x00,//显示颜色
                (byte)0x00,(byte)0x00,//crc校验
        };
        return getCrc(bytes);
    }

    /**
     * 设置当前时间给屏幕
     *
     * @return 返回发送的指令
     * 设置当前时间给屏幕 5a a5 03 80 1f 5a 19 01 06 09 19 18 34 a6
     */
    public static byte[] setCurRtcTime() {
        String year;
        String month;
        String day;
        String hour;
        String minute;
        String second;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate ld = LocalDate.now();
            year = (ld.getYear() + "").substring(2, 4);
            month = (ld.getMonthValue() + 1) + "";
            day = ld.getDayOfMonth() + "";
            LocalTime lt = LocalTime.now();
            hour = lt.getHour() + "";
            minute = lt.getMinute() + "";
            second = lt.getSecond() + "";

        } else {
            Calendar c = Calendar.getInstance();
            year = (c.get(Calendar.YEAR) + "").substring(2, 4);
            month = (c.get(Calendar.MONTH) + 1) + "";
            day = c.get(Calendar.DATE) + "";
            hour = c.get(Calendar.HOUR_OF_DAY) + "";
            minute = c.get(Calendar.MINUTE) + "";
            second = c.get(Calendar.SECOND) + "";
        }
        if (month.length() < 2) {
            month = 0 + month;
        }
        if (day.length() < 2) {
            day = 0 + day;
        }
        if (hour.length() < 2) {
            hour = 0 + hour;
        }
        if (minute.length() < 2) {
            minute = 0 + minute;
        }
        if (second.length() < 2) {
            second = 0 + second;
        }
        String sb = year +
                month +
                day +
                hour +
                minute +
                second;
        System.out.println("时间 " + year + " " + month + " " + day + " " + hour + " " + minute + " " + second);
        byte[] time = ConvertUtil.hexStringToBytes(sb);
        byte[] bytes = new byte[6 + time.length + 2];
        byte[] per = new byte[]{(byte) 0X5A, (byte) 0XA5,
                (byte) 0X03, (byte) 0X80, (byte) 0X1F,
                (byte) 0X5A};
        System.arraycopy(per, 0, bytes, 0, per.length);
        System.arraycopy(time, 0, bytes, 6, time.length);
        return getCrc(bytes);
    }

    /**
     * 设置值，
     *
     * @param order   指令
     * @param address 地址
     * @param value   值
     * @return 返回报文
     */
    public static byte[] setValue(byte order, byte[] address, byte[] value) {
        int addressLength = address.length;
        int numberLength = value.length;
        int count = 1 + addressLength + numberLength + 2;
        byte[] bytes = new byte[2 + 1 + count];
        bytes[0] = LzConstants.HEAD_PER;
        bytes[1] = LzConstants.HEAD_END;
        bytes[2] = ConvertUtil.intToByte(count);
        bytes[3] = order;
        System.arraycopy(address, 0, bytes, 4, addressLength);
        System.arraycopy(value, 0, bytes, 4 + addressLength, numberLength);
        byte[] crc = getCrc(bytes);
        System.out.println("设置文本变量 " + ConvertUtil.bytesToHexString(crc));
        return crc;
    }

    public static byte[] setColorValue(byte order, byte[] address, byte[] color) {
        int addressLength = address.length;
        int colorLength = color.length;
        int count = 1 + addressLength + colorLength + 2;
        byte[] bytes = new byte[2 + 1 + count];
        bytes[0] = LzConstants.HEAD_PER;
        bytes[1] = LzConstants.HEAD_END;
        bytes[2] = (byte) count;
        bytes[3] = order;
        System.arraycopy(address, 0, bytes, 4, addressLength);
        System.arraycopy(color, 0, bytes, 4 + addressLength, colorLength);
        byte[] crc = getCrc(bytes);
        System.out.println("设置文本颜色 " + ConvertUtil.bytesToHexString(crc));
        return crc;
    }

    /**
     * 计算crc校验值
     *
     * @param b 未校验的crc值
     * @return 返回还是b，而且已经校验了crc的值
     */
    public static byte[] getCrc(byte[] b) {
        byte[] crcData = new byte[b.length - 3 - 2];
        System.arraycopy(b, 3, crcData, 0, crcData.length);
        byte[] crc = LzCrcUtils.calcCrc(crcData);
        byte temp;
        temp = crc[0];
        crc[0] = crc[1];
        crc[1] = temp;
        System.arraycopy(crc, 0, b, b.length - 2, 2);
//        System.out.println("计算crc校验值 " + ConvertUtil.bytesToHexString(b));
        return b;
    }

    public static void main(String[] args) {
        System.out.println("读取背光亮度值 " + ConvertUtil.bytes2String(getLedLight()));
        System.out.println("发送读取当前页面编号指令 " + ConvertUtil.bytes2String(getPageId()));
        System.out.println("发送设置背光亮度 " + ConvertUtil.bytesToHexString(setLedLight((byte) 0x30)));
        System.out.println("发送设置页面切换指令 " + ConvertUtil.bytesToHexString(setPageId((byte) 0x04)));
        System.out.println("启动蜂鸣器 " + ConvertUtil.bytesToHexString(setBuzzer((byte) 0x10)));
        System.out.println("复位操作 " + ConvertUtil.bytesToHexString(setReset()));
        System.out.println("触摸屏校准 " + ConvertUtil.bytesToHexString(setCalibration()));
        System.out.println("触摸功能 " + ConvertUtil.bytesToHexString(setTouchFunction((byte) 0x00)));
        System.out.println("设置当前时间给屏幕 " + ConvertUtil.bytesToHexString(setCurRtcTime()));
        System.out.println("获取迪文屏幕软件版本号 " + ConvertUtil.bytesToHexString(getDGUSVersion()));
        System.out.println("crc " + ConvertUtil.bytesToHexString(getCrc(new byte[]{(byte) 0X5A, (byte) 0XA5, (byte) 0X05, (byte) 0X81, (byte) 0X20, (byte) 0X07, (byte) 0x00, (byte) 0x00})));
        System.out.println("设置int类型值 " + ConvertUtil.bytes2String(setNumbericIntType((byte) LzOrderMode.READ_REGISTER, new byte[]{(byte) 0x01, (byte) 0x00}, 5)));
        System.out.println("设置float类型值 " + ConvertUtil.bytes2String(setNumbericFloatType((byte) LzOrderMode.READ_REGISTER, new byte[]{(byte) 0x01, (byte) 0x00}, 5.5f)));
        System.out.println("设置文本类型值 " + ConvertUtil.bytes2String(setTextType((byte) LzOrderMode.WRITE_VARIABLES, new byte[]{(byte) 0x12, (byte) 0x34}, "刘朝")));
        System.out.println("------------------");
        System.out.println("int类型转换成byte数组去掉高位" + ConvertUtil.bytesToHexString(ConvertUtil.intToByteArray(0xffff + 0x03)));
        System.out.println("设置文本颜色 " + ConvertUtil.bytesToHexString(setTextColor((byte) 0X82, ConvertUtil.intToByteArray(0xffff + 0x03), new byte[]{(byte) 0xF0, (byte) 0x00, (byte) 0x00})));
        System.out.println("弹窗设置 " + ConvertUtil.bytesToHexString(showDialog((byte)0x01)));
        System.out.println("隐藏菜单 " + ConvertUtil.bytesToHexString(hideDialog( (byte)0x02)));
        setValue((byte)0x84, new byte[]{(byte)0x01, (byte)0x00},new byte[]{(byte)0x64, (byte)0x00, (byte)0x32, (byte)0x01,
                (byte)0x00, (byte)0x00, (byte)0x50, (byte)0x02, (byte)0x00,
                (byte)0x00, (byte)0x64});
        System.out.println("显示曲线 " + ConvertUtil.bytesToHexString(setValue((byte)0x84, new byte[]{(byte)0x01, (byte)0x00},new byte[]{(byte)0x64, (byte)0x00, (byte)0x32, (byte)0x01,
                (byte)0x00, (byte)0x00, (byte)0x50, (byte)0x02, (byte)0x00,
                (byte)0x00, (byte)0x64})));
        System.out.println("读取值 " + ConvertUtil.bytesToHexString(getNumbericValue(new byte[]{(byte)0x10, (byte)0x00})));
        System.out.println("设置矩形 " + ConvertUtil.bytesToHexString(showRectangle()));
    }
}
