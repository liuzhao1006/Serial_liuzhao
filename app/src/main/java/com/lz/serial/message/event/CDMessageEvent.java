package com.lz.serial.message.event;

import com.alibaba.fastjson.JSON;
import com.lz.serial.fragment.bean.CDBean;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/23 上午11:05
 * 描述     : 在线监测消息处理类
 */
public class CDMessageEvent {

    private CDBean message;

    public CDMessageEvent(String info){
        message = JSON.parseObject(info, CDBean.class);
    }

    public CDBean getMessage() {
        return message;
    }

    public void setMessage(CDBean message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CDMessageEvent{" +
                "message='" + message + '\'' +
                '}';
    }
}
