package com.lz.serial.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lz.serial.R;
import com.lz.serial.fragment.bean.JcBean;
import com.lz.serial.fragment.factory.BaseFragment;
import com.lz.serial.message.event.JCMessageEvent;
import com.lz.serial.net.Contracts;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/28 下午2:13
 * 描述     :
 */
public class JCFragment extends BaseFragment {
    @Override
    protected void registerEvent() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void unregisterEvent() {
        EventBus.getDefault().unregister(this);
    }

    private TextView tv_title_jc;
    private TextView tv_charging_capacity_jc;
    private TextView tv_charging_time_jc;
    private TextView tv_monomer_voltage_jc;
    private TextView tv_current_voltage_set_jc;
    private TextView tv_current_charging_current_jc;
    private TextView tv_current_filling_capacity_jc;
    private TextView tv_current_charging_time_jc;
    private TextView tv_charging_state_jc;
    private ImageView ivStatusJc;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_jc;
    }

    @Override
    public void initViewSaved(View view, Bundle savedInstanceState) {
        ivStatusJc = view.findViewById(R.id.iv_status_jc);
        tv_title_jc = view.findViewById(R.id.tv_title_jc);
        tv_charging_capacity_jc = view.findViewById(R.id.tv_charging_capacity_jc);
        tv_charging_time_jc = view.findViewById(R.id.tv_charging_time_jc);
        tv_monomer_voltage_jc = view.findViewById(R.id.tv_monomer_voltage_jc);

        tv_current_voltage_set_jc = view.findViewById(R.id.tv_current_voltage_set_jc);
        tv_current_charging_current_jc = view.findViewById(R.id.tv_current_charging_current_jc);
        tv_current_filling_capacity_jc = view.findViewById(R.id.tv_current_filling_capacity_jc);
        tv_current_charging_time_jc = view.findViewById(R.id.tv_current_charging_time_jc);
        tv_charging_state_jc = view.findViewById(R.id.tv_charging_state_jc);

        setTextColors(tv_title_jc,Contracts.VALUE_COLOR);
        setTextColors(tv_charging_capacity_jc,Contracts.VALUE_COLOR);
        setTextColors(tv_charging_time_jc,Contracts.VALUE_COLOR);
        setTextColors(tv_monomer_voltage_jc,Contracts.VALUE_COLOR);
        setTextColors(tv_current_voltage_set_jc,Contracts.VALUE_COLOR);
        setTextColors(tv_current_charging_current_jc,Contracts.VALUE_COLOR);
        setTextColors(tv_current_filling_capacity_jc,Contracts.VALUE_COLOR);
        setTextColors(tv_current_charging_time_jc,Contracts.VALUE_COLOR);
        setTextColors(tv_charging_state_jc,Contracts.VALUE_COLOR);
    }


    void setTextView(JcBean bean) {
        setTextViews(tv_title_jc,bean.title);
        setTextViews(tv_charging_capacity_jc,bean.chargingCapacity + Contracts.UNIT_Ah);
        setTextViews(tv_charging_time_jc,bean.chargingTime);
        setTextViews(tv_monomer_voltage_jc,bean.monomerVoltage + Contracts.UNIT_V);

        setTextViews(tv_current_voltage_set_jc, bean.currentVoltageSet + Contracts.UNIT_V);
        setTextViews(tv_current_charging_current_jc, bean.currentChargingCurrent+ Contracts.UNIT_A);
        setTextViews(tv_current_filling_capacity_jc, bean.currentFillingCapacity+ Contracts.UNIT_Ah);
        setTextViews(tv_current_charging_time_jc, bean.currentChargingTime);
        setTextViews(tv_charging_state_jc, bean.chargingState);
        if(bean.isGreen){
            setImageview(ivStatusJc,R.mipmap.status_normal);
            setTextColors(tv_title_jc,Contracts.VALUE_COLOR);
        }else {
            setImageview(ivStatusJc,R.mipmap.status_error);
            setTextColors(tv_title_jc,Contracts.VALUE_COLOR_EXCEPTION);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onJCMessageEvent(JCMessageEvent messageEvent) {
        setTextView(JcBean.getConvert(messageEvent.getJcBean()));
    }
}
