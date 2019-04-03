package com.lz.serial.fragment;

import android.content.Context;
import android.view.View;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/16 上午7:13
 * 描述     :
 */
public interface Ifragment {
    void addView(Context context, int fragmentIndex, View view);

    void destroy();
}
