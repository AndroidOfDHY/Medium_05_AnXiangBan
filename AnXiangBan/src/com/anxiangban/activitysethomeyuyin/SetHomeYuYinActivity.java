package com.anxiangban.activitysethomeyuyin;

import gson.gsonTool;

import java.io.IOException;
import java.net.UnknownHostException;

import com.anxiangban.R;
import com.anxiangban.activityhomewhere.HomeWhereActivity;
import com.anxiangban.activitysetalarm.SetAlarmActivity;
import com.anxiangban.activitysetcenter.SetCenterActivity;
import com.anxiangban.myApplication.MyApplication;
import com.anxiangban.myView.SlipButton;
import com.anxiangban.myView.SlipButton.OnSwitchListener;
import com.anxiangban.thread.ClienIn;

import domain.Home;
import Message.DefaultMessage;
import Message.MessageType;
import Util.jsonUtil;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class SetHomeYuYinActivity extends Activity {
	private RelativeLayout setHomeWhere;
	private RelativeLayout setSchoolTime;
	private RelativeLayout SetNeedTime;
	private SharedPreferences preferences;
	private SlipButton openButton;
	private TextView School_time;
	private TextView Need_time;
	private MyApplication app;
	private Handler socketHandler;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sethome);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		app = (MyApplication)getApplication();
		initView();
		socketHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// 如果发送的消息来自子线程
				if (msg.what == 0x123) {
					DefaultMessage message = gsonTool.getT(msg.obj.toString(),
							DefaultMessage.class);
					System.out.println(msg.obj.toString());
					play(message);
				}
			}
		};
		initClien();
	}

	private void initView() {
		setHomeWhere = (RelativeLayout) findViewById(R.id.setwhere);
		setSchoolTime = (RelativeLayout) findViewById(R.id.setschoolTime);
		SetNeedTime = (RelativeLayout) findViewById(R.id.setneedtime);
		openButton = (SlipButton) findViewById(R.id.openbutton);
		School_time = (TextView)findViewById(R.id.schooltime);
		Need_time = (TextView) findViewById(R.id.needtime);
		if(preferences.getInt("needTimeMin", -1) != -1){
			if(preferences.getInt("needTimeMin", -1)<10 ){
				Need_time.setText(preferences.getInt("needTimeHou", -1)+":0"+preferences.getInt("needTimeMin", -1));
			}else{
				Need_time.setText(preferences.getInt("needTimeHou", -1)+":"+preferences.getInt("needTimeMin", -1));
			}
		}
		if(preferences.getInt("schoolTimeHou", -1) != -1){
			if(preferences.getInt("schoolTimeHou", -1) < 10 ){
				School_time.setText(preferences.getInt("schoolTimeHou", -1)+":0"+preferences.getInt("schoolTimeMin", -1));
			}else{
				School_time.setText(preferences.getInt("schoolTimeHou", -1)+":"+preferences.getInt("schoolTimeMin", -1));
			}
		}
		setSchoolTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int mHour = 16;
				int mMinute = 30;
				if(preferences.getInt("schoolTimeMin", -1) != -1){
					mHour = preferences.getInt("schoolTimeHou", -1);
					mMinute = preferences.getInt("schoolTimeMin", -1);
				}
				new TimePickerDialog(SetHomeYuYinActivity.this,
						new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								Editor editor = preferences.edit();
								editor.putInt("schoolTimeHou", hourOfDay);
								editor.putInt("schoolTimeMin", minute);
								editor.commit();
								if(minute<10){
									School_time.setText(hourOfDay+":0"+minute);
								}else{
									School_time.setText(hourOfDay+":"+minute);
								}
							}
						}, mHour, mMinute, true).show();
			}
		});
		SetNeedTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 定义获取时间
				int mHour = 0;
				int mMinute = 0;
				if(preferences.getInt("needTimeHou", -1) != -1){
					mHour = preferences.getInt("needTimeHou", -1);
					mMinute = preferences.getInt("needTimeMin", -1);
				}
				new TimePickerDialog(SetHomeYuYinActivity.this,
						new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								Editor editor = preferences.edit();
								editor.putInt("needTimeHou", hourOfDay);
								editor.putInt("needTimeMin", minute);
								editor.commit();
								if(minute<10){
									Need_time.setText(hourOfDay+":0"+minute);
								}else{
									Need_time.setText(hourOfDay+":"+minute);
								}
							}
						}, mHour, mMinute, true).show();
			}
		});
		if (!preferences.getString("openHome", "").equals("No")) {
			openButton.setSwitchState(true);
		}
		openButton.setOnSwitchListener(new OnSwitchListener() {
			@Override
			public void onSwitched(boolean isSwitchOn) {
				Editor editor = preferences.edit();
				if (isSwitchOn) {
					editor.putString("openHome", "Yes");
					sendHome();
				} else {
					editor.putString("openHome", "No");
					sendUnHome();
				}
				editor.commit();
			}
		});
		setHomeWhere.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SetHomeYuYinActivity.this,
						HomeWhereActivity.class);
				startActivity(intent);
			}
		});
	}
	
	protected void onRestart() {
		super.onRestart();
	}
	
	private void sendHome(){
		Home home = new Home();
		home.setLat(preferences.getInt("HomeLat", -1));
		home.setLon(preferences.getInt("HomeLon", -1));
		DefaultMessage message = new DefaultMessage(MessageType.MESSAGE_SET_HOME,jsonUtil.createJsonString(home));
		app.thread.sendMessage(message);
	}
	
	private void sendUnHome(){
		DefaultMessage message = new DefaultMessage(MessageType.MESSAGE_UN_HOME,"");
//		out.println(jsonUtil.createJsonString(message));
		app.thread.sendMessage(message);
	}
	private void play(DefaultMessage message){
		if(message.getFlag().equals(MessageType.MESSAGE_RETURN_OK)&&message.getContext().equals("OK")){	
			Toast.makeText(SetHomeYuYinActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
		}else if(message.getFlag().equals(MessageType.MESSAGE_RETURN_OK)&&message.getContext().equals("UN")){
			Toast.makeText(SetHomeYuYinActivity.this, "取消成功", Toast.LENGTH_SHORT).show();
		}
	}
	private void initClien(){
		if(app.socket == null){
			try {
				app.initSocket();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (app.thread != null) {
			app.thread.handler = socketHandler;
		} else {
			try {
				ClienIn thread = new ClienIn(app.socket,socketHandler,app);
				thread.start();
				app.thread = thread;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
