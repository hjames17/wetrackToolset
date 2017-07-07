/**
 * 
 */
package studio.wetrack.base.utils.geo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;


/**
 * @author Administrator
 *
 */
public class GaoDeLocation {
	private double time;
	private int count;
	private String status;
	private List<GaoDeResult> list;
	@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, isGetterVisibility = JsonAutoDetect.Visibility.NONE)
	public static  class GaoDeResult{
		public GaoDeResult() {
		}
		private String level;
		private String name;
		private String province;
		private String district;
		private Double x;
		private Double y;

		public String getLevel() {
			return level;
		}

		public void setLevel(String level) {
			this.level = level;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getProvince() {
			return province;
		}

		public void setProvince(String province) {
			this.province = province;
		}

		public String getDistrict() {
			return district;
		}

		public void setDistrict(String district) {
			this.district = district;
		}

		public Double getX() {
			return x;
		}

		public void setX(Double x) {
			this.x = x;
		}

		public Double getY() {
			return y;
		}

		public void setY(Double y) {
			this.y = y;
		}
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<GaoDeResult> getList() {
		return list;
	}

	public void setList(List<GaoDeResult> list) {
		this.list = list;
	}
}
