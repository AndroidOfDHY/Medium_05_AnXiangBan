package demo.bracelet.dbHelp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import com.mysql.jdbc.PreparedStatement;
import demo.bracelet.DBdomain.HealthData;

public class HealthSql {
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public void HealthInsert(String braceletid,HealthData data) {
		Connection dbConn = null;
		PreparedStatement dbst = null;
		ResultSet dbrs = null;
		String sql = "insert into health (braceletid, bodytemperature, highbloodpressure, lowbloodpressure, heartrate,day) values (?, ?, ?, ?, ?, ?)";
		try {
			dbConn = (Connection) Driverjdbc.getConnection();
			dbst = (PreparedStatement) dbConn.prepareStatement(sql);
			dbst.setString(1, braceletid);
			dbst.setFloat(2, data.getBodyTemperature());
			dbst.setFloat(3, data.getHighBloodPressure());
			dbst.setFloat(4, data.getLowBloodPressure());
			dbst.setInt(5, data.getHeartRate());
			dbst.setString(6, format.format(data.getDay()));
			int rows = dbst.executeUpdate();
			if (rows > 0) {
				System.out.println("Health Operate Successfully!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Driverjdbc.free(dbrs, dbst, dbConn);
		}
	}

	public List<HealthData> HealthSelect(String BraceletId) {
		Connection dbConn = null;
		PreparedStatement dbst = null;
		ResultSet dbrs = null;
		List<HealthData> listData = null;
		String sql = "select * from health where braceletid = ?";
		try {
			dbConn = (Connection) Driverjdbc.getConnection();
			dbst = (PreparedStatement) dbConn.prepareStatement(sql);
			dbst.setString(1, BraceletId);
			dbrs = dbst.executeQuery();
			listData = new ArrayList<HealthData>();
			while (dbrs.next()) {
				HealthData data = new HealthData();
				data.setBodyTemperature(dbrs.getFloat("bodytemperature"));
				data.setHighBloodPressure(dbrs.getFloat("highbloodpressure"));
				data.setLowBloodPressure(dbrs.getFloat("lowbloodpressure"));
				data.setHeartRate(dbrs.getInt("heartrate"));
				data.setDay(format.parse(dbrs.getString("day")));
				listData.add(data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			Driverjdbc.free(dbrs, dbst, dbConn);
		}
		return listData;
	}
}
