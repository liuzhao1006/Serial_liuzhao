package com.lz.serial.fragment.bean;

import com.alibaba.fastjson.JSON;
import com.lz.serial.fragment.JCFFragment;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/28 下午6:21
 * 描述     :
 */
public class JCFBean extends BaseBean {

    /**
     * 图标
     */
    public boolean isGreen;
    /**
     * title
     */
    public String title;

    /**
     * 当前整组电压
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
     * 当前充放时长
     */
    public String currentFillingTime;
    /**
     * 当前充放容量
     */
    public float currentChargingCapacity;

    public static JCFBean getConvert(JCFBean bean){
        JCFBean convertBean = new JCFBean();
        convertBean.isGreen = bean.isGreen;
        convertBean.title = bean.title;
        convertBean.currentChargingCapacity = bean.currentChargingCapacity ;
        convertBean.currentChargingDischargingCurrent = bean.currentChargingDischargingCurrent ;
        convertBean.currentFillingTime = bean.currentFillingTime;
        convertBean.currentRotation = bean.currentRotation;
        convertBean.currentVoltageSet = bean.currentVoltageSet ;
        return convertBean;
    }

    public static String getJCFBean(){
        JCFBean bean = new JCFBean();
        int isGreens = (int)(Math.random()*10);
        bean.isGreen = isGreens > 5;
        bean.title = "JCF";
        bean.currentVoltageSet = (float) (Math.random()*10);
        bean.currentRotation = (int) (Math.random()*10);
        bean.currentChargingDischargingCurrent = (float) (Math.random()*10);
        bean.currentFillingTime = (int) (Math.random()*10) + "";
        bean.currentVoltageSet = (float) (Math.random()*10);
        bean.currentChargingCapacity = (float) (Math.random()*10);
        return JSON.toJSONString(bean);
    }

    public static String getStatusBean(String content){

        StatusBean bean = new StatusBean();
        bean.code = 6;
        bean.msg = "消息正确";
        bean.content = content;
        return JSON.toJSONString(bean);
    }

}
