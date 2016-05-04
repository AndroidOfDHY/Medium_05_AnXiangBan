package domain;

import java.io.Serializable;
import java.util.Arrays;

@SuppressWarnings("serial")
public class UploadFile implements Serializable {
	private String title;
	private String name;
	private byte[] contentData;
	private long congtentSize;
	private String ext ;
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public byte[] getContentData() {
		return contentData;
	}
	public void setContentData(byte[] contentData) {
		this.contentData = contentData;
	}
	public long getCongtentSize() {
		return congtentSize;
	}
	public void setCongtentSize(long congtentSize) {
		this.congtentSize = congtentSize;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String toString() {
		return "UploadFile [title=" + title + ", name=" + name
				+ ", contentData=" + Arrays.toString(contentData)
				+ ", congtentSize=" + congtentSize + ", ext=" + ext + "]";
	}
}
