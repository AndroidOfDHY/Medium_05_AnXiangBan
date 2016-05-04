package com.anxiangban.DBHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import com.anxiangban.DBHelper.domain.HealthData;
import com.anxiangban.DBHelper.domain.PositionData;
import com.anxiangban.DBHelper.domain.SafeDangerData;
import com.anxiangban.myApplication.MyApplication;
import domain.All;

public class InsertHelp {
	private MyApplication app;
	public InsertHelp(MyApplication app){
		this.app = app;
	}
	public void HealthInsert(HealthData healthData){
		int size = 0;
		try {
			MytabCursor mtabReader = new MytabCursor(
					app.dbHelper.getReadableDatabase());
			List<HealthData> list= new ArrayList<HealthData>();
			list = mtabReader.BodyTemperatureFind();
			size = list.size();
			mtabReader.Stop();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		MytabOperate mytabOperate = new MytabOperate(
				app.dbHelper.getWritableDatabase());
		while (size >= 14) {
			mytabOperate.BloodPressureDelete(0);
			mytabOperate.BodyTemperatureDelete(0);
			mytabOperate.HeartRateDelete(0);
			mytabOperate.BodyTemperatureUpdate();
			mytabOperate.BloodPressureUpdate();
			mytabOperate.HeartRateInsertUpdate();
			size--;
		}
		mytabOperate.BodyTemperatureInsert(
				healthData.getBodyTemperature(), healthData.getDay());
		mytabOperate.BloodPressureInsert(
				healthData.getHighBloodPressure(),
				healthData.getLowBloodPressure(), healthData.getDay());
		mytabOperate.HeartRateInsert((short) healthData.getHeartRate(),
				healthData.getDay());
		mytabOperate.Stop();
	}
	public void PositionInsert(PositionData positionData){
		if(positionData.getPlace() != null){
			MytabOperate mytabOperate = new MytabOperate(
					app.dbHelper.getWritableDatabase());
			mytabOperate.PositionInsert(positionData);
			mytabOperate.Stop();
		}
	}
	public void SafeDangerInsert(SafeDangerData safeData){
		MytabCursor mytabCursor = new MytabCursor(
				app.dbHelper.getReadableDatabase());
		int count = mytabCursor.SafeDangerFindCount(safeData.getIsSafe());
		mytabCursor.Stop();
		if (count == 0) {
			MytabOperate mytabOperate = new MytabOperate(
					app.dbHelper.getWritableDatabase());
			mytabOperate.SafeDangerInsert(safeData);
			mytabOperate.Stop();
		} else if (count == 1) {
			MytabOperate mytabOperate = new MytabOperate(
					app.dbHelper.getWritableDatabase());
			mytabOperate.SafeDangerUpdate(safeData);
			mytabOperate.Stop();
		}
	}

	public void AllInsert(All all){
		SafeDangerInsert(all.getDanger());
		SafeDangerInsert(all.getSafe());
		List<PositionData> positionDatas = all.getPositionDatas();
		List<HealthData> healthDatas = all.getHealthDatas();
		for(PositionData positionData : positionDatas){
			PositionInsert(positionData);
		}
		for(HealthData healthData : healthDatas){
			HealthInsert(healthData);
		}
	}
}
