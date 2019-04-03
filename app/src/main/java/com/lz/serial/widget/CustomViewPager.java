package com.lz.serial.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.View;

import com.lz.base.view.indicators.Indicator;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/15 下午7:52
 * 描述     :
 */
public class CustomViewPager extends Indicator {

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public View createTabView(PagerAdapter pagerAdapter, final int position) {
        return new CustomPagerTabView(mContext);
    }
}
