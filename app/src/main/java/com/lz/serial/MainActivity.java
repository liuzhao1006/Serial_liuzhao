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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lz.base.base.BaseActivity;
import com.lz.base.log.LogUtils;
import com.lz.base.protocol.LzCrcUtils;
import com.lz.base.protocol.LzPacket;
import com.lz.base.protocol.LzParser;
import com.lz.base.protocol.common.LzUserOrder;
import com.lz.base.util.ConvertUtil;
import com.lz.serial.adapter.ReadAdapter;
import com.lz.serial.inter.IReadCallBack;
import com.lz.serial.message.LzTestMessage;
import com.lz.serial.message.LzVoltage;
import com.lz.serial.service.SerialService;
import com.lz.serial.utils.Util;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private AutoCompleteTextView tvAdressMessage;
    private AutoCompleteTextView tvReadWriteMessage;
    private AutoCompleteTextView tvContentMessage;
    private ListView lvReadView;
    private ReadAdapter readAdapter;

    private TextView tvTitle;
    private List<String> mList = new ArrayList<>();
    private LzVoltage voltage;
    private LzPacket lzPacket;
    private Spinner spinner;
    private LzTestMessage testMessage;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initUsb();
        voltage = new LzVoltage();
        testMessage = new LzTestMessage();
        tvTitle = findViewById(R.id.tv_title);
        tvAdressMessage = findViewById(R.id.tv_adress_message);
        tvReadWriteMessage = findViewById(R.id.tv_read_write_message);
        tvContentMessage = findViewById(R.id.tv_content_message);
        initListView();
        findViewById(R.id.btn_send_one).setOnClickListener(this);
        findViewById(R.id.btn_send_two).setOnClickListener(this);
        lzPacket = LzPacket.getmInstance();
        lzPacket.registMessage(voltage);
        lzPacket.registMessage(testMessage);
        //收到消息的地方.
        voltage.setIVoltage(msg -> Util.runOnUiThread(() -> {
            Util.showToast("LzVoltage " + msg.toString());
            LogUtils.i("LzVoltage " + msg.toString());
        }));
        testMessage.setIMessage(msg -> {
            Util.showToast("LzTestMessage " + msg.toString());
            LogUtils.i("LzTestMessage " + msg.toString());
        });

        setiReadCallBack(bytes -> Util.runOnUiThread(() -> {
            for (byte anArg0 : bytes) {
                lzPacket.unPack(anArg0);
            }
            if (readAdapter != null) {
                mList.add(ConvertUtil.bytesToHexString(bytes));
                readAdapter.notify(mList);
            }
        }));

        findViewById(R.id.tv_read_message).setOnClickListener(view -> {
            byte[] b = {(byte) 0x5A, (byte) 0XA5,
                    (byte) 0X04, (byte) 0X83, (byte) 0X11, (byte) 0X01, (byte) 0X01};
            Util.showToast(ConvertUtil.bytes2String(b));
            write(b, 0, b.length);
        });

        //spinner
        spinner = findViewById(R.id.spinner);
        String[] arr = this.getResources().getStringArray(R.array.arr);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        byte[] ledLight = LzUserOrder.getLedLight();
                        write(ledLight,0, ledLight.length);

                        Util.showToast(arr[position]+ position+ "  " + ConvertUtil.bytesToHexString(ledLight));
                        break;
                    case 1:
                        byte[] pageId = LzUserOrder.getPageId();
                        write(pageId,0,pageId.length);
                        Util.showToast(arr[position]+ position + "  " + ConvertUtil.bytesToHexString(pageId));
                        break;
                    case 2:
                        byte[] bytes = LzUserOrder.setLedLight(ConvertUtil.intToByte(setLedLight+=5));
                        write(bytes,0,bytes.length);
                        Util.showToast(arr[position]+ position + "  " + ConvertUtil.bytesToHexString(bytes));
                        break;
                    case 3:
                        byte[] pageId3 = LzUserOrder.setPageId(ConvertUtil.intToByte(pageIds++));
                        write(pageId3,0,pageId3.length);
                        Util.showToast(arr[position]+ position + "  " + ConvertUtil.bytesToHexString(pageId3));
                        break;
                    case 4:
                        byte[] bz = LzUserOrder.setBuzzer((byte) 0x20);
                        write(bz,0,bz.length);
                        Util.showToast(arr[position]+ position + "  " + ConvertUtil.bytesToHexString(bz));
                        break;
                    case 5:
                        byte[] reset = LzUserOrder.setReset();
                        write(reset,0,reset.length);
                        Util.showToast(arr[position]+ position + "  " + ConvertUtil.bytesToHexString(reset));
                        break;
                    case 6:
                        byte[] calibration = LzUserOrder.setCalibration();
                        write(calibration,0,calibration.length);
                        Util.showToast(arr[position]+ position + "  " + ConvertUtil.bytesToHexString(calibration));
                        break;
                    case 7:
                        byte[] touch = LzUserOrder.setTouchFunction((byte) 0x00);
                        write(touch,0,touch.length);
                        Util.showToast(arr[position] + position + "  " + ConvertUtil.bytesToHexString(touch));
                        break;
                    case 8:
                        byte[] time = LzUserOrder.setCurRtcTime();
                        write(time,0,time.length);
                        Util.showToast(arr[position]+ position + "  " + ConvertUtil.bytesToHexString(time));
                        break;
                    case 9:
                        String popupWindow = tvReadWriteMessage.getText().toString().trim();
                        LzUserOrder.setPopupWindow((byte)0x01);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private int setLedLight = 5 ;
    private int pageIds = 5 ;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send_one:
                LzParser msg = new LzParser();
                String order = tvReadWriteMessage.getText().toString().trim();
                msg.setOrder(0x82);
                String adress = tvAdressMessage.getText().toString().trim();
                //bytes
                String content = tvContentMessage.getText().toString().trim();
                byte[] bContent = ConvertUtil.hexStringToBytes(adress + content);
                msg.setBytes(bContent);

                LogUtils.i(msg.toString());
                byte[] pack = lzPacket.pack(msg);
                LogUtils.i("onClick R.id.btn_send_one " + ConvertUtil.bytes2String(pack));
                write(pack, 0, pack.length);
                break;
            case R.id.btn_send_two:
                break;
        }
    }


    private void initListView() {
        lvReadView = findViewById(R.id.lv_read_view);
        readAdapter = new ReadAdapter(this);
        lvReadView.setAdapter(readAdapter);
        lvReadView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        lvReadView.setStackFromBottom(true);
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
        lzPacket.unRegistMessage(voltage);
        lzPacket.unRegistMessage(testMessage);
    }

    private static IReadCallBack iReadCallBack;

    private static void setiReadCallBack(IReadCallBack callBack) {
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
//                    Util.showToast(device.toString());
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

                    if (iReadCallBack != null) {
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
