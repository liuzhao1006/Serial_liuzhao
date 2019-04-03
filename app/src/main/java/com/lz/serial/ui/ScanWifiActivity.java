package com.lz.serial.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lz.base.base.BaseActivity;
import com.lz.serial.R;
import com.lz.serial.utils.WifiUtils;

import java.util.List;

public class ScanWifiActivity extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener {


    private Button search_btn;
    private ListView wifi_lv;
    private WifiUtils mUtils;
    private List<String> result;
    private ProgressDialog progressdlg = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scan_wifi;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mUtils = new WifiUtils(this);
        findViews();
        setLiteners();
    }

    private void findViews() {
        this.search_btn = (Button) findViewById(R.id.search_btn);
        this.wifi_lv = (ListView) findViewById(R.id.wifi_lv);
    }

    private void setLiteners() {
        search_btn.setOnClickListener(this);
        wifi_lv.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.search_btn) {
            showDialog();
            new MyAsyncTask().execute();
        }
    }
    /**
     * init dialog and show
     */
    private void showDialog() {
        progressdlg = new ProgressDialog(this);
        progressdlg.setCanceledOnTouchOutside(false);
        progressdlg.setCancelable(false);
        progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressdlg.setMessage(getString(R.string.wait_moment));
        progressdlg.show();
    }


    /**
     * dismiss dialog
     */
    private void progressDismiss() {
        if (progressdlg != null) {
            progressdlg.dismiss();
        }
    }

    class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            //扫描附近WIFI信息
            result = mUtils.getScanWifiResult();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDismiss();
            initListViewData();
        }
    }

    private void initListViewData() {
        if (null != result && result.size() > 0) {
            wifi_lv.setAdapter(new ArrayAdapter<String>(
                    getApplicationContext(), R.layout.wifi_list_item,
                    R.id.ssid, result));
        } else {
            wifi_lv.setEmptyView(findViewById(R.layout.list_empty));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        TextView tv =  arg1.findViewById(R.id.ssid);
        if (!TextUtils.isEmpty(tv.getText().toString())) {
            Intent in = new Intent(ScanWifiActivity.this, WifiConnectActivity.class);
            in.putExtra("ssid", tv.getText().toString());
            startActivity(in);
        }
    }
}
