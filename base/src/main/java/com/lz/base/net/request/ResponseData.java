package com.lz.base.net.request;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lz.base.net.FlowerManager;
import com.lz.base.net.api.NetJsonApi;
import com.lz.base.net.result.LzResult;
import com.lz.base.net.result.LzResultSL;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;

/**
 * 作者 : 刘朝,
 * on 2017/9/6,
 * GitHub : https://github.com/liuzhao1006
 */

public class ResponseData implements OnResponseListener<String> {

    private Context context;
    private NetJsonApi api;
    private static final int PLANE_INFO_LOCATION = 0x0100;//飞机位置信息。
    private static final int LOG_UPLOAD_FILES = 0x003;//日志上传


    public ResponseData(Context context, NetJsonApi api) {
        this.context = context;
        this.api = api;
    }

    @Override
    public void onStart(int what) {
        api.netStart();
    }

    @Override
    public void onSucceed(int what, Response<String> response) {

        if (response.responseCode() == 200) {

            if (what == FlowerManager.FLOWER_INFO) {
                LzResultSL result = JSON.parseObject(JSONObject.parseObject(response.get()).toJSONString(), LzResultSL.class);
                if (result.Status.equals("1001")) {
                    api.netFailed(what, "提交请求内容中未包含img_base64参数，无法获取图片信息");
                    return;
                } else if (result.Status.equals(1002)) {
                    api.netFailed(what, "Failed to recognize the request image.");
                    return;
                } else if (result.Status.equals(1003)) {
                    api.netFailed(what, "植物图片识别失败");
                    return;
                }
                api.netSuccessed(what, result.Result);

            } else if (what == FlowerManager.FLOWER_INFO_DETIAL) {
                LzResult result = JSON.parseObject(JSONObject.parseObject(response.get()).toJSONString(), LzResult.class);
                if (result.status.equals("0")) {
                    api.netSuccessed(what, result.result);

                }
            }else if(what == FlowerManager.CAR_PHOTO){
                Log.i("car", response.get());
            }

        } else {
            if ((what == FlowerManager.FLOWER_INFO_DETIAL || what == FlowerManager.FLOWER_INFO)) {
                if (response.responseCode() == 403) {
                    api.netFailed(what, "您的账户已经欠费,请联系管理员续费后再使用.");
                } else {
                    api.netFailed(what, "网络错误,请稍后再试");
                }

            } else {
                api.netFailed(what, "未知错误。。。。。。");
            }

        }
    }

    @Override
    public void onFailed(int what, Response<String> response) {
        api.netFailed(what, response.get());
    }

    @Override
    public void onFinish(int what) {
        api.netStop();
    }
}
