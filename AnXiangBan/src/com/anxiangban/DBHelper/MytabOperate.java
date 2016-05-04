package com.anxiangban.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.anxiangban.DBHelper.domain.PositionData;
import com.anxiangban.DBHelper.domain.SafeDangerData;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class MytabOperate {
	private static final String Temperature = "BodyTemperature"; // ���±���
	private static final String Pressure = "BloodPressure"; // Ѫѹ����
	private static final String HEARTRATE = "HeartRate"; // ���ʱ���
	private static final String POSITION = "Position";  // ����λ�����ݿ����
	private static final String SAFEDANGER = "SafeDanger";  // ��ȫΣ����
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // ����ʱ���ʽ
	private SQLiteDatabase db = null; // ���ݿ����

	public MytabOperate(SQLiteDatabase db) {
		this.db = db;
	}

	public void BodyTemperatureInsert(float BodyTemperature, Date day) {
		ContentValues cv = new ContentValues();// ��װ����
		cv.put("bodytemperature", BodyTemperature);
		cv.put("day", format.format(day));
		this.db.insert(Temperature, null, cv); // ����ʹ�� ContentValues ��װ
		// this.db.close();
	}

	public void BloodPressureInsert(float HighBloodPressure,
			float LowBloodPressure, Date day) {
		ContentValues cv = new ContentValues();
		cv.put("highbloodpressure", HighBloodPressure);// ��װ����
		cv.put("lowbloodpressure", LowBloodPressure);
		cv.put("day", format.format(day));
		this.db.insert(Pressure, null, cv); // ����ʹ�� ContentValues ��װ
		// this.db.close();
	}

	public void HeartRateInsert(short HeartRate, Date day) {
		ContentValues cv = new ContentValues();
		cv.put("heartrate", HeartRate);// ��װ����
		cv.put("day", format.format(day));
		this.db.insert(HEARTRATE, null, cv); // ����ʹ�� ContentValues ��װ
		// this.db.close();
	}
	
	public void PositionInsert(PositionData poData) {
		ContentValues cv = new ContentValues();
		cv.put("braceletid", poData.getBraceletId());
		cv.put("longitude", poData.getLongitude());
		cv.put("latitude", poData.getLatitude());
		cv.put("place", poData.getPlace());
		cv.put("day", format.format(poData.getDay()));
		this.db.insert(POSITION, null, cv);
	}
	
	public void SafeDangerInsert(SafeDangerData SDData) {
		ContentValues cv = new ContentValues();
		cv.put("longitude1", SDData.getLongitude1());
		cv.put("latitude1", SDData.getLatitude1());
		cv.put("longitude2", SDData.getLongitude2());
		cv.put("latitude2", SDData.getLatitude2());
		cv.put("longitude3", SDData.getLongitude3());
		cv.put("latitude3", SDData.getLatitude3());
		cv.put("longitude4", SDData.getLongitude4());
		cv.put("latitude4", SDData.getLatitude4());
		cv.put("longitude5", SDData.getLongitude5());
		cv.put("latitude5", SDData.getLatitude5());
		cv.put("longitude6", SDData.getLongitude6());
		cv.put("latitude6", SDData.getLatitude6());
		cv.put("longitude7", SDData.getLongitude7());
		cv.put("latitude7", SDData.getLatitude7());
		cv.put("longitude8", SDData.getLongitude8());
		cv.put("latitude8", SDData.getLatitude8());
		cv.put("longitude9", SDData.getLongitude9());
		cv.put("latitude9", SDData.getLatitude9());
		cv.put("longitude10", SDData.getLongitude10());
		cv.put("latitude10", SDData.getLatitude10());
		cv.put("isSafe", SDData.getIsSafe());
		this.db.insert(SAFEDANGER, null, cv);
	}

	public void BodyTemperatureUpdate() {
		String sql = "UPDATE " + Temperature + " SET id = id-1";
		this.db.execSQL(sql);
		// this.db.close();
	}

	public void BloodPressureUpdate() {
		String sql = "UPDATE " + Pressure + " SET id = id-1";
		this.db.execSQL(sql);
		// this.db.close();
	}

	public void HeartRateInsertUpdate() {
		String sql = "UPDATE " + HEARTRATE + " SET id = id-1";
		this.db.execSQL(sql);
		// this.db.close();
	}
	
	public void SafeDangerUpdate(SafeDangerData SDData) {
		// ContentValues ���ݷ�װ��
		ContentValues cv = new ContentValues();
		// ��װ����
		cv.put("longitude1", SDData.getLongitude1());
		cv.put("latitude1", SDData.getLatitude1());
		cv.put("longitude2", SDData.getLongitude2());
		cv.put("latitude2", SDData.getLatitude2());
		cv.put("longitude3", SDData.getLongitude3());
		cv.put("latitude3", SDData.getLatitude3());
		cv.put("longitude4", SDData.getLongitude4());
		cv.put("latitude4", SDData.getLatitude4());
		cv.put("longitude5", SDData.getLongitude5());
		cv.put("latitude5", SDData.getLatitude5());
		cv.put("longitude6", SDData.getLongitude6());
		cv.put("latitude6", SDData.getLatitude6());
		cv.put("longitude7", SDData.getLongitude7());
		cv.put("latitude7", SDData.getLatitude7());
		cv.put("longitude8", SDData.getLongitude8());
		cv.put("latitude8", SDData.getLatitude8());
		cv.put("longitude9", SDData.getLongitude9());
		cv.put("latitude9", SDData.getLatitude9());
		cv.put("longitude10", SDData.getLongitude10());
		cv.put("latitude10", SDData.getLatitude10());
		String whereClause = "isSafe = ?";
		String whereArgs[] = new String[] {String.valueOf(SDData.getIsSafe())};
		this.db.update(SAFEDANGER, cv, whereClause, whereArgs);
	}

	public void BodyTemperatureDelete(int id) {
		String whereClause = "id = ?";
		String whereArgs[] = new String[] { String.valueOf(id) };
		this.db.delete(Temperature, whereClause, whereArgs);
		// this.db.close();
	}

	public void BloodPressureDelete(int id) {
		String whereClause = "id = ?";
		String whereArgs[] = new String[] { String.valueOf(id) };
		this.db.delete(Pressure, whereClause, whereArgs);
		// this.db.close();
	}

	public void HeartRateDelete(int id) {
		String whereClause = "id = ?";
		String whereArgs[] = new String[] { String.valueOf(id) };
		this.db.delete(HEARTRATE, whereClause, whereArgs);
		// this.db.close();
	}
	public void SafeDangerDelete(int id) {
		String whereClause = "id = ?";
		String whereArgs[] = new String[] { String.valueOf(id) };
		this.db.delete(SAFEDANGER, whereClause, whereArgs);
	}
	public void PositionDelete() {
		String sql = "DELETE FROM " + POSITION;
		this.db.execSQL(sql);
	}
	
	public void SafeDangerDelete() {
		String sql = "DELETE FROM " + SAFEDANGER;
		this.db.execSQL(sql);
	}

	public void Stop() {
		db.close();
	}
}
