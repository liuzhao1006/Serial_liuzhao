package com.lz.serial.message;

import com.lz.base.observe.Observer;
import com.lz.base.observe.PusherMessage;
import com.lz.base.observe.Subject;

import java.util.ArrayList;
import java.util.List;


/**
 * 作者      : 刘朝
 * 创建日期  : 2018/12/22 下午11:35
 * 描述     : 当状态发生变化的时候通知所有订阅
 */
public class WriteMessage implements Subject {

    private List<Observer> observers;
    private PusherMessage message;

    public WriteMessage() {
        observers = new ArrayList<>();
    }

    public PusherMessage getMessage() {
        return message;
    }

    public void setMessage(PusherMessage message) {
        this.message = message;
        notifyObservers();
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

    @Override
    public String toString() {
        return "WriteMessage{" +
                "observers=" + observers +
                ", message=" + message +
                '}';
    }
}
