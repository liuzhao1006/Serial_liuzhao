package com.lz.serial.message.event;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationQualityReport;
import com.lz.serial.utils.Util;

/**
 * 作者      : 刘朝
 * 创建日期  : 2019/3/28 上午10:53
 * 描述     :
 */
public class LocationMessageEvent {

    private AMapLocation location;
    private String locationMsg;
    private String simpleMsg;

    public LocationMessageEvent(AMapLocation location) {
        this.location = location;
        if (location != null) {
            StringBuilder sb = new StringBuilder();
            StringBuilder sbSimple = new StringBuilder();
            //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
            if (location.getErrorCode() == 0) {
                sb.append("定位成功" + "\n");
                sb.append("定位类型: ").append(location.getLocationType()).append("\n");
                sb.append("经    度    : ").append(location.getLongitude()).append("\n");
                sb.append("纬    度    : ").append(location.getLatitude()).append("\n");
                sb.append("精    度    : ").append(location.getAccuracy()).append("米").append("\n");
                sb.append("提供者    : ").append(location.getProvider()).append("\n");

                sb.append("速    度    : ").append(location.getSpeed()).append("米/秒").append("\n");
                sb.append("角    度    : ").append(location.getBearing()).append("\n");
                // 获取当前提供定位服务的卫星个数
                sb.append("星    数    : ").append(location.getSatellites()).append("\n");
                sb.append("国    家    : ").append(location.getCountry()).append("\n");
                sb.append("省            : ").append(location.getProvince()).append("\n");
                sb.append("市            : ").append(location.getCity()).append("\n");
                sb.append("城市编码 : ").append(location.getCityCode()).append("\n");
                sb.append("区            : ").append(location.getDistrict()).append("\n");
                sb.append("区域 码   : ").append(location.getAdCode()).append("\n");
                sb.append("地    址    : ").append(location.getAddress()).append("\n");
                sb.append("地    址    : ").append(location.getDescription()).append("\n");
                sb.append("兴趣点    : ").append(location.getPoiName()).append("\n");
                //定位完成的时间
                sb.append("定位时间: ").append(Util.formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss")).append("\n");
                sbSimple.append(location.getAddress());
            } else {
                //定位失败
                sb.append("定位失败" + "\n");
                sb.append("错误码:").append(location.getErrorCode()).append("\n");
                sb.append("错误信息:").append(location.getErrorInfo()).append("\n");
                sb.append("错误描述:").append(location.getLocationDetail()).append("\n");

                sbSimple.append("定位失败" + "\n");
                sbSimple.append("错误描述:").append(location.getLocationDetail()).append("\n");
            }
            sb.append("***定位质量报告***").append("\n");
            sb.append("* WIFI开关：").append(location.getLocationQualityReport().isWifiAble() ? "开启" : "关闭").append("\n");
            sb.append("* GPS状态：").append(getGPSStatusString(location.getLocationQualityReport().getGPSStatus())).append("\n");
            sb.append("* GPS星数：").append(location.getLocationQualityReport().getGPSSatellites()).append("\n");
            sb.append("****************").append("\n");
            //定位之后的回调时间
            sb.append("回调时间: ").append(Util.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss")).append("\n");

            //解析定位结果，
            locationMsg = sb.toString();
            simpleMsg = sbSimple.toString();
        }else {
            locationMsg = "定位失败，loc is null";
            simpleMsg = "定位失败，loc is null";
        }
    }

    /**
     * 获取GPS状态的字符串
     *
     * @param statusCode GPS状态码
     * @return
     */
    private String getGPSStatusString(int statusCode) {
        String str = "";
        switch (statusCode) {
            case AMapLocationQualityReport.GPS_STATUS_OK:
                str = "GPS状态正常";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
                str = "手机中没有GPS Provider，无法进行GPS定位";
                break;
            case AMapLocationQualityReport.GPS_STATUS_OFF:
                str = "GPS关闭，建议开启GPS，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_MODE_SAVING:
                str = "选择的定位模式中不包含GPS定位，建议选择包含GPS定位的模式，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
                str = "没有GPS定位权限，建议开启gps定位权限";
                break;
        }
        return str;
    }

    public String getLocationMsg() {
        return locationMsg;
    }

    public String getSimpleMsg() {
        return simpleMsg;
    }

    @Override
    public String toString() {
        return "LocationMessageEvent{" +
                "location=" + location +
                '}';
    }
}
