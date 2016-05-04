package domain;

import java.util.Calendar;

public class Alarm {
	private Calendar calendar;
	private UploadFile file;
	private Boolean isRepeat;
	
	public Calendar getCalendar() {
		return calendar;
	}
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}
	public UploadFile getFile() {
		return file;
	}
	public void setFile(UploadFile file) {
		this.file = file;
	}
	public Boolean getIsRepeat() {
		return isRepeat;
	}
	public void setIsRepeat(Boolean isRepeat) {
		this.isRepeat = isRepeat;
	}	
}
