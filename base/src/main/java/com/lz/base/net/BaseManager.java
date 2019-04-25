package com.lz.base.net;

import android.content.Context;
import android.util.Log;

import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * 作者 : 刘朝,
 * on 2017/9/6,
 * GitHub : https://github.com/liuzhao1006
 */

public class BaseManager {
    private Object cancelObject = new Object();


    /**
     * 请求数据
     *
     * @param what
     * @param request
     * @param listener
     * @param <T>
     */
    public <T> void request(int what, Request<T> request, OnResponseListener<T> listener) {
        request.setCancelSign(cancelObject);// 这里设置一个sign给这个请求。
        LzCallService.getInstance().add(what, request, listener);
    }


    /**
     * 创建Post请求
     *
     * @param context 上下文
     * @param url     接口路径
     * @return
     */
    public Request<String> doPost(Context context, String url) {
        return NoHttp.createStringRequest(getProperties(context, url), RequestMethod.POST);
    }

    public Request<String> doGet(Context context, String url, String data){
        return NoHttp.createStringRequest(getProperties(context, url)+data, RequestMethod.GET);
    }

    public Request<String> doGet(String url){
        return NoHttp.createStringRequest(url, RequestMethod.GET);
    }


    /**
     * 销毁队列
     */
    public void onDestory() {
        LzCallService.getInstance().cancelBySign(cancelObject); // 在组件销毁的时候调用队列的按照sign取消的方法即可取消。
    }

    /**
     * 获取网络配置
     *
     * @param keyName
     * @param context
     * @return
     */
    public String getProperties(Context context, String keyName) {
        Properties props = new Properties();
        try {
            props.load(context.getAssets().open("config.properties"));
//            props.load(context.openFileInput("config.properties"));

            return props.getProperty(keyName);
        } catch (FileNotFoundException e) {
            Log.e("BaseManager::", "config.properties Not Found Exception", e);
            return "配置文件不存在";
        } catch (IOException e) {
            Log.e("BaseManager::", "config.properties IO Exception", e);
            return "读取失败";
        }

    }

}