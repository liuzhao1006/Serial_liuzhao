package com.lz.serial.message;

import com.lz.base.observe.Observer;
import com.lz.base.protocol.LzParser;


/**
 * 作者      : 刘朝
 * 创建日期  : 2019/1/20 下午2:41
 * 描述     :
 */
public class LzTestMessage implements Observer {
    @Override
    public void update(LzParser msg) {
        if(iMessage != null){
            iMessage.onMessage(msg);
        }
    }

    public interface ITestMessage{
        void onMessage(LzParser msg);
    }
    private ITestMessage iMessage;

    public void setIMessage(ITestMessage iMessage){
        this.iMessage  = iMessage;
    }

}
