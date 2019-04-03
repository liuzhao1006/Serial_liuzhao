package com.lz.serial.net;

import com.alibaba.fastjson.JSON;
import com.lz.serial.fragment.bean.StatusBean;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/19 上午6:57
 * 描述     :
 */
public class ConnectorInfo {
    private final String value;

    private final MessageType messageType;

    ConnectorInfo(String msg) {
        if (msg.contains("code") || msg.contains("content") || msg.contains("msg")) {
            StatusBean bean = JSON.parseObject(msg, StatusBean.class);
            switch (bean.code) {
                case 0:
                    messageType = MessageType.ERROR;
                    value = bean.msg;
                    break;
                case 1:
                    messageType = MessageType.JC;
                    value = bean.content;
                    break;
                case 2:
                    messageType = MessageType.JK;
                    value = bean.content;
                    break;
                case 3:
                    messageType = MessageType.CF;
                    value = bean.content;
                    break;
                case 4:
                    messageType = MessageType.CD;
                    value = bean.content;
                    break;
                case 5:
                    messageType = MessageType.FZ;
                    value = bean.content;
                    break;
                case 6:
                    messageType = MessageType.JCF;
                    value = bean.content;
                    break;
                case 7:
                    messageType = MessageType.JF;
                    value = bean.content;
                    break;
                case 8:
                    messageType = MessageType.DJCF;
                    value = bean.content;
                    break;
                case 9:
                    messageType = MessageType.JC;
                    value = bean.content;
                    break;
                case 10:
                    messageType = MessageType.JC;
                    value = bean.content;
                    break;
                case 11:
                    messageType = MessageType.DJCF;
                    value = bean.content;
                    break;
                case 12:
                    messageType = MessageType.RLPG;
                    value = bean.content;
                    break;
                case 13:
                    messageType = MessageType.HHYC;
                    value = bean.content;
                    break;
                case 14:
                    messageType = MessageType.HHYF;
                    value = bean.content;
                    break;
                case 15:
                    messageType = MessageType.HHYCF;
                    value = bean.content;
                    break;

                default:
                    messageType = MessageType.ERROR;
                    value = bean.msg;
                    break;
            }

        } else {
            messageType = MessageType.ERROR;
            value = "不合理数据";
        }

    }

    public MessageType getMessgeType() {
        return messageType;
    }


    public String getValue() {
        return value;
    }


    public enum MessageType {
        ERROR, JC, JK, CF, CD, FZ, JCF, JF, JNF, JNCF, RJCF, DJCF, RLPG, HHYC,HHYF,HHYCF,
    }
}
