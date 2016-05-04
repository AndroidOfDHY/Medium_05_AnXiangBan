package demo.bracelet.dbHelp;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import demo.bracelet.DBdomain.SafeDangerData;

public class SafeDangerSql {
	public void SafeDangerInert(SafeDangerData data) {
		Connection dbConn = null;
		PreparedStatement dbst = null;
		ResultSet dbrs = null;
		int num = 0;
		String sql = "insert into safedanger(braceletid, " + "longitude1, latitude1, "
				+ "longitude2, latitude2, " + "longitude3, latitude3, "
				+ "longitude4, latitude4, " + "longitude5, latitude5, "
				+ "longitude6, latitude6, " + "longitude7, latitude7, "
				+ "longitude8, latitude8, " + "longitude9, latitude9, "
				+ "longitude10, latitude10, "
				+ "isSafe) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			dbConn = (Connection) Driverjdbc.getConnection();
			dbst = (PreparedStatement) dbConn.prepareStatement(sql);
			dbst.setString(1, data.getBraceletId());
			dbst.setDouble(2, data.getLongitude1());
			dbst.setDouble(3, data.getLatitude1());
			dbst.setDouble(4, data.getLongitude2());
			dbst.setDouble(5, data.getLatitude2());
			dbst.setDouble(6, data.getLongitude3());
			dbst.setDouble(7, data.getLatitude3());
			dbst.setDouble(8, data.getLongitude4());
			dbst.setDouble(9, data.getLatitude4());
			dbst.setDouble(10, data.getLongitude5());
			dbst.setDouble(11, data.getLatitude5());
			dbst.setDouble(12, data.getLongitude6());
			dbst.setDouble(13, data.getLatitude6());
			dbst.setDouble(14, data.getLongitude7());
			dbst.setDouble(15, data.getLatitude7());
			dbst.setDouble(16, data.getLongitude8());
			dbst.setDouble(17, data.getLatitude8());
			dbst.setDouble(18, data.getLongitude9());
			dbst.setDouble(19, data.getLatitude9());
			dbst.setDouble(20, data.getLongitude10());
			dbst.setDouble(21, data.getLatitude10());
			dbst.setInt(22, data.getIsSafe());
			num = dbst.executeUpdate();
			if (num > 0) {
				System.out.println("PhoneInert Operate Successfully!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Driverjdbc.free(dbrs, dbst, dbConn);
		}
	}

	public SafeDangerData SafeDangerSelect(int isSafe,String braceletid) {
		Connection dbConn = null;
		PreparedStatement dbst = null;
		ResultSet dbrs = null;
		SafeDangerData data = new SafeDangerData();
		String sql = "select * from safedanger where isSafe = ? and braceletid = ?";
		try {
			dbConn = (Connection) Driverjdbc.getConnection();
			dbst = (PreparedStatement) dbConn.prepareStatement(sql);
			dbst.setInt(1, isSafe);
			dbst.setString(2, braceletid);
			dbrs = dbst.executeQuery();
			if (dbrs.next()) {
				data = new SafeDangerData();
				data.setLongitude1(dbrs.getDouble("longitude1"));
				data.setLatitude1(dbrs.getDouble("latitude1"));
				data.setLongitude2(dbrs.getDouble("longitude2"));
				data.setLatitude2(dbrs.getDouble("latitude2"));
				data.setLongitude3(dbrs.getDouble("longitude3"));
				data.setLatitude3(dbrs.getDouble("latitude3"));
				data.setLongitude4(dbrs.getDouble("longitude4"));
				data.setLatitude4(dbrs.getDouble("latitude4"));
				data.setLongitude5(dbrs.getDouble("longitude5"));
				data.setLatitude5(dbrs.getDouble("latitude5"));
				data.setLongitude6(dbrs.getDouble("longitude6"));
				data.setLatitude6(dbrs.getDouble("latitude6"));
				data.setLongitude7(dbrs.getDouble("longitude7"));
				data.setLatitude7(dbrs.getDouble("latitude7"));
				data.setLongitude8(dbrs.getDouble("longitude8"));
				data.setLatitude8(dbrs.getDouble("latitude8"));
				data.setLongitude9(dbrs.getDouble("longitude9"));
				data.setLatitude9(dbrs.getDouble("latitude9"));
				data.setLongitude10(dbrs.getDouble("longitude10"));
				data.setLatitude10(dbrs.getDouble("latitude10"));
				data.setIsSafe(isSafe);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Driverjdbc.free(dbrs, dbst, dbConn);
		}
		return data;
	}

	public void SafeDangerUpdate(SafeDangerData data) {
		Connection dbConn = null;
		PreparedStatement dbst = null;
		ResultSet dbrs = null;
		int num = 0;
		String sql = "update safedanger set "
				+ "longitude1 = ?, latitude1 = ?,"
				+ "longitude2 = ?, latitude2 = ?,"
				+ "longitude3 = ?, latitude3 = ?,"
				+ "longitude4 = ?, latitude4 = ?,"
				+ "longitude5 = ?, latitude5 = ?,"
				+ "longitude6 = ?, latitude6 = ?,"
				+ "longitude7 = ?, latitude7 = ?,"
				+ "longitude8 = ?, latitude8 = ?,"
				+ "longitude9 = ?, latitude9 = ?,"
				+ "longitude10 = ?, latitude10 = ?" 
				+ " where isSafe = ? and braceletid = ?";
		try {
			dbConn = (Connection) Driverjdbc.getConnection();
			dbst = (PreparedStatement) dbConn.prepareStatement(sql);
			dbst.setDouble(1, data.getLongitude1());
			dbst.setDouble(2, data.getLatitude1());
			dbst.setDouble(3, data.getLongitude2());
			dbst.setDouble(4, data.getLatitude2());
			dbst.setDouble(5, data.getLongitude3());
			dbst.setDouble(6, data.getLatitude3());
			dbst.setDouble(7, data.getLongitude4());
			dbst.setDouble(8, data.getLatitude4());
			dbst.setDouble(9, data.getLongitude5());
			dbst.setDouble(10, data.getLatitude5());
			dbst.setDouble(11, data.getLongitude6());
			dbst.setDouble(12, data.getLatitude6());
			dbst.setDouble(13, data.getLongitude7());
			dbst.setDouble(14, data.getLatitude7());
			dbst.setDouble(15, data.getLongitude8());
			dbst.setDouble(16, data.getLatitude8());
			dbst.setDouble(17, data.getLongitude9());
			dbst.setDouble(18, data.getLatitude9());
			dbst.setDouble(19, data.getLongitude10());
			dbst.setDouble(20, data.getLatitude10());
			dbst.setInt(21, data.getIsSafe());
			dbst.setString(22, data.getBraceletId());
			num = dbst.executeUpdate();
			if (num > 0) {
				System.out.println("SafeDanger Update Successfully!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Driverjdbc.free(dbrs, dbst, dbConn);
		}
	}
	public boolean isExistSafeDanger(SafeDangerData data) {
		Connection dbConn = null;
		PreparedStatement dbst = null;
		ResultSet dbrs = null;
		String sql = "select count(*) from safedanger where isSafe = ? and braceletid =?";
		boolean flag = false;
		try {
			dbConn =  (Connection) Driverjdbc.getConnection();
			dbst = (PreparedStatement) dbConn.prepareStatement(sql);
			dbst.setInt(1, data.getIsSafe());
			dbst.setString(2, data.getBraceletId());
			dbrs = (ResultSet) dbst.executeQuery();
			dbrs.next();
			int count = dbrs.getInt(1);
			if(count == 1){
				flag = true;
			}else{
				flag = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Driverjdbc.free(dbrs, dbst, dbConn);
		}
		return flag;	
	}
	
}
