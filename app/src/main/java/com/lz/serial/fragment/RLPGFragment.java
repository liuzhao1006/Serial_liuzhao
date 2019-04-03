package com.lz.serial.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lz.serial.R;
import com.lz.serial.fragment.bean.RLPGBean;
import com.lz.serial.fragment.factory.BaseFragment;
import com.lz.serial.message.event.RLPGMessageEvent;
import com.lz.serial.net.Contracts;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/29 上午9:50
 * 描述     : 容量评估页面
 */
public class RLPGFragment extends BaseFragment {

    private TextView tv_title_rlpg;
    private TextView tv_pc_room_rlpg;
    private TextView tv_whole_set_type_rlpg;
    private TextView tv_monomer_type_rlpg;
    private TextView tv_battery_pack_number_rlpg;
    private TextView tv_number_nodes_per_group_rlpg;
    private TextView tv_nominal_capacity_rlpg;
    private TextView tv_test_time_rlpg;
    private TextView tv_monomer_sorting_rlpg;
    private TextView tv_reference_internal_resistance_rlpg;
    private TextView tv_count_down_rlpg;
    private ImageView ivStatusRLPG;

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
        return R.layout.fragment_rlpg;
    }

    @Override
    public void initViewSaved(View mRootView, Bundle savedInstanceState) {
        ivStatusRLPG = mRootView.findViewById(R.id.iv_status_rlpg);
        tv_title_rlpg = mRootView.findViewById(R.id.tv_title_rlpg);
        tv_pc_room_rlpg = mRootView.findViewById(R.id.tv_pc_room_rlpg);
        tv_whole_set_type_rlpg = mRootView.findViewById(R.id.tv_whole_set_type_rlpg);
        tv_monomer_type_rlpg = mRootView.findViewById(R.id.tv_monomer_type_rlpg);
        tv_battery_pack_number_rlpg = mRootView.findViewById(R.id.tv_battery_pack_number_rlpg);
        tv_number_nodes_per_group_rlpg = mRootView.findViewById(R.id.tv_number_nodes_per_group_rlpg);
        tv_nominal_capacity_rlpg = mRootView.findViewById(R.id.tv_nominal_capacity_rlpg);
        tv_test_time_rlpg = mRootView.findViewById(R.id.tv_test_time_rlpg);
        tv_monomer_sorting_rlpg = mRootView.findViewById(R.id.tv_monomer_sorting_rlpg);
        tv_reference_internal_resistance_rlpg = mRootView.findViewById(R.id.tv_reference_internal_resistance_rlpg);
        tv_count_down_rlpg = mRootView.findViewById(R.id.tv_count_down_rlpg);
        setTextColors( tv_title_rlpg,Contracts.VALUE_COLOR);
        setTextColors( tv_pc_room_rlpg,Contracts.VALUE_COLOR);
        setTextColors( tv_whole_set_type_rlpg,Contracts.VALUE_COLOR);
        setTextColors( tv_monomer_type_rlpg,Contracts.VALUE_COLOR);
        setTextColors( tv_battery_pack_number_rlpg,Contracts.VALUE_COLOR);
        setTextColors( tv_number_nodes_per_group_rlpg,Contracts.VALUE_COLOR);
        setTextColors( tv_nominal_capacity_rlpg,Contracts.VALUE_COLOR);
        setTextColors( tv_test_time_rlpg,Contracts.VALUE_COLOR);
        setTextColors( tv_monomer_sorting_rlpg,Contracts.VALUE_COLOR);
        setTextColors( tv_reference_internal_resistance_rlpg,Contracts.VALUE_COLOR);
        setTextColors( tv_count_down_rlpg,Contracts.VALUE_COLOR);
    }

    public void setTextView(RLPGBean bean) {

        setTextViews(tv_pc_room_rlpg,bean.pcRoom);
        setTextViews(tv_whole_set_type_rlpg,bean.wholeSetType + Contracts.UNIT_V);
        setTextViews(tv_monomer_type_rlpg,bean.monomerType + Contracts.UNIT_V);
        setTextViews(tv_title_rlpg,bean.title);
        setTextViews(tv_battery_pack_number_rlpg, bean.batteryPackNumber + Contracts.UNIT_HOURS);
        setTextViews(tv_number_nodes_per_group_rlpg, bean.numberNodesPerGroup + Contracts.UNIT_HOURS);
        setTextViews(tv_nominal_capacity_rlpg, bean.nominalCapacity + Contracts.UNIT_Ah);
        setTextViews(tv_test_time_rlpg, bean.testTime);
        setTextViews(tv_monomer_sorting_rlpg, bean.monomerSorting);
        setTextViews(tv_reference_internal_resistance_rlpg, bean.referenceInternalResistance + Contracts.UNIT_O);
        setTextViews(tv_count_down_rlpg, bean.countDown);
        if(bean.isGreen){
            setImageview(ivStatusRLPG,R.mipmap.status_normal);
            setTextColors(tv_title_rlpg,Contracts.VALUE_COLOR);
        }else {
            setImageview(ivStatusRLPG,R.mipmap.status_error);
            setTextColors(tv_title_rlpg,Contracts.VALUE_COLOR_EXCEPTION);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRLPGMessageEvent(RLPGMessageEvent messageEvent) {
        setTextView(RLPGBean.getConvert(messageEvent.getBean()));
    }
}
