package com.lz.serial.message.event;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.lz.serial.fragment.bean.CFBean;
import com.lz.serial.fragment.bean.JCFBean;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/29 上午9:45
 * 描述     :
 */
public class CFMessageEvent {
    private CFBean bean;


    public CFMessageEvent(String info) {
        bean = JSON.parseObject(info,CFBean.class);
    }

    public CFBean getBean() {
        return bean;
    }

    @NonNull
    @Override
    public String toString() {
        return "JCMessageEvent{" +
                "bean=" + bean +
                '}';
    }
}
