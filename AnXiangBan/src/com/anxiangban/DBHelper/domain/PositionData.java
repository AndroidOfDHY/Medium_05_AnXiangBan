package com.anxiangban.DBHelper.domain;

import java.util.Date;

public class PositionData {
	private String BraceletId = null;  // �ֻ�ID
	private double Longitude = 0.0;  // λ������ľ���
	private double Latitude = 0.0;  // λ�������γ��
	private String Place = null;  // λ������ĵ�ַ
	private Date day = null; // ʱ��
	
	public String getPlace() {
		return Place;
	}

	public void setPlace(String place) {
		Place = place;
	}

	public String getBraceletId() {
		return BraceletId;
	}

	public void setBraceletId(String s) {
		BraceletId = s;
	}

	public double getLongitude() {
		return Longitude;
	}

	public void setLongitude(double longitude) {
		Longitude = longitude;
	}

	public double getLatitude() {
		return Latitude;
	}

	public void setLatitude(double latitude) {
		Latitude = latitude;
	}
	
	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}
}
