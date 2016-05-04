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
	private static final String Temperature = "BodyTemperature"; // 体温表名
	private static final String Pressure = "BloodPressure"; // 血压表名
	private static final String HEARTRATE = "HeartRate"; // 心率表名
	private static final String POSITION = "Position";  // 坐标位置数据库表名
	private static final String SAFEDANGER = "SafeDanger";  // 安全危险区
	private SQLiteDatabase db = null; // 数据库
	private HealthData HData = null; // 健康数据辅助类
	private PositionData PData = null;  // 位置数据辅助类
	private SafeDangerData SDData = null;  // 安全危险区数据辅助类
	private List<HealthData> healthData = null; // 存储健康数据辅助类的 list
	private List<PositionData> positionData = null;  // 存储位置数据辅助类的 list
	private List<SafeDangerData> safedangerData = null;  // 存储安全危险区数据辅助类的list
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 设置时间格式
	public MytabCursor(SQLiteDatabase db) {
		this.db = db;
	}

	public List<HealthData> BodyTemperatureFind() throws ParseException {
		healthData = new ArrayList<HealthData>(); // 实例化 list

		
		String sql = "SELECT id, bodytemperature, day FROM " + Temperature; // 查询SQL语句
		Cursor result = this.db.rawQuery(sql, null); // 执行查询语句
		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) { // 采用循环的方式检索数据
			HData = new HealthData();
			HData.setBodyTemperature(result.getFloat(1)); // 设置体温数据
			HData.setDay(format.parse(result.getString(2))); // 设置时间数据
			healthData.add(HData); // 将数据添加到 listData
		}
		result.close();
		// this.db.close(); // 关闭数据库
		return healthData; // 返回查询的 list
	}

	public List<HealthData> BloodPressureFind() throws ParseException {
		healthData = new ArrayList<HealthData>();
		String sql = "SELECT id, highbloodpressure, lowbloodpressure, day FROM "
				+ Pressure;
		Cursor result = this.db.rawQuery(sql, null); // 执行查询语句
		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) { // 采用循环的方式检索数据
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
		Cursor result = this.db.rawQuery(sql, null); // 执行查询语句
		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) { // 采用循环的方式检索数据
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
		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) { // 采用循环的方式检索数据
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
		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) { // 采用循环的方式检索数据
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
		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) { // 采用循环的方式检索数据
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
		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) { // 采用循环的方式检索数据
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
