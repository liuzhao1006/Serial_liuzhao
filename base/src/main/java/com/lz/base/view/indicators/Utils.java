package com.lz.base.view.indicators;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;


public class Utils {

    /**
     * 将sp值转换为px值
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    /**
     * 计算屏宽度
     */
    public static int calculateScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 颜色渐变，需要把ARGB分别拆开进行渐变
     */
    public static int evaluateColor(int startValue, int endValue, float fraction) {
        if (fraction <= 0) {
            return startValue;
        }
        if (fraction >= 1) {
            return endValue;
        }
        int startInt = startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24)
                | ((startR + (int) (fraction * (endR - startR))) << 16)
                | ((startG + (int) (fraction * (endG - startG))) << 8)
                | ((startB + (int) (fraction * (endB - startB))));
    }

    /**
     * bitmap转base64
     * @param scaledBitmap
     * @return
     */
    public static String convertToBase64(Bitmap scaledBitmap) {
        if (scaledBitmap == null || scaledBitmap.isRecycled()) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
        byte[] bytes = out.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    private static final String TAG = Utils.class.getSimpleName();
    public static final String HBZ_ROOT_DIR = "com.tld.company";

    private static String getFlowerDir() {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + HBZ_ROOT_DIR + File.separator + "花卉识别");
        if (file.exists()) {
            return file.getPath();
        }
        if (file.mkdirs()) {
            return file.getPath();
        }
        Log.e(TAG, "获取花卉识别目录失败");
        return null;
    }

    public static String getFlowerSrcDir() {
        return getFlowerDir();
    }

    public static String getFlowerCropDir() {
        String flowerDir = getFlowerDir();
        if (flowerDir == null) {
            return null;
        }
        File file = new File(flowerDir + File.separator + "crop");
        if (file.exists()) {
            return file.getPath();
        }
        if (file.mkdirs()) {
            return file.getPath();
        }
        Log.e(TAG, "获取花卉识别裁剪目录失败");
        return null;
    }

}
