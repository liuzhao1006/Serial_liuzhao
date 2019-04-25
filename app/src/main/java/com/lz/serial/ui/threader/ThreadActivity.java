package com.lz.serial.ui.threader;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.lz.base.base.BaseActivity;
import com.lz.base.log.LogUtils;
import com.lz.base.socket.Constant;
import com.lz.serial.R;
import com.lz.serial.adapter.RevImageThread;
import com.lz.serial.service.TcpConnectService;
import com.lz.serial.widget.RockerView;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.lz.serial.net.Contracts.BACK_OFF;
import static com.lz.serial.net.Contracts.BACK_OFF_LEFT_TURN;
import static com.lz.serial.net.Contracts.BACK_OFF_RIGHT_TURN;
import static com.lz.serial.net.Contracts.FORWARD;
import static com.lz.serial.net.Contracts.FORWARD_LEFT_TURN;
import static com.lz.serial.net.Contracts.FORWARD_RIGHT_TURN;
import static com.lz.serial.net.Contracts.IP;
import static com.lz.serial.net.Contracts.LEFT_TURN;
import static com.lz.serial.net.Contracts.RIGHT_TURN;
import static com.lz.serial.net.Contracts.STOP;
import static com.lz.serial.net.Contracts.VIDEO;

public class ThreadActivity extends BaseActivity {

    ExecutorService executor;
    private ImageView ivVideo;
    private RevImageThread revImageThread;
    private WorkThread thread;
    private Direction mDirection;
    private Direction mDirectionTemp;
    private EditText etIp;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_thread;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Intent startSrv = new Intent(this.getApplicationContext(), TcpConnectService.class);
        this.getApplicationContext().startService(startSrv);

        RockerView rockerView = findViewById(R.id.v_rocker);
        ivVideo = findViewById(R.id.iv_video);
        etIp = findViewById(R.id.et_ip);
        IP = etIp.getText().toString().trim();
        rockerView.setCallBackMode(RockerView.CallBackMode.CALL_BACK_MODE_STATE_CHANGE);
        rockerView.setOnShakeListener(RockerView.DirectionMode.DIRECTION_8, new RockerView.OnShakeListener() {
            @Override
            public void onStart() {
                LogUtils.i("onStart");

            }

            @Override
            public void direction(RockerView.Direction direction) {
                mDirection = getDirection(direction);
                if(mDirection != mDirectionTemp ){
                    circularTrans(mDirection);
                    mDirectionTemp = mDirection;
                }
            }

            @Override
            public void onFinish() {
                LogUtils.i("onFinish");

                circularTrans(Direction.STOP);
            }
        });

        myHandler = new MyHandler();
        executor = Executors.newSingleThreadExecutor();
        revImageThread = new RevImageThread(myHandler);
        thread = new WorkThread();
    }
    private MyHandler myHandler;

    @Override
    public void initData() {
        super.initData();
        myHandler = new MyHandler();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    private enum Direction {
        STOP,
        FORWARD, BACK_OFF, LEFT_TURN, RIGHT_TURN,
        FORWARD_LEFT_TURN, FORWARD_RIGHT_TURN, BACK_OFF_LEFT_TURN, BACK_OFF_RIGHT_TURN,;
    }

    public void circularTrans(Direction direction) {

            switch (direction) {
                case FORWARD:
                    EventBus.getDefault().post(new SportMessage(FORWARD));
                    break;
                case BACK_OFF:
                    EventBus.getDefault().post(new SportMessage(BACK_OFF));
                    break;
                case LEFT_TURN:
                    EventBus.getDefault().post(new SportMessage(LEFT_TURN));
                    break;
                case RIGHT_TURN:
                    EventBus.getDefault().post(new SportMessage(RIGHT_TURN));
                    break;
                case BACK_OFF_LEFT_TURN:
                    EventBus.getDefault().post(new SportMessage(BACK_OFF_LEFT_TURN));
                    break;
                case FORWARD_RIGHT_TURN:
                    EventBus.getDefault().post(new SportMessage(FORWARD_RIGHT_TURN));
                    break;
                case FORWARD_LEFT_TURN:
                    EventBus.getDefault().post(new SportMessage(FORWARD_LEFT_TURN));
                    break;
                case BACK_OFF_RIGHT_TURN:
                    EventBus.getDefault().post(new SportMessage(BACK_OFF_RIGHT_TURN));
                    break;
                case STOP:
                    EventBus.getDefault().post(new SportMessage(STOP));
                    break;
                default:
                    EventBus.getDefault().post(new SportMessage(STOP));
                    break;
            }
            LogUtils.i("小车运动会:" + mDirection);
    }

    public void leftTurn(View view) {
        EventBus.getDefault().post(new SportMessage(LEFT_TURN));
    }

    public void rightTurn(View view) {
        EventBus.getDefault().post(new SportMessage(RIGHT_TURN));
    }

    public void forward(View view) {
        EventBus.getDefault().post(new SportMessage(FORWARD));
    }

    public void backOff(View view) {
        EventBus.getDefault().post(new SportMessage(BACK_OFF));
    }

    @SuppressLint("HandlerLeak")
    public class MyHandler extends Handler {
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
        IP = etIp.getText().toString().trim();
        EventBus.getDefault().post(new ConnectTcp(IP));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop();
    }

    public void stop() {
        isRunning = false;
        executor.shutdown();
    }

    class WorkThread extends Thread {


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
