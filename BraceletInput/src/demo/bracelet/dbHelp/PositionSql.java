package demo.bracelet.dbHelp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import com.mysql.jdbc.PreparedStatement;
import demo.bracelet.DBdomain.PositionData;

public class PositionSql {
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public void PositionInsert(PositionData data) {
		Connection dbConn = null;
		PreparedStatement dbst = null;
		ResultSet dbrs = null;
		String sql = "insert into position (braceletid, longitude, latitude, place, day) values ( ?, ?, ?, ?, ?)";
		try {
			dbConn = (Connection) Driverjdbc.getConnection();
			dbst = (PreparedStatement) dbConn.prepareStatement(sql);
			dbst.setString(1, data.getBraceletId());
			dbst.setDouble(2, data.getLongitude());
			dbst.setDouble(3, data.getLatitude());
			dbst.setString(4, data.getPlace());
			System.out.println(data.getPlace());
			dbst.setString(5, format.format(data.getDay()) );
			int rows = dbst.executeUpdate();
			if (rows > 0) {
				System.out.println("PositionInsert Operate Successfully!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Driverjdbc.free(dbrs, dbst, dbConn);
		}
	}

	public List<PositionData> PositionSelect(String BraceletId) {
		Connection dbConn = null;
		PreparedStatement dbst = null;
		ResultSet dbrs = null;
		List<PositionData> listData = null;
		String sql = "select * from position where braceletid = ?";
		try {
			dbConn = (Connection) Driverjdbc.getConnection();
			dbst = (PreparedStatement) dbConn.prepareStatement(sql);
			dbst.setString(1, BraceletId);
			dbrs = dbst.executeQuery();
			listData = new ArrayList<PositionData>();
			while (dbrs.next()) {
				PositionData data = new PositionData();
				data.setBraceletId(dbrs.getString("braceletid"));
				data.setLongitude(dbrs.getDouble("longitude"));
				data.setLatitude(dbrs.getDouble("latitude"));
				data.setPlace(dbrs.getString("place"));
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
