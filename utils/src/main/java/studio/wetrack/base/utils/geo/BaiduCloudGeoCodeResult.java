package studio.wetrack.base.utils.geo;

import java.util.List;

/**
 * Created by zhanghong on 17/7/7.
 */
public class BaiduCloudGeoCodeResult {
    public int status;
    public String message;
    public List<Result> result;


    public static class Result{
        public String source;
        public Location location;
        public String bound;
        public String formatted_address;
        public Address_Components address_components;
        public float precise;

    }

    public static class Address_Components{
        public String province;
        public String city;
        public String district;
        public String street;
        public String level;
    }

    public static class Location{
        public double lat;
        public double lng;
    }

    @Override
    public String toString() {
        return "BaiduCloudGeoCodeResult{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
