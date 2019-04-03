package com.lz.serial.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lz.serial.R;
import com.lz.serial.fragment.bean.JCFBean;
import com.lz.serial.fragment.factory.BaseFragment;
import com.lz.serial.message.event.JCFMessageEvent;
import com.lz.serial.net.Contracts;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/28 下午12:04
 * 描述     : 离线充放
 */
public class JCFFragment extends BaseFragment {

    private ImageView ivStatusJCF;
    private TextView tv_title_jcf;
    private TextView tv_current_voltage_set_jcf;
    private TextView tv_current_rotation_jcf;
    private TextView tv_current_charging_discharging_current_jcf;
    private TextView tv_current_filling_time_jcf;
    private TextView tv_current_charging_capacity_jcf;

    @Override
    protected void registerEvent() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void unregisterEvent() {
        EventBus.getDefault().unregister(this);
    }
    public void setTextView(JCFBean bean) {

        setTextViews(tv_current_voltage_set_jcf,bean.currentVoltageSet + Contracts.UNIT_V);
        setTextViews(tv_current_rotation_jcf,bean.currentRotation + Contracts.UNIT_HOURS);
        setTextViews(tv_current_charging_discharging_current_jcf,bean.currentChargingDischargingCurrent + Contracts.UNIT_A);
        setTextViews(tv_title_jcf,bean.title);

        setTextViews(tv_current_filling_time_jcf, bean.currentFillingTime);
        setTextViews(tv_current_charging_capacity_jcf, bean.currentChargingCapacity + Contracts.UNIT_Ah);
        if(bean.isGreen){
            setImageview(ivStatusJCF,R.mipmap.status_normal);
            setTextColors(tv_title_jcf,Contracts.VALUE_COLOR);
        }else {
            setImageview(ivStatusJCF,R.mipmap.status_error);
            setTextColors(tv_title_jcf,Contracts.VALUE_COLOR_EXCEPTION);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_jcf;
    }

    @Override
    public void initViewSaved(View view, Bundle savedInstanceState) {
        ivStatusJCF = view.findViewById(R.id.iv_status_jcf);
        tv_title_jcf = view.findViewById(R.id.tv_title_jcf);
        tv_current_voltage_set_jcf = view.findViewById(R.id.tv_current_voltage_set_jcf);
        tv_current_rotation_jcf = view.findViewById(R.id.tv_current_rotation_jcf);
        tv_current_charging_discharging_current_jcf = view.findViewById(R.id.tv_current_charging_discharging_current_jcf);
        tv_current_filling_time_jcf = view.findViewById(R.id.tv_current_filling_time_jcf);
        tv_current_charging_capacity_jcf = view.findViewById(R.id.tv_current_charging_capacity_jcf);
        setTextColors( tv_title_jcf, Contracts.VALUE_COLOR);
        setTextColors( tv_current_voltage_set_jcf, Contracts.VALUE_COLOR);
        setTextColors( tv_current_rotation_jcf, Contracts.VALUE_COLOR);
        setTextColors( tv_current_charging_discharging_current_jcf, Contracts.VALUE_COLOR);
        setTextColors( tv_current_filling_time_jcf, Contracts.VALUE_COLOR);
        setTextColors( tv_current_charging_capacity_jcf, Contracts.VALUE_COLOR);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onJCFMessageEvent(JCFMessageEvent messageEvent) {
        setTextView(JCFBean.getConvert(messageEvent.getBean()));
    }
}
