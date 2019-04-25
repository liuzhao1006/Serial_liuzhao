package com.lz.base.net;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.lz.base.net.api.BaseNetApi;
import com.lz.base.net.api.NetJsonApi;
import com.lz.base.net.bean.FlowerInfoBean;
import com.lz.base.net.bean.FlowerInfoDetailBean;
import com.lz.base.net.request.ResponseData;
import com.yanzhenjie.nohttp.rest.Request;

import java.util.List;


/**
 * 作者： 刘朝
 * 日期： ${DATA} 10:51
 * 描述：
 */

public class FlowerManager extends BaseManager implements NetJsonApi {
    private Context context;
    private BaseNetApi api;
    private StringBuffer sb;
    private ResponseData responseData;
//    private static final String APP_CODE = "629c3034a88c400ab56e80c0271da1c1";//天问
    private static final String APP_CODE = "c0955d602d984112bc863175d7eaf414";//尚亿达

    public static final int FLOWER_INFO = 0x001;//根据图片获取对应的植物种类接口
    public static final int FLOWER_INFO_DETIAL = 0x002;//根据植物中百度百科对应的编号获取该植物的详情


    private static final String ACCESS_KEY = "LTAIuiJgeQBJmWa6";//阿里云key
    private static final String ACCESS_KEY_SECRET = "nJ1UOj3qFEugZrLtYbITsIYPO0k3tK";//阿里云服务号
    public static final int FACE_RECOGNITION = 0x003;//人脸识别
    public static final int CAR_PHOTO = 0X004;

    private static FlowerManager mInstance;
    public synchronized static FlowerManager getmInstance(){
        if(mInstance == null){
            mInstance = new FlowerManager();
        }

        return mInstance;
    }

    private FlowerManager(){
        sb = new StringBuffer();
    }

    public Context getContext() {
        return context;
    }

    public FlowerManager setContext(Context context) {
        this.context = context;
        if(responseData == null){
            responseData = new ResponseData(context, this);
        }
        return this;
    }

    public BaseNetApi getApi() {
        return api;
    }

    public FlowerManager setApi(BaseNetApi api) {
        this.api = api;
        return this;
    }



//    public void

    /**
     * 拍照识花接口
     *
     * @param base64
     */
    public void getFlowerInfo(String base64) {
        Request<String> request = doPost(context, "host");
        request.addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "APPCODE " +APP_CODE)
                .add("img_base64", base64);
        request(FLOWER_INFO, request, responseData);
    }


    /**
     * 根据InfoCode的值获取详细信息.
     *
     * @param infoCode 植物百科信息获取接口需要的代号
     */
    public void getFlowerDetial(String infoCode) {
        Request<String> request = doPost(context, "baidu_encyclopedia");
        request.addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "APPCODE " +APP_CODE)
                .add("code", infoCode);
        request(FLOWER_INFO_DETIAL, request, responseData);
    }
    public void getCarPhtot() {
        Request<String> request = doGet("http://192.168.0.1:8080/?action=snapshot");
//        request.addHeader("Content-Type", "application/x-www-form-urlencoded")
                ;
        request(FLOWER_INFO_DETIAL, request, responseData);
    }


    @Override
    public void netStart() {

    }

    @Override
    public void netStop() {

    }

    @Override
    public void netSuccessed(int what, String data) {
        switch (what) {
            case FLOWER_INFO:
                //获取植物信息
                if (flowerInfoData != null) {
                    flowerInfoData.onFlowerInfo(what, JSON.parseArray(data, FlowerInfoBean.class));
                    Log.i("photo",JSON.parseArray(data, FlowerInfoBean.class).toString());
                }
                break;
            case FLOWER_INFO_DETIAL:
                if (flowerInfoDetialDatas != null) {
                    flowerInfoDetialDatas.onFlowerInfoDetial(what, JSON.parseObject(data, FlowerInfoDetailBean.class));
                    Log.i("photo",JSON.parseObject(data, FlowerInfoDetailBean.class).toString());
                }
                break;
            case CAR_PHOTO:
                break;
        }
    }

    @Override
    public void netFailed(int what, String message) {
        api.netFailed(what,message);
    }

    /**
     * 根据图片获取植物种类
     */
    public interface FlowerInfoData {
        void onFlowerInfo(int what, List<FlowerInfoBean> mList);
    }

    private FlowerInfoData flowerInfoData;

    public void setOnFlowerInfoData(FlowerInfoData flowerInfoData) {
        this.flowerInfoData = flowerInfoData;
    }

    public interface FlowerInfoDetialDatas {
        void onFlowerInfoDetial(int what, FlowerInfoDetailBean bean);
    }

    private FlowerInfoDetialDatas flowerInfoDetialDatas;

    public void setOnFlowerInfoDetialData(FlowerInfoDetialDatas flowerInfoDetialDatas) {
        this.flowerInfoDetialDatas = flowerInfoDetialDatas;
    }


    public interface CarPhoto{
        void onCarPhoto();
    }

    private CarPhoto carPhoto;
    public void setOnCarPhoto(CarPhoto carPhoto){


        this.carPhoto = carPhoto;
    }



}
