package com.lz.serial.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lz.serial.R;
import com.lz.serial.fragment.bean.HHYFBean;
import com.lz.serial.fragment.factory.BaseFragment;
import com.lz.serial.message.event.HHYFMessageEvent;
import com.lz.serial.net.Contracts;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/29 下午2:42
 * 描述     : 单体放电
 */
public class HHYFFragment extends BaseFragment {
    private TextView tv_title_hhyf;
    private TextView tv_voltage_lower_limit_hhyf;
    private TextView tv_discharge_capacity_hhyf;
    private TextView tv_discharge_time_hhyf;
    private TextView tv_current_voltage_hhyf;
    private TextView tv_current_current_hhyf;
    private TextView tv_discharge_capacity_info_hhyf;
    private TextView tv_discharge_time_info_hhyf;
    private ImageView ivStatusHHYF;


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
        return R.layout.fragment_hhyf;
    }

    @Override
    public void initViewSaved(View mRootView, Bundle savedInstanceState) {
        ivStatusHHYF = mRootView.findViewById(R.id.iv_status_hhyf);

        tv_title_hhyf = mRootView.findViewById(R.id.tv_title_hhyf);
        tv_voltage_lower_limit_hhyf = mRootView.findViewById(R.id.tv_voltage_lower_limit_hhyf);
        tv_discharge_capacity_hhyf = mRootView.findViewById(R.id.tv_discharge_capacity_hhyf);
        tv_discharge_time_hhyf = mRootView.findViewById(R.id.tv_discharge_time_hhyf);
        tv_current_voltage_hhyf = mRootView.findViewById(R.id.tv_current_voltage_hhyf);
        tv_current_current_hhyf = mRootView.findViewById(R.id.tv_current_current_hhyf);
        tv_discharge_capacity_info_hhyf = mRootView.findViewById(R.id.tv_discharge_capacity_info_hhyf);
        tv_discharge_time_info_hhyf = mRootView.findViewById(R.id.tv_discharge_time_info_hhyf);
        setTextColors(tv_title_hhyf, Contracts.VALUE_COLOR);
        setTextColors(tv_voltage_lower_limit_hhyf, Contracts.VALUE_COLOR);
        setTextColors(tv_discharge_capacity_hhyf, Contracts.VALUE_COLOR);
        setTextColors(tv_discharge_time_hhyf, Contracts.VALUE_COLOR);
        setTextColors(tv_current_voltage_hhyf, Contracts.VALUE_COLOR);
        setTextColors(tv_current_current_hhyf, Contracts.VALUE_COLOR);
        setTextColors(tv_discharge_capacity_info_hhyf, Contracts.VALUE_COLOR);
        setTextColors(tv_discharge_time_info_hhyf, Contracts.VALUE_COLOR);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHHYFMessageEvent(HHYFMessageEvent messageEvent) {
        setTextView(HHYFBean.getConvert(messageEvent.getBean()));
    }

    private void setTextView(HHYFBean bean) {
        setTextViews(tv_title_hhyf,bean.title);
        setTextViews(tv_voltage_lower_limit_hhyf,bean.voltageLowerLimit + Contracts.UNIT_V);
        setTextViews(tv_discharge_capacity_hhyf,bean.dischargeCapacity + Contracts.UNIT_Ah);
        setTextViews(tv_discharge_time_hhyf,bean.dischargeDuration);

        setTextViews(tv_current_voltage_hhyf, bean.currentVoltage + Contracts.UNIT_V);
        setTextViews(tv_current_current_hhyf, bean.currentCurrent + Contracts.UNIT_A);
        setTextViews(tv_discharge_capacity_info_hhyf, bean.currentDischargeCapacity + Contracts.UNIT_Ah);
        setTextViews(tv_discharge_time_info_hhyf, bean.currentDischargeDuration);
        if(bean.isGreen){
            setImageview(ivStatusHHYF,R.mipmap.status_normal);
            setTextColors(tv_title_hhyf,Contracts.VALUE_COLOR);
        }else {
            setImageview(ivStatusHHYF,R.mipmap.status_error);
            setTextColors(tv_title_hhyf,Contracts.VALUE_COLOR_EXCEPTION);
        }
    }
}
