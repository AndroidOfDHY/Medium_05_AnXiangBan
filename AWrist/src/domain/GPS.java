package domain;

public class GPS {
	private Double latitude; //¾­¶È
	private Double longitude;//Î³¶È
	public GPS(){
		
	}
	public GPS(Double latitude,Double longitude){
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public GPS(int latitudeE6, int longitudeE6) {
		this.latitude = (double) latitudeE6;
		this.longitude = (double) longitudeE6;
		// TODO Auto-generated constructor stub
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	@Override
	public String toString() {
		return "GPS [latitude=" + latitude + ", longitude=" + longitude + "]";
	}
	
}
