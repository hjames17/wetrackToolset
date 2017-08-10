package studio.wetrack.base.utils.geo;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.apache.commons.io.IOUtils;
import studio.wetrack.base.utils.jackson.Jackson;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhanghong on 2017/8/10.
 */
public class AreaUtil {

    static Map<String, Map> mapById;
    static Map<String, Map> cityMapById = new HashMap<>();
    static Map<String, Map> districtMapById = new HashMap<>();
    static {
        InputStream is = AreaUtil.class.getResourceAsStream("/city.json");
        try {
            String theString = IOUtils.toString(is, "utf-8");
            mapById = Jackson.base().readValue(theString, Map.class);
            mapById.forEach((key, value)->{
                String pName = (String)value.get("name");
                Map mapOfCities = (Map)value.get("cites");
                cityMapById.putAll(mapOfCities);
                Collection<Map> cities = mapOfCities.values();
                for (Map map : cities) {
                    map.put("pName", pName);
                    map.put("pId", key);
                }
                mapOfCities.forEach((cId, v) -> {
                    Map<String, Object> vm = (Map<String, Object>)v;
                    String cName = (String)vm.get("name");
                    Map<String, Map> mapOfDistricts = (Map<String, Map>)vm.get("districts");
                    districtMapById.putAll(mapOfDistricts);
                    Collection<Map> districts = mapOfDistricts.values();

                    for (Map map : districts) {
                        map.put("cName", cName);
                        map.put("cId", cId);
                        map.put("pName", vm.get("pName"));
                        map.put("pId", vm.get("pId"));
                    }
                });
            });
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProvinceName(int pId){
        Map pMap = mapById.get("" + pId);
        if(pMap != null){
            return (String)pMap.get("name");
        }
        return null;
    }

    public static String getCityName(int cId){
        Map cMap = cityMapById.get("" + cId);
        if(cMap != null){
            return (String)cMap.get("name");
        }
        return null;
    }

    public static String getDistrictName(int dId){
        Map dMap = districtMapById.get("" + dId);
        if(dMap != null){
            return (String)dMap.get("name");
        }
        return null;
    }

    public static String getFullName(int dId){

        Map dMap = districtMapById.get("" + dId);
        if(dMap != null){
            return (String)dMap.get("pName") + dMap.get("cName") + dMap.get("name");
        }
        return null;
    }

    public static Integer getProvinceId(String name){
        for (Map map : mapById.values()) {
            if(map.get("name").equals(name)){
                return Integer.parseInt(map.get("id").toString());
            }
        }
        return null;
    }

    public static Integer getCityId(String province, String city){
        for (Map map : cityMapById.values()) {
            if(map.get("name").equals(city) && map.get("pName").equals(province)){
                return Integer.parseInt(map.get("id").toString());
            }
        }
        return null;
    }

    public static Integer getDistrictId(String province, String city, String district){
        for (Map map : districtMapById.values()) {
            if(map.get("name").equals(district) && map.get("cName").equals(city) && map.get("pName").equals(province)){
                return Integer.parseInt(map.get("id").toString());
            }
        }
        return null;
    }



    public static void main(String[] args){

        System.out.println(ObjectSizeCalculator.getObjectSize(AreaUtil.mapById)/1024 + "k");

        Integer did = AreaUtil.getDistrictId("浙江省", "杭州市", "西湖区");
        Integer pid = AreaUtil.getProvinceId("浙江省");
        Integer cid = AreaUtil.getCityId("浙江省", "杭州市");
        System.out.println(did);
        System.out.println(pid);
        System.out.println(cid);
        System.out.println(AreaUtil.getFullName(did));
        System.out.println(AreaUtil.getDistrictName(did));
        System.out.println(AreaUtil.getCityName(cid));
        System.out.println(AreaUtil.getProvinceName(pid));
    }
}
