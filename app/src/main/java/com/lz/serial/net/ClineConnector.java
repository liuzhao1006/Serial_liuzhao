package com.lz.serial.net;

import com.lz.base.clink.box.StringReceivePacket;
import com.lz.base.clink.core.Connector;
import com.lz.base.clink.core.Packet;
import com.lz.base.clink.core.ReceivePacket;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;


/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/18 下午4:57
 * 描述     :
 */
public class ClineConnector extends Connector {
    private volatile String mServerName;
//    private MessageArrivedListener messageArrivedListener;
//    private ConnectorStatusListener connectorStatusListener;

    public ClineConnector(String address, int port)throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        Socket socket = socketChannel.socket();
        socket.setTcpNoDelay(true);
        socket.setPerformancePreferences(1,3,2);
        socket.setReceiveBufferSize(1024);
        socket.setSendBufferSize(256);
        socketChannel.connect(new InetSocketAddress(Inet4Address.getByName(address),port));
        setup(socketChannel);
    }

    @Override
    protected void onReceivedPacket(ReceivePacket packet) {
        super.onReceivedPacket(packet);
        if(packet.type() == Packet.TYPE_MEMORY_STRING){
            String entity = ((StringReceivePacket) packet).entity();

        }
    }

    @Override
    protected File createNewReceiveFile(long l, byte[] bytes) {
        return null;
    }

    @Override
    protected OutputStream createNewReceiveDirectOutputStream(long l, byte[] bytes) {
        return null;
    }

    @Override
    public void onChannelClosed(SocketChannel channel) {
        super.onChannelClosed(channel);
//        if(connectorStatusListener != null){
//            connectorStatusListener
//        }
    }
}
