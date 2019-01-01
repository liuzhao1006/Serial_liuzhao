package com.lz.base.protocol;

import com.lz.base.message.LzPacket;

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
    public SystemStat[] systemStats;

    public LZStats(){
        this(false);
    }
    public LZStats(boolean ignoreRadioPackets) {
        this.ignoreRadioPackets = ignoreRadioPackets;
        resetStats();
    }

    /**
     * Check the new received packet to see if has lost someone between this and
     * the last packet
     *
     * @param packet Packet that should be checked
     */
    public void newPacket(LzPacket packet) {
        if (ignoreRadioPackets && packet.orderId == 0) {
            return;
        }

        if (systemStats[packet.lenId] == null) {
            // only allocate stats for systems that exsist on the network
            systemStats[packet.lenId] = new SystemStat();
        }
        lostPacketCount += systemStats[packet.lenId].newPacket(packet);
        receivedPacketCount++;
    }

    /**
     * Called when a CRC error happens on the parser
     */
    public void crcError() {
        crcErrorCount++;
    }

    public void resetStats() {
        crcErrorCount = 0;
        lostPacketCount = 0;
        receivedPacketCount = 0;
        systemStats = new SystemStat[256];
    }


    public static class SystemStat{
        public int lostPacketCount;
        public int receivedPacketCount;
        public ComponentStat[] componentStats; // stats for each component that is known
        public SystemStat() {
            resetStats();
        }

        public int newPacket(LzPacket packet) {
            int newLostPackets = 0;
            if (componentStats[packet.orderId] == null) {
                // only allocate stats for systems that exsist on the network
                componentStats[packet.orderId] = new ComponentStat();
            }
            newLostPackets = componentStats[packet.orderId].newPacket(packet);
            lostPacketCount += newLostPackets;
            receivedPacketCount++;
            return newLostPackets;
        }

        public void resetStats() {
            lostPacketCount = 0;
            receivedPacketCount = 0;
            componentStats = new ComponentStat[256];
        }

    }

    public static class ComponentStat{
        public int lastPacketSeq;
        public int lostPacketCount;
        public int receivedPacketCount;

        public ComponentStat(){}

        public int newPacket(){
            return 0;
        }

        public int newPacket(LzPacket packet) {
            int newLostPackets = 0;
            if (hasLostPackets(packet)) {
                newLostPackets = updateLostPacketCount(packet);
            }
            lastPacketSeq = packet.lenId;
            advanceLastPacketSequence(packet.lenId);
            receivedPacketCount++;
            return newLostPackets;
        }

        public void resetStats() {
            lastPacketSeq = -1;
            lostPacketCount = 0;
            receivedPacketCount = 0;
        }

        private int updateLostPacketCount(LzPacket packet) {
            int lostPackets;
            if (packet.lenId - lastPacketSeq < 0) {
                lostPackets = (packet.lenId - lastPacketSeq) + 255;
            } else {
                lostPackets = (packet.lenId - lastPacketSeq);
            }
            lostPacketCount += lostPackets;
            return lostPackets;
        }

        private void advanceLastPacketSequence(int lastSeq) {
            // wrap from 255 to 0 if necessary
            lastPacketSeq = (lastSeq + 1) & 0xFF;
        }

        private boolean hasLostPackets(LzPacket packet) {
            return lastPacketSeq >=  0 && packet.lenId != lastPacketSeq;
        }
    }
}
