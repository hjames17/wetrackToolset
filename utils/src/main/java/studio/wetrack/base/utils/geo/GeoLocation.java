package studio.wetrack.base.utils.geo;

/**
 * Created by zhanghong on 15/4/28.
 */
public class GeoLocation {

    public GeoLocation(double longitude, double latitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }

    double longitude;
    double latitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "GeoLocation{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
