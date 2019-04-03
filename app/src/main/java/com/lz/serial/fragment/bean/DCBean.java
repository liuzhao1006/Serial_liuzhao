package com.lz.serial.fragment.bean;

import com.alibaba.fastjson.JSON;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/29 下午2:06
 * 描述     :  DC在线充放电
 */
public class DCBean extends BaseBean {

    /**
     * 图标
     */
    public boolean isGreen;
    /**
     * title
     */
    public String title;
    /**
     * 当前母线电压
     */
    public float currentBusVoltage;
    /**
     * 当前整组类型
     */
    public float currentVoltageSet;
    /**
     * 当前充电电流
     */
    public float currentChargingCurrent;
    /**
     * 当前充电时长
     */

    public String currentChargingTime;
    /**
     * 当前充入容量
     */
    public float currentFillingCapacity;


    public static DCBean getConvert(DCBean bean){
        DCBean convertBean = new DCBean();
        convertBean.isGreen = bean.isGreen;
        convertBean.title = bean.title;
        convertBean.currentBusVoltage = bean.currentBusVoltage ;
        convertBean.currentChargingCurrent = bean.currentChargingCurrent ;
        convertBean.currentChargingTime = bean.currentChargingTime;
        convertBean.currentFillingCapacity = bean.currentFillingCapacity ;
        convertBean.currentVoltageSet = bean.currentVoltageSet ;
        return convertBean;
    }

    public static String getDCBean(){
        DCBean bean = new DCBean();
        int isGreens = (int)(Math.random()*10);
        bean.isGreen = isGreens > 5;
        bean.title = "DC";
        bean.currentBusVoltage = (float) (Math.random()*10);
        bean.currentVoltageSet = (float) (Math.random()*10);
        bean.currentChargingCurrent = (float) (Math.random()*10);
        bean.currentChargingTime = Math.random() + "";
        bean.currentFillingCapacity = (float) (Math.random()*10);
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
