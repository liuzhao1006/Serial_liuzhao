package com.lz.serial;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.lz.base.log.LogUtils;
import com.lz.base.net.HttpInit;
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

    private int count = 0;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        CrashHandler.getInstance().init(getApplicationContext());
//        LogcatHelper.getInstance(this).start();
        //LogUtils.i("应用程序启动了！！！");
        HttpInit.init(getApplicationContext());
        handler = new Handler();
        mainThreadId = android.os.Process.myTid();

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                count ++;
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
                if(count > 0) {
                    count--;
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
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

    /**
     * 判断app是否在后台
     * @return
     */
    public boolean isBackground(){
        if(count <= 0){
            return true;
        } else {
            return false;
        }
    }

}
