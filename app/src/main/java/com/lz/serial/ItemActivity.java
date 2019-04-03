package com.lz.serial;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lz.base.base.BaseActivity;
import com.lz.base.log.LogUtils;
import com.lz.base.view.dialog.AlertDialog;
import com.lz.serial.fragment.bean.CDBean;
import com.lz.serial.fragment.bean.CFBean;
import com.lz.serial.fragment.bean.DCBean;
import com.lz.serial.fragment.bean.FZBean;
import com.lz.serial.fragment.bean.HHYCBean;
import com.lz.serial.fragment.bean.HHYCFBean;
import com.lz.serial.fragment.bean.HHYFBean;
import com.lz.serial.fragment.bean.JCFBean;
import com.lz.serial.fragment.bean.JFBean;
import com.lz.serial.fragment.bean.JcBean;
import com.lz.serial.fragment.bean.JkBean;
import com.lz.serial.fragment.bean.RLPGBean;
import com.lz.serial.fragment.factory.FragmentFactory;
import com.lz.serial.message.event.CDMessageEvent;
import com.lz.serial.message.event.CFMessageEvent;
import com.lz.serial.message.event.DCMessageEvent;
import com.lz.serial.message.event.FZMessageEvent;
import com.lz.serial.message.event.HHYCFMessageEvent;
import com.lz.serial.message.event.HHYCMessageEvent;
import com.lz.serial.message.event.HHYFMessageEvent;
import com.lz.serial.message.event.JCFMessageEvent;
import com.lz.serial.message.event.JCMessageEvent;
import com.lz.serial.message.event.JFMessageEvent;
import com.lz.serial.message.event.JKMessageEvent;
import com.lz.serial.message.event.LocationMessageEvent;
import com.lz.serial.message.event.RLPGMessageEvent;
import com.lz.serial.net.ConnectorInfo;
import com.lz.serial.net.Contracts;
import com.lz.serial.net.TcpClient;
import com.lz.serial.ui.ScanWifiActivity;
import com.lz.serial.utils.Util;
import com.lz.serial.widget.CustomViewPager;
import com.lz.serial.widget.PopupWindowUtils;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;
import net.qiujuer.library.clink.core.Connector;
import net.qiujuer.library.clink.core.IoContext;
import net.qiujuer.library.clink.core.ScheduleJob;
import net.qiujuer.library.clink.core.schedule.IdleTimeoutScheduleJob;
import net.qiujuer.library.clink.impl.IoSelectorProvider;
import net.qiujuer.library.clink.impl.SchedulerImpl;
import net.qiujuer.library.clink.utils.CloseUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class ItemActivity extends BaseActivity implements View.OnClickListener, TcpClient.ConnectorStatusListener {
    static final String[] LOCATIONGPS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE};

    private String[] mStrings = SerialApp.getmContext().getResources().getStringArray(R.array.fragment);
    private static final int BAIDU_READ_PHONE_STATE = 100;//定位权限请求
    private static final int PRIVATE_CODE = 1315;//开启GPS权限
    private TextView tvLocation;
    private TcpClient client;

    public Timer timer ;
    private ImageView ivWifiStatus;
    private ImageView iv;
    private PopupWindowUtils utils;

    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private TextView tvLocationDialog;

    @Override
    protected void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);

        tvLocation = findViewById(R.id.tv_location);
        iv = findViewById(R.id.iv_wifi_connect);
        iv.setOnClickListener(this);
        ivWifiStatus = findViewById(R.id.iv_wifi_status);
        ViewPager mViewPager = findViewById(R.id.view_pager5);

        MyMainAdapter adapter = new MyMainAdapter(getSupportFragmentManager(), mStrings);
        CustomViewPager dynamicPagerIndicator5 = findViewById(R.id.dynamic_pager_indicator5);
        mViewPager.setAdapter(adapter);
        dynamicPagerIndicator5.setViewPager(mViewPager);
        permissionLocation();
        LinearLayout llLocation = findViewById(R.id.ll_location);
        llLocation.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != locationClient) {
            locationClient.disableBackgroundLocation(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        boolean isBackground = ((SerialApp) SerialApp.getmContext()).isBackground();
        //如果app已经切入到后台，启动后台定位功能
        if (isBackground) {
            if (null != locationClient) {
                locationClient.enableBackgroundLocation(2001, buildNotification());
            }
        }
    }

    private void permissionLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager == null) {
            return;
        }
        boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (providerEnabled) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, LOCATIONGPS,
                            BAIDU_READ_PHONE_STATE);
                } else {
                    initLocation();
                }
            } else {
                initLocation();
            }
        } else {
            Toast.makeText(this, "系统检测到未开启GPS定位服务,请开启", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, PRIVATE_CODE);

        }
    }


    /**
     * 初始化定位
     *
     * @since 2.8.0
     */
    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);

        startLocation();
    }

    /**
     * 默认的定位参数
     *
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = location -> EventBus.getDefault().post(new LocationMessageEvent(location));

    /**
     * 停止定位
     *
     * @since 2.8.0
     */
    private void stopLocation() {
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void destroyLocation() {
        if (null != locationClient) {
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }



    /**
     * 开始定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void startLocation() {
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PRIVATE_CODE:
                //请求权限成功
                permissionLocation();
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_item;
    }





    public void showDialog() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("WIFI扫描")
                .setMessage("是否扫描wifi")
                .setCancelable(true)
                .setPositiveButton("YES", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    Toast.makeText(ItemActivity.this, "", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ItemActivity.this, ScanWifiActivity.class));

                }).setNegativeButton("NO", (dialogInterface, i) -> dialogInterface.dismiss())
        ;

        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_wifi_connect:
                showPop();
                break;
            case R.id.ll_location:
                dialog();
                break;
        }
    }

    @SuppressLint("InflateParams")
    private void showPop() {
        utils = new PopupWindowUtils(ItemActivity.this, iv);
        utils.showPop(LayoutInflater.from(this).inflate(R.layout.popup_view, null));
        utils.toggleBright();
        utils.setListeners(listeners);

    }

    private PopupWindowUtils.OnClickListeners listeners = view -> {
        if (view.getId() == R.id.tv_scan) {
            showDialog();
            utils.dismiss();
        } else if (view.getId() == R.id.tv_connect) {
            connectWifi();
            utils.dismiss();
        }
    };

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

        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                fixedThreadPool.execute(new WriteThread());
            }
        }, 1000, 1000);
    }

    public class WriteThread implements Runnable {

        @Override
        public void run() {

            synchronized (WriteThread.class) {
                try {
                    if (client != null) {
                        client.send(CDBean.getStatusBean(CDBean.getCD()));
                    }
                    Thread.sleep(100);
                    if (client != null) {
                        client.send(JkBean.getStatusBean(JkBean.getJkBean()));
                    }
                    Thread.sleep(100);
                    if (client != null) {
                        client.send(JcBean.getStatusBean(JcBean.getJcBean()));
                    }
                    Thread.sleep(100);
                    if (client != null) {
                        client.send(JFBean.getStatusBean(JFBean.getJFBean()));
                    }
                    Thread.sleep(100);
                    if (client != null) {
                        client.send(FZBean.getStatusBean(FZBean.getFZBean()));
                    }
                    Thread.sleep(100);
                    if (client != null) {
                        client.send(JCFBean.getStatusBean(JCFBean.getJCFBean()));
                    }
                    Thread.sleep(100);
                    if (client != null) {
                        client.send(CFBean.getStatusBean(CFBean.getCFBean()));
                    }
                    Thread.sleep(100);
                    if (client != null) {
                        client.send(RLPGBean.getStatusBean(RLPGBean.getRLPGBean()));
                    }
                    Thread.sleep(100);
                    if (client != null) {
                        client.send(DCBean.getStatusBean(DCBean.getDCBean()));
                    }
                    Thread.sleep(100);
                    if (client != null) {
                        client.send(HHYCBean.getStatusBean(HHYCBean.getHHYCBean()));
                    }
                    Thread.sleep(100);
                    if (client != null) {
                        client.send(HHYFBean.getStatusBean(HHYFBean.getHHYFBean()));
                    }
                    Thread.sleep(100);
                    if (client != null) {
                        client.send(HHYCFBean.getStatusBean(HHYCFBean.getHHYCFBean()));
                    }
                    LogUtils.i("message send finish!!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void onConnectorClosed(Connector connector) {
        LogUtils.i("Connetor " + connector.getKey());
        runOnUiThread(() -> {
            if (ivWifiStatus != null) {
                ivWifiStatus.setImageDrawable(getResources().getDrawable(R.mipmap.ic_wifi_disconnect));
                ivWifiStatus.setEnabled(true);
            }
        });

    }

    private void onNewMessageArrived(ConnectorInfo info) {

        switch (info.getMessgeType()) {
            case CF:
                LogUtils.i("cf " + info.getValue());
                EventBus.getDefault().post(new CFMessageEvent(info.getValue()));
                break;
            case FZ:
                LogUtils.i("fz " + info.getValue());
                EventBus.getDefault().post(new FZMessageEvent(info.getValue()));
                break;
            case JC:
                LogUtils.i("jc " + info.getValue());
                EventBus.getDefault().post(new JCMessageEvent(info.getValue()));
                break;
            case CD:
                LogUtils.i("cd " + info.getValue());
                EventBus.getDefault().post(new CDMessageEvent(info.getValue()));
                break;
            case JF:
                LogUtils.i("jf " + info.getValue());
                EventBus.getDefault().post(new JFMessageEvent(info.getValue()));
                break;
            case JK:
                LogUtils.i("jk " + info.getValue());
                EventBus.getDefault().post(new JKMessageEvent(info.getValue()));
                break;
            case JCF:
                LogUtils.i("JCF " + info.getValue());
                EventBus.getDefault().post(new JCFMessageEvent(info.getValue()));
                break;
            case JNF:
                break;
            case DJCF:
                LogUtils.i("DJCF " + info.getValue());
                EventBus.getDefault().post(new DCMessageEvent(info.getValue()));
                break;
            case JNCF:
                break;
            case RJCF:
                break;
            case HHYC:
                LogUtils.i("HHYC " + info.getValue());
                EventBus.getDefault().post(new HHYCMessageEvent(info.getValue()));
                break;
            case HHYF:
                LogUtils.i("HHYF " + info.getValue());
                EventBus.getDefault().post(new HHYFMessageEvent(info.getValue()));
                break;
            case HHYCF:
                LogUtils.i("HHYCF " + info.getValue());
                EventBus.getDefault().post(new HHYCFMessageEvent(info.getValue()));
                break;
            case RLPG:
                LogUtils.i("JCF " + info.getValue());
                EventBus.getDefault().post(new RLPGMessageEvent(info.getValue()));
                break;
            case ERROR:
                runOnUiThread(() -> Toast.makeText(ItemActivity.this, info.getValue(), Toast.LENGTH_SHORT).show());
                break;
            default:
                runOnUiThread(() -> Toast.makeText(ItemActivity.this, info.getValue(), Toast.LENGTH_SHORT).show());
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] paramArrayOfInt) {
        super.onRequestPermissionsResult(requestCode, permissions, paramArrayOfInt);
        switch (requestCode) {
            case BAIDU_READ_PHONE_STATE:
                if (paramArrayOfInt[0] == PERMISSION_GRANTED) {  //有权限
                    // 获取到权限，作相应处理
                    initLocation();
                } else {
                    permissionLocation();
                }
                break;
        }
    }


    private class InitAction implements Action {
        @Override
        public void call() {
            if (client != null && client.isConnect()) {
                LogUtils.i("已经连接了");
                return;
            }
            runOnUiThread(() -> {
                if (ivWifiStatus != null) {
                    ivWifiStatus.setImageDrawable(getResources().getDrawable(R.mipmap.ic_wifi_conne));
                    ivWifiStatus.setEnabled(false);
                }
            });
            try {
                IoContext.setup()
                        .ioProvider(new IoSelectorProvider())
                        .scheduler(new SchedulerImpl(1))
                        .start();
                client = new TcpClient(Contracts.IP, Contracts.PORT);
                client.setMessageArrivedListener(mMessageArrivedListener);
                client.setConnectorStatusListener(ItemActivity.this);
                client.send("我来了, 你们颤抖吧!");
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
            runOnUiThread(() -> {
                if (ivWifiStatus != null) {
                    ivWifiStatus.setImageDrawable(getResources().getDrawable(R.mipmap.ic_wifi_disconnect));
                    ivWifiStatus.setEnabled(true);
                }
            });

            if(timer != null){
                timer.cancel();
                timer = null;
            }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        stopLocation();
        destroyLocation();
        Run.onBackground(new DestroyAction());
        if (fixedThreadPool != null) {
            fixedThreadPool.shutdown();
            fixedThreadPool = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    private static final String NOTIFICATION_CHANNEL_NAME = "BackgroundLocation";
    private NotificationManager notificationManager = null;
    boolean isCreateChannel = false;

    @SuppressLint("NewApi")
    private Notification buildNotification() {

        Notification.Builder builder;
        Notification notification;
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            //Android O上对Notification进行了修改，如果设置的targetSDKVersion>=26建议使用此种方式创建通知栏
            if (null == notificationManager) {
                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            }
            String channelId = getPackageName();
            if (!isCreateChannel) {
                NotificationChannel notificationChannel = new NotificationChannel(channelId,
                        NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.enableLights(true);//是否在桌面icon右上角展示小圆点
                notificationChannel.setLightColor(Color.BLUE); //小圆点颜色
                notificationChannel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
                notificationManager.createNotificationChannel(notificationChannel);
                isCreateChannel = true;
            }
            builder = new Notification.Builder(getApplicationContext(), channelId);
        } else {
            builder = new Notification.Builder(getApplicationContext());
        }
        builder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(Util.getAppName(this))
                .setContentText("正在后台运行")
                .setWhen(System.currentTimeMillis());

        notification = builder.build();
        return notification;
    }


    private void dialog() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_location, null);

        tvLocationDialog = v.findViewById(R.id.tv_location_dialog);
        Button btnSureDialog = v.findViewById(R.id.btn_cancel_dialog);

        WindowManager manager = getWindowManager();
        int height = manager.getDefaultDisplay().getHeight();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setContentView(v)
                .formBottom(true)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, height)//屏幕的二分之一
                .addDefaultAnimation()
                .show();

        btnSureDialog.setOnClickListener(view -> builder.cancel());
    }


    class MyMainAdapter extends FragmentPagerAdapter {
        private String[] mTabNames;

        public MyMainAdapter(FragmentManager fm,String[] info) {
            super(fm);
            mTabNames = info;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabNames[position];
        }

        @Override
        public Fragment getItem(int position) {
            return FragmentFactory.getFragment(position);
        }

        @Override
        public int getCount() {
            return mTabNames.length;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationMessageEvent(LocationMessageEvent messageEvent) {
        LogUtils.i("位置 " + messageEvent.getLocationMsg());
        if(tvLocationDialog != null){
            tvLocationDialog.setText(messageEvent.getLocationMsg());
        }
        if(tvLocation != null){
            tvLocation.setText(messageEvent.getSimpleMsg());
        }
    }

}
