package com.lz.serial.fragment.bean;

import com.alibaba.fastjson.JSON;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/29 下午3:01
 * 描述     : 单体放电
 */
public class HHYFBean extends BaseBean {

    /**
     * 图标
     */
    public boolean isGreen;

    /**
     * title
     */
    public String title;
    /**
     * 电压下限
     */
    public float voltageLowerLimit;
    /**
     * 放出容量
     */
    public float dischargeCapacity;
    /**
     * 放电时长
     */
    public String dischargeDuration;
    /**
     * 当前电压
     */
    public float currentVoltage;
    /**
     * 当前电流
     */
    public float currentCurrent;
    /**
     * 放出容量
     */
    public float currentDischargeCapacity;
    /**
     * 放电时长
     */
    public String currentDischargeDuration;

    public static HHYFBean getConvert(HHYFBean bean){
        HHYFBean convertBean = new HHYFBean();
        convertBean.isGreen = bean.isGreen;
        convertBean.title = bean.title;
        convertBean.currentCurrent = bean.currentCurrent;
        convertBean.currentDischargeCapacity = bean.currentDischargeCapacity ;
        convertBean.currentDischargeDuration = bean.currentDischargeDuration;
        convertBean.currentVoltage = bean.currentVoltage ;
        convertBean.dischargeCapacity = bean.dischargeCapacity ;
        convertBean.dischargeDuration = bean.dischargeDuration;
        convertBean.voltageLowerLimit = bean.voltageLowerLimit;
        return convertBean;

    }

    public static String getHHYFBean(){
        HHYFBean bean = new HHYFBean();
        int isGreens = (int)(Math.random()*10);
        bean.isGreen = isGreens > 5;
        bean.title = "HHYF";
        bean.voltageLowerLimit = (float) (Math.random()*10);
        bean.dischargeCapacity = (float) (Math.random()*10);
        bean.dischargeDuration = Math.random() + "";
        bean.currentVoltage = (float) (Math.random()*10);
        bean.currentCurrent = (float) (Math.random()*10);
        bean.currentDischargeCapacity = (float) (Math.random()*10);
        bean.currentDischargeDuration = Math.random() + "";
        return JSON.toJSONString(bean);
    }

    public static String getStatusBean(String content){

        StatusBean bean = new StatusBean();
        bean.code =11;
        bean.msg = "消息正确";
        bean.content = content;
        return JSON.toJSONString(bean);
    }
}
