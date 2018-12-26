package com.lz.serial.inter;

import android.hardware.usb.UsbDevice;

/**
 * 作者      : 刘朝
 * 创建日期  : 2018/12/24 下午9:39
 * 描述     :
 */
public interface ICallBack {
    void onCallBack(UsbDevice device);
}
