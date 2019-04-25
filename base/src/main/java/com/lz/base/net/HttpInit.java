package com.lz.base.net;

import android.content.Context;

import com.yanzhenjie.nohttp.Logger;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;
import com.yanzhenjie.nohttp.cache.DBCacheStore;
import com.yanzhenjie.nohttp.cache.DiskCacheStore;
import com.yanzhenjie.nohttp.cookie.DBCookieStore;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/4/23 下午3:30
 * 描述     : 初始化http网络配置
 */
public class HttpInit {

    public static void init(Context context){
        Logger.setDebug(true);
        Logger.setTag("LzCloud:");
        NoHttp.initialize(context, new NoHttp.Config()
                .setConnectTimeout(30 * 1000)
                .setReadTimeout(30 * 1000)
                .setCacheStore(
                        new DBCacheStore(context)
                                .setEnable(true)
                )
                .setCacheStore(
                        new DiskCacheStore(context)
                )
                .setCookieStore(
                        new DBCookieStore(context)
                                .setEnable(false)
                )
                .setNetworkExecutor(new OkHttpNetworkExecutor())
        );
    }
}
