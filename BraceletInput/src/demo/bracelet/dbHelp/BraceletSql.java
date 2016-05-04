package demo.bracelet.dbHelp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.sql.ResultSet;
import demo.bracelet.DBdomain.BraceletData;

public class BraceletSql {
	public void BraceletInert(BraceletData data){
		Connection dbConn = null;
		PreparedStatement dbst = null;
		ResultSet dbrs = null;
		String sql = "insert into bracelet values (?,?)";
		int num = 0;
		try {
			dbConn =  (Connection) Driverjdbc.getConnection();
			dbst = (PreparedStatement) dbConn.prepareStatement(sql);
			dbst.setString(1, data.getBraceletId());
			dbst.setString(2, data.getCaptcha());
			num =  dbst.executeUpdate();
			if (num > 0) {
				System.out.println("Bracelet  Successfully!");
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			Driverjdbc.free(dbrs, dbst, dbConn);
		}
	}
	public List<BraceletData> BraceletFindAll(){
		Connection dbConn = null;
		PreparedStatement dbst = null;
		ResultSet dbrs = null;
		String sql = "select * from bracelet";
		List<BraceletData> list = null;
		try {
			dbConn =  (Connection) Driverjdbc.getConnection();
			dbst = (PreparedStatement) dbConn.prepareStatement(sql);
			dbrs = dbst.executeQuery();
			list = new ArrayList<BraceletData>();
			while (dbrs.next()) {
				BraceletData data = new BraceletData();
				data.setBraceletId(dbrs.getString("braceletid"));
				data.setCaptcha(dbrs.getString("captcha"));
				list.add(data);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			Driverjdbc.free(dbrs, dbst, dbConn);
		}
		return list;
	}
	public boolean IsExist(BraceletData data){
		Connection dbConn = null;
		PreparedStatement dbst = null;
		ResultSet dbrs = null;
		String sql = "select * from bracelet where braceletid = ?";
		boolean flag = false;
		try {
			dbConn =  (Connection) Driverjdbc.getConnection();
			dbst = (PreparedStatement) dbConn.prepareStatement(sql);
			dbst.setString(1, data.getBraceletId());
			dbrs = (ResultSet) dbst.executeQuery();
			if( dbrs.next() ){
				flag = true;
			}else{
				flag = false;
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			Driverjdbc.free(dbrs, dbst, dbConn);
		}
		return flag;		
	}
	public String BraceletFindCaptcha(String ID){
		Connection dbConn = null;
		PreparedStatement dbst = null;
		ResultSet dbrs = null;
		String sql = "select captcha from bracelet where braceletid = ?";
		String captcha = null;
		try {
			dbConn =  (Connection) Driverjdbc.getConnection();
			dbst = (PreparedStatement) dbConn.prepareStatement(sql);
			dbst.setString(1, ID);
			dbrs = dbst.executeQuery();
			if (dbrs.next()) {
				captcha = dbrs.getString("captcha");
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			Driverjdbc.free(dbrs, dbst, dbConn);
		}
		return captcha;
	}
}
