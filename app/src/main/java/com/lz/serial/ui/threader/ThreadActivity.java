package com.lz.serial.ui.threader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lz.base.base.BaseActivity;
import com.lz.base.log.LogUtils;
import com.lz.serial.R;
import com.lz.serial.adapter.RevImageThread;
import com.lz.serial.inter.ISendTcp;
import com.lz.serial.service.TcpConnectService;
import com.lz.serial.widget.RockerView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.lz.serial.net.Contracts.BACK_OFF;
import static com.lz.serial.net.Contracts.FORWARD;
import static com.lz.serial.net.Contracts.LEFT_TURN;
import static com.lz.serial.net.Contracts.RIGHT_TURN;
import static com.lz.serial.net.Contracts.STOP;
import static com.lz.serial.net.Contracts.VIDEO;

public class ThreadActivity extends BaseActivity {

    ExecutorService executor;
    private RockerView rockerView;
    private ImageView ivVideo;
    private RevImageThread revImageThread;
    private WorkThread thread;
    private Thread control;
    private Direction mDirection;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_thread;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Intent startSrv = new Intent(this.getApplicationContext(), TcpConnectService.class);
        this.getApplicationContext().startService(startSrv);
        control = new Thread(new ControlThread());
        control.start();

        rockerView = findViewById(R.id.v_rocker);
        ivVideo = findViewById(R.id.iv_video);
        rockerView.setCallBackMode(RockerView.CallBackMode.CALL_BACK_MODE_STATE_CHANGE);
        rockerView.setOnShakeListener(RockerView.DirectionMode.DIRECTION_8, new RockerView.OnShakeListener() {
            @Override
            public void onStart() {
                LogUtils.i("onStart");
            }

            @Override
            public void direction(RockerView.Direction direction) {
                mDirection = getDirection(direction);
                Message msg = Message.obtain();
                msg.arg1 = 1;
            }

            @Override
            public void onFinish() {
                LogUtils.i("onFinish");
            }
        });

        myHander = new MyHander();
        executor = Executors.newSingleThreadExecutor();
        revImageThread = new RevImageThread(myHander);
        thread = new WorkThread();


    }

    private MyHander myHander;

    @Override
    public void initData() {
        super.initData();
        myHander = new MyHander();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TcpConnectService service = new TcpConnectService();
        setiSendTcp(service.getiSendTcp());
    }

    private Direction getDirection(RockerView.Direction direction) {
        Direction message = Direction.STOP;
        switch (direction) {
            case DIRECTION_LEFT:
                message = Direction.LEFT_TURN;
                break;
            case DIRECTION_RIGHT:
                message = Direction.RIGHT_TURN;
                break;
            case DIRECTION_UP:
                message = Direction.FORWARD;
                break;
            case DIRECTION_DOWN:
                message = Direction.BACK_OFF;
                break;
            case DIRECTION_UP_LEFT:
                message = Direction.FORWARD_LEFT_TURN;
                break;
            case DIRECTION_UP_RIGHT:
                message = Direction.FORWARD_RIGHT_TURN;
                break;
            case DIRECTION_DOWN_LEFT:
                message = Direction.BACK_OFF_LEFT_TURN;
                break;
            case DIRECTION_DOWN_RIGHT:
                message = Direction.BACK_OFF_RIGHT_TURN;
                break;
        }
        return message;
    }

    private Bitmap bitmap;

    private ISendTcp iSendTcp;

    public void setiSendTcp(ISendTcp iSendTcp) {
        this.iSendTcp = iSendTcp;
    }


    private boolean isContinue = false;

    private enum Direction {
        STOP,
        FORWARD, BACK_OFF, LEFT_TURN, RIGHT_TURN,
        FORWARD_LEFT_TURN, FORWARD_RIGHT_TURN, BACK_OFF_LEFT_TURN, BACK_OFF_RIGHT_TURN;
    }

    public void circularTrans(Direction direction) {
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            switch (direction) {
                case FORWARD:
                    send(FORWARD);
                    break;
                case BACK_OFF:
                    send(BACK_OFF);
                    break;
                case LEFT_TURN:
                    send(LEFT_TURN);
                    break;
                case RIGHT_TURN:
                    send(RIGHT_TURN);
                    break;
                default:
                    send(STOP);
                    break;
            }
            LogUtils.i("小车运动会:" + mDirection);
        } while (isContinue);

        LogUtils.i("小车运动会,结束:" + mDirection + " " + isContinue);
    }


    private void send(String msg) {
        if (iSendTcp != null) {
            iSendTcp.send(msg);
            LogUtils.i("发送指令:  " + msg);
        }
    }


    public void leftTurn(View view) {
        send(LEFT_TURN);
    }

    public void rightTurn(View view) {
        send(RIGHT_TURN);
    }

    public void forward(View view) {
        send(FORWARD);
    }

    public void backOff(View view) {
        send(BACK_OFF);

    }

    public class MyHander extends Handler {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == VIDEO) {
                bitmap = (Bitmap) msg.obj;
            }

            Log.i("car handleMessage", msg.what + "");
            ivVideo.setImageBitmap(bitmap);
            super.handleMessage(msg);
        }
    }

    boolean isRunning = false;

    public void start(View view) {
        if(!thread.isAlive()){
            isRunning = true;
            thread.start();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop();
    }

    public void stop() {
//        vv.stopPlayback();
        isRunning = false;
        executor.shutdown();
    }

    class ControlThread implements Runnable{
        Handler handler = null;

        @SuppressLint("HandlerLeak")
        @Override
        public void run() {

            Looper.prepare();
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if(msg.arg1 == 1){
                        circularTrans(mDirection);
                        isContinue = mDirection != Direction.STOP;
                    }
                }
            };
            Looper.loop();
        }

    }

    class WorkThread extends Thread {

        public WorkThread() {

        }

        @Override
        public void run() {
            super.run();

            while (isRunning) {
                if (revImageThread != null) {
                    executor.execute(revImageThread);
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
