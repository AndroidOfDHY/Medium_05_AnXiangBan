package domain;

public class Sign {
	private String IMSI = "";
	private String BraceletId= "";
	private String YanZhen= "";
	private boolean IsBracelet = false;
	public String getIMSI() {
		return IMSI;
	}
	public void setIMSI(String iMSI) {
		IMSI = iMSI;
	}
	public String getBraceletId() {
		return BraceletId;
	}
	public void setBraceletId(String braceletId) {
		BraceletId = braceletId;
	}
	public String getYanZhen() {
		return YanZhen;
	}
	public void setYanZhen(String yanZhen) {
		YanZhen = yanZhen;
	}
	public boolean getIsBracelet() {
		return IsBracelet;
	}
	public void setIsBracelet(boolean isBracelet) {
		IsBracelet = isBracelet;
	}
}
