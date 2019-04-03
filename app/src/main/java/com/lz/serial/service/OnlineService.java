package com.lz.serial.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.lz.base.socket.Message;
import com.lz.base.socket.tcp.TCPClientBase;
import com.lz.serial.utils.Util;

public class OnlineService extends Service {
    MyTcpClient myTcpClient;
    private String serverIp = "";
    private int serverPort = 8888;

    public class MyTcpClient extends TCPClientBase {

        public MyTcpClient(String serverAddr, int serverPort)
                throws Exception {
            super( serverAddr, serverPort, 10);

        }

        @Override
        public boolean hasNetworkConnection() {
            return Util.hasNetwork(OnlineService.this);
        }


        @Override
        public void trySystemSleep() {

        }

        @Override
        public void onPushMessage(Message message) {
            if(message == null){
                return;
            }
            if(message.getData() == null || message.getData().length == 0){
                return;
            }


            setPkgsInfo();
        }

    }

    public OnlineService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        resetClient();


    }

    @Override
    public int onStartCommand(Intent param, int flags, int startId) {
        if(param == null){
            return START_STICKY;
        }
        String cmd = param.getStringExtra("CMD");
        if(cmd == null){
            cmd = "";
        }

        if(cmd.equals("TOAST")){
            String text = param.getStringExtra("TEXT");
            if(text != null && text.trim().length() != 0){
                Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            }
        }

        setPkgsInfo();

        return START_STICKY;
    }

    protected void setPkgsInfo(){
        if(this.myTcpClient == null){
            return;
        }
        long sent = myTcpClient.getSentPackets();
        long received = myTcpClient.getReceivedPackets();

    }

    protected void resetClient(){

        if(this.myTcpClient != null){
            try{myTcpClient.stop();}catch(Exception e){}
        }
        try{
            myTcpClient = new MyTcpClient(serverIp, serverPort);
            myTcpClient.setHeartbeatInterval(50);
            myTcpClient.start();

        }catch(Exception e){
            Toast.makeText(this.getApplicationContext(), "操作失败："+e.getMessage(), Toast.LENGTH_LONG).show();
        }
        Toast.makeText(this.getApplicationContext(), "ddpush：终端重置", Toast.LENGTH_LONG).show();
    }








    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
