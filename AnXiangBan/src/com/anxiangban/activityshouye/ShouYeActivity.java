package com.anxiangban.activityshouye;

import com.anxiangban.activitygongnengjianjie.JianJieActivity;
import com.anxiangban.activityhomewhere.HomeWhereActivity;
import com.anxiangban.activityqrcode.QRcodeActivity;
import com.anxiangban.activitysetmima.SetMiMaActivity;
import com.anxiangban.activitytabhost.TabHostActivity;
import com.anxiangban.R;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
/**
 * 打开app时显示
 * 显示5秒后跳转到下一界面
 */
public class ShouYeActivity extends Activity {
	private Intent intent;
	public SharedPreferences preferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shouye);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		if(preferences.getString("isFirst", "").equals("Yes")){
			intent = new Intent (ShouYeActivity.this,JianJieActivity.class);
		}else if (preferences.getString("sign", "").equals("No")) {
			intent = new Intent (ShouYeActivity.this,QRcodeActivity.class);
		}else if(preferences.getString("lock_pwd", "").equals("NULL")){
			intent = new Intent (ShouYeActivity.this,SetMiMaActivity.class);
		}else if(preferences.getInt("HomeLat", -1) == -1){
			intent = new Intent (ShouYeActivity.this,HomeWhereActivity.class);
		}else{
			intent = new Intent (ShouYeActivity.this,SetMiMaActivity.class);
		}
//		intent = new Intent (ShouYeActivity.this,TabHostActivity.class);
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run(){	
				startActivity(intent);			
				ShouYeActivity.this.finish();
			}
		}, 3000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	protected void onRestart() {
		super.onRestart();
	}
	protected void onResume() {
		super.onResume();
	}
	protected void onPause() {
		super.onPause();
	}
	protected void onStop() {
		super.onStop();
	}
	protected void onDestroy() {
		super.onDestroy();
	}
}
