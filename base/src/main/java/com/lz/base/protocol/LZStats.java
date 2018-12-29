package com.lz.base.protocol;

/**
 * 作者      : 刘朝
 * 创建日期  : 2018/12/29 下午5:58
 * 描述     :
 */
public class LZStats {

    public int receivedPacketCount;
    public int crcErrorCount;
    public int lostPacketCount;
    public boolean ignoreRadioPackets;
//    public SystemStat[]

    public static class systemStat{
        public int lostPacketCount;
        public int receivedPacketCount;

    }

    public static class ComponentStat{
        public int lastPacketSeq;
        public int lostPacketCount;
        public int receivedPacketCount;


        public ComponentStat(){}

        public int newPacket(){
            return 0;
        }
    }
}
