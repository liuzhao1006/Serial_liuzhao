package com.lz.serial.net;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/23 上午10:12
 * 描述     : 数据分发
 */
public interface Distribute {

    void onDistrubute(ConnectorInfo msg);
}
