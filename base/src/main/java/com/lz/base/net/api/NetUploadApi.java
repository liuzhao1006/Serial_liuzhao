package com.lz.base.net.api;

/**
 * 作者：刘朝
 * 时间：2018/9/10 16:15
 * 描述：
 */
public interface NetUploadApi {

    /**
     * 上传
     * @param what 标记
     * @param isSuccess 是否上传成功
     * @param msg 消息
     * @param progress 上传进度
     */
    void onFinish(int what, boolean isSuccess, String msg, int progress);
}
