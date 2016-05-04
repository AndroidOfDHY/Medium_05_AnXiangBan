package Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import domain.UploadFile;
import android.annotation.SuppressLint;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Environment;

/**
 * 录音工具类
 */
public class MediaUtil {
	private String voice = Environment.getExternalStorageDirectory() + "/AXB/voice/";// 文件
	private String alarm = Environment.getExternalStorageDirectory() + "/AXB/alarm/alarm.amr";// 文件
	
	public void playMedia(String name) {
		MediaPlayer mediaPlayer = null;
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(voice +name);
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (Exception e) {

		}
	}
	public void playMedia() {
		MediaPlayer mediaPlayer = null;
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(alarm);
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (Exception e) {

		}
	}
	public void openSound(String name, AssetManager am) {
		MediaPlayer mediaPlayer = null;
		AssetFileDescriptor afd =null;
		try {
			afd = am.openFd(name);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(afd.getFileDescriptor(),
					afd.getStartOffset(), afd.getLength());
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (Exception e) {

		}
	}
	public String getAlarm() {
		return alarm;
	}
	@SuppressLint("SdCardPath")
	public Boolean saveFile(UploadFile upload){ // 负责文件内容的保存
		
		File file = new File(voice+ upload.getName());
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdir();
		}
		OutputStream output = null;
		try {
			output = new FileOutputStream(file);
			output.write(upload.getContentData());
			output.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	@SuppressLint("SdCardPath")
	public boolean saveAlarm(UploadFile upload){ // 负责文件内容的保存
		File file = new File(alarm);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdir();
		}
		OutputStream output = null;
		try {
			output = new FileOutputStream(file);
			output.write(upload.getContentData());
			output.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}