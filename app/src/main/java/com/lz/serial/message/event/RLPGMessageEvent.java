package com.lz.serial.message.event;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.lz.serial.fragment.bean.JCFBean;
import com.lz.serial.fragment.bean.RLPGBean;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/28 下午2:22
 * 描述     : RLPG模块的消息接收体
 */
public class RLPGMessageEvent {

    private RLPGBean bean;


    public RLPGMessageEvent(String info) {
        bean = JSON.parseObject(info,RLPGBean.class);
    }

    public RLPGBean getBean() {
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
