package com.lz.serial.fragment.bean;

import android.support.annotation.NonNull;
import android.widget.SimpleCursorTreeAdapter;

import com.alibaba.fastjson.JSON;
import com.lz.serial.net.Contracts;

import java.nio.channels.UnresolvedAddressException;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/27 下午2:27
 * 描述     :
 */
public class JkBean extends BaseBean {

    /**
     * 图标
     */
    public boolean isGreen;
    /**
     * title
     */
    public String title;
    /**
     * 整组电压下限
     */
    public float overLineVoltageSet;
    /**
     * 单体电压下限
     */
    public float singleVoltageOnLine;
    /**
     * 整组电压
     */
    public float wholeVoltage;
    /**
     * 充放电电流
     */
    public float chargingDischargingCurrent;
    /**
     * 充放电容量
     */
    public float chargeDischargeCapacity;
    /**
     * 监测时长
     */
    public String monitoringDuration;

    public static JkBean getConvert(JkBean bean){
        JkBean convertBean = new JkBean();
        convertBean.isGreen = bean.isGreen;
        convertBean.title = bean.title;
        convertBean.wholeVoltage = bean.wholeVoltage ;
        convertBean.singleVoltageOnLine = bean.singleVoltageOnLine ;
        convertBean.overLineVoltageSet = bean.overLineVoltageSet ;
        convertBean.chargingDischargingCurrent = bean.chargingDischargingCurrent;
        convertBean.chargeDischargeCapacity = bean.chargeDischargeCapacity ;
        convertBean.monitoringDuration = bean.monitoringDuration;

        return convertBean;
    }

    public static String getJkBean(){
        JkBean jkBean = new JkBean();
        int isGreens = (int)(Math.random()*10);
        jkBean.isGreen = isGreens > 5;
        jkBean.title = "jk";
        jkBean.chargeDischargeCapacity = (float) (Math.random()*10);
        jkBean.chargingDischargingCurrent = (float) (Math.random()*10);
        jkBean.monitoringDuration = Math.random() + "";
        jkBean.overLineVoltageSet = (float) (Math.random()*10);
        jkBean.singleVoltageOnLine = (float) (Math.random()*10);
        jkBean.wholeVoltage = (float) (Math.random()*10);

        return JSON.toJSONString(jkBean);
    }


    public static String getStatusBean(String content){

        StatusBean bean = new StatusBean();
        bean.code =2;
        bean.msg = "消息正确";
        bean.content = content;
        return JSON.toJSONString(bean);
    }

    @NonNull
    @Override
    public String toString() {
        return "JkBean{" +
                "isGreen=" + isGreen +
                ", title='" + title + '\'' +
                ", overLineVoltageSet=" + overLineVoltageSet +
                ", singleVoltageOnLine=" + singleVoltageOnLine +
                ", wholeVoltage=" + wholeVoltage +
                ", chargingDischargingCurrent=" + chargingDischargingCurrent +
                ", chargeDischargeCapacity=" + chargeDischargeCapacity +
                ", monitoringDuration='" + monitoringDuration + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
