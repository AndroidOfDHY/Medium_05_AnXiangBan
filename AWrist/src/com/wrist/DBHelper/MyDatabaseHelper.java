package com.wrist.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASENAME = "axb_growth.db";
	private static final int DATABASEVERSION = 1;  // �������ݿ�İ汾
	private static final String Temperature = "BodyTemperature";  // ���±���
	private static final String Pressure = "BloodPressure";  // Ѫѹ����
	private static final String HEARTRATE = "HeartRate";  // ���ʱ���
	private static final String POSITION = "Position";  // ����λ�����ݿ����
	private static final String SAFEDANGER = "SafeDanger";  // ��ȫΣ����

	public MyDatabaseHelper(Context context) {  // �û�����ĵ�Ҳ�϶�ֻ�� Context
		super(context, DATABASENAME, null, DATABASEVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {  // �������ݱ�
		String sql_1 = "CREATE TABLE " + Temperature + "(" 
				+ "id INTEGER PRIMARY KEY,"  // �� SQLite ������ Integer��PRIMARY KEY �� ID �Զ�����
				+ "bodytemperature FLOAT NOT NULL,"
				+ "day VARCHAR(25) NOT NULL"
				+ ");";
		db.execSQL(sql_1);  // ִ��SQL
		
		String sql_2 = "CREATE TABLE " + Pressure + "(" 
				+ "id INTEGER PRIMARY KEY,"  // �� SQLite ������ Integer��PRIMARY KEY �� ID �Զ�����
				+ "highbloodpressure FLOAT NOT NULL,"
				+ "lowbloodpressure FLOAT NOT NULL,"
				+ "day VARCHAR(25) NOT NULL"
				+ ");";
		db.execSQL(sql_2);  // ִ��SQL
		
		String sql_3 = "CREATE TABLE " + HEARTRATE + "(" 
				+ "id INTEGER PRIMARY KEY,"  // �� SQLite ������ Integer��PRIMARY KEY �� ID �Զ�����
				+ "heartrate SHORT NOT NULL,"
				+ "day VARCHAR(25) NOT NULL"
				+ ");";
		db.execSQL(sql_3);  // ִ��SQL
		
		String sql_4 = "CREATE TABLE " + POSITION + "("
				+ "id INTEGER PRIMARY KEY,"  // �� SQLite ������ Integer��PRIMARY KEY �� ID �Զ�����
				+ "braceletid VARCHAR(25) NOT NULL,"  // �ֻ�ID
				+ "longitude DOUBLE NOT NULL,"  // ����
				+ "latitude DOUBLE NOT NULL,"  // γ��
				+ "place VARCHAR(50) NOT NULL,"  // ��ַ
				+ "day time NOT NULL"  // ʱ��
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
				+ "isSafe INTEGER NOT NULL"  // �Ƿ�ȫ�������ȫ��Ϊ 1 ������Ϊ 0
				+ ");";
		db.execSQL(sql_5);
		
		System.out.println("*********************** ������onCreate().");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql_1 = "DROP TABLE IF EXISTS " + Temperature + ";";
		db.execSQL(sql_1);  // ִ��SQL
		String sql_2 = "DROP TABLE IF EXISTS " + Pressure + ";";
		db.execSQL(sql_2);  // ִ��SQL
		String sql_3 = "DROP TABLE IF EXISTS " + HEARTRATE + ";";
		db.execSQL(sql_3);  // ִ��SQL
		
		String sql_4 = "DROP TABLE IF EXISTS " + POSITION + ";";
		db.execSQL(sql_4);  // ִ��SQL
		
		String sql_5 = "DROP TABLE IF EXISTS " + SAFEDANGER + ";";
		db.execSQL(sql_5);  // ִ��SQL
		
		System.out.println("*********************** ���£�onUpgrade().");
		this.onCreate(db);
	}

}
