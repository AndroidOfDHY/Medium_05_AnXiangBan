package com.wrist.myapplication;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import test.RandomUtil;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.wrist.DBHelper.MyDatabaseHelper;
import com.wrist.DBHelper.domain.HealthData;
import com.wrist.DBHelper.domain.PositionData;
import com.wrist.DBHelper.domain.SafeDangerData;
import com.wrist.thread.ClienIn;
import domain.GPS;
import Message.DefaultMessage;
import Message.MessageType;
import PhoneMes.SimCardMes;
import Util.DistanceUtil;
import Util.FileUtil;
import Util.GraphUtil;
import Util.jsonUtil;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class MyApplication extends Application {
	public SQLiteOpenHelper dbHelper;
	public static MyApplication app;
	public Socket socket;
	public ClienIn thread = null;
	private final Timer timer = new Timer();
	private TimerTask healthTask;
	private TimerTask dianTask;
	private Boolean Dflag = false;// 发送过为true 否为false
	public List<GPS> safeList = new ArrayList<GPS>();
	public List<GPS> dangerList = new ArrayList<GPS>();
	private int safeflag = -1;
	public SharedPreferences preferences;
	public double homeLat = -1;
	public double homeLon = -1;
	public boolean openHome = false;
	MKSearch mSearch = null;
	public BMapManager mBMapManager = null;
	public static final String strKey = "IxstuQQ6fLZancjje8kWYgjt";
	
	@SuppressLint("NewApi")
	public void onCreate() {
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		super.onCreate();
		app = this;
		dbHelper = new MyDatabaseHelper(this);
		initBaiduMap(this);
		initSharedPreferences();
		if(preferences.getString("openHome", "No").equals("No")){
			homeLat = preferences.getInt("HomeLat", -1);
			homeLon = preferences.getInt("HomeLon", -1);
			homeLat = homeLat / 1E6;
			homeLon = homeLon / 1E6;
			openHome = false;
		}
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		mLocationClient.setAK("WOWuz9LlgpjFkrbVbEugXzFu");
		setOption();
		startLocation();
		dbHelper = new MyDatabaseHelper(this);
		FileUtil.initFile();
		openHealthThread();
		openDianThread();
		mSearch = new MKSearch();
		mSearch.init(mBMapManager, new MKSearchListener() {
			@Override
			public void onGetPoiDetailSearchResult(int type, int error) {
			}

			@SuppressLint("DefaultLocale")
			public void onGetAddrResult(MKAddrInfo res, int error) {
				if (error != 0) {
					String str = String.format("错误号：%d", error);
					System.out.println("++++++++++++++++++" + str);
					return;
				}
				if (res.type == MKAddrInfo.MK_REVERSEGEOCODE) {
					// 反地理编码：通过坐标点检索详细地址及周边poi
					String strInfo = res.strAddr;
					PositionData positionData = new PositionData();
					positionData.setLatitude(((double)res.geoPt.getLatitudeE6()) / 1E6);
					positionData.setLongitude(((double)res.geoPt.getLatitudeE6()) / 1E6);
					positionData.setPlace(strInfo);
					positionData.setDay(new Date());
					positionData.setBraceletId(getIMSI());
					
					DefaultMessage message = new DefaultMessage(
							MessageType.MESSAGE_RETURN_GPS,
							jsonUtil.createJsonString(positionData));
					Toast.makeText(app, "gps"+strInfo, Toast.LENGTH_SHORT).show();
					if(thread != null){System.out.println("YES");
						thread.sendMessage(message);
					}else{
						System.out.println("No");
					}
				}
			}

			public void onGetPoiResult(MKPoiResult res, int type, int error) {

			}

			public void onGetDrivingRouteResult(MKDrivingRouteResult res,
					int error) {
			}

			public void onGetTransitRouteResult(MKTransitRouteResult res,
					int error) {
			}

			public void onGetWalkingRouteResult(MKWalkingRouteResult res,
					int error) {
			}

			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			}

			@Override
			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
			}

			@Override
			public void onGetShareUrlResult(MKShareUrlResult result, int type,
					int error) {
			}

		});
	}
	
	public String getIMSI() {
		SimCardMes siminfo = new SimCardMes(getApplicationContext());
		String IMSI = siminfo.getProvidersName();
		return IMSI;
//		return "460017405312024";
	}
	
	private void initSharedPreferences() {

		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		if (!preferences.contains("lock_pwd")) {
			Editor editor = preferences.edit();
			editor.putString("openHome", "No");
			editor.putInt("HomeLat", -1);
			editor.putInt("HomeLon", -1);
			editor.commit();

		}

	}
	
	public void setSafeDanger(SafeDangerData safeDangerData){
		if(safeDangerData.getIsSafe() == 0){
			dangerList = safeToGPS(safeDangerData);
		}else {
			safeList = safeToGPS(safeDangerData);
		}
	}
	private List<GPS> safeToGPS(SafeDangerData safeDangerData){
		List<GPS> list = new ArrayList<GPS>();
		if (safeDangerData.getLongitude1() != 0 ) {
			list.add(new GPS(safeDangerData.getLatitude1(),safeDangerData.getLongitude1()));
		}
		if (safeDangerData.getLongitude2() != 0) {
			list.add(new GPS(safeDangerData.getLatitude2(),safeDangerData.getLongitude2()));
		}
		if (safeDangerData.getLongitude3() != 0) {
			list.add(new GPS(safeDangerData.getLatitude3(),safeDangerData.getLongitude3()));
		}
		if (safeDangerData.getLongitude4() != 0) {
			list.add(new GPS(safeDangerData.getLatitude4(),safeDangerData.getLongitude4()));
		}
		if (safeDangerData.getLongitude5() != 0) {
			list.add(new GPS(safeDangerData.getLatitude5(),safeDangerData.getLongitude5()));
		}
		if (safeDangerData.getLongitude6() != 0) {
			list.add(new GPS(safeDangerData.getLatitude6(),safeDangerData.getLongitude6()));
		}
		if (safeDangerData.getLongitude7() != 0) {
			list.add(new GPS(safeDangerData.getLatitude7(),safeDangerData.getLongitude7()));
		}
		if (safeDangerData.getLongitude8() != 0) {
			list.add(new GPS(safeDangerData.getLatitude8(),safeDangerData.getLongitude8()));
		}
		if (safeDangerData.getLongitude9() != 0) {
			list.add(new GPS(safeDangerData.getLatitude9(),safeDangerData.getLongitude9()));
		}
		if (safeDangerData.getLongitude10() != 0) {
			list.add(new GPS(safeDangerData.getLatitude10(),safeDangerData.getLongitude10()));
		}
		return list;
	}

	public void initSocket() {
		try {
			socket = new Socket("125.65.110.148", 11111);
			thread = new ClienIn(socket, this);
			thread.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void openHealthThread() {
		healthTask = new TimerTask() {
			@Override
			public void run() {
				HealthData healthData = new HealthData();
				healthData.setDay(new Date());
				healthData.setBodyTemperature(RandomUtil.getTW());
				healthData.setHeartRate(RandomUtil.getXL());
				healthData.setHighBloodPressure(RandomUtil.getSSY());
				healthData.setLowBloodPressure(RandomUtil.getSZY());
//				if(healthData.getDay().getHours() == 6 || healthData.getDay().getHours() == 21){
					
					DefaultMessage message = new DefaultMessage(
							MessageType.MESSAGE_RETURN_HEALTH,
							jsonUtil.createJsonString(healthData));
					if(thread != null){
						thread.sendMessage(message);
					}
//				}
				if(healthData.getBodyTemperature()>38.5){
					DefaultMessage message2 = new DefaultMessage(MessageType.MESSAGE_WARN,"HEAT");
					if(thread != null){
						thread.sendMessage(message2);
					}
				}
			}
		};
//		 timer.schedule(healthTask, 10000, 30*1000);
		timer.schedule(healthTask, 25 *1000, 30*1000);
	}

	private void openDianThread() {
		dianTask = new TimerTask() {
			@Override
			public void run() {
				BatteryInfoBroadcastReceiver receiver = new BatteryInfoBroadcastReceiver();
				IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
				app.registerReceiver(receiver, filter);
			}
		};
//		timer.schedule(dianTask, 2000, 2 * 1000);
		timer.schedule(dianTask, 60 *2000, 60 * 1000);
	}

	public class BatteryInfoBroadcastReceiver extends BroadcastReceiver {
		private int level = 0;
		private int scale = 0;
		private int result = 0;

		@Override
		public void onReceive(Context context, Intent intent) {
			if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
				this.level = intent.getIntExtra("level", 0);
				this.scale = intent.getIntExtra("scale", 100);
				this.result = level * 100 / scale;
				if (result <= 60 && !Dflag) {
					DefaultMessage message = new DefaultMessage(
							MessageType.MESSAGE_WARN, "ELECTRIC");
					if(thread != null){
						thread.sendMessage(message);
					}
					Dflag = true;
				} else if (result > 60 && !Dflag) {
					Dflag = false;
				}
			}
		}

	}

	public BDLocationListener myListener = new MyLocationListener();
	public LocationClient mLocationClient = null;

	public void setOption() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setPriority(LocationClientOption.GpsFirst);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(20*1000);// 设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);// 禁止启用缓存定位
		option.setPoiNumber(5); // 最多返回POI个数
		option.setPoiDistance(1000); // poi查询距离
		option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
		mLocationClient.setLocOption(option);
	}

	public void startLocation() {
		mLocationClient.start();
		if (mLocationClient == null)
			System.out.println("locClient is null ");
		else if (mLocationClient.isStarted()) {
			System.out.println("locClient is not started");
		} else {
			System.out.println("1111");
			mLocationClient.requestLocation();
		}
	}

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				return;
			}
			PositionData positionData = new PositionData();
			positionData.setLatitude(location.getLatitude());
			positionData.setLongitude(location.getLongitude());
			positionData.setPlace(location.getAddrStr());
			positionData.setDay(new Date());
			positionData.setBraceletId(getIMSI());
			int temp = GraphUtil.isSafe(new GPS(location.getLatitude(),location.getLongitude()), safeList, dangerList);
			if(temp == -1 && safeflag != -1){
				//进入危险区
				System.out.println("进入危险区");
				DefaultMessage message = new DefaultMessage();
				message.setFlag(MessageType.MESSAGE_WARN);
				message.setContext("DANGER");
				if(thread != null){
					thread.sendMessage(message);
				}
			}else if(temp != -1 && safeflag == -1){
				//离开危险区
				System.out.println("离开危险区");
			}else if (temp != 1 && safeflag == 1) {
				//离开安全区
				System.out.println("离开安全区");
				DefaultMessage message = new DefaultMessage();
				message.setFlag(MessageType.MESSAGE_WARN);
				message.setContext("EARLY");
				if(thread != null){
					thread.sendMessage(message);
				}
			}
			safeflag = temp;
			System.out.println(openHome);
			if(openHome){
				Double dis = DistanceUtil.GetShortDistance(location.getLongitude(), location.getLatitude(), homeLon, homeLat);
				if(dis < 5000){
					//到家了
					System.out.println("到家了");
					DefaultMessage message = new DefaultMessage(MessageType.MESSAGE_RETURN_HOME,"");
					if(thread != null){
						thread.sendMessage(message);
					}
				}
				System.out.println(dis);
			}
			if(location.getAddrStr()==null){
				mSearch.reverseGeocode(new GeoPoint((int)(location.getLatitude()* 1E6), (int) (location.getLongitude()* 1E6)));
			}else{
				DefaultMessage message = new DefaultMessage(
						MessageType.MESSAGE_RETURN_GPS,
						jsonUtil.createJsonString(positionData));

				Toast.makeText(app, location.getAddrStr(), Toast.LENGTH_SHORT).show();
				if(thread != null){
					thread.sendMessage(message);
				}
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
			System.out.println("33");
		}
	}
	public void destroy() {
		if (mBMapManager != null) {
			mBMapManager.destroy();
			mBMapManager = null;
		}
	}

	private void initBaiduMap(Context context) {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(context);
		}

		if (!mBMapManager.init(strKey, new MyGeneralListener())) {
			Toast.makeText(app.getApplicationContext(),
					"BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
		}
	}
	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
		public static class MyGeneralListener implements MKGeneralListener {

			@Override
			public void onGetNetworkState(int iError) {
				if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
					Toast.makeText(
							app.getApplicationContext(),
							"您的网络出错啦！", Toast.LENGTH_LONG).show();
				}
				// ...
			}

			@Override
			public void onGetPermissionState(int iError) {
				// 非零值表示key验证未通过
				if (iError != 0) {
					Toast.makeText(
							app.getApplicationContext(),
							"keybad", Toast.LENGTH_LONG).show();
				}
			}
		}
}
