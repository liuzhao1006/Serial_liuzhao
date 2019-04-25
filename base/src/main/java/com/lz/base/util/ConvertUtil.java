package com.lz.base.util;

import com.lz.base.log.LogUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ConvertUtil {
    /**
     * byte数组转换为16进制字符串，中间带空格
     *
     * @param byteArrays
     * @return
     */
    public static String bytes2String(byte[] byteArrays) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteArrays.length; i++) {
            String temp = Integer.toHexString(byteArrays[i] & 0xff);
            if (temp.length() == 1) {
                temp = "0" + temp;
            }
            sb.append(temp + " ");
        }
        return sb.toString();
    }


    /**
     * 将java的short转化为c或者c#的ushort
     *
     * @param s
     * @return
     */
    public static int toUnsignedShort(short s) {
        return (s & 0x0FFFF);
    }

    /**
     * Convert
     * byte[] to hex string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     *
     * @param bytes byte[] data
     * @return hex string
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append("0");
            }
            stringBuilder.append(hv+" ");
        }
        return stringBuilder.toString();
    }

    /**
     * Convert
     * byte[] to hex string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     *
     * @param bytes byte[] data
     * @return hex string
     */
    public static String bytesToHexStringWithNoWhiteSpace(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    //使用1字节就可以表示b
    public static String numToHex8(int b) {
        return String.format("%02x", b);//2表示需要两个16进行数
    }
    //需要使用2字节表示b
    public static String numToHex16(int b) {
        return String.format("%04x", b);
    }
    //需要使用4字节表示b
    public static String numToHex32(int b) {
        return String.format("%08x", b);
    }

    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 将一个单字节的byte转换成32位的int
     *
     * @param b
     *            byte
     * @return convert result
     */
    public static int unsignedByteToInt(byte b) {
        return (int) b & 0xFF;
    }

    /**
     * 将一个单字节的Byte转换成十六进制的数
     *
     * @param b
     *            byte
     * @return convert result
     */
    public static String byteToHex(byte b) {
        int i = b & 0xFF;
        return Integer.toHexString(i);
    }

    //byte 与 int 的相互转换
    public static byte intToByte(int x) {
        return (byte) x;
    }

    public static int byteToInt(byte b) {
        //Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
        return b & 0xFF;
    }

    //byte 数组与 int 的相互转换
    public static int byteArrayToInt(byte[] b) {
        return   b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    public static byte[] intToByteArray(int a) {
        return new byte[] {
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }
    public static byte[] intToSubByteArray(int a) {
        return new byte[] {
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }
    private static ByteBuffer longBuffer = ByteBuffer.allocate(8);

    //byte 数组与 long 的相互转换
    public static byte[] longToBytes(long x) {
        longBuffer.putLong(0, x);
        return longBuffer.array();
    }

    public static long bytesToLong(byte[] bytes) {
        longBuffer.put(bytes, 0, bytes.length);
        longBuffer.flip();//need flip
        return longBuffer.getLong();
    }

    /**
     * 合并bytes
     * @param data1
     * @param data2
     * @return data1 与 data2拼接的结果
     */
    public static byte[] combineBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;

    }

    /**
     * 转换为小端序
     * @param data1
     * @return
     */
    public static byte [] getLittleEndian(byte[] data1){
        byte[] result = new byte[data1.length];
        ByteBuffer.wrap(data1).order(ByteOrder.LITTLE_ENDIAN).get(result,0,result.length);
        return result;
    }

    /**
     * 转换为大端序
     * @param data1
     * @return
     */
    public static byte [] getBigEndian(byte[] data1){
        byte[] result = new byte[data1.length];
        ByteBuffer.wrap(data1).order(ByteOrder.BIG_ENDIAN).get(result,0,result.length);
        return result;
    }

    private static ByteBuffer intBuffer = ByteBuffer.allocate(4);


    /**
     * 字符串转换成十六进制字符串
     *
     * @param  str 待转换的ASCII字符串
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
     */
    public static String str2HexStr(String str) {

        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            sb.append(' ');
        }
        return sb.toString().trim();

    }

    public static String intToHex(int n) {
        StringBuilder sb = new StringBuilder(8);
        String a;
        char []b = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        while(n != 0){
            sb = sb.append(b[n%16]);
            n = n/16;
        }
        a = sb.reverse().toString();
        return a;
    }

}
