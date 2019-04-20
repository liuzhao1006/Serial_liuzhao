package com.lz.serial.ui;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.hardware.usb.UsbDevice;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
import com.lz.base.protocol.LzPacket;
import com.lz.base.protocol.LzParser;
import com.lz.base.protocol.common.LzUserOrder;
import com.lz.base.util.ConvertUtil;
import com.lz.serial.R;
import com.lz.serial.adapter.ReadAdapter;
import com.lz.serial.inter.IReadCallBack;
import com.lz.serial.message.LzTestMessage;
import com.lz.serial.message.LzVoltage;
import com.lz.serial.service.TcpConnectService;
import com.lz.serial.service.UsbConnectService;
import com.lz.serial.ui.threader.ThreadActivity;
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

    private Handler handler;

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

        voltage.setIVoltage(msg -> Util.runOnUiThread(() -> Util.showToast(msg.toString(), Toast.LENGTH_LONG)));
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
                        write(ledLight);
                        Util.showToast(arr[position]+ position+ "  " + ConvertUtil.bytesToHexString(ledLight));
                        break;
                    case 1:
                        byte[] pageId = LzUserOrder.getPageId();
                        write(pageId);
                        Util.showToast(arr[position]+ position + "  " + ConvertUtil.bytesToHexString(pageId));
                        break;
                    case 2:
                        byte[] bytes = LzUserOrder.setLedLight(ConvertUtil.intToByte(setLedLight+=5));
                        write(bytes);
                        Util.showToast(arr[position]+ position + "  " + ConvertUtil.bytesToHexString(bytes));
                        break;
                    case 3:
                        byte[] pageId3 = LzUserOrder.setPageId(ConvertUtil.intToByte(pageIds++));
                        write(pageId3);
                        Util.showToast(arr[position]+ position + "  " + ConvertUtil.bytesToHexString(pageId3));
                        break;
                    case 4:
                        byte[] bz = LzUserOrder.setBuzzer((byte) 0x20);
                        write(bz);
                        Util.showToast(arr[position]+ position + "  " + ConvertUtil.bytesToHexString(bz));
                        break;
                    case 5:
                        byte[] reset = LzUserOrder.setReset();
                        write(reset);
                        Util.showToast(arr[position]+ position + "  " + ConvertUtil.bytesToHexString(reset));
                        break;
                    case 6:
                        byte[] calibration = LzUserOrder.setCalibration();
                        write(calibration);
                        Util.showToast(arr[position]+ position + "  " + ConvertUtil.bytesToHexString(calibration));
                        break;
                    case 7:
                        byte[] touch = LzUserOrder.setTouchFunction((byte) 0x00);
                        write(touch);
                        Util.showToast(arr[position] + position + "  " + ConvertUtil.bytesToHexString(touch));
                        break;
                    case 8:
                        byte[] time = LzUserOrder.setCurRtcTime();
                        write(time);
                        Util.showToast(arr[position]+ position + "  " + ConvertUtil.bytesToHexString(time));
                        break;
                    case 9:
                        String popupWindow = tvReadWriteMessage.getText().toString().trim();
                        LzUserOrder.setPopupWindow((byte)0x01);
                        break;
                    case 10:
                        byte[] color = LzUserOrder.setTextColor((byte) 0X82, ConvertUtil.intToByteArray(0xFFFF + 0x03), new byte[]{(byte) 0x82, (byte) 0x08});
                        write(color);
                        Util.showToast(arr[position]);
                        break;
                    case 11:
                        byte[] setNumber = LzUserOrder.setNumbericIntType((byte) 0X82, new byte[]{(byte)0x10,(byte)0x00}, 43);
                        write(setNumber);
                        Util.showToast(arr[position]);
                        break;
                    case 12:
                        byte[] setNumber2 = LzUserOrder.setNumbericIntType((byte) 0X82, new byte[]{(byte)0x10,(byte)0x01}, 55);
                        write(setNumber2);
                        Util.showToast(arr[position]);
                        break;
                    case 13:
                        byte[] setNumber3 = LzUserOrder.setNumbericIntType((byte) 0X82, new byte[]{(byte)0x10,(byte)0x00}, 60);
                        write(setNumber3);
                        byte[] setNumber4 = LzUserOrder.setNumbericIntType((byte) 0X82, new byte[]{(byte)0x10,(byte)0x01}, 61);
                        write(setNumber4);
                        Util.showToast(arr[position]);
                        break;
                    case 14:
                        //菜单弹出
                        byte[] dialog = LzUserOrder.showDialog((byte)0x01);
                        write(dialog);
                        Util.showToast(arr[position]);
                        break;
                    case 15:
                        //隐藏菜单
                        byte[] hide = LzUserOrder.hideDialog((byte)0x02);
                        write(hide);
                        Util.showToast(arr[position]);
                        break;
                    case 16:

                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Intent startSrv = new Intent(this.getApplicationContext(), UsbConnectService.class);
        this.getApplicationContext().startService(startSrv);
    }


    private static final int REQUEST_CODE_WRITE_SETTINGS = 1;
    private void requestWriteSettings() {
        Intent intent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_CODE_WRITE_SETTINGS );
        }

    }

    private int setLedLight = 5 ;
    private int pageIds = 5 ;
    @RequiresApi(api = Build.VERSION_CODES.M)
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
                write(pack);
                break;
            case R.id.btn_send_two:
                // mList.clear();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(!Settings.System.canWrite(this)){
                        requestWriteSettings();
                    }else {
//                        startActivity(new Intent(this, ItemActivity.class));
                        startActivity(new Intent(this, ThreadActivity.class));

                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_WRITE_SETTINGS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.System.canWrite(this)) {
                    startActivity(new Intent(this, ItemActivity.class));
                }
            }
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
                case UsbConnectService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    Util.runOnUiThread(() -> Util.showToast("USB Ready"));
                    break;
                case UsbConnectService.ACTION_USB_BL_PERMISSION_GRANTED:
                    break;
                case UsbConnectService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                    Util.runOnUiThread(() -> Util.showToast("USB Permission not granted"));
                    break;
                case UsbConnectService.ACTION_NO_USB: // NO USB CONNECTED
                    Util.runOnUiThread(() -> Util.showToast("No USB connected"));
                    break;
                case UsbConnectService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                    Util.runOnUiThread(() -> {
                        Util.showToast("USB disconnected");
                        tvTitle.setText("USB disconnected");
                    });
                    break;
                case UsbConnectService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                    Util.runOnUiThread(() -> Util.showToast("USB device connect USB Storage"));
                    break;
            }
        }
    };
    private boolean inUpdateMode;
    private MyHandler mHandler;
    private UsbConnectService usbConnectService;
    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            usbConnectService = ((UsbConnectService.UsbBinder) arg1).getService();
            usbConnectService.setHandler(mHandler);
            usbConnectService.setiCallBack(device -> {
                LogUtils.i(device.toString());
                Util.runOnUiThread(() -> {
                    tvTitle.setText(device.getDeviceName());
//                    Util.showToast(device.toString());
                });
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            usbConnectService = null;
        }
    };

    private void initUsb() {
        mHandler = new MyHandler(this);
    }


    public void onUsbResume() {
        setFilters();  // Start listening notifications from UsbConnectService
        startService(UsbConnectService.class, usbConnection, null); // Start UsbConnectService(if it was not started before) and Bind it
    }

    public void onUsbPause() {
        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);
        stopService(new Intent(this, UsbConnectService.class));
    }

    public void onUsbClose(boolean updateMode) {
        inUpdateMode = updateMode;
        if (inUpdateMode && usbConnectService != null) {
            usbConnectService.closeUsbDevice();
        }
    }

    private UsbDevice usbDevice;

    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbConnectService.SERVICE_CONNECTED) {
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
        filter.addAction(UsbConnectService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbConnectService.ACTION_NO_USB);
        filter.addAction(UsbConnectService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbConnectService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbConnectService.ACTION_USB_PERMISSION_NOT_GRANTED);
        registerReceiver(mUsbReceiver, filter);
    }

    public void connectUsb() {
        usbConnectService.requestPermission();
    }

    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public MyHandler(MainActivity activity) {
            mActivity = new WeakReference<>(activity);

        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbConnectService.MESSAGE_FROM_SERIAL_PORT:
                    byte[] bytes = (byte[]) msg.obj;
                    LogUtils.i("bytes " + ConvertUtil.bytesToHexString(bytes));

                    if (iReadCallBack != null) {
                        iReadCallBack.onIReadCallBack(bytes);
                    }
                    break;
                case UsbConnectService.CTS_CHANGE:
                    Util.runOnUiThread(() -> Toast.makeText(mActivity.get(), "CTS_CHANGE", Toast.LENGTH_LONG).show());
                    break;
                case UsbConnectService.DSR_CHANGE:
                    Util.runOnUiThread(() -> Toast.makeText(mActivity.get(), "DSR_CHANGE", Toast.LENGTH_LONG).show());
                    break;
                case UsbConnectService.USB_CLASS_MASS_STORAGE:
                    Util.runOnUiThread(() -> Toast.makeText(mActivity.get(), "USB_CLASS_MASS_STORAGE", Toast.LENGTH_LONG).show());
                    break;
            }
        }
    }

    public void write(byte[] data){
        write(data,0,data.length);
    }

    public void write(byte[] data, int off, int len) {
        if (usbConnectService != null) {
            if (off == 0 && data.length == len) {
                usbConnectService.write(data);
            } else {
                byte[] buff = Arrays.copyOfRange(data, off, len);
                usbConnectService.write(buff);
            }
        }
    }
}
