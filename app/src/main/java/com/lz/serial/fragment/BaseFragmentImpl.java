package com.lz.serial.fragment;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lz.serial.R;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/16 上午7:28
 * 描述     :
 */
public abstract class BaseFragmentImpl implements Ifragment {

    protected Context context;
    protected int fragmentIndex;
    protected View view;

    public static final String ERROR = "数据异常";



    @Override
    public void addView(Context context, int fragmentIndex, View view) {
        this.context = context;
        this.fragmentIndex = fragmentIndex;
        this.view = view;
        initView(view);
    }

    void setTextColors(TextView tv, int color){
        if(tv == null){
            return;
        }
        tv.setTextColor(context.getResources().getColor(color));
    }

    void setTextViews(TextView tv, String msg){
        if(tv == null ){
            return;
        }
        if(msg == null){
            tv.setText(ERROR);
        }else {
            tv.setText(msg);
        }
    }

    void setImageview(ImageView iv , int icon){
        if(iv == null){
            return;
        }
        iv.setImageDrawable(context.getResources().getDrawable(icon));

    }

    protected abstract void initView(View view);


}
