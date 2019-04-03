package com.lz.serial.fragment.bean;

import com.alibaba.fastjson.JSON;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/29 上午10:37
 * 描述     :
 */
public class RLPGBean extends BaseBean {

    /**
     * 图标
     */
    public boolean isGreen;

    /**
     * title
     */
    public String title;
    /**
     * 机房名称
     */
    public String pcRoom;
    /**
     * 整组类型
     */
    public float wholeSetType;
    /**
     * 单体类型
     */
    public float monomerType;
    /**
     * 电池组数
     */
    public int batteryPackNumber;
    /**
     * 每组节数
     */
    public int numberNodesPerGroup;
    /**
     * 标称容量
     */

    public float nominalCapacity;
    /**
     * 单体排序
     */
    public String monomerSorting;
    /**
     * 测试时间
     */
    public String testTime;
    /**
     * 参考内阻
     */
    public float referenceInternalResistance;
    /**
     * 测试时间倒计时
     */
    public String countDown;

    public static RLPGBean getConvert(RLPGBean bean){
        RLPGBean  convertBean = new RLPGBean();
        convertBean.isGreen = bean.isGreen;
        convertBean.title = bean.title;
        convertBean.countDown = bean.countDown;
        convertBean.batteryPackNumber = bean.batteryPackNumber;
        convertBean.monomerSorting = bean.monomerSorting;
        convertBean.monomerType = bean.monomerType;
        convertBean.nominalCapacity = bean.nominalCapacity ;
        convertBean.numberNodesPerGroup = bean.numberNodesPerGroup;
        convertBean.pcRoom = bean.pcRoom;
        convertBean.referenceInternalResistance = bean.referenceInternalResistance;
        convertBean.testTime = bean.testTime;
        convertBean.wholeSetType = bean.wholeSetType;
        return convertBean;
    }

    public static String getRLPGBean(){
        RLPGBean bean = new RLPGBean();
        int isGreens = (int)(Math.random()*10);
        bean.isGreen = isGreens > 5;
        bean.title = "容量评估";
        bean.pcRoom = (int)(Math.random()*10) + "号";
        bean.wholeSetType = (float) (Math.random()*10);
        bean.monomerType = (float) (Math.random()*10);
        bean.batteryPackNumber = (int) (Math.random()*10);
        bean.numberNodesPerGroup = (int) (Math.random()*10);
        bean.nominalCapacity = (float) (Math.random()*10);
        bean.monomerSorting = Math.random() + "";
        bean.testTime = Math.random() + "";
        bean.referenceInternalResistance = (float) (Math.random()*10);
        bean.countDown = (int)(Math.random()*10) + ":" +(int)(Math.random()*10);
        return JSON.toJSONString(bean);
    }

    public static String getStatusBean(String content){

        StatusBean bean = new StatusBean();
        bean.code = 12;
        bean.msg = "消息正确";
        bean.content = content;
        return JSON.toJSONString(bean);
    }
}
