package com.lz.serial.fragment.bean;

import com.alibaba.fastjson.JSON;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/28 下午5:08
 * 描述     :
 */
public class FZBean extends BaseBean {

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
    public float lowerVoltageLimit;
    /**
     * 放出容量
     */
    public float dischargeCapacity;
    /**
     * 放电时长
     */
    public String dischargeDuration;
    /**
     * 当前整组电压
     */
    public float currentVoltageSet;
    /**
     * 当前放电电流
     */
    public float currentDischargeCurrent;
    /**
     * 当前放出容量
     */
    public float currentReleaseCapacity;
    /**
     * 当前放电时长
     */
    public String currentDischargeDuration;

    public static FZBean getConvert(FZBean bean){
        FZBean convertBean = new FZBean();
        convertBean.isGreen = bean.isGreen;
        convertBean.title = bean.title;
        convertBean.currentDischargeCurrent = bean.currentDischargeCurrent ;
        convertBean.currentDischargeDuration = bean.currentDischargeDuration;
        convertBean.currentReleaseCapacity = bean.currentReleaseCapacity ;
        convertBean.currentVoltageSet = bean.currentVoltageSet ;
        convertBean.dischargeCapacity = bean.dischargeCapacity ;
        convertBean.dischargeDuration = bean.dischargeDuration;
        convertBean.lowerVoltageLimit = bean.lowerVoltageLimit ;
        return convertBean;
    }

    public static String getFZBean(){
        FZBean bean = new FZBean();
        int isGreens = (int)(Math.random()*10);
        bean.isGreen = isGreens > 5;
        bean.title = "FZ";
        bean.lowerVoltageLimit = (float) (Math.random()*10);
        bean.dischargeCapacity = (float) (Math.random()*10);
        bean.dischargeDuration = Math.random() + "";

        bean.currentVoltageSet = (float) (Math.random()*10);
        bean.currentDischargeCurrent = (float) (Math.random()*10);
        bean.currentReleaseCapacity = (float) (Math.random()*10);
        bean.currentDischargeDuration = Math.random() + "";

        return JSON.toJSONString(bean);
    }

    public static String getStatusBean(String content){

        StatusBean bean = new StatusBean();
        bean.code =5;
        bean.msg = "消息正确";
        bean.content = content;
        return JSON.toJSONString(bean);
    }
}
