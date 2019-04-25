package com.lz.base.net;

import android.content.Context;

import com.lz.base.net.api.NetUploadApi;
import com.lz.base.net.request.ResponseFile;


/**
 * 作者：刘朝
 * 时间：2018/9/10 16:14
 * 描述：
 */
public class UploadManager extends BaseManager implements NetUploadApi {
    private Context context;
    private final ResponseFile responseFile;

    public UploadManager(Context context){
        this.context = context;
        responseFile = new ResponseFile(context,this);
    }

    public void upload(){


    }

    @Override
    public void onFinish(int what, boolean isSuccess, String msg, int progress) {

    }
}
