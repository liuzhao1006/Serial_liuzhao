package com.lz.base.protocol;

/**
 * 作者      : 刘朝
 * 创建日期  : 2018/12/29 下午5:53
 * 描述     :
 */
public class Parser {

    /**
     * 数据结构
     */
    enum LZ_states{
        LZ_PARER_STATE_HEAD,
        LZ_PARER_STATE_COUNT,
        LZ_PARER_STATE_ORDER,
        LZ_PARER_STATE_DATA,
        LZ_PARER_STATE_CRC,
    }

    LZ_states state = LZ_states.LZ_PARER_STATE_HEAD;

}
