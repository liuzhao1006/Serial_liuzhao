package com.lz.serial.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lz.serial.R;
import com.lz.serial.fragment.bean.CFBean;
import com.lz.serial.fragment.factory.BaseFragment;
import com.lz.serial.message.event.CFMessageEvent;
import com.lz.serial.net.Contracts;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/29 上午9:29
 * 描述     : 充放试验
 */
public class CFFragment extends BaseFragment {
    private ImageView ivStatusCF;
    private TextView tv_title_cf;
    private TextView tv_current_voltage_set_cf;
    private TextView tv_current_rotation_cf;
    private TextView tv_current_charging_discharging_current_cf;
    private TextView tv_current_filling_time_cf;
    private TextView tv_current_charging_capacity_cf;

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
        return R.layout.fragment_cf;
    }

    @Override
    public void initViewSaved(View view, Bundle savedInstanceState) {
        ivStatusCF = view.findViewById(R.id.iv_status_cf);
        tv_title_cf = view.findViewById(R.id.tv_title_cf);
        tv_current_voltage_set_cf = view.findViewById(R.id.tv_current_voltage_set_cf);
        tv_current_rotation_cf = view.findViewById(R.id.tv_current_rotation_cf);
        tv_current_charging_discharging_current_cf = view.findViewById(R.id.tv_current_charging_discharging_current_cf);
        tv_current_filling_time_cf = view.findViewById(R.id.tv_current_filling_time_cf);
        tv_current_charging_capacity_cf = view.findViewById(R.id.tv_current_charging_capacity_cf);
        setTextColors(tv_title_cf, Contracts.VALUE_COLOR);
        setTextColors(tv_current_voltage_set_cf, Contracts.VALUE_COLOR);
        setTextColors(tv_current_rotation_cf, Contracts.VALUE_COLOR);
        setTextColors(tv_current_charging_discharging_current_cf, Contracts.VALUE_COLOR);
        setTextColors(tv_current_filling_time_cf, Contracts.VALUE_COLOR);
        setTextColors(tv_current_charging_capacity_cf, Contracts.VALUE_COLOR);

    }

    public void setTextView(CFBean bean) {

        setTextViews(tv_current_voltage_set_cf,bean.currentVoltageSet + Contracts.UNIT_V);
        setTextViews(tv_current_rotation_cf,bean.currentRotation + Contracts.UNIT_HOURS);
        setTextViews(tv_current_charging_discharging_current_cf,bean.currentChargingDischargingCurrent + Contracts.UNIT_A);
        setTextViews(tv_title_cf,bean.title);
        setTextViews(tv_current_filling_time_cf, bean.currentFillingTime);
        setTextViews(tv_current_charging_capacity_cf, bean.currentChargingCapacity + Contracts.UNIT_Ah);
        if(bean.isGreen){
            setImageview(ivStatusCF,R.mipmap.status_normal);
            setTextColors(tv_title_cf,Contracts.VALUE_COLOR);
        }else {
            setImageview(ivStatusCF,R.mipmap.status_error);
            setTextColors(tv_title_cf,Contracts.VALUE_COLOR_EXCEPTION);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCFMessageEvent(CFMessageEvent messageEvent) {
        setTextView(CFBean.getConvert(messageEvent.getBean()));
    }
}
