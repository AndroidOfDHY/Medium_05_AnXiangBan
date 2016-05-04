package com.anxiangban.activitysetmima;

import gson.gsonTool;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import com.anxiangban.R;
import com.anxiangban.activitygongnengjianjie.JianJieActivity;
import com.anxiangban.activityhomewhere.HomeWhereActivity;
import com.anxiangban.activityqrcode.QRcodeActivity;
import com.anxiangban.activityshouye.ShouYeActivity;
import com.anxiangban.activitytabhost.TabHostActivity;
import com.anxiangban.myApplication.MyApplication;
import com.anxiangban.myView.LockPatternUtils;
import com.anxiangban.myView.LockPatternView;
import com.anxiangban.myView.LockPatternView.Cell;
import com.anxiangban.myView.LockPatternView.DisplayMode;
import com.anxiangban.myView.LockPatternView.OnPatternListener;
import com.anxiangban.thread.ClienIn;

import Message.DefaultMessage;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

public class SetMiMaActivity extends Activity {
	private LockPatternView lockPatternView;
	private LockPatternUtils lockPatternUtils;
	private TextView tixin;
	private TextView title;
	private SharedPreferences preferences;
	private int Flag;
	private MyApplication app;
	private Handler socketHandler;
	
	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mima);
		Flag = 0;
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		title = (TextView) findViewById(R.id.one);
		tixin = (TextView) findViewById(R.id.two);
		title.setText("������������");
		if (preferences.getString("lock_pwd", "").equals("NULL"))
			tixin.setText("��һ��ʹ�������ý�������");
		else
			tixin.setText("�������������");
		lockPatternView = (LockPatternView) findViewById(R.id.lpv_lock);
		lockPatternUtils = new LockPatternUtils(this);// ����
		lockPatternView.setOnPatternListener(new myPatternListener());
		app = (MyApplication) getApplication();
		socketHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// ������͵���Ϣ�������߳�
				if (msg.what == 0x123) {
					
				}
			}
		};
		initClien();
	}
	protected void onRestart() {
		super.onRestart();
		System.out.println("onRestart");
	}
	class myPatternListener implements OnPatternListener{
		private String pString;

		public void onPatternStart() {}
		public void onPatternDetected(List<Cell> pattern) {
			int result = lockPatternUtils.checkPattern(pattern);
			if (result != 1) {
				if (result == 0) {
					lockPatternView.setDisplayMode(DisplayMode.Wrong);
					Toast.makeText(SetMiMaActivity.this, "�����������",Toast.LENGTH_SHORT).show();
					lockPatternView.clearPattern();
				} else {
					if (Flag == 0) {
						pString = lockPatternUtils.getPatternToString(pattern);
						Flag++;
//						Toast.makeText(SetMiMaActivity.this,"������һ�飬ȷ����������", Toast.LENGTH_SHORT).show();
						lockPatternView.clearPattern();
						tixin.setText("������һ�飬ȷ����������");
					} else if (Flag == 1) {
						if (pString.equals(lockPatternUtils.getPatternToString(pattern))) {
							lockPatternUtils.saveLockPattern(pattern);
//							Toast.makeText(SetMiMaActivity.this,"�����������óɹ�", Toast.LENGTH_SHORT).show();
//							lockPatternView.clearPattern();
							Intent intent = new Intent();
							if(preferences.getInt("HomeLat", -1) == -1){
								intent = new Intent (SetMiMaActivity.this,HomeWhereActivity.class);
							}else{
								intent = new Intent (SetMiMaActivity.this,TabHostActivity.class);
							}
							startActivity(intent);
							SetMiMaActivity.this.finish();
						} else {
							Toast.makeText(SetMiMaActivity.this,"�����������һ������Ĳ�ͬ������������",Toast.LENGTH_SHORT).show();
							lockPatternView.clearPattern();
							tixin.setText("��������������");
							Flag--;
						}
					}
				}
			} else {// ����
				Intent intent = new Intent(SetMiMaActivity.this,TabHostActivity.class);
				startActivity(intent);
				SetMiMaActivity.this.finish();
			}
		}
		public void onPatternCleared() {}
		public void onPatternCellAdded(List<Cell> pattern) {}
	}
	@SuppressLint("NewApi")
	private void initClien(){
		
		if(app.socket == null){
			try {
				app.initSocket();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
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