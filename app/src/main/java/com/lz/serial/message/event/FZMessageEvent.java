package com.lz.serial.message.event;

import com.alibaba.fastjson.JSON;
import com.lz.serial.fragment.bean.FZBean;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/28 下午5:27
 * 描述     : 放电负载测试中数据
 */
public class FZMessageEvent {

    private FZBean bean;

    public FZMessageEvent(String info) {
        bean = JSON.parseObject(info,FZBean.class);
    }

    public FZBean getBean() {
        return bean;
    }

    @Override
    public String toString() {
        return "FZMessageEvent{" +
                "bean=" + bean +
                '}';
    }
}
