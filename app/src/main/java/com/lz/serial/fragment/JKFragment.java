package com.lz.serial.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lz.serial.R;
import com.lz.serial.fragment.bean.JkBean;
import com.lz.serial.fragment.factory.BaseFragment;
import com.lz.serial.message.event.JKMessageEvent;
import com.lz.serial.net.Contracts;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.lz.serial.net.Contracts.VALUE_COLOR;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/16 上午8:02
 * 描述     : 在线监测 jk
 */
public class JKFragment extends BaseFragment {

    @Override
    protected void registerEvent() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void unregisterEvent() {
        EventBus.getDefault().unregister(this);
    }
    private TextView tv0082ValueJk;
    private TextView tv0083ValueJk;
    private TextView tv0084;
    private TextView tv0085;
    private TextView tv0086;
    private TextView tv0087;
    private ImageView ivJk;
    private TextView tvTitleJk;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_jk;
    }

    @Override
    public void initViewSaved(View view, Bundle savedInstanceState) {
        ivJk = view.findViewById(R.id.iv_status_jk);
        tvTitleJk = view.findViewById(R.id.tv_title_jk);

        tv0082ValueJk = view.findViewById(R.id.tv_0082_value_jk);
        tv0083ValueJk = view.findViewById(R.id.tv_0083_value_jk);

        tv0084 = view.findViewById(R.id.tv_0084_value_jk);

        tv0085 = view.findViewById(R.id.tv_0085_value_jk);

        tv0086 = view.findViewById(R.id.tv_0086_value_jk);

        tv0087 = view.findViewById(R.id.tv_0087_value_jk);

        setTextColors(tv0082ValueJk,Contracts.VALUE_COLOR);
        setTextColors(tv0083ValueJk,Contracts.VALUE_COLOR);
        setTextColors(tv0084,Contracts.VALUE_COLOR);
        setTextColors(tv0085,Contracts.VALUE_COLOR);
        setTextColors(tv0086,Contracts.VALUE_COLOR);
        setTextColors(tv0087,Contracts.VALUE_COLOR);
        setTextColors(tvTitleJk,Contracts.VALUE_COLOR);
    }

    public void setTextView(JkBean bean) {
        setTextViews(tvTitleJk,bean.title);
        setTextViews(tv0082ValueJk,bean.overLineVoltageSet + Contracts.UNIT_V);
        setTextViews(tv0083ValueJk,bean.singleVoltageOnLine+ Contracts.UNIT_V);
        setTextViews(tv0084,bean.wholeVoltage+ Contracts.UNIT_V);
        setTextViews(tv0085, bean.chargeDischargeCapacity+ Contracts.UNIT_Ah);
        setTextViews(tv0086, bean.chargingDischargingCurrent+ Contracts.UNIT_A);
        setTextViews(tv0087, bean.monitoringDuration);
        if(bean.isGreen){
            setImageview(ivJk,R.mipmap.status_normal);
            setTextColors(tvTitleJk,VALUE_COLOR);
        }else {
            setImageview(ivJk,R.mipmap.status_error);
            setTextColors(tvTitleJk,Contracts.VALUE_COLOR_EXCEPTION);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onJkMessageEvent(JKMessageEvent messageEvent) {
        setTextView(JkBean.getConvert(messageEvent.getBean()));
    }
}
