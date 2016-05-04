package com.anxiangban.thread;

import gson.gsonTool;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;

import com.anxiangban.DBHelper.InsertHelp;
import com.anxiangban.DBHelper.domain.HealthData;
import com.anxiangban.DBHelper.domain.PositionData;
import com.anxiangban.myApplication.MyApplication;

import domain.All;
import Message.DefaultMessage;
import Message.MessageType;
import Util.MediaUtil;
import Util.jsonUtil;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

public class ClienIn extends Thread {

	private Socket socket;
	public Handler handler;
	private BufferedReader bufferedReader;
	private PrintWriter out = null;
	private MyApplication app;
	InsertHelp insertHelp;

	@SuppressLint("HandlerLeak")
	public ClienIn(Socket socket, Handler handler, MyApplication app)
			throws IOException {
		this.handler = handler;
		this.socket = socket;
		this.app = app;
		insertHelp = new InsertHelp(app);
		bufferedReader = new BufferedReader(new InputStreamReader(
				socket.getInputStream(), "GBK"));
		out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream(), "GBK")), true);
	}

	@Override
	public void run() {
		String jsonString = null;
		while ((jsonString = readFromClient()) != null) {
			System.out.println(jsonString);
			DefaultMessage message = gsonTool.getT(jsonString,
					DefaultMessage.class);
			if (message.getFlag().equals(MessageType.MESSAGE_RETURN_HEALTH)) {
				addHealth(message);
			}else if(message.getFlag().equals(MessageType.MESSAGE_RETURN_GPS)){
				addPosition(message);
			}else if(message.getFlag().equals(MessageType.MESSAGE_RETURN_ALL)){
				addAll(message);
				DefaultMessage message2 = new DefaultMessage(MessageType.MESSAGE_GET_OK, "");
				sendMessage(message2);
			}else if(message.getFlag().equals(MessageType.MESSAGE_WARN)){
				String name = message.getContext();
				showYuYin(name);
			}else if(message.getFlag().equals(MessageType.MESSAGE_RETURN_HOME)){
				daoHome();
			}else{
				sendhandlerMessage(jsonString);
			}
		}
	}
	public void sendMessage(DefaultMessage message){
		String context = jsonUtil.createJsonString(message);
		System.out.println(context);
		out.println(context);
	}

	private void sendhandlerMessage(String content) {
		Message msg = new Message();
		msg.what = 0x123;
		msg.obj = content;
		handler.sendMessage(msg);
	}

	private String readFromClient() {
		String jsString = null;
		try {
			jsString = bufferedReader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsString;
	}
	private void addHealth(DefaultMessage message){
		HealthData healthData = gsonTool.getT(message.getContext(), HealthData.class);
		insertHelp.HealthInsert(healthData);
	}
	private void addPosition(DefaultMessage message){
		PositionData positionData = gsonTool.getT(message.getContext(), PositionData.class);
		insertHelp.PositionInsert(positionData);
	}
	private void addAll(DefaultMessage message){
		All all = gsonTool.getT(message.getContext(), All.class);
		if(all != null){
			insertHelp.AllInsert(all);
		}else{
			System.out.println(message.getContext());
		}
	}
	private void daoHome(){
		SharedPreferences preferences;
		preferences = PreferenceManager.getDefaultSharedPreferences(app);
		int schoolMin = preferences.getInt("schoolTimeMin", -1);
		int schoolHou = preferences.getInt("schoolTimeHou", -1);
		int needMin = preferences.getInt("needTimeMin", -1);
		int needHou = preferences.getInt("needTimeHou", -1);
		int min = (schoolMin + needMin) % 60;
		int hou = (needMin + schoolMin) % 60 + needHou + schoolHou;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int mHour = calendar.get(Calendar.HOUR_OF_DAY);
		int mMinute = calendar.get(Calendar.MINUTE);
		Boolean flagtime = false; 
		if( mHour > schoolHou && mHour < hou){
			flagtime = true;
		}else if(mHour == schoolHou && mMinute > schoolMin){
			flagtime = true;
		}else if(mHour == hou && mMinute < min){
			flagtime = true;
		}
		if(flagtime ){
			MediaUtil mediaUtil = new MediaUtil(app);
			AssetManager am = app.getAssets();
//			mediaUtil.openSound("HOME.amr", am);
			System.out.println("已经放学回家了");
		}
		
	}
	private void showYuYin(String name){
		MediaUtil mediaUtil = new MediaUtil(app);
		AssetManager am = app.getAssets();
		if(name.equals("ELECTRIC")){
			mediaUtil.openSound("ELECTRIC.amr", am);
			System.out.println("ELECTRIC");
		}else if(name.equals("DANGER")){
			mediaUtil.openSound("DANGER.amr", am);
			System.out.println("DANGER");
		}else if(name.equals("EARLY")){
			//安全区
			mediaUtil.openSound("EARLYC.amr", am);
			System.out.println("EARLY");
		}else if(name.equals("HEAT")){
			mediaUtil.openSound("HEAT.amr", am);
			System.out.println("HEAT");
		}else if(name.equals("SHUTDOWN")){
			//关机
//			mediaUtil.openSound("SHUTDOWN.amr", am);
			System.out.println("SHUTDOWN");
		}
	}
}
