package com.anxiangban.activitytimeline;


public class TimeLineEntity {
	@SuppressWarnings("unused")
	private static final String TAG = TimeLineEntity.class.getSimpleName();
	private String text;
	private String time;
	private double lat;
	private double lon;
	private int type;
	public TimeLineEntity() {

	}
	public TimeLineEntity(String time, String text,double lat,double lon, int type) {
		super();
		this.time = time;
		this.text = text;
		this.lat =lat;
		this.lon =lon;
		this.setType(type);
	}
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	
}
