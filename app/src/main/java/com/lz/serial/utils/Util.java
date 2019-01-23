package com.lz.serial.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.lz.serial.SerialApp;


/**
 * 作者      : 刘朝
 * 创建日期  : 2018/12/22 下午9:39
 * 描述     :
 */
public class Util {

    public static Context getmContext(){
        return SerialApp.getmContext();
    }

    // 获取主线程的handler
    public static Handler getMainThreadHandler() {
        return SerialApp.getHandler();
    }

    // 获取主线程的id
    public static int getMainThreadId() {
        return SerialApp.getMainThreadId();
    }

    // 获取字符串资源
    public static String getString(int resId) {
        return getmContext().getResources().getString(resId);
    }

    // 获取字符串数组
    public static String[] getStringArray(int resId) {
        return getmContext().getResources().getStringArray(resId);
    }

    // 获取drawable
    public static Drawable getDrawable(int resId) {
        return getmContext().getResources().getDrawable(resId);
    }

    // 获取color的值
    public static int getColor(int resId) {
        return getmContext().getResources().getColor(resId);
    }

    // 获取颜色的状态选择器
    public static ColorStateList getColorStateList(int resId) {
        return getmContext().getResources().getColorStateList(resId);
    }

    // 获取dimen下定义的值
    public static int getDimen(int resId) {
        return getmContext().getResources().getDimensionPixelSize(resId);
    }

    // 判断当前线程是否处于主线程
    public static boolean isRunOnUiThread() {
        // 1、获取当前线程的线程id
        int currentThreadId = android.os.Process.myTid();
        // 2、获取主线程的线程id
        int mainThreadId = getMainThreadId();
        // 3、比较
        return currentThreadId == mainThreadId;
    }

    // 保证传递进来的r一定是在主线程中运行
    public static void runOnUiThread(Runnable r) {
        if (isRunOnUiThread()) {
            r.run();
            // new Thread(r).start();//此时启动了子线程
        } else {
            getMainThreadHandler().post(r);// 将r丢到了主线程的消息队列当中
        }
    }

    public static View inflate(int resId) {
        return View.inflate(getmContext(), resId, null);
    }

    public static void showToast(String msg) {
        Toast.makeText(getmContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(String msg, int time) {
        Toast.makeText(getmContext(), msg, time).show();
    }

    public static GradientDrawable getGradientDrawable(int radius, int color) {

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(radius);// 设置圆角半径
        drawable.setColor(color);
        return drawable;
    }

    public static StateListDrawable getSelecor(Drawable pressedDrawable, Drawable normalDrawable) {
        StateListDrawable selector = new StateListDrawable();
        //设置某些状态下应该显示的图片
        selector.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        selector.addState(new int[]{}, normalDrawable);
        return selector;
    }
}
