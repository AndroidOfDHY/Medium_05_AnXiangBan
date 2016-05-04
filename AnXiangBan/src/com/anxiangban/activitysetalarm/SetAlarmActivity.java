package com.anxiangban.activitysetalarm;

import gson.gsonTool;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Calendar;
import Message.DefaultMessage;
import Message.MessageType;
import Util.FileUtil;
import Util.MediaUtil;
import Util.jsonUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.anxiangban.R;
import com.anxiangban.myApplication.MyApplication;
import com.anxiangban.myView.SlipButton;
import com.anxiangban.myView.SlipButton.OnSwitchListener;
import com.anxiangban.thread.ClienIn;
import domain.Alarm;
import domain.UploadFile;

public class SetAlarmActivity extends Activity implements OnTouchListener {
	private SlipButton openButton;
	private SlipButton repeatButton;
	private RelativeLayout setTime;
	private RelativeLayout setYuyin;
	private RelativeLayout showYuyin;
	private TextView tv_time;
	private Alarm alarm;
	private MediaUtil mediaUtil;
	private SharedPreferences preferences;
	private Boolean flag;
	private Handler socketHandler;
	private MyApplication app;
//	PrintWriter out;
	
	
	@SuppressLint("HandlerLeak")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setalarm);
		app = ( MyApplication ) getApplication();
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
		alarm = new Alarm();
		alarm.setCalendar(null);
		alarm.setIsRepeat(false);
		flag = false;
		mediaUtil = new MediaUtil(SetAlarmActivity.this);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		initView();
		initClien();
	}

	private void initView() {
		repeatButton = (SlipButton) findViewById(R.id.repeatButton);
		openButton = (SlipButton) findViewById(R.id.openbutton);
		setTime = (RelativeLayout) findViewById(R.id.setTime);
		setYuyin = (RelativeLayout) findViewById(R.id.setYuyin);
		showYuyin = (RelativeLayout) findViewById(R.id.showYuyin);
		tv_time = (TextView) findViewById(R.id.time);
		if (!preferences.getString("alarmTime", "").equals("NULL")) {
			tv_time.setText(preferences.getString("alarmTime", ""));
		}
		if (!preferences.getString("openAlarm", "").equals("No")) {
			openButton.setSwitchState(true);
		}
		openButton.setOnSwitchListener(new OnSwitchListener() {
			@Override
			public void onSwitched(boolean isSwitchOn) {
				Editor editor = preferences.edit();
				if (isSwitchOn) {
					if(!flag){
						openButton.setSwitchState(false);
						Toast.makeText(SetAlarmActivity.this, "请先输入语音", Toast.LENGTH_SHORT).show();
					}else if(alarm.getCalendar() == null){
						openButton.setSwitchState(false);
						Toast.makeText(SetAlarmActivity.this, "请先设置时间", Toast.LENGTH_SHORT).show();
					}else{
						UploadFile file = FileUtil.getUploadFile();
						alarm.setFile(file);
						sendAlarm();
						editor.putString("openAlarm", "Yes");
					}
				} else {
					sendUnAlarm();
					editor.putString("openAlarm", "No");
					System.out.println("close");
				}
				editor.commit();
			}
		});
		if (!preferences.getString("isRepeat", "").equals("No")) {
			repeatButton.setSwitchState(true);
			alarm.setIsRepeat(true);
		}
		repeatButton.setOnSwitchListener(new OnSwitchListener() {
			@Override
			public void onSwitched(boolean CheckState) {
				Editor editor = preferences.edit();
				if (CheckState) {
					alarm.setIsRepeat(true);
					editor.putString("isRepeat", "Yes");
				} else {
					alarm.setIsRepeat(false);
					editor.putString("isRepeat", "No");
				}
				editor.commit();
			}
		});
		setTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());
				// 定义获取时间
				int mHour = calendar.get(Calendar.HOUR_OF_DAY);
				int mMinute = calendar.get(Calendar.MINUTE);
				new TimePickerDialog(SetAlarmActivity.this,
						new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								Calendar calendarTemp = Calendar.getInstance();
								calendarTemp.setTimeInMillis(System
										.currentTimeMillis());
								calendarTemp.set(Calendar.HOUR_OF_DAY,
										hourOfDay);
								calendarTemp.set(Calendar.MINUTE, minute);
								calendarTemp.set(Calendar.SECOND, 0);
								calendarTemp.set(Calendar.MILLISECOND, 0);
								alarm.setCalendar(calendarTemp);
								Editor editor = preferences.edit();
								if(minute<10){
									editor.putInt("schoolTimeMin", minute);
								}else{
									tv_time.setText(hourOfDay+":"+minute);

									editor.putString("isRepeat", "No");
								}
								editor.commit();
							}
						}, mHour, mMinute, true).show();
			}
		});
		showYuyin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (FileUtil.isExists(mediaUtil.getAlarm())) {
					mediaUtil.playMedia();
				} else {
					Toast.makeText(SetAlarmActivity.this, "没有语音文件",
							Toast.LENGTH_LONG).show();
				}
			}
		});
		setYuyin.setOnTouchListener(SetAlarmActivity.this);
	}

	protected void onRestart() {
		super.onRestart();
	}

	public boolean onTouch(View v, MotionEvent event) {
		int id = event.getAction();
		switch (id) {
		case MotionEvent.ACTION_DOWN:
			mediaUtil.start();
			mediaUtil.showVoiceDialog();
			break;
		case MotionEvent.ACTION_UP:
			mediaUtil.hideVoiceDialog();
			mediaUtil.stop();
			flag = true;
			break;
		}
		return false;
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
	private void play(DefaultMessage message){
		if(message.getFlag().equals(MessageType.MESSAGE_RETURN_OK)&&message.getContext().equals("OK")){	
			Toast.makeText(SetAlarmActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
		}else if(message.getFlag().equals(MessageType.MESSAGE_RETURN_OK)&&message.getContext().equals("UN")){
			Toast.makeText(SetAlarmActivity.this, "取消成功", Toast.LENGTH_SHORT).show();
		}
	}
	private void sendAlarm(){
		DefaultMessage message = new DefaultMessage(MessageType.MESSAGE_SET_ALARM_CLOCK,jsonUtil.createJsonString(alarm));
		app.thread.sendMessage(message);
	}
	private void sendUnAlarm(){
		DefaultMessage message = new DefaultMessage(MessageType.MESSAGE_UN_ALARM_CLOCK,"");
//		out.println(jsonUtil.createJsonString(message));
		app.thread.sendMessage(message);
	}
}
