package com.lz.serial.fragment.factory;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lz.serial.fragment.Ifragment;
import com.lz.serial.utils.Util;

import org.greenrobot.eventbus.EventBus;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/28 下午10:50
 * 描述     :
 */
public abstract class BaseFragment extends Fragment {
    protected Activity mActivity;
    public View mRootView;

    public static final String ERROR = "数据异常";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        registerEvent();
        mActivity = getActivity();
        super.onCreate(savedInstanceState);

    }

    protected abstract void registerEvent();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        initViewSaved(mRootView, savedInstanceState);
        return mRootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEvent();
    }

    protected abstract void unregisterEvent();

    /**
     * 初始化布局
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 保存状态
     *
     * @param mRootView
     * @param savedInstanceState
     */
    public abstract void initViewSaved(View mRootView, Bundle savedInstanceState);

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    protected void initData() {
    }

    protected void setTextColors(TextView tv, int color){
        if(tv == null){
            return;
        }
        tv.setTextColor(Util.getmContext().getResources().getColor(color));
    }

    protected void setTextViews(TextView tv, String msg){
        if(tv == null ){
            return;
        }
        if(msg == null){
            tv.setText(ERROR);
        }else {
            tv.setText(msg);
        }
    }

    protected void setImageview(ImageView iv, int icon){
        if(iv == null){
            return;
        }
        iv.setImageDrawable(Util.getmContext().getResources().getDrawable(icon));

    }
}
