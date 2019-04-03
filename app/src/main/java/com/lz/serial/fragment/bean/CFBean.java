package com.lz.serial.fragment.bean;

import com.alibaba.fastjson.JSON;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/29 上午9:43
 * 描述     :
 */
public class CFBean extends BaseBean {
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
    public float currentVoltageSet;
    /**
     * 当前轮次
     */
    public int currentRotation;
    /**
     * 当前充放电流
     */
    public float currentChargingDischargingCurrent;
    /**
     * 当前充放电时长
     */
    public String currentFillingTime;
    /**
     * 当前充放容量
     */
    public float currentChargingCapacity;

    public static CFBean getConvert(CFBean bean){
        CFBean convertBean = new CFBean();
        convertBean.isGreen = bean.isGreen;
        convertBean.title = bean.title;
        convertBean.currentChargingCapacity = bean.currentChargingCapacity ;
        convertBean.currentChargingDischargingCurrent = bean.currentChargingDischargingCurrent ;
        convertBean.currentFillingTime = bean.currentFillingTime;
        convertBean.currentRotation = bean.currentRotation;
        convertBean.currentVoltageSet = bean.currentVoltageSet ;
        return convertBean;
    }

    public static String getCFBean(){
        CFBean bean = new CFBean();
        int isGreens = (int)(Math.random()*10);
        bean.isGreen = isGreens > 5;
        bean.title = "CF";
        bean.currentVoltageSet = (float) (Math.random()*10);
        bean.currentRotation = (int) (Math.random()*10);
        bean.currentChargingDischargingCurrent = (float) (Math.random()*10);
        bean.currentFillingTime = (int) (Math.random()*10)+ "";
        bean.currentVoltageSet = (float) (Math.random()*10);
        bean.currentChargingCapacity = (float) (Math.random()*10);
        return JSON.toJSONString(bean);
    }

    public static String getStatusBean(String content){

        StatusBean bean = new StatusBean();
        bean.code = 3;
        bean.msg = "消息正确";
        bean.content = content;
        return JSON.toJSONString(bean);
    }
}
