package com.anxiangban.myApplication;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import com.anxiangban.DBHelper.MyDatabaseHelper;
import com.anxiangban.thread.ClienIn;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import domain.Sign;
import Message.DefaultMessage;
import Message.MessageType;
import PhoneMes.SimCardMes;
import Util.FileUtil;
import Util.jsonUtil;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class MyApplication extends Application {
	public static final String strKey = "IxstuQQ6fLZancjje8kWYgjt";
	public boolean m_bKeyRight = true;
	public BMapManager mBMapManager = null;
	private static MyApplication mInstance = null;
	public SharedPreferences preferences;
	public SQLiteOpenHelper dbHelper;
	public Socket socket;
	public ClienIn thread;

	@SuppressLint("NewApi")
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		initBaiduMap(this);
		initSharedPreferences();
		dbHelper = new MyDatabaseHelper(this);
		FileUtil.initFile();
	}

	public String getIMSI() {
		SimCardMes siminfo = new SimCardMes(getApplicationContext());
		String IMSI = siminfo.getProvidersName();
		return IMSI;
//		return "460078152801068";
	}

	@SuppressLint("NewApi")
	public void initSocket() throws UnknownHostException, IOException {
//		socket = new Socket("192.168.0.105", 11111);
//		socket = new Socket("108.166.215.91", 11111);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()       
        .detectDiskReads()       
        .detectDiskWrites()       
        .detectNetwork()   // or .detectAll() for all detectable problems       
        .penaltyLog()       
        .build());       
StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()       
        .detectLeakedSqlLiteObjects()    
        .penaltyLog()       
        .penaltyDeath()       
        .build());
		socket = new Socket("125.65.110.148", 11111);
		PrintWriter out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(socket.getOutputStream(), "GBK")), true);
		Sign sign = new Sign();
		sign.setIMSI(getIMSI());
		sign.setIsBracelet(false);
		DefaultMessage message = new DefaultMessage(
				MessageType.MESSAGE_LIANJIE_SERVER,
				jsonUtil.createJsonString(sign));
		out.println(jsonUtil.createJsonString(message));
		thread = null;
	}

	private void initSharedPreferences() {

		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		if (!preferences.contains("lock_pwd")) {
			Editor editor = preferences.edit();
			// editor.putString("Type", "Value");
			editor.putString("TuBiaoType", "TiWen");
			editor.putString("lock_pwd", "NULL");
			editor.putString("sign", "No");
			editor.putString("isFirst", "Yes");
			
			editor.putString("openAlarm", "No");
			editor.putString("isRepeat", "No");
			editor.putString("alarmTime", "NULL");
			
			editor.putInt("schoolTimeMin", -1);
			editor.putInt("schoolTimeHou", -1);
			editor.putInt("needTimeMin", -1);
			editor.putInt("needTimeHou", -1);
			editor.putString("openHome", "No");
			editor.putInt("HomeLat", -1);
			editor.putInt("HomeLon", -1);
			
			// editor.putString("isFirst", "No");
			editor.commit();

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
			Toast.makeText(MyApplication.getInstance().getApplicationContext(),
					"BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
		}
	}

	public static MyApplication getInstance() {
		return mInstance;
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	public static class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(
						MyApplication.getInstance().getApplicationContext(),
						"您的网络出错啦！", Toast.LENGTH_LONG).show();
			}
			// ...
		}

		@Override
		public void onGetPermissionState(int iError) {
			// 非零值表示key验证未通过
			if (iError != 0) {
				Toast.makeText(
						MyApplication.getInstance().getApplicationContext(),
						"keybad", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	
}
