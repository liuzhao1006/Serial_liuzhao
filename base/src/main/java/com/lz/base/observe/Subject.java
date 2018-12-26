package com.lz.base.observe;

/**
 * 作者      : 刘朝
 * 创建日期  : 2018/12/22 下午3:32
 * 描述     : 事件
 */
public interface Subject {

    void registerObserver(Observer o);

    void removeObserver(Observer o);

    void notifyObservers();
}
