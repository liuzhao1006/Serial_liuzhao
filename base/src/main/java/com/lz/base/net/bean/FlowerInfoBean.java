package com.lz.base.net.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者： 刘朝
 * 日期： ${DATA} 10:42
 * 描述：
 */

public class FlowerInfoBean implements Serializable {


    @Override
    public String toString() {
        return "FlowerInfoBean{" +
                "AliasName='" + AliasName + '\'' +
                ", Genus='" + Genus + '\'' +
                ", ImageUrl='" + ImageUrl + '\'' +
                ", Family='" + Family + '\'' +
                ", LatinName='" + LatinName + '\'' +
                ", Score='" + Score + '\'' +
                ", InfoCode='" + InfoCode + '\'' +
                ", Name='" + Name + '\'' +
                ", AliasList=" + AliasList +
                '}';
    }

    /**
     * AliasList : ["节果决明","粉花山扁豆"]
     * AliasName : 节果决明、粉花山扁豆
     * Genus : 决明属
     * ImageUrl : https://static.nongbangzhu.cn/samples_v4/p11k/p11k-watermark/%E8%8A%82%E8%8D%9A%E5%86%B3%E6%98%8E/11eced3421591044.jpg
     * Family : 豆科
     * LatinName : Cassia javanica
     * Score : 34.41
     * InfoCode : hqlOszMB9gBBy779
     * Name : 爪哇决明
     */

    private String AliasName;
    private String Genus;
    private String ImageUrl;
    private String Family;
    private String LatinName;
    private String Score;
    private String InfoCode;
    private String Name;
    private List<String> AliasList;

    public String getAliasName() {
        return AliasName;
    }

    public void setAliasName(String AliasName) {
        this.AliasName = AliasName;
    }

    public String getGenus() {
        return Genus;
    }

    public void setGenus(String Genus) {
        this.Genus = Genus;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }

    public String getFamily() {
        return Family;
    }

    public void setFamily(String Family) {
        this.Family = Family;
    }

    public String getLatinName() {
        return LatinName;
    }

    public void setLatinName(String LatinName) {
        this.LatinName = LatinName;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String Score) {
        this.Score = Score;
    }

    public String getInfoCode() {
        return InfoCode;
    }

    public void setInfoCode(String InfoCode) {
        this.InfoCode = InfoCode;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public List<String> getAliasList() {
        return AliasList;
    }

    public void setAliasList(List<String> AliasList) {
        this.AliasList = AliasList;
    }
}
