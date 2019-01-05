package com.lz.serial.message;

import android.content.Context;

import com.lz.base.log.LogUtils;
import com.lz.base.observe.Display;
import com.lz.base.observe.Observer;
import com.lz.base.protocol.LzParser;
import com.lz.serial.utils.Util;

/**
 * 作者      : 刘朝
 * 创建日期  : 2018/12/22 下午11:43
 * 描述     :
 */
public class Test implements Observer, Display {

    private WriteMessage writeMessage;

    private LzParser message;

    public Test() {

        init();
    }

    public void removeCallback(Observer o){
        writeMessage.removeObserver(o);
    }

    public void setMessage(LzParser msg){

        writeMessage.setMessage(msg);
    }


    private void init(){
        writeMessage = new WriteMessage();
        writeMessage.registerObserver(this);
        LogUtils.i("init " + writeMessage);
    }

    @Override
    public void display() {
        Util.runOnUiThread(() -> Util.showToast("display " ));

    }

    @Override
    public void update(LzParser msg) {
        this.message = msg;

        display();
    }
}
