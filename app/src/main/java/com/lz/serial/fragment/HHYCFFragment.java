package com.lz.serial.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lz.serial.R;
import com.lz.serial.fragment.bean.HHYCFBean;
import com.lz.serial.fragment.factory.BaseFragment;
import com.lz.serial.message.event.HHYCFMessageEvent;
import com.lz.serial.net.Contracts;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/29 下午2:42
 * 描述     : 单体放电
 */
public class HHYCFFragment extends BaseFragment {
    private ImageView iv_status_hhycf;
    private TextView tv_title_hhycf;
    private TextView tv_current_voltage_hhycf;
    private TextView tv_current_rotation_hhycf;
    private TextView tv_current_current_hhycf;
    private TextView tv_current_working_hours_hhycf;
    private TextView tv_current_capacity_hhycf;


    @Override
    protected void registerEvent() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void unregisterEvent() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_hhcf;
    }

    @Override
    public void initViewSaved(View mRootView, Bundle savedInstanceState) {
        iv_status_hhycf = mRootView.findViewById(R.id.iv_status_hhycf);

        tv_title_hhycf = mRootView.findViewById(R.id.tv_title_hhycf);
        tv_current_voltage_hhycf = mRootView.findViewById(R.id.tv_current_voltage_hhycf);
        tv_current_rotation_hhycf = mRootView.findViewById(R.id.tv_current_rotation_hhycf);
        tv_current_current_hhycf = mRootView.findViewById(R.id.tv_current_current_hhycf);
        tv_current_working_hours_hhycf = mRootView.findViewById(R.id.tv_current_working_hours_hhycf);
        tv_current_capacity_hhycf = mRootView.findViewById(R.id.tv_current_capacity_hhycf);
        setTextColors(tv_title_hhycf, Contracts.VALUE_COLOR);
        setTextColors(tv_current_voltage_hhycf,  Contracts.VALUE_COLOR);
        setTextColors(tv_current_rotation_hhycf,  Contracts.VALUE_COLOR);
        setTextColors(tv_current_current_hhycf,  Contracts.VALUE_COLOR);
        setTextColors(tv_current_working_hours_hhycf,  Contracts.VALUE_COLOR);
        setTextColors(tv_current_capacity_hhycf,  Contracts.VALUE_COLOR);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHHYCFMessageEvent(HHYCFMessageEvent messageEvent) {
        setTextView(HHYCFBean.getConvert(messageEvent.getBean()));
    }

    private void setTextView(HHYCFBean bean) {
        setTextViews(tv_title_hhycf,bean.title);
        setTextViews(tv_current_voltage_hhycf,bean.currentVoltage + Contracts.UNIT_V);
        setTextViews(tv_current_rotation_hhycf,bean.currentRotation + Contracts.UNIT_HOURS);
        setTextViews(tv_current_current_hhycf,bean.currentCurrent + Contracts.UNIT_A);
        setTextViews(tv_current_working_hours_hhycf, bean.currentWorkingHours);
        setTextViews(tv_current_capacity_hhycf, bean.currentCapacity + Contracts.UNIT_Ah);
        if(bean.isGreen){
            setImageview(iv_status_hhycf,R.mipmap.status_normal);
            setTextColors(tv_title_hhycf, Contracts.VALUE_COLOR);
        }else {
            setImageview(iv_status_hhycf,R.mipmap.status_error);
            setTextColors(tv_title_hhycf, Contracts.VALUE_COLOR_EXCEPTION);
        }
    }
}
