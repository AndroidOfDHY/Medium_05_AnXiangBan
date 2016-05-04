package demo.bracelet.DBdomain;

import java.util.Date;

public class HealthData {
	private float BodyTemperature = 0; // ����
	private float HighBloodPressure = 0; // ��Ѫѹ
	private float LowBloodPressure = 0; // ��Ѫѹ
	private int HeartRate = 0; // ����
	
	private Date day = null; // ʱ��
	
	public float getBodyTemperature() {
		return BodyTemperature;
	}

	public void setBodyTemperature(float bodyTemperature) {
		BodyTemperature = bodyTemperature;
	}

	public float getHighBloodPressure() {
		return HighBloodPressure;
	}

	public void setHighBloodPressure(float highBloodPressure) {
		HighBloodPressure = highBloodPressure;
	}

	public float getLowBloodPressure() {
		return LowBloodPressure;
	}

	public void setLowBloodPressure(float lowBloodPressure) {
		LowBloodPressure = lowBloodPressure;
	}

	public int getHeartRate() {
		return HeartRate;
	}

	public void setHeartRate(int heartRate) {
		HeartRate = heartRate;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	@Override
	public String toString() {
		return "MyData [BodyTemperature=" + BodyTemperature
				+ ", HighBloodPressure=" + HighBloodPressure
				+ ", LowBloodPressure=" + LowBloodPressure + ", HeartRate="
				+ HeartRate + ", day=" + day + "]";
	}

}
