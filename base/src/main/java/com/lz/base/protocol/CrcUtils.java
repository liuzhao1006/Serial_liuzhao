package com.lz.base.protocol;

import com.lz.base.util.ConvertUtil;

/**
 * 作者      : 刘朝
 * 创建日期  : 2018/12/28 下午2:07
 * 描述     :
 */
public class CrcUtils {

    private static final byte[] crc16_tab_h = { (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x01, (byte) 0xC0,
            (byte) 0x80, (byte) 0x41, (byte) 0x00, (byte) 0xC1,
            (byte) 0x81, (byte) 0x40 };

    private static final byte[] crc16_tab_l = { (byte) 0x00, (byte) 0xC0,
            (byte) 0xC1, (byte) 0x01, (byte) 0xC3, (byte) 0x03,
            (byte) 0x02, (byte) 0xC2, (byte) 0xC6, (byte) 0x06,
            (byte) 0x07, (byte) 0xC7, (byte) 0x05, (byte) 0xC5,
            (byte) 0xC4, (byte) 0x04, (byte) 0xCC, (byte) 0x0C,
            (byte) 0x0D, (byte) 0xCD, (byte) 0x0F, (byte) 0xCF,
            (byte) 0xCE, (byte) 0x0E, (byte) 0x0A, (byte) 0xCA,
            (byte) 0xCB, (byte) 0x0B, (byte) 0xC9, (byte) 0x09,
            (byte) 0x08, (byte) 0xC8, (byte) 0xD8, (byte) 0x18,
            (byte) 0x19, (byte) 0xD9, (byte) 0x1B, (byte) 0xDB,
            (byte) 0xDA, (byte) 0x1A, (byte) 0x1E, (byte) 0xDE,
            (byte) 0xDF, (byte) 0x1F, (byte) 0xDD, (byte) 0x1D,
            (byte) 0x1C, (byte) 0xDC, (byte) 0x14, (byte) 0xD4,
            (byte) 0xD5, (byte) 0x15, (byte) 0xD7, (byte) 0x17,
            (byte) 0x16, (byte) 0xD6, (byte) 0xD2, (byte) 0x12,
            (byte) 0x13, (byte) 0xD3, (byte) 0x11, (byte) 0xD1,
            (byte) 0xD0, (byte) 0x10, (byte) 0xF0, (byte) 0x30,
            (byte) 0x31, (byte) 0xF1, (byte) 0x33, (byte) 0xF3,
            (byte) 0xF2, (byte) 0x32, (byte) 0x36, (byte) 0xF6,
            (byte) 0xF7, (byte) 0x37, (byte) 0xF5, (byte) 0x35,
            (byte) 0x34, (byte) 0xF4, (byte) 0x3C, (byte) 0xFC,
            (byte) 0xFD, (byte) 0x3D, (byte) 0xFF, (byte) 0x3F,
            (byte) 0x3E, (byte) 0xFE, (byte) 0xFA, (byte) 0x3A,
            (byte) 0x3B, (byte) 0xFB, (byte) 0x39, (byte) 0xF9,
            (byte) 0xF8, (byte) 0x38, (byte) 0x28, (byte) 0xE8,
            (byte) 0xE9, (byte) 0x29, (byte) 0xEB, (byte) 0x2B,
            (byte) 0x2A, (byte) 0xEA, (byte) 0xEE, (byte) 0x2E,
            (byte) 0x2F, (byte) 0xEF, (byte) 0x2D, (byte) 0xED,
            (byte) 0xEC, (byte) 0x2C, (byte) 0xE4, (byte) 0x24,
            (byte) 0x25, (byte) 0xE5, (byte) 0x27, (byte) 0xE7,
            (byte) 0xE6, (byte) 0x26, (byte) 0x22, (byte) 0xE2,
            (byte) 0xE3, (byte) 0x23, (byte) 0xE1, (byte) 0x21,
            (byte) 0x20, (byte) 0xE0, (byte) 0xA0, (byte) 0x60,
            (byte) 0x61, (byte) 0xA1, (byte) 0x63, (byte) 0xA3,
            (byte) 0xA2, (byte) 0x62, (byte) 0x66, (byte) 0xA6,
            (byte) 0xA7, (byte) 0x67, (byte) 0xA5, (byte) 0x65,
            (byte) 0x64, (byte) 0xA4, (byte) 0x6C, (byte) 0xAC,
            (byte) 0xAD, (byte) 0x6D, (byte) 0xAF, (byte) 0x6F,
            (byte) 0x6E, (byte) 0xAE, (byte) 0xAA, (byte) 0x6A,
            (byte) 0x6B, (byte) 0xAB, (byte) 0x69, (byte) 0xA9,
            (byte) 0xA8, (byte) 0x68, (byte) 0x78, (byte) 0xB8,
            (byte) 0xB9, (byte) 0x79, (byte) 0xBB, (byte) 0x7B,
            (byte) 0x7A, (byte) 0xBA, (byte) 0xBE, (byte) 0x7E,
            (byte) 0x7F, (byte) 0xBF, (byte) 0x7D, (byte) 0xBD,
            (byte) 0xBC, (byte) 0x7C, (byte) 0xB4, (byte) 0x74,
            (byte) 0x75, (byte) 0xB5, (byte) 0x77, (byte) 0xB7,
            (byte) 0xB6, (byte) 0x76, (byte) 0x72, (byte) 0xB2,
            (byte) 0xB3, (byte) 0x73, (byte) 0xB1, (byte) 0x71,
            (byte) 0x70, (byte) 0xB0, (byte) 0x50, (byte) 0x90,
            (byte) 0x91, (byte) 0x51, (byte) 0x93, (byte) 0x53,
            (byte) 0x52, (byte) 0x92, (byte) 0x96, (byte) 0x56,
            (byte) 0x57, (byte) 0x97, (byte) 0x55, (byte) 0x95,
            (byte) 0x94, (byte) 0x54, (byte) 0x9C, (byte) 0x5C,
            (byte) 0x5D, (byte) 0x9D, (byte) 0x5F, (byte) 0x9F,
            (byte) 0x9E, (byte) 0x5E, (byte) 0x5A, (byte) 0x9A,
            (byte) 0x9B, (byte) 0x5B, (byte) 0x99, (byte) 0x59,
            (byte) 0x58, (byte) 0x98, (byte) 0x88, (byte) 0x48,
            (byte) 0x49, (byte) 0x89, (byte) 0x4B, (byte) 0x8B,
            (byte) 0x8A, (byte) 0x4A, (byte) 0x4E, (byte) 0x8E,
            (byte) 0x8F, (byte) 0x4F, (byte) 0x8D, (byte) 0x4D,
            (byte) 0x4C, (byte) 0x8C, (byte) 0x44, (byte) 0x84,
            (byte) 0x85, (byte) 0x45, (byte) 0x87, (byte) 0x47,
            (byte) 0x46, (byte) 0x86, (byte) 0x82, (byte) 0x42,
            (byte) 0x43, (byte) 0x83, (byte) 0x41, (byte) 0x81,
            (byte) 0x80, (byte) 0x40 };


    /**
     * 计算CRC16校验
     *
     * @param data
     *            需要计算的数组
     * @return CRC16校验值
     */
    public static int calcCrc16(byte[] data) {
        return calcCrc16(data, 0, data.length);
    }

    /**
     * 计算CRC16校验
     *
     * @param data
     *            需要计算的数组
     * @param offset
     *            起始位置
     * @param len
     *            长度
     * @return CRC16校验值
     */
    public static int calcCrc16(byte[] data, int offset, int len) {
        return calcCrc16(data, offset, len, 0xffff);
    }

    /**
     * 计算CRC16校验
     *
     * @param data
     *            需要计算的数组
     * @param offset
     *            起始位置
     * @param len
     *            长度
     * @param preval
     *            之前的校验值
     * @return CRC16校验值
     */
    public static int calcCrc16(byte[] data, int offset, int len, int preval) {
        int ucCRCHi = (preval & 0xff00) >> 8;
        int ucCRCLo = preval & 0x00ff;
        int iIndex;
        for (int i = 0; i < len; ++i) {
            iIndex = (ucCRCLo ^ data[offset + i]) & 0x00ff;
            ucCRCLo = ucCRCHi ^ crc16_tab_h[iIndex];
            ucCRCHi = crc16_tab_l[iIndex];
        }
        return ((ucCRCHi & 0x00ff) << 8) | (ucCRCLo & 0x00ff) & 0xffff;
    }
    public static char Crc16Calc(byte[] data_arr, int data_len)
    {
        char crc16 = 0;
        int i;
        for(i =0; i < (data_len); i++)
        {
            crc16 = (char)(( crc16 >> 8) | (crc16 << 8));
            crc16 ^= data_arr[i]& 0xFF;
            crc16 ^= (char)(( crc16 & 0xFF) >> 4);
            crc16 ^= (char)(( crc16 << 8) << 4);
            crc16 ^= (char)((( crc16 & 0xFF) << 4) << 1);
        }
        return crc16;
    }
    private static String getCrc(byte[] data) {
        int high;
        int flag;
        // 16位寄存器，所有数位均为1
        int wcrc = 0xffff;
        for (int i = 0; i < data.length; i++) {
            // 16 位寄存器的高位字节
            high = wcrc >> 8;
            // 取被校验串的一个字节与 16 位寄存器的高位字节进行“异或”运算
            wcrc = high ^ data[i];

            for (int j = 0; j < 8; j++) {
                flag = wcrc & 0x0001;
                // 把这个 16 寄存器向右移一位
                wcrc = wcrc >> 1;
                // 若向右(标记位)移出的数位是 1,则生成多项式 1010 0000 0000 0001 和这个寄存器进行“异或”运算
                if (flag == 1)
                    wcrc ^= 0xa001;
            }
        }

        return Integer.toHexString(wcrc);
    }
    // 测试
    public static String getCRC(byte[] bytes) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;
        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        return Integer.toHexString(CRC);
    }
    public static void main(String[] args) {
        // 0x02 05 00 03 FF 00 , crc16=7C 09
        byte[] bsrc = {0x02, 0x05, 0x00, 0x03, (byte) 0xff, 0x00};
//        int crc = calcCrc16(new byte[] { 0x02, 0x05, 0x00, 0x03, (byte) 0xff, 0x00 });
//        byte[] bytes = ConvertUtil.intToByteArray(crc);
//        System.out.println(bytes.length);
//        byte[] bs = new byte[2];
//        System.arraycopy(bytes,2,bs,0,bytes.length - 2);
//        System.out.println(ConvertUtil.bytes2String(bs));
//        System.out.println(ConvertUtil.bytes2String(bytes));
//        System.out.println("-------------");
//        System.out.println(String.format("0x%04x", crc));




        SyMessage message = new SyMessage(bsrc);
        message.setOrder(OrderMode.WRITE_REGISTER);
        message.setAdress(new byte[]{(byte)0x11,(byte)0x12});
        byte[] dataValue = message.getDataValue(message.getBytes());
        System.out.println("组装 " + ConvertUtil.bytes2String(dataValue));
        if(dataValue == null){
            throw new CheckDataException("数据有误");
        }
        byte[] crcValue = message.getCrcValue(dataValue);

        System.out.println("crc校验值: " + ConvertUtil.bytesToHexString(crcValue));
        System.out.println("下面是组包------");
        SyMessage syMessage = new SyMessage(bsrc);
        syMessage.setOrder(OrderMode.WRITE_REGISTER);
        syMessage.setAdress(new byte[]{(byte)0x11,(byte)0x12});
        byte[] pack = syMessage.pack();
        System.out.println("下面是解包------");
        System.out.println(ConvertUtil.bytes2String(pack));
        SyMessage msg = new SyMessage();
        SyMessage m = msg.unPack(pack);
        System.out.println(m.toString());


    }
}
