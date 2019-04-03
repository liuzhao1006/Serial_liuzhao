package com.lz.serial.fragment.bean;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/23 下午12:16
 * 描述     :
 */
public class CDBean extends BaseBean {

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
    public float tvChargingCapacityCD;
    /**
     * 充电时长
     */
    public String tvChargingtimeCD;
    /**
     * 当前整组电压
     */
    public float tvCurrentVoltageSetCD;
    /**
     * 当前充电电流
     */
    public float tvCurrentChargingCurrentCD;
    /**
     * 当前充入容量
     */
    public float tvCurrentFillingCapacityCD;
    /**
     * 当前充电时长
     */
    public String tvCurrentChargingTimeCD;
    /**
     * 警告
     */
    public String tvWaringCD;

    public static CDBean getConvert(CDBean bean) {
        CDBean convertCDBean = new CDBean();
        convertCDBean.isGreen = bean.isGreen;
        convertCDBean.title = bean.title;
        convertCDBean.tvChargingCapacityCD = bean.tvChargingCapacityCD ;
        convertCDBean.tvChargingtimeCD = bean.tvChargingtimeCD;
        convertCDBean.tvCurrentVoltageSetCD = bean.tvCurrentVoltageSetCD ;
        convertCDBean.tvCurrentChargingCurrentCD = bean.tvCurrentChargingCurrentCD ;
        convertCDBean.tvCurrentFillingCapacityCD = bean.tvCurrentFillingCapacityCD ;
        convertCDBean.tvCurrentChargingTimeCD = bean.tvCurrentChargingTimeCD;
        convertCDBean.tvWaringCD = bean.tvWaringCD;
        return convertCDBean;
    }

    public static String getCD() {
        CDBean bean = new CDBean();
        int isGreens = (int) (Math.random() * 10);
        bean.isGreen = isGreens > 5;
        bean.title = "CD";
        bean.tvChargingCapacityCD = (float) (Math.random() * 10);
        bean.tvChargingtimeCD = Math.random() + "";
        bean.tvCurrentChargingCurrentCD = (float) (Math.random() * 10);
        bean.tvCurrentChargingTimeCD = Math.random() + "";
        bean.tvCurrentFillingCapacityCD = (float) (Math.random() * 10);
        bean.tvCurrentVoltageSetCD = (float) (Math.random() * 10);
        int random = (int) (Math.random() * 10);
        if (random > 5) {
            bean.tvWaringCD = "正常";
        } else {
            bean.tvWaringCD = "异常";
        }
        return JSON.toJSONString(bean);
    }

    public static String getStatusBean(String content) {

        StatusBean bean = new StatusBean();
        bean.code = 4;
        bean.msg = "消息正确";
        bean.content = content;
        return JSON.toJSONString(bean);
    }

    @NonNull
    @Override
    public String toString() {
        return "CDBean{" +
                "isGreen=" + isGreen +
                ", title='" + title + '\'' +
                ", tvChargingCapacityCD='" + tvChargingCapacityCD + '\'' +
                ", tvChargingtimeCD='" + tvChargingtimeCD + '\'' +
                ", tvCurrentVoltageSetCD='" + tvCurrentVoltageSetCD + '\'' +
                ", tvCurrentChargingCurrentCD='" + tvCurrentChargingCurrentCD + '\'' +
                ", tvCurrentFillingCapacityCD='" + tvCurrentFillingCapacityCD + '\'' +
                ", tvCurrentChargingTimeCD='" + tvCurrentChargingTimeCD + '\'' +
                ", tvWaringCD='" + tvWaringCD + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
