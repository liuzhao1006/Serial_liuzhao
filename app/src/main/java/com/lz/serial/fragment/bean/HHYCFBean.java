package com.lz.serial.fragment.bean;

import com.alibaba.fastjson.JSON;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/29 下午3:01
 * 描述     : 单体充放电
 */
public class HHYCFBean extends BaseBean {

    /**
     * 图标
     */
    public boolean isGreen;

    /**
     * title
     */
    public String title;
    /**
     * 当前电压
     */
    public float currentVoltage;
    /**
     * 当前轮次
     */
    public int currentRotation;
    /**
     * 当前电流
     */
    public float currentCurrent;
    /**
     * 当前工作时长
     */
    public String currentWorkingHours;
    /**
     * 当前容量
     */
    public float currentCapacity;

    public static String getHHYCFBean(){
        HHYCFBean bean = new HHYCFBean();
        int isGreens = (int)(Math.random()*10);
        bean.isGreen = isGreens > 5;
        bean.title = "HHYCF";
        bean.currentRotation = (int)(Math.random()*10);
        bean.currentWorkingHours = Math.random() + "";
        bean.currentCapacity = (float) (Math.random()*10);
        bean.currentVoltage = (float) (Math.random()*10);
        bean.currentCurrent =(float) (Math.random()*10);
        return JSON.toJSONString(bean);
    }


    public static HHYCFBean getConvert(HHYCFBean bean){
        HHYCFBean convertBean = new HHYCFBean();
        convertBean.isGreen = bean.isGreen;
        convertBean.title = bean.title;
        convertBean.currentCapacity = bean.currentCapacity ;
        convertBean.currentCurrent = bean.currentCurrent;
        convertBean.currentRotation = bean.currentRotation;
        convertBean.currentVoltage = bean.currentVoltage ;
        convertBean.currentWorkingHours = bean.currentWorkingHours ;
        return convertBean;
    }
    public static String getStatusBean(String content){

        StatusBean bean = new StatusBean();
        bean.code =15;
        bean.msg = "消息正确";
        bean.content = content;
        return JSON.toJSONString(bean);
    }


}
