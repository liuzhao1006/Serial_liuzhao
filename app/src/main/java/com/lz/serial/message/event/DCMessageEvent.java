package com.lz.serial.message.event;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.lz.serial.fragment.bean.DCBean;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/29 下午2:12
 * 描述     : DC在线监测充放电
 */
public class DCMessageEvent {

    private DCBean bean;


    public DCMessageEvent(String info) {
        bean = JSON.parseObject(info,DCBean.class);
    }

    public DCBean getBean() {
        return bean;
    }

    @NonNull
    @Override
    public String toString() {
        return "DCMessageEvent{" +
                "bean=" + bean +
                '}';
    }
}
