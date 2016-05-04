package com.anxiangban.activityyuyinjiaoliu;

public class YuYinEntity {
	private static final String TAG = YuYinEntity.class.getSimpleName();
	private String name;
	private String date;
	private String text;
	private String time;

	public YuYinEntity() {
	}

	public YuYinEntity(String name, String date, String text) {
		super();
		this.name = name;
		this.date = date;
		this.text = text;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public static String getTag() {
		return TAG;
	}
}
