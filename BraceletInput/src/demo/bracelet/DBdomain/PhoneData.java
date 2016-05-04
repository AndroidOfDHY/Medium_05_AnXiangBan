package demo.bracelet.DBdomain;

public class PhoneData {
	private String IMSI = null;
	private String braceletid = null;
	public PhoneData(String IMSI,String braceletid){
		this.IMSI = IMSI;
		this.braceletid = braceletid;
	}
	public PhoneData(){
	}
	public String getIMSI() {
		return IMSI;
	}

	public void setIMSI(String iMSI) {
		IMSI = iMSI;
	}

	public String getBraceletid() {
		return braceletid;
	}

	public void setBraceletid(String braceletid) {
		this.braceletid = braceletid;
	}
}
