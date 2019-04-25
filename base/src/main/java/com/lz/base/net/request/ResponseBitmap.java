package com.lz.base.net.request;

import android.content.Context;
import android.graphics.Bitmap;

import com.lz.base.net.api.NetBitmapApi;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;

/**
 * 作者 : 刘朝,
 * on 2017/9/7,
 * GitHub : https://github.com/liuzhao1006
 */

public class ResponseBitmap implements OnResponseListener<Bitmap> {
    private Context context;
    private NetBitmapApi api;

    private String imageUrl;

    public ResponseBitmap(Context context, String url, NetBitmapApi api) {
        this.context = context;
        this.api = api;
        String[] split = url.split("/");
        imageUrl = split[split.length - 1];


    }

    @Override
    public void onStart(int what) {
        api.netStart();
    }

    @Override
    public void onSucceed(int what, Response<Bitmap> response) {
        if (response.responseCode() == 200) {
            api.netSuccessed(what, response.get());

        } else if (response.responseCode() == 404) {
            api.netFailed(what, "服务器出错, 请稍后进入,或者联系管理员......");

        } else if (response.responseCode() == 304) {
            //访问网络路径发生改变,比如response.responseCode() == 400



        } else {
            api.netFailed(what, "我也不知道怎么了,反正就是找不到您要的资源......");
        }
    }

    @Override
    public void onFailed(int what, Response<Bitmap> response) {
        api.netFailed(what, "网络错误");
    }

    @Override
    public void onFinish(int what) {
        api.netStop();
    }
}
