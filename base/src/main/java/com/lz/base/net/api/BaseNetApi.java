package com.lz.base.net.api;

/**
 * 作者 : 刘朝,
 * on 2017/9/6,
 * GitHub : https://github.com/liuzhao1006
 */

public interface BaseNetApi {
    /**
     * 访问开始,显示Dialog
     */
    void netStart();

    /**
     * 访问网络结束,退出Dialog
     */
    void netStop();

    /**
     * 获取数据成功
     *
     * @param what
     * @param data
     */
    void netSuccessed(int what, String data);

    /**
     * 获取数据失败
     *
     * @param what
     * @param message
     */
    void netFailed(int what, String message);
}
