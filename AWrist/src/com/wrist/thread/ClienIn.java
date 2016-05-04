package com.wrist.thread;

import gson.gsonTool;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import com.wrist.DBHelper.MytabCursor;
import com.wrist.DBHelper.MytabOperate;
import com.wrist.DBHelper.domain.SafeDangerData;
import com.wrist.myapplication.MyApplication;
import domain.Alarm;
import domain.Home;
import domain.Sign;
import domain.UploadFile;
import Message.DefaultMessage;
import Message.MessageType;
import Util.MediaUtil;
import Util.jsonUtil;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.preference.PreferenceManager;

public class ClienIn extends Thread {

	private Socket socket;
	public Handler handler;
	private BufferedReader bufferedReader;
	private PrintWriter out = null;
	private MyApplication app;

	@SuppressLint("HandlerLeak")
	public ClienIn(Socket socket, MyApplication app) throws IOException {
		this.socket = socket;
		this.app = app;
		bufferedReader = new BufferedReader(new InputStreamReader(
				socket.getInputStream(), "GBK"));
		out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream(), "GBK")), true);
		Sign sign = new Sign();
		sign.setIsBracelet(true);
		sign.setBraceletId(app.getIMSI());
		DefaultMessage message = new DefaultMessage(
				MessageType.MESSAGE_SIGN_SERVER,
				jsonUtil.createJsonString(sign));
		sendMessage(message);
	}

	@Override
	public void run() {
		String jsonString = null;
		while ((jsonString = readFromClient()) != null) {
			System.out.println(jsonString);
			DefaultMessage message = gsonTool.getT(jsonString,
					DefaultMessage.class);
			if (message.getFlag().equals(MessageType.MESSAGE_SET_ALARM_CLOCK)) {
				setAlarm(message);
			} else if (message.getFlag().equals(MessageType.MESSAGE_SET_DANGER)) {
				setSafeDanger(message);
			} else if (message.getFlag().equals(MessageType.MESSAGE_SET_SAFE)) {
				setSafeDanger(message);
			} else if (message.getFlag().equals(MessageType.MESSAGE_UN_ALARM_CLOCK)) {
				unAlarm(message);
			} else if (message.getFlag().equals(MessageType.MESSAGE_SAID)) {
				said(message);
			}else if (message.getFlag().equals(MessageType.MESSAGE_SET_HOME)) {
				setHome(message);
			}else if (message.getFlag().equals(MessageType.MESSAGE_UN_HOME)) {
				UnHome(message);
			}
		}
	}

	public void sendMessage(DefaultMessage message) {
		System.out.println(message.getContext());
		String context = jsonUtil.createJsonString(message);
		out.println(context);
	}

	private String readFromClient() {
		String jsString = null;
		try {
			jsString = bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsString;
	}

	private void said(DefaultMessage message) {
		MediaUtil mediaUtil = new MediaUtil();
		UploadFile file = gsonTool.getT(message.getContext(), UploadFile.class);
		mediaUtil.saveFile(file);
		mediaUtil.playMedia(file.getName());
	}
	
	private void setHome(DefaultMessage message) {
		Home home = gsonTool.getT(message.getContext(), Home.class);
		app.homeLat = home.getLat();
		app.homeLon = home.getLon();
		app.homeLat = app.homeLat / 1E6;
		app.homeLon = app.homeLon / 1E6;
		System.out.println(app.homeLat);
		app.openHome = true;
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(app);
		Editor editor = preferences.edit();
		editor.putString("openHome", "Yes");
		editor.putInt("HomeLat", (int) (app.homeLat * 1E6));
		editor.putInt("HomeLon", (int) (app.homeLon * 1E6));
		editor.commit();
		DefaultMessage senMessage = new DefaultMessage();
		senMessage.setFlag(MessageType.MESSAGE_RETURN_OK);
		senMessage.setContext("OK");
		sendMessage(senMessage);
	}
	
	private void UnHome(DefaultMessage message) {
		app.openHome = false;
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(app);
		Editor editor = preferences.edit();
		editor.putString("openHome", "No");
		editor.commit();
		DefaultMessage senMessage = new DefaultMessage();
		senMessage.setFlag(MessageType.MESSAGE_RETURN_OK);
		senMessage.setContext("UN");
		sendMessage(senMessage);
	}
	
	private void setAlarm(DefaultMessage message) {
		MediaUtil mediaUtil = new MediaUtil();
		DefaultMessage senMessage = new DefaultMessage();
		Alarm alarm = gsonTool.getT(message.getContext(), Alarm.class);
		if (mediaUtil.saveAlarm(alarm.getFile())) {
			Intent intent = new Intent(app,
					com.wrist.broadcastreceiver.CallAlarm.class);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(app, 0,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager am;
			am = (AlarmManager) app.getSystemService(Context.ALARM_SERVICE);
			am.setRepeating(AlarmManager.RTC_WAKEUP, alarm.getCalendar()
					.getTimeInMillis(), (24 * 60 * 60 * 1000), pendingIntent);
			senMessage.setFlag(MessageType.MESSAGE_RETURN_OK);
			senMessage.setContext("OK");
			sendMessage(senMessage);
		} else {
			System.out.println("false");
		}
	}

	private void unAlarm(DefaultMessage message) {
		DefaultMessage senMessage = new DefaultMessage();
		Intent intent = new  Intent (app,com.wrist.broadcastreceiver.CallAlarm.class);
		PendingIntent sender = PendingIntent.getBroadcast(app, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager)app.getSystemService(Context.ALARM_SERVICE);
		am.cancel(sender);
		senMessage.setFlag(MessageType.MESSAGE_RETURN_OK);
		senMessage.setContext("UN");
		sendMessage(senMessage);
	}

	private void setSafeDanger(DefaultMessage message) {
		SafeDangerData safeData = gsonTool.getT(message.getContext(), SafeDangerData.class);
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
		app.setSafeDanger(safeData);
		DefaultMessage senMessage = new DefaultMessage();
		senMessage .setFlag(MessageType.MESSAGE_RETURN_OK);
		senMessage.setContext("OK");
		sendMessage(senMessage);
	}
}
