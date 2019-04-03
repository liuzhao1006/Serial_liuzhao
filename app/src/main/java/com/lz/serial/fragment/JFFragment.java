package com.lz.serial.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lz.serial.R;
import com.lz.serial.fragment.bean.JFBean;
import com.lz.serial.fragment.factory.BaseFragment;
import com.lz.serial.message.event.JFMessageEvent;
import com.lz.serial.net.Contracts;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/28 下午4:12
 * 描述     : 监测放电界面
 */
public class JFFragment extends BaseFragment {
    @Override
    protected void registerEvent() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void unregisterEvent() {
        EventBus.getDefault().unregister(this);
    }
    private ImageView ivStatusJF;
    private TextView tv_title_jf;
    private TextView tv_lower_voltage_limit_jf;
    private TextView tv_lower_limit_monomer_voltage_jf;
    private TextView tv_discharge_capacity_jf;
    private TextView tv_discharge_time_jf;
    private TextView tv_current_voltage_set_jf;
    private TextView tv_current_discharge_current_jf;
    private TextView tv_current_release_capacity_jf;
    private TextView tv_current_discharge_duration_jf;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_jf;
    }

    @Override
    public void initViewSaved(View view, Bundle savedInstanceState) {
        ivStatusJF = view.findViewById(R.id.iv_status_jf);
        tv_title_jf = view.findViewById(R.id.tv_title_jf);
        tv_lower_voltage_limit_jf = view.findViewById(R.id.tv_lower_voltage_limit_jf);
        tv_lower_limit_monomer_voltage_jf = view.findViewById(R.id.tv_lower_limit_monomer_voltage_jf);
        tv_discharge_capacity_jf = view.findViewById(R.id.tv_discharge_capacity_jf);
        tv_discharge_time_jf = view.findViewById(R.id.tv_discharge_time_jf);
        tv_current_voltage_set_jf = view.findViewById(R.id.tv_current_voltage_set_jf);
        tv_current_discharge_current_jf = view.findViewById(R.id.tv_current_discharge_current_jf);
        tv_current_release_capacity_jf = view.findViewById(R.id.tv_current_release_capacity_jf);
        tv_current_discharge_duration_jf = view.findViewById(R.id.tv_current_discharge_duration_jf);

        setTextColors( tv_title_jf,Contracts.VALUE_COLOR);
        setTextColors(tv_lower_voltage_limit_jf ,Contracts.VALUE_COLOR);
        setTextColors( tv_lower_limit_monomer_voltage_jf,Contracts.VALUE_COLOR);
        setTextColors( tv_discharge_capacity_jf,Contracts.VALUE_COLOR);
        setTextColors( tv_discharge_time_jf,Contracts.VALUE_COLOR);
        setTextColors( tv_current_voltage_set_jf,Contracts.VALUE_COLOR);
        setTextColors( tv_current_discharge_current_jf,Contracts.VALUE_COLOR);
        setTextColors( tv_current_release_capacity_jf,Contracts.VALUE_COLOR);
        setTextColors( tv_current_discharge_duration_jf,Contracts.VALUE_COLOR);

    }

    void setTextView(JFBean bean) {

        setTextViews(tv_title_jf,bean.title);
        setTextViews(tv_lower_voltage_limit_jf,bean.lowerVoltageLimit + Contracts.UNIT_V);
        setTextViews(tv_lower_limit_monomer_voltage_jf,bean.lowerLimitMonomerVoltage + Contracts.UNIT_V);
        setTextViews(tv_discharge_capacity_jf,bean.dischargeCapacity + Contracts.UNIT_Ah);

        setTextViews(tv_discharge_time_jf, bean.dischargeDuration);
        setTextViews(tv_current_voltage_set_jf, bean.currentVoltageSet + Contracts.UNIT_V);
        setTextViews(tv_current_discharge_current_jf, bean.currentDischargeCurrent + Contracts.UNIT_A);
        setTextViews(tv_current_release_capacity_jf, bean.currentReleaseCapacity + Contracts.UNIT_Ah);
        setTextViews(tv_current_discharge_duration_jf, bean.currentDischargeDuration);
        if(bean.isGreen){
            setImageview(ivStatusJF,R.mipmap.status_normal);
            setTextColors(tv_title_jf,Contracts.VALUE_COLOR);
        }else {
            setImageview(ivStatusJF,R.mipmap.status_error);
            setTextColors(tv_title_jf,Contracts.VALUE_COLOR_EXCEPTION);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onJFMessageEvent(JFMessageEvent messageEvent) {
        setTextView(JFBean.getConvert(messageEvent.getJfBean()));
    }
}
