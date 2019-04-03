package com.lz.serial.message.event;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.lz.serial.fragment.bean.HHYCBean;
import com.lz.serial.fragment.bean.JCFBean;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/29 下午2:40
 * 描述     : 单体充电
 */
public class HHYCMessageEvent {
    private HHYCBean bean;


    public HHYCMessageEvent(String info) {
        bean = JSON.parseObject(info,HHYCBean.class);
    }

    public HHYCBean getBean() {
        return bean;
    }

    @NonNull
    @Override
    public String toString() {
        return "HHYCMessageEvent{" +
                "bean=" + bean +
                '}';
    }
}
