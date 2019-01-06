package com.lz.base.protocol.common;

import com.lz.base.protocol.LzCrcUtils;
import com.lz.base.util.ConvertUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;

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
        byte[] bytes = {(byte) 0X5A, (byte) 0XA5, (byte) 0X05, (byte) 0X81, (byte) 0X01, (byte) 0X01,(byte)0x00,(byte)0x00};
        byte[] crc = LzCrcUtils.calcCrc(new byte[]{(byte) 0X81, (byte) 0X01, (byte) 0X01});
        byte temp;
        temp = crc[0];
        crc[0] = crc[1];
        crc[1] = temp;
        System.arraycopy(crc,0,bytes,bytes.length-2,2);
        return bytes;
    }

    /**
     * 发送设置背光亮度
     *
     * @param b 0x00-0x40
     * @return 返回发送的指令
     */
    public static byte[] setLedLight(byte b) {

        byte[] bytes = {(byte) 0X5A, (byte) 0XA5, (byte) 0X05, (byte) 0X81,(byte) 0X01, b,(byte)0x00,(byte)0x00};
        byte[] crc = LzCrcUtils.calcCrc(new byte[]{(byte) 0X81,(byte) 0X01, b});
        byte temp;
        temp = crc[0];
        crc[0] = crc[1];
        crc[1] = temp;
        System.arraycopy(crc,0,bytes,bytes.length-2,2);
        return bytes;
    }

    /**
     * 发送读取当前页面编号指令
     *
     * @return 返回发送的指令
     */
    public static byte[] getPageId() {
        byte[] bytes = {(byte) 0X5A, (byte) 0XA5, (byte) 0X05, (byte) 0X81, (byte) 0X03, (byte) 0X02,(byte)0x00,(byte)0x00};
        byte[] crc = LzCrcUtils.calcCrc(new byte[]{(byte) 0X81, (byte) 0X03, (byte) 0X02});
        byte temp;
        temp = crc[0];
        crc[0] = crc[1];
        crc[1] = temp;
        System.arraycopy(crc,0,bytes,bytes.length-2,2);
        return bytes;
    }

    /**
     * 发送设置页面切换指令
     *
     * @param b 需要切换的页面
     * @return 返回发送的指令
     */
    public static byte[] setPageId(byte b) {
        byte[] bytes = {(byte) 0X5A, (byte) 0XA5, (byte) 0X06, (byte) 0X80, (byte) 0X03, (byte) 0X00, b,(byte)0x00,(byte)0x00};
        byte[] crc = LzCrcUtils.calcCrc(new byte[]{ (byte) 0X80, (byte) 0X03, (byte) 0X00, b});
        byte temp;
        temp = crc[0];
        crc[0] = crc[1];
        crc[1] = temp;
        System.arraycopy(crc,0,bytes,bytes.length-2,2);
        return bytes;
    }

    /**
     * 启动蜂鸣器
     *
     * @param b 鸣笛时长
     * @return 返回发送的指令
     */
    public static byte[] setBuzzer(byte b) {
        byte[] bytes = {(byte) 0X5A, (byte) 0XA5, (byte) 0X05, (byte) 0X80, (byte) 0X02, b,(byte)0x00,(byte)0x00};
        byte[] crc = LzCrcUtils.calcCrc(new byte[]{ (byte) 0X80, (byte) 0X02, b});
        byte temp;
        temp = crc[0];
        crc[0] = crc[1];
        crc[1] = temp;
        System.arraycopy(crc,0,bytes,bytes.length-2,2);
        return bytes;
    }

    /**
     * 复位操作
     *
     * @return 返回发送的指令
     */
    public static byte[] setReset() {
        byte[] bytes = {(byte) 0X5A, (byte) 0XA5, (byte) 0X06, (byte) 0X80, (byte) 0XEE, (byte) 0X5A, (byte) 0XA5,(byte)0x00,(byte)0x00};
        byte[] crc = LzCrcUtils.calcCrc(new byte[]{ (byte) 0X80, (byte) 0XEE, (byte) 0X5A, (byte) 0XA5});
        byte temp;
        temp = crc[0];
        crc[0] = crc[1];
        crc[1] = temp;
        System.arraycopy(crc,0,bytes,bytes.length-2,2);
        return bytes;
    }

    /**
     * 触摸屏校准
     *
     * @return 返回发送的指令
     */
    public static byte[] setCalibration() {
        byte[] bytes = {(byte) 0X5A, (byte) 0XA5, (byte) 0X05, (byte) 0X80, (byte) 0XEA, (byte) 0X5A,(byte)0x00,(byte)0x00};
        byte[] crc = LzCrcUtils.calcCrc(new byte[]{ (byte) 0X80, (byte) 0XEA, (byte) 0X5A});
        byte temp;
        temp = crc[0];
        crc[0] = crc[1];
        crc[1] = temp;
        System.arraycopy(crc,0,bytes,bytes.length-2,2);
        return bytes;
    }

    /**
     * 触摸功能
     *
     * @param b 0表示关闭触摸功能,1表示开启触摸功能.
     * @return 返回发送的指令
     */
    public static byte[] setTouchFunction(byte b) {
        byte[] bytes = {(byte) 0X5A, (byte) 0XA5, (byte) 0X05, (byte) 0X80, (byte) 0X0B, b,(byte)0x00,(byte)0x00};
        byte[] crc = LzCrcUtils.calcCrc(new byte[]{ (byte) 0X80, (byte) 0X0B, b});
        byte temp;
        temp = crc[0];
        crc[0] = crc[1];
        crc[1] = temp;
        System.arraycopy(crc,0,bytes,bytes.length-2,2);
        return bytes;
    }

    /**
     * 设置当前时间给屏幕
     * @return 返回发送的指令
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
            year = (ld.getYear()+ "").substring(2,4);
            month = (ld.getMonthValue() + 1) + "";
            day = ld.getDayOfMonth() + "";
            LocalTime lt = LocalTime.now();
            hour = lt.getHour() + "";
            minute = lt.getMinute() + "";
            second = lt.getSecond() + "";

        } else {
            Calendar c = Calendar.getInstance();
            year = (c.get(Calendar.YEAR)+ "").substring(2,4);
            month = (c.get(Calendar.MONTH) + 1) + "";
            day = c.get(Calendar.DATE) + "";
            hour = c.get(Calendar.HOUR_OF_DAY) + "";
            minute = c.get(Calendar.MINUTE) + "";
            second = c.get(Calendar.SECOND) + "";
        }
        if(month.length() < 2){
            month = 0 + month;
        }
        if(day.length() < 2){
            day = 0 + day;
        }
        if(hour.length() < 2){
            hour = 0 + hour;
        }
        if(minute.length() < 2){
            minute = 0 + minute;
        }
        if(second.length() < 2){
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
        byte[] crc = new byte[3 + time.length];
        crc[0] = (byte) 0X80;
        crc[1] = (byte) 0X1F;
        crc[2] = (byte) 0X5A;
        System.arraycopy(time,0,crc,3,time.length);
        byte[] crcValue = LzCrcUtils.calcCrc(crc);
        byte temp;
        temp = crcValue[0];
        crcValue[0] = crcValue[1];
        crcValue[1] = temp;
        System.arraycopy(per,0,bytes,0,per.length);
        System.arraycopy(time,0,bytes,6,time.length);
        System.arraycopy(crcValue,0,bytes,bytes.length - 2,2);
        return bytes;
    }

    public static void main(String[] args) {
        System.out.println("读取背光亮度值 " + ConvertUtil.bytes2String(getLedLight()));
        System.out.println("发送读取当前页面编号指令 " + ConvertUtil.bytes2String(getPageId()));
        System.out.println("发送设置背光亮度 " + ConvertUtil.bytesToHexString(setLedLight((byte) 0x30)));
        System.out.println("发送设置页面切换指令 " + ConvertUtil.bytesToHexString(setPageId((byte) 0x10)));
        System.out.println("启动蜂鸣器 " + ConvertUtil.bytesToHexString(setBuzzer((byte) 0x10)));
        System.out.println("复位操作 " + ConvertUtil.bytesToHexString(setReset()));
        System.out.println("触摸屏校准 " + ConvertUtil.bytesToHexString(setCalibration()));
        System.out.println("触摸功能 " + ConvertUtil.bytesToHexString(setTouchFunction((byte) 0x00)));
        System.out.println("设置当前时间给屏幕 " + ConvertUtil.bytesToHexString(setCurRtcTime()));
    }
}
