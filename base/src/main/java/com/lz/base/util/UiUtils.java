package com.lz.base.util;

import android.content.Context;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * 作者      : 刘朝
 * 创建日期  : 2018/12/22 下午4:14
 * 描述     :
 */
public class UiUtils {




    /**
     * 获取网络配置
     *
     * @param keyName
     * @param context
     * @return
     */
    public static String getProperties(Context context, String keyName) {
        Properties props = new Properties();
        try {
            props.load(context.getAssets().open("config.properties"));
//            props.load(context.openFileInput("config.properties"));
            return props.getProperty(keyName);
        } catch (FileNotFoundException e) {
            Log.e("BaseManager::", "config.properties Not Found Exception", e);
            return "配置文件不存在";
        } catch (IOException e) {
            Log.e("BaseManager::", "config.properties IO Exception", e);
            return "读取失败";
        }

    }



    /**
     * 处理键盘显示和隐藏的方法
     *
     * @param v
     * @param event
     * @return
     */
    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if ((v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            return !(event.getRawX() > left) || !(event.getRawX() < right)
                    || !(event.getY() > top) || !(event.getY() < bottom);
        }
        return false;
    }

    public static void hideSoftInput(Context context, IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
