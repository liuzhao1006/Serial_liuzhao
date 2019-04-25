package com.lz.base.net.request;


import android.content.Context;

import com.lz.base.net.api.NetUploadApi;
import com.yanzhenjie.nohttp.OnUploadListener;

/**
 * 作者：刘朝
 * 时间：2018/9/10 13:57
 * 描述：
 */
public class ResponseFile implements OnUploadListener {

    private Context context;
    private NetUploadApi api;
    public ResponseFile(Context context, NetUploadApi api){
        this.context = context;
        this.api = api;
    }

    @Override
    public void onStart(int what) {

    }

    @Override
    public void onCancel(int what) {

    }

    @Override
    public void onProgress(int what, int progress) {

    }

    @Override
    public void onFinish(int what) {

    }

    @Override
    public void onError(int what, Exception exception) {

    }
}
