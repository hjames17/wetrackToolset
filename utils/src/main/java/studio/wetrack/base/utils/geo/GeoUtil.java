package studio.wetrack.base.utils.geo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.wetrack.base.utils.HttpUtil;
import studio.wetrack.base.utils.jackson.Jackson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

/**
 * Created by zhanghong on 15/4/28.
 */
public class GeoUtil {
    private static final Logger log = LoggerFactory.getLogger(GeoUtil.class);
    protected static String BAI_DU_KEY ="22f107d711f3ff7037ebb4e66f57b613";
    protected final static String GAO_DE_KEY ="6ff8f1e952d704ca83f3efff035856b6";
    protected final static String BAI_DU_LOCATION_SERVICE_URL = "http://api.map.baidu.com/geocoder/v2/?address=%s&output=json&ak=%s";
    protected final static String BAI_DU_CLOUD_GEO_CODE_SERVICE_URL = "http://api.map.baidu.com/cloudgc/v1?address=%s&ak=%s";
    protected final static String GAO_DE_LOCATION_SERVICE_URL = "http://restapi.amap.com/geocode/simple?resType=json&encode=utf-8&range=100&roadnum=3&crossnum=2&poinum=1&retvalue=1&sid=7000&rid=%d&address=%S&ia=1&key=%s";
    protected final static String GAODE_JSON_RESULT_APPEND="AMap.MAjaxResult[%d]=";
    protected final static Random rd=new Random();
    protected final static int seed=1000000;

    public static void setBaiduAkey(String ak){
        GeoUtil.BAI_DU_KEY = ak;
    }
//    protected static JsonBinder binder = JsonBinder.buildNonDefaultBinder();

//    public static BaiduLocatonResult generaterLocationFromBaidu(String location) {
//        String url = String
//                .format(BAI_DU_LOCATION_SERVICE_URL,
//                        location, BAI_DU_KEY);
//
//
//        BaiduLocatonResult result = null;
//        try {
//            result = Jackson.simple().readValue(HttpUtil.get(url, "UTF-8"), BaiduLocatonResult.class);
////            result = binder.fromJson(Util.readUrlContent(url, "UTF-8", 5000),
////                    BaiduLocatonResult.class);
//        } catch (Exception e) {
//            log.info(e.getMessage());
//        }
//        return result;
//    }

    public static BaiduCloudGeoCodeResult generateLocationFromBaiduCloudGC(String location) {

        String url = String
                .format(BAI_DU_CLOUD_GEO_CODE_SERVICE_URL,
                        location, BAI_DU_KEY);


        BaiduCloudGeoCodeResult result = null;
        try {
            result = Jackson.simple().readValue(HttpUtil.get(url, "UTF-8"), BaiduCloudGeoCodeResult.class);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return result;
    }

    public static GaoDeLocation generaterLocationFromGaoDe(String location,int count) throws UnsupportedEncodingException {
        location= URLEncoder.encode(location, "UTF-8");
        int rand=rd.nextInt(seed);
        String url = String
                .format(GAO_DE_LOCATION_SERVICE_URL,
                        rand	,location, GAO_DE_KEY);
        GaoDeLocation result = null;
        try {
            String content=HttpUtil.get(url, "UTF-8");
            content=content.replace(String
                    .format(GAODE_JSON_RESULT_APPEND, rand), "");
            result =Jackson.simple().readValue(content,
                    GaoDeLocation.class);
        } catch (Exception e) {
            count++;
            if(count<=3){
                result=generaterLocationFromGaoDe(location,count);
            }
            log.info(e.getMessage());
        }
        return result;
    }

    public static GeoLocation getGeoLocationFromAddress(String address) throws UnsupportedEncodingException {
        String safeString = address.replaceAll(" ", "");
        URLEncoder.encode(safeString, "utf-8");
//        BaiduLocatonResult baiDuResult = GeoUtil.generaterLocationFromBaidu(address);
        BaiduCloudGeoCodeResult baiDuResult = GeoUtil.generateLocationFromBaiduCloudGC(safeString);
//        if(baiDuResult==null||baiDuResult.status!=0||baiDuResult.result.size()==0||baiDuResult.result.get(0).precise<1){
//            //百度地址不可用，尝试高德
//            GaoDeLocation result=GeoUtil.generaterLocationFromGaoDe(safeString, 0);
//            if (result == null || (!result.getStatus().equals("E0")) || result.getList()== null||result.getList().size()==0) {
//                return null;
//            }else{
//                GaoDeLocation.GaoDeResult l=result.getList().get(0);
//                return new GeoLocation(l.getX(), l.getY());
//            }
//
//        }else{
//            return new GeoLocation(baiDuResult.result.get(0).location.lng
//                    ,baiDuResult.result.get(0).location.lat);
//
//        }
        if(baiDuResult != null && baiDuResult.status == 0 && baiDuResult.result.size() > 0){
            return new GeoLocation(baiDuResult.result.get(0).location.lng
                    ,baiDuResult.result.get(0).location.lat);
        }else{
            return null;
        }
    }


    static public void main(String[] args) throws UnsupportedEncodingException {
        GeoUtil.setBaiduAkey("GGz0btpSqduDiVx7Hukt9sxcZRldt3Wr");
        String address = "浙江省嘉兴市南湖区 吉玛 西路";
        System.out.print(GeoUtil.getGeoLocationFromAddress(address).toString());

    }


}
