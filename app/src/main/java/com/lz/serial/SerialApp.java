package com.lz.serial;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.lz.base.log.LogUtils;
import com.lz.base.util.CrashHandler;


/**
 * 作者      : 刘朝
 * 创建日期  : 2018/12/22 下午5:11
 * 描述     :
 */
public class SerialApp extends Application {

    private static Context mContext;
    private static Handler handler;
    private static int mainThreadId;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        CrashHandler.getInstance().init(getApplicationContext());
        LogUtils.setAppLogDir(LogUtils.LOG_ROOT_PATHE + LogUtils.APP_LOG_PATHE, 0 + "", 1, -1);
        LogUtils.setEnable(true);
//        LogcatHelper.getInstance(this).start();
        LogUtils.i("应用程序启动了！！！");

        handler = new Handler();

        mainThreadId = android.os.Process.myTid();
    }

    public static Context getmContext(){
        return mContext;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }
}
