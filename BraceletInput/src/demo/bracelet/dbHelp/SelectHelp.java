package demo.bracelet.dbHelp;

import domain.All;

public class SelectHelp {
	public String IMSI;
	private String braceletId;	
	public SelectHelp(String IMSI) {
		this.IMSI = IMSI;
	}
	public All getAll(){
		All all = new All();
		HealthSql healthSql = new HealthSql();
		PositionSql positionSql = new PositionSql();
		SafeDangerSql safeDangerSql = new SafeDangerSql();		
		setBraceletId();
		all.setHealthDatas(healthSql.HealthSelect(braceletId));
		all.setPositionDatas(positionSql.PositionSelect(braceletId));
		all.setDanger(safeDangerSql.SafeDangerSelect(0,braceletId));
		all.setSafe(safeDangerSql.SafeDangerSelect(1,braceletId));
		return all;
	}
	public String getBraceletId() {
		return braceletId;
	}

	public void setBraceletId() {
		PhoneSql phoneSql = new PhoneSql();
		braceletId = phoneSql.PhoneFindID(IMSI);
	}
}
