package com.lz.serial.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lz.base.base.BaseActivity;
import com.lz.base.log.LogUtils;
import com.lz.base.util.ConvertUtil;
import com.lz.serial.R;
import com.lz.serial.utils.Util;
import com.lz.serial.utils.WifiUtils;

public class WifiConnectActivity extends BaseActivity implements View.OnClickListener {


    private Button connect_btn;
    private TextView wifi_ssid_tv;
    private WifiUtils mUtils;
    // wifi之ssid
    private String ssid;
    private ProgressDialog progressdlg = null;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Util.showToast("WIFI连接成功");
                    finish();
                    break;
                case 1:
                    Util.showToast("WIFI连接失败");
                    break;

            }
            progressDismiss();
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wifi_connect;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mUtils = new WifiUtils(this);
        findViews();
        setLiteners();
        initDatas();
    }
    /**
     * init dialog
     */
    private void progressDialog() {
        progressdlg = new ProgressDialog(this);
        progressdlg.setCanceledOnTouchOutside(false);
        progressdlg.setCancelable(false);
        progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressdlg.setMessage(getString(R.string.wait_moment));
        progressdlg.show();
    }

    /**
     * dissmiss dialog
     */
    private void progressDismiss() {
        if (progressdlg != null) {
            progressdlg.dismiss();
        }
    }

    private void initDatas() {
        ssid = getIntent().getStringExtra("ssid");
        if (!TextUtils.isEmpty(ssid)) {
            ssid = ssid.replace("\"", "");
        }
        this.wifi_ssid_tv.setText(ssid);
    }

    private void findViews() {
        this.connect_btn = findViewById(R.id.connect_btn);
        this.wifi_ssid_tv = findViewById(R.id.wifi_ssid_tv);
    }

    private void setLiteners() {
        connect_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.connect_btn) {// 下一步操作
            //获取密码.
            int intPwd;
            try {
                intPwd = Integer.parseInt(ssid.substring(2));
            }catch (NumberFormatException e){
                Util.showToast("wifi连接有误,请重新连接");
                return;
            }

            if(intPwd == 0){
                Util.showToast("wifi连接有误,请重新连接");
                return;
            }
            String hexPwd = ConvertUtil.intToHex(intPwd);
            LogUtils.i("网络连接:账号 " + ssid + " 密码 " + hexPwd);
            progressDialog();
            // 在子线程中处理各种业务
            dealWithConnect(ssid, hexPwd);
        }
    }

    private void dealWithConnect(final String ssid, final String pwd) {
        new Thread(() -> {
            Looper.prepare();
            // 检验密码输入是否正确
            boolean pwdSucess = mUtils.connectWifiTest(ssid, pwd);
            try {
                Thread.sleep(4000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (pwdSucess) {
                mHandler.sendEmptyMessage(0);
            } else {
                mHandler.sendEmptyMessage(1);
            }
        }).start();
    }



}
