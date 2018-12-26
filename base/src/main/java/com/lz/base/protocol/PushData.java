package com.lz.base.protocol;

import com.lz.base.util.ConvertUtil;

/**
 * 作者      : 刘朝
 * 创建日期  : 2018/12/25 上午10:44
 * 描述     : 迪文屏数据部分
 */
public class PushData {

    private byte cmdPageId;//页面id
    private byte cmdItemId;//控件id
    private byte cmdChara;//字符集
    private byte[] bytes = null;


    /**
     * 解包用
     */
    public PushData() {

    }

    /**
     * 组包用
     *
     * @param cmdPageId 页面id
     * @param cmdItemId 控件id
     * @param cmdChara  字符集
     */
    public PushData(byte cmdPageId, byte cmdItemId, byte cmdChara) {
        this.cmdPageId = cmdPageId;
        this.cmdItemId = cmdItemId;
        this.cmdChara = cmdChara;
    }

    public byte getCmdPageId() {
        return cmdPageId;
    }

    public void setCmdPageId(byte cmdPageId) {
        this.cmdPageId = cmdPageId;
    }

    public byte getCmdItemId() {
        return cmdItemId;
    }

    public void setCmdItemId(byte cmdItemId) {
        this.cmdItemId = cmdItemId;
    }

    public byte getCmdChara() {
        return cmdChara;
    }

    public void setCmdChara(byte cmdChara) {
        this.cmdChara = cmdChara;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * 组包
     *
     * @return 返回字节数组
     */
    public byte[] pack(byte[] bytes) {
        int count = 0;//数据长度
        count += 1;//页面id占一位
        count += 1;//控件id占一位
        count += 1;//字符集占一位
        count += bytes.length;//数据长度
        byte[] b = new byte[count];
        b[0] = getCmdPageId();
        b[1] = getCmdItemId();
        b[2] = getCmdChara();
        System.arraycopy(bytes, 0, b, 3, bytes.length);
        return b;
    }

    /**
     * 解包
     * @return 返回解的对象
     */
    public PushData unPack(byte[] b){
        PushData pushData = new PushData();
        pushData.setCmdPageId(b[0]);
        pushData.setCmdItemId(b[1]);
        pushData.setCmdChara(b[2]);
        bytes = new byte[b.length - 3];
        System.arraycopy(b,3,this.bytes,0,b.length - 3);
        pushData.setBytes(bytes);
        return pushData;

    }

    @Override
    public String toString() {
        return "PushData{" +
                "cmdPageId=" + cmdPageId +
                ", cmdItemId=" + cmdItemId +
                ", cmdChara=" + cmdChara +
                ", bytes=" + ConvertUtil.bytesToHexString(bytes) +
                '}';
    }
}
