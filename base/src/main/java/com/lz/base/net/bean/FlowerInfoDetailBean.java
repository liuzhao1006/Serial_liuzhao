package com.lz.base.net.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者： 刘朝
 * 日期： ${DATA} 15:21
 * 描述：
 */

public class FlowerInfoDetailBean implements Serializable {


    /**
     * nameStd : 紫薇
     * nameLt : Lagerstroemia indica
     * familyCn : 千屈菜科
     * genusCn : 紫薇属
     * alias : 痒痒花、痒痒树、紫金花
     * description : 紫薇树姿优美，树干光滑洁净，花色艳丽；开花时正当夏秋少花季节，花期长，故有“百日红”之称，又有“盛夏绿遮眼，此花红满堂”的赞语。
     * info : {"xgsc":"紫薇花对紫微翁，名目虽同貌不同。\n独占芳菲当夏景，不将颜色托春风。\n浔阳官舍双高树，兴善僧庭一大丛。\n何似苏州安置处，花堂栏下月明中。\n--《紫薇花》年代：唐  作者：白居易 ","jzgy":"花色鲜艳美丽，花期长，寿命长，树龄有达200年的，现热带地区已广泛栽培为庭园观赏树，有时亦作盆景。花白色的称银薇 L. indica Linn. f. alba ( Nichols.) Rehd.。紫薇的木材坚硬、耐腐，可作农具、家具、建筑等用材；树皮、叶及花为强泻剂；根和树皮煎剂可治咯血、吐血、便血。","hyyy":"好运、雄辩、女性；沉迷的爱；和平。","fbdq":"原产或栽培于吉林、辽宁、河北、山东、河南、陕西、安徽、浙江、江西、湖南、湖北、四川、贵州、云南、福建、台湾、广东、广西、海南","mcll":"","yhjs":"紫薇其喜暖湿气候，喜光，略耐阴，喜肥，尤喜深厚肥沃的砂质壤土，好生于略有湿气之地，亦耐干旱，忌涝，忌种在地下水位高的低湿地方，性喜温暖，而能抗寒，萌蘖性强。紫薇还具有较强的抗污染能力，对二氧化硫、氟化氢及氯气的抗性较强。 半阴生，喜生于肥沃湿润的土壤上，也能耐旱，不论钙质土或酸性土都生长良好。","bxtz":"落叶灌木或小乔木，高可达7米；树皮平滑，灰色或灰褐色；枝干多扭曲，小枝纤细，具4棱，略成翅状。叶互生或有时对生，纸质，椭圆形、阔矩圆形或倒卵形，长2.5-7厘米，宽1.5-4厘米，顶端短尖或钝形，有时微凹，基部阔楔形或近圆形，无毛或下面沿中脉有微柔毛，侧脉3-7对，小脉不明显；无柄或叶柄很短。花淡红色或紫色、白色，直径3-4厘米，常组成7-20厘米的顶生圆锥花序；花梗长3-15毫米，中轴及花梗均被柔毛；花萼长7-10毫米，外面平滑无棱，但鲜时萼筒有微突起短棱，两面无毛，裂片6，三角形，直立，无附属体；花瓣6，皱缩，长12-20毫米，具长爪；雄蕊36-42，外面6枚着生于花萼上，比其余的长得多；子房3-6室，无毛。蒴果椭圆状球形或阔椭圆形，长1-1.3厘米，幼时绿色至黄色，成熟时或干燥时呈紫黑色，室背开裂；种子有翅，长约8毫米。","hksj":"花期6-9月。"}
     * images : ["https://api.aiplants.cn/resource/1/紫薇/270596.jpg","https://api.aiplants.cn/resource/1/紫薇/2834343.jpg","https://api.aiplants.cn/resource/1/紫薇/2834344.jpg","https://api.aiplants.cn/resource/1/紫薇/2831099.jpg"]
     */

    private String nameStd;
    private String nameLt;
    private String familyCn;
    private String genusCn;
    private String alias;
    private String description;
    private InfoBean info;
    private List<String> images;

    @Override
    public String toString() {
        return "FlowerInfoDetailBean{" +
                "nameStd='" + nameStd + '\'' +
                ", nameLt='" + nameLt + '\'' +
                ", familyCn='" + familyCn + '\'' +
                ", genusCn='" + genusCn + '\'' +
                ", alias='" + alias + '\'' +
                ", description='" + description + '\'' +
                ", info=" + info +
                ", images=" + images +
                '}';
    }

    public String getNameStd() {
        return nameStd;
    }

    public void setNameStd(String nameStd) {
        this.nameStd = nameStd;
    }

    public String getNameLt() {
        return nameLt;
    }

    public void setNameLt(String nameLt) {
        this.nameLt = nameLt;
    }

    public String getFamilyCn() {
        return familyCn;
    }

    public void setFamilyCn(String familyCn) {
        this.familyCn = familyCn;
    }

    public String getGenusCn() {
        return genusCn;
    }

    public void setGenusCn(String genusCn) {
        this.genusCn = genusCn;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public static class InfoBean implements Serializable{
        @Override
        public String toString() {
            return "InfoBean{" +
                    "xgsc='" + xgsc + '\'' +
                    ", jzgy='" + jzgy + '\'' +
                    ", hyyy='" + hyyy + '\'' +
                    ", fbdq='" + fbdq + '\'' +
                    ", mcll='" + mcll + '\'' +
                    ", yhjs='" + yhjs + '\'' +
                    ", bxtz='" + bxtz + '\'' +
                    ", hksj='" + hksj + '\'' +
                    '}';
        }

        /**
         * xgsc : 紫薇花对紫微翁，名目虽同貌不同。
         独占芳菲当夏景，不将颜色托春风。
         浔阳官舍双高树，兴善僧庭一大丛。
         何似苏州安置处，花堂栏下月明中。
         --《紫薇花》年代：唐  作者：白居易
         * jzgy : 花色鲜艳美丽，花期长，寿命长，树龄有达200年的，现热带地区已广泛栽培为庭园观赏树，有时亦作盆景。花白色的称银薇 L. indica Linn. f. alba ( Nichols.) Rehd.。紫薇的木材坚硬、耐腐，可作农具、家具、建筑等用材；树皮、叶及花为强泻剂；根和树皮煎剂可治咯血、吐血、便血。
         * hyyy : 好运、雄辩、女性；沉迷的爱；和平。
         * fbdq : 原产或栽培于吉林、辽宁、河北、山东、河南、陕西、安徽、浙江、江西、湖南、湖北、四川、贵州、云南、福建、台湾、广东、广西、海南
         * mcll :
         * yhjs : 紫薇其喜暖湿气候，喜光，略耐阴，喜肥，尤喜深厚肥沃的砂质壤土，好生于略有湿气之地，亦耐干旱，忌涝，忌种在地下水位高的低湿地方，性喜温暖，而能抗寒，萌蘖性强。紫薇还具有较强的抗污染能力，对二氧化硫、氟化氢及氯气的抗性较强。 半阴生，喜生于肥沃湿润的土壤上，也能耐旱，不论钙质土或酸性土都生长良好。
         * bxtz : 落叶灌木或小乔木，高可达7米；树皮平滑，灰色或灰褐色；枝干多扭曲，小枝纤细，具4棱，略成翅状。叶互生或有时对生，纸质，椭圆形、阔矩圆形或倒卵形，长2.5-7厘米，宽1.5-4厘米，顶端短尖或钝形，有时微凹，基部阔楔形或近圆形，无毛或下面沿中脉有微柔毛，侧脉3-7对，小脉不明显；无柄或叶柄很短。花淡红色或紫色、白色，直径3-4厘米，常组成7-20厘米的顶生圆锥花序；花梗长3-15毫米，中轴及花梗均被柔毛；花萼长7-10毫米，外面平滑无棱，但鲜时萼筒有微突起短棱，两面无毛，裂片6，三角形，直立，无附属体；花瓣6，皱缩，长12-20毫米，具长爪；雄蕊36-42，外面6枚着生于花萼上，比其余的长得多；子房3-6室，无毛。蒴果椭圆状球形或阔椭圆形，长1-1.3厘米，幼时绿色至黄色，成熟时或干燥时呈紫黑色，室背开裂；种子有翅，长约8毫米。
         * hksj : 花期6-9月。
         */

        private String xgsc;
        private String jzgy;
        private String hyyy;
        private String fbdq;
        private String mcll;
        private String yhjs;
        private String bxtz;
        private String hksj;

        public String getXgsc() {
            return xgsc;
        }

        public void setXgsc(String xgsc) {
            this.xgsc = xgsc;
        }

        public String getJzgy() {
            return jzgy;
        }

        public void setJzgy(String jzgy) {
            this.jzgy = jzgy;
        }

        public String getHyyy() {
            return hyyy;
        }

        public void setHyyy(String hyyy) {
            this.hyyy = hyyy;
        }

        public String getFbdq() {
            return fbdq;
        }

        public void setFbdq(String fbdq) {
            this.fbdq = fbdq;
        }

        public String getMcll() {
            return mcll;
        }

        public void setMcll(String mcll) {
            this.mcll = mcll;
        }

        public String getYhjs() {
            return yhjs;
        }

        public void setYhjs(String yhjs) {
            this.yhjs = yhjs;
        }

        public String getBxtz() {
            return bxtz;
        }

        public void setBxtz(String bxtz) {
            this.bxtz = bxtz;
        }

        public String getHksj() {
            return hksj;
        }

        public void setHksj(String hksj) {
            this.hksj = hksj;
        }
    }
}
