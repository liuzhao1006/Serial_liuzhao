package com.lz.serial.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lz.serial.R;
import com.lz.serial.fragment.bean.CDBean;
import com.lz.serial.fragment.factory.BaseFragment;
import com.lz.serial.message.event.CDMessageEvent;
import com.lz.serial.net.Contracts;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/16 上午7:58
 * 描述     : 离线充电
 */
public class CDFragment extends BaseFragment {
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
        return R.layout.fragment_cd;
    }

    @Override
    public void initViewSaved(View view, Bundle savedInstanceState) {
        ivStatus = view.findViewById(R.id.iv_status);
        tvTitleCd = view.findViewById(R.id.tv_title_cd);
        tvChargingCapacityCD = view.findViewById(R.id.tv_charging_capacity_cd);
        tvChargingtimeCD = view.findViewById(R.id.tv_charging_time_cd);
        tvCurrentVoltageSetCD = view.findViewById(R.id.tv_current_voltage_set_cd);
        tvCurrentChargingCurrentCD = view.findViewById(R.id.tv_current_charging_current_cd);
        tvCurrentFillingCapacityCD = view.findViewById(R.id.tv_current_filling_capacity_cd);
        tvCurrentChargingTimeCD = view.findViewById(R.id.tv_current_charging_time_cd);
        tvWaringCD = view.findViewById(R.id.tv_waring_cd);
        setTextColors(tvTitleCd,Contracts.VALUE_COLOR);
        setTextColors(tvChargingCapacityCD,Contracts.VALUE_COLOR);
        setTextColors(tvChargingtimeCD,Contracts.VALUE_COLOR);
        setTextColors(tvCurrentVoltageSetCD,Contracts.VALUE_COLOR);
        setTextColors(tvCurrentChargingCurrentCD,Contracts.VALUE_COLOR);
        setTextColors(tvCurrentFillingCapacityCD,Contracts.VALUE_COLOR);
        setTextColors(tvCurrentChargingTimeCD,Contracts.VALUE_COLOR);
        setTextColors(tvWaringCD,Contracts.VALUE_COLOR);
    }

    /**
     * 充入容量
     */
    private TextView tvChargingCapacityCD,tvChargingtimeCD,
            tvCurrentVoltageSetCD,tvCurrentChargingCurrentCD,
            tvCurrentFillingCapacityCD,tvCurrentChargingTimeCD,
            tvWaringCD,tvTitleCd;
    private ImageView ivStatus;


    synchronized void setTextView(final CDBean cdBean){
        setTextViews(tvTitleCd,cdBean.title);
        setTextViews(tvChargingCapacityCD,cdBean.tvChargingCapacityCD + Contracts.UNIT_Ah);
        setTextViews(tvChargingtimeCD,cdBean.tvChargingtimeCD);
        setTextViews(tvCurrentChargingCurrentCD,cdBean.tvCurrentChargingCurrentCD+ Contracts.UNIT_A);
        setTextViews(tvCurrentChargingTimeCD,cdBean.tvCurrentChargingTimeCD);
        setTextViews(tvCurrentFillingCapacityCD,cdBean.tvCurrentFillingCapacityCD+ Contracts.UNIT_Ah);
        setTextViews(tvCurrentVoltageSetCD,cdBean.tvCurrentVoltageSetCD+ Contracts.UNIT_V);
        setTextViews(tvWaringCD,cdBean.tvWaringCD);
        if(cdBean.tvWaringCD.equals("异常")){
            setTextColors(tvWaringCD,Contracts.VALUE_COLOR_EXCEPTION);
        }else {
            setTextColors(tvWaringCD,Contracts.VALUE_COLOR);
        }
        if(cdBean.isGreen){
            setImageview(ivStatus,R.mipmap.status_normal);
        }else {
            setImageview(ivStatus,R.mipmap.status_error);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCDMessageEvent(CDMessageEvent messageEvent) {
        setTextView(CDBean.getConvert(messageEvent.getMessage()));
    }

}
