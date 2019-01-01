package com.lz.base.protocol;

import com.lz.base.message.LzPacket;

/**
 * 作者      : 刘朝
 * 创建日期  : 2018/12/29 下午5:53
 * 描述     :
 */
public class Parser {

    /**
     * 数据结构
     */
    enum LZ_states {
        LZ_PARER_STATE_HEAD,
        LZ_PARER_STATE_COUNT,
        LZ_PARER_STATE_ORDER,
        LZ_PARER_STATE_DATA,
        LZ_PARER_STATE_CRC,
    }

    LZ_states state = LZ_states.LZ_PARER_STATE_HEAD;

    public LZStats lzStats;
    private LzPacket m;

    public Parser(boolean ignoreRadioPacketStats) {
        lzStats = new LZStats(ignoreRadioPacketStats);

    }

    public LzPacket mavlink_parse_char(int c) {
        switch (state) {
            case LZ_PARER_STATE_CRC:
                break;
            case LZ_PARER_STATE_DATA:
                break;
            case LZ_PARER_STATE_HEAD:
                break;
            case LZ_PARER_STATE_COUNT:
                break;
            case LZ_PARER_STATE_ORDER:
                break;
        }

        return null;
    }

    public LzPacket mavlink_parse_char(int c,int index) {
        switch (state){
            case LZ_PARER_STATE_ORDER:
                break;
            case LZ_PARER_STATE_COUNT:
                break;
            case LZ_PARER_STATE_HEAD:
                break;
            case LZ_PARER_STATE_DATA:
                break;
            case LZ_PARER_STATE_CRC:
                break;

        }
        return null;
    }

}
