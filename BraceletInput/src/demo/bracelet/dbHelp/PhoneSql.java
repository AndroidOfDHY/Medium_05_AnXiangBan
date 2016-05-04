package demo.bracelet.dbHelp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import demo.bracelet.DBdomain.PhoneData;

public class PhoneSql {
	public boolean IsExist(String IMSI){
		Connection dbConn = null;
		PreparedStatement dbst = null;
		ResultSet dbrs = null;
		String sql = "select * from phoneclient where IMSI = ?";
		boolean flag = false;
		try {
			dbConn =  (Connection) Driverjdbc.getConnection();
			dbst = (PreparedStatement) dbConn.prepareStatement(sql);
			dbst.setString(1, IMSI);
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
	public boolean PhoneUpdate(String IMSI,String braceletid){
		Connection dbConn = null;
		PreparedStatement dbst = null;
		ResultSet dbrs = null;
		String sql = "update phoneclient set braceletid = ? where IMSI = ?";
		boolean flag = false;
		try {
			dbConn =  (Connection) Driverjdbc.getConnection();
			dbst = (PreparedStatement) dbConn.prepareStatement(sql);
			dbst.setString(1, braceletid);
			dbst.setString(2, IMSI);
			int num =  dbst.executeUpdate();
			if( num == 1 ){
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
	public void PhoneInert(PhoneData data){
		Connection dbConn = null;
		PreparedStatement dbst = null;
		ResultSet dbrs = null;
		String sql = "insert into phoneclient (IMSI,braceletid) values (?,?)";
		int num = 0;
		try {
			dbConn =  (Connection) Driverjdbc.getConnection();
			dbst = (PreparedStatement) dbConn.prepareStatement(sql);
			dbst.setString(1, data.getIMSI());
			dbst.setString(2, data.getBraceletid());
			num =  dbst.executeUpdate();
			if (num > 0) {
				System.out.println("PhoneInert Operate Successfully!");
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			Driverjdbc.free(dbrs, dbst, dbConn);
		}
	}
	public List<PhoneData> PhoneFindAll(){
		Connection dbConn = null;
		PreparedStatement dbst = null;
		ResultSet dbrs = null;
		String sql = "select * from phoneclient";
		List<PhoneData> list = null;
		try {
			dbConn =  (Connection) Driverjdbc.getConnection();
			dbst = (PreparedStatement) dbConn.prepareStatement(sql);
			dbrs = dbst.executeQuery();
			list = new ArrayList<PhoneData>();
			while (dbrs.next()) {
				PhoneData data = new PhoneData();
				data.setIMSI(dbrs.getString("IMSI"));
				data.setBraceletid(dbrs.getString("braceletid"));
				list.add(data);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			Driverjdbc.free(dbrs, dbst, dbConn);
		}
		return list;
	}
	public List<String> PhoneFindIMSI(String id){
		Connection dbConn = null;
		PreparedStatement dbst = null;
		ResultSet dbrs = null;
		String sql = "select IMSI from phoneclient where braceletid = ?";
		List<String> list = null;
		try {
			dbConn =  (Connection) Driverjdbc.getConnection();
			dbst = (PreparedStatement) dbConn.prepareStatement(sql);
			dbst.setString(1, id);
			dbrs = dbst.executeQuery();
			list = new ArrayList<String>();
			while (dbrs.next()) {
				list.add(dbrs.getString("IMSI"));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			Driverjdbc.free(dbrs, dbst, dbConn);
		}
		return list;
	}
	public String PhoneFindID(String IMSI){
		Connection dbConn = null;
		PreparedStatement dbst = null;
		ResultSet dbrs = null;
		String sql = "select braceletid from phoneclient where IMSI = ?";
		String ID = null;
		try {
			dbConn =  (Connection) Driverjdbc.getConnection();
			dbst = (PreparedStatement) dbConn.prepareStatement(sql);
			dbst.setString(1, IMSI);
			dbrs = dbst.executeQuery();
			if (dbrs.next()) {
				ID = dbrs.getString("braceletid");
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			Driverjdbc.free(dbrs, dbst, dbConn);
		}
		return ID;
	}
}
