package com.lz.serial;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lz.base.protocol.LzPacket;
import com.lz.base.base.BaseActivity;
import com.lz.base.log.LogUtils;
import com.lz.base.observe.Subject;
import com.lz.base.protocol.LzParser;
import com.lz.base.util.ConvertUtil;
import com.lz.serial.adapter.ReadAdapter;
import com.lz.serial.inter.IReadCallBack;

import com.lz.serial.service.SerialService;
import com.lz.serial.utils.Util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private AutoCompleteTextView tvAdressMessage;
    private AutoCompleteTextView tvReadWriteMessage;
    private AutoCompleteTextView tvContentMessage;
    private ListView lvReadView;
    private ReadAdapter readAdapter;

    private TextView tvTitle;
    private List<String> mList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initUsb();

        tvTitle = findViewById(R.id.tv_title);
        tvAdressMessage = findViewById(R.id.tv_adress_message);
        tvReadWriteMessage = findViewById(R.id.tv_read_write_message);
        tvContentMessage = findViewById(R.id.tv_content_message);
        initListView();
        initEditText();
        findViewById(R.id.btn_send_one).setOnClickListener(this);
        findViewById(R.id.btn_send_two).setOnClickListener(this);
       /* findViewById(R.id.btn_send_one).setOnClickListener(view -> {
            String msg = "5A A5 05 82 11 01 00 32";
            byte[] bytes = ConvertUtil.hexStringToBytes(msg);
            byte[] b = {(byte) 0x5A, (byte) 0XA5,
                    (byte) 0X05,(byte) 0X82,(byte) 0X11,(byte) 0X01,(byte) 0X00,
                    (byte) 0X32};

            Util.showToast(ConvertUtil.bytes2String(b));
            write(b, 0, b.length);
        });
        findViewById(R.id.btn_send_two).setOnClickListener(view -> {
            String msg = "5A A5 05 82 11 01 00 11";
            byte[] bytes = ConvertUtil.hexStringToBytes(msg);
            byte[] b = {(byte) 0x5A, (byte) 0XA5,
                    (byte) 0X05,(byte) 0X82,(byte) 0X11,(byte) 0X01,(byte) 0X00,
                    (byte) 0X11};

            Util.showToast(ConvertUtil.bytes2String(b));
            write(b, 0, b.length);
        });*/

        LzPacket lzPacket = LzPacket.getmInstance();
        setiReadCallBack(bytes -> Util.runOnUiThread(() -> {
            for (byte anArg0 : bytes) {
                lzPacket.unPack(anArg0);
            }
            if(readAdapter !=null){
                mList.add(ConvertUtil.bytesToHexString(bytes));
                readAdapter.notify(mList);
            }
        }));

        findViewById(R.id.tv_read_message).setOnClickListener(view -> {
            byte[] b = {(byte) 0x5A, (byte) 0XA5,
                    (byte) 0X04,(byte) 0X83,(byte) 0X11,(byte) 0X01,(byte) 0X01};
            Util.showToast(ConvertUtil.bytes2String(b));
            write(b, 0, b.length);
        });
    }

    private byte[] bData = new byte[255];

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_send_one:
                //获取地址
                String adress = tvAdressMessage.getText().toString().trim();
                //需要校验一下,这里忽略
                byte[] bAdress = ConvertUtil.hexStringToBytes(adress);
                String order = tvReadWriteMessage.getText().toString().trim();
                int bOrder = Integer.parseInt(order);
                String content = tvContentMessage.getText().toString().trim();
                byte[] bContent = ConvertUtil.hexStringToBytes(content);
                LzParser msg = new LzParser(bContent);
                msg.setAdress(bAdress);
                msg.setOrder(bOrder);


                LogUtils.i(msg.toString());

                break;
            case R.id.btn_send_two:
                break;
        }
    }

    private void initEditText() {
        tvAdressMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

            @Override
            public void afterTextChanged(Editable editable) {
                LogUtils.i("afterTextChanged " + editable.toString());

            }
        });
    }

    private void initListView() {
        lvReadView = findViewById(R.id.lv_read_view);
        readAdapter = new ReadAdapter(this);
        lvReadView.setAdapter(readAdapter);
        lvReadView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lvReadView.setStackFromBottom(true);
        /*//创造假数据
        List<String> mList = new ArrayList<>();
        for (int i = 0 ; i < 30 ; i++){
            mList.add("刘朝:" + i);
        }

        readAdapter.notify(mList);*/
    }


    @Override
    protected void onStart() {
        super.onStart();
        onUsbResume();//usb连接
    }

    @Override
    protected void onStop() {
        super.onStop();
        onUsbPause();
    }

    private static IReadCallBack iReadCallBack;
    private static void setiReadCallBack(IReadCallBack callBack){
        iReadCallBack = callBack;
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == null) {
                return;
            }
            switch (intent.getAction()) {
                case SerialService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    Util.runOnUiThread(() -> Util.showToast("USB Ready"));
                    break;
                case SerialService.ACTION_USB_BL_PERMISSION_GRANTED:
                    break;
                case SerialService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                    Util.runOnUiThread(() -> Util.showToast("USB Permission not granted"));
                    break;
                case SerialService.ACTION_NO_USB: // NO USB CONNECTED
                    Util.runOnUiThread(() -> Util.showToast("No USB connected"));
                    break;
                case SerialService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                    Util.runOnUiThread(() -> {
                        Util.showToast("USB disconnected");
                        tvTitle.setText("USB disconnected");
                    });
                    break;
                case SerialService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                    Util.runOnUiThread(() -> Util.showToast("USB device connect USB Storage"));
                    break;
            }
        }
    };
    private boolean inUpdateMode;
    private MyHandler mHandler;
    private SerialService serialService;
    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            serialService = ((SerialService.UsbBinder) arg1).getService();
            serialService.setHandler(mHandler);
            serialService.setiCallBack(device -> {
                LogUtils.i(device.toString());
                Util.runOnUiThread(() -> {
                    tvTitle.setText(device.getDeviceName());
                    Util.showToast(device.toString());
                });
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            serialService = null;
        }
    };

    private void initUsb() {
        mHandler = new MyHandler(this);
    }


    public void onUsbResume() {
        setFilters();  // Start listening notifications from SerialService
        startService(SerialService.class, usbConnection, null); // Start SerialService(if it was not started before) and Bind it
    }

    public void onUsbPause() {
        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);
        stopService(new Intent(this, SerialService.class));
    }

    public void onUsbClose(boolean updateMode) {
        inUpdateMode = updateMode;
        if (inUpdateMode && serialService != null) {
            serialService.closeUsbDevice();
        }
    }

    private UsbDevice usbDevice;

    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!SerialService.SERVICE_CONNECTED) {
            Intent startService = new Intent(this, service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            startService(startService);
        }
        Intent bindingIntent = new Intent(this, service);
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(SerialService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(SerialService.ACTION_NO_USB);
        filter.addAction(SerialService.ACTION_USB_DISCONNECTED);
        filter.addAction(SerialService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(SerialService.ACTION_USB_PERMISSION_NOT_GRANTED);
        registerReceiver(mUsbReceiver, filter);
    }

    public void connectUsb() {
        serialService.requestPermission();
    }

    private Subject testData;




    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public MyHandler(MainActivity activity) {
            mActivity = new WeakReference<>(activity);

        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SerialService.MESSAGE_FROM_SERIAL_PORT:
                    byte[] bytes = (byte[]) msg.obj;
                    LogUtils.i("bytes " + ConvertUtil.bytesToHexString(bytes));

                    if(iReadCallBack != null){
                        iReadCallBack.onIReadCallBack(bytes);
                    }
                    break;
                case SerialService.CTS_CHANGE:
                    Util.runOnUiThread(() -> Toast.makeText(mActivity.get(), "CTS_CHANGE", Toast.LENGTH_LONG).show());
                    break;
                case SerialService.DSR_CHANGE:
                    Util.runOnUiThread(() -> Toast.makeText(mActivity.get(), "DSR_CHANGE", Toast.LENGTH_LONG).show());
                    break;
                case SerialService.USB_CLASS_MASS_STORAGE:
                    Util.runOnUiThread(() -> Toast.makeText(mActivity.get(), "USB_CLASS_MASS_STORAGE", Toast.LENGTH_LONG).show());
                    break;
            }
        }
    }

    public void write(byte[] data, int off, int len) {
        if (serialService != null) {
            if (off == 0 && data.length == len) {
                serialService.write(data);
            } else {
                byte[] buff = Arrays.copyOfRange(data, off, len);
                serialService.write(buff);
            }
        }
    }


}
