package com.lz.serial.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.lz.serial.SerialApp;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Locale;


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

    public static char convertDigit(int value) {
        value &= 0x0f;
        if (value >= 10)
            return ((char) (value - 10 + 'a'));
        else
            return ((char) (value + '0'));
    }

    public static String convert(final byte bytes[]) {

        StringBuffer sb = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            sb.append(convertDigit((int) (bytes[i] >> 4)));
            sb.append(convertDigit((int) (bytes[i] & 0x0f)));
        }
        return (sb.toString());

    }

    public static String convert(final byte bytes[],int pos, int len) {

        StringBuffer sb = new StringBuffer(len * 2);
        for (int i = pos; i < pos+len; i++) {
            sb.append(convertDigit((int) (bytes[i] >> 4)));
            sb.append(convertDigit((int) (bytes[i] & 0x0f)));
        }
        return (sb.toString());

    }

    public static byte[] md5Byte(String encryptStr) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(encryptStr.getBytes("UTF-8"));
        return md.digest();
    }

    public static String md5(String encryptStr) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(encryptStr.getBytes("UTF-8"));
        byte[] digest = md.digest();
        StringBuffer md5 = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            md5.append(Character.forDigit((digest[i] & 0xF0) >> 4, 16));
            md5.append(Character.forDigit((digest[i] & 0xF), 16));
        }

        encryptStr = md5.toString();
        return encryptStr;
    }

    public static boolean hasNetwork(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     *  开始定位
     */
    public final static int MSG_LOCATION_START = 0;
    /**
     * 定位完成
     */
    public final static int MSG_LOCATION_FINISH = 1;
    /**
     * 停止定位
     */
    public final static int MSG_LOCATION_STOP= 2;

    public final static String KEY_URL = "URL";
    public final static String URL_H5LOCATION = "file:///android_asset/sdkLoc.html";
    /**
     * 根据定位结果返回定位信息的字符串
     * @param location
     * @return
     */
    public synchronized static String getLocationStr(AMapLocation location){
        if(null == location){
            return null;
        }
        StringBuffer sb = new StringBuffer();
        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
        if(location.getErrorCode() == 0){
            sb.append("定位成功" + "\n");
            sb.append("定位类型: " + location.getLocationType() + "\n");
            sb.append("经    度    : " + location.getLongitude() + "\n");
            sb.append("纬    度    : " + location.getLatitude() + "\n");
            sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
            sb.append("提供者    : " + location.getProvider() + "\n");

            sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
            sb.append("角    度    : " + location.getBearing() + "\n");
            // 获取当前提供定位服务的卫星个数
            sb.append("星    数    : " + location.getSatellites() + "\n");
            sb.append("国    家    : " + location.getCountry() + "\n");
            sb.append("省            : " + location.getProvince() + "\n");
            sb.append("市            : " + location.getCity() + "\n");
            sb.append("城市编码 : " + location.getCityCode() + "\n");
            sb.append("区            : " + location.getDistrict() + "\n");
            sb.append("区域 码   : " + location.getAdCode() + "\n");
            sb.append("地    址    : " + location.getAddress() + "\n");
            sb.append("兴趣点    : " + location.getPoiName() + "\n");
            //定位完成的时间
            sb.append("定位时间: " + formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
        } else {
            //定位失败
            sb.append("定位失败" + "\n");
            sb.append("错误码:" + location.getErrorCode() + "\n");
            sb.append("错误信息:" + location.getErrorInfo() + "\n");
            sb.append("错误描述:" + location.getLocationDetail() + "\n");
        }
        //定位之后的回调时间
        sb.append("回调时间: " + formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");
        return sb.toString();
    }

    private static SimpleDateFormat sdf = null;
    public  static String formatUTC(long l, String strPattern) {
        if (TextUtils.isEmpty(strPattern)) {
            strPattern = "yyyy-MM-dd HH:mm:ss";
        }
        if (sdf == null) {
            try {
                sdf = new SimpleDateFormat(strPattern, Locale.CHINA);
            } catch (Throwable e) {
            }
        } else {
            sdf.applyPattern(strPattern);
        }
        return sdf == null ? "NULL" : sdf.format(l);
    }

    /**
     * 获取app的名称
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        String appName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            appName =  context.getResources().getString(labelRes);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return appName;
    }
}
