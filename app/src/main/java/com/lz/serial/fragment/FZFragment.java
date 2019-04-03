package com.lz.serial.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lz.serial.R;
import com.lz.serial.fragment.bean.FZBean;
import com.lz.serial.fragment.factory.BaseFragment;
import com.lz.serial.message.event.FZMessageEvent;
import com.lz.serial.net.Contracts;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/28 下午5:31
 * 描述     : 放电负载界面
 */
public class FZFragment extends BaseFragment {
    @Override
    protected void registerEvent() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void unregisterEvent() {
        EventBus.getDefault().unregister(this);
    }

    private ImageView ivStatusFZ;
    private TextView tv_title_fz;
    private TextView tv_lower_voltage_limit_fz;
    private TextView tv_discharge_capacity_fz;
    private TextView tv_discharge_time_fz;
    private TextView tv_current_voltage_set_fz;
    private TextView tv_current_discharge_current_fz;
    private TextView tv_current_release_capacity_fz;
    private TextView tv_current_discharge_duration_fz;


    public void setTextView(FZBean bean) {
        setTextViews(tv_title_fz, bean.title);
        setTextViews(tv_lower_voltage_limit_fz, bean.lowerVoltageLimit + Contracts.UNIT_V);
        setTextViews(tv_discharge_capacity_fz, bean.dischargeCapacity + Contracts.UNIT_Ah);
        setTextViews(tv_discharge_time_fz, bean.dischargeDuration);

        setTextViews(tv_current_voltage_set_fz, bean.currentVoltageSet + Contracts.UNIT_V);
        setTextViews(tv_current_discharge_current_fz, bean.currentDischargeCurrent + Contracts.UNIT_A);
        setTextViews(tv_current_release_capacity_fz, bean.currentReleaseCapacity + Contracts.UNIT_Ah);
        setTextViews(tv_current_discharge_duration_fz, bean.currentDischargeDuration);
        if (bean.isGreen) {
            setImageview(ivStatusFZ, R.mipmap.status_normal);
            setTextColors(tv_title_fz, Contracts.VALUE_COLOR);
        } else {
            setImageview(ivStatusFZ, R.mipmap.status_error);
            setTextColors(tv_title_fz, Contracts.VALUE_COLOR_EXCEPTION);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_fz;
    }

    @Override
    public void initViewSaved(View view, Bundle savedInstanceState) {
        ivStatusFZ = view.findViewById(R.id.iv_status_fz);

        tv_title_fz = view.findViewById(R.id.tv_title_fz);
        tv_lower_voltage_limit_fz = view.findViewById(R.id.tv_lower_voltage_limit_fz);
        tv_discharge_capacity_fz = view.findViewById(R.id.tv_discharge_capacity_fz);
        tv_discharge_time_fz = view.findViewById(R.id.tv_discharge_time_fz);
        tv_current_voltage_set_fz = view.findViewById(R.id.tv_current_voltage_set_fz);
        tv_current_discharge_current_fz = view.findViewById(R.id.tv_current_discharge_current_fz);
        tv_current_release_capacity_fz = view.findViewById(R.id.tv_current_release_capacity_fz);
        tv_current_discharge_duration_fz = view.findViewById(R.id.tv_current_discharge_duration_fz);

        setTextColors(tv_title_fz, Contracts.VALUE_COLOR);
        setTextColors(tv_lower_voltage_limit_fz, Contracts.VALUE_COLOR);
        setTextColors(tv_discharge_capacity_fz, Contracts.VALUE_COLOR);
        setTextColors(tv_discharge_time_fz, Contracts.VALUE_COLOR);
        setTextColors(tv_current_voltage_set_fz, Contracts.VALUE_COLOR);
        setTextColors(tv_current_discharge_current_fz, Contracts.VALUE_COLOR);
        setTextColors(tv_current_release_capacity_fz, Contracts.VALUE_COLOR);
        setTextColors(tv_current_discharge_duration_fz, Contracts.VALUE_COLOR);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFZMessageEvent(FZMessageEvent messageEvent) {
        setTextView(FZBean.getConvert(messageEvent.getBean()));
    }
}
