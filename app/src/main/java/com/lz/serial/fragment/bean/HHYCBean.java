package com.lz.serial.fragment.bean;

import com.alibaba.fastjson.JSON;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/29 下午2:35
 * 描述     : 单体充电
 */
public class HHYCBean extends BaseBean {

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
     * 当前电池电压
     */
    public float currentBatteryVoltage;
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

    public static HHYCBean getConvert(HHYCBean bean){
        HHYCBean convertBean = new HHYCBean();
        convertBean.isGreen = bean.isGreen;
        convertBean.title = bean.title;
        convertBean.chargingCapacity = bean.chargingCapacity;
        convertBean.chargingTime = bean.chargingTime;
        convertBean.currentBatteryVoltage = bean.currentBatteryVoltage;
        convertBean.currentChargingCurrent = bean.currentChargingCurrent;
        convertBean.currentChargingTime = bean.currentChargingTime;
        convertBean.currentFillingCapacity = bean.currentFillingCapacity ;
        return convertBean;
    }

    public static String getHHYCBean(){
        HHYCBean bean = new HHYCBean();
        int isGreens = (int)(Math.random()*10);
        bean.isGreen = isGreens > 5;
        bean.title = "HHYC";
        bean.chargingCapacity = (float) (Math.random()*10);
        bean.chargingTime = Math.random() + "";
        bean.currentBatteryVoltage = (float) (Math.random()*10);
        bean.currentChargingCurrent = (float) (Math.random()*10);
        bean.currentFillingCapacity = (float) (Math.random()*10);
        bean.currentChargingTime = Math.random() + "";
        return JSON.toJSONString(bean);
    }

    public static String getStatusBean(String content){

        StatusBean bean = new StatusBean();
        bean.code =10;
        bean.msg = "消息正确";
        bean.content = content;
        return JSON.toJSONString(bean);
    }
}
