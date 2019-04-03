package com.lz.serial.net;

import com.lz.base.log.LogUtils;
import com.lz.serial.ItemActivity;
import com.lz.serial.fragment.bean.JcBean;

import net.qiujuer.library.clink.box.StringReceivePacket;
import net.qiujuer.library.clink.core.Connector;
import net.qiujuer.library.clink.core.IoContext;
import net.qiujuer.library.clink.core.Packet;
import net.qiujuer.library.clink.core.ReceivePacket;
import net.qiujuer.library.clink.core.SendPacket;
import net.qiujuer.library.clink.impl.IoSelectorProvider;
import net.qiujuer.library.clink.impl.SchedulerImpl;
import net.qiujuer.library.clink.utils.CloseUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.channels.SocketChannel;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/18 下午4:42
 * 描述     :
 */
public class TcpClient extends Connector {



    private volatile String mServerName;
    private MessageArrivedListener mMessageArrivedListener;
    private ConnectorStatusListener mConnectorStatusListener;
    private boolean connect;

    public TcpClient(String address, int port) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        Socket socket = socketChannel.socket();
        // 无延迟发送
        socket.setTcpNoDelay(true);
        // 延迟最重要、带宽其次、之后是链接
        socket.setPerformancePreferences(1, 3, 2);
        // 接收数据缓冲区
        socket.setReceiveBufferSize(1024);
        // 发送数据缓冲区
        socket.setSendBufferSize(256);
        // 连接
        SocketAddress inetSocketAddress = new InetSocketAddress(Inet4Address.getByName(address), port);
        LogUtils.i(((InetSocketAddress) inetSocketAddress).getAddress().getHostAddress());
        socketChannel.connect(inetSocketAddress );
        // 开启
        setup(socketChannel);
        connect = true;
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
    protected void onReceivedPacket(ReceivePacket packet) {
        super.onReceivedPacket(packet);
        if (packet.type() == Packet.TYPE_MEMORY_STRING) {
            String entity = ((StringReceivePacket) packet).entity();
            ConnectorInfo info = new ConnectorInfo(entity);
            mMessageArrivedListener.onNewMessageArrived(info);
        }
    }

    @Override
    public void onChannelClosed(SocketChannel channel) {
        super.onChannelClosed(channel);
        connect = false;
        if (mConnectorStatusListener != null) {
            mConnectorStatusListener.onConnectorClosed(this);
        }
    }
    public boolean isConnect() {
        return connect;
    }

    public void setConnect(boolean connect) {
        this.connect = connect;
    }

    /**
     * 获取服务器的名称
     *
     * @return 服务器标示
     */
    public synchronized String getServerName() {
        if (mServerName == null) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return mServerName;
    }

    /**
     * 设置消息监听
     *
     * @param messageArrivedListener 回调
     */
    public void setMessageArrivedListener(MessageArrivedListener messageArrivedListener) {
        mMessageArrivedListener = messageArrivedListener;
    }

    /**
     * 设置状态监听
     *
     * @param connectorStatusListener 状态变化监听
     */
    public void setConnectorStatusListener(ConnectorStatusListener connectorStatusListener) {
        mConnectorStatusListener = connectorStatusListener;
    }

    /**
     * 当消息信息到达时回调
     */
    public interface MessageArrivedListener {
        void onNewMessageArrived(ConnectorInfo info);
    }


    /**
     * 链接状态监听
     */
    public interface ConnectorStatusListener {
        void onConnectorClosed(Connector connector);
    }
}
