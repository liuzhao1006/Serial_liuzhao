package com.lz.serial.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.lz.base.Test;
import com.lz.serial.inter.ICallBack;
import com.lz.usbserial.driver.CDCSerialDevice;
import com.lz.usbserial.driver.UsbSerialDevice;
import com.lz.usbserial.driver.UsbSerialInterface;

import java.util.HashMap;
import java.util.Map;

public class SerialService extends Service {

    public static final String TAG = "SerialService";
    public static final String ACTION_USB_READY = "com.felhr.connectivityservices.USB_READY";
    public static final String ACTION_USB_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
    public static final String ACTION_USB_DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";
    public static final String ACTION_USB_NOT_SUPPORTED = "com.felhr.usbservice.USB_NOT_SUPPORTED";
    public static final String ACTION_NO_USB = "com.felhr.usbservice.NO_USB";
    public static final String ACTION_USB_PERMISSION_GRANTED = "com.felhr.usbservice.USB_PERMISSION_GRANTED";
    public static final String ACTION_USB_BL_PERMISSION_GRANTED = "com.felhr.usbservice.USB_BL_PERMISSION_GRANTED";
    public static final String ACTION_USB_PERMISSION_NOT_GRANTED = "com.felhr.usbservice.USB_PERMISSION_NOT_GRANTED";
    public static final String ACTION_USB_DISCONNECTED = "com.felhr.usbservice.USB_DISCONNECTED";
    public static final String ACTION_CDC_DRIVER_NOT_WORKING = "com.felhr.connectivityservices.ACTION_CDC_DRIVER_NOT_WORKING";
    public static final String ACTION_USB_DEVICE_NOT_WORKING = "com.felhr.connectivityservices.ACTION_USB_DEVICE_NOT_WORKING";
    public static final int USB_CLASS_MASS_STORAGE = 0x08;

    /**
     * subclass 6 means that the usb mass storage device implements the SCSI
     * transparent command set
     */
    public static final int INTERFACE_SUBCLASS = 6;

    /**
     * public 80 means the communication happens only via bulk transfers
     */
    public static final int INTERFACE_PROTOCOL = 80;
    public static final int MESSAGE_FROM_SERIAL_PORT = 0x10;
    public static final int CTS_CHANGE = 0x11;
    public static final int DSR_CHANGE = 0x12;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private static final int BAUD_RATE = 115200;
    public static boolean SERVICE_CONNECTED = false;

    private IBinder binder = new UsbBinder();

    private Context context;
    private Handler mHandler;
    private UsbManager usbManager;
    private UsbDevice device;
    private UsbDeviceConnection connection;
    private UsbSerialDevice serialPort;
    private PendingIntent mPermissionIntent;
    private boolean serialPortConnected;

    private UsbSerialInterface.UsbReadCallback mCallback = new UsbSerialInterface.UsbReadCallback() {
        @Override
        public void onReceivedData(byte[] arg0) {


            if (mHandler != null)
                mHandler.obtainMessage(MESSAGE_FROM_SERIAL_PORT, arg0).sendToTarget();
        }
    };

    private UsbSerialInterface.UsbCTSCallback ctsCallback = new UsbSerialInterface.UsbCTSCallback() {
        @Override
        public void onCTSChanged(boolean state) {
            if (mHandler != null)
                mHandler.obtainMessage(CTS_CHANGE).sendToTarget();
        }
    };

    private UsbSerialInterface.UsbDSRCallback dsrCallback = new UsbSerialInterface.UsbDSRCallback() {
        @Override
        public void onDSRChanged(boolean state) {
            if (mHandler != null)
                mHandler.obtainMessage(DSR_CHANGE).sendToTarget();
        }
    };

    public void requestPermission() {
        if(usbManager != null && device != null){
            usbManager.requestPermission(device, mPermissionIntent);
        }
    }

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            if (arg1.getAction().equals(ACTION_USB_PERMISSION)) {
                if(!usbManager.hasPermission(device)){
                    return;
                }
                boolean granted = arg1.getExtras().getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED);
                if (granted) {
                    if (!checkIfBootModeUsbDevice(device)) {
                        Intent intent = new Intent(ACTION_USB_PERMISSION_GRANTED);
                        arg0.sendBroadcast(intent);
                        connection = usbManager.openDevice(device);
                        new ConnectionThread().start();
                    } else {
                        Intent intent = new Intent(ACTION_USB_BL_PERMISSION_GRANTED);
                        arg0.sendBroadcast(intent);
                    }
                } else {
                    Intent intent = new Intent(ACTION_USB_PERMISSION_NOT_GRANTED);
                    arg0.sendBroadcast(intent);
                }
            } else if (arg1.getAction().equals(ACTION_USB_ATTACHED)) {
                if (!serialPortConnected)
                    findSerialPortDevice();
            } else if (arg1.getAction().equals(ACTION_USB_DETACHED)) {
                Intent intent = new Intent(ACTION_USB_DISCONNECTED);
                arg0.sendBroadcast(intent);
                if (serialPortConnected) {
                    serialPort.close();
                }
                serialPortConnected = false;
            }
        }
    };

    @Override
    public void onCreate() {
        this.context = this;
        serialPortConnected = false;
        SerialService.SERVICE_CONNECTED = true;
        setFilter();
        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        findSerialPortDevice();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SerialService.SERVICE_CONNECTED = false;
        unregisterReceiver(usbReceiver);
    }

    public void write(byte[] data) {
        if (serialPort != null)
            serialPort.write(data);
    }

    public void setHandler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    private boolean checkIfBootModeUsbDevice(UsbDevice device) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String productName = device.getProductName();
            String productNameUp = (productName != null) ? productName.toUpperCase() : "NULL";
            //do not connect in tanven bootloader mode
            if (productNameUp.contains("TANVEN BL")) {
                return true;
            }
        }
        return false;
    }

    private ICallBack iCallBack;
    public void setiCallBack(ICallBack iCallBack){
        this.iCallBack = iCallBack;
    }

    private void findSerialPortDevice() {
        HashMap<String, UsbDevice> usbDevices = usbManager.getDeviceList();
        Log.i(TAG, "USB,usbManager:" + usbManager.getAccessoryList());
        if (!usbDevices.isEmpty()) {
            boolean keep = true;
            for (Map.Entry<String, UsbDevice> entry : usbDevices.entrySet()) {
                device = entry.getValue();
                int deviceVID = device.getVendorId();
                int devicePID = device.getProductId();
                Log.i(TAG,"USB, device:" + device.toString());
                if (deviceVID != 0x1d6b && (devicePID != 0x0001 && devicePID != 0x0002 && devicePID != 0x0003) && deviceVID != 0x5c6 && devicePID != 0x904c) {
                    requestUserPermission();
                    keep = false;
                } else {
                    connection = null;
                    device = null;
                }

                if(iCallBack != null){
                    iCallBack.onCallBack(device);
                }

                if (!keep)
                    break;
            }
            if (!keep) {
                Intent intent = new Intent(ACTION_NO_USB);
                sendBroadcast(intent);
            }
            Log.i(TAG,"USB, connection:" + connection + ", device:" + device + ", keep:" + keep);
        } else {
            Intent intent = new Intent(ACTION_NO_USB);
            sendBroadcast(intent);
        }
    }

    private void setFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(ACTION_USB_DETACHED);
        filter.addAction(ACTION_USB_ATTACHED);
        registerReceiver(usbReceiver, filter);
    }

    private void requestUserPermission() {
        PendingIntent mPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        usbManager.requestPermission(device, mPendingIntent);
    }

    public class UsbBinder extends Binder {
        public SerialService getService() {
            return SerialService.this;
        }
    }

    public void closeUsbDevice() {
        if (serialPort != null && serialPortConnected) {
            serialPort.close();
            serialPortConnected = false;
        }
    }

    private class ConnectionThread extends Thread {
        @Override
        public void run() {
            serialPort = UsbSerialDevice.createUsbSerialDevice(device, connection);
            if (serialPort != null) {
                if (serialPort.open()) {
                    serialPortConnected = true;
                    serialPort.setBaudRate(BAUD_RATE);
                    serialPort.setDataBits(UsbSerialInterface.DATA_BITS_8);
                    serialPort.setStopBits(UsbSerialInterface.STOP_BITS_1);
                    serialPort.setParity(UsbSerialInterface.PARITY_NONE);
                    serialPort.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF);
                    serialPort.read(mCallback);
                    serialPort.getCTS(ctsCallback);
                    serialPort.getDSR(dsrCallback);
                    Intent intent = new Intent(ACTION_USB_READY);
                    context.sendBroadcast(intent);
                } else {
                    if (serialPort instanceof CDCSerialDevice) {
                        Intent intent = new Intent(ACTION_CDC_DRIVER_NOT_WORKING);
                        context.sendBroadcast(intent);
                    } else {
                        Intent intent = new Intent(ACTION_USB_DEVICE_NOT_WORKING);
                        context.sendBroadcast(intent);
                    }
                }
            } else {
                Intent intent = new Intent(ACTION_USB_NOT_SUPPORTED);
                context.sendBroadcast(intent);
            }
        }
    }
}
