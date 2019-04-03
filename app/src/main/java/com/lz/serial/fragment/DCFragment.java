package com.lz.serial.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lz.serial.R;
import com.lz.serial.fragment.bean.DCBean;
import com.lz.serial.fragment.factory.BaseFragment;
import com.lz.serial.message.event.DCMessageEvent;
import com.lz.serial.net.Contracts;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/29 下午2:14
 * 描述     : 在线监测充放电DJCF
 */
public class DCFragment extends BaseFragment {
    private static final int VALUE_COLOR = R.color.lz_blue;
    private static final int VALUE_COLOR_EXCEPTION = R.color.lz_red;

    private ImageView ivStatusDC;
    private TextView tv_title_dc;
    private TextView tv_current_bus_voltage_dc;
    private TextView tv_current_voltage_set_dc;
    private TextView tv_current_charging_current_dc;
    private TextView tv_current_charging_time_dc;
    private TextView tv_current_filling_capacity_dc;

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
        return R.layout.fragment_dc;
    }

    @Override
    public void initViewSaved(View mRootView, Bundle savedInstanceState) {
        ivStatusDC = mRootView.findViewById(R.id.iv_status_dc);
        tv_title_dc = mRootView.findViewById(R.id.tv_title_dc);
        tv_current_bus_voltage_dc = mRootView.findViewById(R.id.tv_current_bus_voltage_dc);
        tv_current_voltage_set_dc = mRootView.findViewById(R.id.tv_current_voltage_set_dc);
        tv_current_charging_current_dc = mRootView.findViewById(R.id.tv_current_charging_current_dc);
        tv_current_charging_time_dc = mRootView.findViewById(R.id.tv_current_charging_time_dc);
        tv_current_filling_capacity_dc = mRootView.findViewById(R.id.tv_current_filling_capacity_dc);
        setTextColors(tv_title_dc, VALUE_COLOR);
        setTextColors(tv_current_bus_voltage_dc, VALUE_COLOR);
        setTextColors(tv_current_voltage_set_dc, VALUE_COLOR);
        setTextColors(tv_current_charging_current_dc, VALUE_COLOR);
        setTextColors(tv_current_charging_time_dc, VALUE_COLOR);
        setTextColors(tv_current_filling_capacity_dc, VALUE_COLOR);
    }

    public void setTextView(DCBean bean) {

        setTextViews(tv_title_dc,bean.title);
        setTextViews(tv_current_bus_voltage_dc,bean.currentBusVoltage + Contracts.UNIT_V);
        setTextViews(tv_current_voltage_set_dc,bean.currentVoltageSet + Contracts.UNIT_V);
        setTextViews(tv_current_charging_current_dc,bean.currentChargingCurrent + Contracts.UNIT_A);

        setTextViews(tv_current_charging_time_dc, bean.currentChargingTime);
        setTextViews(tv_current_filling_capacity_dc, bean.currentFillingCapacity + Contracts.UNIT_Ah);
        if(bean.isGreen){
            setImageview(ivStatusDC,R.mipmap.status_normal);
            setTextColors(tv_title_dc,VALUE_COLOR);
        }else {
            setImageview(ivStatusDC,R.mipmap.status_error);
            setTextColors(tv_title_dc,VALUE_COLOR_EXCEPTION);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDCMessageEvent(DCMessageEvent messageEvent) {
        setTextView(DCBean.getConvert(messageEvent.getBean()));
    }


}
