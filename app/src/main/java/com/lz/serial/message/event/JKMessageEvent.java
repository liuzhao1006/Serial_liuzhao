package com.lz.serial.message.event;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.lz.serial.fragment.bean.JkBean;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/27 下午2:39
 * 描述     : JK模块数据解析
 */
public class JKMessageEvent {

    private JkBean bean;

    public JKMessageEvent(String info){

        bean = JSON.parseObject(info, JkBean.class);
    }

    public JkBean getBean() {
        return bean;
    }

    public void setBean(JkBean bean) {
        this.bean = bean;
    }

    @NonNull
    @Override
    public String toString() {
        return "JKMessageEvent{" +
                "bean=" + bean +
                '}';
    }
}
