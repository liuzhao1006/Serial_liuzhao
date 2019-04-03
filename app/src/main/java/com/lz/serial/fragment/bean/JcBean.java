package com.lz.serial.fragment.bean;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/20 下午2:28
 * 描述     :  监测离线充电JC 数据封装
 */
public class JcBean extends BaseBean {

    /**
     * 图标
     */
    public boolean isGreen;

    /**
     * title
     */
    public String title;
    /**
     * 充入容量
     */
    public float chargingCapacity;
    /**
     * 充电时长
     */
    public String chargingTime;
    /**
     * 单体电压
     */
    public float monomerVoltage;

    /**
     * 当前整组电压
     */
    public float currentVoltageSet;
    /**
     * 当前充电电流
     */
    public float currentChargingCurrent;
    /**
     * 当前充入容量
     */
    public float currentFillingCapacity;
    /**
     * 当前充电时长
     */
    public String currentChargingTime;
    /**
     * 充电状态
     */
    public String chargingState;

    public static JcBean getConvert(JcBean bean){
        JcBean convertBean = new JcBean();
        convertBean.isGreen = bean.isGreen;
        convertBean.title = bean.title;
        convertBean.monomerVoltage = bean.monomerVoltage;
        convertBean.currentVoltageSet = bean.currentVoltageSet ;
        convertBean.currentFillingCapacity = bean.currentFillingCapacity ;
        convertBean.currentChargingTime = bean.currentChargingTime;
        convertBean.currentChargingCurrent = bean.currentChargingCurrent ;
        convertBean.chargingTime = bean.chargingTime;
        convertBean.chargingState = bean.chargingState;
        convertBean.chargingCapacity = bean.chargingCapacity ;
        return convertBean;
    }

    public static JcBean getJcBean(String json){
        return JSON.parseObject(json, JcBean.class);
    }
    public static String getJcBean(){
        JcBean jcBean = new JcBean();
        int isGreens = (int)(Math.random()*10);
        jcBean.isGreen = isGreens > 5;
        jcBean.title = "jc";
        jcBean.chargingCapacity = (float) (Math.random()*10);
        jcBean.chargingTime = Math.random() + "";
        jcBean.monomerVoltage = (float) (Math.random()*10);
        jcBean.currentVoltageSet = (float) (Math.random()*10);
        jcBean.currentChargingCurrent = (float) (Math.random()*10);
        jcBean.currentFillingCapacity = (float) (Math.random()*10);
        jcBean.currentChargingTime = Math.random() + "";
        jcBean.chargingState = Math.random() + "";
        return JSON.toJSONString(jcBean);
    }

    @NonNull
    @Override
    public String toString() {
        return "JcBean{" +
                "isGreen=" + isGreen +
                ", title='" + title + '\'' +
                ", chargingCapacity='" + chargingCapacity + '\'' +
                ", chargingTime='" + chargingTime + '\'' +
                ", monomerVoltage='" + monomerVoltage + '\'' +
                ", currentVoltageSet='" + currentVoltageSet + '\'' +
                ", currentChargingCurrent='" + currentChargingCurrent + '\'' +
                ", currentFillingCapacity='" + currentFillingCapacity + '\'' +
                ", currentChargingTime='" + currentChargingTime + '\'' +
                ", chargingState='" + chargingState + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }

    public static String getStatusBean(String content){

        StatusBean bean = new StatusBean();
        bean.code =1;
        bean.msg = "消息正确";
        bean.content = content;
        return JSON.toJSONString(bean);
    }

}
