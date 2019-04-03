package com.lz.serial.message.event;

import com.alibaba.fastjson.JSON;
import com.lz.serial.fragment.bean.JFBean;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/28 下午4:01
 * 描述     :
 */
public class JFMessageEvent {

    private JFBean jfBean;

    public JFMessageEvent(String info) {
       jfBean = JSON.parseObject(info,JFBean.class);
    }

    public JFBean getJfBean() {
        return jfBean;
    }

    @Override
    public String toString() {
        return "JFMessageEvent{" +
                "jfBean=" + jfBean +
                '}';
    }
}
