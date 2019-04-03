package com.lz.serial.message.event;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.lz.serial.fragment.bean.HHYCFBean;
import com.lz.serial.fragment.bean.HHYFBean;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/29 下午3:07
 * 描述     : 单体充放电
 */
public class HHYCFMessageEvent {

    private HHYCFBean bean;


    public HHYCFMessageEvent(String info) {
        bean = JSON.parseObject(info,HHYCFBean.class);
    }

    public HHYCFBean getBean() {
        return bean;
    }

    @NonNull
    @Override
    public String toString() {
        return "HHYCFMessageEvent{" +
                "bean=" + bean +
                '}';
    }
}
