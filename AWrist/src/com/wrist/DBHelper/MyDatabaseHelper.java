package com.wrist.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASENAME = "axb_growth.db";
	private static final int DATABASEVERSION = 1;  // 设置数据库的版本
	private static final String Temperature = "BodyTemperature";  // 体温表名
	private static final String Pressure = "BloodPressure";  // 血压表名
	private static final String HEARTRATE = "HeartRate";  // 心率表名
	private static final String POSITION = "Position";  // 坐标位置数据库表名
	private static final String SAFEDANGER = "SafeDanger";  // 安全危险区

	public MyDatabaseHelper(Context context) {  // 用户最关心的也肯定只是 Context
		super(context, DATABASENAME, null, DATABASEVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {  // 创建数据表
		String sql_1 = "CREATE TABLE " + Temperature + "(" 
				+ "id INTEGER PRIMARY KEY,"  // 在 SQLite 中设置 Integer、PRIMARY KEY 则 ID 自动增长
				+ "bodytemperature FLOAT NOT NULL,"
				+ "day VARCHAR(25) NOT NULL"
				+ ");";
		db.execSQL(sql_1);  // 执行SQL
		
		String sql_2 = "CREATE TABLE " + Pressure + "(" 
				+ "id INTEGER PRIMARY KEY,"  // 在 SQLite 中设置 Integer、PRIMARY KEY 则 ID 自动增长
				+ "highbloodpressure FLOAT NOT NULL,"
				+ "lowbloodpressure FLOAT NOT NULL,"
				+ "day VARCHAR(25) NOT NULL"
				+ ");";
		db.execSQL(sql_2);  // 执行SQL
		
		String sql_3 = "CREATE TABLE " + HEARTRATE + "(" 
				+ "id INTEGER PRIMARY KEY,"  // 在 SQLite 中设置 Integer、PRIMARY KEY 则 ID 自动增长
				+ "heartrate SHORT NOT NULL,"
				+ "day VARCHAR(25) NOT NULL"
				+ ");";
		db.execSQL(sql_3);  // 执行SQL
		
		String sql_4 = "CREATE TABLE " + POSITION + "("
				+ "id INTEGER PRIMARY KEY,"  // 在 SQLite 中设置 Integer、PRIMARY KEY 则 ID 自动增长
				+ "braceletid VARCHAR(25) NOT NULL,"  // 手环ID
				+ "longitude DOUBLE NOT NULL,"  // 经度
				+ "latitude DOUBLE NOT NULL,"  // 纬度
				+ "place VARCHAR(50) NOT NULL,"  // 地址
				+ "day time NOT NULL"  // 时间
				+ ");";
		db.execSQL(sql_4);
		
		String sql_5 = "CREATE TABLE " + SAFEDANGER + "("
				+ "id INTEGER PRIMARY KEY,"
				+ "longitude1 DOUBLE,"
				+ "latitude1 DOUBLE,"
				+ "longitude2 DOUBLE,"
				+ "latitude2 DOUBLE,"
				+ "longitude3 DOUBLE,"
				+ "latitude3 DOUBLE,"
				+ "longitude4 DOUBLE,"
				+ "latitude4 DOUBLE,"
				+ "longitude5 DOUBLE,"
				+ "latitude5 DOUBLE,"
				+ "longitude6 DOUBLE,"
				+ "latitude6 DOUBLE,"
				+ "longitude7 DOUBLE,"
				+ "latitude7 DOUBLE,"
				+ "longitude8 DOUBLE,"
				+ "latitude8 DOUBLE,"
				+ "longitude9 DOUBLE,"
				+ "latitude9 DOUBLE,"
				+ "longitude10 DOUBLE,"
				+ "latitude10 DOUBLE,"
				+ "isSafe INTEGER NOT NULL"  // 是否安全，如果安全则为 1 ，否则为 0
				+ ");";
		db.execSQL(sql_5);
		
		System.out.println("*********************** 创建：onCreate().");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql_1 = "DROP TABLE IF EXISTS " + Temperature + ";";
		db.execSQL(sql_1);  // 执行SQL
		String sql_2 = "DROP TABLE IF EXISTS " + Pressure + ";";
		db.execSQL(sql_2);  // 执行SQL
		String sql_3 = "DROP TABLE IF EXISTS " + HEARTRATE + ";";
		db.execSQL(sql_3);  // 执行SQL
		
		String sql_4 = "DROP TABLE IF EXISTS " + POSITION + ";";
		db.execSQL(sql_4);  // 执行SQL
		
		String sql_5 = "DROP TABLE IF EXISTS " + SAFEDANGER + ";";
		db.execSQL(sql_5);  // 执行SQL
		
		System.out.println("*********************** 更新：onUpgrade().");
		this.onCreate(db);
	}

}
