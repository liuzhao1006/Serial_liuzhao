package com.lz.serial.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lz.serial.R;
import com.lz.serial.fragment.bean.HHYCBean;
import com.lz.serial.fragment.factory.BaseFragment;
import com.lz.serial.message.event.HHYCMessageEvent;
import com.lz.serial.net.Contracts;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/29 下午2:42
 * 描述     : 单体充电测试
 */
public class HHYCFragment extends BaseFragment {

    private TextView tv_title_hhyc;
    private TextView tv_charging_capacity_hhyc;
    private TextView tv_charging_time_hhyc;
    private TextView tv_current_battery_voltage_hhyc;
    private TextView tv_current_charging_current_hhyc;
    private TextView tv_current_filling_capacity_hhyc;
    private TextView tv_current_charging_time_hhyc;
    private ImageView ivStatusHHYC;

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
        return R.layout.fragment_hhyc;
    }

    @Override
    public void initViewSaved(View mRootView, Bundle savedInstanceState) {
        ivStatusHHYC = mRootView.findViewById(R.id.iv_status_hhyc);

        tv_title_hhyc = mRootView.findViewById(R.id.tv_title_hhyc);
        tv_charging_capacity_hhyc = mRootView.findViewById(R.id.tv_charging_capacity_hhyc);
        tv_charging_time_hhyc = mRootView.findViewById(R.id.tv_charging_time_hhyc);
        tv_current_battery_voltage_hhyc = mRootView.findViewById(R.id.tv_current_battery_voltage_hhyc);
        tv_current_charging_current_hhyc = mRootView.findViewById(R.id.tv_current_charging_current_hhyc);
        tv_current_filling_capacity_hhyc = mRootView.findViewById(R.id.tv_current_filling_capacity_hhyc);
        tv_current_charging_time_hhyc = mRootView.findViewById(R.id.tv_current_charging_time_hhyc);
        setTextColors( tv_title_hhyc, Contracts.VALUE_COLOR);
        setTextColors( tv_charging_capacity_hhyc,  Contracts.VALUE_COLOR);
        setTextColors( tv_charging_time_hhyc,  Contracts.VALUE_COLOR);
        setTextColors( tv_current_battery_voltage_hhyc,  Contracts.VALUE_COLOR);
        setTextColors( tv_current_charging_current_hhyc,  Contracts.VALUE_COLOR);
        setTextColors( tv_current_filling_capacity_hhyc,  Contracts.VALUE_COLOR);
        setTextColors( tv_current_charging_time_hhyc,  Contracts.VALUE_COLOR);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHHYCMessageEvent(HHYCMessageEvent messageEvent) {
        setTextView(HHYCBean.getConvert(messageEvent.getBean()));
    }

    private void setTextView(HHYCBean bean) {
        setTextViews(tv_charging_capacity_hhyc,bean.chargingCapacity + Contracts.UNIT_Ah);
        setTextViews(tv_charging_time_hhyc,bean.chargingTime);
        setTextViews(tv_current_battery_voltage_hhyc,bean.currentBatteryVoltage + Contracts.UNIT_V);
        setTextViews(tv_title_hhyc,bean.title);

        setTextViews(tv_current_charging_current_hhyc, bean.currentChargingCurrent + Contracts.UNIT_A);
        setTextViews(tv_current_filling_capacity_hhyc, bean.currentFillingCapacity + Contracts.UNIT_Ah);
        setTextViews(tv_current_charging_time_hhyc, bean.currentChargingTime);
        if(bean.isGreen){
            setImageview(ivStatusHHYC,R.mipmap.status_normal);
            setTextColors(tv_title_hhyc,Contracts.VALUE_COLOR);
        }else {
            setImageview(ivStatusHHYC,R.mipmap.status_error);
            setTextColors(tv_title_hhyc,Contracts.VALUE_COLOR_EXCEPTION);
        }
    }
}
