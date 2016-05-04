package Message;


public class DefaultMessage {
	private String Flag;
	private String Context;
	public DefaultMessage(){
		
	}
	public DefaultMessage(String _Flag,String _Context){
		Flag = _Flag;
		Context = _Context;
	}
	public String getFlag() {
		return Flag;
	}
	public void setFlag(String _Flag) {
		Flag = _Flag;
	}
	public String getContext() {
		return Context;
	}
	public void setContext(String _Context) {
		this.Context = _Context;
	}
	@Override
	public String toString() {
		return "DefaultMessage [MessageFlag=" + Flag
				+ ", MessageContextString=" + Context + "]";
	}
	
}
