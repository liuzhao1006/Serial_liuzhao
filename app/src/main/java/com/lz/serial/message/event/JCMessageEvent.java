package com.lz.serial.message.event;

import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.lz.serial.fragment.bean.JcBean;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/28 下午2:22
 * 描述     : JC模块的消息接收体
 */
public class JCMessageEvent {

    private JcBean jcBean;


    public JCMessageEvent(String info) {
        jcBean = JSON.parseObject(info,JcBean.class);
    }

    public JcBean getJcBean() {
        return jcBean;
    }

    @NonNull
    @Override
    public String toString() {
        return "JCMessageEvent{" +
                "jcBean=" + jcBean +
                '}';
    }
}
