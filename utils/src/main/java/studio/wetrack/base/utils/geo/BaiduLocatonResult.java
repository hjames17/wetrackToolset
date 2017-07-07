/**
 * 
 */
package studio.wetrack.base.utils.geo;


import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * @author Administrator
 *
 */
public class BaiduLocatonResult {
	
	public BaiduLocatonResult() {
	}
	private int status;	
	private Result result;
	@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
	public static  class Result{
		public Result() {
		}
		private Location location;
		private int precise;
		private int confidence;
		private String level;

		public Location getLocation() {
			return location;
		}

		public void setLocation(Location location) {
			this.location = location;
		}

		public int getPrecise() {
			return precise;
		}

		public void setPrecise(int precise) {
			this.precise = precise;
		}

		public int getConfidence() {
			return confidence;
		}

		public void setConfidence(int confidence) {
			this.confidence = confidence;
		}

		public String getLevel() {
			return level;
		}

		public void setLevel(String level) {
			this.level = level;
		}
	}
	public static  class Location{
		private double lng;
		private double lat;
		public Location() {
		}

		public double getLng() {
			return lng;
		}

		public void setLng(double lng) {
			this.lng = lng;
		}

		public double getLat() {
			return lat;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}
}
