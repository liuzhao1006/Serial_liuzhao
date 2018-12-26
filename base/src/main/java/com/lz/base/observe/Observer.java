package com.lz.base.observe;

/**
 * 作者      : 刘朝
 * 创建日期  : 2018/12/22 下午3:58
 * 描述     : 观察者
 */
public interface Observer {
    void update(PusherMessage msg);
}
