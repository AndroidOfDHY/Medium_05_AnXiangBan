package com.anxiangban.DBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import com.anxiangban.DBHelper.domain.*;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MytabCursor {
	private static final String Temperature = "BodyTemperature"; // ���±���
	private static final String Pressure = "BloodPressure"; // Ѫѹ����
	private static final String HEARTRATE = "HeartRate"; // ���ʱ���
	private static final String POSITION = "Position";  // ����λ�����ݿ����
	private static final String SAFEDANGER = "SafeDanger";  // ��ȫΣ����
	private SQLiteDatabase db = null; // ���ݿ�
	private HealthData HData = null; // �������ݸ�����
	private PositionData PData = null;  // λ�����ݸ�����
	private SafeDangerData SDData = null;  // ��ȫΣ�������ݸ�����
	private List<HealthData> healthData = null; // �洢�������ݸ������ list
	private List<PositionData> positionData = null;  // �洢λ�����ݸ������ list
	private List<SafeDangerData> safedangerData = null;  // �洢��ȫΣ�������ݸ������list
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // ����ʱ���ʽ
	public MytabCursor(SQLiteDatabase db) {
		this.db = db;
	}

	public List<HealthData> BodyTemperatureFind() throws ParseException {
		healthData = new ArrayList<HealthData>(); // ʵ���� list

		
		String sql = "SELECT id, bodytemperature, day FROM " + Temperature; // ��ѯSQL���
		Cursor result = this.db.rawQuery(sql, null); // ִ�в�ѯ���
		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) { // ����ѭ���ķ�ʽ��������
			HData = new HealthData();
			HData.setBodyTemperature(result.getFloat(1)); // ������������
			HData.setDay(format.parse(result.getString(2))); // ����ʱ������
			healthData.add(HData); // ��������ӵ� listData
		}
		result.close();
		// this.db.close(); // �ر����ݿ�
		return healthData; // ���ز�ѯ�� list
	}

	public List<HealthData> BloodPressureFind() throws ParseException {
		healthData = new ArrayList<HealthData>();
		String sql = "SELECT id, highbloodpressure, lowbloodpressure, day FROM "
				+ Pressure;
		Cursor result = this.db.rawQuery(sql, null); // ִ�в�ѯ���
		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) { // ����ѭ���ķ�ʽ��������
			HData = new HealthData();
			HData.setHighBloodPressure(result.getFloat(1));
			HData.setLowBloodPressure(result.getFloat(2));
			HData.setDay(format.parse(result.getString(3)));
			healthData.add(HData);
		}
		// this.db.close();
		return healthData;
	}

	public List<HealthData> HeartRateFind() throws ParseException {
		healthData = new ArrayList<HealthData>();
		String sql = "SELECT id, heartrate, day FROM " + HEARTRATE;
		Cursor result = this.db.rawQuery(sql, null); // ִ�в�ѯ���
		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) { // ����ѭ���ķ�ʽ��������
			HData = new HealthData();
			HData.setHeartRate(result.getShort(1));
			HData.setDay(format.parse(result.getString(2)));
			healthData.add(HData);
		}
		result.close();
		// this.db.close();
		return healthData;
	}
	
	public List<PositionData> PositionFind() throws ParseException {
		positionData = new ArrayList<PositionData>();
		String sql = "SELECT id, braceletid, longitude, latitude, place, day FROM " + POSITION;
		Cursor result = this.db.rawQuery(sql, null);
		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) { // ����ѭ���ķ�ʽ��������
			PData = new PositionData();
			PData.setBraceletId(result.getString(1));
			PData.setLongitude(result.getDouble(2));
			PData.setLatitude(result.getDouble(3));
			PData.setPlace(result.getString(4));
			PData.setDay(format.parse(result.getString(5)));
			positionData.add(PData);
		}
		result.close();
		return positionData;
	}
	
	public List<SafeDangerData> SafeDangerFind() {
		safedangerData = new ArrayList<SafeDangerData>();
		String sql = "SELECT id, " +
				"longitude1, latitude1, " +
				"longitude2, latitude2, " +
				"longitude3, latitude3, " +
				"longitude4, latitude4, " +
				"longitude5, latitude5, " +
				"longitude6, latitude6, " +
				"longitude7, latitude7, " +
				"longitude8, latitude8, " +
				"longitude9, latitude9, " +
				"longitude10, latitude10, " +
				"isSafe FROM " + SAFEDANGER;
		Cursor result = this.db.rawQuery(sql, null);
		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) { // ����ѭ���ķ�ʽ��������
			SDData = new SafeDangerData();
			SDData.setLongitude1(result.getDouble(1));
			SDData.setLatitude1(result.getDouble(2));
			SDData.setLongitude2(result.getDouble(3));
			SDData.setLatitude2(result.getDouble(4));
			SDData.setLongitude3(result.getDouble(5));
			SDData.setLatitude3(result.getDouble(6));
			SDData.setLongitude4(result.getDouble(7));
			SDData.setLatitude4(result.getDouble(8));
			SDData.setLongitude5(result.getDouble(9));
			SDData.setLatitude5(result.getDouble(10));
			SDData.setLongitude6(result.getDouble(11));
			SDData.setLatitude6(result.getDouble(12));
			SDData.setLongitude7(result.getDouble(13));
			SDData.setLatitude7(result.getDouble(14));
			SDData.setLongitude8(result.getDouble(15));
			SDData.setLatitude8(result.getDouble(16));
			SDData.setLongitude9(result.getDouble(17));
			SDData.setLatitude9(result.getDouble(18));
			SDData.setLongitude10(result.getDouble(19));
			SDData.setLatitude10(result.getDouble(20));
			SDData.setIsSafe(result.getInt(21));
			safedangerData.add(SDData);
		}
		result.close();
		return safedangerData;
	}
	public int SafeDangerFindId(int IsSafe) {
		safedangerData = new ArrayList<SafeDangerData>();
		String sql = "SELECT id FROM " + SAFEDANGER + " WHERE isSafe = "+IsSafe;
		Cursor result = this.db.rawQuery(sql, null);
		int i = -1;
		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) { // ����ѭ���ķ�ʽ��������
			System.out.println(result.getInt(0));
			i = result.getInt(0);
		}
		return i;
	}
	public int SafeDangerFindCount(int IsSafe) {
		safedangerData = new ArrayList<SafeDangerData>();
		String sql = "SELECT COUNT(*) FROM " + SAFEDANGER + " WHERE isSafe = "+IsSafe;
		Cursor result = this.db.rawQuery(sql, null);
		int i = -1;
		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) { // ����ѭ���ķ�ʽ��������
			i = result.getInt(0);
		}
		result.close();
		return i;
	}
	public int BodyTemperatureCount() {
		Cursor result = this.db.query(Temperature, null, null, null, null, null, null);
		int count = 0;
		count = result.getCount();
//		System.out.println("++++++++Cursor Temperature count = " + count);
		return count;
	}
	
	public int BloodPressureCount() {
		Cursor result = this.db.query(Pressure, null, null, null, null, null, null);
		int count = 0;
		count = result.getCount();
//		System.out.println("++++++++Cursor Pressure count = " + count);
		return count;
	}
	
	public int HeartRateCount() {
		Cursor result = this.db.query(HEARTRATE, null, null, null, null, null, null);
		int count = 0;
		count = result.getCount();
//		System.out.println("++++++++Cursor HEARTRATE count = " + count);
		return count;
	}

	public void Stop() {
		db.close();
	}
}
