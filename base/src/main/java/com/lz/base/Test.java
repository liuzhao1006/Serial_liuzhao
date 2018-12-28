package com.lz.base;

import com.lz.base.protocol.CrcUtils;
import com.lz.base.util.ConvertUtil;

/**
 * 作者      : 刘朝
 * 创建日期  : 2018/12/22 下午3:31
 * 描述     :
 */
public class Test {

    public static void main(String[] args) {
        byte[] bsrc = {0x02, 0x05, 0x00, 0x03, (byte) 0xff, 0x00};
        int i = CrcUtils.calcCrc16(bsrc);
        System.out.println(ConvertUtil.numToHex16(i));
    }
}
