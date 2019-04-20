package com.lz.serial.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.lz.base.clink.core.Connector;
import com.lz.base.clink.core.IoContext;
import com.lz.base.clink.core.ScheduleJob;
import com.lz.base.clink.core.schedule.IdleTimeoutScheduleJob;
import com.lz.base.clink.impl.IoSelectorProvider;
import com.lz.base.clink.impl.SchedulerImpl;
import com.lz.base.clink.utils.CloseUtils;
import com.lz.base.log.LogUtils;
import com.lz.serial.inter.ISendTcp;
import com.lz.serial.net.ConnectorInfo;
import com.lz.serial.net.Contracts;
import com.lz.serial.net.TcpClient;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static com.lz.serial.net.Contracts.LEFT_TURN;

public class TcpConnectService extends Service implements TcpClient.ConnectorStatusListener,ISendTcp {


    public TcpConnectService() {
    }
    private TcpClient client;
    private int startId;

    public ISendTcp getiSendTcp() {
        return this;
    }



    @Override
    public void onConnectorClosed(Connector connector) {
        LogUtils.i("Connetor " + connector.getKey());

    }

    @Override
    public void send(String msg) {
        if(client != null && msg != null){
            client.send(msg);
        }
    }

    public enum Control {
        SEND, PAUSE, STOP
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.startId = startId;
        connectWifi();
        LogUtils.i("onStartCommand开始连接");
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void connectWifi() {
        if (client == null) {
            Run.onBackground(new InitAction());
        } else {
            Run.onBackground(new DestroyAction());
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (client == null) {
                        Run.onBackground(new InitAction());
                    }
                }
            }, 1000);
        }
    }

    private class InitAction implements Action {
        @Override
        public void call() {
            if (client != null && client.isConnect()) {
                LogUtils.i("已经连接了");
                return;
            }
            LogUtils.i("开始连接tcpConnectsERVICE");
            try {
                IoContext.setup()
                        .ioProvider(new IoSelectorProvider())
                        .scheduler(new SchedulerImpl(1))
                        .start();
                client = new TcpClient(Contracts.IP, Contracts.PORT);
                client.setMessageArrivedListener(mMessageArrivedListener);
                client.setConnectorStatusListener(TcpConnectService.this);
                client.send(LEFT_TURN);
                ScheduleJob job = new IdleTimeoutScheduleJob(Contracts.IDLE_TIMEOUT_SCHEDULE_JOB, TimeUnit.SECONDS, client);
                client.schedule(job);

            } catch (IOException e) {
                e.printStackTrace();
                new DestroyAction().call();
            }
        }
    }

    /**
     * 数据分发
     */
    private TcpClient.MessageArrivedListener mMessageArrivedListener = this::onNewMessageArrived;

    private class DestroyAction implements Action {
        @Override
        public void call() {
            if (client != null) {
                client.setConnect(false);
                CloseUtils.close(client);
                client = null;
            }
            try {
                IoContext.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onNewMessageArrived(ConnectorInfo info) {
        LogUtils.i("接收消息 " + info.getValue());
    }

}
