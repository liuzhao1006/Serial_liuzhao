package com.lz.base.observe;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者      : 刘朝
 * 创建日期  : 2018/12/31 下午8:31
 * 描述     :
 */
public class LzObserve implements Subject {

    private List<Observer> observers;
    private PusherMessage message;

    public PusherMessage getMessage() {
        return message;
    }

    public void setMessage(PusherMessage message) {
        this.message = message;
    }

    public LzObserve() {
        observers = new ArrayList<>();
    }


    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        int i = observers.indexOf(o);
        if (i >= 0) {
            observers.remove(o);
        }
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(message);
        }
    }
}
