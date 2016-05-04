package domain;

import java.util.ArrayList;
import java.util.List;
import demo.bracelet.DBdomain.HealthData;
import demo.bracelet.DBdomain.PositionData;
import demo.bracelet.DBdomain.SafeDangerData;

public class All {
	private List<HealthData> healthDatas = new ArrayList<HealthData>();
	private List<PositionData> positionDatas= new ArrayList<PositionData>();;
	private SafeDangerData safe = new SafeDangerData();
	private SafeDangerData Danger = new SafeDangerData();
	public List<HealthData> getHealthDatas() {
		return healthDatas;
	}
	public void setHealthDatas(List<HealthData> healthDatas) {
		this.healthDatas = healthDatas;
	}
	public List<PositionData> getPositionDatas() {
		return positionDatas;
	}
	public void setPositionDatas(List<PositionData> positionDatas) {
		this.positionDatas = positionDatas;
	}
	public SafeDangerData getSafe() {
		return safe;
	}
	public void setSafe(SafeDangerData safe) {
		this.safe = safe;
	}
	public SafeDangerData getDanger() {
		return Danger;
	}
	public void setDanger(SafeDangerData danger) {
		Danger = danger;
	}

}
