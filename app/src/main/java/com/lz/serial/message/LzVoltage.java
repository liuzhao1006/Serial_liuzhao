package com.lz.serial.message;

import com.lz.base.observe.Observer;
import com.lz.base.protocol.LzParser;


/**
 * 作者      : 刘朝
 * 创建日期  : 2019/1/2 下午5:08
 * 描述     :
 */
public class LzVoltage implements Observer {
    @Override
    public void update(LzParser msg) {
        if(iVoltage != null){
            iVoltage.onVoltage(msg);
        }
    }

    public interface IVoltage{
        void onVoltage(LzParser msg);
    }
    private IVoltage iVoltage;

    public void setIVoltage(IVoltage iVoltage){
        this.iVoltage  = iVoltage;
    }
}
