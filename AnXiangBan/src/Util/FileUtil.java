package Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.os.Environment;
import domain.UploadFile;

public class FileUtil {
	private static String PATH = Environment.getExternalStorageDirectory() + "/AXB/alarm/alarm.amr";
	private static String voice = Environment.getExternalStorageDirectory() + "/AXB/voice/";
	@SuppressWarnings("resource")
	public static UploadFile getUploadFile() {
		UploadFile myFile = new UploadFile();
		File file = new File(PATH);
		myFile.setName(file.getName());
		InputStream input = null;
		try {
			input = new FileInputStream(file); //从文件中读取
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte data[] = new byte[1024];
			int len = 0;
			while((len = input.read(data)) != -1){
				bos.write(data, 0, len);
			}
			myFile.setCongtentSize(file.length());
			myFile.setContentData(bos.toByteArray());
			myFile.setExt("amr");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myFile;
	}
	
	@SuppressWarnings("resource")
	public static UploadFile getUploadFile(String name) {
		UploadFile myFile = new UploadFile();
		File file = new File(voice+name);
		myFile.setName(file.getName());
		InputStream input = null;
		try {
			input = new FileInputStream(file); //从文件中读取
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte data[] = new byte[1024];
			int len = 0;
			while((len = input.read(data)) != -1){
				bos.write(data, 0, len);
			}
			myFile.setCongtentSize(file.length());
			myFile.setContentData(bos.toByteArray());
			myFile.setExt("amr");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myFile;
	}
	public static void initFile() {
		String status = Environment.getExternalStorageState();
		File filetemp = Environment.getExternalStorageDirectory();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			File AXB = new File(filetemp + "/AXB");
			if (!AXB.exists()) {
				AXB.mkdirs();
			}
			File voice = new File(filetemp + "/AXB/voice");
			if (!voice.exists()) {
				voice.mkdirs();
			}
			File alarm = new File(filetemp + "/AXB/alarm");
			if (!alarm.exists()) {
				alarm.mkdirs();
			}
			
		}
	}
	public static boolean isExists(String Path){
		File file = new File(Path);
		return file.exists();
	}
}
